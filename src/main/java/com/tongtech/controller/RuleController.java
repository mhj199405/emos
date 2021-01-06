package com.tongtech.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtech.common.RuleMethodUrlProperties;
import com.tongtech.common.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.*;
import org.springframework.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.Charset;
import java.util.*;

@RestController
@RequestMapping("/api/rule")
public class RuleController {

    @Autowired
    private RuleMethodUrlProperties ruleMethodUrlProperties;

    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    public RestTemplate getRestClientService() {
        RestTemplate restTemplate = restTemplateBuilder
//                .basicAuthorization("oss", "oss123")
                .setConnectTimeout(3000)
                .setReadTimeout(120000)
                .build();
        return restTemplate;
    }
//    public static URI handleUrlParameters(String baseUrl, Map<String, Object> parameterMap) {
//        Preconditions.checkArgument(StringUtils.hasText(baseUrl), "Error: url不能为空");
//        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        if (CollectionUtil.isNotEmpty(parameterMap)) {
//            parameterMap.forEach((key, value) -> {
//                if(Objects.nonNull(value)){
//                    params.put(key, Collections.singletonList(String.valueOf(value)));
//                }
//            });
//        }
//        return builder.queryParams(params).build().encode().toUri();
//    }
//    RestTemplate restTemplate1=new RestTemplate();
    @GetMapping("/list/query")
    public  RestResult queryForRule(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "20") int size,
                                   @RequestParam(required = false) String name,

                                   @RequestParam(required = false) String type){
        List<MethodForUrl> methodList = ruleMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
//        RestTemplate restTemplate=restTemplate1;
        String url=methodList.get(3).getUrl()+"?page={page}"+"&size={size}"+"&type={type}";
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("page", page);
        map.put("size",size);
        map.put("type",type);
        if (name!=null && !name.trim().equals("")){
            map.put("name",name);
            url=url+"&name={name}";
        }
        //+"?page="+page+"&size="+size+"&type="+type
//        ResponseEntity<Object> entity = restTemplate.exchange(url, HttpMethod.GET, null, Object.class,map);
//        Object body = resultEntity.getBody();
        ResponseEntity<Object> entity = restTemplate.getForEntity(url, Object.class, map);
        HttpStatus statusCode = entity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createErrorResult("查询失败，请重新尝试");
        }
//        ObjectMapper objectMapper = new ObjectMapper();
//        List content = entity.getBody().getContent();
//        if (content != null && content.size() > 0){
//            for (Object o : content) {
//                Map map1= (Map) o;
//                Object content1 = map1.get("content");
//                map1.put("contentStr",new String((byte[]) content1));
//            }
//        }
        return RestResultFactory.createSuccessResult(entity.getBody());
    }

    /**
     * // 规则新增
     *     @PostMapping("/api/rule_engine/list/add")
     *     public CommonRuleT addRule(@RequestBody CommonRuleT obj) {
     */
    @PostMapping("/list/add")
    public RestResult addRule(@RequestBody CommonRuleTVo commonRuleTVo){
//        byte[] bytes = commonRuleTVo.getContentStr().getBytes();
//        commonRuleTVo.setContent(bytes);
        List<MethodForUrl> methodList = ruleMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
        CommonRuleTVo o = restTemplate.postForObject(methodList.get(0).getUrl(), commonRuleTVo, CommonRuleTVo.class);
        if (o == null){
            return  RestResultFactory.createErrorResult("There is a failure for the addition of rule,please try again");
        }
//        byte[] content = o.getContent();
//        o.setContentStr(new String(content));
        return RestResultFactory.createSuccessResult(o,"the addition of rule is completed");
    }

    /**
     * /api/rule/list/update
     * // 规则修改
     *     @PutMapping("/api/rule_engine/list/update")
     *     public CommonRuleT modifyRule(@RequestBody CommonRuleT obj) {
     */
    @PutMapping("/list/update")
    public RestResult modifyRule(@RequestBody CommonRuleTVo commonRuleTVo){
        List<MethodForUrl> methodList = ruleMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();

        // 请求头
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
//         请求体
        headers.setContentType(mediaType);
        //提供json转化功能
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            if (commonRuleTVo != null) {
                str = mapper.writeValueAsString(commonRuleTVo);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return RestResultFactory.createErrorResult("传输内容为空，请重新尝试");
        }
//         发送请求
        HttpEntity entity = new HttpEntity(commonRuleTVo, headers);
        ResponseEntity<Object> resultEntity = restTemplate.exchange(methodList.get(2).getUrl(), HttpMethod.PUT, entity, Object.class);
//        try {
//            restTemplate.put(methodList.get(2).getUrl(),commonRuleTVo,Object.class);
//            return RestResultFactory.createSuccessResult("更新成功");
//        } catch (RestClientException e) {
//            e.printStackTrace();
//            return RestResultFactory.createErrorResult("更新失败，请重新尝试");
//        }
        if (!resultEntity.getStatusCode().equals(HttpStatus.OK)){
            return RestResultFactory.createErrorResult("更新失败，请重新尝试");
        }
        return RestResultFactory.createSuccessResult(null,"更新成功");
    }

    /**
     * http://ip:port/api/rule/list/delete
     *   // 删除
     *     @DeleteMapping("/api/rule_engine/list/delete")
     *     public Integer deleteRule(@RequestBody Integer ruleId) {
     */
    @DeleteMapping("/list/delete")
    public RestResult deleteRule(@RequestBody RuleDeleteRequestVo ruleId){
        if (ruleId.getRuleId() == null || ruleId.getRuleId().isEmpty()) {
            return RestResultFactory.createErrorResult("没有找到规则");
        }
        List<MethodForUrl> methodList = ruleMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();

        // 请求头
        HttpHeaders headers = new HttpHeaders();
        MimeType mimeType = MimeTypeUtils.parseMimeType("application/json");
        MediaType mediaType = new MediaType(mimeType.getType(), mimeType.getSubtype(), Charset.forName("UTF-8"));
        // 请求体
        headers.setContentType(mediaType);
        //提供json转化功能
        ObjectMapper mapper = new ObjectMapper();
        String str = null;
        try {
            if (ruleId.getRuleId() != null) {
                str = mapper.writeValueAsString(ruleId.getRuleId());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return RestResultFactory.createErrorResult("传输内容为空，请重新尝试");
        }
        // 发送请求
        HttpEntity<String> entity = new HttpEntity<>(str, headers);
        ResponseEntity<Object> resultEntity = restTemplate.exchange(methodList.get(1).getUrl(), HttpMethod.DELETE, entity, Object.class);
        if (resultEntity.getBody() == null){
            return RestResultFactory.createErrorResult("删除失败，请重新尝试");
        }
        return RestResultFactory.createSuccessResult(null,"删除成功");
    }

    /**
     * // 编译
     *     @PostMapping(path = "/api/rule_engine/compile")
     *     public JsonResult compileRules(@RequestBody Integer ruleId) {
     *
     *     http://ip:port/api/rule/release/{ruleId}
     */
    @GetMapping("/compile/{ruleId}")
    public Object compileRules(@PathVariable("ruleId") Integer ruleId){
        List<MethodForUrl> methodList = ruleMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
//        Map param=new HashMap();
//        param.put("ruleId",ruleId);
        Object result = restTemplate.postForObject(methodList.get(4).getUrl(), ruleId, Object.class);
        return result;
    }

    /**
     *  // 发布
     *     @PostMapping(path = "/api/rule_engine/release")
     *     public JsonResult releaseRule(@RequestBody Integer ruleId) {
     *
     *     http://ip:port/api/rule/release/{ruleId}
     */
    @GetMapping("/release/{ruleId}")
    public Object releaseRule(@PathVariable("ruleId") Integer ruleId){
        List<MethodForUrl> methodList = ruleMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
//        Map param=new HashMap();
//        param.put("ruleId",ruleId);
        Object result = restTemplate.postForObject(methodList.get(5).getUrl(), ruleId, Object.class);
        return result;
    }


}
