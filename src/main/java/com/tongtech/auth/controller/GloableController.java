package com.tongtech.auth.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tongtech.auth.ch_auth.CustomUser;
import com.tongtech.auth.ch_web.UploadPath;
import com.tongtech.auth.data.db_sys_menu.DbSysMenu;
import com.tongtech.auth.data.db_sys_menu.DbSysMenuRepository;
import com.tongtech.auth.data.db_upload_download.FrmUploadDownload;
import com.tongtech.auth.data.db_upload_download.FrmUploadDownloadRepository;
import com.tongtech.auth.utils.toolsUtil;
import com.tongtech.auth.vo.RestResult;
import com.tongtech.auth.vo.RestResultFactory;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GloableController {
    @Autowired
    private DbSysMenuRepository dbSysMenuRepository;

    @Autowired
    private UploadPath uploadPath;

    @RequestMapping("/sys/getcurrentuser")
    public RestResult GetCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            RestResult<String> failedResult = RestResultFactory.createFailedResult("无法获取当前用户信息");
            failedResult.setStatus(1);
            return failedResult;
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        if (user == null) {
            RestResult<String> failedResult = RestResultFactory.createFailedResult("无法获取当前用户信息");
            failedResult.setStatus(1);
            return failedResult ;
        }
        List<DbSysMenu> controlList = dbSysMenuRepository.getListByUserId(user.getVoUserMenu().getId(), 1);
        JSONObject jo = new JSONObject();
        jo.put("name", user.getVoUserMenu().getLoginName());
        jo.put("avatar", "");
        jo.put("menuList", GetMenuTree(controlList, -1,new ArrayList<>()));
        Integer currentUserId = user.getVoUserMenu().getId();
        List<DbSysMenu> allAuthorities = dbSysMenuRepository.findAllAuthoritiesByUserIdThroughRole(currentUserId);
        List<DbSysMenu> dbSysMenus = dbSysMenuRepository.findAllPublicAuthorities();
        allAuthorities.addAll(dbSysMenus);
        jo.put("authorities", allAuthorities);
        return RestResultFactory.createSuccessResult(jo);
    }

    JSONArray GetMenuTree(List<DbSysMenu> menuList, Integer parentId, List<String> parents) {
        JSONArray ja = new JSONArray();
        for (DbSysMenu item : menuList
        ) {

            if (item.getParentId().equals(parentId)) {
                JSONObject mJo = new JSONObject();
                mJo.put("hideInMenu", item.getIsShow() == 0);
                mJo.put("icon", item.getMenuIcon());
                mJo.put("name", item.getMenuName());
                mJo.put("key", String.valueOf(item.getId()));
                mJo.put("pro_layout_parentKeys", parents);
                if (item.getMenuUrl() != null && item.getMenuUrl().length() > 0) {
                    mJo.put("path", item.getMenuUrl());
                }

                List<String> cParents = new ArrayList<>();
                cParents.addAll(parents);
                cParents.add(String.valueOf(item.getId()));
                JSONArray children = GetMenuTree(menuList, item.getId(), cParents);
//                mJo.put("key", String.valueOf(item.getId()));
//                mJo.put("path", item.getMenuUrl());
//                JSONArray children = GetMenuTree(menuList, item.getId());
                if (children != null) {
                    mJo.put("children", children);
                }
                ja.add(mJo);
            }
        }
        if (ja.size() == 0) {
            return null;
        }
        return ja;
    }

    @RequestMapping("/sys/getroutes")
    public RestResult GetRoutes() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return RestResultFactory.createFailedResult("无法获取当前用户信息");
        }
        CustomUser user = null;
        if (principal instanceof CustomUser) {
            user = (CustomUser) principal;
        }
        if (user == null) {
            return RestResultFactory.createFailedResult("无法获取当前用户信息");
        }
        List<DbSysMenu> controlList = dbSysMenuRepository.getListByUserId(user.getVoUserMenu().getId(), 1);
        return RestResultFactory.createSuccessResult(controlList.stream().filter(s -> !toolsUtil.isNullOrEmpty(s.getMenuComponent())).collect(Collectors.toList()));
    }

    /**
     * //     * 上传文件到后端服务器
     * //     * @param file
     * //     * @return
     * //
     */
    @PostMapping("/access/upload")
    public RestResult uploadImages(@RequestParam("file") MultipartFile file,@RequestParam("formInstId") Integer formInstId,@RequestParam("formItemId") String formItemId) {
        String url = uploadImages1(file,formInstId,formItemId);
        if (url == null) {
            return RestResultFactory.createFailedResult("image upload fail!");
        }
        return RestResultFactory.createSuccessResult(url);
    }


    @Autowired
    private FrmUploadDownloadRepository frmUploadDownloadRepository;
    /**
     * //     * 上传文件到后端服务器,通过/images/图片名称可以加载到相应的图片
     * //     * @param file
     * //     * @return
     * //
     */
    @Transactional
    public String uploadImages1(MultipartFile file, Integer formInstId, String formItemId) {
        if (file == null) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        originalFilename = System.currentTimeMillis() + "_" + originalFilename;
        String uploadPath = this.uploadPath.getUploadPath();
        File file2 = new File(uploadPath);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        System.out.println(originalFilename);
        System.out.println(uploadPath);
        File file1 = new File(uploadPath, originalFilename);
        System.out.println(file1.getAbsoluteFile());
        String resultUrl = uploadPath.substring(uploadPath.indexOf("/") + 1);
        resultUrl = resultUrl.substring(resultUrl.indexOf("/")) + originalFilename;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file1.getAbsoluteFile());
            InputStream inputStream = file.getInputStream();
            byte[] bytes = new byte[1024 * 1024];
            int size = 0;
            while ((size = inputStream.read(bytes)) != -1) {
                fileOutputStream.write(bytes, 0, size);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        //将相关信息存储到数据库当中
        FrmUploadDownload frmUploadDownload = new FrmUploadDownload();
        frmUploadDownload.setCreateTime(LocalDateTime.now());
        frmUploadDownload.setOriginName(file.getOriginalFilename());
        frmUploadDownload.setSavePath(file1.getAbsolutePath());
        frmUploadDownload.setFormInstId(formInstId);
        frmUploadDownload.setFormItemId(formItemId);
        frmUploadDownloadRepository.saveAndFlush(frmUploadDownload);
        return resultUrl;
    }

    @RequestMapping("/access/download")
    public void downLoadFile(@RequestParam(name = "fileName") String fileName, HttpServletResponse response, HttpServletRequest request) throws Exception {
        FrmUploadDownload frmUploadDownload = frmUploadDownloadRepository.findFirstByOriginName(fileName);
        String savePath = frmUploadDownload.getSavePath();
        String extendFileName=null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(savePath));
            extendFileName = fileName.substring(fileName.lastIndexOf("."));
            response.setContentType(request.getSession().getServletContext().getMimeType(extendFileName));
            response.setHeader("content-disposition","attachment;fileName="+ URLEncoder.encode(fileName,"UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            FileCopyUtils.copy(fileInputStream,outputStream);
        } catch (Exception e) {
            throw e;
        }finally {
            if (fileInputStream != null){
                fileInputStream.close();
            }
        }
    }

}
