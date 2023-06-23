package com.hae.library.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorHandlerController implements ErrorController {
//     @Override
//     public String getErrorPath() {
//         return null;
//     }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        // error로 들어온 에러의 status를 불러온다 (ex:404,500,403...)

        if(status != null){
            int statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404";
            } else {
                return "error";
            }
        }

        return "error";
    }
}
