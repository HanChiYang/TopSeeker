package com.topseeker.member.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.mailutil.MailManager;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.redisconfig.TokenService;

import hibernate.util.CompositeQuery.HibernateUtil_CompositeQuery_Mem;

@Controller
@RequestMapping("/protected/member")
public class MemberControllerValidated {

	@Value("${mail.user}")
	private String mailServerUser;

	@Value("${mail.pwd}")
	private String mailServerPwd;
	
	@Autowired
	MemberService memSvc;
	
	@Autowired
    private SessionFactory sessionFactory;

	/*************************** 會員驗證頁面 **************************/
	@PostMapping("verifyPage")
	public String verifyMem(Model model) {
		return "front-end/member/verifyMem";
	}

	/*************************** 信件驗證功能 **************************/
	// 1. 發送驗證信

	@GetMapping("sendVerifyMail")
	@ResponseBody
	public void sendVerifyMail(HttpSession session) {
		// 產生隨機碼
		String allChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		char[] charArray = allChar.toCharArray(); // 轉為陣列

		String verifyString = "";
		for (int i = 0; i <= 8; i++) {
			int ranIdx = (int) (Math.random() * allChar.length());
			verifyString += charArray[ranIdx];
		}

		// 將驗證碼存於session中
		session.setAttribute("verifyString", verifyString);

		// 取得會員email並寄出
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		String memberEmail = loggedInMember.getMemEmail();

		MailManager mailManager = new MailManager(mailServerPwd, mailServerUser);
		List<String> to = List.of(memberEmail);
		List<String> cc = List.of();
		List<String> bcc = List.of();
		mailManager.sentMail(memberEmail, to, cc, bcc, "TopSeeker踏徙客", "TopSeeker會員驗證", "您的會員驗證碼為: \n" + verifyString);
	}

	// 2. 會員輸入驗證碼並驗證

	@PostMapping("verifyMem")
	public String verifyMem(Model model, HttpSession session, @RequestParam("verifyCode") String verifyCode) {
		String verifyString = session.getAttribute("verifyString").toString();

		if (verifyString.equals(verifyCode)) {
			MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
			Integer loggedInMemberNo = loggedInMember.getMemNo();

			// 修改會員狀態
			memSvc.verifyMem(loggedInMemberNo);

			// 將原session去除
			session.removeAttribute("loggedInMember");
			MemberVO verifiedMember = memSvc.getOneMem(loggedInMemberNo);

			// set新的session
			session.setAttribute("loggedInMember", verifiedMember);

			return "front-end/member/indexMem";

		} else {
			// 驗證碼錯誤失敗，顯示錯誤訊息
			model.addAttribute("verifyError", "驗證碼錯誤");
			return "front-end/member/verifyMem";
		}

	}

	/*************************** 修改密碼 ******************/

	// 修改密碼頁面mapping
	@PostMapping("updatePasswordPage")
	public String updatePassword(Model model) {
		return "front-end/member/updatePasswordPage";
	}

	// 確認並修改密碼
	@PostMapping("confirmPassword")
	public String confirmPassword(@RequestParam("password1") String password1,
			@RequestParam("password2") String password2, ModelMap model, HttpSession session) {

		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

		// 來自信件重設密碼
		if (loggedInMember == null) {

			String memEmailFromReset = (String) session.getAttribute("resetMemEmail");

			Optional<MemberVO> memberOpt = memSvc.findByEmail(memEmailFromReset);
			MemberVO memberFromReset = memberOpt.get();
			memSvc.updatePassword(password1, memberFromReset.getMemNo());
			return "front-end/member/loginMem";
		} else {
			// 登入後重設密碼
			memSvc.updatePassword(password1, loggedInMember.getMemNo());
			return "front-end/member/loginMem";
		}
	}

	/*************************** 修改大頭貼 *********************/
	@PostMapping("updatePic")
	public String updatePic(@RequestParam("memImg") MultipartFile[] parts, HttpSession session) throws IOException {
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			byte[] memImg = memSvc.getOneMem(loggedInMember.getMemNo()).getMemImg();
			loggedInMember.setMemImg(memImg);
			memSvc.updateMemImg(memImg, loggedInMember.getMemNo());

		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] memImg = multipartFile.getBytes();
				loggedInMember.setMemImg(memImg);
				memSvc.updateMemImg(memImg, loggedInMember.getMemNo());
			}
		}
		return "front-end/member/indexMem";
	}

	/*************************** 檢視會員資料 ******************/

	@PostMapping("inspect")
	public String getOne_For_Inspect(HttpSession session, Model model) {
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		model.addAttribute("memberVO", loggedInMember);
		return "front-end/member/inspectMem"; 
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(MemberVO memberVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(memberVO, "memberVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

}