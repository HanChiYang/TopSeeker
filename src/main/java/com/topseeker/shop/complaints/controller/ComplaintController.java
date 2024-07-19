package com.topseeker.shop.complaints.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.topseeker.shop.complaints.model.ComplaintVO;
import com.topseeker.shop.order.model.OrderService;
import com.topseeker.shop.order.model.OrderVO;

@Controller
@RequestMapping("/shop/complaint")
public class ComplaintController {
	 	
		
	    private JavaMailSender mailSender;
	    
	    @Autowired
	    OrderService orderSvc;
	    
	    @Autowired
	    public ComplaintController(JavaMailSender mailSender) {
	        this.mailSender = mailSender;
	    }

	    @GetMapping("complaint")
	    public String showComplaintForm(
	    		@RequestParam("orderNo") Integer orderNo,
	    		Model model) {
	    	
	    	OrderVO orderVO = orderSvc.getOneOrder(orderNo);
	    	 ComplaintVO complaintVO = new ComplaintVO();
	    	 complaintVO.setOrderNo(orderVO.getOrderNo());
	    	 complaintVO.setName(orderVO.getMemberVO().getMemName());
	    
	        model.addAttribute("complaintVO", complaintVO);
	        model.addAttribute("orderVO", orderVO);
	        return "/front-end/shop/complaintForm";
	    }

	    @PostMapping("complaintSend")
	    public String submitComplaint(@Valid ComplaintVO complaintVO, 
	    		BindingResult result, 
	    		RedirectAttributes redirectAttributes) {
	    	
	    	if (result.hasErrors()) {
				return "front-end/shop/complaintForm";
			}
	    	
	        sendComplaintEmail(complaintVO);
	        redirectAttributes.addFlashAttribute("successMessage", "我們已收到您的意見，煩請靜待回覆");
	        return "redirect:/shop/order/memOrders";
	    }

	    private void sendComplaintEmail(ComplaintVO complaintVO) {
	        SimpleMailMessage message = new SimpleMailMessage();
	        OrderVO orderVO = orderSvc.getOneOrder(complaintVO.getOrderNo());
	        
	        message.setTo("cia102g2.topseeker@gmail.com");
	        message.setSubject("訂單編號: " + complaintVO.getOrderNo() + "提出訂單意見");
	        message.setText("訂單編號: " + complaintVO.getOrderNo() + 
	        				"\n訂購人: " + orderVO.getMemberVO().getMemName() + 
	        				"\n電子郵件: " + complaintVO.getEmail() + 
	        				"\n\n內容:\n" + complaintVO.getMessage());
	        mailSender.send(message);
	    }


	
	
}
