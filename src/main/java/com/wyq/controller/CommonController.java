package com.wyq.controller;


import com.wyq.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;


/**
 * 下载上传控制器
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String FilePath;

    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload (MultipartFile file){
        //file是一个临时文件，需要转存到指定位置，否则本次请求结束就会删除
        log.info("接收到文件{}",file.toString());

        //获取原始文件名
        String originFilename = file.getOriginalFilename();
        String suffix = originFilename.substring(originFilename.lastIndexOf("."));
        String FileName = UUID.randomUUID().toString()+suffix;

        //创建一个配置文件中的目录
        File dir = new File(FilePath);

        //判断目录是否存在
        if(!dir.exists())
        {
            dir.mkdirs();
        }

        try {
            file.transferTo(new File(FilePath + FileName ));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(FileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        //输入流获取文件内容
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(FilePath+name));

            //输出流，将输出流文件写入浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes))!=-1)
            {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //释放资源
            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




}
