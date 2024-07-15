package com.topseeker.act.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.artcomment.model.ArtCommentVO;
import com.topseeker.message.model.MessageService;
import com.topseeker.message.model.MessageVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;



@Controller
@Validated
@RequestMapping("/act")
public class ActnoController {
	
	@Autowired
	ActService actSvc;
	
	@Autowired
	MemberService memberSvc;

	@Autowired
	MessageService messageSvc;
	
	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="活動編號: 請勿空白")
		@Digits(integer = 4, fraction = 0, message = "活動編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "活動編號: 不能小於{value}")
		@Max(value = 9999, message = "活動編號: 不能超過{value}")
		@RequestParam("actNo") String actNo,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		ActVO actVO = actSvc.getOneAct(Integer.valueOf(actNo));
		
		List<ActVO> list = actSvc.getAll();
		model.addAttribute("actListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("memberVO", new MemberVO());  // for select_page.html 第133行用
		List<MemberVO> list2 = memberSvc.getAll();
    	model.addAttribute("memberListData",list2);    // for select_page.html 第135行用
		
		if (actVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "front-end/act/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("actVO", actVO);
		
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/emp/listOneEmp";  // 查詢完成後轉交listOneEmp.html
		return "front-end/act/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/emp/select_page.html用的 ====   

		List<ActVO> list = actSvc.getAll();
		model.addAttribute("actListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("memberVO", new MemberVO());  // for select_page.html 第133行用
		List<MemberVO> list2 = memberSvc.getAll();
    	model.addAttribute("memberListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("front-end/act/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	//活動詳情
	@PostMapping("details")
    public String getActDetails(@RequestParam("actNo") String actNo, Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		
        ActVO actVO = actSvc.getOneAct(Integer.valueOf(actNo));
        model.addAttribute("actVO", actVO);
        model.addAttribute("messageVO", new MessageVO());
        return "front-end/act/listOneAct";
    }
	
	//新增活動留言
		@PostMapping("/addMsg")
	    public String addMsg(@ModelAttribute MessageVO messageVO, 
	    		@RequestParam("actNo") Integer actNo, BindingResult result, 
	    		Model model, HttpServletRequest request) {
	        if (result.hasErrors()) {
	            return "front-end/act/listOneAct";
	        }       
	        
	        HttpSession session = request.getSession();
	        MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

	        if (loggedInMember != null) {
	            messageVO.setMemberVO(loggedInMember);
	        } else {
	            model.addAttribute("errorMessage", "需要先登入才能留言");
	            return "front-end/member/loginMem";
	        }

	        ActVO actVO = actSvc.getOneAct(actNo);
	        messageVO.setActVO(actVO);

	        messageSvc.addMessage(messageVO);
//	        return "redirect:/act/getOne_For_Display?actNo=" + actNo;
	        return "redirect:/act/listOneAct";
	    }
	
}