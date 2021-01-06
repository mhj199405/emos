package com.tongtech.service.yunweidaiwei;

import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import com.tongtech.common.vo.RestResult;
import com.tongtech.common.vo.RestResultFactory;
import com.tongtech.dao.entity.TawSystemDept;
import com.tongtech.dao.entity.TawSystemRole;
import com.tongtech.dao.entity.TawSystemUser;
import com.tongtech.dao.entity.TawSystemUserRefRole;
import com.tongtech.dao.repository.analysis.TawSystemDeptRepository;
import com.tongtech.dao.repository.analysis.TawSystemRoleRepository;
import com.tongtech.dao.repository.analysis.TawSystemUserRefRoleRepository;
import com.tongtech.dao.repository.analysis.TawSystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.criteria.*;
import java.util.*;

@Service
public class YunWeiDaiWeiServiceImpl implements YunWeiDaiWeiService{

    @Autowired
    private TawSystemRoleRepository tawSystemRoleRepository;
    /**
     * 添加角色
     * @param role
     * @return
     */
    @Override
    @Transactional
    public RestResult addRole(TawSystemRole role) {
        //需要判断是否是当前用户的角色的子角色，不能够越级添加角色
        String userId = currentUserId();
        //根据当前用户的id判断添加的角色是否是当前用户所属角色的子角色，或者子角色的子角色
        Boolean roleFlag = judgeRole(userId,role);
        if (!roleFlag){
            return RestResultFactory.createErrorResult("当前角色不属于当前用户的角色及子角色所有");
        }
        tawSystemRoleRepository.saveAndFlush(role);
        return RestResultFactory.createSuccessResult("角色创建成功");
    }
    //判断当前添加的角色，是否是当前系统用户的子角色或者子角色的子角色
    private Boolean judgeRole(String userId, TawSystemRole role) {
        List<TawSystemRole> roleList = tawSystemRoleRepository.findUserRoles(userId);
        if (roleList.size()>0){
            //判断是否含有插入角色的父角色
            boolean flag=false;
            String parentIdStr = role.getParentId()+"";
            for (TawSystemRole tawSystemRole : roleList) {
                if (parentIdStr.equals(tawSystemRole.getRoleId()+"")){
                    flag=true;
                    break;
                }
            }
            if (flag){
                return true;
            }else {
                for (TawSystemRole tawSystemRole : roleList) {
                    Integer roleId = tawSystemRole.getRoleId();
                    Boolean result = judgeRoleByRoleId(roleId,parentIdStr);
                    if (result){
                        return result;
                    }
                }
            }
        }
        return false;
    }
    //判断是否存在父角色
    private Boolean judgeRoleByRoleId(Integer roleId, String parentIdStr) {
        List<TawSystemRole> roles = tawSystemRoleRepository.findAllByParentId(roleId);
        Boolean flag = false;
        if (roles.size()>0){
            for (TawSystemRole role : roles) {
                if (parentIdStr.equals(role.getRoleId()+"")){
                   return true;
                }
            }
            //没有相等的，就需要继续去寻找子角色进行查找
            for (TawSystemRole role : roles) {
                boolean flag1 = judgeRoleByRoleId(role.getRoleId(),parentIdStr);
                if (flag1){
                    return true;
                }
            }
        }
        return false;
    }

    //获取当前登录用户的id
    private String currentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            com.tongtech.auth.vo.RestResult<String> failedResult = com.tongtech.auth.vo.RestResultFactory.createFailedResult("无法获取当前用户信息");
            failedResult.setStatus(1);
            return null;
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        Integer userId = user.getVoUserMenu().getId();
        return userId+"";
    }

    /**
     * 删除角色
     *
     * @param
     * @param roleId
     * @return
     */
    @Override
    @Transactional
    public RestResult deleteRole(String deptId, Integer roleId) {
        //判断当前角色是否是当前用户所有，是否被其他用户所占用
        TawSystemRole tawSystemRole = new TawSystemRole();
        tawSystemRole.setParentId(roleId);
        String s = currentUserId();
        Boolean aBoolean = judgeRole(s, tawSystemRole);
        if (!aBoolean){
            return RestResultFactory.createErrorResult("删除的角色不归属于当前用户所有");
        }
        //判断当前角色是否有其他用户使用
        Specification<TawSystemUserRefRole> tawSystemUserRefRoleSpecification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> pathList=new ArrayList<>();
                pathList.add( criteriaBuilder.equal(root.get("roleid").as(Integer.class),roleId));
                Predicate predict = query.where(pathList.toArray(new Predicate[pathList.size()])).getRestriction();
                return predict;
            }
        };
        long count = tawSystemUserRefRoleRepository.count(tawSystemUserRefRoleSpecification);
        if (count > 0){
            return RestResultFactory.createErrorResult("当前角色有用户在使用，不能够删除");
        }
        tawSystemRoleRepository.deleteByRoleIdAndDeptId(roleId,deptId);
        return RestResultFactory.createSuccessResult("删除成功");
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @Override
    @Transactional
    public RestResult modifyRole(TawSystemRole role) {
        //判断当前角色是否是当前用户所有，是否被其他用户所占用
        TawSystemRole tawSystemRole1 = new TawSystemRole();
        tawSystemRole1.setParentId(role.getRoleId());
        String s = currentUserId();
        Boolean aBoolean = judgeRole(s, tawSystemRole1);
        if (!aBoolean){
            return RestResultFactory.createErrorResult("删除的角色不归属于当前用户所有");
        }
        Optional<TawSystemRole> optional = tawSystemRoleRepository.findById(role.getRoleId());
        TawSystemRole tawSystemRole = optional.orElseGet(null);
        if (tawSystemRole == null){
            RestResultFactory.createSuccessResult("数据库中没有相关记录");
        }
        tawSystemRoleRepository.saveAndFlush(role);
        return RestResultFactory.createSuccessResult("修改成功");
    }

    /**
     * 查询角色
     * @param roleName
     * @param deptId
     * @return
     */
    @Override
    public RestResult queryRole(String roleName, String deptId) {
        if ("".equals(roleName)){
            List<TawSystemRole> list = tawSystemRoleRepository.findAllByDeptId(deptId);

            //没有构建树状的结构，考虑怎么构建树状的结构
            return RestResultFactory.createSuccessResult(list);
        }
        String roleName1="%"+roleName+"%";
        List<TawSystemRole> list1 =  tawSystemRoleRepository.findAllByDeptIdAndRoleNames(roleName1,deptId);
        return RestResultFactory.createSuccessResult(list1);
    }

    /**
     * 使用树状结构获取相应的角色列表
     * @return
     */
    public RestResult queryRoleByTree(String deptId){
        List<TawSystemRole> tawSystemRoles = tawSystemRoleRepository.findAllByDeptId(deptId);
        TawSystemRole tawSystemRoleObject = null;
        //取出父节点
        for (TawSystemRole tawSystemRole : tawSystemRoles) {
            if (tawSystemRole.getParentId() == -1){
                tawSystemRoleObject = tawSystemRole;
                break;
            }
        }
        //根据父节点，逐层创建数据，下面需要使用递归
        if (tawSystemRoleObject == null){
            return RestResultFactory.createErrorResult("角色查询失败，请重新尝试");
        }
        HashMap<String, Object> oneMap = new HashMap<>();
        oneMap.put("parent",tawSystemRoleObject);
        buildChildRole(tawSystemRoleObject,oneMap);
        ArrayList<Map> maps = new ArrayList<>();
        maps.add(oneMap);
        return RestResultFactory.createSuccessResult(maps);
    }

    //根据父节点，构建子节点
    private void buildChildRole(TawSystemRole tawSystemRoleObject, Map<String, Object> oneMap) {
        List<Map> childMaps = new ArrayList<>();
        List<TawSystemRole> childList = tawSystemRoleRepository.findAllByParentId(tawSystemRoleObject.getRoleId());
        for (TawSystemRole tawSystemRole : childList) {
            Map<String,Object> oneChildMap = new HashMap<>();
            oneChildMap.put("parent",tawSystemRole);
            buildChildRole(tawSystemRole,oneChildMap);
            childMaps.add(oneChildMap);
        }
        oneMap.put("children",childMaps);
    }

    @Autowired
    private TawSystemDeptRepository tawSystemDeptRepository;

    /**
     * 添加部门
     * @param dept
     * @return
     */
    @Override
    @Transactional
    public RestResult addDept(TawSystemDept dept) {
        //需要判断当前用户的部门，添加的部门必须是属于当前用户的子部门
        TawSystemDept tawSystemDept = getCurrentDept();
        if (tawSystemDept == null){
            return RestResultFactory.createErrorResult("当前用户的部门不存在");
        }
        //查询添加的部门是否属于当前用户的部门或者他的子部门
        Boolean flat = judgeDept1(tawSystemDept,dept);
        if (!flat){
            return RestResultFactory.createErrorResult("当前部门不归属于当前用户的部门或者子部门");
        }
        UUID uuid = UUID.randomUUID();
        dept.setDeptid(uuid.toString());
        tawSystemDeptRepository.saveAndFlush(dept);
        return RestResultFactory.createSuccessResult("添加部门成功");
    }

    /**
     * 判断添加的部门是否属于当前部门或者他的子部门
     * @param
     * @return
     */
    private Boolean judgeDept(TawSystemDept currentDept, TawSystemDept addDept) {
        if (currentDept.getDeptid().equals(addDept.getDeptid())){
            return true;
        }
        List<TawSystemDept> allDept = tawSystemDeptRepository.findAllByParentdeptid(currentDept.getDeptid());
        if (allDept == null || allDept.size() == 0){
            return false;
        }
        //进行进一步的判断
        for (TawSystemDept tawSystemDept : allDept) {
            Boolean aBoolean = judgeDept(tawSystemDept, addDept);
            if (aBoolean){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断添加的部门是否属于当前部门或者他的子部门
     * @param
     * @return
     */
    private Boolean judgeDept1(TawSystemDept currentDept, TawSystemDept addDept) {
        if (currentDept.getDeptid().equals(addDept.getParentdeptid())){
            return true;
        }
        List<TawSystemDept> allDept = tawSystemDeptRepository.findAllByParentdeptid(currentDept.getDeptid());
        if (allDept == null || allDept.size() == 0){
            return false;
        }
        //进行进一步的判断
        for (TawSystemDept tawSystemDept : allDept) {
            Boolean aBoolean = judgeDept(tawSystemDept, addDept);
            if (aBoolean){
                return true;
            }
        }
        return false;
    }
    //获取当前用户所在的部门
    private TawSystemDept getCurrentDept() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            com.tongtech.auth.vo.RestResult<String> failedResult = com.tongtech.auth.vo.RestResultFactory.createFailedResult("无法获取当前用户信息");
            failedResult.setStatus(1);
            return null;
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        Integer userId = user.getVoUserMenu().getId();
        TawSystemDept tawDept = tawSystemDeptRepository.findDeptByUserId(userId + "");
        return tawDept;
    }

    /**
     * 删除部门
     * @param id
     * @return
     */
    @Override
    @Transactional
    public synchronized RestResult deleteDept(Integer id) {
        TawSystemDept tawSystemDept = tawSystemDeptRepository.findById(id).orElseGet(null);
        if (tawSystemDept == null){
            return RestResultFactory.createErrorResult("数据库中没有相关数据");
        }
        List<TawSystemDept> deptList = tawSystemDeptRepository.findAllByParentdeptid(tawSystemDept.getDeptid());
        if (deptList != null && deptList.size() > 0){
            return RestResultFactory.createErrorResult("当前部门有子部门存在");
        }
        TawSystemDept currentDept = getCurrentDept();
        Boolean aBoolean = judgeDept1(currentDept, tawSystemDept);
        if (!aBoolean){
            return RestResultFactory.createErrorResult("所删除部门不属于当前用户所在部门的子部门");
        }
        tawSystemDeptRepository.deleteById(id);
        return RestResultFactory.createSuccessResult("删除部门成功");
    }

    /**
     * 修改部门信息
     * @param dept
     * @return
     */
    @Override
    @Transactional
    public synchronized RestResult modifyDept(TawSystemDept dept) {
        Integer id =dept.getId();
        TawSystemDept tawSystemDept = tawSystemDeptRepository.findById(id).orElseGet(null);
        if (tawSystemDept == null){
            return RestResultFactory.createErrorResult("数据库中没有相关数据");
        }

        TawSystemDept currentDept = getCurrentDept();
        Boolean aBoolean = judgeDept(currentDept, tawSystemDept);
        if (!aBoolean){
            return RestResultFactory.createErrorResult("所修改部门不属于当前用户所在部门的子部门");
        }
        tawSystemDeptRepository.saveAndFlush(dept);
        return RestResultFactory.createSuccessResult("修改成功");
    }

    /**
     * 查询部门
     * @param deptName
     * @param deptId
     * @return
     */
    @Override
    public RestResult queryDept(String deptName, String deptId) {
//        if ("".equals(deptName)){
//            List<TawSystemDept> deptList = tawSystemDeptRepository.findAllByParentdeptid(deptId);
//            //构建部门的树状结构,这里先不构建部门的树状结构了。
//            //查询所有的情况下，需要构建树状结构
//            Object object = getTreeList(deptList,deptId);
//            List list = new ArrayList<>();
//            list.add(object);
//            return RestResultFactory.createSuccessResult(list);
//        }
        TawSystemDept taw = tawSystemDeptRepository.findByDeptid(deptId);
        List<TawSystemDept> list1 = new ArrayList<>();
        list1.add(taw);
        findChildDept(list1,taw);
        if (("".equals(deptName))){
            return RestResultFactory.createSuccessResult(list1);
        }
        String modifyDeptName="%" + deptName + "%";
        Specification<TawSystemDept> tawSystemDeptSpecification = new Specification<TawSystemDept>() {
            @Override
            public Predicate toPredicate(Root<TawSystemDept> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<Predicate>();
                list.add(criteriaBuilder.like(root.get("deptname").as(String.class),modifyDeptName));
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("deptid").as(String.class));
                for (TawSystemDept tawSystemDept : list1) {
                    in.value(tawSystemDept.getDeptid());
                }
                list.add(in);
                Predicate predict = query.where(list.toArray(new Predicate[list.size()])).getRestriction();
                return predict;
            }
        };
        List<TawSystemDept> tawSystemDeptList = tawSystemDeptRepository.findAll(tawSystemDeptSpecification);
//        List<TawSystemDept> deptLists = tawSystemDeptRepository.findAllByDeptNameAndDeptIds(modifyDeptName,deptId);
        return RestResultFactory.createSuccessResult(tawSystemDeptList);
    }

    //构建树状的列表
    private Object getTreeList(List<TawSystemDept> deptList, String deptId) {
        TawSystemDept tawdept = tawSystemDeptRepository.findByDeptid(deptId);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("parent",tawdept);
        List<Map> list = new ArrayList<Map>();
        for (TawSystemDept tawSystemDept : deptList) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("parent",tawSystemDept);
            madeTree1(tawSystemDept,map);
            list.add(map);
        }
        resultMap.put("childList",list);
        return resultMap;
    }

    private void madeTree1(TawSystemDept tawSystemDept, Map<String, Object> map) {
        List<TawSystemDept> depts = tawSystemDeptRepository.findAllByParentdeptid(tawSystemDept.getDeptid());
        List<Map> list = new ArrayList<Map>();
        if (depts.size() >0){
            for (TawSystemDept dept : depts) {
                Map map1 = new HashMap<String,Object>();
                map1.put("parent",dept);
                madeTree1(dept,map1);
                list.add(map1);
            }
            map.put("childList",list);
        }
    }

    //创建一棵树状结构
    private void madeTree(List<TawSystemDept> deptList, Map<String, Object> resultMap, String deptId) {
        List<Map> list = new ArrayList<Map>();
        for (TawSystemDept tawSystemDept : deptList) {
            if (tawSystemDept.getParentdeptid().equals(deptId)){
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("parent",tawSystemDept);
                list.add(map);
            }
        }
        resultMap.put("childList",list);
        //如果存在子类，再进行继续的迭代
        if (list.size() > 0){
            for (Map map : list) {
                TawSystemDept parent = (TawSystemDept) map.get("parent");
                madeTree(deptList,map,parent.getDeptid());
            }
        }
    }

    @Autowired
    private TawSystemUserRepository tawSystemUserRepository;

    @Autowired
    private TawSystemUserRefRoleRepository tawSystemUserRefRoleRepository;
    /**
     * 添加用户
     * @param user
     * @return
     */
    @Override
    @Transactional
    public RestResult addUser(TawSystemUser user) {
        //用户的id是字符串，需要使用uuid来进行生成
        String id = UUID.randomUUID().toString();
        user.setId(id);
        //用户的userid需要和系统的相应的用户进行绑定
        DbSysUser flag = isSystemUser(user);
//        Boolean flag = false;
        if (flag == null){
            return RestResultFactory.createErrorResult("主系统中不存在该用户");
        }
        //判断当前用户所分配的部门是否是当前部门的子部门，这个暂时先不写
        tawSystemUserRepository.saveAndFlush(user);
        //用户保存成功，查看是否有相应的角色需要关联
        List<Integer> roleList = user.getRoleList();
        if (roleList != null && roleList.size() > 0) {
            ArrayList<TawSystemUserRefRole> tawSystemUserRefRoles =new ArrayList<>();
            for (Integer tawSystemRole : roleList) {
                TawSystemUserRefRole tawSystemUserRefRole=new TawSystemUserRefRole();
                tawSystemUserRefRole.setUserid(user.getId());
                tawSystemUserRefRole.setUsername(user.getUsername());
                tawSystemUserRefRole.setRoleid(tawSystemRole);
//                tawSystemUserRefRole.setRolename(tawSystemRole.getRoleName());
                tawSystemUserRefRoles.add(tawSystemUserRefRole);
            }
                tawSystemUserRefRoleRepository.saveAll(tawSystemUserRefRoles);
        }
        return RestResultFactory.createSuccessResult("用户添加成功");
    }

    /**
     * 删除用户
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public synchronized RestResult deleteUser(String userId) {
        //需要判断删除的用户是否属于当前用户所在的部门，或者他的子部门
        TawSystemUser tawSystemUser = tawSystemUserRepository.findById(userId).orElse(null);
        if (tawSystemUser == null){
            return RestResultFactory.createErrorResult("要删除的用户不存在");
        }
        String deptid = tawSystemUser.getDeptid();
        TawSystemDept tawSystemDept = tawSystemDeptRepository.findByDeptid(deptid);
        //查询添加的部门是否属于当前用户的部门或者他的子部门
        TawSystemDept currentDept = getCurrentDept();
        Boolean aBoolean = judgeDept(currentDept, tawSystemDept);
        if (!aBoolean){
            return RestResultFactory.createErrorResult("所删除用户不属于当前用户所在部门的子部门");
        }
        //根据id删除用户
        tawSystemUserRepository.deleteById(userId);
        return RestResultFactory.createSuccessResult("用户删除成功");
    }

    /**
     * 修改用户
     * @param user
     * @return
     */
    @Override
    @Transactional
    public synchronized RestResult modifyUser(TawSystemUser user) {
        //需要舔加锁，控制住当如果有用户的时候，不能够删除，否则，就会造成用户的创建，不符合业务的需求，暂时加到方法上，这样虽然锁的粒度大一些，也能够满足需要
        TawSystemUser tawSystemUser = tawSystemUserRepository.findById(user.getId()).orElseGet(null);
        if (tawSystemUser==null){
            return RestResultFactory.createErrorResult("数据库当中不存在该用户，不能够进行修改");
        }
        //需要判断删除的用户是否属于当前用户所在的部门，或者他的子部门
        String deptid = tawSystemUser.getDeptid();
        TawSystemDept tawSystemDept = tawSystemDeptRepository.findByDeptid(deptid);
        //查询添加的部门是否属于当前用户的部门或者他的子部门
        TawSystemDept currentDept = getCurrentDept();
        if (currentDept == null){
            return RestResultFactory.createErrorResult("当前登录用户不存在系统关联用户，故当前用户不存在部门");
        }
        Boolean aBoolean = judgeDept(currentDept, tawSystemDept);
        if (!aBoolean){
            return RestResultFactory.createErrorResult("所修改用户不属于当前用户所在部门的子部门");
        }
        tawSystemUserRepository.saveAndFlush(user);
        List<Integer> roleList = user.getRoleList();
        if (roleList != null && roleList.size() > 0){
            //首先需要删除所有的关联数据
            tawSystemUserRefRoleRepository.deleteAllByUserid(user.getId());
            ArrayList<TawSystemUserRefRole> tawSystemUserRefRoles =new ArrayList<>();
            for (Integer tawSystemRole : roleList) {
                TawSystemUserRefRole tawSystemUserRefRole=new TawSystemUserRefRole();
                tawSystemUserRefRole.setUserid(user.getId());
                tawSystemUserRefRole.setUsername(user.getUsername());
                tawSystemUserRefRole.setRoleid(tawSystemRole);
//                tawSystemUserRefRole.setRolename(tawSystemRole.getRoleName());
                tawSystemUserRefRoles.add(tawSystemUserRefRole);
            }
            tawSystemUserRefRoleRepository.saveAll(tawSystemUserRefRoles);
        }
        return RestResultFactory.createSuccessResult("用户修改成功");
    }

    /**
     * 进行用户的查询
     * @param userName
     * @param deptId
     * @return
     */
    @Override
    public RestResult queryUser(String userName, String deptId) {
        //根据部门id来进行逐层的查找，查找出来所有的部门，然后再根据这些部门来进行角色的查找
        //查找所有的部门
        List<TawSystemDept> list = new ArrayList<>();
        TawSystemDept tawSystemDept = tawSystemDeptRepository.findByDeptid(deptId);
        list.add(tawSystemDept);
        findChildDept(list,tawSystemDept);
        //根据所有的部门查找所有的用户
        Specification<TawSystemUser> tawSystemUserSpecification = new Specification<TawSystemUser>() {
            @Override
            public Predicate toPredicate(Root<TawSystemUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get("deptid").as(String.class));
                for (TawSystemDept systemDept : list) {
                    in.value(systemDept.getDeptid());
                }
                Predicate predict = query.where(in).getRestriction();
                return predict;
            }
        };
        List<TawSystemUser> tawSystemUsers = tawSystemUserRepository.findAll(tawSystemUserSpecification);
        for (TawSystemUser tawSystemUser : tawSystemUsers) {
            List<TawSystemRole> roleList = tawSystemRoleRepository.findUserRoles1(tawSystemUser.getId());
            tawSystemUser.setRoleObjectList(roleList);
            ArrayList<Integer> idList = new ArrayList<>();
            for (TawSystemRole tawSystemRole : roleList) {
                idList.add(tawSystemRole.getRoleId());
            }
            tawSystemUser.setRoleList(idList);
        }
        return RestResultFactory.createSuccessResult(tawSystemUsers);
    }

    /**
     * 查询当前登录用户的部门及其子部门
     * @return
     */
    @Override
    public RestResult queryCurrentLoginUserDept() {
        TawSystemDept currentDept = getCurrentDept();
        if (currentDept == null){
            return RestResultFactory.createErrorResult("当前登录用户没有部门");
        }
        List<TawSystemDept> list = new ArrayList<TawSystemDept>();
        list.add(currentDept);
        findChildDept(list,currentDept);
        return RestResultFactory.createSuccessResult(list);
    }

    /**
     * 构建用户和角色的树状结构
     * @param deptId
     * @return
     */
    public RestResult queryTreeOfRoleAndUser1(String deptId) {
        List<TawSystemRole> tawSystemRoles = tawSystemRoleRepository.findAllByDeptId(deptId);
        TawSystemRole tawSystemRoleObject = null;
        //取出父节点
        for (TawSystemRole tawSystemRole : tawSystemRoles) {
            if (tawSystemRole.getParentId() == -1){
                tawSystemRoleObject = tawSystemRole;
                break;
            }
        }
        //根据父节点，逐层创建数据，下面需要使用递归
        if (tawSystemRoleObject == null){
            return RestResultFactory.createErrorResult("角色查询失败，请重新尝试");
        }
        HashMap<String, Object> oneMap = new HashMap<>();
        oneMap.put("parent",tawSystemRoleObject);
        List<TawSystemUser> userList = tawSystemUserRepository.findAllUserByRole(tawSystemRoleObject.getRoleId());
        oneMap.put("users",userList);
        buildChildRole1(tawSystemRoleObject,oneMap);
        ArrayList<Map> maps = new ArrayList<>();
        maps.add(oneMap);
        return RestResultFactory.createSuccessResult(maps);
    }


    /**
     * 构建用户和角色的树状结构
     * @param deptId
     * @return
     */
    @Override
    public RestResult queryTreeOfRoleAndUser(String deptId) {
        List<TawSystemRole> tawSystemRoles = tawSystemRoleRepository.findAllByDeptId(deptId);
        TawSystemRole tawSystemRoleObject = null;
        //取出父节点
        for (TawSystemRole tawSystemRole : tawSystemRoles) {
            if (tawSystemRole.getParentId() == -1){
                tawSystemRoleObject = tawSystemRole;
                break;
            }
        }
        //根据父节点，逐层创建数据，下面需要使用递归
        if (tawSystemRoleObject == null){
            return RestResultFactory.createErrorResult("角色查询失败，请重新尝试");
        }
        //直接返回{status:number.data?:[{id:string,name:string,pId:string}],message?:string}
        List<Map> resultList = new ArrayList<>();
        HashMap<String, Object> oneMap = new HashMap<>();
        oneMap.put("id",tawSystemRoleObject.getRoleId().toString());
        oneMap.put("name",tawSystemRoleObject.getRoleName());
        oneMap.put("pId","-1");
        resultList.add(oneMap);
        List<TawSystemUser> userList = tawSystemUserRepository.findAllUserByRole(tawSystemRoleObject.getRoleId());
        for (TawSystemUser tawSystemUser : userList) {
            HashMap<String, Object> oneMap1 = new HashMap<>();
            oneMap1.put("id",tawSystemUser.getUserid()+"u");
            oneMap1.put("name",tawSystemUser.getUsername());
            oneMap1.put("pId",tawSystemRoleObject.getRoleId()+"");
            resultList.add(oneMap1);
        }
        buildChildRole2(tawSystemRoleObject,resultList);
        return RestResultFactory.createSuccessResult(resultList);
    }

    //根据父节点，构建子节点
    private void buildChildRole2(TawSystemRole tawSystemRoleObject, List<Map> resultList) {
        List<TawSystemRole> childList = tawSystemRoleRepository.findAllByParentId(tawSystemRoleObject.getRoleId());
        for (TawSystemRole tawSystemRole : childList) {
            HashMap<String, Object> oneMap1 = new HashMap<>();
            oneMap1.put("id",tawSystemRole.getRoleId()+"");
            oneMap1.put("name",tawSystemRole.getRoleName());
            oneMap1.put("pId",tawSystemRoleObject.getRoleId()+"");
            resultList.add(oneMap1);
            List<TawSystemUser> userList = tawSystemUserRepository.findAllUserByRole(tawSystemRole.getRoleId());
            for (TawSystemUser tawSystemUser : userList) {
                HashMap<String, Object> oneMap2 = new HashMap<>();
                oneMap2.put("id",tawSystemUser.getUserid()+"u");
                oneMap2.put("name",tawSystemUser.getUsername());
                oneMap2.put("pId",tawSystemRole.getRoleId()+"");
                resultList.add(oneMap2);
            }
            buildChildRole2(tawSystemRole,resultList);
        }
    }

    //根据父节点，构建子节点
    private void buildChildRole1(TawSystemRole tawSystemRoleObject, Map<String, Object> oneMap) {
        List<Map> childMaps = new ArrayList<>();
        List<TawSystemRole> childList = tawSystemRoleRepository.findAllByParentId(tawSystemRoleObject.getRoleId());
        for (TawSystemRole tawSystemRole : childList) {
            Map<String,Object> oneChildMap = new HashMap<>();
            oneChildMap.put("parent",tawSystemRole);
            List<TawSystemUser> userList = tawSystemUserRepository.findAllUserByRole(tawSystemRole.getRoleId());
            oneMap.put("users",userList);
            buildChildRole(tawSystemRole,oneChildMap);
            childMaps.add(oneChildMap);
        }
        oneMap.put("children",childMaps);
    }

    //查找所有的部门
    private void findChildDept(List<TawSystemDept> list, TawSystemDept tawSystemDept) {
        List<TawSystemDept> depts = tawSystemDeptRepository.findAllByParentdeptid(tawSystemDept.getDeptid());
        if (depts.size() > 0){
            list.addAll(depts);
            for (TawSystemDept dept : depts) {
                findChildDept(list,dept);
            }
        }
    }


    @Autowired
    private DbsysUserRepository dbsysUserRepository;
    /**
     * 判断该用户是否和主系统中的用户相关联
     * @param user
     * @return
     */
    private DbSysUser isSystemUser(TawSystemUser user) {
        //判断主系统中是否有这个角色，而且在这个系统当中的这个字段，以后需要加一个唯一索引
        String userid = user.getUserid();
        DbSysUser dbSysUser = dbsysUserRepository.findById(Integer.parseInt(userid)).orElseGet(null);
        if (dbSysUser == null){
            return null;
        }
        return dbSysUser;
    }


}
