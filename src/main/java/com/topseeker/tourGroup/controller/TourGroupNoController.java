package com.topseeker.tourGroup.controller;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourGroup.model.TourGroupService;
import com.topseeker.tourGroup.model.TourGroupVO;


@Controller
@Validated
@RequestMapping("/tourGroup")
public class TourGroupNoController {
	
	@Autowired
	TourGroupService tourGroupSvc;
	
	@Autowired
	TourService tourSvc;

	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="行程開團編號: 請勿空白")
		@Digits(integer = 4, fraction = 0, message = "行程開團編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "行程開團編號: 不能小於{value}")
		@Max(value = 9999, message = "行程開團編號: 不能超過{value}")
		@RequestParam("groupNo") String groupNo,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		tourGroupService tourGroupSvc = new tourGroupService();
		TourGroupVO tourGroupVO = tourGroupSvc.getOneTourGroup(Integer.valueOf(groupNo));
		
		List<TourGroupVO> list = tourGroupSvc.getAll();
		model.addAttribute("tourGroupListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourVO", new TourVO());  // for select_page.html 第133行用
		List<TourVO> list2 = tourSvc.getAll();
    	model.addAttribute("tourListData",list2);    // for select_page.html 第135行用
		
		if (tourGroupVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/tourGroup/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("tourGroupVO", tourGroupVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/tourGroup/listOnetourGroup";  // 查詢完成後轉交listOnetourGroup.html
		return "back-end/tourGroup/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOnetourGroup.html內的th:fragment="listOnetourGroup-div
	}
	
	@PostMapping("/tourGroups123")
    public String getTourGroupsByTourNo(@RequestParam("tourNo") Integer tourNo, Model model) {
        List<TourGroupVO> tourGroups = tourGroupSvc.findGroupsByTourNo(tourNo);
        model.addAttribute("tourGroups", tourGroups);
        return "test2"; // 返回至相應的HTML模板，例如tourGroupsList.html
    }
	

	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/ttourGrouplates/back-end/tourGroup/select_page.html用的 ====   
//	    model.addAttribute("tourGroupVO", new tourGroupVO());
//    	tourGroupService tourGroupSvc = new tourGroupService();
		List<TourGroupVO> list = tourGroupSvc.getAll();
		model.addAttribute("tourGroupListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourVO", new TourVO());  // for select_page.html 第133行用
		List<TourVO> list2 = tourSvc.getAll();
    	model.addAttribute("tourListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/tourGroup/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}