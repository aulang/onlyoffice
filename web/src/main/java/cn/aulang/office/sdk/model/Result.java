package cn.aulang.office.sdk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OnlyOffice回调结果
 *
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 22:09
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private int error = 0;
    private String msg = null;

    public static Result success() {
        return new Result(0, null);
    }

    public static Result fail(String msg) {
        return new Result(-1, msg);
    }
}
