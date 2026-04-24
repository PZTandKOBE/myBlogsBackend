package org.pztsblog.blogsend.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.COSObjectSummary;
import com.qcloud.cos.model.ListObjectsRequest;
import com.qcloud.cos.model.ObjectListing;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import org.pztsblog.blogsend.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    @Value("${tencent.cos.secret-id}")
    private String secretId;

    @Value("${tencent.cos.secret-key}")
    private String secretKey;

    @Value("${tencent.cos.region}")
    private String region;

    @Value("${tencent.cos.bucket-name}")
    private String bucketName;

    /**
     * 图片上传接口
     */
    @PostMapping
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error(400, "文件不能为空");
        }

        COSClient cosClient = null;
        File localFile = null;

        try {
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            Region regionObj = new Region(region);
            ClientConfig clientConfig = new ClientConfig(regionObj);
            clientConfig.setHttpProtocol(HttpProtocol.https);
            cosClient = new COSClient(cred, clientConfig);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + extension;
            String key = "images/" + newFileName;

            localFile = File.createTempFile("temp", extension);
            file.transferTo(localFile);

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            cosClient.putObject(putObjectRequest);

            String url = "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;
            return Result.success(url);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "文件上传 COS 失败：" + e.getMessage());
        } finally {
            if (localFile != null && localFile.exists()) {
                localFile.delete();
            }
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }

    /**
     * 新增：获取云端图片列表接口
     */
    @GetMapping("/images")
    public Result<List<String>> getImageList() {
        COSClient cosClient = null;
        List<String> imageUrls = new ArrayList<>();

        try {
            // 1. 初始化 COS 客户端
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            Region regionObj = new Region(region);
            ClientConfig clientConfig = new ClientConfig(regionObj);
            clientConfig.setHttpProtocol(HttpProtocol.https);
            cosClient = new COSClient(cred, clientConfig);

            // 2. 构造查询请求，指定 bucket 和目录前缀
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            listObjectsRequest.setBucketName(bucketName);
            listObjectsRequest.setPrefix("images/"); // 只扫描 images 目录
            listObjectsRequest.setMaxKeys(1000); // 设置单次拉取的最大数量

            // 3. 执行查询
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            List<COSObjectSummary> objectSummaries = objectListing.getObjectSummaries();

            // 4. 遍历提取对象键 (Key)，并拼装成完整的 CDN 链接
            for (COSObjectSummary cosObjectSummary : objectSummaries) {
                String key = cosObjectSummary.getKey();
                // 排除掉 "images/" 目录本身这条记录
                if (!key.equals("images/")) {
                    String url = "https://" + bucketName + ".cos." + region + ".myqcloud.com/" + key;
                    imageUrls.add(url);
                }
            }

            return Result.success(imageUrls);

        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(500, "获取图片列表失败：" + e.getMessage());
        } finally {
            // 别忘了关闭客户端
            if (cosClient != null) {
                cosClient.shutdown();
            }
        }
    }
}