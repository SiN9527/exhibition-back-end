package com.exhibition.service.impl;


import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.dto.signup.*;
import com.exhibition.endpointService.UserService;
import com.exhibition.entity.*;
import com.exhibition.enums.ErrorCode;
import com.exhibition.exception.LogicalProhibitedException;
import com.exhibition.repo.*;
import com.exhibition.service.RegistrationService;
import com.exhibition.utils.JwtUtil;
import com.exhibition.utils.MapperUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RegistrationMainRepository registrationMainRepository;
    private final RegistrationDetailRepository registrationDetailRepository;
    private final RegistrationExtraRepository registrationExtraRepository;
    private final RegistrationGroupMainRepository registrationGroupMainRepository;
    private final AccompanyingPersonEntityRepository accompanyingPersonEntityRepository;
    private final RegistrationPaymentInfoRepository registrationPaymentInfoRepository;
    private final MemberMainRepository memberMainRepository;

    private final IdGeneratorUtils idGeneratorUtils;
    private final JwtUtil jwtUtil;
    private final MapperUtils mapper;
    private final UserService userService;

    @Override
    public RegistrationQueryResponse registerStep1Query(UserDetails userDetails, HttpServletResponse response) {
        String memberId = userDetails.getUsername();
        MemberMainEntityDto member = userService.getMember(memberId);

        MemberMainDto memberDto = MapperUtils.map(member, MemberMainDto.class);
        return RegistrationQueryResponse.builder().memberMainDto(memberDto).build();

    }

    /**
     * Step 1 報名：填寫個人資料（個人報名或團體主報名人）
     */
    @Override
    @Transactional
    public SoloRegistrationEventResponse registerStep1(SoloRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response) {


        String memberId = userDetails.getUsername();
        MemberMainEntityDto member = userService.getMember(memberId);


        RegistrationMainDto registrationMainDto = req.getRegistrationMainDto();
        RegistrationDetailDto registrationDetailDto = req.getRegistrationDetailDto();
        RegistrationExtraDto registrationExtraDto = req.getRegistrationExtraDto();
        RegistrationPaymentInfoDto registrationPaymentInfoDto = req.getRegistrationPaymentInfoDto();
        List<AccompanyingPersonDto> accompanyingPersonDtoList = req.getAccompanyingPersonDtoList();

        String registrationId = idGeneratorUtils.generateRegNo(registrationDetailDto.getCountryOfAffiliation(),registrationMainDto.getRegistrationType());
        // 1. 檢查該會員是否已有此活動報名紀錄
        Optional<RegistrationMainEntity> mainEntity = registrationMainRepository.findByEventIdAndMemberId(registrationMainDto.getEventId(), member.get().getMemberId());

    if (mainEntity.isPresent()) {
            throw new LogicalProhibitedException(
                    "You have already registered for this event. If you need to update your registration, please contact the event organizer.");
        }
        createNewRegistrationMain(req,member, registrationId);
        // 2. 儲存報名詳細資料
       saveRegistrationDetail(registrationDetailDto,registrationId);
       saveRegistrationExtra(registrationExtraDto,registrationId);
        saveRegistrationPaymentInfo(registrationPaymentInfoDto,registrationId,false);
       saveRegistrationAccompany(accompanyingPersonDtoList,registrationId);


        SoloRegistrationEventResponse soloRegistrationEventResponse = querySoloRegistrationResponse(member.get().getMemberId());

        return ResponseEntity.ok(ApiResponseTemplate.success("Registration Step 1 completed.",soloRegistrationEventResponse
        ));
    }

    private void saveRegistrationPaymentInfo(RegistrationPaymentInfoDto registrationPaymentInfoDto, String registrationId,boolean isGroup) {
        RegistrationPaymentInfoEntity registrationPaymentInfoEntity = MapperUtils.map(registrationPaymentInfoDto, RegistrationPaymentInfoEntity.class);
        registrationPaymentInfoEntity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        registrationPaymentInfoEntity.setRegistrationId(registrationId);
        registrationPaymentInfoEntity.setGroup(isGroup);

        registrationPaymentInfoRepository.save(registrationPaymentInfoEntity);
        registrationPaymentInfoRepository.flush();
    }

    private void saveRegistrationAccompany(List<AccompanyingPersonDto> accompanyingPersonDtoList, String registrationId) {
        if (!accompanyingPersonDtoList.isEmpty()) {
            AtomicInteger seq = new AtomicInteger(0);
            accompanyingPersonDtoList.forEach(x ->
            {
                x.setMemberFollowedId(registrationId);
                x.setSeq(seq.getAndIncrement());
                x.setCreateAt(new Timestamp(System.currentTimeMillis()));
            });
            List<AccompanyingPersonEntity> accompanyingPersonEntities = MapperUtils.mapList(accompanyingPersonDtoList, AccompanyingPersonEntity.class);
            accompanyingPersonEntityRepository.saveAll(accompanyingPersonEntities);
        }
    }

    /**
     * 團體報名 Step1
     */
    @Transactional
    @Override
    public ResponseEntity<ApiResponseTemplate<GroupRegistrationEventResponse>> registerGroupStep1(GroupRegistrationEventRequest req, UserDetails userDetails, HttpServletResponse response) {

        Optional<MemberMainEntity> member = jwtUtil.validateAndGetEntity(userDetails, memberMainRepository);
        if (member.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseTemplate.fail(400, ErrorCode.MEMBER_NOT_FOUND));
        }
       String eventId = req.getGroupRegistrationMainDto().get(0).getEventId();
        // 產生團體代碼，例如 GP1-2025-UUID縮寫
        Integer groupId = idGeneratorUtils.generateGroupIndex(eventId);
        String groupCode = "GP" + groupId;
        int seq = 1;

        AtomicInteger counter = new AtomicInteger(1);


        for (GroupRegistrationMainDto dto : req.getGroupRegistrationMainDto()) {
            String regId = idGeneratorUtils.generateFullRegistrationNumber(groupCode, seq);
            if (dto.getIsGroupMain()){
                saveRegistrationPaymentInfo(req.getRegistrationPaymentInfoDto(),regId,true);
            }
            RegistrationDetailDto registrationDetailDto = dto.getRegistrationDetailDto();
            RegistrationExtraDto registrationExtraDto = dto.getRegistrationExtraDto();
            List<AccompanyingPersonDto> accompanyingPersonDtoList = dto.getAccompanyingPersonDtoList();

            saveGroupRegistrationMain(dto,member.get(),regId,groupCode);
            saveRegistrationDetail(registrationDetailDto,regId);
            saveRegistrationExtra(registrationExtraDto,regId);
            saveRegistrationAccompany(accompanyingPersonDtoList,regId);

            RegistrationGroupMainEntity groupMainEntity = RegistrationGroupMainEntity.builder()
                    .groupId(groupCode)
                    .eventId(eventId)
                    .contactName(member.get().getLastName())
                    .contactEmail(member.get().getEmail())
                    .contactPhone(member.get().getTelNumber())
                    .groupSize(req.getGroupRegistrationMainDto().size())
                    .paymentStatus("UNPAID")
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .build();
            registrationGroupMainRepository.save(groupMainEntity);
            seq++;
            counter.incrementAndGet();
        }

        GroupRegistrationEventResponse groupRegistrationEventResponse = queryGroupRegistrationResponse(member.get().getMemberId(), groupCode);
        return ResponseEntity.ok(ApiResponseTemplate.success(("Group registration submitted successfully. Group Code: " + groupCode),groupRegistrationEventResponse));
    }


    private SoloRegistrationEventResponse querySoloRegistrationResponse(String memberId) {
        List<RegistrationMainEntity> registrationMainList = registrationMainRepository.findByMemberIdAndGroupCode(memberId, null);
        if (registrationMainList.isEmpty()) {
            throw new RuntimeException("No registration details found for member ID: " + memberId );
        }
        RegistrationMainDto registrationMainDto = MapperUtils.map(registrationMainList.get(0), RegistrationMainDto.class);
        RegistrationPaymentInfoEntity paymentInfoEntity = registrationPaymentInfoRepository.findByRegistrationIdAndIsGroup(registrationMainList.get(0).getRegistrationId(),false);
        if (paymentInfoEntity == null) {
            throw new RuntimeException("No payment information found for registration ID: " + registrationMainList.get(0).getMemberId());
        }
        RegistrationPaymentInfoDto paymentInfoDto = MapperUtils.map(paymentInfoEntity, RegistrationPaymentInfoDto.class);


        SoloRegistrationEventResponse response = new SoloRegistrationEventResponse();
        response.setRegistrationMainDto(registrationMainDto);
        response.setRegistrationPaymentInfoDto(paymentInfoDto);
        return response;

    }


    private GroupRegistrationEventResponse queryGroupRegistrationResponse(@Size(max = 50) String memberId, String groupCode) {
        List<RegistrationMainEntity> registrationMainList = registrationMainRepository.findByMemberIdAndGroupCode(memberId, groupCode);
        if (registrationMainList.isEmpty()) {
            throw new RuntimeException("No registration details found for member ID: " + memberId );
        }
        String groupLeaderId = registrationMainList.get(0).getRegistrationId();

        RegistrationPaymentInfoEntity paymentInfoEntity = registrationPaymentInfoRepository.findByRegistrationIdAndIsGroup(groupLeaderId,true);
        if (paymentInfoEntity == null) {
            throw new RuntimeException("No payment information found for registration ID: " + groupLeaderId);
        }
        RegistrationPaymentInfoDto paymentInfoDto = MapperUtils.map(paymentInfoEntity, RegistrationPaymentInfoDto.class);

        List<RegistrationMainEntity> registrationMainEntities = registrationMainRepository.findByGroupCode(groupCode);
        List<RegistrationMainDto> registrationMainDtoList = MapperUtils.mapList(registrationMainEntities, RegistrationMainDto.class);
        GroupRegistrationEventResponse response = new GroupRegistrationEventResponse();
        response.setRegistrationMainDtoList(registrationMainDtoList);
        response.setRegistrationPaymentInfoDto(paymentInfoDto);
        return response;

    }


    private void saveGroupRegistrationMain(GroupRegistrationMainDto dto, MemberMainEntityDto member, String regId,String groupCode){



        RegistrationMainEntity mainEntity = RegistrationMainEntity.builder()
                .registrationId(regId)
                .eventId(dto.getEventId())
                .memberId(dto.getIsGroupMain()? member.getMemberId():"")
                .groupCode(groupCode)
                .registrationType(dto.getRegistrationType())
                .isDomestic(dto.getIsDomestic())
                .feeAmount(dto.getFeeAmount()) // Step2再計算
                .paymentStatus("UNPAID")
                .registrationStatus("PENDING")
                .isGroupMain(dto.getIsGroupMain())
                .anyAccompanyingPerson(dto.getAnyAccompanyingPerson())
                .build();
        registrationMainRepository.save(mainEntity);
        registrationMainRepository.flush();

    }

    private void saveRegistrationDetail(RegistrationDetailDto registrationDetailDto,String registrationId){

        RegistrationDetailEntity detailEntity = RegistrationDetailEntity.builder()
                .registrationId(registrationId)
                .title(registrationDetailDto.getTitle())
                .firstName(registrationDetailDto.getFirstName())
                .lastName(registrationDetailDto.getLastName())
                .fullNameCn("Taiwan".equals(registrationDetailDto.getCountryOfAffiliation()) ? registrationDetailDto.getFullNameCn() : "")
                .gender(registrationDetailDto.getGender())
                .dateOfBirth(registrationDetailDto.getDateOfBirth())
                .nationality(registrationDetailDto.getNationality())
                .passportNumber(registrationDetailDto.getPassportNumber())
                .department(registrationDetailDto.getDepartment())
                .affiliation(registrationDetailDto.getAffiliation())
                .cityOfAffiliation(registrationDetailDto.getCityOfAffiliation())
                .countryOfAffiliation(registrationDetailDto.getCountryOfAffiliation())
                .telNumber(registrationDetailDto.getTelNumber())
                .mobileNumber(registrationDetailDto.getMobileNumber())
                .email(registrationDetailDto.getEmail())
                .registrationRole("PRESENT")
                .uploadUrl("")
                .isAccompanyingPerson(false)
                .build();
        registrationDetailRepository.save(detailEntity);
    }

    private void saveRegistrationExtra(RegistrationExtraDto dto,String registrationId){

        RegistrationExtraEntity registrationExtraEntity = MapperUtils.map(dto, RegistrationExtraEntity.class);
        registrationExtraEntity.setCreateTime(new Timestamp(System.currentTimeMillis()));
        registrationExtraEntity.setRegistrationId(registrationId);
        registrationExtraRepository.save(registrationExtraEntity);
    }


    /**
     * 若無主表則新增一筆
     */
    private RegistrationMainEntity createNewRegistrationMain(SoloRegistrationEventRequest req, MemberMainEntityDto member, String regId) {
        RegistrationMainDto registrationMainDto = req.getRegistrationMainDto();
        RegistrationMainEntity mainEntity = RegistrationMainEntity.builder()
                .registrationId(regId)
                .eventId(req.getRegistrationMainDto().getEventId())
                .memberId(member.getMemberId())
                .registrationType(registrationMainDto.getRegistrationType())
                .isDomestic(registrationMainDto.getIsDomestic())
                .feeAmount(registrationMainDto.getFeeAmount()) // Step2再計算
                .paymentStatus("UNPAID")
                .registrationStatus("PENDING")
                .isGroupMain(false)
                .anyAccompanyingPerson(registrationMainDto.getAnyAccompanyingPerson())
                .build();
        registrationMainRepository.save(mainEntity);
        return mainEntity;
    }
}
