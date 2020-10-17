package cn.aulang.office.web.service;

import cn.aulang.office.sdk.enums.DocumentStatus;
import cn.aulang.office.sdk.enums.DocumentType;
import cn.aulang.office.sdk.util.UUID;
import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.repository.DocRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 文档服务
 * @email aulang@qq.com
 * @date 2020-10-17 15:13
 */
@Slf4j
@Service
public class DocService {
    @Autowired
    private DocRepository docRepository;

    public Doc get(String id) {
        return docRepository.findById(id).orElseThrow();
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
}
