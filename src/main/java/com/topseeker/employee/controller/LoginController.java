package com.topseeker.employee.controller;

import javax.servlet.http.HttpSession;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	 @GetMapping("/login")
	public String showLoginPage() {
        return "login";  // 返回 login.html 模板
    }
	
	 @RequestMapping("/login")
	    public String login(
	    		@RequestParam("username") String username,
	    		@RequestParam("password") String password,
	    		Model model, HttpSession session) {
		 	
		 	if(!StringUtils.isEmpty(username) && "1234".equals(password)) {
		 		session.setAttribute("loginUser", username);
		 		return "redirect:/emp/select_page";
		 	}
		 	else {
		 		model.addAttribute("error", "輸入錯誤，再輸入一次");
		 		return "login";    // 返回登入頁面的名稱，對應 select_page.html
		 	}
		 
		 
	        // 返回登入頁面的名稱，對應 select_page.html
	    }

//	    @RequestMapping("/")
//	    public String selectPage() {
//	        return "select_page"; // 登入成功後的跳轉頁面，假設為 select_page.html
//	    }
}
