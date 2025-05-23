package com.exhibition.service.base;

import com.exhibition.dto.auth.SwaggerUserLoginRequest;
import com.exhibition.dto.common.CommonCodeList;


import java.util.List;

public interface BaseService {


    List<CommonCodeList> searchByCodeTypes(List<String> codeTypes);


}
