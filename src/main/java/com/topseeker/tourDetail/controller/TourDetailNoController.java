package com.topseeker.tourDetail.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourDetail.model.TourDetailService;
import com.topseeker.tourDetail.model.TourDetailVO;


@Controller
@Validated
@RequestMapping("/tourDetail")
public class TourDetailNoController {
	
	@Autowired
	TourDetailService tourDetailSvc;
	
	@Autowired
	TourService tourSvc;

	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		
		@NotNull(message="行程編號: 請勿空白")
        @Digits(integer = 4, fraction = 0, message = "行程編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "行程編號: 不能小於{value}")
		@RequestParam("tourNo") String tourNo,	
			
			
			
			
		@NotNull(message="第幾天: 請勿空白")
		@Digits(integer = 4, fraction = 0, message = "第幾天: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "第幾天: 不能小於{value}")
		@RequestParam("detailDay") String detailDay,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		tourDetailService tourDetailSvc = new tourDetailService();
		
		TourVO tourVO = tourSvc.getOneTour(Integer.parseInt(tourNo));
		System.out.print("==============" + tourVO.toString());
	    TourDetailVO tourDetailVO = tourDetailSvc.getByTourNoAndDetailDay(tourVO, Integer.valueOf(detailDay));

		
		List<TourDetailVO> list = tourDetailSvc.getAll();
		model.addAttribute("tourDetailListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourVO", new TourVO());  // for select_page.html 第133行用
		List<TourVO> list2 = tourSvc.getAll();
    	model.addAttribute("tourListData",list2);    // for select_page.html 第135行用
		
		if (tourDetailVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/tourDetail/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("tourDetailVO", tourDetailVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/tourDetail/listOnetourDetail";  // 查詢完成後轉交listOnetourDetail.html
		return "back-end/tourDetail/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOnetourDetail.html內的th:fragment="listOnetourDetail-div
	}

	
	
	
	

//    @PostMapping("getTourNo_For_Display")
//    public String getOneForDisplay(@RequestParam("tourNo") Integer tourNo, ModelMap model) {
//    	
//    	if (tourNo == null) {
//            // 適當的錯誤處理或返回錯誤信息給前端
//            return "error_page";
//        }
//    	List<TourDetailVO> tourDetailList = tourDetailService.getByTourNo(tourNo);
//        model.addAttribute("tourDetailList", tourDetailList);
//        return "back-end/tourDetail/select_page";
//    }
    
    
    
    
    
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/ttourDetaillates/back-end/tourDetail/select_page.html用的 ====   
//	    model.addAttribute("tourDetailVO", new tourDetailVO());
//    	tourDetailService tourDetailSvc = new tourDetailService();
		List<TourDetailVO> list = tourDetailSvc.getAll();
		model.addAttribute("tourDetailListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourVO", new TourVO());  // for select_page.html 第133行用
		List<TourVO> list2 = tourSvc.getAll();
    	model.addAttribute("tourListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/tourDetail/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}