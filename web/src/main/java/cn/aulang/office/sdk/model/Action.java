package cn.aulang.office.sdk.model;

import lombok.Data;

/**
 * OnlyOffice回调动作
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 22:12
 */
@Data
public class Action {
    /**
     * 0：用户连接
     * 1：用户断开连接
     */
    private int type;
    private String userid;
}
