package cn.aulang.office.web.service;

import cn.aulang.office.sdk.util.SignatureUtils;
import cn.aulang.office.web.properties.OnlyOfficeProperties;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
