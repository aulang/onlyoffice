package cn.aulang.office.web.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wulang
 * @email wulang02@megvii.com
 * @date 2020-10-17 14:26
 */
@Data
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String creatorId;
    protected String creatorName;
    protected String createTime;
    protected String modifierId;
    protected String modifierName;
    protected String modifiedTime;
}
