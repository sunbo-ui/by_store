package com.mysb.core.controller;

import com.mysb.core.pojo.entry.Result;
import com.mysb.core.utils.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/upload")
@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER;

    @RequestMapping("/uploadFile")
    public Result uploadFile(MultipartFile file) throws Exception {
        try {
            FastDFSClient fastDFS = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
            //上传文件返回文件保存的路径和文件名
            String path = fastDFS.uploadFile(file.getBytes(), file.getOriginalFilename(), file.getSize());
            //拼接上服务器的地址返回给前端
            return new Result(true, FILE_SERVER + path);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败!");
        }
    }
    @RequestMapping("/delImg")
    public Result deleteFile(String url) throws Exception {
        String path  = url.substring(FILE_SERVER.length());
        System.out.println(path);
        FastDFSClient fastDFS = new FastDFSClient("classpath:fastDFS/fdfs_client.conf");
        Integer integer = fastDFS.delete_file(path);
        if (integer == 0){
            return new Result(true, "删除成功");
        }else {
            return new Result(true, "删除失败");
        }
    }
}
