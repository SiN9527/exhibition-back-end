package com.exhibition.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    UserAccountMapper instance = Mappers.getMapper(UserAccountMapper.class);

    UserAccountDto UserAccountToUserAccountDto(UserAccount userAccount);

    UserAccount UserAccountDtoToUserAccount(UserAccountDto dto);
}
