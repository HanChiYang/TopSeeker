package com.topseeker.shop.sale.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.shop.sale.model.SaleService;
import com.topseeker.shop.sale.model.SaleVO;

@Controller
@Validated
@RequestMapping("/shop/sale")
public class SalenoController {
	
	@Autowired
	SaleService saleSvc;
	
	@PostMapping("getOne_For_Display")
	public String getOne_For_Display(
			@NotEmpty(message="請輸入方案編號")
			@Digits(fraction = 0, message = "方案編號有誤", integer = 2)
			@RequestParam("saleNo") String saleNo,
			ModelMap model) {
			
			SaleVO saleVO = saleSvc.getOneSale(Integer.valueOf(saleNo));
			
			List<SaleVO> list = saleSvc.getAll();
			model.addAttribute("saleListData", list);     
			
			if (saleVO == null) {
				model.addAttribute("errorMessage", "查無資料");
				return "back-end/shop/sale/select_page";
			}
			
			model.addAttribute("saleVO", saleVO);
			model.addAttribute("getOne_For_Display", "true"); 
			return "back-end/shop/sale/select_page"; 
	}
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下是當前面返回 /src/main/resources/templates/back-end/shop/select_page.html用的 ====   

		List<SaleVO> list = saleSvc.getAll();
		model.addAttribute("saleListData", list);     // for select_page.html 第97 109行用
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/shop/sale/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	
}
