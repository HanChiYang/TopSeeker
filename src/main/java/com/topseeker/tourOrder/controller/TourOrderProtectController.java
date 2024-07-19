package com.topseeker.tourOrder.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.sql.Date;
import java.time.LocalDate;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.tourGroup.model.TourGroupService;
import com.topseeker.tourGroup.model.TourGroupVO;
import com.topseeker.tourOrder.model.TourOrderService;
import com.topseeker.tourOrder.model.TourOrderVO;

@Controller
@RequestMapping("/protected/tourOrder")
public class TourOrderProtectController {

	@Autowired
	TourOrderService tourOrderSvc;
	
	@Autowired
	MemberService memberSvc;
	
	@Autowired
	TourGroupService tourGroupSvc;
	
	@GetMapping("addOrder")
	public String addOrder(ModelMap model) {
		TourOrderVO tourOrderVO = new TourOrderVO();
		model.addAttribute("tourOrderVO", tourOrderVO);
		return "front-end/tourOrder/addOrder";
	}
	
	@GetMapping("/confirm")
	public String showConfirmOrderForm(Model model) {
	    TourOrderVO tourOrderVO = new TourOrderVO(); // 这里创建或从数据库加载你的订单对象
	    model.addAttribute("tourOrderVO", tourOrderVO);
	    return "front-end/tourOrder/confirmOrder"; // 确认订单页面的视图名
	}
	
	@GetMapping("/historical_Order")
	public String showHistoricalOrders(HttpSession session, Model model) {
	    // 获取会话中的 loggedInMember
	    MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

	    // 检查 loggedInMember 是否为空
	     // 获取会员编号
	    Integer loggedInMemberNo = loggedInMember.getMemNo();
	    
	    // 获取历史订单列表
	    List<TourOrderVO> list = tourOrderSvc.getHistoricalOrders(loggedInMemberNo);

	    // 添加订单列表数据到模型
	    model.addAttribute("tourOrderListData", list);

	    // 返回到历史订单页面
	    return "front-end/tourOrder/historical_Order";
	}
	
	
	
	@PostMapping("/orderDetails")
    public String getOrderDetail(HttpSession session, @RequestParam String orderNo ,Model model) {
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

	    // 检查 loggedInMember 是否为空
	     // 获取会员编号
	    Integer loggedInMemberNo = loggedInMember.getMemNo();
		
		TourOrderVO tourOrderVO = tourOrderSvc.getOneOrder(Integer.valueOf(orderNo));// 这里创建或从数据库加载你的订单对象
		
		 if (tourOrderVO != null && tourOrderVO.getMemberVO().getMemNo().equals(loggedInMemberNo)) {
	            model.addAttribute("tourOrderVO", tourOrderVO);
	            return "front-end/tourOrder/orderDetails";
	        }
		 else {
			 return "front-end/tourOrder/historical_Order";
		 }
//	    model.addAttribute("tourOrderVO", tourOrderVO);
//	    return "front-end/tourOrder/orderDetails"; // 确认订单
         // 返回訂單詳情頁面
    }
	
	
	
	@PostMapping("/historical_Order")
	public String filterHistoricalOrders(HttpSession session, Model model, @RequestParam(value = "filter", defaultValue = "all") String filter) throws IOException {
	    // 获取会话中的 loggedInMember
	    MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

	    // 检查 loggedInMember 是否为空
	    if (loggedInMember == null) {
	        // 用户未登录，重定向到登录页面或显示错误信息
	        return "back-end/tourOrder/loginMem"; // 或者适当的错误页面
	    }

	    // 获取会员编号
	    Integer loggedInMemberNo = loggedInMember.getMemNo();
	    
	    List<TourOrderVO> list;
	    switch (filter) {
	        case "week":
	            list = tourOrderSvc.getOrdersWithinDays(loggedInMemberNo, 7);
	            break;
	        case "month":
	            list = tourOrderSvc.getOrdersWithinDays(loggedInMemberNo, 30);
	            break;
	        case "threeMonths":
	            list = tourOrderSvc.getOrdersWithinDays(loggedInMemberNo, 90);
	            break;
	        case "sixMonths":
	            list = tourOrderSvc.getOrdersWithinDays(loggedInMemberNo, 180);
	            break;
	        default:
	            list = tourOrderSvc.getHistoricalOrders(loggedInMemberNo);
	            break;
	    }

	    // 添加订单列表数据和成功消息到模型
	    model.addAttribute("tourOrderListData", list);
	    model.addAttribute("success", "- (新增成功)");

	    // 返回到历史订单页面
	    return "front-end/tourOrder/historical_Order";
	}
	
	@PostMapping("insert")
	public String insert(@Valid TourOrderVO tourOrderVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		if (result.hasErrors()) {
			return "front-end/tourOrder/addOrder";
		}
		
		int pricePerPerson = 16888; // 每人的價格，例如1000元
		int totalPrice = tourOrderVO.getOrderNums() * pricePerPerson;
		tourOrderVO.setOrderPrice(totalPrice);
		
		if(tourOrderVO.getOrderPay()== 1 ) {
			tourOrderVO.setOrderStatus((byte)1);
		}else {
			tourOrderVO.setOrderStatus((byte)2);
		}
		
		
		Date currentDate = Date.valueOf(LocalDate.now());
		tourOrderVO.setOrderDate(currentDate);
		
		if (tourOrderVO.getDepartureDate().before(currentDate)) {
	        result.rejectValue("departureDate", "error.tourOrderVO", "出發日期必須在訂單日期之後");
	        return "front-end/tourOrder/addOrder";
	    }
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		tourOrderSvc.addOrder(tourOrderVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<TourOrderVO> list = tourOrderSvc.getAll();
		model.addAttribute("tourOrderListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "front-end/tourOrder/confirmOrder"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}
	
	
	
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listOrders_ByCompositeQuery")
	public String listAllOrder(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<TourOrderVO> list = tourOrderSvc.getAll(map);
		model.addAttribute("tourOrderListData", list); // for listAllOrder.html 第85行用
		return "back-end/tourOrder/listAllOrder";
	}
}
