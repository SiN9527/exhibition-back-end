package com.exhibition.mapper;


import com.exhibition.dto.auth.AdminMainEntityDto;
import com.exhibition.entity.admin.AdminMainEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AdminMainMapper {

//    AdminMainMapper instance = Mappers.getMapper(AdminMainMapper.class);

    AdminMainEntityDto AdminMainEntityToAdminMainEntityDto(AdminMainEntity entity);

    AdminMainEntity AdminMainEntityDtoToAdminMainEntity(AdminMainEntityDto dto);
}
