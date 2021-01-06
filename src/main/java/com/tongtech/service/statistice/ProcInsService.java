package com.tongtech.service.statistice;


import com.tongtech.common.vo.BpmInsVO;
import com.tongtech.common.vo.StatInsVO;
import com.tongtech.dao.entity.BpmDefProc;
import com.tongtech.dao.entity.BpmInsNode;
import com.tongtech.dao.entity.BpmInsProc;
import com.tongtech.dao.repository.DefProcRepository;
import com.tongtech.dao.repository.InsNodeRepository;
import com.tongtech.dao.repository.InsProcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

//import com.tongtech.dao.repository.StatisticsRepository;

@Service
public class ProcInsService {
    @PersistenceContext
    private EntityManager entityManager;
    //    @Autowired
//    StatisticsRepository statisticsRepository;
    @Autowired
    InsProcRepository insProcRepository;
    @Autowired
    InsNodeRepository insNodeRepository;
    @Autowired
    DefProcRepository defProcRepository;

    public StatInsVO getProcIns(Integer procId, Integer insProcState, Integer state, String startDate, String endDate, Integer deptId, Integer optPersionId) {


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
                List<Predicate> predicatesList = new ArrayList<>();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                CriteriaBuilder.In<Object> procIdBuilder = cb.in(root.get("procId"));
                if (!StringUtils.isEmpty(defProcList)) {
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
        List<BpmInsProc> newBpmInsProcList = new ArrayList<>();

        newBpmInsProcList = bpmInsProcList;
        List<BpmInsProc> exceptionBpmInsProcList = new ArrayList<>();
        Map<Integer, Integer> exceptionMap = new HashMap();   //存放<procID,exceptionNumSum>  异常数量
        if (!StringUtils.isEmpty(state)) { //0正常1异常3全部
            if (state == 0) {
                newBpmInsProcList = newBpmInsProcList.stream()
                        .filter(proc -> !StringUtils.isEmpty(proc.getCreateTime()) && !StringUtils.isEmpty(proc.getLastOptTime()))
                        .filter(proc -> !StringUtils.isEmpty(proc.getTimeout()))
                        .filter(proc -> proc.getTimeout() >=
                                (Duration.between(proc.getCreateTime(), proc.getLastOptTime())).toMillis() / 1000)
                        .collect(Collectors.toList());

            } else if (state == 1) {
                newBpmInsProcList = newBpmInsProcList.stream()
                        .filter(proc -> !StringUtils.isEmpty(proc.getCreateTime()) && !StringUtils.isEmpty(proc.getLastOptTime()))
                        .filter(proc -> !StringUtils.isEmpty(proc.getTimeout()))
                        .filter(proc -> proc.getTimeout() <
                                (Duration.between(proc.getCreateTime(), proc.getLastOptTime())).toMillis() / 1000)
                        .collect(Collectors.toList());
                exceptionBpmInsProcList = newBpmInsProcList;
                for (BpmInsProc bpmInsProc : exceptionBpmInsProcList) {
                    if (exceptionMap.containsKey(bpmInsProc.getProcId())) {
                        int excNum = exceptionMap.get(bpmInsProc.getProcId()) + 1;
                        exceptionMap.put(bpmInsProc.getProcId(), excNum);
                    } else {
                        exceptionMap.put(bpmInsProc.getProcId(), 1);
                    }
                }
            } else {
                exceptionBpmInsProcList = newBpmInsProcList.stream()
                        .filter(proc -> !StringUtils.isEmpty(proc.getCreateTime()) && !StringUtils.isEmpty(proc.getLastOptTime()))
                        .filter(proc -> !StringUtils.isEmpty(proc.getTimeout()))
                        .filter(proc -> proc.getTimeout() < Duration.between(proc.getCreateTime(), proc.getLastOptTime()).toMillis() / 1000)
                        .collect(Collectors.toList());


                for (BpmInsProc bpmInsProc : exceptionBpmInsProcList) {
                    if (exceptionMap.containsKey(bpmInsProc.getProcId())) {
                        int excNum = exceptionMap.get(bpmInsProc.getProcId()) + 1;
                        exceptionMap.put(bpmInsProc.getProcId(), excNum);
                    } else {
                        exceptionMap.put(bpmInsProc.getProcId(), 1);
                    }
                }
            }
        }

        Map<Integer, String> mapName = new HashMap();   //存放<procID,procName>  流程名称
        Map<Integer, Integer> procInsNumMap = new HashMap();   //存放<procID,procName>  流程名称

        for (BpmInsProc bpmInsProc : newBpmInsProcList) {

            if (!StringUtils.isEmpty(bpmInsProc.getProcId())) {

                if (!mapName.containsKey(bpmInsProc.getProcId())) {
                    procInsNumMap.put(bpmInsProc.getProcId(), 1);
                    BpmDefProc bpmDefProc = defProcRepository.getBpmDefProcById(bpmInsProc.getProcId());
                    if (!StringUtils.isEmpty(bpmDefProc) && !StringUtils.isEmpty(bpmDefProc.getProcName())) {
                        mapName.put(bpmInsProc.getProcId(), bpmDefProc.getProcName());
                    } else {
                        mapName.put(bpmInsProc.getProcId(), "");
                    }
                } else {
                    int num = procInsNumMap.get(bpmInsProc.getProcId()) + 1;
                    procInsNumMap.put(bpmInsProc.getProcId(), num);
                }
            }
        }


//        ===========================================异常处理


//=================================================
        StatInsVO statInsVO = new StatInsVO();
        List<BpmInsVO> bpmInsVOList = new ArrayList<>();
        Iterator<Map.Entry<Integer, String>> iterator = mapName.entrySet().iterator();
        while (iterator.hasNext()) {
            BpmInsVO bpmInsVO = new BpmInsVO();
            Map.Entry<Integer, String> next = iterator.next();
            Integer procid = next.getKey();
            String name = next.getValue();
            if (!StringUtils.isEmpty(exceptionMap.get(procid))) {
                bpmInsVO.setExceptionNum(exceptionMap.get(procid));
            } else {
                bpmInsVO.setExceptionNum(0);
            }
            bpmInsVO.setInsNum(procInsNumMap.get(procid));
            bpmInsVO.setProcId(procid);
            bpmInsVO.setProcName(name);
            String num = String.format("%.2f", (bpmInsVO.getExceptionNum() * 1.0 / bpmInsVO.getInsNum()) * 100);
            bpmInsVO.setExceptionRate(num);
            bpmInsVOList.add(bpmInsVO);
        }
        statInsVO.setBpmInsVOList(bpmInsVOList);
        statInsVO.setInsSumNum(newBpmInsProcList.size());
        statInsVO.setExceptionSumNum(exceptionBpmInsProcList.size());
        String num = String.format("%.2f", (statInsVO.getExceptionSumNum() * 1.0 / statInsVO.getInsSumNum()) * 100);
        statInsVO.setExceptionSumRate(num);
        return statInsVO;
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
                List<Predicate> predicatesList = new ArrayList<>();
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
        Set<Integer> failedInsProcIds = new HashSet<>();        //失败insProc
        for (Integer insId : insProcIds) {
//            bpmInsVO.setInsNum();
//                exceptionNum++;


            if (!StringUtils.isEmpty(insId)) {
                BpmInsProc bpmInsProc = insProcRepository.getInsProcByInsProcId(insId);
//                List<BpmInsNode> insProcNode = insNodeRepository.getInsNodeByInsProcId(insId);
                if (bpmInsProc==null) {
                    failedInsProcIds.add(insId);
                }
                if (!StringUtils.isEmpty(bpmInsProc.getCreateTime()) && !StringUtils.isEmpty(bpmInsProc.getLastOptTime())) {
                    Duration time = Duration.between(bpmInsProc.getCreateTime(), bpmInsProc.getLastOptTime());
                    if (time.toMillis() * 1000 > bpmInsProc.getTimeout()) {
                        failedInsProcIds.add(insId);
                    }
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
        for (Integer id : procIds2) {

            defProcs = defProcRepository.getBpmDefProcById(id);

            List<Integer> insProcid = insProcRepository.getInsProcSumByProcId(defProcs.getProcId());

            BpmInsVO bpmInsVO = new BpmInsVO();
            bpmInsVO.setProcId(defProcs.getProcId());
            bpmInsVO.setProcName(defProcs.getProcName());
            bpmInsVO.setInsNum(map.get(id).size());
            if (map2.get(bpmInsVO.getProcId()) != null) {
                bpmInsVO.setExceptionNum(map2.get(bpmInsVO.getProcId()).size());
            } else {
                map2.put(bpmInsVO.getProcId(), new ArrayList<>());
                bpmInsVO.setExceptionNum(0);
            }
            insSumExceptionNumAll += map2.get(bpmInsVO.getProcId()).size();

            String num = String.format("%.2f", (bpmInsVO.getExceptionNum() * 1.0 / bpmInsVO.getInsNum())*100);
            bpmInsVO.setExceptionRate(num);
            insList.add(bpmInsVO);


        }
        statInsVO.setBpmInsVOList(insList);
        statInsVO.setInsSumNum(insSumAll);
        statInsVO.setExceptionSumNum(insSumExceptionNumAll);

        String num = String.format("%.2f", (insSumExceptionNumAll * 1.0 / insSumAll)*100);
        statInsVO.setExceptionSumRate(num);
        return statInsVO;
    }
*/
}
