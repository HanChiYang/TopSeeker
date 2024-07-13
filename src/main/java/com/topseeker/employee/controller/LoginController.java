//package com.topseeker.employee.controller;
//
//import java.util.Optional;
//
//import javax.servlet.http.HttpSession;
//
//import org.apache.groovy.parser.antlr4.util.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.topseeker.employee.model.EmployeeVO;
//import com.topseeker.employee.model.EmployeeService;
//
//@Controller
//@RequestMapping("/emp")
//public class LoginController {
//	
//	
//	@Autowired
//	EmployeeService empSvc;
//
//	 @GetMapping("/login")
//	public String showLoginPage() {
//        return "login";  // 返回 login.html 模板
//    }
//	
//	  @PostMapping("/login")
//	    public String login(
//	    		@RequestParam("empAccount") String empAccount,
//	    		@RequestParam("empPassword") String empPassword,
//	    		Model model, HttpSession session) {
//		 	
//		 
//
//			/*************************** 2.開始查詢資料 ***************************************/
//			Optional<EmployeeVO> employeeOpt = empSvc.empLogin(empAccount, empPassword);
//
//			/*************************** 3.查詢成功，登入 **************************/
//			if (employeeOpt.isPresent()) {
//				EmployeeVO employee = employeeOpt.get();
//				if (employee.getEmpStatus() == 0) {
//					model.addAttribute("loginError", "您已被停權");
//					return "back-end/employee/login";// 若被停權，無法登入
//				}
//				session.setAttribute("loggedInEmployee", employee); // 將會員信息保存到 session
//				return "back-end/back_end_index"; // 重導至欲前往的頁面
//
//			} else {
//				model.addAttribute("loginError", "無效的帳號或密碼");
//				return "back-end/employee/login"; // 登入失敗，返回登入頁面並顯示錯誤信息
//			}
//		 	
//		 
//	        
//	    }
//
////	    @RequestMapping("/")
////	    public String selectPage() {
////	        return "select_page"; // 登入成功後的跳轉頁面，假設為 select_page.html
////	    }
//}
