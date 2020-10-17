package cn.aulang.office.web.service;

import cn.aulang.office.web.entity.Doc;
import cn.aulang.office.web.repository.DocRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 文档服务
 * @email aulang@qq.com
 * @date 2020-10-17 15:13
 */
@Slf4j
@Service
public class DocService {
    @Autowired
    private DocRepository docRepository;

    public Doc get(String id) {
        return docRepository.findById(id).orElseThrow();
    }
}
