package com.tongtech.auth.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;
import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_department.DbSysDepartment;
import com.tongtech.auth.data.db_sys_department.DbSysDepartmentRepository;
import com.tongtech.auth.data.db_sys_relation_ru.SysRelationRu;
import com.tongtech.auth.data.db_sys_relation_ru.SysRelationRuRepository;
import com.tongtech.auth.data.db_sys_role.DbSysRole;
import com.tongtech.auth.data.db_sys_role.DbSysRoleRepository;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import com.tongtech.auth.service.UserService;
import com.tongtech.auth.utils.MD5;
import com.tongtech.auth.utils.ObjectParserUtil;
import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class UserServiceImpl  implements UserService {

    @Autowired
    private DbsysUserRepository dbsysUserRepository;
    @Autowired
    private SysRelationRuRepository sysRelationRuRepository;
    @Autowired
    private DbSysDepartmentRepository dbSysDepartmentRepository;
    /**
     * 获取用户信息
     * @param model
     * @return
     */
    @Override
    public RestResult getUserList(Map<String, Object> model) {
        Integer pageIndex = ObjectParserUtil.toInteger(model.get("pageIndex"), 1);
        Integer pageSize = ObjectParserUtil.toInteger(model.get("pageSize"), 10);
        Integer total = ObjectParserUtil.toInteger(model.get("total"), 0);
        String loginName = ObjectParserUtil.toString(model.get("loginName"));
        String status = ObjectParserUtil.toString(model.get("status"));
        String gender = ObjectParserUtil.toString(model.get("gender"));
        String realName = ObjectParserUtil.toString(model.get("realName"));
        String mobile = ObjectParserUtil.toString(model.get("mobile"));
        String deptId = ObjectParserUtil.toString(model.get("deptId"));
        String roleId = ObjectParserUtil.toString(model.get("roleId"));
        Pageable pageable=new PageRequest(pageIndex-1,pageSize,new Sort(Sort.Direction.ASC,"id"));
        Specification<DbSysUser> dbSysUserSpecification = new Specification<DbSysUser>() {
            @Override
            public Predicate toPredicate(Root<DbSysUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                if (loginName != null && loginName.length() > 0) {
                    list.add(criteriaBuilder.like(root.get("loginName"),"%"+loginName+"%"));
                }
                if (status != null && status.length() > 0) {
                    list.add(criteriaBuilder.equal(root.get("status").as(Integer.class),Integer.parseInt(status)));
                }
                if (gender != null && gender.length() > 0) {
                    list.add(criteriaBuilder.equal(root.get("gender").as(String.class),gender));
                }
                if (realName != null && realName.length() > 0) {
                    list.add(criteriaBuilder.equal(root.get("realName").as(String.class),realName));
                }
                if (mobile != null && mobile.length() > 0) {
                    list.add(criteriaBuilder.equal(root.get("mobile").as(String.class),mobile));
                }
                if (deptId != null && deptId.length() > 0) {
                    list.add(criteriaBuilder.equal(root.get("deptId").as(Integer.class),deptId));
                }
                if (roleId != null && roleId.length() > 0) {
                    List<SysRelationRu> ruList = sysRelationRuRepository.findAllByRoleId(Integer.parseInt(roleId));
                    if (ruList.size()>0){
                        CriteriaBuilder.In<Integer> in = criteriaBuilder.in(root.get("id").as(Integer.class));
                        for (SysRelationRu sysRelationRu : ruList) {
                            in.value(sysRelationRu.getUserId());
                        }
                        list.add(in);
                    }
                }
                query.where(list.toArray(new Predicate[list.size()]));
                return query.getRestriction();
            }
        };
        Page<DbSysUser> page = dbsysUserRepository.findAll(dbSysUserSpecification, pageable);
        JSONObject jo = new JSONObject();
        if (total > 0) {
            jo.put("total", total);
        } else {
            jo.put("total", page.getTotalElements());
        }
        jo.put("list", page.getContent());
        return RestResultFactory.createSuccessResult(jo);
    }

    /**
     * 根据id删除用户
     * @param id
     * @return
     */
    @Transactional
    @Override
    public RestResult deleteControl(String id) {
        try {
            dbsysUserRepository.deleteById(Integer.parseInt(id));
            return RestResultFactory.createSuccessResult("角色删除成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("角色删除失败，请重新尝试");
        }
    }

    /**
     * 更新用户
     * @param id
     * @param operType
     * @param value
     * @return
     */
    @Transactional
    @Override
    public RestResult updateUser(String id, String operType, String value) {
        Optional<DbSysUser> userOptional = dbsysUserRepository.findById(Integer.parseInt(id));
        if (!userOptional.isPresent()){
            return RestResultFactory.createFailedResult("操作用户不存在");
        }
        DbSysUser model = userOptional.get();
        if (operType.equals("frozen")) {
            if (model.getStatus() == 1) {
                return RestResultFactory.createFailedResult("当前用户为锁定，不须解封");
            }
            model.setStatus(1);
        } else if (operType.equals("modify")) {
            String encode = MD5.encode(value.trim());
            model.setPassword(encode);
        } else {
            return RestResultFactory.createFailedResult("操作类型不存在");
        }
        try {
            dbsysUserRepository.save(model);
            return RestResultFactory.createSuccessResult("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("更新失败，请重试");
        }
    }

    @Transactional
    @Override
    public  RestResult insertUser(DbSysUser model) {
        model.setCreateTime(new Date());
        try {
            String password = model.getPassword();
            if (password != null && !("".equals(password.trim()))){
                String encode = MD5.encode(password.trim());
                model.setPassword(encode);
            }
            dbsysUserRepository.save(model);
            //用户保存成功，用户当中的角色进行保存
            List<Integer> dbSysRoleList = model.getRoleIdList();
            if (dbSysRoleList != null && dbSysRoleList.size() > 0){
                List<SysRelationRu> list=new ArrayList<>();
                for (Integer dbSysRole : dbSysRoleList) {
                    SysRelationRu sysRelationRu = new SysRelationRu();
                    sysRelationRu.setRoleId(dbSysRole);
                    sysRelationRu.setUserId(model.getId());
                    list.add(sysRelationRu);
                }
                sysRelationRuRepository.saveAll(list);
            }
            return RestResultFactory.createSuccessResult("插入用户成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("插入用户失败，请重试");
        }
    }

    /**
     * 获取部门信息
     * @return
     */
    @Override
    public RestResult getDept() {

        List<DbSysDepartment> list = dbSysDepartmentRepository.findAllByOrderByOrderSortDesc();
        if (true) {
            DbSysDepartment dbSysDepartment = GetUserDept();
            if (dbSysDepartment==null){
                return RestResultFactory.createFailedResult("用户不存在部门");
            }
            Integer currentDeptId = dbSysDepartment.getId();
            DbSysDepartment CurrentDept = null;
            for (DbSysDepartment item : list
            ) {
                if (item.getId() == currentDeptId) {
                    CurrentDept = item;
                    break;
                }
            }
            if (CurrentDept == null) {
                return RestResultFactory.createSuccessResult(new ArrayList<>());
            }
            List<DbSysDepartment> result = GetDeptList(currentDeptId, list);
            result.add(0, CurrentDept);
            return RestResultFactory.createSuccessResult(result);
        }
        //这里没有必要返回全部，走不到这里
        return RestResultFactory.createSuccessResult(list);
    }

    @Override
    public RestResult getUserInfo(String id) {
        Optional<DbSysUser> userOptional = dbsysUserRepository.findById(Integer.parseInt(id));
        if (!userOptional.isPresent()){
            return RestResultFactory.createFailedResult("用户不存在");
        }
        DbSysUser user = userOptional.get();
        try {
            HashMap<String, Object> resultMap = convertToMap(user);
            List<SysRelationRu> list = sysRelationRuRepository.findAllByUserId(user.getId());
            List<Integer> roleIds = new ArrayList<>();
            for (SysRelationRu ru : list
            ) {
                roleIds.add(ru.getRoleId());
            }
            resultMap.put("roleIdList",roleIds);
            return RestResultFactory.createSuccessResult(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("获取用户信息失败，请重试");
        }
    }

    @Override
    public RestResult findAllUser(String userName) {
//        Specification<DbSysUser> specification = new Specification() {
//            @Override
//            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
//                if (!"".equals(userName)){
//                    criteriaBuilder.like(root.get("loginName").as(String.class),userName);
//                }
//                return null;
//
//            }
//        };
//        if ("".equals(userName))
        return null;
    }

    /**
     * 获取系统中所有的用户
     * @return
     */
    @Override
    public RestResult getAllUser() {
        List<DbSysUser> dbSysUsers = dbsysUserRepository.findAll();
        return RestResultFactory.createSuccessResult(dbSysUsers);
    }

    private HashMap<String, Object> convertToMap(Object obj)
            throws Exception {

        HashMap<String, Object> map = new HashMap<String, Object>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            String varName = fields[i].getName();
            boolean accessFlag = fields[i].isAccessible();
            fields[i].setAccessible(true);

            Object o = fields[i].get(obj);
            if (o != null)
                map.put(varName, o.toString());

            fields[i].setAccessible(accessFlag);
        }

        return map;
    }

    private DbSysDepartment GetUserDept() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null && principal instanceof CustomUser){
            CustomUser customUser= (CustomUser) principal;
            DbSysUser dbSysUser = customUser.getVoUserMenu();
            Integer deptId=dbSysUser.getDeptId();
            if (deptId==null){
                return null;
            }
            Optional<DbSysDepartment> deptOption = dbSysDepartmentRepository.findById(deptId);
            if (!deptOption.isPresent()){
                return null;
            }
            DbSysDepartment dbSysDepartment = deptOption.get();
            return dbSysDepartment;
        }
        return null;
    }

    private List<DbSysDepartment> GetDeptList(Integer parentId, List<DbSysDepartment> list) {
        List<DbSysDepartment> result = new ArrayList<>();
        for (DbSysDepartment item : list
        ) {
            if (item.getParentId() == parentId) {
                result.add(item);

                List<DbSysDepartment> chilren = GetDeptList(item.getId(), list);
                if (chilren == null) {
                    continue;
                }
                if (chilren.size() == 0) {
                    continue;
                }
                result.addAll(chilren);
            }
        }
        return result;
    }
}
