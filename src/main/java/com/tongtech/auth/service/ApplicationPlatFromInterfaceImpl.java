package com.tongtech.auth.service;//package org.ch.service;
//
//import org.ch.ch_web.UploadPath;
//import org.ch.data.db_sys_department.DbSysDepartment;
//import org.ch.data.db_sys_department.DbSysDepartmentRepository;
//import org.ch.data.db_sys_hospital.DbSysHospital;
//import org.ch.data.db_sys_hospital.DbSysHospitalRepository;
//import org.ch.data.db_sys_menu.DbSysMenu;
//import org.ch.data.db_sys_menu.DbSysMenuRepository;
//import org.ch.data.db_sys_operation.DbSysOperation;
//import org.ch.data.db_sys_operation.DbSysOperationRepository;
//import org.ch.data.db_sys_relation_rm.DbSysRelationRm;
//import org.ch.data.db_sys_relation_rm.DbSysRelationRmRepository;
//import org.ch.data.db_sys_relation_ro.DbSysRelationRo;
//import org.ch.data.db_sys_relation_ro.DbSysRelationRoRepository;
//import org.ch.data.db_sys_relation_ur.DbSysRelationUr;
//import org.ch.data.db_sys_relation_ur.DbSysRelationUrRepository;
//import org.ch.data.db_sys_role.DbSysRole;
//import org.ch.data.db_sys_role.DbSysRoleRepository;
//import org.ch.data.db_sys_user.DbSysUser;
//import org.ch.data.db_sys_user.DbsysUserRepository;
//import org.ch.vo.CreateUserVo;
//import org.ch.vo.RoleResultVo;
//import org.ch.vo.SetRoleVo;
//import org.ch.vo.UserResultVo;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.*;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//
//@Service
//public class ApplicationPlatFromInterfaceImpl implements ApplicationPlatfromInterface {
//    @Autowired
//    private DbSysDepartmentRepository dbSysDepartmentRepository;
//    @Autowired
//    private DbsysUserRepository dbsysUserRepository;
//    @Autowired
//    private DbSysRelationRmRepository dbSysRelationRmRepository;
//    @Autowired
//    private DbSysOperationRepository dbSysOperationRepository;
//    @Autowired
//    private DbSysRoleRepository dbSysRoleRepository;
//    @Autowired
//    private DbSysRelationRoRepository dbSysRelationRoRepository;
//    @Autowired
//    private DbSysHospitalRepository dbSysHospitalRepository;
//    @Autowired
//    private DbSysRelationUrRepository dbSysRelationUrRepository;
//    @Autowired
//    private DbSysMenuRepository dbSysMenuRepository;
//    @Autowired
//    private UploadPath uploadPath;
//    /**
//     * 获取登录配置信息
//     */
//    @Override
//    public HashMap<String, Object> getConfig() {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        //创建一个用于封装结果的map集合
////        name="admin";
//        HashMap<String, Object> resultMap = new HashMap<>();
//        Optional<DbSysUser> loginName = dbsysUserRepository.findByLoginName(name);
//        DbSysUser dbSysUser=null;
//        if (loginName.isPresent()){
//            dbSysUser = loginName.get();
//        }
//        //将当前用户的信息发送给前端
//        if (dbSysUser==null){
//            return null;
//        }
//        resultMap.put("user",dbSysUser);
//        List<DbSysRole> dbSysRoleList = dbSysUser.getDbSysRoleList();
//        if (dbSysRoleList==null||dbSysRoleList.size()==0){
//            //如果当前用户没有角色存在，就返回一个没有权限的用户
//            resultMap.put("menuList",null);
//            resultMap.put("operationList",null);
//           return resultMap;
//        }
//        //创建一个集合用于封装所有的对象operation对象
//        HashSet<DbSysOperation> dbSysOperationHashSet = new HashSet<>();
//        for (DbSysRole dbSysRole : dbSysRoleList) {
//            List<DbSysOperation> dbSysOperationList = dbSysRole.getDbSysOperationList();
//            if (dbSysOperationList==null||dbSysOperationList.size()==0){
//               continue;
//            }
//            //一次性的添加一个集合，查看是否具有去重效果。
//            dbSysOperationHashSet.addAll(dbSysOperationList);
//        }
//        //查询所有的符合条件的menu
//        List<DbSysMenu> dbSysMenuList= dbSysMenuRepository.findAllMenusByOne(name);
////        List<DbSysRelationRm> dbSysRelationRmList= dbSysRelationRmRepository.findAllMenus(name);
//        resultMap.put("menuList",dbSysMenuList);
//        resultMap.put("operationList",dbSysOperationHashSet);
//        return resultMap;
//    }
//
//    /**
//     * 获取所有的部门信息
//     * @return
//     */
//    @Override
//    public List<DbSysDepartment> getDepartments() {
//        Sort orderIndex = new Sort(Sort.Direction.ASC, "orderIndex");
//        List<DbSysDepartment> dbSysDepartments = dbSysDepartmentRepository.findAll(orderIndex);
//        return dbSysDepartments;
//    }
//
//    /**
//     * 创建部门信息
//     * @return
//     * @param dbSysDepartment
//     */
//    @Override
//    @Transactional
//    public Boolean createDepartment(DbSysDepartment dbSysDepartment) {
//        try {
//            dbSysDepartment.setCreateTime(new Date());
//            dbSysDepartmentRepository.save(dbSysDepartment);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 删除部门信息
//     * @param deptId
//     * @return
//     */
//    @Override
//    @Transactional
//    public Boolean deleteDepartment(Integer deptId) {
//        try {
//            dbSysDepartmentRepository.deleteById(deptId);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 更新部门信息（）
//     * @param dbSysDepartment
//     * @return
//     */
//    @Override
//    @Transactional
//    public Boolean updateDepartment(DbSysDepartment dbSysDepartment) {
//        try {
//            dbSysDepartment.setUpdateTime(new Date());
//            dbSysDepartmentRepository.save(dbSysDepartment);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 查询功能信息
//     * @return
//     */
//    @Override
//    public List<DbSysOperation> getOperations() {
//        List<DbSysOperation> all = dbSysOperationRepository.findAll();
//        return all;
//    }
//
//    /**
//     * 删除功能信息
//     * @param operId
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean deleteOperation(Integer operId) {
//        try {
//            dbSysOperationRepository.deleteById(operId);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 创建功能信息
//     * @param dbSysOperation
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean createOperation(DbSysOperation dbSysOperation) {
//        try {
//            dbSysOperation.setCreateTime(new Date());
//            dbSysOperationRepository.save(dbSysOperation);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 更新功能信息
//     * @param dbSysOperation
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean updateOperation(DbSysOperation dbSysOperation) {
//        try {
//            dbSysOperation.setUpdateTime(new Date());
//            //更新时，后台维护创建时间
//            Optional<DbSysOperation> oldOptional = dbSysOperationRepository.findById(dbSysOperation.getOperId());
//            DbSysOperation dbSysOperation1 = oldOptional.get();
//            dbSysOperation.setCreateTime(dbSysOperation1.getCreateTime());
//            dbSysOperationRepository.save(dbSysOperation);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 获取角色信息
//     * @return
//     */
//    @Override
//    public List<RoleResultVo> getRoles() {
//        List<DbSysRole> dbSysRoleList = dbSysRoleRepository.findAll();
//        if (dbSysRoleList==null||dbSysRoleList.size()==0){
//            return new ArrayList<>();
//        }
//        //创建一个集合，用于装所查询的每一个角色对象
//        List<RoleResultVo> list=new ArrayList<>();
//        RoleResultVo roleResultVo=null;
//        for (DbSysRole dbSysRole : dbSysRoleList) {
//            roleResultVo = new RoleResultVo();
//            BeanUtils.copyProperties(dbSysRole,roleResultVo);
//            roleResultVo.setOperList(dbSysRole.getDbSysOperationList());
//            List<DbSysMenu> dbSysMenuList=dbSysMenuRepository.findByRoleId(dbSysRole.getRoleId());
////            List<DbSysRelationRm> dbSysRelationRmList = dbSysRelationRmRepository.findByRoleId(dbSysRole.getRoleId());
//            roleResultVo.setMenuList(dbSysMenuList);
//            list.add(roleResultVo);
//        }
//        return list;
//    }
//
//    /**
//     * 根据id删除角色，需要先删除有关联字段的对象，然后在删除相关的，不然会出问题的。
//     * @param roleId
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean deleteRoles(Integer roleId) {
//        try {
//            //删除rm表当中的所有相关的内容,这里先删除主表当中的是因为建立了联系，这时候，如果先删除被引用的
//            //可能后面会发生两次删除，这时候对象已经被删除了，因此可能出现下面的问题
//            //开启多对多的时候，删除数据的时候，也会将中间表进行相应的级联删除，对于相关的数据
//            long countByRoleId = dbSysRelationRmRepository.getCountByRoleId(roleId);
//            if (countByRoleId>0){
//                dbSysRelationRmRepository.deleteByRoleId(roleId);
//            }
////            long countByRoleId1 = dbSysRelationRoRepository.getCountByRoleId(roleId);
////            if (countByRoleId1>0){
////                dbSysRelationRoRepository.deleteByRoleId(roleId);
////            }
//            long countByRoleId2 = dbSysRelationUrRepository.getCountByRoleId(roleId);
//            if (countByRoleId2>0){
//                dbSysRelationUrRepository.deleteByRoleId(roleId);
//            }
//            dbSysRoleRepository.deleteById(roleId);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 更新角色,当使用@many之类的时候，注意查看打印出来的sql语句，不然容易出问题，当关联字段是null的时候，容易出问题。
//     * @param dbSysRole
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean updateRoles(DbSysRole dbSysRole) {
//        //进行更新的时候，记得要将相应的数据查询出来，放到要更新的对象当中，不然会出现问题，会将后面的数据进行删除
//        try {
//            Optional<DbSysRole> byId = dbSysRoleRepository.findById(dbSysRole.getRoleId());
//            if (byId.isPresent()){
//                DbSysRole dbSysRole1 = byId.get();
//                dbSysRole.setDbSysOperationList(dbSysRole1.getDbSysOperationList());
//                dbSysRole.setCreateTime(dbSysRole1.getCreateTime());
//            }
//            dbSysRole.setUpdateTime(new Date());
//            dbSysRoleRepository.save(dbSysRole);
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 创建角色
//     * @param dbSysRole
//     * @return
//     */
//    @Override
//    @Transactional
//    public Integer createRoles(DbSysRole dbSysRole) {
//        try {
//            String roleName = dbSysRole.getRoleName();
//            if (roleName==null||roleName.trim().equals("")){
//                //角色名称不能为空或者为空格
//                return -2;
//            }
//            //根据角色名称查询是否存在该角色名称，如果存在，则返回false
//            Integer roleNum = dbSysRoleRepository.countAllByRoleName(roleName.trim());
//            if (roleNum>0){
//                //角色名称已存在
//                return -3;
//            }
//            //将角色名称两端的空格进行去除
//            dbSysRole.setRoleName(roleName.trim());
//            dbSysRole.setCreateTime(new Date());
//            dbSysRoleRepository.save(dbSysRole);
//            return 0;
//        } catch (Exception e) {
//            e.printStackTrace();
//            //由于发生异常，导致的失败
//            return -1;
//        }
//    }
//
//    /**
//     * 设置角色的权限
//     * @param setRoleVo
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean setRole(SetRoleVo setRoleVo) {
//        try {
//            Integer roleId = setRoleVo.getRoleId();
//            //如果没有传角色id直接返回设置权限失败
//            if (roleId==null){
//                return false;
//            }
//            List<DbSysOperation> dbSysOperationList = setRoleVo.getOperList();
//            //如果这个集合不为空，说明才需要进行修改，否则，就不需要修改，维持原状
//            if (dbSysOperationList!=null&&dbSysOperationList.size()>0){
//                //先把当前角色的权限进行删除，然后再将角色的权限进行保存
//                dbSysRelationRoRepository.deleteByRoleId(roleId);
//                //然后创建中间表的关系进行保存
//                //创建一个集合用于装中间表的对象
//                ArrayList<DbSysRelationRo> dbSysRelationRos = new ArrayList<>();
//                DbSysRelationRo dbSysRelationRo=null;
//                for (DbSysOperation dbSysOperation : dbSysOperationList) {
//                    dbSysRelationRo=new DbSysRelationRo();
//                    dbSysRelationRo.setRoleId(roleId);
//                    dbSysRelationRo.setOperId(dbSysOperation.getOperId());
//                    dbSysRelationRos.add(dbSysRelationRo);
//                }
//                //将权限关系进行保存
//                for (DbSysRelationRo sysRelationRo : dbSysRelationRos) {
//                    dbSysRelationRoRepository.save(sysRelationRo);
//                }
//            }
//            //进行菜单列表的保存操作
//            List<DbSysRelationRm> dbSysRelationRmList = setRoleVo.getMenuList();
//            //当传递的菜单不为空的时候，才有必要进行操作，否则，维持原状
//            if (dbSysRelationRmList!=null&&dbSysRelationRmList.size()>0){
//                //首先将数据库当中的菜单信息进行删除
//                dbSysRelationRmRepository.deleteByRoleId(roleId);
//                //删除完成之后，开始进行保存
//                for (DbSysRelationRm dbSysRelationRm : dbSysRelationRmList) {
////                    dbSysRelationRm.setCreateTime(new Date());
//                    dbSysRelationRm.setRoleId(roleId);
//                    dbSysRelationRmRepository.save(dbSysRelationRm);
//                }
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 获取用户的信息
//     * @param page
//     * @param size
//     */
//    @Override
//    public HashMap<String, Object> getUserInfo(Integer page, Integer size) {
//        HashMap<String, Object> resultMap = new HashMap<>();
//        //用于封装前端所需要显示的数据
//        Pageable pageable=new PageRequest(page-1,size);
//        Page<DbSysUser> dbSysUserPage = dbsysUserRepository.findAll(pageable);
//        List<DbSysUser> all = dbsysUserRepository.findAll();
//        resultMap.put("page",page);
//        resultMap.put("pageSize",size);
//        resultMap.put("totalPage",dbSysUserPage.getTotalPages());
//        List<DbSysUser> dbSysUsersList = dbSysUserPage.getContent();
//        if (dbSysUsersList==null||dbSysUsersList.size()==0){
//            //没有必要处理，返回一个null；
//            return null;
//        }
//        List<UserResultVo> list=new ArrayList<>();
//        for (DbSysUser dbSysUser : dbSysUsersList) {
//            UserResultVo userResultVo = new UserResultVo();
//            BeanUtils.copyProperties(dbSysUser,userResultVo);
//            List<DbSysRole> dbSysRoleList = dbSysUser.getDbSysRoleList();
//            userResultVo.setRoleList(dbSysRoleList);
//            //存储部门
//            Integer deptId = dbSysUser.getDeptId();
//            if (deptId==null){
//                userResultVo.setDepartment(null);
//            }else {
//                Optional<DbSysDepartment> byId = dbSysDepartmentRepository.findById(deptId);
//                if (byId.isPresent()){
//                    DbSysDepartment dbSysDepartment = byId.get();
//                    userResultVo.setDepartment(dbSysDepartment);
//                }else {
//                    userResultVo.setDepartment(null);
//                }
//            }
//            //存储医院
//            Integer hospId = dbSysUser.getHospId();
//            if (hospId==null){
//                userResultVo.setDbSysHospital(null);
//            }else {
//                Optional<DbSysHospital> byId = dbSysHospitalRepository.findById(hospId);
//                if (byId.isPresent()){
//                    DbSysHospital dbSysHospital = byId.get();
//                    userResultVo.setDbSysHospital(dbSysHospital);
//                }else {
//                    userResultVo.setDbSysHospital(null);
//                }
//            }
//            list.add(userResultVo);
//        }
//        resultMap.put("users",list);
//        return resultMap;
//    }
//
//    /**
//     * create user
//     * @param createUserVo
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean createUser(CreateUserVo createUserVo) {
//        try {
//            String loginName = createUserVo.getLoginName();
//            if (loginName==null||loginName.trim().equals("")){
//                return false;
//            }
//            DbSysUser dbSysUser = new DbSysUser();
//            BeanUtils.copyProperties(createUserVo,dbSysUser);
//            //对创建用户的用户名的两端进行空格的去除
//            dbSysUser.setLoginName(loginName.trim());
//            dbSysUser.setCreateTime(new Date());
//            //save user
//            DbSysUser save = dbsysUserRepository.save(dbSysUser);
//            //save user-role table
//            List<Integer> roleIds = createUserVo.getRoleId();
//            if (roleIds==null||roleIds.size()==0){
//                return true;
//            }else {
//                for (Integer roleId : roleIds) {
//                    DbSysRelationUr dbSysRelationUr = new DbSysRelationUr();
//                    dbSysRelationUr.setUserId(save.getUserId());
//                    dbSysRelationUr.setRoleId(roleId);
//                    dbSysRelationUrRepository.save(dbSysRelationUr);
//                }
//            }
//            return true;
//        } catch (BeansException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * delete user
//     * @param userId
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean deleteUser(Integer userId) {
//        try {
//            Optional<DbSysUser> byId = dbsysUserRepository.findById(userId);
//            if (!byId.isPresent()){
//                return true;
//            }else {
//                DbSysUser dbSysUser = byId.get();
////                dbSysUser.setDbSysRoleList(null);
//                dbsysUserRepository.delete(dbSysUser);
////                dbSysRelationUrRepository.deleteByUserId(userId);
//            }
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * update user
//     * @param createUserVo
//     * @return
//     */
//    @Override
//    @Transactional
//    public boolean updateUser(CreateUserVo createUserVo) {
//        DbSysUser dbSysUser = new DbSysUser();
//        BeanUtils.copyProperties(createUserVo,dbSysUser);
//        List<Integer> roleIds = createUserVo.getRoleId();
//        DbSysUser dbSysUser1 = dbsysUserRepository.findById(dbSysUser.getUserId()).get();
//        dbSysUser.setDbSysRoleList(dbSysUser1.getDbSysRoleList());
//        dbSysUser.setCreateTime(dbSysUser1.getCreateTime());
//        dbsysUserRepository.save(dbSysUser);
//        if (roleIds==null||roleIds.size()==0){
//            return true;
//        }
//        dbSysRelationUrRepository.deleteByUserId(dbSysUser.getUserId());
//        for (Integer roleId : roleIds) {
//            DbSysRelationUr dbSysRelationUr = new DbSysRelationUr();
//            dbSysRelationUr.setRoleId(roleId);
//            dbSysRelationUr.setUserId(dbSysUser.getUserId());
//            dbSysRelationUrRepository.save(dbSysRelationUr);
//        }
//        return true;
//    }
//
//    /**
//     * judge weather user exist
//     * @param username
//     * @return
//     */
//    @Override
//    public boolean judgeUserExist(String username) {
//        if (username==null||username.trim().equals("")){
//            return true;
//        }
//        Integer integer = dbsysUserRepository.countAllByLoginName(username.trim());
//        if (integer>0){
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 获取所有的菜单列表
//     * @return
//     */
//    @Override
//    public List<DbSysMenu> getAllMenu() {
//        List<DbSysMenu> menuList = dbSysMenuRepository.findAll(new Sort(Sort.Direction.ASC,"orderIndex"));
//        return menuList;
//    }
//
//    /**
//     * 获取所有的用户列表
//     * @return
//     */
//    @Override
//    public List<DbSysUser> getAllUser() {
//        List<DbSysUser> dbSysUserList = dbsysUserRepository.findAll();
//        for (DbSysUser dbSysUser : dbSysUserList) {
//            dbSysUser.setDbSysRoleList(null);
//        }
//        return dbSysUserList;
//    }
//
//    /**
//     * 获取当前用户能够访问的部门
//     * @return
//     */
//    @Override
//    public List<DbSysDepartment> getAccessedDepartment() {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
////        name="admin";
//        if (name==null||"anonymousUser".equalsIgnoreCase(name)){
//            return null;
//        }
//        //根据用户名为查找当前用户所能够访问的所有部门
//        List<DbSysDepartment> dbSysDepartmentList= dbSysDepartmentRepository.findDepartmentForOne(name);
//        return dbSysDepartmentList;
//    }
//
//    /**
//     * 上传文件到后端服务器,通过/images/图片名称可以加载到相应的图片
//     * @param file
//     * @return
//     */
//    @Override
//    public String uploadImages(MultipartFile file) {
//        if (file==null){
//            return null;
//        }
//        String originalFilename = file.getOriginalFilename();
//        originalFilename=System.currentTimeMillis()+"_"+originalFilename;
//        String uploadPath = this.uploadPath.getUploadPath();
//        File file2 = new File(uploadPath);
//        if (!file2.exists()){
//            file2.mkdirs();
//        }
//        System.out.println(originalFilename);
//        System.out.println(uploadPath);
//        File file1 = new File(uploadPath, originalFilename);
//        System.out.println(file1.getAbsoluteFile());
//        String resultUrl= uploadPath.substring(uploadPath.indexOf("/")+1);
//        resultUrl=resultUrl.substring(resultUrl.indexOf("/"))+originalFilename;
//        try {
//            FileOutputStream fileOutputStream = new FileOutputStream(file1.getAbsoluteFile());
//            InputStream inputStream = file.getInputStream();
//            byte[] bytes = new byte[1024 * 1024];
//            int size =0;
//            while ((size= inputStream.read(bytes))!=-1){
//                fileOutputStream.write(bytes,0,size);
//                fileOutputStream.flush();
//            }
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//        return resultUrl;
//    }
//}
