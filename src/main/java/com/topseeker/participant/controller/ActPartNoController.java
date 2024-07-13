package com.topseeker.participant.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;

import com.topseeker.member.model.MemberService;
import com.topseeker.participant.model.ParticipantService;
import com.topseeker.participant.model.ParticipantVO;

@Controller
@Validated
@RequestMapping("/participant")
public class ActPartNoController {
	
	

	@Autowired
	ParticipantService participantSvc;
	
	@Autowired
	ActService actSvc;
	
	@Autowired
	MemberService memberSvc;

	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="參團活動編號: 請勿空白")
		@Digits(integer = 4, fraction = 0, message = "員工編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "參團活動編號: 不能小於{value}")
		@Max(value = 7777, message = "參團活動編號: 不能超過{value}")
		@RequestParam("actPartNo") String actPartNo,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		ParticipantVO participantVO = participantSvc.getOneParticipant(Integer.valueOf(actPartNo));
		
		List<ParticipantVO> list = participantSvc.getAll();
		model.addAttribute("participantListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("actVO", new ActVO());  // for select_page.html 第133行用
		List<ActVO> list2 = actSvc.getAll();
    	model.addAttribute("actListData",list2);    // for select_page.html 第135行用
		
		if (participantVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "front-end/participant/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("participantVO", participantVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "front-end/emp/listOneEmp";  // 查詢完成後轉交listOneEmp.html
		return "front-end/participant/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
	}

	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/front-end/emp/select_page.html用的 ====   
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
		List<ParticipantVO> list = participantSvc.getAll();
		model.addAttribute("participantListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("actVO", new ActVO());  // for select_page.html 第133行用
		List<ActVO> list2 = actSvc.getAll();
    	model.addAttribute("actListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("front-end/participant/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}

}
