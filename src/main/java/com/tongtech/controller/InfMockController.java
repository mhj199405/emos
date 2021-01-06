package com.tongtech.controller;



import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tongtech.common.config.InfConfig;
import com.tongtech.common.vo.*;
import com.tongtech.dao.entity.CommonKeyDictT;
import com.tongtech.dao.entity.InfMockLog;
import com.tongtech.dao.repository.CommonKeyDictTRepository;
import com.tongtech.dao.repository.CommonProcDataTRepository;
import com.tongtech.dao.repository.InfMockLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class InfMockController {

    @Autowired
    private InfMockLogRepository infMockLogRepository;
    @Autowired
    private CommonKeyDictTRepository commonKeyDictTRepository;
    @Autowired
    private CommonProcDataTRepository commonProcDataTRepository;
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Autowired
    InfConfig infConfig;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(path = "/api/inf_mock/create")
    public RestResult createProcInst(@RequestBody InfMockRequestVo requestVo) {
        BpmDefProc proc = requestVo.getProcess();

        // 保存数据
        Map<String, String> values = saveProcData(requestVo);
        if (values == null || values.get("complaintType") == null) {
            return RestResultFactory.createErrorResult("参数错误，或者没有complaintType类型，请重新尝试");
        }
        int begin=0;
        int idx = 1;
        String strCode;

        String busiTypeStr = values.get("complaintType").substring(2);
        if (busiTypeStr != null && busiTypeStr.length()>0) {
            while(begin<busiTypeStr.length()) {
                strCode = busiTypeStr.substring(begin,begin+2);
                values.put("busicode"+ idx, strCode);
                begin += 2;
                idx += 1;
            }
        }
        System.out.println("++++++++++++++++++++++++++++++++++");
        for (Map.Entry<String,String> entry : values.entrySet()) {
            System.out.println("\t" + entry.getKey() +" \t" + entry.getValue());
        }
        System.out.println("++++++++++++++++++++++++++++++++++");


        // 保存日志
        InfMockLog log = new InfMockLog();
        log.setCreateTime(LocalDateTime.now());
        log.setStatus(1);
        Map<String,String>  paramstr = requestVo.getParams();
        try {
            log.setValueList(objectMapper.writeValueAsString(paramstr));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.setValueList("");
        }
        saveLog(log);

        // 创建请求对象
        String userId="3";
        userId = infConfig.getUserId();
        String loginName = "wfadmin";
        loginName = infConfig.getUserName();
        String groupId="1";
        groupId = infConfig.getDeparId();
        String password = infConfig.getPassword();


        VoNewProcIns newProcIns = new VoNewProcIns();
        newProcIns.setProcId(proc.getProcId());
        newProcIns.setOptPersonId(Integer.parseInt(infConfig.getUserId()));
        newProcIns.setOptOrganizationId(Integer.parseInt(infConfig.getDeparId()));
        newProcIns.setData(values);

        // 请求数据
        RestTemplate restTemplate=getRestClientService(loginName, password);
        String url= infConfig.getUrl();
        // "http://168.1.26.15:8210/api/process-engine/new_proc_ins_with_par";

        ResponseEntity<Object> objectResponseEntity =
                restTemplate.postForEntity(url, newProcIns, Object.class);
        HttpStatus statusCode = objectResponseEntity.getStatusCode();
        if (statusCode.equals(HttpStatus.OK)){
            log.setStatus(2);
            saveLog(log);
            return RestResultFactory.createSuccessResult(objectResponseEntity.getBody());
        }else {
            log.setStatus(3);
            saveLog(log);
            return RestResultFactory.createErrorResult("处理失败，请重新尝试");
        }
      //  return RestResultFactory.createErrorResult("处理失败，请重新尝试");
    }

    @Transactional
    public void saveLog(InfMockLog log) {
        infMockLogRepository.saveAndFlush(log);
    }

    @Transactional
    public Map<String, String> saveProcData(InfMockRequestVo requestVo) {
        BpmDefProc proc = requestVo.getProcess();
        // get max key_id
        Integer keyId = commonKeyDictTRepository.getMaxKeyId();
        if (keyId == null ) { keyId = 0; }
        // parse xml to json
//        Order order = null;
//        try {
//            order = xmlMapper.readValue(requestVo.getParams(), Order.class);
//            System.out.println(order.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (OrderFieldInfo field : order.getOrder().get(0).getFieldInfos()) {
//            values.put(field.getFieldEnName(),field.getFieldContent());
//        }

        Map<String, String> values = new HashMap<>();
        values = requestVo.getParams();
        if (values == null) {
            return null;
        }
        // save data
        List<CommonKeyDictT> keyList;
        for (Map.Entry<String,String> entry : values.entrySet()) {
            String keyName = entry.getKey();
            String value = entry.getValue();
            keyList = commonKeyDictTRepository.findByKeyName(keyName);
            if (keyList == null || keyList.isEmpty()) {
                keyId += 1;
                CommonKeyDictT key = new CommonKeyDictT();
                key.setBusiType(String.valueOf(proc.getBusiType()));
                key.setBusiTypeName("");
                key.setKeyId(keyId);
                key.setKeyName(keyName);
                key.setVerNumber(0);
                key.setCreateTime(LocalDateTime.now());
                key = commonKeyDictTRepository.saveAndFlush(key);
            }
        }
        return values;
    }

    public RestTemplate getRestClientService(String userId, String password) {
        RestTemplate restTemplate = restTemplateBuilder
                //.basicAuthentication(userId,password)
                .setConnectTimeout(3000000)
                .setReadTimeout(3000000)
                .build();
        return restTemplate;
    }


}
