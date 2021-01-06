package com.tongtech.auth.service.serviceImpl;

import com.alibaba.fastjson.JSONObject;

import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.data.db_sys_data_control.SysDataControl;
import com.tongtech.auth.data.db_sys_department.DbSysDepartment;
import com.tongtech.auth.data.db_sys_department.DbSysDepartmentRepository;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_menu.DbSysMenuRepository;
import com.tongtech.auth.data.db_sys_relation_rd.SysRelationRd;
import com.tongtech.auth.data.db_sys_relation_rd.SysRelationRdRepository;
import com.tongtech.auth.data.db_sys_relation_rm.DbSysRelationRm;
import com.tongtech.auth.data.db_sys_relation_rm.DbSysRelationRmRepository;
import com.tongtech.auth.data.db_sys_role.DbSysRole;
import com.tongtech.auth.data.db_sys_role.DbSysRoleRepository;
import com.tongtech.auth.data.db_sys_user.DbSysUser;
import com.tongtech.auth.service.RoleService;
import com.tongtech.auth.utils.ObjectParserUtil;
import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import com.tongtech.dao.entity.DepartmentForm;
import com.tongtech.dao.repository.DepartmentFormRepository;
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
import java.util.*;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private DbSysRoleRepository dbSysRoleRepository;

    @Autowired
    private DbSysDepartmentRepository dbSysDepartmentRepository;

    @Autowired
    private SysRelationRdRepository sysRelationRdRepository;

    @Autowired
    private DbSysMenuRepository dbSysMenuRepository;

    @Autowired
    private DbSysRelationRmRepository dbSysRelationRmRepository;
    /**
     * 查询角色的方法
     * @param model
     * @return
     */
    @Override
    public RestResult getDataControlList(Map<String, Object> model) {

        Integer pageIndex = ObjectParserUtil.toInteger(model.get("pageIndex"), 1);
        Integer pageSize = ObjectParserUtil.toInteger(model.get("pageSize"), 10);
        Integer total = ObjectParserUtil.toInteger(model.get("total"), 0);
        String roleName = ObjectParserUtil.toString(model.get("roleName"));
        Integer roleStatus = ObjectParserUtil.toInteger(model.get("roleStatus"), -1);
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        Sort sort = new Sort(order);
        Pageable pageable=new PageRequest(pageIndex-1,pageSize,sort);
        DbSysDepartment dbSysDepartment = GetUserDept();
        if (dbSysDepartment==null){
            return RestResultFactory.createFailedResult("用户部门不存在");
        }
        Specification<DbSysRole> dbSysRoleSpecification = new Specification<DbSysRole>() {
            @Override
            public Predicate toPredicate(Root<DbSysRole> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.equal(root.get("deptId").as(Integer.class),dbSysDepartment.getId()));
                if (roleName != null && roleName.length() > 0) {
                    predicates.add(criteriaBuilder.like(root.get("roleName").as(String.class),"%"+roleName+"%"));
                }
                if (roleStatus >= 0) {
                    predicates.add(criteriaBuilder.equal(root.get("roleStatus").as(Integer.class),roleStatus));
                }

                query.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));
                return query.getRestriction();
            }
        };
        Page<DbSysRole> page = null;
        try {
            page = dbSysRoleRepository.findAll(dbSysRoleSpecification, pageable);
            JSONObject jo = new JSONObject();
            if (total > 0) {
                jo.put("total", total);
            } else {
                jo.put("total", page.getTotalElements());
            }
            jo.put("list", page.getContent());
            return RestResultFactory.createSuccessResult(jo);
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("查询失败，请重新尝试");
        }
    }
    @Autowired
    private DepartmentFormRepository departmentFormRepository;
    /**
     * 删除角色
     * @param id
     * @return
     */
    @Transactional
    @Override
    public  RestResult deleteRole(String id) {
        //加一层判断，与表单存在关联的角色不能够删除
        List<DepartmentForm> departmentForms = departmentFormRepository.findAllByRoleId(Integer.parseInt(id));
        if (departmentForms.size() > 0){
            return RestResultFactory.createFailedResult("存在与角色关联的表单，不能删除该角色");
        }
        try {
            dbSysRoleRepository.deleteByIdWithNotProtected(Integer.parseInt(id));
//            dbSysRoleRepository.deleteById(Integer.parseInt(id));
            return RestResultFactory.createSuccessResult("删除成功");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("删除过程中出现意外，请重试");
        }
    }

    /**
     * 更新角色，由于有创建时间，所以要先判断一下，创建时间存在没有
     * @param model
     * @return
     */
    @Transactional
    @Override
    public RestResult updateRole(DbSysRole model) {
        Optional<DbSysRole> roleOptional = dbSysRoleRepository.findById(model.getId());
        if (!roleOptional.isPresent()){
            model.setCreateTime(new Date());
        }
        model.setUpdateTime(new Date());
        try {
            dbSysRoleRepository.save(model);
            return RestResultFactory.createSuccessResult("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("更新失败，请重试");
        }
    }

    @Transactional
    @Override
    public RestResult insertRole(DbSysRole model) {
        DbSysDepartment dbSysDepartment = GetUserDept();
        if (dbSysDepartment==null){
            return RestResultFactory.createFailedResult("用户部门不存在");
        }
        model.setDeptId(dbSysDepartment.getId());
        model.setCreateTime(new Date());
        //默认设置不被保护
        if (model.getBeProtected() == null){
            model.setBeProtected(0);
        }
        try {
            dbSysRoleRepository.save(model);
            return RestResultFactory.createSuccessResult("插入成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("插入角色失败，请重试");
        }
    }

    /**
     * 获取角色信息
     * @param id
     * @return
     */
    @Override
    public RestResult getRoleInfo(String id) {
        List<DbSysMenu> menuList;
        List<SysDataControl> controlList;
        controlList = sysRelationRdRepository.getDataControlListByRoleId(Integer.parseInt(id));
        menuList=dbSysMenuRepository.getMenuListByRoleId(id);
        JSONObject result = new JSONObject();
        result.put("controlList", controlList);
        result.put("menuList", menuList);
        return RestResultFactory.createSuccessResult(result);
    }

    /**
     * 给角色授予权限
     * @param roleId
     * @param menuList
     * @param controlList
     * @return
     */
    @Transactional
    @Override
    public RestResult onAuthorized(String roleId, List<DbSysMenu> menuList, List<SysDataControl> controlList) {
        Optional<DbSysRole> roleOptional = dbSysRoleRepository.findById(Integer.parseInt(roleId));
        if (!roleOptional.isPresent()){
            return RestResultFactory.createFailedResult("授权角色不存在");
        }
        try {
            if (controlList.size() > 0) {
                sysRelationRdRepository.deleteAllByRoleId(Integer.parseInt(roleId));
                List<SysRelationRd> sqlList = new ArrayList<>();
                for (SysDataControl item : controlList
                ) {
                    SysRelationRd model = new SysRelationRd();
                    model.setDataId(item.getId());
                    model.setDataName(item.getDataName());
                    model.setDataOperation(item.getDataOperation());
                    model.setDataParam(item.getDataParam());
                    model.setDataValue1(item.getDataValue1());
                    model.setDataValue2(item.getDataValue2());
                    model.setRoleId(Integer.parseInt(roleId));
                    model.setControlOrder(item.getControlOrder());
                    model.setDataDesc(item.getDataDesc());
                    sqlList.add(model);
                }
                sysRelationRdRepository.saveAll(sqlList);
            }
            if (menuList.size() > 0) {
                dbSysRelationRmRepository.deleteAllByRoleId(Integer.parseInt(roleId));

                List<DbSysRelationRm> sqlList = new ArrayList<>();
                for (DbSysMenu item : menuList
                ) {
                    DbSysRelationRm model = new DbSysRelationRm();
                    model.setMenuId(item.getId());
                    model.setRoleId(Integer.parseInt(roleId));
                    sqlList.add(model);
                }
                dbSysRelationRmRepository.saveAll(sqlList);
            }
            return RestResultFactory.createSuccessResult("授权成功");
        } catch (Exception e) {
            return RestResultFactory.createFailedResult("授权失败，请重试");
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

}
