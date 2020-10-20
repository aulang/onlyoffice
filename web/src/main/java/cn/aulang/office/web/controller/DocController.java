package cn.aulang.office.web.controller;

import cn.aulang.office.web.common.Constants;
import cn.aulang.office.web.converter.DocConverter;
import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.model.dto.User;
import cn.aulang.office.web.model.response.PageResponse;
import cn.aulang.office.web.model.vo.DocVO;
import cn.aulang.office.web.service.DocService;
import cn.aulang.office.web.service.StorageService;
import cn.aulang.office.web.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private DocService docService;
    @Autowired
    private UserService userService;
    @Autowired
    private DocConverter docConverter;
    @Autowired
    private StorageService storageService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> open(@RequestParam("file") MultipartFile file) {
        User user = userService.currentUser();

        String name = file.getOriginalFilename();
        try {
            storageService.put(user.getId(), name, file.getInputStream(), file.getSize());
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("文件上传失败！");
        }

        Doc doc = docService.create(user.getId(), user.getName(), name);

        return ResponseEntity.ok(docConverter.toVO(doc));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") String id) {
        Doc doc = docService.get(id);
        return ResponseEntity.ok(docConverter.toVO(doc));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") String id) {
        docService.delete(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/list")
    public ResponseEntity<Object> list(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "fileType", required = false) String fileType,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "pageSize", defaultValue = "20") Integer pageSize) {
        if (page < 0) {
            page = 0;
        }

        if (page > 0) {
            page = page - 1;
        }

        if (pageSize < 1) {
            pageSize = 20;
        }

        Page<Doc> docs = docService.search(name, fileType, page, pageSize);

        PageResponse<DocVO> response = PageResponse.of(
                docs.getNumber() + 1,
                docs.getSize(),
                docs.getTotalPages(),
                docConverter.toVOs(docs.getContent())
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<StreamingResponseBody> file(@PathVariable("id") String id) {
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
