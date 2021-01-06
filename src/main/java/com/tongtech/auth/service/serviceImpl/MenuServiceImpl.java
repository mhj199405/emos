package com.tongtech.auth.service.serviceImpl;


import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_menu.DbSysMenuRepository;
import com.tongtech.auth.service.MenuService;
import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private DbSysMenuRepository dbSysMenuRepository;

    /**
     * 查询所有的菜单
     * @param model
     * @return
     */
    @Override
    public RestResult getDataControlList(Map<String, Object> model) {

        List<DbSysMenu> allMenu = dbSysMenuRepository.findAllMenu();
        return RestResultFactory.createSuccessResult(allMenu);
    }

    /**
     * 删除某些菜单
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public RestResult deleteControl(List<Integer> ids) {
        try {
            dbSysMenuRepository.deleteAllInIds(ids);
            return RestResultFactory.createSuccessResult("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("保存过程中出现意外，请重试");
        }
    }

    @Transactional
    @Override
    public RestResult updateControl(DbSysMenu model) {
        Optional<DbSysMenu> menuOptional = dbSysMenuRepository.findById(model.getId());
        if (!menuOptional.isPresent()){
            return RestResultFactory.createFailedResult("修改规则不存在");
        }
        DbSysMenu dc=menuOptional.get();
        if (model.getMenuName().length() == 0) {
            return RestResultFactory.createFailedResult("菜单名称不能为空");
        }
        model.setCreateTime(dc.getCreateTime());

        try {
            dbSysMenuRepository.save(model);
            return RestResultFactory.createSuccessResult("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("更新过程中出现意外，请重试");
        }
    }

    @Override
    @Transactional
    public RestResult insertControl(DbSysMenu model) {
        try {
            dbSysMenuRepository.save(model);
            return RestResultFactory.createSuccessResult("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RestResultFactory.createFailedResult("保存过程中出现意外，请重试");
        }
    }


}
