package com.exhibition.controller;

import com.exhibition.dto.ApiResponseTemplate;
import com.exhibition.dto.common.CommonCodeList;
import com.exhibition.dto.common.CommonCodeReqDTO;
import com.exhibition.service.base.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/base")
public class BaseController {


    private final BaseService baseService;



    //CommonCode 暫放
    @PostMapping("/getCommonCode")
    @Operation(summary = "CommonCode-根據codeType回傳資料，舉例['IDREC_LOCATION','INDUSTRY_CODE']")
    public ResponseEntity<ApiResponseTemplate<List<CommonCodeList>>> searchByCodeType(@RequestBody CommonCodeReqDTO reqDTO) {
        List<CommonCodeList> result = baseService.searchByCodeTypes(reqDTO.getCodeType());

        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(ApiResponseTemplate.success(result));
        }
        return ResponseEntity.notFound().build();
    }



}
