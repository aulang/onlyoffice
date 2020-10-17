package cn.aulang.office.web.config;

import cn.aulang.office.web.properties.MinioProperties;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Minio配置
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 15:27
 */
@Configuration
@EnableConfigurationProperties(MinioProperties.class)
public class MinioConfig {
    @Autowired
    private MinioProperties properties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(properties.getEndpoint())
                .credentials(properties.getAccessKey(), properties.getSecretKey())
                .build();
    }

}
