package com.example.demo.controller.handler;

import com.example.demo.dto.ErrorDTO;
import com.example.demo.error.DemoException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.constant.DemoResponseConst.ReturnCode.E9999;

@ControllerAdvice
@RestController
public class ErrorHandlerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlerController.class);

    @ExceptionHandler(DemoException.class)
    public ResponseEntity handleDemoException(DemoException e){
        LOGGER.info("DemoException occurred, Error code : {}, Error message: {}", e.getReturnCode(), e.getMessage());
        LOGGER.debug("DemoException occurred, Error : {}",
                ToStringBuilder.reflectionToString(e, ToStringStyle.JSON_STYLE));
        return returnErrorResponse(e.getReturnCode(), e.getExtInfo(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception e){
        LOGGER.error("Exception occurred, Error stack: ", e);
        return returnErrorResponse(E9999.name(), "", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorDTO> returnErrorResponse(String errorCode, String msg, HttpStatus httpStatus) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setReturnCode(errorCode);
        errorDTO.setReturnMsg(msg);

        return ResponseEntity.status(httpStatus)
                .body(errorDTO);
    }
}
