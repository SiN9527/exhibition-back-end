package com.exhibition.exception.handle;


import com.exhibition.exception.ArgumentValidationException;
import com.exhibition.exception.FailedAndForgetException;
import com.exhibition.exception.LogicalProhibitedException;
import com.exhibition.exception.payload.ProcessPayload;

public class UsecaseExcHandler<T> extends ExceptionHandleTemplate<T> {

    @Override
    public ProcessPayload<T> execute() {
        try {
            T res = super.runnable.run();
            return ProcessPayload.success(res);
        } catch (ArgumentValidationException ex) {
            return ProcessPayload.invalidArg("欄位輸入驗證錯誤", ex.getInvalidFields(), ex.getInvalidReasons(), ex);
        } catch (FailedAndForgetException ex) {
            return ProcessPayload.tryLater(ex.getBriefMessage() == null ? "請稍後嘗試" : ex.getBriefMessage(), ex);
        } catch (LogicalProhibitedException ex) {
            return ProcessPayload.prevent(ex.getBriefMessage() == null ? "非法操作" : ex.getBriefMessage(), ex);
        } catch (RuntimeException ex) {
            return ProcessPayload.unknownError(ex.getMessage() == null ? "系統錯誤請迅速通知客服人員" : ex.getMessage(),ex);
        }
    }

    protected UsecaseExcHandler(ThrowableRunnable<T> runnable) {
        super(runnable);
    }

    public static <T> UsecaseExcHandler<T> runFor(ThrowableRunnable<T> runnable) {
        return new UsecaseExcHandler<T>(runnable);
    }
}
