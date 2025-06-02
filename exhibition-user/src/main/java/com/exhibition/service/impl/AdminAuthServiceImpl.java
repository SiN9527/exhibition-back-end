package com.exhibition.service.impl;

import com.exhibition.dto.auth.AdminRegisterRequest;
import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.dto.user.AdminMemberDeleteRequest;
import com.exhibition.dto.user.AdminMemberListRequest;
import com.exhibition.dto.user.AdminMemberProfileResponse;
import com.exhibition.dto.user.AdminMemberUpdateRequest;
import com.exhibition.entity.admin.AdminEventEntity;
import com.exhibition.entity.admin.AdminMainEntity;
import com.exhibition.entity.member.MemberMainEntity;
import com.exhibition.exception.AuthorizationFailedException;
import com.exhibition.exception.LogicalProhibitedException;
import com.exhibition.repository.*;
import com.exhibition.service.AdminAuthService;
import com.exhibition.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    // 使用 LoggerFactory 建立 Logger 實例，傳入當前類別作為參數
    private static final Logger logger = LoggerFactory.getLogger(AdminAuthServiceImpl.class);

    private final PasswordEncoder passwordEncoder;
    private final AdminMainRepository adminMainRepository;
    //    private final AdminMainRepository adminMainRepository;
    private final AdminEventRepository adminEventRepository;
    //    private final EventRepository eventRepository;
    private final MemberMainRepository memberMainRepository;
    private final MemberEventRepository memberEventRepository;
    private final MemberMainRoleRepository memberMainRoleRepository;



    @Override
    public String adminRegister(@RequestBody AdminRegisterRequest req) {


        if (adminMainRepository.existsByAccount(req.getAccount())) {
            log.info("Admin registration failed: Account already exists. Please use another Account.");
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
           throw new LogicalProhibitedException("Account already exists. Please use another Account.");
        }
        // 建立新使用者實體，並設定相關欄位
        AdminMainEntity user = new AdminMainEntity();
        user.setAccount(req.getAccount());
        user.setEmail(req.getEmail() != null ? req.getEmail() : "");
        user.setUserName(req.getUserName());
        user.setEnabled(true); // 預設帳號啟用
        // 密碼加密處理
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEventId("All");
        // 儲存使用者資料到資料庫
        adminMainRepository.save(user);
        logger.info("Admin registered successfully: {}", user.getEmail());
        // 使用 ApiResponse.success() 包裝成功訊息，再回傳 ResponseEntity
        return "Admin registered successfully.";
    }


    @Override
    public String adminLogin(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public String adminLogout() {
        return null;
    }

    @Override
    public String adminRefreshToken(String refreshToken) {
        return null;
    }

    @Override
    public String adminUpdateProfile(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public List<AdminMemberProfileResponse> adminGetMemberList(AdminMemberListRequest req) {

        String account = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Admin get member list: {}", account);
        // 驗證管理員是否有對應活動的權限
        Optional<AdminEventEntity> adminEventOpt = adminEventRepository.findByAccountAndEventId(account, req.getEventId());

        if (adminEventOpt.isEmpty()) {
           throw new AuthorizationFailedException("Access Denied: You do not have permission to access this event.");
        }
        // 查詢該活動下所有的會員 ID
        List<String> memberIds = memberEventRepository.findMemberIdsByEventId(req.getEventId());

        // 查詢會員主資料
        List<MemberMainEntity> members = memberMainRepository.findAllByMemberIdIn(memberIds);

        // 封裝成 DTO 回傳

        return members.stream()
                .map(member -> MapperUtils.map(member, AdminMemberProfileResponse.class))
                .collect(Collectors.toList());


    }

    @Override
    public MemberMainEntityDto adminGetMemberProfile(AdminRegisterRequest req) {
        return null;
    }


    @Override
    public String adminUpdatePwd(AdminRegisterRequest req) {
        return null;
    }

    /**
     * 編輯會員資訊
     */
    @Transactional
    @Override
    public String adminUpdateMemberProfile(AdminMemberUpdateRequest req) {

        String account = SecurityContextHolder.getContext().getAuthentication().getName();

        // 驗證管理員是否有對應活動的權限
        Optional<AdminEventEntity> adminEventOpt = adminEventRepository.findByAccountAndEventId(account, req.getEventId());

        if (adminEventOpt.isEmpty()) {
            throw new AuthorizationFailedException("Access Denied: You do not have permission to access this event.");
        }

        MemberMainEntity member = memberMainRepository.findById(req.getMemberId()).orElse(null);
        if (member == null) {
           throw new LogicalProhibitedException("Member not found");
        }


        req.setMemberId(member.getMemberId());
        req.setEmail(member.getEmail());
        req.setPassword(member.getPassword());


        MemberMainEntity entity = MapperUtils.map(req, MemberMainEntity.class);
        entity.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        entity.setUpdatedBy(account);
        entity.setCreatedBy(member.getCreatedBy());
        entity.setCreateTime(member.getCreateTime());
        entity.setEnabled(member.getEnabled());
        entity.setRegistrationDate(member.getRegistrationDate());

        memberMainRepository.save(entity);

        return "Profile updated successfully ";
    }

    /**
     * 刪除會員
     */
    @Transactional
    @Override
    public String adminDeleteMemberProfile(AdminMemberDeleteRequest req) {

        String memberId = req.getMemberId();
        String eventId = req.getEventId();
        String account = SecurityContextHolder.getContext().getAuthentication().getName();

        // 驗證管理員是否有對應活動的權限
        Optional<AdminEventEntity> adminEventOpt = adminEventRepository.findByAccountAndEventId(account, req.getEventId());

        if (adminEventOpt.isEmpty()) {
            throw new AuthorizationFailedException("Access Denied: You do not have permission to access this event.");
        }

        MemberMainEntity member = memberMainRepository.findById(req.getMemberId()).orElse(null);
        if (member == null) {
            throw new LogicalProhibitedException("Member not found");
        }

        // 刪除關聯表再刪主資料
        memberEventRepository.deleteByMemberIdAndEventId(memberId, eventId);
        memberMainRoleRepository.deleteByPk_MemberId(memberId);
        memberMainRepository.deleteById(memberId);

        return "Member Delete Success !";
    }


//    /**
//     * 取得該 Admin 所管理的活動下的所有會員清單
//     */
//    @Override
//    public ResponseEntity<ApiResponseTemplate<List<AdminMemberListResponse>>> adminGetMemberList(String adminId, String eventId) {
//
//        // 驗證該 admin 是否有管理該活動
//        boolean isAuthorized = adminMainEventRepository.existsByAdminIdAndEventId(adminId, eventId);
//        if (!isAuthorized) {
//            return ResponseEntity.status(403).body(ApiResponseTemplate.fail(403, "FORBIDDEN", "您無權限存取此活動資料"));
//        }
//
//        // 找出該活動的所有會員
//        List<MemberMainEntity> members = memberMainEventRepository.findAllByEventId(eventId)
//                .stream()
//                .map(MemberMainEventEntity::getMember)
//                .collect(Collectors.toList());
//
//        List<AdminMemberListResponse> responseList = members.stream()
//                .map(member -> MapperUtils.map(member, AdminMemberListResponse.class))
//                .collect(Collectors.toList());
//
//        return ResponseEntity.ok(ApiResponseTemplate.success("取得會員清單成功", responseList));
//    }
//
//    /**
//     * 根據 memberId 取得會員詳細資料
//     */
//    @Override
//    public ResponseEntity<ApiResponseTemplate<AdminMemberProfileResponse>> adminGetMemberProfile(String adminId, String eventId, String memberId) {
//
//        boolean isAuthorized = adminMainEventRepository.existsByAdminIdAndEventId(adminId, eventId);
//        if (!isAuthorized) {
//            return ResponseEntity.status(403).body(ApiResponseTemplate.fail(403, "FORBIDDEN", "無權限檢視會員資料"));
//        }
//
//        MemberMainEntity member = memberMainRepository.findById(memberId).orElse(null);
//        if (member == null) {
//            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, "MEMBER_NOT_FOUND", "找不到會員"));
//        }
//
//        AdminMemberProfileResponse response = MapperUtils.map(member, AdminMemberProfileResponse.class);
//        return ResponseEntity.ok(ApiResponseTemplate.success("取得會員資料成功", response));
//    }
//


//
//
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminLogin(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminLogout() {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminRefreshToken(String refreshToken) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateProfile(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminGetMemberList(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminGetMemberProfile(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateMemberProfile(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminDeleteMemberProfile(AdminRegisterRequest req) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<ApiResponseTemplate<?>> adminUpdatePwd(AdminRegisterRequest req) {
//        return null;
//    }


}

// // **使用 Spring Security 的 `authenticate()` 驗證身份**  不手動loadUser
//    Authentication authentication = authenticationManager.authenticate(
//            new UsernamePasswordAuthenticationToken(email, password)
//    );
//
//    log.info("驗證通過: {}", authentication.getName());
//
//    // **取得 UserDetails**
//    UserDetails userDetails = (UserDetails) authentication.getPrincipal();