package cn.aulang.office.web.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

/**
 * 存储服务
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 15:25
 */
@Slf4j
@Service
public class StorageService {
    @Autowired
    private MinioClient client;

    public void put(String bucketName, String objectName, InputStream stream, long size) throws IOException {
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(stream, size, -1)
                .build();

        try {
            client.putObject(args);
        } catch (Exception e) {
            log.error("上传对象失败！", e);
            throw new IOException(e);
        }

        if (stream != null) {
            stream.close();
        }
    }

    public InputStream get(String bucketName, String objectName) throws IOException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build();
        try {
            return client.getObject(args);
        } catch (Exception e) {
            log.error("获取对象失败！", e);
            throw new IOException(e);
        }
    }
}
