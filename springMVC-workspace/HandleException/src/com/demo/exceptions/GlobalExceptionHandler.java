package com.demo.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//모든 컨트롤러에 적용됨
@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

	@ExceptionHandler(Exception.class)
	public String handleException(Exception ex, Model model) {
		System.out.println("예외발생 : " + ex.getMessage());
		model.addAttribute("msg", "잠시후 다시 시도해주세요");
		return "error/error";
	}
}
