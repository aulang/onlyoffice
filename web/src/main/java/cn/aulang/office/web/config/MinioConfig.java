package cn.aulang.office.web.config;

import io.minio.MinioClient;
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
public class MinioConfig {

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder().build();
    }

}
