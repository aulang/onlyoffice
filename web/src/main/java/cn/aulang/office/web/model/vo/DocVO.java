package cn.aulang.office.web.model.vo;

import cn.aulang.office.sdk.enums.DocumentStatus;
import lombok.Data;
import org.w3c.dom.DocumentType;

/**
 * 文档VO
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 16:10
 */
@Data
public class DocVO {
    public String id;
    private String name;

    private String owner;
    private String ownerName;

    private String key;
    private String fileType;
    private DocumentType documentType;
    private DocumentStatus status;
}
