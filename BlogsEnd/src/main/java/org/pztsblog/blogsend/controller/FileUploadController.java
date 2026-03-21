package org.pztsblog.blogsend.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.pztsblog.blogsend.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    // 从 application.yml 中自动读取配置
    @Value("${tencent.cos.secret-id}")
    private String secretId;

    @Value("${tencent.cos.secret-key}")
    private String secretKey;

    @Value("${tencent.cos.region}")
    private String region;

    @Value("${tencent.cos.bucket-name}")
    private String bucketName;

    @PostMapping
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(400, "文件不能为空");
        }

        COSClient cosClient = null;
        File localFile = null;

        try {
            // 1. 初始化用户身份信息 (SecretId, SecretKey)
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            // 2. 设置 bucket 的地域
            Region regionObj = new Region(region);
            ClientConfig clientConfig = new ClientConfig(regionObj);
            // 建议使用 https 协议
            clientConfig.setHttpProtocol(HttpProtocol.https);
            // 3. 生成 cos 客户端
            cosClient = new COSClient(cred, clientConfig);

            // 4. 获取文件后缀并生成全新文件名
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + extension;

            // 我们把图片统一放到 COS 里的 "images/" 目录下
            String key = "images/" + newFileName;

            // 5. 将前端传来的 MultipartFile 临时转存为本地文件（COS SDK 要求传 File）
            localFile = File.createTempFile("temp", extension);
            file.transferTo(localFile);

            // 6. 执行上传到腾讯云！
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            cosClient.putObject(putObjectRequest);

            // 7. 拼装出腾讯云 CDN 的真实访问网址
            String url = "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;

            return Result.success(url);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "文件上传 COS 失败：" + e.getMessage());
        } finally {
            // 8. 无论成功失败，一定要清理临时文件并关闭客户端，防止内存泄漏
            if (localFile != null && localFile.exists()) {
                localFile.delete();
            }
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }
}