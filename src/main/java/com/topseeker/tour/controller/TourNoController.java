package com.topseeker.tour.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.member.model.MemberVO;
import com.topseeker.tour.model.TourService;
import com.topseeker.tour.model.TourVO;
import com.topseeker.tourArea.model.TourAreaService;
import com.topseeker.tourArea.model.TourAreaVO;
import com.topseeker.tourDetail.model.TourDetailService;
import com.topseeker.tourDetail.model.TourDetailVO;
import com.topseeker.tourGroup.model.TourGroupService;
import com.topseeker.tourGroup.model.TourGroupVO;
import com.topseeker.tourOrder.model.TourOrderService;
import com.topseeker.tourOrder.model.TourOrderVO;


@Controller
@Validated
@RequestMapping("tour")
public class TourNoController {
	
	@Autowired
	TourService tourSvc;
	
	@Autowired
	TourAreaService tourAreaSvc;
	
	@Autowired
	TourGroupService tourGroupSvc;
	
	@Autowired
	TourDetailService tourDetailSvc; 
	
	@Autowired
	TourOrderService tourOrderSvc;

	/*
	 * This method will be called on select_page.html form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
		@NotEmpty(message="行程編號: 請勿空白")
		@Digits(integer = 3, fraction = 0, message = "行程編號: 請填數字-請勿超過{integer}位數")
		@Min(value = 1, message = "行程編號: 不能小於{value}")
		@Max(value = 999, message = "行程編號: 不能超過{value}")
		@RequestParam("tourNo") String tourNo,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		TourService tourSvc = new TourService();
		TourVO tourVO = tourSvc.getOneTour(Integer.valueOf(tourNo));
		
		
		List<TourVO> list = tourSvc.getAll();
		model.addAttribute("tourListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourAreaVO", new TourAreaVO());  // for select_page.html 第133行用
		List<TourAreaVO> list2 = tourAreaSvc.getAll();
    	model.addAttribute("tourAreaListData",list2);    // for select_page.html 第135行用
		

		List<TourGroupVO> list3 = tourGroupSvc.getAll();
		model.addAttribute("tourListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourAreaVO", new TourAreaVO());
    	
		if (tourVO == null) {
			model.addAttribute("errorMessage", "查無資料");
			return "back-end/tour/select_page";
		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("tourVO", tourVO);
		model.addAttribute("getOne_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/tour/listOneTour";  // 查詢完成後轉交listOneTour.html
		return "back-end/tour/select_page"; // 查詢完成後轉交select_page.html由其第158行insert listOneTour.html內的th:fragment="listOneTour-div
	}
	
	
	
	
	
	
	
//	============傳送tourNo顯示開團日期，顯示行程詳情==============
	@RequestMapping(value = "getTourNo_For_Display", method = {RequestMethod.GET, RequestMethod.POST})
//	@PostMapping("getTourNo_For_Display")
//	@GetMapping("getTourNo_For_Display")
	public String getTourNo_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
//		@NotEmpty(message="行程編號: 請勿空白")
//		@Digits(integer = 3, fraction = 0, message = "行程編號: 請填數字-請勿超過{integer}位數")
//		@Min(value = 1, message = "行程編號: 不能小於{value}")
//		@Max(value = 999, message = "行程編號: 不能超過{value}")
		@RequestParam("tourNo") String tourNo,
//		@RequestParam("collected") Boolean collected,
		
		HttpServletRequest req,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		TourService tourSvc = new TourService();
		TourVO tourVO = tourSvc.getOneTour(Integer.valueOf(tourNo));
		
		List<TourVO> list = tourSvc.getAll();
		model.addAttribute("tourListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourAreaVO", new TourAreaVO());  // for select_page.html 第133行用
		List<TourAreaVO> list2 = tourAreaSvc.getAll();
    	model.addAttribute("tourAreaListData",list2);    // for select_page.html 第135行用
		
    	Map<String, String[]> map = req.getParameterMap();
		List<TourGroupVO> list3 = tourGroupSvc.getAll(map);
		
		Map<String, String[]> map2 = req.getParameterMap();
//		List<TourDetailVO> list4 = tourDetailSvc.getAll(map2);
		List<TourVO> list4 = tourSvc.getAll(map2);

//		List<TourGroupVO> tourGroupVO = tourGroupSvc.findGroupsByTourNo(Integer.valueOf(tourNo));
//		List<TourGroupVO> list5 = tourGroupSvc.getAll();
//		model.addAttribute("tourGroupListData2", list5);
    	
		List<TourDetailVO> tourDetailVO = tourDetailSvc.findDetailsByTourNo(Integer.valueOf(tourNo));
		List<TourDetailVO> list6 = tourDetailSvc.findDetailsByTourNo(Integer.valueOf(tourNo));
		model.addAttribute("tourDetailListData2", list6);
		
//		if (tourVO == null) {
//			model.addAttribute("errorMessage", "查無資料");
//			return "back-end/tour/select_page";
//		}
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("tourVO", tourVO);
//		model.addAttribute("tourGroupVO", tourGroupVO);
//		model.addAttribute("tourDetailVO", tourDetailVO);
		
		model.addAttribute("tourGroupListData", list3);
		model.addAttribute("tourDetailListData", list4);
		model.addAttribute("getTourNo_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/tour/listOneTour";  // 查詢完成後轉交listOneTour.html
		return "front-end/tour/tourDetail"; // 查詢完成後轉交select_page.html由其第158行insert listOneTour.html內的th:fragment="listOneTour-div
	}
	
	
//	============傳送GroupNo顯示開團日期價格等資訊==============

	@PostMapping("getGroupNo_For_Display")
	public String getGroupNo_For_Display(
		/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
//		@NotEmpty(message="行程編號: 請勿空白")
//		@Digits(integer = 3, fraction = 0, message = "行程編號: 請填數字-請勿超過{integer}位數")
//		@Min(value = 1, message = "行程編號: 不能小於{value}")
//		@Max(value = 999, message = "行程編號: 不能超過{value}")
		@RequestParam("groupNo") String groupNo,
		HttpServletRequest req,
		ModelMap model) {
		
		/***************************2.開始查詢資料*********************************************/
//		TourService tourSvc = new TourService();
		TourGroupVO tourGroupVO = tourGroupSvc.getOneTourGroup(Integer.valueOf(groupNo));
		
		List<TourGroupVO> list = tourGroupSvc.getAll();
		model.addAttribute("tourGroupListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourVO", new TourVO());  // for select_page.html 第133行用
		List<TourVO> list2 = tourSvc.getAll();
    	model.addAttribute("tourListData",list2);    // for select_page.html 第135行用
		
    	
		
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
		model.addAttribute("tourGroupVO", tourGroupVO);
//		model.addAttribute("tourGroupVO", tourGroupVO);
//		model.addAttribute("tourDetailVO", tourDetailVO);
		
		model.addAttribute("getTourGroupNo_For_Display", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
		
//		return "back-end/tour/listOneTour";  // 查詢完成後轉交listOneTour.html
		return "front-end/tour/testGroup"; // 查詢完成後轉交select_page.html由其第158行insert listOneTour.html內的th:fragment="listOneTour-div
	}
	
	
	
	// 抓取seesion內已登入會員的編號
//	MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
//	if (loggedInMember == null) {
//		// 如果未登入，重定向到登入頁面
//		return "redirect:/member/loginMem";
//	}
//
//	Integer loggedInMemberNo = loggedInMember.getMemNo();
//	List<TourOrderVO> tourOrderVO1 = tourOrderSvc.getHistoricalOrders(loggedInMemberNo);
//	model.addAttribute("tourOrderVO", tourOrderVO1);
	
	
	
	
	
	
	
	//tourGroup單筆查詢
	@PostMapping("/addOrder")
    public String addOrder(HttpSession session,@RequestParam("groupNo") Integer groupNo, Model model) {
        // 在這裡處理 groupNo，例如查詢相關的行程信息並添加到 model 中
  System.out.println(groupNo);
  
  
//  ==============登入================
//抓取seesion內已登入會員的編號
	MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
	if (loggedInMember == null) {
		// 如果未登入，重定向到登入頁面
		return "redirect:/member/loginMem";
	}

	Integer loggedInMemberNo = loggedInMember.getMemNo();
	List<TourOrderVO> tourOrderVO1 = tourOrderSvc.getHistoricalOrders(loggedInMemberNo);
	model.addAttribute("tourOrderVO", tourOrderVO1);
  
//	  TourOrderVO tourOrderVO = tourOrderSvc.getOneTourGroup(Integer.valueOf(groupNo));

  
  
  TourGroupVO tourGroupVO = tourGroupSvc.getOneTourGroup(Integer.valueOf(groupNo));

//  List<TourGroupVO> list = tourGroupSvc.getAll();
//  model.addAttribute("tourGroupListData", list);     // for select_page.html 第97 109行用
//  model.addAttribute("tourVO", new TourVO());  // for select_page.html 第133行用
//  List<TourVO> list2 = tourSvc.getAll();
//     model.addAttribute("tourListData",list2);    // for select_page.html 第135行用
  
  /***************************3.查詢完成,準備轉交(Send the Success view)*****************/
     
     
//     model.addAttribute("tourOrderVO", new TourOrderVO());
     model.addAttribute("tourGroupVO", tourGroupVO);
  model.addAttribute("addOrder", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
        model.addAttribute("groupNo", groupNo);
        return "front-end/tour/addOrder"; // 返回 addOrder 頁面
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//排序
//	=============================================
	
	@GetMapping("/sortedByPrice")
    public List<TourVO> getToursSortedByPrice() {
        return tourSvc.getAllToursSortedByPrice();
    }
	
	@GetMapping("/sorted")
    public List<TourVO> getToursSorted(@RequestParam String sortField, @RequestParam String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Sort sort = Sort.by(direction, sortField);
        return tourSvc.getAllToursSorted(sort);
    }
	
	
	
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	//@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下第92~96行是當前面第77行返回 /src/main/resources/templates/back-end/tour/select_page.html用的 ====   
//	    model.addAttribute("tourVO", new TourVO());
//    	TourService tourSvc = new TourService();
		List<TourVO> list = tourSvc.getAll();
		model.addAttribute("tourListData", list);     // for select_page.html 第97 109行用
		model.addAttribute("tourAreaVO", new TourAreaVO());  // for select_page.html 第133行用
		List<TourAreaVO> list2 = tourAreaSvc.getAll();
    	model.addAttribute("tourAreaListData",list2);    // for select_page.html 第135行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/tour/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}