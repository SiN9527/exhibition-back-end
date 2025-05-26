package com.exhibition.exception.handler;


import com.exhibition.exception.*;
import com.exhibition.exception.handle.ThrowableRunnable;
import com.exhibition.exception.handle.UsecaseExcHandler;
import com.exhibition.exception.payload.ProcessPayload;

public class AuthUsecaseExcHandler<T> extends UsecaseExcHandler<T> {

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
        } catch (ServiceRecoverableException ex) {
            return ProcessPayload.softAbort(ex.getBriefMessage() == null ? "操作失敗" : ex.getBriefMessage(), ex);
        } catch (ServiceUnRecoverableException ex) {
            return ProcessPayload.softError(ex.getBriefMessage() == null ? "系統錯誤請迅速通知客服人員" : ex.getBriefMessage(), ex);
        }  catch (AuthenticationFailedException ex) {
            return ProcessPayload.authenticateFail(ex.getMessage() == null ? "帳號登入失敗" : ex.getMessage(), ex);
        }   catch (AuthorizationFailedException ex) {
            return ProcessPayload.authorizedFail(ex.getMessage() == null ? "權限驗證失敗" : ex.getMessage(), ex);
        }    catch (UnauthenticatedException ex) {
            return ProcessPayload.unauthorized(ex.getMessage() == null ? "TOKEN失效或未驗證" : ex.getMessage(), ex);
        }
    }

    private AuthUsecaseExcHandler(ThrowableRunnable<T> runnable) {
        super(runnable);
    }

    public static <T> AuthUsecaseExcHandler<T> runFor(ThrowableRunnable<T> runnable) {
        return new AuthUsecaseExcHandler<T>(runnable);
    }
}
