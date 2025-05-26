package com.exhibition.service.base.impl;

import com.exhibition.dto.common.CommonCodeList;
import com.exhibition.entity.CommonCodeEntity;
import com.exhibition.repository.CommonCodeRepository;
import com.exhibition.service.base.BaseService;
import com.exhibition.utils.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class BaseServiceImpl implements BaseService {

    private final CommonCodeRepository commonCodeRepository;




    @Override
    public List<CommonCodeList> searchByCodeTypes(List<String> codeTypes) {
        List<CommonCodeList> resultList = new ArrayList<>();

        for (String codeType : codeTypes) {
            List<CommonCodeEntity> byCodeTypes = commonCodeRepository.findByCodeType(codeType);
            resultList.addAll(MapperUtils.mapList(byCodeTypes, CommonCodeList.class));
        }

        return resultList;
    }
}
