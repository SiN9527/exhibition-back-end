package com.exhibition.mapper;

import com.hititoff.dto.user.UserProfileDto;
import com.hititoff.dto.user.UserSportProfileDto;
import com.hititoff.entity.UserProfile;
import com.hititoff.entity.UserSportProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    UserProfileMapper instance = Mappers.getMapper(UserProfileMapper.class);


    UserProfileDto UserProfileToUserProfileDto(UserProfile userProfile);



    UserProfile UserProfileDtoToUserProfile(UserProfileDto dto);


    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    void updateEntityFromDto(UserProfileDto dto, @MappingTarget UserProfile entity);
}
