package com.tongtech.controller;

import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.common.BusinessException;
import com.tongtech.common.vo.*;
import com.tongtech.dao.entity.Form;
import com.tongtech.dao.entity.FormFormItem;
import com.tongtech.dao.entity.FormItem;
import com.tongtech.dao.entity.InstanceFormItem;
import com.tongtech.dao.repository.InstanceFormRepository;
import com.tongtech.service.form.FormService;
import com.tongtech.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/template")
public class FormController {

    @Autowired
    private FormService formService;

    @Autowired
    private StorageService storageService;

/**
     *  获取表单模板
     * @explain 获取所有的isTemplate是1的根据isTemplate 是否是模板获取  1表示是模板*/


    @GetMapping("/template_form")
    public RestResult getFormTemplate() {
        List<Form> result=null;
        result = formService.findFormTemplates();
        return RestResultFactory.createSuccessResult(result);
    }

    /*  从模板中拷贝表单 formId，name，isTemplate,description
	    srcId: 源表单id
	    newName: 新表单名称
	    type:    是否模板
	    @explain
	 **/
    @PostMapping("/template_form")
    public RestResult copyFormFromTemplate(@RequestParam(required = true) Long formId,
                                     @RequestParam(required = true) String name,
                                     @RequestParam(required = false) Integer isTemplate,
                                     @RequestParam(required = false) String description) {
        Form newForm = formService.copyFormFromTemplate(formId,name,isTemplate,description);
        return RestResultFactory.createSuccessResult(newForm);
    }

    /**
     *  获取公共表单模板字段
     */
    @GetMapping("/form_items")
    public RestResult getTemplateFormItems(@RequestParam(required = false) String formId) {
        List<FormItem> result=null;
        Long id = -1L;
        if (formId != null) {
            id = Long.parseLong(formId);
        }
        result = formService.getTemplateItems(id);
        return RestResultFactory.createSuccessResult(result);
    }


    /**
     * 获取模板数据项
     * @param itemName
     * @param itemType
     * @return
     */
    @GetMapping("/form_items1")
    public RestResult getTemplateItems(@RequestParam(required = false) String itemName, @RequestParam(required = false)String itemType,
                                           @RequestParam(defaultValue = "2") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum){

        Page<FormItem> page = formService.getTemplateItemes(itemName, itemType, pageSize, pageNum);
        return RestResultFactory.createSuccessResult(page);
    }

    /**
     * 复制数据项到我的数据项,传送参数的时候，不要传送id，由于是新数据，前面也不直到id
     *//*
    @PostMapping("/copyTemplateItem")
    public RestResult copyTemplateItemsForMe(@RequestBody FormItem formItem){
        RestResult restResult= formService.copyTemplateItemsForMe(formItem);
        return restResult;
    }

    *//**
     * 1.2.1.2	获取表单层级数据
     *
     * @return
     */
    @GetMapping("/form/querylist")
    public RestResult<Form> findByFormId(@RequestParam Long id ) {
        Form form = formService.findOneForm(id);
        return RestResultFactory.createSuccessResult(form);
    }

    /**
     * 创建表单层级数据
     * @param ffi
     * @return FormFormItem
     */
    @PostMapping("/formFormItem")
    public RestResult<FormFormItem> createFormFormItem(
            @RequestParam Long formId,
            @RequestBody FormFormItem ffi){
        ffi = formService.createFormFormItem(formId, ffi);
        return RestResultFactory.createSuccessResult(ffi);
    }

    // 更新表单层级关系，ID不能更新.
    @PutMapping("/formFormItem")
    public RestResult<FormFormItem> updateFormFormItem(
            @RequestBody FormFormItem ffi){
        ffi = formService.updateFormFormItem(ffi);
        if (ffi == null){
            return RestResultFactory.createErrorResult("更新失败");
        }
        return RestResultFactory.createSuccessResult(ffi);
    }

    // 删除表单层级关系
    @DeleteMapping("/formFormItem")
    public RestResult<FormFormItem> deleteFormFormItem( @RequestParam Long id) {
        formService.deleteFormFormItem(id);
        return RestResultFactory.createSuccessResult();
    }

    /**
     * 创建表单
     * @param form
     * @return
     */
    @PostMapping("/form")
    public RestResult<Form> createForm(@RequestBody Form form){
        try {
            form = formService.createForm(form);
            return RestResultFactory.createSuccessResult(form);
        } catch (BusinessException e) {
            form = null;
            return RestResultFactory.createResult(3,form, e.getMessage());
        }
    }


    /**
     * 修改表单生成新的版本
     * @param form
     * @return
     */
    @PutMapping("/form")
    public RestResult<Form> updateForm(@RequestBody Form form){
        try {
            form = formService.updateForm(form);
            return RestResultFactory.createSuccessResult(form);
        } catch (BusinessException e) {
            form = null;
            return RestResultFactory.createResult(3,form, e.getMessage());
        }
    }

    /**
     * 删除表单
     * @param formId
     * @return
     */
    @DeleteMapping("/form")
    public RestResult<Form> delForm(@RequestParam Long formId){
        try {
            formService.delForm(formId);
            return RestResultFactory.createSuccessResult(null);
        } catch (BusinessException e) {
            return RestResultFactory.createResult(3,null, e.getMessage());
        }
    }

    /**
     *  获取节点模板
     */
    @GetMapping("/template_section")
    public RestResult getSectionTemplate() {
        List<FormFormItem> result=null;
        result = formService.findSectionTemplates();
        return RestResultFactory.createSuccessResult(result);
    }

    /**
     * 复制节点模板
     * @param ffiId
     * @param destFormId
     * @param destFfiId
     * @param destFfiOrder
     * @param destName
     * @param isTemplate
     * @return
     */
    @PostMapping("/template_section")
    public RestResult copyFormSectionFromTemplate(@RequestParam(required = true) Long ffiId,
                                                    @RequestParam(required = true) Long destFormId,
                                                    @RequestParam(required = true) Long destFfiId,
                                                    @RequestParam(required = true) Long destFfiOrder,
                                                    @RequestParam(required = false) String destName,
                                                    @RequestParam(required = false) Integer isTemplate) {
        FormFormItem newFormSection =
                formService.copyFormSectionFromTemplate(ffiId,destFormId,destFfiId,destFfiOrder,destName,isTemplate);
        return RestResultFactory.createSuccessResult(newFormSection);
    }

    /**
     * 获取所有Form以及FormItem
     *
     * @return
     */
    @GetMapping("/form")
    public RestResult<Page<Form>> findAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<Form> list = formService.findAll(page, size);
        return RestResultFactory.createSuccessResult(list);
    }

    /**
     * 获取所有的Form，不包括FormItem
     * @return
     */
    @GetMapping(path = "/form/simple")
    public RestResult<List<Form>> findAllSimple(@RequestParam(required = false) Long diseaseId) {
//        //获取当前用户所属的部门
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal == null) {
//            return RestResultFactory.createErrorResult("无法获取当前用户信息");
//        }
//        CustomUser user = null;
//        if (principal instanceof CustomUser) {
//            user = (CustomUser) principal;
//        }
//        if (user == null) {
//            return RestResultFactory.createErrorResult("无法获取当前用户信息");
//        }
//        DbSysUser voUserMenu = user.getVoUserMenu();
//        Integer departmentId=voUserMenu.getDeptId();
        List<Form> list = formService.findAllSimple();
        List<Long> idList=new ArrayList<>();
        List<Form> resultList=new ArrayList<>();
        for (Form form : list) {
            if (!idList.contains(form.getId())){
                idList.add(form.getId());
                resultList.add(form);
            }
        }
        return RestResultFactory.createSuccessResult(resultList);
    }

    /**
     * 修改FormItem
     * @param item
     * @return
     */
    @PutMapping(path = "/item")
    public RestResult<FormItem> updateFormItem(@RequestBody FormItem item) {
        item = formService.updateFormItem(item);
        return RestResultFactory.createSuccessResult(item);
    }

    /**
     * 获取此人的所有FormItem
     * @param page
     * @param size
     * @return
     */
    @GetMapping(path = "/formItem")
    public RestResult<Page<FormItem>> findMyFormItem(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size){
//        Long userId = UserPrincipal.getSessionUser().getUser().getId();
        //获取当前用户所在的部门
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
        Integer deptId=voUserMenu.getDeptId();
        Page<FormItem> list = formService.findMyFormItem(deptId, page, size);
        return RestResultFactory.createSuccessResult(list);
    }

    /**
     * 获取此人的所有Form
     * @param page
     * @param size
     * @return
     */
    @GetMapping(path = "/form1")
    public RestResult<Page<Form>> findMyForm(
            @RequestParam(required = false,defaultValue = "1") int page,
            @RequestParam(required = false,defaultValue = "20") int size,
            @RequestParam(required = false) String tableName){
//        SpdbUser user = UserPrincipal.getSessionUser().getUser();
//        Boolean isAdmin = false;
//        for (Role role : user.getRoleList() ) {
//            if (role.getId() == 1) {
//                isAdmin = true;
//                break;
//            }
//            if (role.getName().indexOf("系统管理") >= 0) {
//                isAdmin = true;
//                break;
//            }
//        }
//        Long userId = user.getId();
        //根据当前用户所属部门，获取当前用户所关联的表单
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
        Integer deptId=voUserMenu.getDeptId();
        Page<Form> list = formService.findMyForm(deptId, page, size,tableName);
        return RestResultFactory.createSuccessResult(list);
    }

    /**
     * 获取此人的所有节点
     * @param page
     * @param size
     * @return
     */
    @GetMapping(path = "/section")
    public RestResult<Page<FormFormItem>> findMySection(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
//        Long userId = UserPrincipal.getSessionUser().getUser().getId();
        //根据部门id获取当前用户所有的节点 ，由于没有做节点和部门的关联，因此目前这个暂不支持
//        Page<FormFormItem> pageData = formFormItemService.findMySection(userId, page, size);
//        return RestResultFactory.createSuccessResult(pageData);
        return null;
    }

    // 获取一个节点
    @GetMapping(path = "/section/{id}")
    public RestResult<FormFormItem> findOneSection(@PathVariable Long id) {
        FormFormItem item = formService.findOneSection(id);
        return RestResultFactory.createSuccessResult(item);
    }

    /**
     * 导入表单
     * @param file
     * @return
     */
    @PostMapping("/import_form")
//    @Transactional(value = "one_tm", transactionManager = "one_tm")
    public String importForm(@RequestParam("file") MultipartFile file) {
        List<String> failList = new ArrayList<>(0);
//        Long user_id = UserPrincipal.getSessionUser().getUser().getId();
//        String username = UserPrincipal.getSessionUser().getUser().getUsername();
//        Long userId = UserPrincipal.getSessionUser().getUser().getId();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return "无法获取当前用户信息";
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        if (user == null) {
            return "无法获取当前用户信息";
        }
        DbSysUser voUserMenu = user.getVoUserMenu();
        //获取当前用户
        String username=""+voUserMenu.getLoginName();
        Integer userId=voUserMenu.getId();
        String pre = username + "FORM_" + userId + "_";
        Path path = formService.store(pre, file);
        String rtn = formService.form2db(path, userId, failList);
        storageService.deleteRecursively(pre);
        return rtn;
    }

    // 倒出表单
    @PostMapping("/export_form")
    @ResponseBody
    @Transactional(value = "one_tm", transactionManager = "one_tm")
    public ResponseEntity<Resource> exportFormDef(@RequestParam("formId") Long formId) {
        String rtn = "";
//        UserPrincipal baseUser = UserPrincipal.getSessionUser();

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
        DbSysUser baseUser = user.getVoUserMenu();
        //获取当前用户
        Form form = formService.findOneForm(formId);
        //构建文件名字
        String filename = "spdb_form_" + baseUser.getLoginName() + "_" + form.getId() + ".xlsx";
//        String filename = "emos_form_" + "xx" + "_" + form.getId() + ".xlsx";

        Path path = storageService.load(filename);
        formService.expFormDef(path, form);
        Resource file = storageService.loadAsResource(filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);

    }

    @PostMapping("/formFormItem/updateOrders")
    public RestResult<String> updateFormFormItemOrders(
            @RequestParam Long id1,
            @RequestParam Long order1,
            @RequestParam Long  id2,
            @RequestParam Long order2){
        formService.updateFormFormItemOrders(id1,order1,id2,order2);
        return RestResultFactory.createSuccessResult("");
    }

    @GetMapping("/data_item/query")
    public RestResult<Page<DataItemVO>> getAll(
            @RequestParam(defaultValue = "", required = false) String name,
            @RequestParam(defaultValue = "-1", required = false) Long classId,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "20", required = false) int size) {
        Page<DataItemVO> page1 = formService.getAll(name, classId, page, size);
        return RestResultFactory.createSuccessResult(page1);
    }

    @PostMapping("/data_item")
    public RestResult<DataItemVO> createDataItem(@RequestBody DataItemVO item) {
        return formService.createDataItem(item);
    }

    @PutMapping("/data_item")
    public RestResult<DataItemVO> updateDataItem(@RequestBody DataItemVO itemvo) {
        return formService.updateDataItem(itemvo);
    }

    @DeleteMapping("/data_item/{id}")
    public RestResult<String> deleteDateItem(@PathVariable  String id) {
        return formService.deleteDateItem(id);
    }

    /**
     * 获取一个表单实例，有就加载，没有就创建一个返回
     * ins_proc_id      int                  comment '流程实例id',
     *   ins_node_id      int                  comment '节点实例id',
     *   根据这两个字段，能够确定唯一的表单
     * @return
     */

    @GetMapping("/getOneFormInstance")
    public RestResult getOneFormInstance(@RequestParam("ins_proc_id") Integer ins_proc_id,
                                         @RequestParam("ins_node_id") Integer ins_node_id,
                                         @RequestParam("procId") Integer procId,
                                         @RequestParam("nodeId") Integer nodeId) {
        if (ins_node_id == null || ins_proc_id == null){
            return RestResultFactory.createErrorResult("参数不全，请重试");
        }
        RestResult restResult =  formService.getOneForInstance(ins_node_id,ins_proc_id,procId,nodeId);
        return restResult;
    }

    /**8210，15机器
     * 对一个表单实例的数据进行保存
     *  form_inst_id     int                  comment '表单实例ID',
     *   ins_proc_id      int                  comment '流程实例id',
     *   ins_node_id      int                  comment '节点实例id',
     *   保存区分更新还是添加
     */
    @PostMapping("/saveInstanceDataOfForm")
    public RestResult saveInstanceDataOfForm(@RequestBody List<InstanceFormItem> instanceFormItems,
                                             @RequestParam("form_inst_id") Integer form_inst_id,
                                             @RequestParam("ins_proc_id") Integer  ins_proc_id,
                                             @RequestParam("ins_node_id") Integer  ins_node_id,
                                             @RequestParam("proc_id") Integer  proc_id){
        if (instanceFormItems == null || instanceFormItems.size() == 0){
            return RestResultFactory.createErrorResult("没有传递数据进行保存");
        }
        RestResult restResult = formService.saveInstanceDataOfForm(instanceFormItems,form_inst_id, ins_proc_id, ins_node_id,proc_id);
        return restResult;
    }

    /**
     *获取前面人已经填写的表单，根据连接接口来进行填写，如果排在这个人之前的节点，可定是都已经填完的，然后，加载这些表单实例和相应的定义，仅查看
     */
    @GetMapping("/getCompletedForm/{ins_proc_id}")
    public RestResult getCompletedForm(@PathVariable(name="ins_proc_id") String ins_proc_id){

        RestResult restResult = formService.getCompletedForm(ins_proc_id);
        return restResult;
    }

    /**
     * 获取地理信息
     * @param idStr
     * @return
     */
    @GetMapping("/getGeographyInfo")
    public RestResult getGeographyInfo(@RequestParam String idStr){
        if (idStr == null || idStr.trim().equals("")){
            return RestResultFactory.createErrorResult("传递的参数为空");
        }
        RestResult restResult=formService.getGeographyInfo(idStr);
        return restResult;
    }

    /**
     * 返回新的级联，对于省市县联动
     * @return
     */
    @GetMapping("/getGeographyInfo1")
    public RestResult getGeographyInfo1(){
        RestResult restResult = formService.getGeographyInfoNoStructure();
        return restResult;
    }

    /**
     * 获取当前表单的操作人信息
     * @return
     */
    @GetMapping("/getOperationUser")
    public RestResult getOperationUser(){
        RestResult restResult = formService.getOperationUser();
        return restResult;
    }

    /**
     * 获取所有的表单节点模板
     * @return
     *//*
    @GetMapping("/getAllFormNode")
    public RestResult getAllFormNode(){
        RestResult restResult = formService.getAllFormNode();
        return restResult;
    }


    *//**
     * 获取所有的表单模板
     *//*
    @GetMapping("/getFormTemplate")
    public RestResult getFormTemplate1(){

        RestResult restResult = formService.getFormTemplate1();
        return restResult;
    }

    *//**
     * 复制表单模板到我的模板库，不需要所有的id
     * @param form
     * @return
     *//*
    @PostMapping("/copyFormTemplate")
    public RestResult copyFormTemplate(@RequestBody Form form){
        RestResult restResult = formService.copyFormTemplate(form);
        return restResult;
    }

    *//**
     * 获取我的数据项
     * @param itemName
     * @return
     *//*
    @GetMapping("/getMyDataItem")
    public RestResult getMyDataItem(@RequestParam(required = false) String itemName){
        RestResult restResult = formService.getMyDataItem(itemName);
        return restResult;
    }

    *//**
     * 删除我的数据项
     * @return
     *//*
    @DeleteMapping("/deleteMyDataItems")
    public RestResult deleteMyDataItem(@RequestParam(required = true) List){

    }*/
    //根据传递的表名和相应的列名，查询相应的结果行
    @PostMapping("/getDictData")
    public RestResult findDateByTableAndColumnName(@RequestBody TableAndColumn tableAndColumn){
        RestResult restResult = formService.findDataByTableAndColumnName(tableAndColumn);
        return restResult;
    }


}
