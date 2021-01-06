package com.tongtech.controller;

import com.tongtech.common.config.CollectConfig;
import com.tongtech.common.utils.JsonResult;
import com.tongtech.common.utils.ObjectParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
public class CollectController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CollectConfig collectConfig;

    @RequestMapping(value = "/api/collect/data/recollect", method = RequestMethod.GET)
    public JsonResult recollectFile(@RequestParam Map<String, Object> model) {
        Object result = null;
        String etime=null;
        String url=null;
        String omc_id = ObjectParserUtil.toString(model.get("omc_id"));
        String type = ObjectParserUtil.toString(model.get("type"));
        String stime = ObjectParserUtil.toString(model.get("stime"));

        if(model.containsKey("etime")) {
             etime = ObjectParserUtil.toString(model.get("etime"));
        }
        String event = ObjectParserUtil.toString(model.get("event"));

        Map<String, Object> params = new HashMap<>();
        params.put("event",event);
        params.put("omc_id",omc_id);
        params.put("type",type);
        params.put("stime",stime);
        if (etime != null && etime.length()>0) {
            params.put("etime",etime);
            url = collectConfig.getCollectPath();
        }else{
            url=collectConfig.getCollectPath1();
        }


        String obj = null;
        try{
             obj = restTemplate.getForObject(url, String.class, params);
        } catch(RestClientException e) {
            e.printStackTrace();
            return JsonResult.error("调用["+ url + "] 失败！！！");
        }
        String successMessage="DC Task send success[ event: "+event +" omc_id: "+omc_id +" StartTime:"+stime+" EndTime:"+etime+"]";
        return JsonResult.normal(successMessage,obj);
    }
}
