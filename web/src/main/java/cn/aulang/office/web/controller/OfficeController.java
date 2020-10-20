package cn.aulang.office.web.controller;

import cn.aulang.office.sdk.enums.DocumentStatus;
import cn.aulang.office.sdk.model.Callback;
import cn.aulang.office.sdk.model.Result;
import cn.aulang.office.web.common.Constants;
import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.model.request.TokenRequest;
import cn.aulang.office.web.service.DocService;
import cn.aulang.office.web.service.OnlyOfficeService;
import cn.aulang.office.web.service.StorageService;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 22:04
 */
@Slf4j
@RestController
@RequestMapping("/office")
public class OfficeController {
    @Autowired
    private DocService docService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private OnlyOfficeService onlyOfficeService;

    @GetMapping(path = "/doc/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StreamingResponseBody> doc(
            @PathVariable("id") String id,
            @RequestHeader("Authorization") String auth) {

        if (onlyOfficeService.verify(auth)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        Doc doc = docService.get(id);

        String name = doc.getName();

        InputStream input;
        try {
            input = storageService.get(doc.getOwner(), name);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }

        StreamingResponseBody body = output -> IOUtils.copy(input, output);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        String filename = URLEncoder.encode(name, StandardCharsets.UTF_8);
        headers.setContentDispositionFormData(Constants.ATTACHMENT, filename);

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }


    @PostMapping(path = "/callback", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result callback(@RequestBody Callback body, @RequestHeader(name = "Authorization") String auth) {
        if (onlyOfficeService.verify(auth)) {
            return Result.fail("认证失败");
        }

        String key = body.getKey();
        String url = body.getUrl();
        int status = body.getStatus();

        String user = null;
        List<String> users = body.getUsers();
        if (!CollectionUtils.isEmpty(users)) {
            user = users.get(0);
        }

        switch (status) {
            case 0:
                // 文档不存在
                log.warn("回调文档不存在：{}", key);
                break;
            case 1:
                // 正在编辑文档
                log.info("回调正在编辑文档：{}", body.toString());
                docService.saveStatus(key, DocumentStatus.editing, user);
            case 2: {
                // 关闭并保存文档
                log.info("回调关闭并保存文档：{}", body.toString());
                saveStatusAndFile(key, DocumentStatus.saved, user, url);
                break;
            }
            case 3:
                // 关闭并保存文档出错
                log.warn("回调关闭并保存文档出错：{}", body.toString());
                saveStatusAndFile(key, DocumentStatus.error, user, url);
                break;
            case 4:
                // 关闭并未修改文档
                log.info("回调关闭并未修改文档：{}", body.toString());
                docService.saveStatus(key, DocumentStatus.saved, user);
                break;
            case 6:
                // 编辑中保存文档
                log.info("回调编辑中保存文档：{}", body.toString());
                saveStatusAndFile(key, DocumentStatus.editing, user, url);
                break;
            case 7:
                // 编辑中保存文档出错
                log.warn("回调编辑中保存文档出错：{}", body.toString());
                saveStatusAndFile(key, DocumentStatus.editing, user, url);
                break;
            default:
                log.info("未知的回调状态：{}", body.toString());
                break;
        }

        return Result.success();
    }

    public void saveStatusAndFile(String key, DocumentStatus status, String user, String url) {
        Doc doc = docService.saveStatus(key, status, user);
        if (doc != null) {
            try {
                InputStream inputStream = onlyOfficeService.downloadFile(url);
                storageService.put(doc.getOwner(), doc.getName(), inputStream);
            } catch (Exception e) {
                log.error("回调保存文件失败！", e);
                docService.saveStatus(key, DocumentStatus.error, user);
            }
        }
    }

    @PostMapping(path = "/token", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> token(@RequestBody TokenRequest request) {
        try {
            String jwt = onlyOfficeService.genToken(request.getContent());
            return ResponseEntity.ok(jwt);
        } catch (JOSEException e) {
            log.error("生成JWT失败！", e);
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
