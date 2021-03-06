package cn.aulang.office.web.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 14:26
 */
@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String creatorId;
    protected String creatorName;
    protected Date createTime;
    protected String modifierId;
    protected String modifierName;
    protected Date modifiedTime;

    public void setCreator(String creatorId, String creatorName) {
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.createTime = new Date();
    }

    public void setModifier(String modifierId, String modifierName) {
        this.modifierId = modifierId;
        this.modifierName = modifierName;
        this.modifiedTime = new Date();
    }
}
