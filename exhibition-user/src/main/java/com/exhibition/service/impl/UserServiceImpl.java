package com.exhibition.service.impl;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserProfileRepository userProfileRepository;
    private final UserSportProfileRepository userSportProfileRepository;
    private final UserProfileMapper UserProfileMapper;
    private final UserSportProfileMapper userSportProfileMapper;



    @Override
    public ProcessPayload<UserDataQueryDto> userDataQuery(CustomUserDetail userDetails) {


        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userDetails.getUserId());
        UserProfileDto dto = userProfile.map(UserProfileMapper::UserProfileToUserProfileDto).orElse(null);
        if (dto == null) {
            return ProcessPayload.authenticateFail("找不到用戶資料",new RuntimeException("找不到用戶資料"));
        }
        List <UserSportProfile> userSportProfilesList = userSportProfileRepository.findByUserId(dto.getUserId());
        List<UserSportProfileDto> userSportProfileDtoList = MapperUtils.mapList(userSportProfilesList, UserSportProfileDto.class);
        UserDataQueryDto userDataQueryDto = new UserDataQueryDto();
        userDataQueryDto.setUserProfileDto(dto);
        userDataQueryDto.setUserSportProfileDtoList(userSportProfileDtoList);

        return ProcessPayload.success(userDataQueryDto);
    }

    @Transactional
    @Override
    public ProcessPayload<String> updateUserProfile(CustomUserDetail userDetails, UserProfileUpdateReq userProfileUpdateReq) {

        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userDetails.getUserId());
        if (userProfile.isPresent()) {
            UserProfile userProfileEntity = userProfile.get();
            UserProfileDto dto = userProfileUpdateReq.getUserProfileDto();
            UserProfileMapper.updateEntityFromDto(dto, userProfileEntity);
            userProfileRepository.save(userProfileEntity);
            return ProcessPayload.success("用戶資料更新成功");
        } else {
            return ProcessPayload.fail("用戶資料不存在，更新失敗");
        }
      
    }

    @Transactional
    @Override
    public ProcessPayload<String> updateUserSportProfile(CustomUserDetail userDetails, UserSportProfileUpdateReq userSportProfileUpdateReq) {
        List<UserSportProfile> userSportProfileEntityList = userSportProfileRepository.findByUserId(userDetails.getUserId());
        if (userSportProfileEntityList.isEmpty()) {
            for (UserSportProfileDto dto : userSportProfileUpdateReq.getUserSportProfileDtoList()) {
                UserSportProfile userSportProfileEntity = userSportProfileMapper.UserSportProfileDtoToUserSportProfile(dto);
                userSportProfileEntity.setUserId(userDetails.getUserId());
                userSportProfileRepository.save(userSportProfileEntity);
            }

        } else {
            Map<String, UserSportProfile> userSportProfileEntityMap = userSportProfileEntityList.stream()
                    .collect(Collectors.toMap(UserSportProfile::getSportType, Function.identity()));
            for (UserSportProfileDto userSportProfileDto : userSportProfileUpdateReq.getUserSportProfileDtoList()) {
                UserSportProfile userSportProfileEntity = userSportProfileEntityMap.get(userSportProfileDto.getSportType());
                if (userSportProfileEntity != null) {
                    userSportProfileMapper.updateEntityFromDto(userSportProfileDto, userSportProfileEntity);
                    userSportProfileRepository.save(userSportProfileEntity);
                }
            }


        }
        return ProcessPayload.success("用戶運動資料更新成功");
    }


}
