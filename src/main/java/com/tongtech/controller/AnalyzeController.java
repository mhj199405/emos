package com.tongtech.controller;/*
package com.tongtech.controller;

import com.tongtech.common.AnalyzeMethodUrlProperties;
import com.tongtech.common.vo.MethodForUrl;
import com.tongtech.common.vo.RestResult;
import com.tongtech.common.vo.RestResultFactory;
import com.tongtech.common.vo.TemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AnalyzeController {


    @Autowired
    private RestTemplateBuilder restTemplateBuilder;


    @Autowired
    private AnalyzeMethodUrlProperties analyzeMethodUrlProperties;

    public RestTemplate getRestClientService() {
        RestTemplate restTemplate = restTemplateBuilder
//                .basicAuthorization("oss", "oss123")
                .setConnectTimeout(3000)
                .setReadTimeout(120000)
                .build();
        return restTemplate;
    }

    @RequestMapping(value = "/api/custom/analysis/gtheme", method = RequestMethod.GET)
    public Object getThemesList(){
        List<MethodForUrl> methodList = analyzeMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
        String url=methodList.get(0).getUrl();
        ResponseEntity<Object> entity = restTemplate.getForEntity(url, Object.class);
        HttpStatus statusCode = entity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createErrorResult("查询失败，请重新尝试");
        }
        return entity.getBody();
    }

    @RequestMapping(value = "/api/custom/analysis/query", method = RequestMethod.POST)
    public Object searchData(@RequestBody TemplateVo model, @RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "1") Integer pageNum){
        List<MethodForUrl> methodList = analyzeMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
        String url=methodList.get(1).getUrl()+"?pageSize={pageSize}"+"&pageNum={pageNum}";
        Map map=new HashMap();
        map.put("pageSize",pageSize);
        map.put("pageNum",pageNum);
        ResponseEntity<Object> entity = restTemplate.postForEntity(url,model, Object.class,map);
        HttpStatus statusCode = entity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createErrorResult("查询失败，请重新尝试");
        }
        return entity.getBody();
    }

    @RequestMapping(value = "/api/custom/analysis/export", method = RequestMethod.POST)
    public Object exportDataForTemplate(@RequestBody TemplateVo model) {
        List<MethodForUrl> methodList = analyzeMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
        ResponseEntity<ResponseEntity> entity = restTemplate.postForEntity(methodList.get(2).getUrl(), model, ResponseEntity.class);
        HttpStatus statusCode = entity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createErrorResult("导出失败，请重新尝试");
        }
        return entity.getBody();
    }

    @RequestMapping(value = "/api/custom/analysis/gtemplate/{themeId}", method = RequestMethod.GET)
    public Object GetTemplateList(@PathVariable String themeId) {
        List<MethodForUrl> methodList = analyzeMethodUrlProperties.getMethodList();
        RestTemplate restTemplate = getRestClientService();
        String url = methodList.get(3).getUrl() + "/" + themeId;
        ResponseEntity<Object> entity = restTemplate.getForEntity(url, Object.class);
        HttpStatus statusCode = entity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)) {
            return RestResultFactory.createErrorResult("查询失败，请重新尝试");
        }
        return entity.getBody();
    }

    @RequestMapping(value = "/api/custom/analysis/ptemplate", method = RequestMethod.POST)
    public Object SaveTemplateList(@RequestBody TemplateVo templateVo) {
        List<MethodForUrl> methodList = analyzeMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
        ResponseEntity<Object> entity = restTemplate.postForEntity(methodList.get(4).getUrl(), templateVo, Object.class);
        HttpStatus statusCode = entity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)){
            return RestResultFactory.createErrorResult("导出失败，请重新尝试");
        }
        return entity.getBody();
    }

    @RequestMapping(value = "/api/custom/analysis/glevel", method = RequestMethod.GET)
    public Object getGLevel(@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "5") Integer size,String themeId,
                                String version,String dimId,String levelId){
        List<MethodForUrl> methodList = analyzeMethodUrlProperties.getMethodList();
        RestTemplate restTemplate=getRestClientService();
        String url = methodList.get(5).getUrl()+ "?page={page}&size={size}&themeId={themeId}&version={version}&dimId={dimId}&levelId={levelId}";
        ResponseEntity<Object> entity = restTemplate.getForEntity(url, Object.class);
        HttpStatus statusCode = entity.getStatusCode();
        if (!statusCode.equals(HttpStatus.OK)) {
            return RestResultFactory.createErrorResult("查询失败，请重新尝试");
        }
        return entity.getBody();
    }

    }
*/
