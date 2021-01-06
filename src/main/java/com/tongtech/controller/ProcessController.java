package com.tongtech.controller;


import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_department.DbSysDepartmentRepository;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.common.ProcessMethodUrlProperties;
import com.tongtech.common.vo.*;
import com.tongtech.dao.entity.CommonBusiDict;
import com.tongtech.dao.entity.InstanceFormItem;
import com.tongtech.dao.repository.CommonBusiDictRepository;
import com.tongtech.service.form.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@RestController
//@RequestMapping("/api/process")
public class ProcessController {

    @Autowired
    private ProcessMethodUrlProperties processMethodUrlProperties;


    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @GetMapping("/list/getProcessNumber")
    public RestResult getProcessNumber(){

        RestResult<Object> restResult = RestResult.newInstance();
        restResult.setMessage("xxxxx");
        restResult.setStatus(0);
        HashMap<Object, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("total",32);
        objectObjectHashMap.put("publish",23);
        restResult.setData(objectObjectHashMap);
        return restResult;
    }
    @PostMapping("/list/query")
    public  RestResult getListOfProcess(@RequestParam Map map){
        RestResult<Object> restResult = RestResult.newInstance();
        restResult.setMessage("xxxxx");
        restResult.setStatus(0);
        List list=new ArrayList();
        BpmDefProcVo bpmDefProcVo = new BpmDefProcVo();
        bpmDefProcVo.setBusiType("1");
        bpmDefProcVo.setCreateDeptId("2");
        bpmDefProcVo.setCreateLoginId("xxxx");
        bpmDefProcVo.setCreateTime(LocalDateTime.now());
        bpmDefProcVo.setEffectiveTime(LocalDateTime.now());
        bpmDefProcVo.setExpiresTime(LocalDateTime.now());
        bpmDefProcVo.setProcDesc("xxxxddddd");
        bpmDefProcVo.setProcId(3);
        bpmDefProcVo.setProcName("jljl");
        bpmDefProcVo.setReleaseFlag(4);
        bpmDefProcVo.setReleaseTime(LocalDateTime.now());
        bpmDefProcVo.setStdProcId("333");
        bpmDefProcVo.setStdProcVersion("dfdf");
        bpmDefProcVo.setTimeout(4);
        bpmDefProcVo.setVerNumber(5);
        list.add(bpmDefProcVo);
        list.add(bpmDefProcVo);
        Pageable pageable=new PageRequest(1,5);
        Page page=new PageImpl(list,pageable,200);
        restResult.setData(page);
        return restResult;
    }

    public RestTemplate getRestClientService() {
        RestTemplate restTemplate = restTemplateBuilder
//                .basicAuthorization("oss", "oss123")
                .setConnectTimeout(3000)
                .setReadTimeout(120000)
                .build();
        return restTemplate;
    }
    /**
     * /api/process-engine/proc_create
     * POST /api/process-engine/proc_create HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     */
    @PostMapping("/api/process-engine/proc_create")
    public RestResult procCreate(@RequestBody BpmDefProc bpmDefProc){
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
        String userId=voUserMenu.getId()+"";
        String groupId=voUserMenu.getDeptId()+"";
        bpmDefProc.setCreateDeptId(groupId);
        bpmDefProc.setCreateLoginId(userId);
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(0).getUrl(), bpmDefProc, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * POST /api/process-engine/proc_delete/{proc_id} HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     */
    @GetMapping("/api/process-engine/proc_delete")
    public RestResult procDelete(@RequestParam(name="proc_id") String procId){
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(1).getUrl()+"/"+procId, null, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * POST /api/process-engine/proc_modify HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     */
    @PostMapping("/api/process-engine/proc_modify")
    public RestResult procModify(@RequestBody BpmDefProc bpmDefProc){
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(2).getUrl(), bpmDefProc, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * GET /api/process-engine/query_proc HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     * 输入格式
     *
     * @RequestParam(name="page",defaultValue="1") int page,
     * @RequestParam(name="size",defaultValue="20") int size,
     * //流程名称
     * @RequestParam(name="proc_name",defaultValue="") int proc_name,
     * //流程状态 0原始, 1 发布, 2 ALL
     * @RequestParam(name="release_flag",defaultValue="2") int release_flag,
     */
    @GetMapping("/api/process-engine/query_proc")
    public RestResult queryProc(@RequestParam(name="page",defaultValue="1") int page,
                                @RequestParam(name="size",defaultValue="20") int size,
                                @RequestParam(name="proc_name",defaultValue="") String proc_name,
                                @RequestParam(name="release_flag",defaultValue="2") int release_flag){
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        Map<String,Object> param=new HashMap<>();
        param.put("page",page);
        param.put("size",size);
        param.put("release_flag",release_flag);
        String url = methodList.get(3).getUrl()+"?page={page}&size={size}&release_flag={release_flag}";
        if (!("".equals(proc_name))){
            param.put("proc_name",proc_name);
            url=url+"&proc_name={proc_name}";
        }
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(url, Object.class, param);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * POST /api/process-engine/proc_define HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     */
    @PostMapping("/api/process-engine/proc_define")
    public RestResult ProcDefine(@RequestBody String voDefProc){
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(4).getUrl(), voDefProc, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }


    /**
     * POST /api/process-engine/proc_config_node_attr HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     */
    @PostMapping("/api/process-engine/proc_config_node_attr")
    public RestResult ProcConfigNodeAttr(@RequestBody List<VoNodeAttr> voNodeAttrList){
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(5).getUrl(),voNodeAttrList, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * GET /api/process-engine/query_proc_define/{proc_id} HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     */
    @GetMapping("/api/process-engine/query_proc_define/{proc_id}")
    public RestResult queryProcDefine(@PathVariable("proc_id") String procId){
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(methodList.get(6).getUrl()+"/"+procId,Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     *GET /api/process-engine/query_node_attr/{proc_id} HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     */
    @GetMapping("/api/process-engine/query_node_attr/{proc_id}")
    public RestResult queryNodeAttr(@PathVariable("proc_id") String procId){
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(methodList.get(7).getUrl()+"/"+procId,Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }
    /*
     Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return RestResultFactory.createFailedResult("无法获取当前用户信息");
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        if (user == null) {
            return RestResultFactory.createFailedResult("无法获取当前用户信息");
        }
     */
    @Autowired
    private DbSysDepartmentRepository dbSysDepartmentRepository;

    /**
     * 获取当前用户和部门的方法
     * @return
     */
//    public static DbSysUser getCurrentUserAndDepartment(){
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
//    }
    /**
     * GET /api/process-engine/query_todo/{group_id}/{user_id} HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     *
     * 无 body, Param传参数
     * 输入格式
     * @RequestParam(name="page",defaultValue="1") int page,
     * @RequestParam(name="size",defaultValue="20") int size,
     * //流程发起人
     * @RequestParam(name="opt_persion_id", defaultValue="0") int opt_persion_id,
     * //业务标题
     * @RequestParam(name="title", defaultValue="") String title,
     */
    @GetMapping("/api/process-engine/query_todo")
    public RestResult queryToDo(@RequestParam(name="page",defaultValue="1") int page,
                                @RequestParam(name="size",defaultValue="20") int size,
                                @RequestParam(name="opt_person_id", defaultValue="0") int opt_person_id,
                                @RequestParam(name="title", defaultValue="") String title
                                ){
        // 获取当前用户id和部门id进行替换
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
        String userId=voUserMenu.getId()+"";
        String groupId=voUserMenu.getDeptId()+"";
//        String groupId="20";
//        String userId="2";
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        Map map=new HashMap();
        map.put("page",page);
        map.put("size",size);
        map.put("opt_persion_id",opt_person_id);
        map.put("groupId",groupId);
        map.put("userId",userId);
        String url=methodList.get(8).getUrl()+"?page={page}&size={size}&opt_persion_id={opt_persion_id}&group_id={groupId}&user_id={userId}";
        if (!title.equals((""))){
            map.put("title",title);
            url=url+"&title={title}";
        }
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(url,Object.class,map);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     *GET /api/process-engine/query_orig/{opt_persion_id} HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     * 输入格式
     输入格式
     @RequestParam(name = "page", defaultValue = "1") int page,
     @RequestParam(name = "size", defaultValue = "20") int size,
     //1 curr, 2 his
     @RequestParam(name = "flag_curr_his", defaultValue = "1") int flag_curr_his,
     @RequestParam(name = "title", defaultValue = "") String title,
     @RequestParam(name = "user_id", defaultValue = "0") int user_id
     */
    @GetMapping("/api/process-engine/query_orig")
    public RestResult queryOrigin(
                                  @RequestParam(name="page",defaultValue="1") int page,
                                  @RequestParam(name="size",defaultValue="20") int size,
                                  @RequestParam(name = "flag_curr_his", defaultValue = "1") int flag_curr_his,
                                  @RequestParam(name="title", defaultValue="") String title){
//      // 获取当前用户的id,然后进行填充
//        String optPersionId="2";
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
        String userId=voUserMenu.getId()+"";
//        String groupId=voUserMenu.getDeptId()+"";
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        Map map=new HashMap();
        map.put("page",page);
        map.put("size",size);
        map.put("user_id",userId);
        String url=methodList.get(9).getUrl()+"?page={page}&size={size}&flag_curr_his={flag_curr_his}&user_id={user_id}";
        if (!title.equals((""))){
            map.put("title",title);
            url=url+"&title={title}";
        }
        map.put("flag_curr_his",flag_curr_his);
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(url,Object.class,map);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }
    @Autowired
    private FormService formService;
    /**
     * POST /api/process-engine/commit_task HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     *输入格式VoCommitTask
     * JSON body
     */
    @PostMapping("/api/process-engine/commit_task")
//    public RestResult commitTask(@RequestBody VoCommitTask voCommitTask){
    @Transactional
    public RestResult commitTask(@RequestBody List<InstanceFormItem> instanceFormItems,
                                 @RequestParam("form_inst_id") Integer form_inst_id,
                                 @RequestParam("ins_proc_id") Integer  ins_proc_id,
                                 @RequestParam("ins_node_id") Integer  ins_node_id,
                                 @RequestParam("proc_id") Integer  proc_id,
                                 @RequestParam("assignOrganize") Integer  assignOrganize,
                                 @RequestParam("assignUserId") Integer  assignUserId,
                                 @RequestParam(name = "decision",defaultValue = "") String decision){
        if (instanceFormItems == null || instanceFormItems.size() == 0){
            return RestResultFactory.createErrorResult("没有传递数据进行保存");
        }
        RestResult restResult = formService.saveInstanceDataOfForm(instanceFormItems,form_inst_id, ins_proc_id, ins_node_id, proc_id);
        //实例保存完毕，进行提交
//        String decisionValue="";
//        for (InstanceFormItem instanceFormItem : instanceFormItems) {
//            //查找decision的值
//            if ("decision".equals(instanceFormItem.getSeqName())){
//                decisionValue = instanceFormItem.getData();
//            }
//        }
        if (decision.equals("")){
            return RestResultFactory.createErrorResult("decision字段未提供，不能够进行提交");
        }
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
//        String userId=voUserMenu.getId()+"";
//        String groupId=voUserMenu.getDeptId()+"";
        VoCommitTask voCommitTask=new VoCommitTask();
        voCommitTask.setDecision(decision);
        voCommitTask.setInsNodeId(ins_node_id);
        voCommitTask.setAssignGroupId(assignOrganize);
        voCommitTask.setAssignUserId(assignUserId);
        // 获取当前用户进行提交
        voCommitTask.setOptOrganizationId(voUserMenu.getDeptId());
        voCommitTask.setOptPersonId(voUserMenu.getId());
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(10).getUrl(),voCommitTask, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * POST /api/process-engine/new_proc_ins HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     *
     * JSON body
     */
    @PostMapping("/api/process-engine/new_proc_ins")
    public RestResult newProcIns(@RequestBody VoNewProcIns voNewProcIns){
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
        voNewProcIns.setOptOrganizationId(voUserMenu.getDeptId());
        voNewProcIns.setOptPersonId(voUserMenu.getId());
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(11).getUrl(),voNewProcIns, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     *GET /api/process-engine/query_proc_ins HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     *
     * 无 body, Param传参数
     * @RequestParam(name = "page", defaultValue = "1") int page,
     * @RequestParam(name = "size", defaultValue = "20") int size,
     * //1 curr, 2 his
     * @RequestParam(name = "flag_curr_his", defaultValue = "1") int flag_curr_his,
     * @RequestParam(name = "title", defaultValue = "") String title,
     * @RequestParam(name="opt_person_id", defaultValue="0") int opt_person_id
     */
    @GetMapping("/api/process-engine/query_proc_ins")
    public RestResult queryProcIns(@RequestParam(name = "page", defaultValue = "1") int page,
                                   @RequestParam(name = "size", defaultValue = "20") int size,
                                   @RequestParam(name = "flag_curr_his", defaultValue = "1") int flag_curr_his,
                                   @RequestParam(name = "title", defaultValue = "") String title,
                                   @RequestParam(name="opt_person_id", defaultValue="0") int opt_person_id){

        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        Map map=new HashMap();
        map.put("page",page);
        map.put("size",size);
        map.put("title",title);
        map.put("flag_curr_his",flag_curr_his);
        map.put("opt_person_id",opt_person_id);
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(methodList.get(12).getUrl()+"?page={page}&size={size}&flag_curr_his={flag_curr_his}&opt_person_id={opt_person_id}&title={title}",Object.class,map);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * GET /api/process-engine/query_proc_ins_one/{ins_proc_id} HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     *
     * 无 body
     */
    @GetMapping("/api/process-engine/query_proc_ins_one/{ins_proc_id}")
    public RestResult queryProcInsOne(@PathVariable(name="ins_proc_id") String ins_proc_id){

        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(methodList.get(13).getUrl()+"/"+ins_proc_id,Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * GET /api/process-engine/query_node_ins/{ins_proc_id} HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     *
     * 无 body
     */

    @GetMapping("/api/process-engine/query_node_ins/{ins_proc_id}")
    public RestResult queryNodeIns(@PathVariable(name="ins_proc_id") String ins_proc_id){

        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(methodList.get(14).getUrl()+"/"+ins_proc_id,Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * GET /api/process-engine/query_link_ins/{ins_proc_id} HTTP/1.1
     * Cookie: JSESSIONID=登录成功的session
     * X-CSRF-TOKEN: 登录成功的token
     *
     * 无 body
     */
    @GetMapping("/api/process-engine/query_link_ins/{ins_proc_id}")
    public RestResult queryLinkIns(@PathVariable(name="ins_proc_id") String ins_proc_id){

        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(methodList.get(15).getUrl()+"/"+ins_proc_id,Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    /**
     * 统计流程的接口
     */
    @GetMapping("/api/process-engine/query_def_proc_sum")
    public RestResult queryProcNum(){
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(methodList.get(16).getUrl(),Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }


    @PostMapping("/api/process-engine/new_proc_ins_with_par")
    public RestResult newProcInsWithPar(@RequestBody VoNewProcInsWithPa voNewProcInsWithPa){
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
        voNewProcInsWithPa.setOptOrganizationId(voUserMenu.getDeptId());
        voNewProcInsWithPa.setOptPersonId(voUserMenu.getId());
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(17).getUrl(),voNewProcInsWithPa, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }

    @GetMapping("/api/process-engine/query_after_in_condition/{ins_node_id}")
    public RestResult queryAfterInCondition(@PathVariable(name="ins_node_id") String ins_node_id){

        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.getForEntity(methodList.get(18).getUrl()+"/"+ins_node_id,Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }


    @PostMapping("/api/process-engine/accept_task")
    @Transactional
    public RestResult acceptTask(@RequestBody List<InstanceFormItem> instanceFormItems,
                                 @RequestParam("form_inst_id") Integer form_inst_id,
                                 @RequestParam("ins_proc_id") Integer  ins_proc_id,
                                 @RequestParam("ins_node_id") Integer  ins_node_id,
                                 @RequestParam("proc_id") Integer  proc_id){
        if (instanceFormItems == null || instanceFormItems.size() == 0){
            return RestResultFactory.createErrorResult("没有传递数据进行保存");
        }
        RestResult restResult = formService.saveInstanceDataOfForm(instanceFormItems,form_inst_id, ins_proc_id, ins_node_id, proc_id);
        //实例保存完毕，进行提交
//        String decisionValue="";
//        for (InstanceFormItem instanceFormItem : instanceFormItems) {
//            //查找decision的值
//            if ("decision".equals(instanceFormItem.getSeqName())){
//                decisionValue = instanceFormItem.getData();
//            }
//        }
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
//        String userId=voUserMenu.getId()+"";
//        String groupId=voUserMenu.getDeptId()+"";
        VoAcceptTask voAcceptTask = new VoAcceptTask();
        voAcceptTask.setInsNodeId(ins_node_id);
        // 获取当前用户进行提交
        voAcceptTask.setOptOrganizationId(voUserMenu.getDeptId());
        voAcceptTask.setOptPersonId(voUserMenu.getId());
        RestTemplate restTemplate=getRestClientService();
        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
        ResponseEntity<Object> objectResponseEntity = restTemplate.postForEntity(methodList.get(19).getUrl(),voAcceptTask, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
    }


    @Autowired
    private CommonBusiDictRepository commonBusiDictRepository;

    @PostMapping("/api/process-engine/addBusiType")
    @Transactional
    public RestResult addCommonBusiType(@RequestBody List<CommonBusiDict> commonBusiDicts){
        if (commonBusiDicts != null && commonBusiDicts.size() > 0){
            commonBusiDictRepository.saveAll(commonBusiDicts);
        }
        return RestResultFactory.createErrorResult("没有传送过来数据，请重试");
    }

//    @RequestMapping("/api/testSession")
//    public Object testSeesion(){
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return principal;
//    }

    /**
     *获取前面人已经填写的表单，根据连接接口来进行填写，如果排在这个人之前的节点，可定是都已经填完的，然后，加载这些表单实例和相应的定义，仅查看
     */
//    @GetMapping("/getCompletedForm/{ins_proc_id}")
//    public RestResult getCompletedForm(@PathVariable(name="ins_proc_id") String ins_proc_id){
//        RestTemplate restTemplate=getRestClientService();
//        List<MethodForUrl> methodList = processMethodUrlProperties.getMethodList();
//        ResponseEntity<List> objectResponseEntity = restTemplate.getForEntity(methodList.get(15).getUrl()+"/"+ins_proc_id,List.class);
//        HttpStatus statusCode = objectResponseEntity.getStatusCode();
//        if (!statusCode.equals(HttpStatus.OK)){
//            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
//        }
//        List list = objectResponseEntity.getBody();
//        if (list.size() == 0){
//            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
//        }
//        //创建一个list，用于装前面的元素
//        List<String> nodeIdList=new ArrayList<>();
//        for (Object o : list) {
//            Map map= (Map) o;
//            //如果没有到等于这个节点的所有节点，说明已经是完成的，可以查看的节点id
//            if (!ins_proc_id.equals(map.get("toNodeId").toString())){
//                nodeIdList.add(map.get("toNodeId").toString());
//            }
//        }
//        //根据节点id获取相应的表单,并创建一个list用于装最终的结果
//
//
//    }
}

