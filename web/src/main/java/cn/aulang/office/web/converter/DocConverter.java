package cn.aulang.office.web.converter;

import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.model.vo.DocVO;
import org.mapstruct.Mapper;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 18:39
 */
@Mapper(componentModel = "spring")
public interface DocConverter {

    DocVO toVO(Doc doc);

}
