package com.exhibition.service.impl;

import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.AdminRegisterRequest;
import com.exhibition.dto.user.AdminMemberDeleteRequest;
import com.exhibition.dto.user.AdminMemberListRequest;
import com.exhibition.dto.user.AdminMemberProfileResponse;
import com.exhibition.dto.user.AdminMemberUpdateRequest;
import com.exhibition.entity.admin.AdminEventEntity;
import com.exhibition.entity.admin.AdminMainEntity;
import com.exhibition.entity.member.MemberMainEntity;
import com.exhibition.enums.ErrorCode;
import com.exhibition.repository.*;
import com.exhibition.service.AdminAuthService;
import com.exhibition.utils.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    /**
     * 註冊 API，建立新使用者後回傳統一格式的成功訊息。
     *
     * @param req 前端傳入的使用者註冊資料
     * @return 統一格式的 ApiResponse 物件，payload 為成功訊息
     */

    public ResponseEntity<ApiResponseTemplate<String>> adminRegister(@RequestBody AdminRegisterRequest req) {


        if (adminMainRepository.existsByAccount(req.getAccount())) {
            log.info("Admin registration failed: Account already exists. Please use another Account.");
            // 使用 ApiResponse.fail() 包裝失敗訊息，再回傳 ResponseEntity
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400,
                    ErrorCode.ACCOUNT_ALREADY_REGISTERED)
            );

        }
        // 建立新使用者實體，並設定相關欄位
        AdminMainEntity user = new AdminMainEntity();
        user.setAccount(req.getAccount());
        user.setEmail(req.getEmail() != null ? req.getEmail() : "");
        user.setUserName(req.getUserName());
        user.setEnabled(false); // 預設帳號未啟用
        // 密碼加密處理
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setEventId("All");
        // 儲存使用者資料到資料庫
        adminMainRepository.save(user);
        logger.info("Admin registered successfully: {}", user.getEmail());
        // 使用 ApiResponse.success() 包裝成功訊息，再回傳 ResponseEntity
        return ResponseEntity.ok(ApiResponseTemplate.success("Admin registered successfully."));
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminLogin(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminLogout() {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminRefreshToken(String refreshToken) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminUpdateProfile(AdminRegisterRequest req) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponseTemplate<List<AdminMemberProfileResponse>>> adminGetMemberList(AdminMemberListRequest req) {

        String account = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Admin get member list: {}", account);
        // 驗證管理員是否有對應活動的權限
        Optional<AdminEventEntity> adminEventOpt = adminEventRepository.findByAccountAndEventId(account, req.getEventId());

        if (adminEventOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseTemplate.fail(403, "You do not have access to this event."));
        }
        // 查詢該活動下所有的會員 ID
        List<String> memberIds = memberEventRepository.findMemberIdsByEventId(req.getEventId());

        // 查詢會員主資料
        List<MemberMainEntity> members = memberMainRepository.findAllByMemberIdIn(memberIds);

        // 封裝成 DTO 回傳
        List<AdminMemberProfileResponse> responseList = members.stream()
                .map(member -> MapperUtils.map(member, AdminMemberProfileResponse.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponseTemplate.success("Successfully retrieved member list.", responseList));


    }

    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminGetMemberProfile(AdminRegisterRequest req) {
        return null;
    }


    @Override
    public ResponseEntity<ApiResponseTemplate<?>> adminUpdatePwd(AdminRegisterRequest req) {
        return null;
    }

    /**
     * 編輯會員資訊
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponseTemplate<String>> adminUpdateMemberProfile(AdminMemberUpdateRequest req) {

        String account = SecurityContextHolder.getContext().getAuthentication().getName();

        // 驗證管理員是否有對應活動的權限
        Optional<AdminEventEntity> adminEventOpt = adminEventRepository.findByAccountAndEventId(account, req.getEventId());

        if (adminEventOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseTemplate.fail(403, "You do not have access to this event."));
        }

        MemberMainEntity member = memberMainRepository.findById(req.getMemberId()).orElse(null);
        if (member == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, ErrorCode.MEMBER_NOT_FOUND));
        }


        req.setMemberId(member.getMemberId());
        req.setEmail(member.getEmail());
        req.setPassword(member.getPassword());


        MemberMainEntity entity = MapperUtils.map(req, MemberMainEntity.class);
        entity.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setUpdatedBy(account);
        entity.setCreatedBy(member.getCreatedBy());
        entity.setCreatedAt(member.getCreatedAt());
        entity.setEnabled(member.getEnabled());
        entity.setRegistrationDate(member.getRegistrationDate());

        memberMainRepository.save(entity);


        return ResponseEntity.ok(ApiResponseTemplate.success("Profile updated successfully "));
    }

    /**
     * 刪除會員
     */
    @Override
    @Transactional
    public ResponseEntity<ApiResponseTemplate<String>> adminDeleteMemberProfile(AdminMemberDeleteRequest req) {

        String memberId = req.getMemberId();
        String eventId = req.getEventId();
        String account = SecurityContextHolder.getContext().getAuthentication().getName();

        // 驗證管理員是否有對應活動的權限
        Optional<AdminEventEntity> adminEventOpt = adminEventRepository.findByAccountAndEventId(account, req.getEventId());

        if (adminEventOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponseTemplate.fail(403, "You do not have access to this event."));
        }

        MemberMainEntity member = memberMainRepository.findById(memberId).orElse(null);
        if (member == null) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(404, ErrorCode.MEMBER_NOT_FOUND));
        }

        // 刪除關聯表再刪主資料
        memberEventRepository.deleteByMemberIdAndEventId(memberId, eventId);
        memberMainRoleRepository.deleteByPk_MemberId(memberId);
        memberMainRepository.deleteById(memberId);

        return ResponseEntity.ok(ApiResponseTemplate.success("會員資料刪除成功"));
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