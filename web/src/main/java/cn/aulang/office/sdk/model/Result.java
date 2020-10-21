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

    public static Result success() {
        return new Result(0);
    }
}
