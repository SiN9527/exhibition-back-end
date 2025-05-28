package com.exhibition.service.base.impl;


import com.exhibition.dto.common.CommonCodeList;
import com.exhibition.entity.CommonCodeEntity;
import com.exhibition.repository.CommonCodeRepository;
import com.exhibition.service.base.CommonCodeService;
import com.exhibition.utils.MapperUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 郭庭安
 * @Create 2024/7/27 10:41 PM
 * @Version 1.0
 */
@Slf4j
@Service
public class CommonCodeServiceImpl implements CommonCodeService {

    @Resource
    private CommonCodeRepository commonCodeRepository;

    @Override
    public Map<String, List<CommonCodeList>> searchByCodeTypes(List<String> codeTypes) {
        Map<String, List<CommonCodeList>> result = new HashMap<>();

        for (String codeType : codeTypes) {
            List<CommonCodeEntity> byCodeTypes = commonCodeRepository.findByCodeType(codeType);
            List<CommonCodeList> commonCodeLists = MapperUtils.mapList(byCodeTypes, CommonCodeList.class);
            result.put(codeType, commonCodeLists);
        }

        return result;
    }

}
