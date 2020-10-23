package cn.aulang.office.web.service;

import cn.aulang.oauth.client.user.User;
import cn.aulang.office.sdk.enums.DocumentStatus;
import cn.aulang.office.sdk.enums.DocumentType;
import cn.aulang.office.sdk.util.UUID;
import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.repository.DocRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author 文档服务
 * @email aulang@qq.com
 * @date 2020-10-17 15:13
 */
@Slf4j
@Service
public class DocService {
    @Autowired
    private UserService userService;
    @Autowired
    private DocRepository docRepository;

    public Doc get(String id) {
        return docRepository.findById(id).orElseThrow();
    }

    public Doc delete(String id) {
        Doc doc = get(id);
        docRepository.deleteById(id);
        return doc;
    }

    public Doc create(String owner, String ownerName, String name) {
        Doc doc = new Doc();
        doc.setName(name);
        doc.setOwner(owner, ownerName);

        doc.setKey(UUID.generate());

        String fileType = FilenameUtils.getExtension(doc.getName());
        doc.setFileType(fileType);

        DocumentType documentType = DocumentType.fileType(fileType);
        doc.setDocumentType(documentType.name());

        doc.setStatus(DocumentStatus.saved.name());

        return docRepository.save(doc);
    }

    public Page<Doc> search(String name, String fileType, int page, int pageSize, String owner) {
        Doc doc = new Doc();

        doc.setOwner(owner);

        if (!StringUtils.isEmpty(name)) {
            doc.setName(name);
        }

        if (!StringUtils.isEmpty(fileType)) {
            doc.setFileType(fileType);
        }

        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withMatcher("name",
                        ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING));

        Example<Doc> example = Example.of(doc, matcher);

        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.DESC, "createTime");
        return docRepository.findAll(example, pageable);
    }

    public Doc saveStatus(String key, DocumentStatus status, String modifierId) {
        Doc doc = docRepository.findByKey(key);
        if (doc == null) {
            log.warn("不存在的文档key：{}", key);
            return null;
        }

        String modifierName;
        User user = userService.getById(modifierId);
        if (user != null) {
            modifierName = user.getName();
        } else {
            modifierName = doc.getCreatorName();
        }

        doc.setModifier(modifierId, modifierName);
        doc.setStatus(status.name());
        return docRepository.save(doc);
    }
}
