package com.topseeker.follow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.follow.model.FollowService;
import com.topseeker.follow.model.FollowVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;

@Controller
@Validated
@RequestMapping("/protected/follow")
public class FollowController {

	@Autowired
	FollowService followSvc;

	@Autowired
	MemberService memberSvc;

	@Autowired
	ActService actSvc;

	// 追隨頁面
	@GetMapping
	public String followPage(ModelMap model, HttpSession session) {

		// 獲取登入者的會員編號
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		Integer loggedInMemberNo = loggedInMember.getMemNo();

		// 獲取所有追隨者
		List<FollowVO> followListData = followSvc.findAllFollowers(loggedInMemberNo);
		model.addAttribute("followListData", followListData);

		// 準備一個 Map 裝追隨者的開團中活動
		Map<Integer, List<ActVO>> beFollowedMemActMap = new HashMap<>();

		// 獲取所有追隨者的開團中活動
		for (FollowVO follow : followListData) {
			MemberVO beFollowedMemberVO = follow.getBeFollowedMemberVO();
			List<ActVO> beFollowedMemAct = actSvc.findMyOpenGroup(beFollowedMemberVO.getMemNo());

			// 將每個追隨者的活動添加到Map中
			beFollowedMemActMap.put(beFollowedMemberVO.getMemNo(), beFollowedMemAct);
		}

		model.addAttribute("beFollowedMemActMap", beFollowedMemActMap);

		return "front-end/follow/allFollow";

	}

	@ResponseBody
	@PostMapping("insert")
	public String insert(@RequestParam("beFollowedMemNo") Integer beFollowedMemNo, HttpSession session) {

		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		Integer loggedInMemberNo = loggedInMember.getMemNo();

		//確認是否已被追隨過
		if (followSvc.checkFollowVO(loggedInMemberNo, beFollowedMemNo).isPresent()) {
			return "{\"message\":\"您已追隨此會員\"}";
		} else {
		//若未追隨過，則新增於資料庫
			followSvc.addFollow(loggedInMemberNo, beFollowedMemNo);
			return "{\"message\":\"追隨成功\"}";
		}
	}

	@PostMapping("delete")
	public String delete(@RequestParam("followedMemNo") Integer beFollowedMemNo, ModelMap model,
			HttpSession session) {
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		Integer loggedInMemberNo = loggedInMember.getMemNo();

		// 刪除追隨者
		followSvc.deleteFollow(loggedInMemberNo, beFollowedMemNo);

		// 重新獲取追隨者及其開團列表
		List<FollowVO> followListData = followSvc.findAllFollowers(loggedInMemberNo);
		model.addAttribute("followListData", followListData);

		// 準備一個 Map 裝追隨者的開團中活動
		Map<Integer, List<ActVO>> beFollowedMemActMap = new HashMap<>();

		// 獲取所有追隨者的開團中活動
		for (FollowVO follow : followListData) {
			MemberVO beFollowedMemberVO = follow.getBeFollowedMemberVO();
			List<ActVO> beFollowedMemAct = actSvc.findMyOpenGroup(beFollowedMemberVO.getMemNo());

			// 將每個追隨者的活動添加到Map中
			beFollowedMemActMap.put(beFollowedMemberVO.getMemNo(), beFollowedMemAct);
		}

		model.addAttribute("beFollowedMemActMap", beFollowedMemActMap);

		return "front-end/follow/allFollow";
	}
}
