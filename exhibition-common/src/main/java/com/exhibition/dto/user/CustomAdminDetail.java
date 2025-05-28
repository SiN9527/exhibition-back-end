package com.exhibition.dto.user;

import com.exhibition.dto.auth.AdminMainEntityDto;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Builder
public class CustomAdminDetail implements UserDetails {


    private final AdminMainEntityDto dto;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomAdminDetail(AdminMainEntityDto dto, Collection<? extends GrantedAuthority> authorities) {
        this.dto = dto;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return dto.getPassword();
    }

    public String getAccount() {
        return dto.getAccount();
    }

    public Long getAdminId() {
        return dto.getAdminId();
    }

    public String getEmail() {
        return dto.getEmail();
    }

    @Override
    public String getUsername() {
        return dto.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !dto.getEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return dto.getEnabled();
    }
}
