package com.st.mall.goodsservice.controller;

import cn.hutool.core.util.IdUtil;
import com.st.mall.common.bean.RespBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping({"/category", "/goods"})
public class UploadController {
    @Value("${st.picDir: D:/situ/code/Project02/pic/}")
    private String pidDir;

    @PostMapping("/upload")
    public RespBean upload(MultipartFile pic) throws IOException {
        //生成图片的名字
        String filename = pic.getOriginalFilename();
        //获取图片的后缀
        String suffix = filename.substring(filename.lastIndexOf('.'));
        //生成唯一的文件名
        filename = IdUtil.getSnowflakeNextIdStr() + suffix;
        //保存图片
        pic.transferTo(new File(pidDir + filename));
        //返回
        return RespBean.ok("上传成功", filename);
    }
}
