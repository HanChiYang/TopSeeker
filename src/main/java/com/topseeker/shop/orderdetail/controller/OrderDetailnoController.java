package com.topseeker.shop.orderdetail.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
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

import com.topseeker.shop.orderdetail.model.CompositeDetail;
import com.topseeker.shop.orderdetail.model.OrderDetailService;
import com.topseeker.shop.orderdetail.model.OrderDetailVO;

@Controller
@Validated
@RequestMapping("/shop/orderDetail")
public class OrderDetailnoController {
	
	@Autowired
	OrderDetailService orderDetailSvc;
	
    public OrderDetailnoController(OrderDetailService orderDetailSvc) {
        this.orderDetailSvc = orderDetailSvc;
    }
	
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
			@NotEmpty(message="請輸入訂單編號")
			@RequestParam("orderNo") String orderNo,
			@NotEmpty(message="請輸入商品編號")
			@RequestParam("prodNo") String prodNo,
			ModelMap model) {
 
			CompositeDetail compositeDetail = new CompositeDetail(Integer.valueOf(orderNo), Integer.valueOf(prodNo));
			OrderDetailVO orderDetailVO = orderDetailSvc.getOneOrderDetail(compositeDetail);
			
			List<OrderDetailVO> list = orderDetailSvc.getAll();
			model.addAttribute("orderDetailListData", list);     
			
			
			model.addAttribute("orderDetailVO", orderDetailVO);
			model.addAttribute("getOne_For_Display", "true"); 
			return "back-end/shop/order/listOneOrderDetail"; 
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    } 

		List<OrderDetailVO> list = orderDetailSvc.getAll();
		model.addAttribute("orderDetailListData", list);     // for select_page.html 第97 109行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/shop/order/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}
