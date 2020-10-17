package cn.aulang.office.sdk.model;

/**
 * OnlyOffice回调对象
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 22:11
 */

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Callback {
    private String key;
    /**
     * 0：文档不存在
     * 1：正在编辑文档
     * 2：关闭并保存文档
     * 3：关闭并保存文档出错
     * 4：关闭并未修改文档
     * 6：编辑中保存文档
     * 7：编辑中保存文档出错
     */
    private int status;
    private String url;

    private String userdata;
    private List<String> users;
    private List<Action> actions;

    private String changesurl;
    private Integer forcesavetype;
    private Map<String, Object> history;
}
