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
@RequestMapping("/member")
public class MemberController {

	@Value("${mail.user}")
	private String mailServerUser;

	@Value("${mail.pwd}")
	private String mailServerPwd;

	@Autowired
	MemberService memSvc;

	@Autowired
	TokenService tokSvc;
	
	@Autowired
    private SessionFactory sessionFactory;


	/*************************** 註冊帳號 ******************/

	@PostMapping("register")
	public String insert(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("memImg") MultipartFile[] parts,
			@RequestParam("memAccount") String memAccount,
			@RequestParam("memEmail") String memEmail,
			@RequestParam("memUid") String memUid, HttpSession session) throws IOException {

		// 去除BindingResult中upFiles欄位的FieldError紀錄
		result = removeFieldError(memberVO, result, "memImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			memberVO.setMemImg(null); // 可以不上傳
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				memberVO.setMemImg(buf);
			}
		}
		memberVO.setMemStatus((byte) 1); // 預設1，未驗證狀態

		if (result.hasErrors()) {
			return "front-end/member/registrationMem";
		}
		//檢查是否有相同名稱帳號
	    List<String> errorMessages = new LinkedList<>();
	    if (memSvc.findByAccount(memAccount) != null) {
	        errorMessages.add("已有相同名稱帳號");
	    }
	    if (memSvc.findByEmail(memEmail).isPresent()) {
	        errorMessages.add("已有相同電子郵件");
	    }
	    if (memSvc.findByUid(memUid) != null) {
	        errorMessages.add("已有相同身份證字號");
	    }

	    if (!errorMessages.isEmpty()) {
	        model.addAttribute("errorMessages", errorMessages);
	        return "front-end/member/registrationMem";
	    }
		
		/*************************** 2.開始新增資料 ***************************************/
		memSvc.addMem(memberVO);
		session.setAttribute("loggedInMember", memberVO);

		/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
		return "front-end/member/verifyMem";
	}

	/*************************** 登出、登入功能 ******************/
	// 1. 登入
	@PostMapping("loginMem")
	public String loginMem(Model model, HttpSession session,
			@RequestParam("memAccount") String memAccount,
			@RequestParam("memPassword") String memPassword) {

		String oriURL = session.getAttribute("oriURL") != null ? session.getAttribute("oriURL").toString() : "/";

		/*************************** 2.開始查詢資料 ***************************************/
		Optional<MemberVO> memberOpt = memSvc.memLogin(memAccount, memPassword);

		/*************************** 3.查詢成功，登入 **************************/
		if (memberOpt.isPresent()) {
			MemberVO member = memberOpt.get();
			if (member.getMemStatus() == 0) {
				model.addAttribute("loginError", "您已被停權");
				return "front-end/member/loginMem";// 若被停權，無法登入
			}
			session.setAttribute("loggedInMember", member); // 將會員資料存於session內
			return "redirect:" + oriURL; // 重導至欲前往的頁面

		} else {
			model.addAttribute("loginError", "無效的帳號或密碼");
			return "front-end/member/loginMem";
		}
	}

	// 2. 登出
	@GetMapping("logout")
	public String logoutMem(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}

	/*********************** 忘記密碼 ******************/

	@GetMapping("forgetPW")
	public String forgetPW() {
		return "front-end/member/forgetPW";
	}

	// 確認信箱
	@PostMapping("checkEmail")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam("memEmail") String memEmail, HttpSession session) {
	    
		Optional<MemberVO> memberOpt = memSvc.findByEmail(memEmail);
	    Map<String, Object> response = new HashMap<>();

	    // 若找到
	    if (memberOpt.isPresent()) {
	        Integer memNo = memberOpt.get().getMemNo();
	        String token = UUID.randomUUID().toString();

	        tokSvc.saveToken(token, memNo, memEmail);

	        String url = "http://localhost:8080/member/resetPassword?token=" + token;
	        String message = "請於三分鐘內點擊超連結以重設密碼: \n" + url;

	        MailManager mailManager = new MailManager(mailServerPwd, mailServerUser);
	        List<String> to = List.of(memEmail);
	        List<String> cc = List.of();
	        List<String> bcc = List.of();
	        mailManager.sentMail(memEmail, to, cc, bcc, "TopSeeker踏徙客", "TopSeeker會員密碼重設", message);

	        response.put("success", true);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	        response.put("success", false);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	}

	// 顯示重設密碼頁面
	@GetMapping("resetPassword")
	public String showResetPWPage(@RequestParam("token") String token, HttpSession session) {

		// 取得存在redis的會員編號及email
		MemberVO resetMember = tokSvc.checkToken(token);

		if (resetMember == null) {
			return "front-end/member/resetFail";
		} else {
			String resetMemEmail = resetMember.getMemEmail();
			session.setAttribute("resetMemEmail", resetMemEmail);
			return "front-end/member/resetPasswordPage";
		}
	}
	
	@PostMapping("resetPassword")
	public String resetPassword(@RequestParam("password1") String password1,
			@RequestParam("password2") String password2, HttpSession session) {

			String memEmailFromReset = (String) session.getAttribute("resetMemEmail");

			Optional<MemberVO> memberOpt = memSvc.findByEmail(memEmailFromReset);
			MemberVO memberFromReset = memberOpt.get();
			memSvc.updatePassword(password1, memberFromReset.getMemNo());
			return "front-end/member/loginMem";
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