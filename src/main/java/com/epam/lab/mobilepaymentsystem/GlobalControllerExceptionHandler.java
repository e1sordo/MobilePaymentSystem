package com.epam.lab.mobilepaymentsystem;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@ControllerAdvice
class GlobalControllerExceptionHandler {

    private static final String DEFAULT_ERROR_VIEW = "errorhandler";
    // TODO: make a pretty page with error stuff and no stack trace

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        introduceErrors(e, mav);
        return mav;
    }

    private void introduceErrors(Exception e, ModelAndView mav) throws Exception {
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }

        String s = Arrays.deepToString(e.getStackTrace());
        mav.addObject("exception", e.toString());
        mav.addObject("stackTrace", s);
        mav.addObject("message", e.getMessage());
        mav.setViewName(DEFAULT_ERROR_VIEW);
    }
}
