package com.topseeker.artreport.controller;

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

import com.topseeker.artreport.model.ArtReportService;
import com.topseeker.artreport.model.ArtReportVO;
import com.topseeker.employee.model.EmployeeService;
import com.topseeker.article.model.ArticleService;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;

@Controller
@Validated
@RequestMapping("/artreport")
public class ArtReportNoController {
	
	@Autowired
	ArtReportService artreportSvc;
	
	
	@Autowired
	ArticleService articleSvc;
	
	@Autowired
	MemberService memberSvc;
	
	@Autowired
	EmployeeService employeeSvc;
	
	


	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="登山情報編號: 請勿空白")
		@Digits(integer = 4, fraction = 0, message = "登山情報編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "登山情報編號: 不能小於{value}")
		@Max(value = 7777, message = "登山情報編號: 不能超過{value}")
		@RequestParam("artReportNo") String artReportNo,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		EmpService empSvc = new EmpService();
		ArtReportVO artreportVO = artreportSvc.getOneArtReport(Integer.valueOf(artReportNo));
		
		List<ArtReportVO> list = artreportSvc.getAll();
		model.addAttribute("artreportListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("memberVO", new MemberVO());  // for select_page.html 第133行用
		List<MemberVO> list2 = memberSvc.getAll();
    	model.addAttribute("memberListData",list2);    // for select_page.html 第135行用
		
		if (artreportVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/artreport/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("artreportVO", artreportVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/emp/listOneEmp";  // 查詢完成後轉交listOneEmp.html
		return "back-end/artreport/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneEmp.html內的th:fragment="listOneEmp-div
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
//	    model.addAttribute("empVO", new EmpVO());
//    	EmpService empSvc = new EmpService();
		List<ArtReportVO> list = artreportSvc.getAll();
		model.addAttribute("artreportListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("memberVO", new MemberVO());  // for select_page.html 第133行用
		List<MemberVO> list2 = memberSvc.getAll();
    	model.addAttribute("memberListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/artreport/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}


}
