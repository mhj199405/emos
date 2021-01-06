package com.tongtech.service.statistice;


import com.tongtech.common.vo.BpmInsVO;
import com.tongtech.common.vo.BpmNodeVO;
import com.tongtech.common.vo.StatInsVO;
import com.tongtech.common.vo.StatNodeVO;
import com.tongtech.dao.entity.BpmDefNode;
import com.tongtech.dao.entity.BpmDefProc;
import com.tongtech.dao.entity.BpmInsNode;
import com.tongtech.dao.entity.BpmInsProc;

import com.tongtech.dao.repository.DefNodeRepository;
import com.tongtech.dao.repository.DefProcRepository;
import com.tongtech.dao.repository.InsNodeRepository;
import com.tongtech.dao.repository.InsProcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NodeInsService {

    @Autowired
    InsProcRepository insProcRepository;
    @Autowired
    InsNodeRepository insNodeRepository;
    @Autowired
    DefProcRepository defProcRepository;
    @Autowired
    DefNodeRepository defNodeRepository;

    public StatNodeVO getDefNode(Integer procId, Integer insProcState, Integer state, String startDate, String endDate, Integer deptId, Integer optPersionId) {


        Specification<BpmDefProc> specification = new Specification<BpmDefProc>() {
            @Override
            public Predicate toPredicate(Root<BpmDefProc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicatesList = new ArrayList<>();
                if (!StringUtils.isEmpty(procId)) {
                    Predicate procIdPredicate = cb.equal(root.get("procId"), procId);
                    predicatesList.add(procIdPredicate);
                }

                if (!StringUtils.isEmpty(deptId)) {
                    Predicate deptIdPredicate = cb.equal(root.get("createDeptId"), deptId);
                    predicatesList.add(deptIdPredicate);
                }

                Predicate[] predicates = new Predicate[predicatesList.size()];
                query.where(predicatesList.toArray(predicates));
                return query.getRestriction();
            }
        };
        List<BpmDefProc> defProcList = defProcRepository.findAll(specification);


        Specification<BpmInsProc> specification2 = new Specification<BpmInsProc>() {
            @Override
            public Predicate toPredicate(Root<BpmInsProc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                List<Predicate> predicatesList = new ArrayList<>();
                CriteriaBuilder.In<Object> procIdBuilder = cb.in(root.get("procId"));
                if (!CollectionUtils.isEmpty(defProcList)) {
                    for (BpmDefProc bpmDefProc : defProcList) {
                        if (!StringUtils.isEmpty(bpmDefProc.getProcId())) {
                            procIdBuilder.value(bpmDefProc.getProcId());
                        }
                    }
                } else {
                    procIdBuilder.value(0);
                }
                if (predicatesList.size() == 0) {
                    procIdBuilder.value(0);
                }
                predicatesList.add(procIdBuilder);

                Predicate[] predicates = new Predicate[predicatesList.size()];
                query.where(predicatesList.toArray(predicates));
                return query.getRestriction();
            }
        };
        List<BpmInsProc> bpmInsProcList = insProcRepository.findAll(specification2);


        Specification<BpmInsNode> specificationInsNode = new Specification<BpmInsNode>() {

            @Override
            public Predicate toPredicate(Root<BpmInsNode> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicatesList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(bpmInsProcList)) {
                    CriteriaBuilder.In<Object> procIdBuilder = cb.in(root.get("insProcId"));
                    for (BpmInsProc bpmInsProc : bpmInsProcList) {
                        if (!StringUtils.isEmpty(bpmInsProc.getInsProcId())) {
                            procIdBuilder.value(bpmInsProc.getInsProcId());
                        }
                    }
                    predicatesList.add(procIdBuilder);
                }
                if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
                    Predicate datePredicate = cb.between(root.get("readyTime").as(String.class), startDate, endDate);
                    predicatesList.add(datePredicate);
                } else if (!StringUtils.isEmpty(startDate)) {
//                    Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), startTime.toString());
                    Predicate startDatePredicate = cb.greaterThan(root.get("readyTime").as(String.class), startDate);
                    predicatesList.add(startDatePredicate);
                } else if (!StringUtils.isEmpty(endDate)) {
                    Predicate endDatePredicate = cb.lessThan(root.get("readyTime").as(String.class), endDate);
                    predicatesList.add(endDatePredicate);
                }
                if (!StringUtils.isEmpty(optPersionId)) {
                    Predicate optPersionIdPredicate = cb.equal(root.get("optPersonId"), optPersionId);
                    predicatesList.add(optPersionIdPredicate);
                }

                if (!StringUtils.isEmpty(insProcState)) {
                    Predicate insProcStatePredicate = cb.equal(root.get("insNodeState"), insProcState);
                    predicatesList.add(insProcStatePredicate);
                }
                Predicate[] predicates = new Predicate[predicatesList.size()];
                query.where(predicatesList.toArray(predicates));
                return query.getRestriction();
            }
        };
        List<BpmInsNode> bpmInsNodeList = insNodeRepository.findAll(specificationInsNode);


        List<BpmInsProc> newBpmInsProcList = new ArrayList<>();
        List<BpmInsNode> newBpmInsNodeList = new ArrayList<>();
//
        newBpmInsNodeList = bpmInsNodeList;
//        List<BpmInsNode> exceptionBpmInsProcList = new ArrayList<>();
        Map<Integer, Integer> exceptionMap = new HashMap();   //存放<procID,exceptionNumSum>  异常数量
        if (!StringUtils.isEmpty(state)) { //0正常1异常3全部
            if (state == 0) {
                newBpmInsNodeList = bpmInsNodeList.stream()
                        .filter(insNode -> !StringUtils.isEmpty(insNode.getBeginTime()) && !StringUtils.isEmpty(insNode.getEndTime()))
                        .filter(insNode -> !StringUtils.isEmpty(insNode.getTimeout()))
                        .filter(insNode -> insNode.getTimeout() >=
                                (Duration.between(insNode.getBeginTime(), insNode.getEndTime())).toMillis() / 1000)
                        .collect(Collectors.toList());
            } else if (state == 1) {
                newBpmInsNodeList = bpmInsNodeList.stream()
                        .filter(insNode -> !StringUtils.isEmpty(insNode.getBeginTime()) && !StringUtils.isEmpty(insNode.getEndTime()))
                        .filter(insNode -> !StringUtils.isEmpty(insNode.getTimeout()))
                        .filter(insNode -> insNode.getTimeout() <
                                (Duration.between(insNode.getBeginTime(), insNode.getEndTime())).toMillis() / 1000)
                        .collect(Collectors.toList());

            }
        }

        Integer procIds = null;
//            Set<Integer> newProcIds = new HashSet<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        Set<Integer> procIds2 = new HashSet<>();
        for (BpmInsNode bpmInsNode : newBpmInsNodeList) {
//             newProcIds = new HashSet<>();
            procIds = insProcRepository.getProcIdByInsProcId(bpmInsNode.getInsProcId());
            if (!StringUtils.isEmpty(procIds)) {
                procIds2.add(procIds);
                if (map.containsKey(procIds)) {
                    List list = new ArrayList();
                    list = map.get(procIds);
                    list.add(bpmInsNode.getInsProcId());
                    map.put(procIds, list);
                } else {
                    List list = new ArrayList();
                    list.add(bpmInsNode.getInsProcId());
                    map.put(procIds, list);
                }
            }
//                newProcIds.add(bpmInsProc.getInsProcId());
        }

        Map<Integer, Map<Integer, Integer>> nodeSumMap = new HashMap<>();   //记录每行任务总数
        Map<Integer, Map<Integer, Integer>> filedNodeMap = new HashMap<>();          //记录每行失败
        Map<Integer, Map<Integer, Long>> nodeAvgTimeMap = new HashMap<>();   //记录每行时间总和
        Map<Integer, Set<Integer>> procIdNodeIdMap = new HashMap<>();   //存放procId和对应的nodeId
        Map<Integer, Map<Integer, Integer>> nodeNullTimeSumMap = new HashMap<>();   //记录每行任务总数
        for (Integer id : procIds2) {
            Map<Integer, Long> nodeSumTimeMap2 = new HashMap<>();
            Map<Integer, Integer> filedNodeMap2 = new HashMap<>();
            Map<Integer, Integer> nodeSumMap2 = new HashMap<>();
            Map<Integer, Integer> nodeNullTimeSumMap2 = new HashMap<>();
            Set<Integer> nodeSet = new HashSet<>();
//                List<BpmInsNode> insProcNode = new ArrayList<>();
            List<BpmInsNode> bpmInsNodes = new ArrayList<>();
            bpmInsNodes = newBpmInsNodeList;
            bpmInsNodes = bpmInsNodes.stream().filter(s -> s.getProcId().equals(id)).collect(Collectors.toList());
            for (BpmInsNode insNode : bpmInsNodes) {
//                           long timeNodeSum =0;
                if (!StringUtils.isEmpty(insNode.getNodeId())) {
                    nodeSet.add(insNode.getNodeId());
                }

                if (!StringUtils.isEmpty(insNode.getBeginTime()) && !StringUtils.isEmpty(insNode.getEndTime())) {
                    Duration time = Duration.between(insNode.getBeginTime(), insNode.getEndTime());
                    if (time.toMillis() * 1000 > insNode.getTimeout()) {

                        if (filedNodeMap2.containsKey(insNode.getNodeId())) {
                            int filenodeSum = filedNodeMap2.get(insNode.getNodeId());
                            filenodeSum++;
                            filedNodeMap2.put(insNode.getNodeId(), filenodeSum);
                        } else {
                            filedNodeMap2.put(insNode.getNodeId(), 1);
                        }
                        filedNodeMap.put(insNode.getProcId(), filedNodeMap2);
//                        failedNodeIds.add(insNode.getNodeId());
                    }
                }
//                    <procId,<nodeId,nodeIdsum>>

//                    ======================================================

                if (!StringUtils.isEmpty(insNode.getNodeId())) {
                    if (nodeSumMap2.containsKey(insNode.getNodeId())) {
                        int nodeSums = nodeSumMap2.get(insNode.getNodeId());
                        nodeSums++;
                        nodeSumMap2.put(insNode.getNodeId(), nodeSums);
                    } else {

                        nodeSumMap2.put(insNode.getNodeId(), 1);
                    }

                    ////先记录proc_id和不重复的node_id
                    nodeSumMap.put(insNode.getProcId(), nodeSumMap2);
//                   ======================================================
                    if (!StringUtils.isEmpty(insNode.getBeginTime()) && !StringUtils.isEmpty(insNode.getEndTime())) {
                        Duration time2 = Duration.between(insNode.getBeginTime(), insNode.getEndTime());
                        long newTime = time2.toHours();
//                                timeNodeSum+=newTime;
                        if (nodeSumTimeMap2.containsKey(insNode.getNodeId())) {
                            long nodetime = nodeSumTimeMap2.get(insNode.getNodeId());
                            nodeSumTimeMap2.put(insNode.getNodeId(), nodetime + newTime);
                        } else {
                            nodeSumTimeMap2.put(insNode.getNodeId(), newTime);
                        }
                    }else{
                        if (nodeNullTimeSumMap2.containsKey(insNode.getNodeId())) {
                            Integer nullTime = nodeNullTimeSumMap2.get(insNode.getNodeId())+1;
                            nodeNullTimeSumMap2.put(insNode.getNodeId(), nullTime);
                        } else {
                            nodeNullTimeSumMap2.put(insNode.getNodeId(), 1);
                        }
                    }

                }
                ////先记录proc_id和不重复的node_id
                nodeAvgTimeMap.put(insNode.getProcId(), nodeSumTimeMap2);
                procIdNodeIdMap.put(id, nodeSet);
                nodeNullTimeSumMap.put(id,nodeNullTimeSumMap2);
            }


        }


        StatNodeVO statNodeVO = new StatNodeVO();
//        statNodeVO.setBpmNodeVOList();


        List<BpmNodeVO> bpmNodeVOS = new ArrayList<>();


        int nodeCountSum = 0;
        int filedCountSum = 0;

        Iterator<Map.Entry<Integer, Set<Integer>>> procIdNodeIdEntries = procIdNodeIdMap.entrySet().iterator();
        while (procIdNodeIdEntries.hasNext()) {
            Map.Entry<Integer, Set<Integer>> entries = procIdNodeIdEntries.next();
            Integer procid = entries.getKey();


            for (Integer nodeid : entries.getValue()) {
                BpmNodeVO bpmNodeVO = new BpmNodeVO();

                BpmDefNode bpmDefNode = defNodeRepository.getBpmDefNodeByNodeIdAndProcId(procid, nodeid);

                if (!StringUtils.isEmpty(bpmDefNode) && !StringUtils.isEmpty(bpmDefNode.getNodeName())) {

                    bpmNodeVO.setNodeName(bpmDefNode.getNodeName());
                } else {
                    continue;
                }

//                =======================================
                Map<Integer, Integer> nodesumMap = nodeSumMap.get(procid);
                Integer nodeSum = nodesumMap.get(nodeid);
                bpmNodeVO.setProcId(procid);
                bpmNodeVO.setNodeId(nodeid);
                bpmNodeVO.setNodeSum(nodeSum);
                nodeCountSum += nodeSum;
//                =========================================
                Integer filedSum = 0;
                Map<Integer, Integer> filednodeMap = filedNodeMap.get(procid);
                if (filednodeMap != null) {
                    if (filednodeMap.get(nodeid) != null) {
                        filedSum = filednodeMap.get(nodeid);
                        filedCountSum += filedSum;
                        bpmNodeVO.setExceptionNum(filedSum);
                        String num = String.format("%.2f", (filedSum * 1.0 / nodeSum)*100);
                        bpmNodeVO.setExceptionRate(num);

                    } else {
                        bpmNodeVO.setExceptionNum(0);
                        bpmNodeVO.setExceptionRate("0");
                    }

                } else {
                    bpmNodeVO.setExceptionNum(0);
                    bpmNodeVO.setExceptionRate("0");
                }


//                ===========================================
                Map<Integer, Integer> integerIntegerMap = nodeNullTimeSumMap.get(procid);
                Integer nullTimeSum = 0;
                if(!StringUtils.isEmpty(integerIntegerMap.get(nodeid))){
                    nullTimeSum=integerIntegerMap.get(nodeid);
                }

                Map<Integer, Long> nodeavgTimeMap = nodeAvgTimeMap.get(procid);
                Long sumTime = nodeavgTimeMap.get(nodeid);
                if(!StringUtils.isEmpty(sumTime)){
                    bpmNodeVO.setAvgTime(sumTime  / nodeSum-nullTimeSum);
                    bpmNodeVOS.add(bpmNodeVO);
                }

            }


        }
        statNodeVO.setExceptionSumNum(filedCountSum);
        statNodeVO.setNodeSumNum(nodeCountSum);
        String num = String.format("%.2f", (filedCountSum * 1.0 / nodeCountSum)*100);
        statNodeVO.setExceptionSumRate(num);
        statNodeVO.setBpmNodeVOList(bpmNodeVOS);
        return statNodeVO;

    }

        /*
        //BpmDefProc
        //BpmInsProc
        //BpmInsNode

//        Specification<BpmInsNode> specification = new Specification<BpmInsNode>(){
//
//            @Override
//            public Predicate toPredicate(Root<BpmInsNode> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicatesList = new ArrayList<>();
//                if (!StringUtils.isEmpty(procId)) {
//                    Predicate procIdPredicate = cb.equal(root.get("procId"), procId);
//                    predicatesList.add(procIdPredicate);
//                }
//                return null;
//            }
//        };
//


        Specification<BpmDefProc> specification = new Specification<BpmDefProc>() {
            @Override
            public Predicate toPredicate(Root<BpmDefProc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {


                List<Predicate> predicatesList = new ArrayList<>();
                if (!StringUtils.isEmpty(procId)) {
                    Predicate procIdPredicate = cb.equal(root.get("procId"), procId);
                    predicatesList.add(procIdPredicate);
                }

                if (!StringUtils.isEmpty(deptId)) {
                    Predicate deptIdPredicate = cb.equal(root.get("createDeptId"), deptId);
                    predicatesList.add(deptIdPredicate);
                }

                Predicate[] predicates = new Predicate[predicatesList.size()];
                query.where(predicatesList.toArray(predicates));
                return query.getRestriction();
            }
        };
        List<BpmDefProc> defProcList = defProcRepository.findAll(specification);


        Specification<BpmInsProc> specification2 = new Specification<BpmInsProc>() {
            @Override
            public Predicate toPredicate(Root<BpmInsProc> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                List<Predicate> predicatesList = new ArrayList<>();
                if (!StringUtils.isEmpty(startDate) && !StringUtils.isEmpty(endDate)) {
                    String startdate = startDate;
                    String enddate = endDate;
                    LocalDateTime startTime = LocalDateTime.parse(startdate, df);
                    LocalDateTime endTime = LocalDateTime.parse(enddate, df);
                    Predicate datePredicate = cb.between(root.get("createTime"), startTime, endTime);
                    predicatesList.add(datePredicate);
                } else if (!StringUtils.isEmpty(startDate)) {
                    String startdate = startDate;
                    LocalDateTime startTime = LocalDateTime.parse(startdate, df);
                    System.out.println(startTime);
//                    Predicate startDatePredicate = cb.greaterThanOrEqualTo(root.get("createTime").as(String.class), startTime.toString());
                    Predicate startDatePredicate = cb.greaterThan(root.get("createTime"), startTime);
                    predicatesList.add(startDatePredicate);
                } else if (!StringUtils.isEmpty(endDate)) {
                    String enddate = endDate;
                    LocalDateTime endTime = LocalDateTime.parse(enddate, df);
                    Predicate endDatePredicate = cb.lessThan(root.get("createTime").as(LocalDateTime.class), endTime);
                    predicatesList.add(endDatePredicate);
                }
                if (!StringUtils.isEmpty(optPersionId)) {
                    Predicate optPersionIdPredicate = cb.equal(root.get("optPersonId"), optPersionId);
                    predicatesList.add(optPersionIdPredicate);
                }

                if (!StringUtils.isEmpty(insProcState)) {
                    Predicate insProcStatePredicate = cb.equal(root.get("insProcState"), insProcState);
                    predicatesList.add(insProcStatePredicate);
                }

                Predicate[] predicates = new Predicate[predicatesList.size()];
                query.where(predicatesList.toArray(predicates));
                return query.getRestriction();
            }
        };
        List<BpmInsProc> bpmInsProcList = insProcRepository.findAll(specification2);


        //第一次查询得到的insProcIds
        List<Integer> insProcIds = new ArrayList<>();
        List<Integer> insProcIdsSum = new ArrayList<>();
        for (BpmDefProc defProc : defProcList) {
            insProcIds = insProcRepository.getInsProcSumByProcId(defProc.getProcId());
            for (Integer insid : insProcIds) {
                insProcIdsSum.add(insid);
            }

        }
        insProcIds = insProcIdsSum;
        //第二次查询 过滤
        List<Integer> insProcIds2 = new ArrayList<>();
        for (BpmInsProc ins : bpmInsProcList) {
            insProcIds2.add(ins.getInsProcId());
        }

        insProcIds = insProcIds.stream().filter(id -> insProcIds2.contains(id)).collect(Collectors.toList());


        List<Integer> successInsProcIds = new ArrayList<>();
        List<Integer> failedInsProcIds = new ArrayList<>();        //失败insProc
        List<Integer> failedNodeIds = new ArrayList<>();
        for (Integer insId : insProcIds) {
//            bpmInsVO.setInsNum();
//                exceptionNum++;

            List<BpmInsNode> insProcNode = insNodeRepository.getInsNodeByInsProcId(insId);

            if (insProcNode.size() == 0) {
                failedInsProcIds.add(insId);
            }
            for (BpmInsNode insNode : insProcNode) {
                //新添加的，避免空指针
                if (insNode.getBeginTime() == null || insNode.getEndTime() == null) {
                    continue;
                }
                Duration time = Duration.between(insNode.getBeginTime(), insNode.getEndTime());
                if (time.toMillis() * 1000 > insNode.getTimeout()) {

//                    successInsProcIds.add(insId);
//                    break;
                    failedInsProcIds.add(insId);
                    break;
                }
            }
            for (BpmInsNode insNode : insProcNode) {
                //新添加的，避免空指针
                if (insNode.getBeginTime() == null || insNode.getEndTime() == null) {
                    continue;
                }
                Duration time = Duration.between(insNode.getBeginTime(), insNode.getEndTime());
                if (time.toMillis() * 1000 > insNode.getTimeout()) {
                    failedNodeIds.add(insNode.getNodeId());
                }
            }
        }
        Map<Integer, List<Integer>> map2 = new HashMap<>();//存放失败的
        for (Integer filedInsId : failedInsProcIds) {

//             newProcIds = new HashSet<>();
            int procIds2 = insProcRepository.getProcIdByInsProcId(filedInsId);
            if (map2.containsKey(procIds2)) {
                List list = new ArrayList();
                list = map2.get(procIds2);
                list.add(filedInsId);
                map2.put(procIds2, list);
            } else {
                List list = new ArrayList();
                list.add(filedInsId);
                map2.put(procIds2, list);
            }
        }

        List<Integer> newInsProcIds = new ArrayList<>();
        newInsProcIds = insProcIds;
//        insProcIds = insProcIds.stream().filter(id ->
//                !(failedInsProcIds.contains(id))).collect(Collectors.toList());
//        if (state != null) {
//            if (state == 1) { //成功
////                List<Integer> finalSuccessInsProcIds = successInsProcIds;
//                insProcIds = insProcIds.stream().filter(id ->
//                        !(failedInsProcIds.contains(id))).collect(Collectors.toList());
//            } else if (state == 2) { //失败
////                List<Integer> finalFailedInsProcIds = failedInsProcIds;
//                insProcIds = insProcIds.stream().filter(s -> failedInsProcIds.contains(s)).collect(Collectors.toList());
//            }
//        }

        Integer procIds = null;
        Set<Integer> newProcIds = new HashSet<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        Set<Integer> procIds2 = new HashSet<>();
        for (Integer insProcId : insProcIds) {
//             newProcIds = new HashSet<>();
            procIds = insProcRepository.getProcIdByInsProcId(insProcId);
            procIds2.add(procIds);
            if (map.containsKey(procIds)) {
                List list = new ArrayList();
                list = map.get(procIds);
                list.add(insProcId);
                map.put(procIds, list);
            } else {
                List list = new ArrayList();
                list.add(insProcId);
                map.put(procIds, list);
            }
            newProcIds.add(insProcId);


        }

        BpmDefProc defProcs = new BpmDefProc();

//        List<Integer> insProcId = new ArrayList<>();
        int insSumAll = 0;
        int insSumExceptionNumAll = 0;
        StatInsVO statInsVO = new StatInsVO();

        List<BpmInsVO> insList = new ArrayList<>();

        insSumAll += newProcIds.size();
        Map<Integer, Map<Integer, Integer>> nodeSumMap = new HashMap<>();   //记录每行任务总数
        Map<Integer, Map<Integer, Integer>> filedNodeMap = new HashMap<>();          //记录每行失败
        Map<Integer, Map<Integer, Long>> nodeAvgTimeMap = new HashMap<>();   //记录每行时间总和
        Map<Integer, Set<Integer>> procIdNodeIdMap = new HashMap<>();   //存放procId和对应的nodeId
        for (Integer id : procIds2) {
            Map<Integer, Long> nodeSumTimeMap2 = new HashMap<>();
            Map<Integer, Integer> filedNodeMap2 = new HashMap<>();
            Map<Integer, Integer> nodeSumMap2 = new HashMap<>();
            Set<Integer> nodeSet = new HashSet<>();


            defProcs = defProcRepository.getBpmDefProcById(id);

            List<Integer> insProcid = insProcRepository.getInsProcSumByProcId(defProcs.getProcId());

            for (Integer insId : insProcid) {

//                StatNodeVO statNodeVO = new StatNodeVO();
//                statNodeVO.setBpmNodeVOList();
//                statNodeVO.setExceptionSumNum();
//                statNodeVO.setExceptionSumRate();
//                statNodeVO.setNodeSumNum();
//
//                List<BpmNodeVO> bpmNodeVOS = new ArrayList<>();
//                BpmNodeVO bpmNodeVO = new BpmNodeVO();
//                bpmNodeVO.setAvgTime();
//                bpmNodeVO.setExceptionNum();
//                bpmNodeVO.setExceptionRate();
//
//                bpmNodeVO.setNodeSum();


                List<BpmInsNode> insProcNode = insNodeRepository.getInsNodeByInsProcId(insId);


                for (BpmInsNode insNode : insProcNode) {

                    nodeSet.add(insNode.getNodeId());

                    Integer filedNode = 0;
                    //新添加的，避免空指针
                    if (insNode.getBeginTime() == null || insNode.getEndTime() == null) {
                        continue;
                    }
                    Duration time = Duration.between(insNode.getBeginTime(), insNode.getEndTime());
                    if (time.toMillis() * 1000 > insNode.getTimeout()) {

                        if (filedNodeMap2.containsKey(insNode.getNodeId())) {
                            int filenodeSum = filedNodeMap2.get(insNode.getNodeId());
                            filenodeSum++;
                            filedNodeMap2.put(insNode.getNodeId(), filenodeSum);
                        } else {
                            filedNodeMap2.put(insNode.getNodeId(), 1);
                        }
                        filedNodeMap.put(id, filedNodeMap2);
//                        failedNodeIds.add(insNode.getNodeId());
                    }

//                    <procId,<nodeId,nodeIdsum>>

//                    ======================================================
                    if (nodeSumMap2.containsKey(insNode.getNodeId())) {
                        int nodeSums = nodeSumMap2.get(insNode.getNodeId());
                        nodeSums++;
                        nodeSumMap2.put(insNode.getNodeId(), nodeSums);
                    } else {
                        nodeSumMap2.put(insNode.getNodeId(), 1);
                    }
                    ////先记录proc_id和不重复的node_id
                    nodeSumMap.put(id, nodeSumMap2);
//                   ======================================================
                    Duration time2 = Duration.between(insNode.getBeginTime(), insNode.getEndTime());
                    long newTime = time2.toHours();
//                    timeNodeSum+=newTime;
                    if (nodeSumTimeMap2.containsKey(insNode.getNodeId())) {

                        long nodetime = nodeSumTimeMap2.get(insNode.getNodeId());
                        nodeSumTimeMap2.put(insNode.getNodeId(), newTime + nodetime);
                    } else {
                        nodeSumTimeMap2.put(insNode.getNodeId(), newTime);
                    }
                    ////先记录proc_id和不重复的node_id
                    nodeAvgTimeMap.put(id, nodeSumTimeMap2);


                }

            }
            procIdNodeIdMap.put(id, nodeSet);
//
//            BpmInsVO bpmInsVO = new BpmInsVO();
//            bpmInsVO.setProcId(defProcs.getProcId());
//            bpmInsVO.setProcName(defProcs.getProcName());
//            bpmInsVO.setInsNum(map.get(id).size());
//            if (map2.get(bpmInsVO.getProcId()) != null) {
//                bpmInsVO.setExceptionNum(map2.get(bpmInsVO.getProcId()).size());
//            } else {
//                map2.put(bpmInsVO.getProcId(), new ArrayList<>());
//                bpmInsVO.setExceptionNum(0);
//            }
//            insSumExceptionNumAll += map2.get(bpmInsVO.getProcId()).size();
//
//            String num = String.format("%.2f", bpmInsVO.getExceptionNum() * 1.0 / bpmInsVO.getInsNum());
//            bpmInsVO.setExceptionRate(num);
//            insList.add(bpmInsVO);
//
//
//        }
//        statInsVO.setBpmInsVOList(insList);
//        statInsVO.setInsSumNum(insSumAll);
//        statInsVO.setExceptionSumNum(insSumExceptionNumAll);
//
//        String num = String.format("%.2f", insSumExceptionNumAll * 1.0 / insSumAll);
//        statInsVO.setExceptionSumRate(num);
//        return statInsVO;

        }
////        ===========================================拆分合并
//        Map<Integer, Map<Integer, Integer>> nodeSumMap = new HashMap<>();   //记录每行任务总数
//        Map<Integer, Map<Integer, Integer>> filedNodeMap = new HashMap<>();          //记录每行失败
//        Map<Integer, Map<Integer, Long>> nodeAvgTimeMap = new HashMap<>();   //记录每行时间总和


//        Iterator<Entry<String, String>> entries = map.entrySet().iterator();
//        while(entries.hasNext()){
//            Entry<String, String> entry = entries.next();
//            String key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println(key+":"+value);
//        }
//        Iterator<Map.Entry<Integer, Map<Integer, Integer>>> nodeSumEntries = nodeSumMap.entrySet().iterator();
//        while(nodeSumEntries.hasNext()){
//            Map.Entry<Integer, Map<Integer, Integer>> entry = nodeSumEntries.next();
//            Integer procid = entry.getKey();
//            Iterator<Map.Entry<Integer, Map<Integer, Integer>>> nodeSumEntries = nodeSumMap.entrySet().iterator();
//
//        }


        StatNodeVO statNodeVO = new StatNodeVO();
//        statNodeVO.setBpmNodeVOList();


        List<BpmNodeVO> bpmNodeVOS = new ArrayList<>();


        int nodeCountSum = 0;
        int filedCountSum = 0;

        Iterator<Map.Entry<Integer, Set<Integer>>> procIdNodeIdEntries = procIdNodeIdMap.entrySet().iterator();
        while (procIdNodeIdEntries.hasNext()) {
            Map.Entry<Integer, Set<Integer>> entries = procIdNodeIdEntries.next();
            Integer procid = entries.getKey();


            for (Integer nodeid : entries.getValue()) {
                BpmNodeVO bpmNodeVO = new BpmNodeVO();

                BpmDefNode bpmDefNode = defNodeRepository.getBpmDefNodeByNodeIdAndProcId(procid, nodeid);
                if (StringUtils.isEmpty(bpmDefNode)) {
                    bpmNodeVO.setNodeName(bpmDefNode.getNodeName());
                }

//                =======================================
                Map<Integer, Integer> nodesumMap = nodeSumMap.get(procid);
                Integer nodeSum = nodesumMap.get(nodeid);
                bpmNodeVO.setProcId(procid);
                bpmNodeVO.setNodeId(nodeid);
                bpmNodeVO.setNodeSum(nodeSum);
                nodeCountSum += nodeSum;
//                =========================================
                Integer filedSum = 0;
                Map<Integer, Integer> filednodeMap = filedNodeMap.get(procid);
                if (filednodeMap != null) {
                    if (filednodeMap.get(nodeid) != null) {
                        filedSum = filednodeMap.get(nodeid);
                        filedCountSum += filedSum;
                        bpmNodeVO.setExceptionNum(filedSum);
                        String num = String.format("%.2f", (filedSum * 1.0 / nodeSum) * 100);
                        bpmNodeVO.setExceptionRate(num);

                    } else {
                        bpmNodeVO.setExceptionNum(0);
                        bpmNodeVO.setExceptionRate("0");
                    }
                } else {
                    bpmNodeVO.setExceptionNum(0);
                    bpmNodeVO.setExceptionRate("0");
                }


//                ===========================================
                Map<Integer, Long> nodeavgTimeMap = nodeAvgTimeMap.get(procid);
                Long sumTime = nodeavgTimeMap.get(nodeid);
                bpmNodeVO.setAvgTime(sumTime * 1.0 / nodeSum);
                bpmNodeVOS.add(bpmNodeVO);
            }


        }
        statNodeVO.setExceptionSumNum(filedCountSum);
        statNodeVO.setNodeSumNum(nodeCountSum);
        String num = String.format("%.2f", (filedCountSum * 1.0 / nodeCountSum) * 100);
        statNodeVO.setExceptionSumRate(num);
        statNodeVO.setBpmNodeVOList(bpmNodeVOS);
        return statNodeVO;
    }

         */
}
