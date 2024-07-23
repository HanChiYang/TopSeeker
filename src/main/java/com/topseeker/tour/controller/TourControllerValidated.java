package com.topseeker.tour.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.topseeker.member.model.MemberVO;
import com.topseeker.tour.model.TourService;
import com.topseeker.tourGroup.model.TourGroupService;
import com.topseeker.tourGroup.model.TourGroupVO;
import com.topseeker.tourOrder.model.TourOrderService;
import com.topseeker.tourOrder.model.TourOrderVO;

@Controller
@RequestMapping("/tour")
public class TourControllerValidated {

	@Autowired
	TourService tourSvc;

	@Autowired
	TourGroupService tourGroupSvc;
	
	@Autowired
	TourOrderService tourOrderSvc;

	@GetMapping("/addOrder")
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
  
  TourGroupVO tourGroupVO = tourGroupSvc.getOneTourGroup(Integer.valueOf(groupNo));

     	model.addAttribute("tourGroupVO", tourGroupVO);
     	model.addAttribute("addOrder", "true"); // 旗標getOne_For_Display見select_page.html的第156行 -->
        model.addAttribute("groupNo", groupNo);
        return "front-end/tour/addOrder"; // 返回 addOrder 頁面
    }
}
