package com.meta.model.request.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
public class UserOauthVo implements UserDetails {

    private Long userId;
    private String username;
    private String password;
    private Boolean enabled;

    private Collection<SimpleGrantedAuthority> authorities;

    public UserOauthVo(Long userId, String username, String password, Boolean enabled, Collection<SimpleGrantedAuthority> authorities) {
        this.setUserId(userId);
        this.setUsername(username);
        this.setPassword(password);
        this.setEnabled(true);
        this.setAuthorities(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
