package com.meta.configure;

import com.meta.model.ErrorEnum;
import com.meta.model.FastRunTimeException;
import com.meta.model.ReturnData;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.util.ThrowableAnalyzer;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class OauthWebResponseExceptionTranslator implements WebResponseExceptionTranslator {

    private ThrowableAnalyzer throwableAnalyzer = new ThrowableAnalyzer();

    @Override
    public ResponseEntity translate(Exception e) throws Exception {

        //获取异常栈信息
        Throwable[] throwables = this.throwableAnalyzer.determineCauseChain(e);

        //获取AuthenticationException异常信息,不存在则返回的是null
        Exception ase = (AuthenticationException) this.throwableAnalyzer.getFirstThrowableOfType(AuthenticationException.class, throwables);
        if (ase != null) {
            log.error(ase.getMessage());
            return ResponseEntity.ok(ReturnData.failed(new FastRunTimeException(ErrorEnum.认证异常)));
        }

        //获取AccessDeniedException异常信息,不存在则返回的是null
        ase = (AccessDeniedException) this.throwableAnalyzer.getFirstThrowableOfType(AccessDeniedException.class, throwables);
        if (ase != null) {
            log.error(ase.getMessage());
            return ResponseEntity.ok(ReturnData.failed(new FastRunTimeException(ErrorEnum.权限异常)));
        }
        return ResponseEntity.ok(ReturnData.success());
    }

}

