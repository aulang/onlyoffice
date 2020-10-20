package cn.aulang.office.web.model.vo;

import lombok.Data;

/**
 * 文档VO
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 16:10
 */
@Data
public class DocVO {
    private String id;
    private String name;

    private String owner;
    private String ownerName;

    private String key;
    private String fileType;
    private String documentType;
    private String status;

    private String createTime;
    private String modifiedTime;
}
