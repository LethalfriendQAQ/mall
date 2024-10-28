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
    @Value("${st.picDir:static/pic/}")
    private String pidDir;

    private String getAbsolutePath(String relativePath) {
        return new File(relativePath).getAbsolutePath();
    }

    @PostMapping("/upload")
    public RespBean upload(MultipartFile pic) {
        try {
            //生成图片的名字
            String filename = pic.getOriginalFilename();
            //获取图片的后缀
            String suffix = filename.substring(filename.lastIndexOf('.'));
            //生成唯一的文件名
            filename = IdUtil.getSnowflakeNextIdStr() + suffix;

            //确保目录存在
            File directory = new File(getAbsolutePath(pidDir));
            if (!directory.exists()) {
                directory.mkdirs(); // 创建目录
            }

            //保存图片
            pic.transferTo(new File(directory, filename));

            //返回
            return RespBean.ok("上传成功", filename);
        } catch (IOException e) {
            return RespBean.error("上传失败: " + e.getMessage());
        }
    }
}

