package com.bookstore.controller.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {
    
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("status", "404");
                model.addAttribute("error", "Not Found");
                model.addAttribute("message", "Страница не найдена");
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("status", "500");
                model.addAttribute("error", "Internal Server Error");
                model.addAttribute("message", "Внутренняя ошибка сервера");
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("status", "403");
                model.addAttribute("error", "Forbidden");
                model.addAttribute("message", "Доступ запрещен");
            } else {
                model.addAttribute("status", statusCode);
                model.addAttribute("error", "Error");
                model.addAttribute("message", "Произошла ошибка");
            }
        } else {
            model.addAttribute("status", "500");
            model.addAttribute("error", "Internal Server Error");
            model.addAttribute("message", "Произошла непредвиденная ошибка");
        }
        
        return "error";
    }
}


