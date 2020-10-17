package cn.aulang.office.web.controller;

import cn.aulang.office.web.common.Constants;
import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.model.vo.DocVO;
import cn.aulang.office.web.service.DocService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 文档Controller
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 11:02
 */
@Slf4j
@RestController
@RequestMapping("/api/doc")
public class DocController {
    @Autowired
    private DocService docService;
    @Autowired
    private StorageService storageService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> open(MultipartFile file) {
        String name = file.getOriginalFilename();
        String owner = "";
        try {
            storageService.put(owner, name, file.getInputStream(), file.getSize());

            DocVO vo = new DocVO();

            return ResponseEntity.ok(vo);
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败！");
        }
    }

    @GetMapping("/{id}/file")
    public ResponseEntity<StreamingResponseBody> file(@PathVariable String id) {
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

}
