package com.topseeker.notification.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.topseeker.member.model.MemberVO;
import com.topseeker.notification.model.NotificationService;
import com.topseeker.notification.model.NotificationVO;


@Controller
@Validated
@RequestMapping("/protected/notification")
public class NotificationController {
	
	@Autowired
	NotificationService notiSvc;
	
//	@PostMapping("listAllNoti")
//	public String listAllNoti(HttpServletRequest req , Model model) {
//		List<NotificationVO> list = notiSvc.getAll();
//		model.addAttribute("notiListData", list);
//		return "front-end/notification/allNoti";
//	}
	
	@GetMapping("allNoti")
	public String listAllNoti(Model model, HttpSession session) {
		
		//自session取得登入會員資料
        MemberVO memberVO = (MemberVO) session.getAttribute("loggedInMember");
		Integer memNo = memberVO.getMemNo();
		
		//修改通知狀態
    	notiSvc.readAllNoti(memNo);

    	//取得該會員所有通知
		List<NotificationVO> notiListData = notiSvc.getMemNoti(memNo);
		
		model.addAttribute("notiListData", notiListData);
		return "front-end/notification/allNoti";
	}
	
}