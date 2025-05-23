package com.exhibition.service.impl;

import com.hititoff.dto.UserDto;
import com.hititoff.dto.auth.RegiReq;
import com.hititoff.dto.auth.RegiResp;
import com.hititoff.dto.user.UserProfileDto;
import com.hititoff.entity.UserProfile;
import com.hititoff.mapper.UserProfileMapper;
import com.hititoff.repository.UserProfileRepository;
import com.hititoff.service.UserEndpointService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserEndpointServiceImpl implements UserEndpointService {
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;



    @Override
    public RegiResp registerUser(RegiReq regiReq) {



        UserProfileDto dto = UserProfileDto.builder()
                .userId(regiReq.getId())
                .nickname(regiReq.getNickname())
                .location(regiReq.getLocation())
                .gender(regiReq.getGender())
                .level(regiReq.getLevel())
                .intro(regiReq.getIntro())
                .birthday(regiReq.getBirthday())
                .avatar(regiReq.getAvatar()).build();
        log.info("Registering user with ID: {}", dto.getUserId());
        UserProfile userProfile = userProfileMapper.UserProfileDtoToUserProfile(dto);

        userProfileRepository.save(userProfile);

        RegiResp regiResp = new RegiResp();
        regiResp.setStatus(200L);
        return regiResp;
    }

    @Override
    public UserDto getUser(String userId) {
        Optional<UserProfile> userProfile = userProfileRepository.findByUserId(userId);
        UserProfileDto userProfileDto = userProfile.map(userProfileMapper::UserProfileToUserProfileDto).orElseThrow(null);
        UserDto userDto = new UserDto();
        userDto.setUserId(userProfileDto.getUserId());
        userDto.setNickName(userProfileDto.getNickname());
        return userDto;
    }
}
