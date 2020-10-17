package cn.aulang.office.web.entity;

import cn.aulang.office.sdk.enums.DocumentStatus;
import cn.aulang.office.sdk.enums.DocumentType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 文档
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 11:24
 */
@Data
@Document
@EqualsAndHashCode(callSuper = true)
public class Doc extends BaseEntity {
    @Id
    private String id;
    private String name;
    /**
     * 文件所有者ID，存储桶Name，必须
     */
    @Indexed
    private String owner;
    private String ownerName;
    /**
     * 文件Key
     */
    @Indexed(unique = true)
    private String key;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文档类型
     */
    private String documentType;
    /**
     * 文档状态
     */
    private String status;

    public void setOwner(String owner, String ownerName) {
        this.owner = owner;
        this.ownerName = ownerName;
        setCreator(owner, ownerName);
    }
}