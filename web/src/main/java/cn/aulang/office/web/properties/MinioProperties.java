package cn.aulang.office.web.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 18:10
 */
@Data
@ConfigurationProperties("minio")
public class MinioProperties {
    private String endpoint;
    private String accessKey;
    private String secretKey;
}
