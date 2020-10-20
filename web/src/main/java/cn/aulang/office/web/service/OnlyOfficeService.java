package cn.aulang.office.web.service;

import cn.aulang.office.sdk.util.SignatureUtils;
import cn.aulang.office.web.common.Constants;
import cn.aulang.office.web.factory.HttpsUrlConnectionFactory;
import cn.aulang.office.web.properties.OnlyOfficeProperties;
import com.nimbusds.jose.JOSEException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;

/**
 * OnlyOffice服务
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 21:42
 */
@Slf4j
@Service
public class OnlyOfficeService {
    @Autowired
    private OnlyOfficeProperties properties;

    public boolean verify(String token) {
        try {
            SignatureUtils.parseToken(token, properties.getSecret());
            return true;
        } catch (JOSEException e) {
            return false;
        }
    }

    public String genToken(String content) throws JOSEException {
        return SignatureUtils.genToken(content, properties.getSecret());
    }

    @SneakyThrows
    public InputStream downloadFile(String url) {
        HttpURLConnection connection = HttpsUrlConnectionFactory.create(url);

        connection.setRequestProperty(Constants.AUTHORIZATION, SignatureUtils.genToken("{}", properties.getSecret()));

        return connection.getInputStream();
    }
}
