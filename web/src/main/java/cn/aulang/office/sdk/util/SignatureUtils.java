package cn.aulang.office.sdk.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import org.springframework.util.StringUtils;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 22:00
 */
public class SignatureUtils {
    private static final String EMPTY = " ";

    public static String parseToken(String authorization, String secret) throws JOSEException {
        try {
            String token = StringUtils.split(authorization, EMPTY)[1].trim();

            JWSObject jwsObject = JWSObject.parse(token);

            JWSVerifier jwsVerifier = new MACVerifier(secret);

            if (jwsObject.verify(jwsVerifier)) {
                Payload payload = jwsObject.getPayload();
                byte[] bytes = payload.toBytes();
                return new String(bytes);
            } else {
                throw new JOSEException("不合法的JWT");
            }
        } catch (Exception e) {
            throw new JOSEException("不合法的JWT", e);
        }
    }

    public static String genToken(String content, String secret) throws JOSEException {
        Payload payload = new Payload(content.getBytes());
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        JWSSigner jwsSigner = new MACSigner(secret);
        jwsObject.sign(jwsSigner);

        return jwsObject.serialize();
    }
}
