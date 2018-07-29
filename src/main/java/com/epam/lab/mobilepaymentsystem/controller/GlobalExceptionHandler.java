package com.epam.lab.mobilepaymentsystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String DEFAULT_ERROR_VIEW = "errorhandler";
    private Logger logger = LoggerFactory.getLogger(ServiceUnitController.class);

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

        mav.addObject("exception", e.toString());
        mav.addObject("message", e.getMessage());
        mav.setViewName(DEFAULT_ERROR_VIEW);
        e.printStackTrace();
        logger.error("There is an error: " + e.toString() + " Default message: " + e.getMessage(), e);
    }
}
