package com.tongtech.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
public class DownLoad {
    @GetMapping(value = "/download/{sign}")
    public void download(@PathVariable("sign") String sign, HttpServletResponse response) throws IOException {
        try {
            System.out.println(sign);
//            JwtResult jr = JwtResult.build(sign);
//            String name = jr.getSubject();
//            String path = jr.getId();
            //前面需要传递这两个参数
            String path="";
            String name="";
            //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");
            //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
            response.setHeader("Content-Disposition", "attachment;fileName=" +  URLEncoder.encode(name,"utf-8" ));
            ServletOutputStream out;
            //通过文件路径获得File对象(假如此路径中有一个download.pdf文件)


            try {
                //3.通过response获取ServletOutputStream对象(out)
                out = response.getOutputStream();
                File file = new File(path);
                if (!file.exists()) {
                    byte[] bytes = "文件不存在".getBytes();
                    out.write(bytes);
                    out.close();
                    out.flush();
                    return;
                }
                FileInputStream inputStream = new FileInputStream(file);
                int b = 0;
                byte[] buffer = new byte[512];
                while (b != -1) {
                    b = inputStream.read(buffer);
                    //4.写到输出流(out)中
                    out.write(buffer, 0, b);
                }
                inputStream.close();
                out.close();
                out.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}