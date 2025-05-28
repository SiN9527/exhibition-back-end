package com.exhibition.service;

import com.exhibition.dto.user.CustomAdminDetail;
import com.exhibition.dto.user.CustomMemberDetail;
import com.exhibition.entity.admin.AdminMainEntity;
import com.exhibition.entity.admin.AdminRoleEntity;
import com.exhibition.entity.admin.JwtAdminDetails;
import com.exhibition.exception.AuthenticationFailedException;
import com.exhibition.mapper.AdminMainMapper;
import com.exhibition.repository.AdminMainRepository;
import com.exhibition.repository.AdminMainRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class JwtAdminDetailsService implements UserDetailsService {

    private final AdminMainRepository adminMainRepository;
    private final AdminMainRoleRepository adminMainRoleRepository;

    private final AdminMainMapper mapper;

    // 使用構造器注入，並使用 final 保持依賴的不可變性




    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        // 查詢使用者
        AdminMainEntity admin = adminMainRepository.findByAccount(account)
                .orElseThrow(() -> new AuthenticationFailedException("Not found with account: " + account));
        if (!admin.getEnabled()) {
            throw new AuthenticationFailedException("Account is disabled");
        }

        // 透過關聯表查詢該使用者的角色
        List<AdminRoleEntity> roles = adminMainRoleRepository.findRolesByAdminId(admin.getAdminId());

        // 轉換成 Spring Security 需要的角色格式
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode())) // Spring Security 需要加 "ROLE_"
                .collect(Collectors.toList());

        return new CustomAdminDetail(mapper.AdminMainEntityToAdminMainEntityDto(admin), authorities);
    }

   public boolean  adminExistsByEmail(String email) {
        return adminMainRepository.existsByEmail(email);
    }
    public boolean  adminExistsByAccount(String account) {
        return adminMainRepository.existsByAccount(account);
    }
}

