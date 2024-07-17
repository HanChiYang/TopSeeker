package com.topseeker.tourCol.contorller;

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

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourCol.model.TourColService;
import com.topseeker.tourCol.model.TourColVO;


@Controller
@Validated
@RequestMapping("/tourCol")
public class TourColNoController {
	
	@Autowired
	TourColService tourColSvc;
	
	@Autowired
	TourService tourSvc;
	
	@Autowired
	MemberService memberSvc;

	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		
		@NotNull(message="會員編號: 請勿空白")
        @Digits(integer = 5, fraction = 0, message = "會員編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1000, message = "會員編號: 不能小於{value}")
		@RequestParam("memNo") String memNo,	
			
			
		@NotNull(message="行程編號: 請勿空白")
        @Digits(integer = 4, fraction = 0, message = "行程編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "行程編號: 不能小於{value}")
		@RequestParam("tourNo") String tourNo,		
			
		
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		tourColService tourColSvc = new tourColService();
		
		TourVO tourVO = tourSvc.getOneTour(Integer.parseInt(tourNo));
		MemberVO memberVO = memberSvc.getOneMem(Integer.parseInt(memNo));
	    TourColVO tourColVO = tourColSvc.getByMemberVOAndTourVo(memberVO, tourVO);

		
		
		List<TourColVO> list = tourColSvc.getAll();
		model.addAttribute("tourColListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("memberColListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourVO", new TourVO());  // for select_page.html 第133行用
		model.addAttribute("memberVO", new MemberVO());  // for select_page.html 第133行用
		
		List<TourVO> list2 = tourSvc.getAll();
		
    	model.addAttribute("tourListData",list2);    // for select_page.html 第135行用
    	
    	List<MemberVO> list3 = memberSvc.getAll();
    	model.addAttribute("memberListData",list3);    // for select_page.html 第135行用
		
		if (tourColVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/tourCol/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("tourColVO", tourColVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/tourCol/listOnetourCol";  // 查詢完成後轉交listOnetourCol.html
		return "back-end/tourCol/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOnetourCol.html內的th:fragment="listOnetourCol-div
	}
	
	
	
	
//	=============登入 ===============
//	@PostMapping("getOne_For_MemberDisplay")
//	public String getOne_For_MemberDisplay(
//			/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
//			
//			@NotNull(message="會員編號: 請勿空白")
//	        @Digits(integer = 5, fraction = 0, message = "會員編號: 請填數字-請勿超過{integer}位數")
//			@Min(value = 1000, message = "會員編號: 不能小於{value}")
//			@RequestParam("memNo") String memNo,	
//				
//				
//			
//			ModelMap model) {
//			
//			/***************************2.開始查詢資料*********************************************/
////			tourColService tourColSvc = new tourColService();
//			System.out.print("abc"+memNo);
//			MemberVO memberVO = memberSvc.getOneMem(Integer.parseInt(memNo));
//		    List<TourColVO> tourColVO = tourColSvc.getByMemberVO(memberVO);
//
//			
//			
//			List<TourColVO> list = tourColSvc.getAll();
//			model.addAttribute("tourColListData", list);     // for select_page.html 第97 109行用
//			model.addAttribute("memberColListData", list);     // for select_page.html 第97 109行用
//			model.addAttribute("memberVO", new MemberVO());  // for select_page.html 第133行用
//			
//	    	
//	    	List<MemberVO> list3 = memberSvc.getAll();
//	    	model.addAttribute("memberListData",list3);    // for select_page.html 第135行用
//			
//			if (tourColVO == null) {
//				model.addAttribute("errorMessage", "查無資料");
//				return "back-end/tourCol/select_page";
//			}
//			
//			/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
//			model.addAttribute("tourColVO", tourColVO);
//			model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
//			
////			return "back-end/tourCol/listOnetourCol";  // 查詢完成後轉交listOnetourCol.html
//			return "back-end/tourCol/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOnetourCol.html內的th:fragment="listOnetourCol-div
//		}
	
	
	

	
	
	
	

    
    
    
    
    
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/ttourCollates/back-end/tourCol/select_page.html用的 ====   
//	    model.addAttribute("tourColVO", new tourColVO());
//    	tourColService tourColSvc = new tourColService();
		List<TourColVO> list = tourColSvc.getAll();
		model.addAttribute("tourColListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("memberColListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourVO", new TourVO());  // for select_page.html 第133行用
		model.addAttribute("memberVO", new MemberVO());  // for select_page.html 第133行用
		List<TourVO> list2 = tourSvc.getAll();
    	model.addAttribute("tourListData",list2);    // for select_page.html 第135行用
    	model.addAttribute("memberListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/tourCol/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}