package com.tongtech.service.form;

import com.tongtech.common.vo.DataItemVO;
import com.tongtech.common.vo.RestResult;
import com.tongtech.common.vo.TableAndColumn;
import com.tongtech.dao.entity.Form;
import com.tongtech.dao.entity.FormFormItem;
import com.tongtech.dao.entity.FormItem;
import com.tongtech.dao.entity.InstanceFormItem;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

public interface FormService {
    /**
     * 获取表单模板
     * @return
     */
    List<Form> findFormTemplates();

    List<FormItem> getTemplateItems(Long id);

    Form findOneForm(Long id);

    FormFormItem createFormFormItem(Long formId, FormFormItem ffi);

    FormFormItem updateFormFormItem(FormFormItem ffi);

    void deleteFormFormItem(Long id);

    Form createForm(Form form);

    Form updateForm(Form form);

    void delForm(Long formId);

    List<FormFormItem> findSectionTemplates();

    Form copyFormFromTemplate(Long formId, String name, Integer isTemplate, String description);

    FormFormItem copyFormSectionFromTemplate(Long ffiId, Long destFormId, Long destFfiId, Long destFfiOrder, String destName, Integer isTemplate);

    Page<Form> findAll(int page, int size);

    List<Form> findAllSimple();

    FormItem updateFormItem(FormItem item);

    Page<FormItem> findMyFormItem(Integer deptId, int page, int size);

    Page<Form> findMyForm(Integer deptId, int page, int size, String tableName);

    FormFormItem findOneSection(Long id);

    Path store(String pre, MultipartFile file);

    String form2db(Path path, Integer userId, List<String> failList);

    String expFormDef(Path path, Form form);

    /**
     * 获取模板数据项
     * @param itemName
     * @param itemType
     * @param pageSize
     * @param pageNum
     * @return
     */
    Page<FormItem> getTemplateItemes(String itemName, String itemType, Integer pageSize, Integer pageNum);

    String updateFormFormItemOrders(Long id1, Long order1, Long id2, Long order2);

    Page<DataItemVO> getAll(String name, Long classId, int page, int size);

    RestResult<DataItemVO> createDataItem(DataItemVO item);

    RestResult<DataItemVO> updateDataItem(DataItemVO itemvo);

    RestResult<String> deleteDateItem(String id);

    RestResult getOneForInstance(Integer ins_node_id, Integer ins_node_id1, Integer procId, Integer nodeId);

    RestResult saveInstanceDataOfForm(List<InstanceFormItem> instanceFormItems, Integer form_inst_id, Integer ins_proc_id, Integer ins_node_id, Integer proc_id);

    RestResult getCompletedForm(String ins_proc_id);

    RestResult getGeographyInfo(String idStr);

    RestResult findDataByTableAndColumnName(TableAndColumn tableAndColumn);

    RestResult getGeographyInfoNoStructure();

    RestResult getOperationUser();

    /**
     * 复制数据项到我的数据项
     * @param formItem
     * @return
     *//*
    RestResult copyTemplateItemsForMe(FormItem formItem);

    *//**
     * 获取所有的表单节点
     * @return
     *//*
    RestResult getAllFormNode();

    *//**
     * 获取所有的表单模板
     * @return
     *//*
    RestResult getFormTemplate1();

    *//**
     * 复制表单模板到我的模板库
     * @param form
     * @return
     *//*
    RestResult copyFormTemplate(Form form);

    *//**
     * 获取我的数据项
     * @param itemName
     * @return
     *//*
    RestResult getMyDataItem(String itemName);*/
}
