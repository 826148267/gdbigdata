package edu.jnu.service;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.*;
import edu.jnu.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

/**
 * @author Guo zifan
 * @version 1.0
 * @date 2022年03月01日 19:10
 */
@Service
public class OSSService {

    @Autowired
    private OSS oss;

    @Value("${aliyun.oss.bucket.name}")
    private String bucketName;

    /**
     * 上传文件到OSS服务器.
     * 将用户提供的对象上传到阿里云的OSS服务器.
     * @param objectName    填写Object完整路径，例如exampledir/exampleobject.txt。Object完整路径中不能包含Bucket名称
     * @param inputStream   输入待传文件的输入流
     */
    public void uploadObj2OSS(String objectName, InputStream inputStream) {
        String bucketName = this.bucketName;
        // 创建PutObjectRequest对象。
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
        // 如果需要上传时设置存储类型和访问权限，请参考以下示例代码。
        ObjectMetadata metadata = new ObjectMetadata();
        // 根据文件后缀来决定应该上传什么格式的数据。
        metadata.setContentType(Tools.getContentType(objectName));
        putObjectRequest.setMetadata(metadata);
        // 上传文件。
        oss.putObject(putObjectRequest);
    }

    /**
     * 获取用户文件内容.
     * 根据用户给的文件名获取存储在bucket bucketName中的文件.
     * @param objName   文件名
     * @return  返回文件内容的数据输入流
     */
    public InputStream downloadObj(String objName) {
        String bucketName = this.bucketName;
        OSSObject result = oss.getObject(new GetObjectRequest(bucketName, objName));
        return result.getObjectContent();
    }
}
