package cn.aulang.office.web.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Aulang
 * @email aulang@qq.com
 * @date 2020-10-22 12:47
 */
@ControllerAdvice
public class WebExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
