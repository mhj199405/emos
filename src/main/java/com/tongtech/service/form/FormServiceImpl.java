package com.tongtech.service.form;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_department.DbSysDepartment;
import com.tongtech.auth.data.db_sys_department.DbSysDepartmentRepository;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_relation_ru.SysRelationRu;
import com.tongtech.auth.data.db_sys_relation_ru.SysRelationRuRepository;
import com.tongtech.auth.data.db_sys_role.DbSysRole;
import com.tongtech.auth.data.db_sys_role.DbSysRoleRepository;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import com.tongtech.common.ProcessMethodUrlProperties;
import com.tongtech.common.StorageException;
import com.tongtech.common.StorageProperties;
import com.tongtech.common.enums.FormScope;
import com.tongtech.common.utils.StringUtils;
import com.tongtech.common.BusinessException;
import com.tongtech.common.utils.SysUtils;
import com.tongtech.common.vo.*;
import com.tongtech.dao.entity.*;
import com.tongtech.dao.repository.*;
import com.tongtech.service.storage.StorageService;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class FormServiceImpl  implements FormService{

    private final Path rootLocation;
    private final Path locationDownload;
    @Autowired
    public FormServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.locationDownload = Paths.get(properties.getLocationDownload());
    }
    @Autowired
    private FormRepository formRepo;

    @Autowired
    private FormFormItemRepository formFormItemRepo;

    @Autowired
    private FormItemRepository formItemRepo;

    @Autowired
    private FormItemFieldDataRepository formItemFieldDataRepository;

    @Autowired
    private DepartmentFormRepository departmentFormRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private TemplateDataItemRepository templateDataItemRepository;

    @Autowired
    private TemplateDataItemDataRepository templateDataItemDataRepository;

    @Autowired
    private CommonProvinceRepository commonProvinceRepository;

    @Autowired
    private CommonRegionRepository commonRegionRepository;

    @Autowired
    private CommonCityRepository commonCityRepository;

    @Autowired
    private SysRelationRuRepository sysRelationRuRepository;

    private ObjectMapper mapper = new ObjectMapper();;

    /**
     * 获取所有的表单模板
     * @return
     */
    @Override
    public List<Form> findFormTemplates() {
        List<Form> result=null;
        result = formRepo.findFormTemplates();
        if (result != null) {
            for(Form form:result) {
                this.buildAllFormItem(form);
            }
        }
        return result;
    }


    @Override
    public List<FormItem> getTemplateItems(Long id) {
        List<FormItem> result = new ArrayList<>();
        List<FormItem> itemList = null;
        List<FormFormItem> ffiList = null;
        if (id == -1) {
            ffiList = formFormItemRepo.findAllTemplateFormFormItem();
        } else {
            ffiList = formFormItemRepo.findAllTemplateFormFormItem(id);
        }
        if (ffiList != null) {
            for(FormFormItem formFormItem:ffiList) {
                itemList = formItemRepo.findByLayerId(formFormItem.getId());
                if (itemList != null) {
                    for(FormItem i:itemList) {
                        result.add(i);
                    }
                }
            }
        }
        List<FormItemFieldData> fieldDataList = null;
        if (!result.isEmpty()) {
            for (FormItem r:result) {
                fieldDataList = formItemFieldDataRepository.findByFieldId(r.getId());
                if (fieldDataList != null) {
                    for (FormItemFieldData data:fieldDataList) {
                        r.addFormItemData(data);
                    }
                }
            }

        }
        return result;
    }

    /**
     * 查找一个表单
     * @param formId
     * @return
     */
    @Override
    public Form findOneForm(Long formId) {
        Form form  = formRepo.findById(formId).orElse(null);
        if (form == null ) {
            return null;
        }
        return this.buildAllFormItem(form);
    }

    @Override
    @Transactional
    public synchronized FormFormItem createFormFormItem(Long formId, FormFormItem ffi) {
        FormFormItem rtn = null;
        Form form = null;

        // 获取关联的 FormFormItem
        Long tmpId = -1L;
        if (formId != -1L) {
            form = formRepo.findById(formId).orElse(null);
            if (form == null) {
                return null;
            }
            tmpId = form.getId();
        } else {
            tmpId = -1L;
        }
        FormFormItem newObj = new FormFormItem();
        BeanUtils.copyProperties(ffi,newObj);
        ffi.setFormId(tmpId);
        ffi.setId(null);
        ffi.setCreateTime(new Date());
        newObj = formFormItemRepo.save(ffi);

        List<FormItem> formItemList = ffi.getFormItemList();
        newObj.setFormItemList(formItemList);
        if (formItemList != null) {
            for (FormItem formItem: formItemList) {
                FormItem item = new FormItem();
                BeanUtils.copyProperties(formItem, item);
                item.setLayerId(ffi.getId());
                item = formItemRepo.save(item);
                formItem.setId(item.getId());
                List<FormItemFieldData> dataList = formItem.getDataSourceList();
                if (dataList != null) {
                    for(FormItemFieldData data: dataList) {
                        formItemFieldDataRepository.save(data);
                    }
                }
            }
        }
        return newObj;
    }

    @Override
    @Transactional
    public synchronized FormFormItem updateFormFormItem(FormFormItem ffi) {
        FormFormItem srcObj = null;
        if (ffi.getId() != null) {
            srcObj = formFormItemRepo.findById(ffi.getId()).orElse(null);
        }
        if (srcObj != null) {
            BeanUtils.copyProperties(ffi,srcObj);
            srcObj.setUpdateTime(new Date());
            srcObj = formFormItemRepo.save(srcObj);
            List<FormItem> formItemList = ffi.getFormItemList();
            srcObj.setFormItemList(formItemList);

            formItemRepo.deleteByFormFormItem(ffi.getId());
            if (formItemList != null) {
                for (FormItem formItem: formItemList) {
//					FormItem item = formItemRepo.findById(formItem.getId()).orElse(null);
//					if (item == null) { item = new FormItem(); }
                    FormItem item = new FormItem();
                    BeanUtils.copyProperties(formItem, item);
                    if (item.getId() == null) continue;
                    item.setLayerId(ffi.getId());
                    item.setCreateTime(new Date());
                    item = formItemRepo.save(item);
                    List<FormItemFieldData> dataList = formItem.getDataSourceList();
                    if (dataList != null) {
                        formItemFieldDataRepository.deleteByFieldId(formItem.getId());
                        for(FormItemFieldData data: dataList) {
                            data = formItemFieldDataRepository.save(data);
                        }
                    }
                }
            }
        } else {
//            srcObj = ffi;
            return null;
        }
        return srcObj;
    }

    @Override
    @Transactional
    public synchronized void deleteFormFormItem(Long id) {
        List<FormItem> formItemList = formItemRepo.findByLayerId(id);
        if (formItemList != null) {
            for (FormItem formItem: formItemList) {
                formItemFieldDataRepository.deleteByFieldId(formItem.getId());
                formItemRepo.delete(formItem);
            }
        }
        formFormItemRepo.deleteById(id);
    }

    @Override
    @Transactional
    public synchronized Form createForm(Form form) {
        if (form == null)
            return null;
        // 查找最大版本
        Integer version = formRepo.findMaxVersion(form.getName());
        if (version == null) {
            form.setVersions(1);
        } else {
            throw new BusinessException("存在相同名称的表单:" + form.getName() );
        }
        form.setId(null);
        form.setName(StringUtils.trim(form.getName()));
        form = formRepo.saveAndFlush(form);

        // 创建Form与User的关联关系
        // IsTemplate: 是否模板: 0: 否；   1:是
        // Status:     状态    ：0: 禁用，1：启用
        if (form.getIsTemplate() != 1 && (!form.getStatus().equals("0"))) {
//            Long userId = UserPrincipal.getSessionUser().getUser().getId();//从Session获取user
//            UserForm uf = new UserForm();
//            uf.setFormId(form.getId());
//            uf.setUserId(userId);
//            userFormRepo.saveAndFlush(uf);
            //将部门和创建的表单相关联
//            DepartmentForm departmentForm = new DepartmentForm();
//            departmentForm.setFormId(Integer.parseInt(form.getId().toString()));
            //整合登录模块后，再查询相应的数据进行插入
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal == null) {
                return null;
            }
            CustomUser user = null;
            if (principal instanceof CustomUser) {
                user = (CustomUser) principal;
            }
            if (user == null) {
                return null;
            }
            DbSysUser voUserMenu = user.getVoUserMenu();
//            departmentForm.setDepartmentId(voUserMenu.getDeptId());
//            departmentForm.setUserId(voUserMenu.getId());
            //根据角色创建多个与表单关联的记录
            List<SysRelationRu> sysRelationRus = sysRelationRuRepository.findAllByUserId(voUserMenu.getId());
            List<DepartmentForm> departmentFormsList=new ArrayList<>();
            if (sysRelationRus != null && sysRelationRus.size() > 0){
                for (SysRelationRu relationRus : sysRelationRus) {
                    DepartmentForm departmentForms = new DepartmentForm();
                    departmentForms.setFormId(Integer.parseInt(form.getId().toString()));
                    departmentForms.setUserId(voUserMenu.getId());
                    departmentForms.setRoleId(relationRus.getRoleId());
                    departmentFormsList.add(departmentForms);
                }
            }
            if (departmentFormsList.size() > 0){
                departmentFormRepository.saveAll(departmentFormsList);
            }
        }
        return form;
    }

    @Override
    @Transactional
    public synchronized Form updateForm(Form form) {
        Form dbObj = formRepo.findById(form.getId()).orElse(null);
        if (dbObj != null) {
            Integer version = dbObj.getVersions() ;
            if (version == null) {
                version = 1;
            }
            Integer version1 = form.getVersions();
            if (version1 != null && version1 > version) {
                version = version1;
            } else {
                version += 1;
            }
            BeanUtils.copyProperties(form,dbObj);
            dbObj.setVersions(version);
            dbObj.setUpdateTime(new Date());
            dbObj = formRepo.save(dbObj);
        } else {
            throw new BusinessException("修改表单不存在:" + form.getId() );
        }
        return dbObj;
    }

    /**
     * 删除表单
     * @param formId
     */
    @Override
    @Transactional
    public void delForm(Long formId) {
        //首先判断Form是否被病种、课题引用，引用时不能删除
//        Long count = diseaseRepo.countByForm(formId);
//        if(count != null && count > 0) {
//            throw new BusinessException("此表单已被病种引用，不能删除");
//        }
//        count = topicRepo.countByForm(formId);
//        if(count != null && count > 0) {
//            throw new BusinessException("此表单已被课题引用，不能删除");
//        }
        //这里以后需要判断一下，表单是否被流程实例所引用，查一下表单实例表，是否被应用

        // 逻辑删除，感觉这里可以具体删除，因为属于某一个用户名下的，如果没有引用删除掉是没有影响的，逻辑删除，以后也是没有用的，除非到时候统一清理。
        Form form = formRepo.findById(formId).orElse(null);//删除表单
        if (form != null) {
            form.setStatus("0");
            form.setUpdateTime(new Date());
            formRepo.save(form);
        }
//        userFormRepo.deleteByFormId(formId);//删除表单与用户之间的关系
        //删除表单和部门之间的关系
        departmentFormRepository.deleteAllByFormId(Integer.parseInt(formId.toString()));
    }

    /**
     *获取节点模板
     * @return
     */
    @Override
    public List<FormFormItem> findSectionTemplates() {
        List<FormFormItem> result=null;
        result = formFormItemRepo.findAllTemplateSection();
        if (result != null) {
            for(FormFormItem ffi:result) {
                this.buildFormFormItem(ffi);
            }
        }
        return result;
    }

    /**
     * 拷贝表单模板
     * @param formId
     * @param name
     * @param isTemplate
     * @param description
     * @return
     */
    @Override
    @Transactional
    public synchronized Form copyFormFromTemplate(Long formId, String name, Integer isTemplate, String description) {
        Form newForm = new Form();
        Map<Long,Form> formMap = new HashMap();

        // 表单
        Form srcObj = formRepo.findById(formId).orElse(null);
        if (srcObj == null) {
            return null;
        }
        BeanUtils.copyProperties(srcObj,newForm, SysUtils.getNullPropertyNames(srcObj));
        newForm.setId(null);
        newForm.setName(name);
        newForm.setIsTemplate(isTemplate);
        newForm.setDescription(description);
        newForm.setCreateTime(new Date());
        newForm.setUpdateTime(null);
        if (isTemplate == 1) {
            newForm.setScope(FormScope.Sys);
        } else {
            newForm.setScope(FormScope.User);
        }
        newForm = formRepo.save(newForm);
        formMap.clear();
        formMap.put(formId,newForm);

        // 层级关系
        Map<Long,FormFormItem> ffiMap = new HashMap<>();
        List<FormFormItem> ffiNewList = new ArrayList<>();
        List<FormFormItem> ffiList = formFormItemRepo.findByFormId(formId);
        if (ffiList == null) {
            return newForm;
        }
        FormFormItem ffiNew;
        for (FormFormItem ffi:ffiList ) {
            ffiNew = new FormFormItem();
            BeanUtils.copyProperties(ffi,ffiNew, SysUtils.getNullPropertyNames(ffi));
            ffiNew.setId(null);
            ffiNew.setFormId(newForm.getId());
            ffiNew.setCreateTime(new Date());
            ffiNew.setUpdateTime(null);
            ffiNew = formFormItemRepo.save(ffiNew);
            ffiMap.put(ffi.getId(),ffiNew);
            ffiNewList.add(ffiNew);
        }
        for (FormFormItem ffi:ffiNewList ) {
            if (ffi.getParentId() != null && ffi.getParentId() > 0) {
                ffiNew = ffiMap.get(ffi.getParentId());
                if (ffiNew != null) {
                    ffi.setParentId(ffiNew.getId());
                } else {
                    ffi.setParentId(null);
                }
            }
        }
        formFormItemRepo.saveAll(ffiNewList);
        newForm.setFormFormItemList(ffiNewList);

        // 字段
        List<FormItem> formItemList;
        List<FormItemFieldData> itemFieldDataList;
        List<FormItem> formItemNewList  = new ArrayList<>();
        List<FormItemFieldData> itemFieldDataNewList = new ArrayList<>();
        Map<String, FormItem> formItemMap = new HashMap();
        Map<String, FormItemFieldData> formItemFieldDataMap = new HashMap();
        FormItem itemNew;
        HashMap<String,FormItem> tempFormItemMap=new HashMap<>();
        HashMap<String,FormItem> tempFormItemMapForValue=new HashMap<>();
        // 层级
        for (FormFormItem ffi:ffiList ) {
            if (ffi.getIsList() == 1) {
                continue;
            }
            // 字段
            formItemList = formItemRepo.findByLayerId(ffi.getId());
            if (formItemList == null) { continue;}
            for(FormItem item: formItemList) {
                itemNew = new FormItem();
                BeanUtils.copyProperties(item, itemNew, SysUtils.getNullPropertyNames(item));
                itemNew.setId(UUID.randomUUID().toString());
                // 层级ID
                itemNew.setLayerId(ffiMap.get(ffi.getId()).getId());
                // 父控件触发ID 父控件触发值
                if (itemNew.getParentCtlid() != null && (!itemNew.getParentCtlid().isEmpty()) &&
                        (!itemNew.getParentCtlid().equals("0"))) {
                    String[] parentIds = item.getParentCtlid().split(",");
                    String pidStr = null;
                    for (String tmpid : parentIds) {
                        String[] pidArr = tmpid.split(":");
                        if (pidArr.length == 2) {
                            FormItem tmpItem = formItemMap.get(pidArr[1]);
                            //这里有一些遗漏的父id还没有被拷贝，因此需要进行临时储存，当完成后再进行填充
                            if (tmpItem==null){
                                tempFormItemMap.put(pidArr[1],itemNew);
                                continue;
                            }
                            if (pidStr == null) {
                                pidStr = tmpItem.getOrders() + ":" + tmpItem.getId();
                            } else {
                                pidStr = pidStr + "," + tmpItem.getOrders() + ":" + tmpItem.getId();
                            }
                        }
                    }
                    itemNew.setParentCtlid(pidStr);
                }
                if (itemNew.getParentCtlvalue() != null && !itemNew.getParentCtlvalue().isEmpty()) {
                    String[] pValIds = itemNew.getParentCtlvalue().split(",");
                    String pValStr = null;
                    for (String tmpVal : pValIds) {
                        String[] pvalArr = tmpVal.split(":");
                        if (pvalArr.length == 2) {
                            FormItemFieldData tmpdata = formItemFieldDataMap.get(pvalArr[1]);
                            //捕获遗漏的
                            if(tmpdata == null){
                                tempFormItemMapForValue.put(pvalArr[1],itemNew);
                                continue;
                            }
                            if (pValStr == null) {
                                pValStr = tmpdata.getOrders() + ":" + tmpdata.getId();
                            } else {
                                pValStr = pValStr + "," + tmpdata.getOrders() + ":" + tmpdata.getId();
                            }
                        }
                    }
                    itemNew.setParentCtlvalue(pValStr);
                }
                itemNew.setCreateTime(new Date());
                itemNew.setUpdateTime(null);
                formItemNewList.add(itemNew);
                ffiMap.get(ffi.getId()).addFormItem(itemNew);
                formItemMap.put(item.getId(), itemNew);

                // 字段选项
                itemFieldDataList = formItemFieldDataRepository.findByFieldId(item.getId());
                if (itemFieldDataList != null) {
                    Map<String,FormItemFieldData> map=new HashMap<>();
                    for (FormItemFieldData data : itemFieldDataList) {
                        FormItemFieldData dataNew = new FormItemFieldData();
                        BeanUtils.copyProperties(data, dataNew, SysUtils.getNullPropertyNames(data));
                        dataNew.setId(UUID.randomUUID().toString());
                        dataNew.setFieldId(itemNew.getId());
                        //map的使用过早，可能未存储值，然后进行获取，造成空指针和部分数据的填充不完整
                        if (dataNew.getParentId() != null && !dataNew.getParentId().isEmpty() &&
                                !dataNew.getParentId().equals("-1")) {
                            if (formItemFieldDataMap.get(dataNew.getParentId()) != null) {
                                dataNew.setParentId(formItemFieldDataMap.get(dataNew.getParentId()).getId());
                            }else {
                                map.put(dataNew.getParentId(),dataNew);
                            }
                        }
                        itemFieldDataNewList.add(dataNew);
                        itemNew.addFormItemData(dataNew);
                        formItemFieldDataMap.put(data.getId(), dataNew);
                    }
                    Set<String> set1 = map.keySet();
                    for (String s : set1) {
                        FormItemFieldData formItemFieldData = map.get(s);
                        FormItemFieldData formItemFieldData1 = formItemFieldDataMap.get(s);
                        if (formItemFieldData1 != null){
                            formItemFieldData.setParentId(formItemFieldData1.getId());
                        }
                    }
                }

            }
        }
        //当上面的完成之后，再对遗漏的元素进行赋值
        Set<String> set1 = tempFormItemMap.keySet();
        for (String s : set1) {
            FormItem tmpItem = tempFormItemMap.get(s);
            FormItem formItem = formItemMap.get(s);
            String pidStr = tmpItem.getParentCtlid();
            if (formItem!=null){
                if (pidStr == null) {
                    pidStr = formItem.getOrders() + ":" + formItem.getId();
                } else {
                    pidStr = pidStr + "," + formItem.getOrders() + ":" + formItem.getId();
                }
            }
            tmpItem.setParentCtlid(pidStr);
        }
        Set<String> set2 = tempFormItemMapForValue.keySet();
        for (String s : set2) {
            FormItem formItem = tempFormItemMapForValue.get(s);
            FormItemFieldData formItemFieldData = formItemFieldDataMap.get(s);
            String pValStr = formItem.getParentCtlvalue();
            if (formItemFieldData != null){
                if (pValStr == null) {
                    pValStr = formItemFieldData.getOrders() + ":" + formItemFieldData.getId();
                } else {
                    pValStr = pValStr + "," + formItemFieldData.getOrders() + ":" + formItemFieldData.getId();
                }
            }
            formItem.setParentCtlvalue(pValStr);
        }
        formItemRepo.saveAll(formItemNewList);
        formItemFieldDataRepository.saveAll(itemFieldDataNewList);
        // 自动填写
//        List<FieldFilterMap> fieldFilterMapList = fieldFilterMapRepository.findAllByFormId(formId);
//        if (fieldFilterMapList != null && !fieldFilterMapList.isEmpty()) {
//            List<FieldFilterMap> fieldFilterMapNewList = new ArrayList<>();
//            for (FieldFilterMap fieldMap: fieldFilterMapList) {
//                FieldFilterMap tmpMap = new FieldFilterMap();
//                BeanUtils.copyProperties(fieldMap,tmpMap, SysUtils.getNullPropertyNames(fieldMap));
//                tmpMap.setId(null);
//                tmpMap.setFormId(newForm.getId());
//                tmpMap.setFieldId(formItemMap.get(fieldMap.getFieldId()).getId());
//                tmpMap.setCreateTime(new Date());
//                tmpMap.setUpdateTime(null);
//                fieldFilterMapNewList.add(tmpMap);
//            }
//            fieldFilterMapRepository.saveAll(fieldFilterMapNewList);
//        }

        // user_form
        if (newForm.getIsTemplate() != 1 && (!newForm.getStatus().equals("0"))) {
//            Long userId = UserPrincipal.getSessionUser().getUser().getId();//从Session获取user
//            UserForm uf = new UserForm();
//            uf.setFormId(newForm.getId());
//            uf.setUserId(userId);
//            userFormRepo.save(uf);
            //复制完表单之后，将表单和部门相关联
//            DepartmentForm departmentForm=new DepartmentForm();
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal == null) {
                return null;
            }
            CustomUser user = null;
            if (principal instanceof CustomUser) {
                user = (CustomUser) principal;
            }
            if (user == null) {
                return null;
            }
            DbSysUser voUserMenu = user.getVoUserMenu();
//            departmentForm.setUserId(voUserMenu.getId());
//            departmentForm.setDepartmentId(voUserMenu.getDeptId());
//            departmentForm.setFormId(Integer.parseInt(newForm.getId()+""));
            //根据角色创建多个与表单关联的记录
            List<SysRelationRu> sysRelationRus = sysRelationRuRepository.findAllByUserId(voUserMenu.getId());
            List<DepartmentForm> departmentFormsList=new ArrayList<>();
            if (sysRelationRus != null && sysRelationRus.size() > 0){
                for (SysRelationRu relationRus : sysRelationRus) {
                    DepartmentForm departmentForms = new DepartmentForm();
                    departmentForms.setFormId(Integer.parseInt(newForm.getId().toString()));
                    departmentForms.setUserId(voUserMenu.getId());
                    departmentForms.setRoleId(relationRus.getRoleId());
                    departmentFormsList.add(departmentForms);
                }
            }
            if (departmentFormsList.size() > 0){
                departmentFormRepository.saveAll(departmentFormsList);
            }
        }
        return newForm;
    }

    @Override
    @Transactional
    public FormFormItem copyFormSectionFromTemplate(Long ffiId, Long destFormId, Long destFfiId, Long destFfiOrder, String destName, Integer isTemplate) {
        FormFormItem ffi = formFormItemRepo.findById(ffiId).orElse(null);
        FormFormItem ffiDest = formFormItemRepo.findById(destFfiId).orElse(null);
        if (ffi == null) { return null;}
        if (ffi.getIsList() == 1) {return null;}

        FormFormItem ffiNew  = new FormFormItem();
        BeanUtils.copyProperties(ffi,ffiNew, SysUtils.getNullPropertyNames(ffi));
        if (destName != null && destName.length()>0 && !destName.equalsIgnoreCase("null")) {
            ffiNew.setName(destName);
        } else {
            ffiNew.setName(ffi.getName());
        }
        ffiNew.setId(null);

        if (isTemplate == 1) {
            ffiNew.setParentId(null);
            ffiNew.setFormId(-1L);
            ffiNew.setOrders(0L);
        } else {
            if (ffiDest == null || ffiDest.getIsList() == 0) {
                ffiNew.setParentId(-1L);
            } else {
                ffiNew.setParentId(destFfiId);
            }
            ffiNew.setFormId(destFormId);
            ffiNew.setOrders(destFfiOrder);
        }
        ffiNew.setCreateTime(new Date());
        ffiNew.setUpdateTime(null);
        ffiNew.setIsTemplate(isTemplate);
        ffiNew = formFormItemRepo.save(ffiNew);

        List<FormItemFieldData> itemFieldDataList;
        List<FormItem> formItemNewList  = new ArrayList<>();
        List<FormItemFieldData> itemFieldDataNewList = new ArrayList<>();
        Map<String, FormItem> formItemMap = new HashMap();
        Map<String, FormItemFieldData> formItemFieldDataMap = new HashMap();
        FormItem itemNew;
        List<FormItem> formItemList = formItemRepo.findByLayerId(ffiId);
        if (formItemList == null) { return ffiNew;}
        for(FormItem item: formItemList) {
            itemNew = new FormItem();
            BeanUtils.copyProperties(item,itemNew, SysUtils.getNullPropertyNames(item));
            itemNew.setId(UUID.randomUUID().toString());
            // 层级ID
            itemNew.setLayerId(ffiNew.getId());
            itemNew.setCreateTime(new Date());
            itemNew.setUpdateTime(null);
            formItemNewList.add(itemNew);
            ffiNew.addFormItem(itemNew);
            formItemMap.put(item.getId(),itemNew);

            // 字段选项
            itemFieldDataList = formItemFieldDataRepository.findByFieldId(item.getId());
            if (itemFieldDataList != null) {
                for(FormItemFieldData data: itemFieldDataList) {
                    FormItemFieldData dataNew = new FormItemFieldData();
                    BeanUtils.copyProperties(data,dataNew, SysUtils.getNullPropertyNames(data));
                    dataNew.setId(UUID.randomUUID().toString());
                    dataNew.setFieldId(itemNew.getId());
                    itemFieldDataNewList.add(dataNew);
                    itemNew.addFormItemData(dataNew);
                    formItemFieldDataMap.put(data.getId(),dataNew);
                }
            }
        }
        if (formItemNewList != null && !formItemNewList.isEmpty()) {
            for(FormItem item1:formItemNewList) {
                // 父控件触发ID
                String parentid = item1.getParentCtlid();
                if (parentid != null && parentid.length()>0 && (!parentid.equals("0"))) {
//                    FormItem tmp1 = formItemMap.get(parentid);
//                    item1.setParentCtlid(tmp1==null?null:tmp1.getId());
                    String[] parentIds = parentid.split(",");
                    String pidStr = null;
                    for (String tmpid: parentIds) {
                        String[] pidArr = tmpid.split(":");
                        if (pidArr.length == 2){
                            FormItem tmpItem = formItemMap.get(pidArr[1]);
                            if (tmpItem == null) {continue;}
                            if (pidStr == null) {
                                pidStr = tmpItem.getOrders() + ":"+ tmpItem.getId();
                            } else {
                                pidStr = pidStr + "," + tmpItem.getOrders() + ":"+ tmpItem.getId();
                            }
                        }
                    }
                    item1.setParentCtlid(pidStr);
                }

                // 父控件触发值
                String parentvalue = item1.getParentCtlvalue();
                if (parentvalue != null && parentvalue.length()>0) {
//                    FormItemFieldData fielddata1 = formItemFieldDataMap.get(parentvalue);
//                    item1.setParentCtlvalue(fielddata1==null?null:fielddata1.getId());
                    String[] pvalIds = parentvalue.split(",");
                    String pvalStr = null;
                    for (String tmpval : pvalIds) {
                        String[] pvalArr = tmpval.split(":");
                        if (pvalArr.length == 2) {
                            FormItemFieldData tmpData = formItemFieldDataMap.get(pvalArr[1]);
                            if (tmpData == null) {continue;}
                            if (pvalStr == null) {
                                pvalStr = tmpData.getOrders() + ":"+ tmpData.getId();
                            } else {
                                pvalStr = pvalStr + "," + tmpData.getOrders() + ":"+ tmpData.getId();
                            }
                        }
                    }
                    item1.setParentCtlvalue(pvalStr);
                }
            }
        }
        if (itemFieldDataNewList != null && !itemFieldDataNewList.isEmpty()) {
            for(FormItemFieldData data1:itemFieldDataNewList) {
                String parentid = data1.getParentId();
                if (parentid != null && parentid.length()>0 && !parentid.equals("-1")) {
                    FormItemFieldData fielddata1=formItemFieldDataMap.get(parentid);
                    data1.setParentId(fielddata1==null?null:fielddata1.getId());
                }
            }
        }
        formItemRepo.saveAll(formItemNewList);
        formItemFieldDataRepository.saveAll(itemFieldDataNewList);

        return ffiNew;
    }

    /**
     * 获取所有form以及formitem
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Form> findAll(int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Specification<Form> specification = new Specification<Form>() {
            @Override
            public Predicate toPredicate(Root<Form> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("status"),"1"));
                Predicate[] ps = new Predicate[list.size()];
                return cb.and(list.toArray(ps));
            }
        };

        Page<Form> pageData = formRepo.findAll(specification, pageable);

        List<Long> buildedList = new ArrayList<>();
        List<Form> formList = pageData.getContent();
        for (Form form : formList) {
            buildAllFormItem(form);// 获取所有的FormItem
        }

        return pageData;
    }

    /**
     * 获取当前用户所属部门的所有form,不包括formitem
     * @param
     * @return
     */
    @Override
    public List<Form> findAllSimple() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return null;
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        if (user == null) {
            return null;
        }
        DbSysUser voUserMenu = user.getVoUserMenu();
        List<SysRelationRu> sysRelationRus = sysRelationRuRepository.findAllByUserId(voUserMenu.getId());
        List<Integer> roleIds = new ArrayList<>();
        for (SysRelationRu relationRus : sysRelationRus) {
            roleIds.add(relationRus.getRoleId());
        }
        List<Form> allForm = formRepo.findAllFormByDepartmentId(roleIds);
        return allForm;
    }

    @Override
    @Transactional
    public synchronized FormItem updateFormItem(FormItem i) {
        FormItem existItem = formItemRepo.findById(i.getId()).orElse(null);
        if (existItem == null) {
            return null;
        }
        BeanUtils.copyProperties(i, existItem);
        existItem = formItemRepo.save(existItem);
        List<FormItemFieldData> dataList = i.getDataSourceList();
        existItem.setDataSourceList(dataList);
        if (dataList != null) {
            //这里应该先删除多余的条目，然后再进行插入，不然那些已经删除的数据条目还是会存在的
            for(FormItemFieldData data: dataList) {
                data = formItemFieldDataRepository.save(data);
            }
        }
        return existItem;
    }

    /**
     * 获取当前用户的条目，这里由于没有建立条目和部门的关系，因此没有我的条目的显示
     * @param deptId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<FormItem> findMyFormItem(Integer deptId, int page, int size) {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // 从UserFormItem查询此用户的FormItem
        Specification<DepartmentForm> specification = new Specification<DepartmentForm>() {
            @Override
            public Predicate toPredicate(Root<DepartmentForm> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                list.add(cb.equal(root.get("departmentId"), deptId));
                Predicate[] ps = new Predicate[list.size()];
                return cb.and(list.toArray(ps));
            }
        };

        // 获取分页数据
        Page<DepartmentForm> userFormItemPage = departmentFormRepository.findAll(specification, pageable);

        // 获取此页的FormItem
        List<DepartmentForm> userFormItemList = userFormItemPage.getContent();
        List<FormItem> itemList = new ArrayList<>(userFormItemList.size());
//        for (DepartmentForm ufItem : userFormItemList) {
//            List<FormItem> formItemList = formItemRepo.findByLayerId(ufItem.getLayerId());
//            if (formItemList != null) {
//                for(FormItem item:formItemList) {
//                    itemList.add(item);
//                    List<FormItemFieldData> dataList = fieldDataRepo.findByFieldId(item.getId());
//                    if (dataList != null) {
//                        for (FormItemFieldData data: dataList) {
//                            item.addFormItemData(data);
//                        }
//                    }
//                }
//            }
//        }

        // 组装FormItem的分页数据
        Page<FormItem> formItemPage = new PageImpl<FormItem>(itemList, pageable, userFormItemPage.getTotalElements());
        return formItemPage;
    }

    /**
     * 获取当前用户名下的表单
     * @param deptId
     * @param page
     * @param size
     * @param tableName
     * @return
     */
    @Override
    public Page<Form> findMyForm(Integer deptId, int page, int size, String tableName) {
        //先拿出哪些id包含这些名字

        List<Long>  idList=new ArrayList<>();
        if ( tableName == null || "".equals(tableName.trim())){
           idList = formRepo.findAllIds();
        }else {
            idList=formRepo.findAllIdsByTableName("%"+tableName+"%");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return null;
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        if (user == null) {
            return null;
        }
        DbSysUser voUserMenu = user.getVoUserMenu();
        List<SysRelationRu> sysRelationRus = sysRelationRuRepository.findAllByUserId(voUserMenu.getId());
        List<Integer> roleIds = new ArrayList<>();
        for (SysRelationRu relationRus : sysRelationRus) {
            roleIds.add(relationRus.getRoleId());
        }
        Integer startIndex = (page-1)*size;
        Integer endIndex=page*size;
        Sort sort = new Sort(Sort.Direction.DESC, "form_id");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Integer> userFormPage = departmentFormRepository.findFormByPage(idList,roleIds,pageable);
//        // 从UserForm查询此用户的Form
//        List<Long> finalIdList = idList;
//        Specification<DepartmentForm> specification = new Specification<DepartmentForm>() {
//            @Override
//            public Predicate toPredicate(Root<DepartmentForm> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                List<Predicate> list = new ArrayList<>();
////                if (!isAdmin) {
////                    list.add(cb.equal(root.get("userId"), userId));
////                }
//                list.add(cb.equal(root.get("departmentId").as(Integer.class),deptId));
//                CriteriaBuilder.In<Integer> in = cb.in(root.get("formId"));
//                for (Long integer : finalIdList) {
//                    in.value(Integer.valueOf(""+integer));
//                }
//                list.add(in);
//                Predicate[] ps = new Predicate[list.size()];
//                return cb.and(list.toArray(ps));
//            }
//        };

        // 获取分页数据
//        Page<DepartmentForm> userFormPage = departmentFormRepository.findAll(specification, pageable);

        // 获取此页的FormItem
        List<Integer> userFormList = userFormPage.getContent();
        Page<Form> formPage = null;
        List<Form> formList = null;
        Long cnt = 0L;
        if (userFormList != null && userFormList.size() > 0) {
            formList = new ArrayList<>(userFormList.size());
            List<Long> buildedList = new ArrayList<>();//标记已经构建过的FormItem
            for (Integer uf : userFormList) {
                // log.info("查找表单：id={}", uf.getFormId());
                Form form = formRepo.findById(Long.parseLong(uf+"")).orElse(null);
                if (form == null) continue;
                // 构建此Form的所有FormItem
                if(tableName!=null){
                    if(form.getName().contains(tableName)){
//                        this.buildAllFormItem(form);
                        formList.add(form);
                    }
                }else{
//                    this.buildAllFormItem(form);
                    formList.add(form);
                }
            }
            cnt = userFormPage.getTotalElements();
        } else {
            formList = new ArrayList<>(0);
            cnt = 0L;
        }
        // 组装FormItem的分页数据
        formPage = new PageImpl<Form>(formList, pageable, cnt);
        return formPage;
    }

    /**
     * 获取一个节点，根据节点id来获取一个节点
     * @param id
     * @return
     */
    @Override
    public FormFormItem findOneSection(Long id) {
        FormFormItem item = formFormItemRepo.findById(id).orElse(null);
        if (item != null) {
            buildFormFormItem(item);
        }
        return item;
    }

    /**
     * 将上传的表单存储到本地
     * @param pre
     * @param file
     * @return
     */
    @Override
    public Path store(String pre, MultipartFile file) {
        String			prename  = org.springframework.util.StringUtils.cleanPath(pre);
        String			filename = org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());
        StringBuffer	sb = new StringBuffer();
        Path			tmp;
        int				index;
        String			dirname;

        tmp = Paths.get(prename);
        tmp.forEach(name->sb.append(name).append("/"));
        tmp = Paths.get(filename);
        tmp.forEach(name->sb.append(name).append("/"));
        sb.deleteCharAt(sb.length()-1);
        filename = sb.toString();
        dirname = filename;
        index = dirname.lastIndexOf("/");
        if (index != -1)
            dirname = dirname.substring(0, index);


        Path path = null;
        Path dir  = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            dir  = this.rootLocation.resolve(dirname);
            Files.createDirectories(dir);
            path = this.rootLocation.resolve(filename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return path;
    }

    /**
     * 表单定义EXCEL文件导入数据库
     * @param path
     * @param userId
     * @param failList
     * @return
     */
    @Override
    public String form2db(Path path, Integer userId, List<String> failList) {
        DataFormatter dataFormatter = new DataFormatter();
        Workbook wb = null;
        Form form = null;
        try {
            wb = WorkbookFactory.create(path.toFile());
            int numSheet = wb.getNumberOfSheets();

            // 表单定义：【表单定义】sheet页
            Integer numSheets = wb.getNumberOfSheets();
            if (numSheets < 4) {
                if (numSheets>=1 && !wb.getSheetAt(0).getSheetName().equals("基础数据") ) {
                    return "缺少【基础数据】页";
                }
                if (numSheets>=2 && !wb.getSheetAt(1).getSheetName().equals("sample")) {
                    return "缺少【sample】页";
                }
                if (numSheets>=3 && !wb.getSheetAt(1).getSheetName().equals("表单定义")) {
                    return "缺少【表单定义】页";
                }
                return "表单定义文件不完整";
            }
            Sheet sheet = wb.getSheetAt(2);
            Row row = null;
            if (sheet != null) {
                row = sheet.getRow(1);
                Long   id    = null;
                id   = (long)row.getCell(0).getNumericCellValue();             // 1 表单id
                row.getCell(1).setCellType(CellType.STRING);
                String nameStr = StringUtils.trim(row.getCell(1).getStringCellValue());             // 2 表单名称
                if (id != null) {
                    form = formRepo.findById(id).orElse(null);
                    if (form == null) {
                        List<Form> tmpList = formRepo.findByName(nameStr);
                        if(tmpList != null && !tmpList.isEmpty()) {
                            form = tmpList.get(0);
                        }
                    }
                }
                if (form == null) {
                    form = new Form();
                    form.setId(id);
                    form.setName(nameStr);
                }
                try {
                    form.setIsTemplate((int)row.getCell(2).getNumericCellValue()); // 3 模板标志
                } catch(Exception e) {
                    form.setIsTemplate(0);
                }
                form.setStatus("1");
                form.setVersions(1);
                FormScope scope = FormScope.User;
                if (row.getCell(3).getStringCellValue().equals("Sys")){         // 4 可见范围
                    scope = FormScope.Sys;
                }
                form.setScope(scope);
                if (row.getLastCellNum()>=4)  {
                    form.setDescription(row.getCell(4).getStringCellValue());   // 5 描述
                }
                if (row.getLastCellNum()>=5)  {
                    try {
                        form.setIsHideFfi((int)row.getCell(5).getNumericCellValue()); // 6 隐藏层级关系标志
                    } catch(Exception e) {
                        form.setIsHideFfi(0);
                    }
                } else {
                    form.setIsHideFfi(0);
                }
            } else {
                return "没有表单定义信息【表单定义】页";
            }
            // 保存表单
            form = formRepo.saveAndFlush(form);
//            UserForm userForm = userFormRepo.findFirstByUserIdAndFormId(user_id,form.getId());
//            if (userForm == null && form.getIsTemplate() != 1) {
//                UserForm uf = new UserForm();
//                uf.setFormId(form.getId());
//                uf.setUserId(user_id);
//                uf.setCreateTime(new Date());
//                userFormRepo.saveAndFlush(uf);
//            }
            //进行部门和表单的关联，如果已经存在关联就不用管，如果没有，就需要创建一个关联
            DepartmentForm departmentForm = departmentFormRepository.findFirstByUserIdAndFormId(userId, Integer.parseInt(form.getId() + ""));
            if (departmentForm == null && form.getIsTemplate() != 1){
                //根据角色创建多个与表单关联的记录
                List<SysRelationRu> sysRelationRus = sysRelationRuRepository.findAllByUserId(userId);
                List<DepartmentForm> departmentFormsList=new ArrayList<>();
                if (sysRelationRus != null && sysRelationRus.size() > 0){
                    for (SysRelationRu relationRus : sysRelationRus) {
                        DepartmentForm departmentForms = new DepartmentForm();
                        departmentForms.setFormId(Integer.parseInt(form.getId().toString()));
                        departmentForms.setUserId(userId);
                        departmentForms.setRoleId(relationRus.getRoleId());
                        departmentFormsList.add(departmentForms);
                    }
                }
                if (departmentFormsList.size() > 0){
                    departmentFormRepository.saveAll(departmentFormsList);
                }
//                DepartmentForm departmentForm1 = new DepartmentForm();
//                departmentForm1.setFormId(Integer.parseInt(form.getId() + ""));
//                //根据用户id获取它的部门id
//                departmentForm1.setDepartmentId(2);
//                departmentForm1.setUserId(userId);
//                departmentFormRepository.saveAndFlush(departmentForm1);
            }
            // 表单数据项目
            String moduleName=null;
            List<FormFormItem> ffiList = new ArrayList<>(2);
            form.setFormFormItemList(ffiList);
            FormFormItem curFfi = null;
            Long order = 0L;
            Boolean is2Layer = false;
            for(int i=3;i<numSheet;i++) {
                sheet = wb.getSheetAt(i);
                moduleName = sheet.getSheetName();
                String[] names = moduleName.split("\\|");
                if (names != null && names.length>=2 && names[1].equals("0")) {
                    is2Layer = true;
                } else {
                    is2Layer = false;
                }
                if (!is2Layer){
                    if (names.length>=2 && names[1]!=null){
                        try {
                            curFfi = formFormItemRepo.findById(Long.parseLong(names[1])).orElse(null);
                            if (curFfi != null) {
                                curFfi.setUpdateTime(new Date());
                            }
                        } catch (NumberFormatException e) {curFfi = null;}
                    }
                    if (curFfi == null){
                        curFfi = new FormFormItem();
                        curFfi.setCreateTime(new Date());
                    }
                    curFfi.setName(names[0]);
                    curFfi.setParentId(-1L);
                    curFfi.setIsList(1);
                    curFfi.setLabel(names[0]);
                    curFfi.setDescription(null);
                    curFfi.setFormId(form.getId());
                    curFfi.setOrders(order++);
                    curFfi.setIsRepeatable(0);
                    curFfi = formFormItemRepo.saveAndFlush(curFfi);
                    ffiList.add(curFfi);
                } else {
                    curFfi = new FormFormItem();
                    curFfi.setId(0L);
                    curFfi.setCreateTime(new Date());
                    curFfi.setName(names[0]);
                    curFfi.setParentId(-1L);
                    curFfi.setIsList(0);
                    curFfi.setLabel(names[0]);
                    curFfi.setDescription(null);
                    curFfi.setFormId(form.getId());
                    curFfi.setOrders(0L);
                    curFfi.setIsRepeatable(0);
                }
                order = this.importFormFormItem(sheet,form, order, is2Layer, curFfi,ffiList);
            }
            StringBuilder   json = new StringBuilder();
            try {
                json.append(mapper.writeValueAsString(form));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return "form("+ form.getId()+":"+ form.getName()+")层级关系序列化失败:" ;
            }
            return json.toString();
        } catch (EncryptedDocumentException | IOException e) {
            // throw new RuntimeException("Failed to todb file " + path.toString(), e);
            return "倒入文件操作失败| " + path.toFile() + "  |" + e.getMessage();
        } finally {
            if (wb != null)
                try { wb.close(); } catch (Exception e) {}
        }
    }

    /**
     * 导出表单
     * @param path
     * @param form
     */
    @Transactional
    @Override
    public String expFormDef(Path path, Form form) {
        String rtn = null;
        DataFormatter dataFormatter = new DataFormatter();
        Path example = storageService.load("exampleFormDef.xlsx");

        try {
            try (InputStream inputStream = new FileInputStream(example.toFile())) {
                Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
            }

            try (Workbook wb = WorkbookFactory.create(path.toFile())) {
                Sheet sheet,sheet2;
                Row   row,row2;
                Cell cell,cell2;
                CellStyle cellStyle;

                // 倒出表单定义
                sheet = wb.getSheetAt(2);
                row = sheet.getRow(1);
                cell = row.getCell(0);
                cell.setCellValue(form.getId());
                cell = row.getCell(1);
                cell.setCellValue(form.getName());
                cell = row.getCell(2);
                cell.setCellValue(form.getIsTemplate());
                cell = row.getCell(3);
                cell.setCellValue(form.getScope().getCode());
                cell = row.getCell(4);
                cell.setCellValue(form.getDescription());
                cell = row.getCell(5);
                cell.setCellValue(form.getIsHideFfi());

                // 找出模块和模块下的非叶子节点
                // ffiMap:    key = FormFormItem.id
                // subFfiMap: key = FormFormItem.id
                Map<Long,FormFormItem> ffiMap = new LinkedHashMap<>();
                Map<Long,List<FormFormItem>> subFfiMap = new LinkedHashMap<>();
                Long parentId = 0L;
                Boolean is2Layer = true;
                for (FormFormItem ffi:form.getFormFormItemList()) {
                    if (ffi.getParentId() == -1 && ffi.getIsList() != 0) {
                        is2Layer = false;
                    }
                }
                for (FormFormItem ffi:form.getFormFormItemList()) {
                    parentId = ffi.getParentId();
                    if (parentId == -1 && ffi.getIsList() != 0) {
                        ffiMap.put(ffi.getId(),ffi);
                    } else {
                        if (ffi.getFormItemList() != null && !ffi.getFormItemList().isEmpty()) {
                            FormFormItem obj = null;
                            while(parentId != -1L && parentId != null){
                                obj = ffiMap.get(parentId);
                                if (obj == null) break;
                                parentId = obj.getParentId();
                            }
                            if (is2Layer && obj == null) {
                                if (ffiMap.isEmpty()||ffiMap.get(0L) == null) {
                                    FormFormItem ffiTmp = new FormFormItem();
                                    ffiTmp.setId(0L);
                                    ffiTmp.setName(form.getName());
                                    ffiMap.put(0L,ffiTmp);
                                }
                                obj = ffiMap.get(0L);
                            }
                            if (obj != null) {
                                List<FormFormItem> fflist = subFfiMap.get(obj.getId());
                                if (fflist == null) {
                                    fflist = new ArrayList<>();
                                    subFfiMap.put(obj.getId(),fflist);
                                }
                                fflist.add(ffi);
                            }
                        }
                    }
                }
                // 输出到excel页：每个模块一个sheet页，每个字段一行
                for (Map.Entry<Long, FormFormItem> entry : ffiMap.entrySet()) {
                    Long id = entry.getKey();
                    FormFormItem ffiObj = entry.getValue();
                    // 页
                    List<FormFormItem> sublist = subFfiMap.get(id);
                    if (sublist == null||sublist.isEmpty()) {
                        continue;
                    }
                    sheet2 = wb.createSheet(ffiObj.getName() +"|"+ ffiObj.getId());

                    // 创建标题
                    sheet = wb.getSheet("sample");
                    row= sheet.getRow(0);
                    cellStyle = row.getCell(0).getCellStyle();
                    row2 = sheet2.createRow(0);
                    for(int k=0;k<24;k++) {
                        cell = row.getCell(k);
                        cell2 = row2.createCell(k);
                        cell2.setCellValue(cell.getStringCellValue());
                        cell2.setCellStyle(cell.getCellStyle());
                    }

                    // 创建数据
                    int i=0;
                    int j=0;
                    List<List<String>> data = new ArrayList<>();
                    for(FormFormItem ffi: sublist) {
                        List<FormItem> itemlist = ffi.getFormItemList();
                        if (itemlist == null) { continue;}
                        for(FormItem item: itemlist) {
                            List<String> rowData = new  ArrayList<>(24);
                            data.add(rowData);

                            rowData.add(String.valueOf(item.getOrders()));       // 1  顺序
                            rowData.add(ffi.getName());                          // 2  子分类
                            rowData.add(String.valueOf(ffi.getIsRepeatable()));  // 3  是否容许重复填写
                            rowData.add(item.getName());                         // 4  字段名称
                            rowData.add(item.getEname());                        // 5  英文名称
                            rowData.add(item.getType());                         // 6  字段类型
                            rowData.add(String.valueOf(item.getIsTemplate()));   // 7  是否模板
                            rowData.add(String.valueOf(item.getIsHidden()));     // 8  是否隐藏
                            rowData.add(String.valueOf(item.getIsSearch()));     // 9  是否查询条件
                            rowData.add(String.valueOf(item.getIsRequired()));   // 10 是否必填
                            rowData.add(String.valueOf(item.getIsMultText()));   // 11 是否多行文本
                            rowData.add(String.valueOf(item.getWidth()));        // 12 宽度百分比
                            rowData.add(item.getParentCtlid());                  // 13 父控件序号
                            rowData.add(item.getParentCtlvalue());               // 14 父控件值
                            String datatype = "";
                            if (item.getDataType() != null) {
                                datatype = item.getType() +":"+item.getDataType();
                            }
                            rowData.add(datatype);                                // 15 数据类型
                            if (item.getDataSourceList() != null && !item.getDataSourceList().isEmpty()) {
                                StringBuffer buffer= new StringBuffer();
                                item.getDataSourceList().stream().forEach(value -> {
                                    if (buffer.length() >0) {buffer.append("|"); }
                                    String tmp = value.getParentId()==null?"":value.getParentId();
                                    buffer.append(value.getId() +","+ tmp +","+ value.getName() +","+ value.getOrders());
                                });
                                rowData.add(buffer.toString());                 // 16 数据选项
                            }else {
                                rowData.add("");
                            }
                            rowData.add(item.getUnit());                        // 17 单位
                            rowData.add(item.getMinValue());                    // 18 最小值
                            rowData.add(item.getMaxValue());                    // 19 最大值
                            rowData.add(item.getExtend());                      // 20 ext取值URL等
                            rowData.add(item.getValue());                       // 21 默认值
                            rowData.add(item.getNote());                        // 22 描述
                            rowData.add(item.getId());                          // 23 id
                            rowData.add(String.valueOf(item.getLayerId()));     // 24 层级编码

                            i++;
                        }
                        i=0;
                    }
                    // 输出数据到sheet页
                    for(i=0;i<data.size();i++) {
                        List<String> rowData = data.get(i);
                        row2 = sheet2.createRow(i+1);
                        for(j=0;j<rowData.size(); j++) {
                            cell2 = row2.createCell(j);
                            cell2.setCellValue(rowData.get(j));;
                            cell2.setCellStyle(cellStyle);
                        }
                    }
                }

                try (OutputStream fileOut = new NullOutputStream()) {
                    wb.write(fileOut);
                }

            }

        } catch (EncryptedDocumentException | IOException e) {
            throw new RuntimeException("tofile Failed ", e);
        }
        return "";
    }

    private Long importFormFormItem(Sheet sheet, Form form, Long order, Boolean is2Layer,
                                    FormFormItem ffi, List<FormFormItem> ffiList) {
        FormFormItem lastFfi=null;
        FormFormItem curFfi = null;
        FormItem lastFormItem = null;
        FormItem curFormItem  = null;

        int i=0;
        int j=0;
        int beginNum = sheet.getFirstRowNum();
        int endNum = sheet.getLastRowNum();
        Row row = null;
        long layerId = 0;
        String sectionName=null;
        Map<Long,FormItem> formItemMap = new HashMap<>();
        for (i=beginNum+1;i<=endNum; i++) {
            j=0;
            row = sheet.getRow(i);
            row.getCell(22).setCellType(CellType.STRING);
            String idStr = row.getCell(22).getStringCellValue();                    // 22 id
            row.getCell(23).setCellType(CellType.STRING);
            String layerStr = row.getCell(23).getStringCellValue();
            if (layerStr != null && layerStr.length()>0) layerId = Long.parseLong(layerStr); // 23 layer_id
            if (idStr != null && idStr.indexOf("-")>=0) {
                curFormItem = formItemRepo.findById(idStr).orElse(null);
            } else {
                curFormItem = null;
            }
            if (curFormItem == null){
                curFormItem = new FormItem();
                curFormItem.setId(UUID.randomUUID().toString());
                curFormItem.setVersions(1);
                curFormItem.setCreateTime(new Date());
            }  else {
                curFormItem.setVersions(curFormItem.getVersions()==null?1:curFormItem.getVersions()+1);
                curFormItem.setUpdateTime(new Date());
            }
            Long orders = 0L;
            row.getCell(j).setCellType(CellType.STRING);
            orders = Long.parseLong(row.getCell(j++).getStringCellValue());                   // 0 顺序号
            curFormItem.setOrders(orders);
            formItemMap.put(curFormItem.getOrders(),curFormItem);

            sectionName              = row.getCell(j++).getStringCellValue();                 // 1 子分类
            row.getCell(j).setCellType(CellType.STRING);
            int sectionRepeatable = Integer.parseInt(row.getCell(j++).getStringCellValue());  // 2 重复填写标志
            if (lastFfi==null || (!lastFfi.getName().equals(sectionName))) {
                FormFormItem obj = null;
                if (layerId >0)  {
                    obj = formFormItemRepo.findById(layerId).orElse(null);
                    if (obj == null && !is2Layer) {
                        obj = formFormItemRepo.findFirstByFormIdAndParentIdAndName(form.getId(),ffi.getId(),sectionName);
                    }
                }
                if (obj == null) {
                    curFfi = new FormFormItem();
                    curFfi.setCreateTime(new Date());
                } else {
                    curFfi = obj;
                    curFfi.setUpdateTime(new Date());
                }
                lastFfi = curFfi;
                curFfi.setName(sectionName);
                if (is2Layer) {
                    curFfi.setParentId(-1L);
                } else {
                    curFfi.setParentId(ffi.getId());
                }
                curFfi.setIsList(0);
                curFfi.setLabel(null);
                curFfi.setDescription(null);
                curFfi.setFormId(form.getId());
                curFfi.setOrders(order);
                curFfi.setIsRepeatable(sectionRepeatable);
                order++;
                curFfi = formFormItemRepo.saveAndFlush(curFfi);
                if (curFfi.getFormItemList()!=null) {curFfi.getFormItemList().clear();}
                ffiList.add(curFfi);
                formItemMap.clear();
                formItemMap.put(curFormItem.getOrders(),curFormItem);
            }
            curFfi.addFormItem(curFormItem);
            curFormItem.setLayerId(curFfi.getId());                         // 23设置层级关系
            curFormItem.setName(row.getCell(j++).getStringCellValue());     // 3 字段名称
            curFormItem.setEname(row.getCell(j++).getStringCellValue());    // 4 英文名称
            String fldType = row.getCell(j++).getStringCellValue();         // 5 字段类型
            switch(fldType) {
                case "text":
                    curFormItem.setIcon("edit");
                    curFormItem.setType("text");
                    break;
                case "radio":
                    curFormItem.setIcon("check-circle");
                    curFormItem.setType("radio");
                    break;
                case "checkbox":
                    curFormItem.setIcon("check-square");
                    curFormItem.setType("checkbox");
                    break;
                case "datepicker":
                    curFormItem.setIcon("calendar");
                    curFormItem.setType("datepicker");
                    break;
                case "dropdown":
                    curFormItem.setIcon("hdd");
                    curFormItem.setType("dropdown");
                    break;
                case "cascader":
                    curFormItem.setIcon("ordered-list");
                    curFormItem.setType("cascader");
                    break;
                default:
                    curFormItem.setIcon("edit");
                    curFormItem.setType("text");
                    break;
            }
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setIsTemplate(Integer.parseInt(row.getCell(j++).getStringCellValue()));  // 6 是否模板
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setIsHidden(Integer.parseInt(row.getCell(j++).getStringCellValue()));    // 7 是否隐藏
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setIsSearch(row.getCell(j++).getStringCellValue());                     // 8 是否查询条件
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setIsRequired(Integer.parseInt(row.getCell(j++).getStringCellValue()));  // 9 是否必填
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setIsMultText(Integer.parseInt(row.getCell(j++).getStringCellValue()));  // 10 是否多行文本
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setWidth(Integer.parseInt(row.getCell(j++).getStringCellValue()));       // 11 宽带百分比
            row.getCell(j).setCellType(CellType.STRING);
            String parentIdStr = row.getCell(j++).getStringCellValue();                          // 12 父控件序号
            row.getCell(j).setCellType(CellType.STRING);
            String parentValStr = row.getCell(j++).getStringCellValue();                         // 13 父控件值
            curFormItem.setParentCtlid("");
            curFormItem.setParentCtlvalue("");
            if (parentIdStr != null &&  parentIdStr.length()>0) {
                if ( parentIdStr.indexOf("-")>=0||parentValStr.indexOf("-")>=0) {   // 倒出数据id为UUID
                    curFormItem.setParentCtlid(parentIdStr);
                    curFormItem.setParentCtlvalue(parentValStr);
                } else {
                    /*
                     *  多个父节点，每个父节点可能多个值
                     */
                    try {
                        String[] parentSeqList = parentIdStr.split(",");
                        String[] parentValList = parentValStr!= null?parentValStr.split(","):null;
                        Map<Long,List<String>> valMap = new HashMap<>();
                        List<String> val0List = null;
                        if (parentValList!= null && parentValList.length>0) {
                            for(String str0 : parentValList) {
                                String[] vals = str0.split(":");
                                if(vals != null && vals.length == 2) {
                                    val0List = valMap.get(Long.parseLong(vals[0]));
                                    if (val0List == null) {
                                        val0List = new ArrayList();
                                        valMap.put(Long.parseLong(vals[0]), val0List);
                                    }
                                    val0List.add(vals[1]);
                                }
                            }
                        }
                        if (parentSeqList != null && parentSeqList.length>0) {
                            int k=0;
                            String pid=null,pidVal=null;
                            for(String strSeq : parentSeqList) {
                                Long parentSeq = Long.parseLong(strSeq);
                                FormItem obj = formItemMap.get(parentSeq);
                                val0List = valMap.get(parentSeq);
                                if (obj == null || obj.getDataSourceList() == null) {continue;}
                                if (val0List == null || val0List.isEmpty()) {continue;}
                                if (pid == null) {
                                    pid = obj.getOrders() + ":" + obj.getId();
                                } else {
                                    pid += "," + obj.getOrders() + ":" + obj.getId();
                                }
                                for(int m=0;m<val0List.size();m++) {
                                    for(FormItemFieldData data:obj.getDataSourceList()) {
                                        if (data.getName().equals(val0List.get(m))) {
                                            if (pidVal == null) {
                                                pidVal = data.getOrders() + ":" + data.getId();
                                            } else {
                                                pidVal += "," + data.getOrders() + ":" + data.getId();
                                            }
                                            break;
                                        }
                                    }
                                }
                                k++;
                            }
                            curFormItem.setParentCtlid(pid);
                            curFormItem.setParentCtlvalue(pidVal);
                        }
                        ////////////////////////////////////////////////
//                        Long parentSeq = Long.parseLong(parentIdStr);
//                        FormItem obj = formItemMap.get(parentSeq);
//                        if (obj != null) { // 自定义数据，没有UUID，只有序号
//                            if (obj.getDataSourceList() != null) {
//                                for(FormItemFieldData data:obj.getDataSourceList()) {
//                                    if(data.getName().equals(parentValStr)) {
//                                        curFormItem.setParentCtlid(obj.getId());
//                                        curFormItem.setParentCtlvalue(data.getId());
//                                        break;
//                                    }
//                                }
//                            }
//                        }
                    } catch (NumberFormatException e) {e.printStackTrace();}
                }
            }

            // 忽略text
            row.getCell(j).setCellType(CellType.STRING);
            String tmpStr = row.getCell(j++).getStringCellValue();                 // 14 数据类型
            curFormItem.setDataType("");
            if (tmpStr != null && tmpStr.length()>0) {
                String[] dataType = tmpStr.split(":");
                if (dataType.length==2 && dataType[1] != null && dataType[1].length()>0) {
                    if (!dataType[1].equals("text")) {
                        curFormItem.setDataType(dataType[1]);
                    }
                }
            }


            // 忽略null
            List<FormItemFieldData> dataList = null;
            row.getCell(j).setCellType(CellType.STRING);
            tmpStr = row.getCell(j++).getStringCellValue();                       // 15 数据选项
            if (tmpStr != null && tmpStr.length()>0) {
                String[] dataSourceList = tmpStr.split("\\|");
                dataList = new ArrayList<>();
                Long order1 = 0L;
                for (String data: dataSourceList) {
                    String[] fieldList = data.split(",");
                    if (fieldList == null||fieldList.length<1) continue;
                    FormItemFieldData fieldData = new FormItemFieldData();
                    fieldData.setFieldId(curFormItem.getId());
                    if (fieldList.length==1) {
                        fieldData.setId(UUID.randomUUID().toString());
                        fieldData.setName(fieldList[0]);
                        fieldData.setOrders(order1);
                        fieldData.setParentId(null);
                    } else if (fieldList.length==4) {
                        fieldData.setId(fieldList[0]);
                        fieldData.setParentId(fieldList[1]);
                        fieldData.setName(fieldList[2]);
                        try {
                            if (fieldList[3] != null)  {
                                fieldData.setOrders(Long.parseLong(fieldList[3]));
                            } else {
                                fieldData.setOrders(order1);
                            }
                        } catch (NumberFormatException e) {fieldData.setOrders(order1);}
                    } else {
                        continue;
                    }
                    dataList.add(fieldData);
                    order1++;
                }
                formItemFieldDataRepository.saveAll(dataList);
            }
            curFormItem.setDataSourceList(dataList);
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setUnit(row.getCell(j++).getStringCellValue());            // 16 单位
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setMinValue(row.getCell(j++).getStringCellValue());        // 17 最小值
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setMaxValue(row.getCell(j++).getStringCellValue());        // 18 最大值
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setExtend(row.getCell(j++).getStringCellValue());          // 19 取值URL等
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setValue(row.getCell(j++).getStringCellValue());           // 20 默认值
            row.getCell(j).setCellType(CellType.STRING);
            curFormItem.setNote(row.getCell(j++).getStringCellValue());            // 21 描述
            curFormItem.setScope("User");
            curFormItem = formItemRepo.saveAndFlush(curFormItem);
        }
        return  order;
    }
    @Override
    public Page<FormItem> getTemplateItemes(String itemName, String itemType, Integer pageSize, Integer pageNum) {

        Specification<FormItem> formItemSpecification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                if (itemName != null && !itemName.trim().equals("")){
                    list.add(criteriaBuilder.like(root.get("name").as(String.class),"%"+itemName+"%"));
                }
                if (itemType != null && !itemType.trim().equals("")){
                    list.add(criteriaBuilder.like(root.get("name").as(String.class),"%"+itemType+"%"));
                }
                list.add(criteriaBuilder.equal(root.get("isTemplate").as(Integer.class),1));
                CriteriaQuery resultQuery = query.where(criteriaBuilder.and(list.toArray(new Predicate[list.size()])));
                return resultQuery.getRestriction();
            }
        };
        Pageable pageable=new PageRequest(pageNum-1,pageSize,new Sort(Sort.Direction.DESC,"orders"));
        Page<FormItem> page = formItemRepo.findAll(formItemSpecification, pageable);
        List<FormItem> content = page.getContent();
        if (content != null && content.size()>0){
            for (FormItem item : content) {
                List<FormItemFieldData> dataList = formItemFieldDataRepository.findByFieldId(item.getId());
                if (dataList != null) {
                    for (FormItemFieldData data: dataList) {
                        item.addFormItemData(data);
                    }
                }
            }
        }
        return page;
    }

    @Override
    @Transactional
    public synchronized String updateFormFormItemOrders(Long id1, Long order1, Long id2, Long order2) {
        String rtn="";
        FormFormItem srcObj = null;
        if (id1 == null|| id2 == null) {
            rtn = "层级关系ID1不能为空";
        } else {
            srcObj = formFormItemRepo.findById(id1).orElse(null);
            if (srcObj == null) {
                rtn = id1 + "层级关系不存在";
            } else {
                formFormItemRepo.updateOrder(order1, id1);
            }

            FormFormItem srcObj2 = null;
            srcObj2 = formFormItemRepo.findById(id2).orElse(null);
            if (srcObj2 == null) {
                rtn = id2 + "层级关系不存在";
            } else {
                formFormItemRepo.updateOrder(order2, id2);
            }
        }
        return rtn;
    }

    /**
     * 获取数据项模板
     * @param name
     * @param classId
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<DataItemVO> getAll(String name, Long classId, int page, int size) {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Specification<TemplateDataItem> specification = new Specification<TemplateDataItem>() {
            @Override
            public Predicate toPredicate(Root<TemplateDataItem> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if (name != null && name.length()>0 &&
                        (!"null".equals(name.toLowerCase())) &&
                        (!"\"\"".equals(name.toLowerCase()))) {
                    list.add(cb.like(root.get("title"), "%"+name+"%"));
                }
                if (classId != -1L && classId != 0L) {
                    list.add(cb.equal(root.get("classId"), classId));
                }
                Predicate[] ps = new Predicate[list.size()];
                return cb.and(list.toArray(ps));
            }
        };
        Page<TemplateDataItem> qryList = templateDataItemRepository.findAll(specification,pageable);
        List<DataItemVO> dataList;
        Long cnt;
        if (!qryList.getContent().isEmpty()) {
            cnt = qryList.getTotalElements();
            dataList = new ArrayList<DataItemVO>(cnt.intValue());
            for (TemplateDataItem di:qryList) {
                DataItemVO itemVo = new DataItemVO();
                itemVo.setDataItem(di);
                List<TemplateDataItemData> dataItemDataList = templateDataItemDataRepository.findbyDataitemId(di.getId());
                itemVo.setDataItemDataList(dataItemDataList);
                dataList.add(itemVo);
            }
        } else {
            dataList = new ArrayList<>(0);
            cnt = 0L;
        }
        Page<DataItemVO> rtnList = new PageImpl<>(dataList,pageable, cnt);
        return rtnList;
    }

    @Override
    @Transactional
    public synchronized RestResult<DataItemVO> createDataItem(DataItemVO item) {

        TemplateDataItem dataitem = item.getDataItem();
        List<TemplateDataItemData> itemDataList =  item.getDataItemDataList();

        TemplateDataItem dbObj = templateDataItemRepository.findById(dataitem.getId()).orElse(null);
        if (dataitem != null && dbObj == null) {

            dataitem.setCreateTime(new Date());
            templateDataItemRepository.save(dataitem);

            templateDataItemDataRepository.deleteByDataIemId(dataitem.getId());
            if (itemDataList != null && !itemDataList.isEmpty()) {
                templateDataItemDataRepository.saveAll(itemDataList);
            }
        }
        return RestResultFactory.createSuccessResult(item);
    }

    /**
     * 更新
     * @param itemvo
     * @return
     */
    @Override
    @Transactional
    public synchronized RestResult<DataItemVO> updateDataItem(DataItemVO itemvo) {
        TemplateDataItem dataitem = itemvo.getDataItem();
        if (dataitem != null) {
            TemplateDataItem dbItem = templateDataItemRepository.findById(dataitem.getId()).orElse(null);
            if (dbItem != null) {

                BeanUtils.copyProperties(dataitem, dbItem);
                templateDataItemRepository.save(dbItem);
            } else {
                dbItem = templateDataItemRepository.save(dataitem);
                dataitem.setId(dbItem.getId());
            }
            List<TemplateDataItemData> dataList = itemvo.getDataItemDataList();
            TemplateDataItemData dbData = null;
            templateDataItemDataRepository.deleteByDataIemId(dataitem.getId());
            if (dataList != null && !dataList.isEmpty()) {
                templateDataItemDataRepository.saveAll(dataList);
            }
        }
        return RestResultFactory.createSuccessResult(itemvo);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    @Transactional
    public RestResult<String> deleteDateItem(String id) {
        if (id != null) {
            TemplateDataItem item = templateDataItemRepository.findById(id).orElse(null);
            if (item != null) {
                templateDataItemRepository.deleteById(id);
                templateDataItemDataRepository.deleteByDataIemId(id);
            }
        }
        return RestResultFactory.createSuccessResult(null);
    }

    @Autowired
    private InstanceFormRepository instanceFormRepository;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Autowired
    private ProcessMethodUrlProperties processMethodUrlProperties;

    @Autowired
    private InstanceFormItemRepository instanceFormItemRepository;

    /**
     * 获取一个表单实例
     * @param ins_node_id
     * @param
     * @return
     */
    @Override
    @Transactional
    public RestResult getOneForInstance(Integer ins_node_id, Integer ins_proc_id,Integer procId,Integer nodeId) {
        InstanceForm instanceForm = instanceFormRepository.findByInsProcIdAndInsNodeId(ins_proc_id, ins_node_id);
        Map<String,Object> map=new HashMap<String,Object>();
        if (instanceForm == null ){
            //如果没有，就创建一个
            InstanceForm instanceForms = new InstanceForm();
            instanceForms.setCreateTime(new Date());
            //  查找当前用户信息，进行设置进去
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal == null) {
                return RestResultFactory.createErrorResult("无法获取当前用户信息");
            }
            CustomUser user = null;
            if (principal instanceof CustomUser) {
                user = (CustomUser) principal;
            }
            if (user == null) {
                return RestResultFactory.createErrorResult("无法获取当前用户信息");
            }
            DbSysUser voUserMenu = user.getVoUserMenu();
            instanceForms.setCreateUser(voUserMenu.getId());
            instanceForms.setCreateGroup(voUserMenu.getDeptId());
            //获取关联表单id,调用1.2.8/api/process-engine/query_node_attr/{proc_id}，根据流程id查询，然后获取相应的节点id
            instanceForms.setId(Long.parseLong(ins_node_id+""));
            instanceForms.setInsNodeId(ins_node_id);
            instanceForms.setInsProcId(ins_proc_id); //前端传递过来的
            instanceForms.setNodeId(nodeId); //这个前端传递过来
            instanceForms.setProcId(procId); //前端传递过来
            //前面四个属性，前端必须传递过来
            RestTemplate restTemplate=getRestClientService();
            List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
            ResponseEntity<List> objectResponseEntity = restTemplate.getForEntity(methodList.get(7).getUrl()+"/"+procId,List.class);
            HttpStatus statusCode = objectResponseEntity.getStatusCode();
            if (!statusCode.equals(HttpStatus.OK)){
                return RestResultFactory.createErrorResult("接口调用失败，请重新尝试");
            }
            List list = objectResponseEntity.getBody();
            Long formId=null;
            for (Object nodeObject : list) {
                Map map1= (Map) nodeObject;
                String nodeId1 = map1.get("nodeId").toString();
                if (nodeId1.equals(nodeId+"")){
                    String formId1 = map1.get("formId").toString();
                    formId=Long.parseLong(formId1);
                    break;
                }
            }
            instanceForms.setFormId(Integer.parseInt(formId+""));
            Form form = formRepo.findById(formId).orElse(null);
            if (form == null){
                return RestResultFactory.createErrorResult("表单定义加载失败，请重试");
            }
            instanceForms.setFormKey(form.getFormKey()); //从表单定义中能够拿到
            instanceForms.setVerNumber(1);
            instanceFormRepository.save(instanceForms);
            //实例创建完毕，然后使用表单定义，对前端进行返回，构建表单
            buildAllFormItem(form);
            map.put("form",form);
            map.put("instanceOfForm",instanceForms);
            map.put("formData",new ArrayList());
        }else {
            map.put("instanceOfForm",instanceForm);
            Integer formId = instanceForm.getFormId();
            Form form = formRepo.findById(Long.parseLong(formId+"")).orElse(null);
            if (form == null){
                return RestResultFactory.createErrorResult("表单定义加载失败，请重试");
            }
            //直接返回当前库中的表单实例
            buildAllFormItem(form);
            map.put("form",form);
            //表单构建完成，开始查询相应的填写的数据
            List<InstanceFormItem> instanceFormItems = instanceFormItemRepository.findAllByFormInstId(Integer.parseInt(instanceForm.getId() + ""));
            map.put("formData",instanceFormItems);
        }
        return RestResultFactory.createSuccessResult(map);
    }

    /**
     * 进行数据的保存
     * @param instanceFormItems
     * @param form_inst_id
     * @param ins_proc_id
     * @param ins_node_id
     * @param proc_id
     * @return
     */
    @Override
    @Transactional
    public RestResult saveInstanceDataOfForm(List<InstanceFormItem> instanceFormItems, Integer form_inst_id, Integer ins_proc_id, Integer ins_node_id, Integer proc_id) {
        //遍历，然后进行添加或者修改,直径使用savaall即可，没有就会添加，有就会执行修改
        for (InstanceFormItem instanceFormItem : instanceFormItems) {
            instanceFormItem.setFormInstId(form_inst_id);
            instanceFormItem.setInsProcId(ins_proc_id);
            instanceFormItem.setInsNodeId(ins_node_id);
            //添加了流程id和默认的版本号
            instanceFormItem.setProcId(proc_id);
            instanceFormItem.setVerNumber(1);
        }
        try {
            instanceFormItemRepository.saveAll(instanceFormItems);
            return RestResultFactory.createSuccessResult("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createErrorResult("保存失败");
        }
    }

    @Override
    public RestResult getCompletedForm(String ins_proc_id) {
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<List> objectResponseEntity = restTemplate.getForEntity(methodList.get(15).getUrl()+"/"+ins_proc_id,List.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
        List list = objectResponseEntity.getBody();
        if (list.size() == 0){
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
        //创建一个list，用于装前面的元素
        List<String> nodeIdList=new ArrayList<>();
        for (Object o : list) {
            Map map= (Map) o;
            //如果没有到等于这个节点的所有节点，说明已经是完成的，可以查看的节点id
//            if (!ins_proc_id.equals(map.get("toNodeId").toString())){
                nodeIdList.add(map.get("toNodeId").toString());
//            }
        }
        //根据节点id获取相应的表单,并创建一个list用于装最终的结果,根据流程实例id和流程节点id进行查询,获取关联的表单实例，然后根据表单实例中的表单id查询相应的表单定义
        List<Map> resultList=new ArrayList<>();
        for (String s : nodeIdList) {
            Map<String,Object> oneMap=new HashMap<>();
            InstanceForm instanceForm = instanceFormRepository.findFirstByInsProcIdAndAndNodeId(Integer.parseInt(ins_proc_id), Integer.parseInt(s));
            //构建表单定义
            if (instanceForm == null){
                continue;
            }
            Form form1 = formRepo.findById(Long.parseLong(instanceForm.getFormId() + "")).orElse(null);
            if (form1 == null){
                return RestResultFactory.createErrorResult("表单定义构建失败，请重试");
            }
            Form form = new Form();
            BeanUtils.copyProperties(form1,form);
            buildAllFormItem(form);
            oneMap.put("formDefine",form);
            oneMap.put("formInstance",instanceForm);
            //构建表单实例的数据
            List<InstanceFormItem> itemList = instanceFormItemRepository.findAllByFormInstId(Integer.parseInt(instanceForm.getId() + ""));
            oneMap.put("itemList",itemList);
            resultList.add(oneMap);
        }
        return RestResultFactory.createSuccessResult(resultList);
    }

    /**
     * 获取地理纬度信息
     * @param idStr
     * @return
     */
    @Override
    public RestResult getGeographyInfo(String idStr) {
        List lists=new ArrayList();
        if (idStr.equals("100000")){
        //查找全部的省
           lists = commonProvinceRepository.findAllNoCountry();
        }else if (idStr.substring(2).equals("0000")){
         //查找全部的市
            lists = commonRegionRepository.findAllByProvinceId(BigDecimal.valueOf(Long.parseLong(idStr)));
        }else if (idStr.substring(4).equals("00")){
        //查找全部的县
            lists = commonCityRepository.findAllByRegionId(BigDecimal.valueOf(Long.parseLong(idStr)));
        }
        return RestResultFactory.createSuccessResult(lists);
    }

    @Autowired
    private EntityManager entityManager;
    /**
     * 根据表名和列名进行数据的查找
     * @param tableAndColumn
     * @return
     */
    @Override
    public RestResult findDataByTableAndColumnName(TableAndColumn tableAndColumn) {
        String tableName = tableAndColumn.getTableName();
        if (tableName == null || "".equals(tableName.trim())){
            return RestResultFactory.createErrorResult("没有传表名");
        }
        List<String> columnNames = tableAndColumn.getColumnName();
        if (columnNames == null || columnNames.size() == 0){
            return RestResultFactory.createErrorResult("没有传列名");
        }
        Query nativeQuery = entityManager.createNativeQuery("select table_name from information_schema.tables");
        List resultList = nativeQuery.getResultList();
        if (resultList == null) {
            return RestResultFactory.createErrorResult("数据库错误，请重新尝试");
        }
        Boolean flag=false;
        for (Object o : resultList) {
            if (o.toString().equals(tableName)){
                flag=true;
            }
        }
        if (!flag){
            return RestResultFactory.createErrorResult("数据库中没有相关的表");
        }
        StringBuilder stringBuilder=new StringBuilder("select  ");
        int num = columnNames.size();
        for (int i = 0; i < columnNames.size(); i++) {
         stringBuilder.append(columnNames.get(i));
         if (i < num-1){
             stringBuilder.append(" , ");
         }else {
             stringBuilder.append(" ");
         }
        }
        stringBuilder.append(" from "+tableName);
        String s = stringBuilder.toString();
        Query query = entityManager.createNativeQuery(s);
        List resultList1 = query.getResultList();
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("data",resultList1);
        resultMap.put("columnOrder",columnNames);
        return RestResultFactory.createSuccessResult(resultMap);
    }

    /**
     * 获取省市县联结的数据结构
     * @return
     */
    @Override
    public RestResult getGeographyInfoNoStructure() {
        List<Map> resultList = new ArrayList<>();
        List<CommonProvince> allProvince = commonProvinceRepository.findAll();
        for (CommonProvince commonProvince : allProvince) {
            HashMap<String, Object> oneResult = new HashMap<>();
            if (commonProvince.getId().intValue() == 100000){
                oneResult.put("id",commonProvince.getId().toString());
                oneResult.put("name",commonProvince.getName());
                oneResult.put("pId","-1");
            }else {
                oneResult.put("id",commonProvince.getId().toString());
                oneResult.put("name",commonProvince.getName());
                oneResult.put("pId","100000");
            }
            resultList.add(oneResult);
        }
        List<CommonRegion> allRegion = commonRegionRepository.findAll();
        for (CommonRegion commonRegion : allRegion) {
            HashMap<String, Object> oneResult = new HashMap<>();
            oneResult.put("id",commonRegion.getId().toString());
            oneResult.put("name",commonRegion.getName());
            oneResult.put("pId",commonRegion.getProvinceId().toString());
            resultList.add(oneResult);
        }
        List<CommonCity> allCity = commonCityRepository.findAll();
        for (CommonCity commonCity : allCity) {
            HashMap<String, Object> oneResult = new HashMap<>();
            oneResult.put("id",commonCity.getId().toString());
            oneResult.put("name",commonCity.getName());
            oneResult.put("pId",commonCity.getRegionId().toString());
            resultList.add(oneResult);
        }
        return RestResultFactory.createSuccessResult(resultList);
    }

    @Autowired
    private DbsysUserRepository dbsysUserRepository;
    @Autowired
    private DbSysDepartmentRepository dbSysDepartmentRepository;
    @Autowired
    private DbSysRoleRepository dbSysRoleRepository;

    /**
     * 获取表单的操作人员的信息
     * @return
     */
    @Override
    public RestResult getOperationUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            RestResult failedResult = RestResultFactory.createErrorResult("无法获取当前用户信息");
            failedResult.setStatus(1);
            return failedResult;
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        if (user == null) {
            RestResult failedResult = RestResultFactory.createErrorResult("无法获取当前用户信息");
            failedResult.setStatus(1);
            return failedResult;
        }
        DbSysUser dbSysUser = dbsysUserRepository.findById(user.getVoUserMenu().getId()).orElse(null);
        if (dbSysUser == null) {
            RestResult failedResult = RestResultFactory.createErrorResult("无法获取当前用户信息");
            failedResult.setStatus(1);
            return failedResult;
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("operationUser",dbSysUser.getRealName());
        map.put("telephone",dbSysUser.getMobile());
        DbSysDepartment dbSysDepartment = dbSysDepartmentRepository.findById(dbSysUser.getDeptId()).orElse(null);
        if (dbSysDepartment == null){
            map.put("department","");
        }else {
            map.put("department",dbSysDepartment.getDeptName());
        }
        List<DbSysRole> dbSysRoles = dbSysRoleRepository.findAllByUserId(dbSysUser.getId());
        String roleStr = "";
        for (int i = 0; i < dbSysRoles.size(); i++) {
            roleStr = roleStr + dbSysRoles.get(i).getRoleName();
            if (i != (dbSysRoles.size()-1)){
                roleStr = roleStr +",";
            }
        }
        map.put("role",roleStr);
        return RestResultFactory.createSuccessResult(map);
    }

    public RestTemplate getRestClientService() {
        RestTemplate restTemplate = restTemplateBuilder
//                .basicAuthorization("oss", "oss123")
                .setConnectTimeout(3000)
                .setReadTimeout(5000)
                .build();
        return restTemplate;
    }

    /**
     * 复制数据项到我的数据项
     * @param formItem
     * @return
     */
//    @Override
    @Transactional
    public RestResult copyTemplateItemsForMe(FormItem formItem) {
        //复制过来就不是模板了，就是私人可以修改的定义了
        try {
            formItem.setIsTemplate(0);
            formItem.setLayerId(-1L);
            formItemRepo.save(formItem);
            System.out.println(formItem);
            List<FormItemFieldData> dataSourceList = formItem.getDataSourceList();
            if (dataSourceList != null){
                for (FormItemFieldData formItemFieldData : dataSourceList) {
                    formItemFieldData.setFieldId(formItem.getId());
                    formItemFieldDataRepository.save(formItemFieldData);
                }
            }
            return RestResultFactory.createSuccessResult("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createErrorResult("复制失败，请重新尝试");
        }
    }

    /**
     * 获取所有的表单模板节点
     * @return
     */
//    @Override
//    public RestResult getAllFormNode() {
//        List<FormFormItem> list = formFormItemRepo.getAllFormNode();
//        if (list != null && list.size() >0){
//            for (FormFormItem formFormItem : list) {
//                buildFormFormItem(formFormItem);
//            }
//        }
//        return RestResultFactory.createSuccessResult(list);
//    }

    /**
     * 获取所有的表单模板
     * @return
     */
//    @Override
//    public RestResult getFormTemplate1() {
//        List<Form> forms = formRepo.getFormTemplate();
//        //获取表单之后封装表单条目和调单数据项以及数据源
//        if (forms != null && forms.size() >1){
//            for (Form form : forms) {
//                buildAllFormItem(form);
//            }
//        }
//        return RestResultFactory.createSuccessResult(forms);
//    }

    /**
     * 复制表单模板到我的模板库
     * @param form
     * @return
     */
//    @Transactional
//    @Override
//    public RestResult copyFormTemplate(Form form) {
//        form.setIsTemplate(0);
//        form.setId(null);
//        formRepo.save(form);
//        List<FormFormItem> formFormItemList = form.getFormFormItemList();
//        if (formFormItemList != null && formFormItemList.size() > 0){
//            for (FormFormItem formFormItem : formFormItemList) {
//                formFormItem.setId(null);
//                formFormItem.setFormId(form.getId());
//                formFormItem.setIsTemplate(0);
//                formFormItemRepo.save(formFormItem);
//                List<FormItem> formItemList = formFormItem.getFormItemList();
//                if (formItemList != null && formItemList.size() >0){
//                    for (FormItem formItem : formItemList) {
//                        formItem.setId(null);
//                        formItem.setIsTemplate(0);
//                        formItem.setLayerId(formFormItem.getId());
//                        formItemRepo.save(formItem);
//                        List<FormItemFieldData> dataSourceList = formItem.getDataSourceList();
//                        if (dataSourceList != null && dataSourceList.size() > 0){
//                            for (FormItemFieldData formItemFieldData : dataSourceList) {
//                                formItemFieldData.setId(null);
//                                formItemFieldData.setFieldId(formItem.getId());
//                                formItemFieldDataRepository.save(formItemFieldData);
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return RestResultFactory.createSuccessResult("复制成功");
//    }

//    @Override
//    public RestResult getMyDataItem(String itemName) {
//        Specification<FormItem> specification=new Specification() {
//            @Override
//            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
//                //如果有名称就用名称，没有就查找非模板==0，且不属于上层的数据项==-1
//                List<Predicate> predicates =new ArrayList<>();
//                predicates.add(criteriaBuilder.equal(root.get("isTemplate").as(Integer.class),0));
//                predicates.add(criteriaBuilder.equal(root.get("layerId").as(Long.class),-1));
//                if (itemName != null && !itemName.trim().equals("")){
//                    predicates.add(criteriaBuilder.like(root.get("name").as(String.class),"%"+itemName+"%"));
//                }
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        };
//        List<FormItem> formItems = formItemRepo.findAll(specification, new Sort(Sort.Direction.DESC, "createTime"));
//        if (formItems != null && formItems.size() >0){
//            for (FormItem item : formItems) {
//                List<FormItemFieldData> dataList = formItemFieldDataRepository.findByFieldId(item.getId());
//                if (dataList != null) {
//                    for (FormItemFieldData data : dataList) {
//                        item.addFormItemData(data);
//                    }
//                }
//            }
//        }
//        return RestResultFactory.createSuccessResult(formItems);
//    }*/

    /**
     * 递归获取所有的FormItem
     *
     * @param form
     * @return
     */
    @Transactional(readOnly = true)
    public Form buildAllFormItem(Form form) {
        if (form == null)
            return null;

        List<FormFormItem> formFormItemList =  formFormItemRepo.findByFormId(form.getId());
        if (formFormItemList != null) {
            for(FormFormItem ffi: formFormItemList) {
                FormFormItem formFormItem = new FormFormItem();
                BeanUtils.copyProperties(ffi,formFormItem);
                this.buildFormFormItem(formFormItem);
                form.addFormFormItem(formFormItem);
            }
        }
        return form;
    }
    public FormFormItem buildFormFormItem(FormFormItem ffi) {
        List<FormItem> formItemList = formItemRepo.findByLayerId(ffi.getId());
        if (formItemList != null) {
            for (FormItem item: formItemList) {
                FormItem formItem = new FormItem();
                BeanUtils.copyProperties(item,formItem);
                ffi.addFormItem(formItem);
                List<FormItemFieldData> dataList = formItemFieldDataRepository.findByFieldId(item.getId());
                if (dataList != null) {
                    for (FormItemFieldData data: dataList) {
                        formItem.addFormItemData(data);
                    }
                }
            }
        }
        return  ffi;
    }
}
