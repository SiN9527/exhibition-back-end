package com.exhibition.enumerate;

public enum ServiceExceptionReason {
    // failed and forget exception
    IGNORE_AND_FORGET_EXCEPTION,
    DB_TRANSIENT_FAILED,
    DB_OPTIMISTIC_LOCK_FAILED,

    // logical prohibited exception
    BUSINESS_LOGICAL_PROHIBITED,

    // default of service recoverable and unrecoverable 黨你不知道用哪個表示時可以用這個
    DEFAULT_SERVICE_EXCEPTION,

    // service recoverable exception
    DEFAULT_RECOVERABLE_EXCEPTION,
    MICRO_SERVICE_CONNECTION_TIMEOUT,
    MICRO_SERVICE_UNAVAILABLE,
    MICRO_SERVICE_AUTH_FAILED,
    DB_CONNECTION_FAILED,
    DB_CUSTOM_QUERY_FAILED,
    DB_CUSTOM_UPDATE_FAILED,
    DB_CUSTOM_INSERT_FAILED,
    DB_CUSTOM_DELETE_FAILED,
    DB_UNEXPECTED_FAILED,
    DB_CONSTRAINT_VIOLATION,

    // service unrecoverable exception
    DEFAULT_UNRECOVERABLE_EXCEPTION,
    PROGRAMING_ERROR,
    BAD_SQL, DB_TX_COMMIT_FAILED,
}
