package com.exhibition.monitor;

import com.exhibition.monitor.event.ServiceExceptionAlertEvent;
import com.exhibition.monitor.event.StackTraceRecordEvent;
import com.exhibition.monitor.event.UnknowThrowableAlertEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class GlobalMonitorEventListener {

    private final AlertHandler alertHandler;

    private final StackTraceHandler stackTraceHandler;

    @Async
    @EventListener
    public void handleServiceException(ServiceExceptionAlertEvent event) {
        alertHandler.notifyDevOps();
        // 屬於這樣的異常屬於我們知道會發生，但我們用一些方法去追蹤
    }

    @Async
    @EventListener
    public void handleControllerUnknowThrowableException(UnknowThrowableAlertEvent event) {
        // 可能是程式沒寫好。因為噴出的throwable 並非自訂義的或是預期的，所以需特別紀錄下來。
        // 供未來修改程式
        alertHandler.alertHealthCheck();
        alertHandler.notifyDevOps();
    }

    @Async
    @EventListener
    public void handleStackTraceRecord(StackTraceRecordEvent event) {
        stackTraceHandler.recordStackTrace();
    }
}
