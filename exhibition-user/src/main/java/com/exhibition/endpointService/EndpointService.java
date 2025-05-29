package com.exhibition.endpointService;

import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.dto.user.MemberProfileResponse;
import com.exhibition.dto.user.MemberUpdateRequest;
import com.exhibition.entity.member.MemberMainEntity;
import com.exhibition.exception.LogicalProhibitedException;
import com.exhibition.mapper.MemberMainMapper;
import com.exhibition.repository.AdminMainRepository;
import com.exhibition.repository.MemberMainRepository;
import com.exhibition.repository.MemberMainRoleRepository;
import com.exhibition.service.JwtMemberDetailsService;
import com.exhibition.service.JwtService;
import com.exhibition.service.MemberService;
import com.exhibition.utils.CodeGeneratorUtils;
import com.exhibition.utils.JwtUtil;
import com.exhibition.utils.MapperUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class EndpointService  {

    private final MemberMainRepository memberMainRepository;
    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(EndpointService.class);
    private final JwtUtil jwtUtil;
    private final JwtService jwtService;
    private final JwtMemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CodeGeneratorUtils codeGeneratorUtils;
    private final MemberMainMapper memberMainMapper;
    private final NotificationService notificationService;
    private final AdminMainRepository userRepository;
    private final MemberMainRoleRepository memberMainRoleRepository;
    private final MapperUtils mapperUtils;


    // **取得會員資料**

    public MemberMainEntityDto memberGetProfile(String memberId) {

        Optional<MemberMainEntity> memberOpt = memberMainRepository.findByMemberId(memberId);
        if (memberOpt.isEmpty()) {
            throw new LogicalProhibitedException("Member not found with ID: " + memberId);
        }
        MemberMainEntity member = memberOpt.get();

        return memberMainMapper.MemberMainEntityToMemberMainEntityDto(member);
    }




}

