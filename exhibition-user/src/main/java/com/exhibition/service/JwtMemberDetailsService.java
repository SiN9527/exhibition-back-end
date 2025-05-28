package com.exhibition.service;


import com.exhibition.dto.user.CustomMemberDetail;
import com.exhibition.entity.member.JwtMemberDetails;
import com.exhibition.entity.member.MemberMainEntity;
import com.exhibition.entity.member.MemberRoleEntity;
import com.exhibition.exception.AuthenticationFailedException;
import com.exhibition.mapper.MemberMainMapper;
import com.exhibition.repository.MemberMainRepository;
import com.exhibition.repository.MemberMainRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtMemberDetailsService implements UserDetailsService {

    private final MemberMainRepository memberMainRepository;
    private final MemberMainRoleRepository memberMainRoleRepository;
    private final MemberMainMapper memberMainMapper;

    // 使用構造器注入
    public JwtMemberDetailsService(MemberMainRepository memberMainRepository,
                                   MemberMainRoleRepository memberMainRoleRepository,
                                   MemberMainMapper memberMainMapper) {
        this.memberMainRepository = memberMainRepository;
        this.memberMainRoleRepository = memberMainRoleRepository;
        this.memberMainMapper = memberMainMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        // 查詢會員
        MemberMainEntity member = memberMainRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationFailedException("Not found with email"));

        if (!member.getEnabled()) {
            throw new AuthenticationFailedException("Account is disabled");
        }

        // 透過關聯表查詢該會員的角色
        List<MemberRoleEntity> roles = memberMainRoleRepository.findRolesByMemberId(member.getMemberId());

        // 轉換成 Spring Security 需要的角色格式
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleCode())) // Spring Security 需要加 "ROLE_"
                .collect(Collectors.toList());


        // 返回 Spring Security 的 UserDetails
        return new CustomMemberDetail(memberMainMapper.MemberMainEntityToMemberMainEntityDto(member), authorities);
    }


    public boolean memberExists(String email) {
        return memberMainRepository.existsByEmail(email);
    }
}
