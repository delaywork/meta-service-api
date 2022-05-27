package com.meta.configure;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class MetaPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        log.info("MetaPasswordEncoder ---> encode");
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        log.info("MetaPasswordEncoder ---> matches");
        return false;
    }
}
