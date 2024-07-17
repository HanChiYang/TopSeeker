//package com.topseeker.security.controller;
//
//import javax.servlet.http.HttpServletRequest;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import com.topseeker.employee.model.EmployeeRepository;
//import com.topseeker.employee.model.EmployeeVO;
//
//@Controller
//public class LoginEmpController {
//
//    @Autowired
//    private EmployeeRepository empRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    // 登入
//    @GetMapping("/login")
//    public String login(@RequestParam(required = false) String error, @RequestParam(required = false) String logout,
//            HttpServletRequest request, Model model) {
//
//        // 判斷如果是登出，清除session
//        if ("true".equals(logout)) {
//            request.getSession().invalidate();
//            model.addAttribute("message", "您已成功登出。");
//        }
//
//        // 檢查是否有錯誤訊息
//        if (error != null) {
//            String loginError = "用戶名稱或密碼錯誤";
//            model.addAttribute("loginError", loginError);
//        }
//
//        return "back-end/employee/login"; // 返回自定義的登入頁面
//    }
//
//    
//    @GetMapping("/admin/dashboard")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String adminDashboard() {
//        return "back-end/back_end_index";
//    }
//
//    @GetMapping("/employee/profile")
//    @PreAuthorize("hasRole('EMPLOYEE')")
//    public String employeeProfile() {
//        return "back-end/employee/select_page";
//    }
//}
//
