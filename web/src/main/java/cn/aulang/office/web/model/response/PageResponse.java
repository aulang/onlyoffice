package cn.aulang.office.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-19 21:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class PageResponse<T> {
    private int page;
    private int pageSize;
    private int totalPages;
    private List<T> content;
}
