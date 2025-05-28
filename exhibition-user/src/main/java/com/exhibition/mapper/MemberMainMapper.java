package com.exhibition.mapper;


import com.exhibition.dto.auth.MemberMainEntityDto;
import com.exhibition.entity.member.MemberMainEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MemberMainMapper {

    MemberMainMapper instance = Mappers.getMapper(MemberMainMapper.class);

    MemberMainEntityDto MemberMainEntityToMemberMainEntityDto(MemberMainEntity entity);

    MemberMainEntity MemberMainEntityDtoToMemberMainEntity(MemberMainEntityDto dto);
}
