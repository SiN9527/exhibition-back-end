package com.exhibition.enumerate;

public enum ProcessReturnStatus {
    TRY_LATER,
    WARN, // 通常是進入function 的第一步檢查沒過 argValidateException
    PREVENT,
    SOFT_ABORT, // 狀態有復原
    SOFT_ERROR,  // 狀態不可復原

    // 專案中各layer所丟出的是非自訂義的Throwable，代表沒有好好去實作例外處理導致，需通報alert且後續要修復
    // 因為丟出的是不認識的Throwable也沒處理所以代表不確定是否真的有辦法恢復，因此不論實際情況如何catch非自訂義的皆視為可能不可恢復
    FATAL_ERROR,
    UNAUTHORIZED,  //未登入
    AUTHENTICATE_FAILED , //驗證失敗
    FORBIDDEN ,    //權限不足
    UNKNOWN, // 其他狀況
    CONTROLLER_FATAL_ERROR,
    CONTROLLER_ABORT,
    CONTROLLER_ERROR,

    ;
}
