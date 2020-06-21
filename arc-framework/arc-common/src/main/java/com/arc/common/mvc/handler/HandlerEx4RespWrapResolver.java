package com.arc.common.mvc.handler;

import com.arc.common.ServiceException;
import com.arc.util.http.BaseResponse;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class HandlerEx4RespWrapResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        if (MethodArgumentNotValidException.class.isInstance(ex)) {
            this.handleError((MethodArgumentNotValidException) ex);
        }

        if (ServiceException.class.isInstance(ex)) {
            throw (ServiceException) ex;
        } else {
            ServiceException error = new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, "系统错误, 请稍后再试!");
            error.initCause(ex);
            throw error;
        }
    }

    private void handleError(MethodArgumentNotValidException error) throws ServiceException {
        if (error.getBindingResult().hasErrors()) {
            List<ObjectError> errors = error.getBindingResult().getAllErrors();
            StringBuilder ber = new StringBuilder();
            for (int i = 0; errors != null && i < errors.size(); i++) {
                ObjectError err = errors.get(i);
                ber.append(i == 0 ? "" : "; ");
                ber.append(err.getDefaultMessage());
            }
            throw new ServiceException(BaseResponse.STATUS_SYSTEM_FAILURE, ber.toString());
        }
    }

}
