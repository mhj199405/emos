package com.tongtech.auth.service.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_data_control.SysDataControl;
import com.tongtech.auth.data.db_sys_data_control.SysDataControlRepository;
import com.tongtech.auth.data.db_sys_department.DbSysDepartment;
import com.tongtech.auth.data.db_sys_department.DbSysDepartmentRepository;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_menu.DbSysMenuRepository;
import com.tongtech.auth.data.db_sys_relation_dd.SysRelationDd;
import com.tongtech.auth.data.db_sys_relation_dd.SysRelationDdRepository;
import com.tongtech.auth.data.db_sys_relation_dm.SysRelationDm;
import com.tongtech.auth.data.db_sys_relation_dm.SysRelationDmRepository;
import com.tongtech.auth.data.db_sys_relation_rd.SysRelationRdRepository;
import com.tongtech.auth.data.db_sys_relation_rm.DbSysRelationRmRepository;
import com.tongtech.auth.data.db_sys_role.DbSysRole;
import com.tongtech.auth.data.db_sys_role.DbSysRoleRepository;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.data.db_sys_user.DbsysUserRepository;
import com.tongtech.auth.service.DeptService;
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
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DbSysDepartmentRepository dbSysDepartmentRepository;
    @Autowired
    private DbsysUserRepository dbsysUserRepository;
    @Autowired
    private SysDataControlRepository sysDataControlRepository;
    @Autowired
    private DbSysMenuRepository dbSysMenuRepository;
    @Autowired
    private SysRelationDdRepository sysRelationDdRepository;
    @Autowired
    private SysRelationDmRepository sysRelationDmRepository;
    @Autowired
    private DbSysRoleRepository dbSysRoleRepository;
    @Autowired
    private SysRelationRdRepository sysRelationRdRepository;
    @Autowired
    private DbSysRelationRmRepository dbSysRelationRmRepository;
    @Transactional
    @Override
    public RestResult deleteControl(Integer id) {
        if (id == null) {
            return RestResultFactory.createFailedResult("删除部门ID不能为空");
        }
        Optional<DbSysDepartment> deptOptional = dbSysDepartmentRepository.findById(id);
        if (!deptOptional.isPresent()){
            //部门不存在，则直接结束
            return RestResultFactory.createFailedResult("部门不存在");
        }
        DbSysDepartment dept = deptOptional.get();
        if (dept.getParentId() == -1) {
            return RestResultFactory.createFailedResult("根部门无法删除");
        }
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal==null){
            return RestResultFactory.createFailedResult("");
        }
        //查询当前用户的id
        Integer depId=null;
        if (principal instanceof CustomUser){
            CustomUser customUser= (CustomUser) principal;
            depId=customUser.getVoUserMenu().getDeptId();
        }
        Optional<DbSysDepartment> depOptional;
        if (depId==null){
            return RestResultFactory.createFailedResult("");
        }else {
            depOptional = dbSysDepartmentRepository.findById(depId);
            if (!depOptional.isPresent()) {
                return RestResultFactory.createFailedResult("");
            }
        }
        DbSysDepartment current = depOptional.get();
        //判断一个部门是否是另外一个部门的子部门
        if (!IsChildDept(dept.getId(), current)) {
            return RestResultFactory.createFailedResult("删除部门不归属当前部门");
        }

        if (IsExistUser(dept)) {
            return RestResultFactory.createFailedResult("当前部门或者子部门不能有用户");
        }
        List<DbSysDepartment> dbSysDepartmentList= dbSysDepartmentRepository.findChildrenDeptById(dept.getId());
        List<Integer> deptIdList = GetDeptIdByDept(dbSysDepartmentList);
        deptIdList.add(dept.getId());
        try {
            dbSysDepartmentRepository.deleteDeptsByIds(deptIdList);
        } catch (Exception e) {
            return RestResultFactory.createFailedResult("删除过程中出现意外，请重试");
        }
        return RestResultFactory.createSuccessResult("删除成功");
    }

    @Override
    @Transactional
    public RestResult updateControl(DbSysDepartment model) {
        Optional<DbSysDepartment> deptOptional = dbSysDepartmentRepository.findById(model.getId());
        if (!deptOptional.isPresent()){
            return RestResultFactory.createFailedResult("修改规则不存在");
        }
        DbSysDepartment dc = deptOptional.get();
        DbSysDepartment current = GetUserDept();
        if (current.getId() == dc.getId()) {
            if (!current.getDeptName().equals(model.getDeptName())) {
                return RestResultFactory.createFailedResult("无法修改归属部门名称");
            }
        } else {
            if (!IsChildDept(dc.getId(), current)) {
                return RestResultFactory.createFailedResult("修改部门不归属于当前部门");
            }
        }
        if (model.getDeptName().length() == 0) {
            return RestResultFactory.createFailedResult("菜单名称不能为空");
        }
        model.setCreateTime(dc.getCreateTime());

        try {
            dbSysDepartmentRepository.save(model);
        } catch (Exception e) {
            return RestResultFactory.createFailedResult("保存过程中出现意外，请重试");
        }
        return RestResultFactory.createSuccessResult("更新成功");
    }

    @Transactional
    @Override
    public RestResult insertControl(DbSysDepartment model) {

        if (model == null) {
            return RestResultFactory.createFailedResult("参数异常");
        }
        DbSysDepartment current = GetUserDept();
        if (current==null){
            return RestResultFactory.createFailedResult("当前用户没有部门");
        }
        if (!IsChildDept(model.getParentId(), GetUserDept())) {
            return RestResultFactory.createFailedResult("新建部门归属部门ID错误");
        }
        if (model.getDeptName().length() == 0) {
            return RestResultFactory.createFailedResult("菜单名称不能为空");
        }
        model.setCreateTime(new Date());
        // model.setId(SnowIdUtil.uniqueLong());
        try {
            dbSysDepartmentRepository.save(model);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("保存过程中出现意外，请重试");
        }
        return RestResultFactory.createSuccessResult("保存成功");
    }

    @Override
    public RestResult getBaseInfo() {
        DbSysDepartment current=GetUserDept();
        if (current==null){
            return RestResultFactory.createFailedResult("当前用户不存在部门");
        }
        List<DbSysMenu> menuList;
        List<SysDataControl> controlList;
        if (current.getParentId() == -1) {
            controlList = sysDataControlRepository.findAll();
            menuList = dbSysMenuRepository.findMenuList();
        } else {
            controlList=sysDataControlRepository.getDataControlListByDeptId(current.getId());
            //下面这个sql删除1
            menuList=dbSysMenuRepository.getMenuListByDeptId(current.getId());
        }

        JSONObject result = new JSONObject();
        result.put("controlList", controlList);
        result.put("menuList", menuList);
        return RestResultFactory.createSuccessResult(result);
    }

    @Override
    public RestResult getDeptInfo(Integer id) {
        List<DbSysMenu> menuList;
        List<SysDataControl> controlList;
        DbSysDepartment current = GetUserDept();
        if (current==null){
            return RestResultFactory.createFailedResult("当前用户不存在部门");
        }
        if (!IsChildDept(id, current)) {
            return RestResultFactory.createFailedResult("查询部门不归属当前部门");
        }
        controlList = sysDataControlRepository.getDataControlListByDeptId(id);
        menuList = dbSysMenuRepository.getMenuListByDeptId(id);
        JSONObject result = new JSONObject();
        result.put("controlList", controlList);
        result.put("menuList", menuList);
        return RestResultFactory.createSuccessResult(result);
    }

    @Override
    @Transactional
    public RestResult onAuthorized(Map<String, Object> dict) {
        Integer deptId = Integer.valueOf(JSON.toJSONString(dict.get("id")));
        List<DbSysMenu> menuList = JSON.parseArray(JSON.toJSONString(dict.get("menuList")), DbSysMenu.class);
        List<SysDataControl> controlList = JSON.parseArray(JSON.toJSONString(dict.get("controlList")), SysDataControl.class);
        Optional<DbSysDepartment> deptOptional = dbSysDepartmentRepository.findById(deptId);
        if (!deptOptional.isPresent()){
            return RestResultFactory.createFailedResult("授权部门不存在");
        }
        DbSysDepartment dept=deptOptional.get();
        DbSysDepartment current = GetUserDept();
        if (current==null){
            return RestResultFactory.createFailedResult("当前用户部门不存在");
        }
        if (!dept.getParentId().equals(current.getId())){
            return RestResultFactory.createFailedResult("授权部门不是当前用户直属部门，无法授权");
        }
        List<DbSysDepartment> childrenDepts = dbSysDepartmentRepository.findChildrenDeptById(dept.getId());
        List<Integer> children = GetDeptIdByDept(childrenDepts);

        List<SysDataControl> deleteControlList = new ArrayList<>();
        List<DbSysMenu> deleteMenuList = new ArrayList<>();
        if (children.size() > 0) {
            if (menuList.size() > 0) {
                List<DbSysMenu> originalMenuList = dbSysMenuRepository.getMenuListByDeptId(deptId);
                for (DbSysMenu item : originalMenuList) {
                    if (!menuList.contains(item)) {
                        deleteMenuList.add(item);
                    }
                }
            }
            if (controlList.size() > 0) {
                List<SysDataControl> originalControlList = sysDataControlRepository.getDataControlListByDeptId(deptId);
                for (SysDataControl item : originalControlList
                ) {
                    if (!controlList.contains(item)) {
                        deleteControlList.add(item);
                    }
                }
            }

        }

        if (deleteControlList.size() == 0 && deleteMenuList.size() == 0) {
            children = new ArrayList<>();
        }
        //需要删除子部门的相应的权限和相应的操作
        boolean flag = authorized(menuList, controlList, deptId, deleteMenuList, deleteControlList, children);
        if (!flag) {
            return RestResultFactory.createFailedResult("保存过程中出现意外，请重试");
        }
        return RestResultFactory.createSuccessResult("授权成功");
    }

    /**
     * 获取所有的部门
     * @param deptName
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public RestResult getAllDepartment(String deptName, Integer pageIndex, Integer pageSize) {
        Pageable pageable=new PageRequest(pageIndex-1,pageSize,new Sort(Sort.Direction.ASC,"id"));
        Specification<DbSysDepartment> dbSysDepartmentSpecification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list=new ArrayList<>();
                if (!"".equals(deptName)){
                    list.add(criteriaBuilder.like(root.get("deptName").as(String.class),"%"+deptName+"%"));
                }
                query.where(list.toArray(new Predicate[list.size()]));
                return query.getRestriction();
            }
        };
        Page<DbSysDepartment> page = dbSysDepartmentRepository.findAll(dbSysDepartmentSpecification, pageable);
        return RestResultFactory.createSuccessResult(page);
    }

    @Transactional
    public boolean authorized(List<DbSysMenu> menuList, List<SysDataControl> controlList, Integer deptId, List<DbSysMenu> deleteMenuList, List<SysDataControl> deleteControlList, List<Integer> children) {
        try {
            if (controlList != null && controlList.size() > 0) {
                //这里没有处理deptid--roleid--dataid这条路线上的关系，下面处理子类的时候处理了。
                sysRelationDdRepository.deleteByDeptId(deptId);
                List<SysRelationDd> sqlList = new ArrayList<>();
                for (SysDataControl item : controlList
                ) {
                    SysRelationDd model = new SysRelationDd();
                    model.setDataId(item.getId());
                    model.setDataName(item.getDataName());
                    model.setDataOperation(item.getDataOperation());
                    model.setDataParam(item.getDataParam());
                    model.setDataValue1(item.getDataValue1());
                    model.setDataValue2(item.getDataValue2());
                    model.setDeptId(deptId);
                    model.setControlOrder(item.getControlOrder());
                    model.setDataDesc(item.getDataDesc());
                    sqlList.add(model);
                }
                sysRelationDdRepository.saveAll(sqlList);
            }
            if (menuList != null && menuList.size() > 0) {
                sysRelationDmRepository.deleteByDeptId(deptId);

                List<SysRelationDm> sqlList = new ArrayList<>();
                for (DbSysMenu item : menuList
                ) {
                    SysRelationDm model = new SysRelationDm();
                    model.setMenuId(item.getId());
                    model.setDeptId(deptId);
                    sqlList.add(model);
                }
                sysRelationDmRepository.saveAll(sqlList);
            }

            //将指定的部门添加进去
            children.add(deptId);
            List<DbSysRole> roleList = dbSysRoleRepository.findAllByDeptIdIn(children);

            if (deleteControlList.size() > 0) {
                for (SysDataControl item : deleteControlList
                ) {
                    for (Integer childrenDeptId : children
                    ) {
                        sysRelationDdRepository.deleteAllByDeptIdAndDataIds(childrenDeptId,item.getId());
                    }

                    for (DbSysRole role : roleList
                    ) {
                        sysRelationRdRepository.deleteByRoleIdAndDataIds(role.getId(),item.getId());
                    }
                }

            }
            if (deleteMenuList.size() > 0) {
                for (DbSysMenu item : deleteMenuList
                ) {
                    for (Integer childrenDeptId : children
                    ) {
                        sysRelationDmRepository.deleteByMenuIdAndDeptIds(item.getId(),childrenDeptId);
                    }

                    for (DbSysRole role : roleList
                    ) {
                        dbSysRelationRmRepository.deleteByRoleIdAndMenuIds(role.getId(),item.getId());
                    }
                }
            }
            return true;
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
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

    private boolean IsExistUser(DbSysDepartment dept) {
        if (dept == null) {
            return false;
        }
        List<DbSysDepartment> dbSysDepartmentList= dbSysDepartmentRepository.findChildrenDeptById(dept.getId());
        List<Integer> deptIdList = GetDeptIdByDept(dbSysDepartmentList);
        deptIdList.add(dept.getId());
        //查找一个用户，看看是否存在用户
        List<DbSysUser> userList= dbsysUserRepository.findUsersByDeptIds(deptIdList);
        if (userList != null && userList.size()>0) {
            return true;
        }
        return false;
    }

    private List<Integer> GetDeptIdByDept(List<DbSysDepartment> deptList) {
        List<Integer> list = new ArrayList<>();
        if (deptList != null && deptList.size() > 0) {
            for (DbSysDepartment item : deptList
            ) {
                list.add(item.getId());
                List<DbSysDepartment> childDepartments= dbSysDepartmentRepository.findChildrenDeptById(item.getId());
                List<Integer> child = GetDeptIdByDept(childDepartments);
                if (child.size() > 0) {
                    list.addAll(child);
                }
            }
        }
        return list;
    }

    private boolean IsChildDept(Integer deptId, DbSysDepartment current) {
        if (current.getId() == deptId) {
            return true;
        }
        List<DbSysDepartment> dbSysDepartmentList= dbSysDepartmentRepository.findChildrenDeptById(current.getId());
        if (dbSysDepartmentList != null && dbSysDepartmentList.size() > 0) {
            for (DbSysDepartment item : dbSysDepartmentList) {
                boolean temp = IsChildDept(deptId, item);
                if (temp) {
                    return true;
                }
            }
        }
        return false;
    }

}
