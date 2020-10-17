package cn.aulang.office.web.controller;

import cn.aulang.office.web.common.Constants;
import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.service.DocService;
import cn.aulang.office.web.service.OnlyOfficeService;
import cn.aulang.office.web.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 22:04
 */
@Slf4j
@RestController
@RequestMapping("/api/office")
public class OnlyOfficeController {
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
    public void callback() {

    }
}
