package com.topseeker.shop.order.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
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

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.order.model.OrderService;
import com.topseeker.shop.order.model.OrderVO;
import com.topseeker.shop.sale.model.SaleService;
import com.topseeker.shop.sale.model.SaleVO;

@Controller
@Validated
@RequestMapping("/shop/order")
public class OrdernoController {
	
	@Autowired
	OrderService orderSvc;
	@Autowired
	SaleService saleSvc;
	@Autowired 
	MemberService memSvc;
	
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
			 @Valid
			/***************************1.接收請求參數 - 輸入格式的錯誤處理*************************/
			@NotEmpty(message="請輸入訂單編號")
			@Digits(fraction = 0, message = "訂單編號有誤", integer = 2)
			@Min(value=1, message = "訂單編號不能小於{value}")
			@RequestParam("orderNo") String orderNo,
			ModelMap model) {
			
		/***************************2.開始查詢資料*********************************************/
			OrderVO orderVO = orderSvc.getOneOrder(Integer.valueOf(orderNo));
			
			List<OrderVO> list = orderSvc.getAll();
			model.addAttribute("orderListData", list); 
			model.addAttribute("saleVO", new SaleVO());
			List<SaleVO> list2 = saleSvc.getAll();
			model.addAttribute("saleListData", list2);
			List<MemberVO> list3 = memSvc.getAll();
			model.addAttribute("memListData", list3);
			
			if (orderVO == null) {
				model.addAttribute("errorMessage", "查無資料");
				return "back-end/shop/order/select_page";
			}
		/***************************3.查詢完成,準備轉交(Send the Success view)*****************/
			model.addAttribute("orderVO", orderVO);
			model.addAttribute("getOne_For_Display", "true"); 
			return "back-end/shop/order/listOneOrder"; 
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下是當前面返回 /src/main/resources/templates/back-end/shop/select_page.html用的 ====   

		List<OrderVO> list = orderSvc.getAll();
		model.addAttribute("orderListData", list);    
		model.addAttribute("saleVO", new SaleVO());
		List<SaleVO> list2 = saleSvc.getAll();
		model.addAttribute("saleListData", list2);
		model.addAttribute("memberVO", new MemberVO());
		List<MemberVO> list3 = memSvc.getAll();
		model.addAttribute("memListData", list3);
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/shop/order/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}
