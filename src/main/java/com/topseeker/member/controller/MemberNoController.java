package com.topseeker.member.controller;

import javax.servlet.http.HttpServletRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;

import java.util.*;


@Controller
@Validated
@RequestMapping("/member")
public class MemberNoController {
	
	@Autowired
	MemberService memSvc;
	
	/*
	 * This method will be called on select_page.jsp form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="會員編號: 請勿空白")
//		@Digits(integer = 4, fraction = 0, message = "會員編號: 請填數字-請勿超過{integer}位數")
//		@Min(value = 7001, message = "會員編號: 不能小於{value}")
//		@Max(value = 7777, message = "會員編號: 不能超過{value}")
		@RequestParam("memNo") String memNo,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		MemberVO memberVO = memSvc.getOneMem(Integer.valueOf(memNo));
		
		List<MemberVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);     // for select_page.jsp 第96 108行用
		model.addAttribute("memberVO", new MemberVO());    // for select_page.jsp 第94 106行用
		if (memberVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/member/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("memberVO", memberVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.jsp的第155行 -->
		
//		return "back-end/emp/listOneEmp";  // 查詢完成後轉交listOneEmp.jsp
		return "back-end/member/select_page"; // 查詢完成後轉交select_page.jsp由其第157行include file="listOneEmp-div-fragment.file"
	}

	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.jsp用的 ====   
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
		List<MemberVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);  // for select_page.jsp 第96 108行用
		model.addAttribute("memberVO", new MemberVO()); // for select_page.jsp 第94 106行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/member/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}