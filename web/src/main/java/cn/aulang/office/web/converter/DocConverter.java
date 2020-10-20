package cn.aulang.office.web.converter;

import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.model.vo.DocVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-17 18:39
 */
@Mapper(componentModel = "spring")
public interface DocConverter {


    @Mapping(source = "createTime", target = "createTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "modifiedTime", target = "modifiedTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    DocVO toVO(Doc doc);

    List<DocVO> toVOs(List<Doc> docs);

}
