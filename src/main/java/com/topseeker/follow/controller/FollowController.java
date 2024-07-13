package com.topseeker.follow.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.follow.model.FollowService;
import com.topseeker.follow.model.FollowVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
 
@Controller
@Validated
@RequestMapping("/follow")
public class FollowController {
	
	@Autowired
	FollowService followSvc;
	
	@Autowired
	MemberService memberSvc;

	//追隨頁面
    @GetMapping
	public String followPage(ModelMap model, HttpSession session) {
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		Integer loggedInMemberNo = loggedInMember.getMemNo();
		
		List<FollowVO> followListData = followSvc.findAllFollowers(loggedInMemberNo);
//		List<MemberVO> followMemVOList = memberSvc.f
				
		model.addAttribute("followListData", followListData);
		
		return "front-end/follow/allFollow";
	}

	@PostMapping("insert")
	public String insert(@RequestParam("memNoB") Integer memNoB, ModelMap model, HttpSession session) {
		
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		Integer loggedInMemberNo = loggedInMember.getMemNo();
		
		followSvc.addFollow(loggedInMemberNo, memNoB);
		
		List<FollowVO> followListData = followSvc.findAllFollowers(loggedInMemberNo);
		model.addAttribute("followListData", followListData);
		return "redirect:/follow/listAllFollow"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}
	
	@PostMapping("delete")
	public String delete(@RequestParam("beFollowedMemNo") Integer beFollowedMemNo, ModelMap model, HttpSession session) {
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		Integer loggedInMemberNo = loggedInMember.getMemNo();
		
		followSvc.deleteFollow(loggedInMemberNo, beFollowedMemNo);
		
		List<FollowVO> followListData = followSvc.findAllFollowers(loggedInMemberNo);
		model.addAttribute("followListData", followListData);
		
		return "front-end/follow/allFollow";
	}
}
