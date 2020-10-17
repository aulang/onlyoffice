package cn.aulang.office.web.repository;

import cn.aulang.office.web.entity.Doc;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 文档Repository
 * @email aulang@qq.com
 * @date 2020-10-17 15:11
 */
@Repository
public interface DocRepository extends MongoRepository<Doc, String> {
}
