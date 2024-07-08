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


	/*************************** 註冊帳號 ******************/

	@PostMapping("register")
	public String insert(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("memImg") MultipartFile[] parts,
			@RequestParam("memAccount") String memAccount,
			@RequestParam("memEmail") String memEmail,
			@RequestParam("memUid") String memUid, HttpSession session) throws IOException {

		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(memberVO, result, "memImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			memberVO.setMemImg(null); // 可以不上傳
//			model.addAttribute("errorMessage", "員工照片: 請上傳照片");  --> 由messages.properties 第五行處理錯誤信息
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

	// 會員驗證頁面mapping
	@PostMapping("protected/verifyPage")
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
	@PostMapping("protected/updatePasswordPage")
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

	/******************** 後台進入單一會員修改頁面 ******************/
	
	@PostMapping("backend_protected/update")
	public String getOne_For_Update(@RequestParam("memNo") String memNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ******************/
		/*************************** 2.開始查詢資料 ***************************************/
		MemberVO memberVO = memSvc.getOneMem(Integer.valueOf(memNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) ***********/
		model.addAttribute("memberVO", memberVO);
		return "back-end/member/update_mem_input"; 
	}
	
	/******************** 後台修改會員資料 ******************/

	@PostMapping("update")
	public String update(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("memImg") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ******************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(memberVO, result, "memImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			byte[] memImg = memSvc.getOneMem(memberVO.getMemNo()).getMemImg();
			memberVO.setMemImg(memImg);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] memImg = multipartFile.getBytes();
				memberVO.setMemImg(memImg);
			}
		}
		if (result.hasErrors()) {
			return "back-end/member/update_mem_input";
		}
		/*************************** 2.開始修改資料 ***************************************/
		memSvc.updateMem(memberVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
		model.addAttribute("success", "- (修改成功)");
		memberVO = memSvc.getOneMem(Integer.valueOf(memberVO.getMemNo()));
		model.addAttribute("MemberVO", memberVO);
		
		return "back-end/member/listOneMem";
    
	}
	
	/*************************** 檢視會員資料 ******************/

	@PostMapping("protected/inspect")
	public String getOne_For_Inspect(HttpSession session, Model model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ******************/
		/*************************** 2.開始查詢資料 ***************************************/
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
//		Integer loggedInMemberNo = loggedInMember.getMemNo();
//		MemberVO memberVO = memSvc.getOneMem(Integer.valueOf(memNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) ***********/
		model.addAttribute("memberVO", loggedInMember);
		return "front-end/member/inspectMem"; 
	}

	@PostMapping("listMems_ByCompositeQuery")
	public String listAllMems(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<MemberVO> list = memSvc.getAll(map);
		model.addAttribute("memListData", list); // for listAllEmp.jsp 第85行用
		return "back-end/member/listAllMem";
	}
	
	//以下Ajax測試用
//	@GetMapping("listAllMembyAjax")
//	public String listAllMemAjax (Model model) {
//		return "back-end/member/listAllMembyAjax";
//	}
//
//	@PostMapping("ajaxSearch")
//	public String ajaxSearch(HttpServletRequest req, Model model, HttpSession session) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<MemberVO> list = memSvc.getAll(map);
//		model.addAttribute("memListData", list); // for listAllEmp.jsp 第85行用
//        return "back-end/member/listAllFragment :: resultsList";
//	}
//	
//	//以下Ajax會員詳情測試用
//	@PostMapping("ajaxDetail")
//	public String ajaxDetail(@RequestParam("memNo") String memNo, HttpServletRequest req, Model model, HttpSession session) {
//			MemberVO memberVO = (MemberVO) memSvc.getOneMem(Integer.valueOf(memNo));
//
//			model.addAttribute("memberVO", memberVO);
//		return "back-end/member/inspectMemFragment :: memberDetail";
//	}
//	
//	@GetMapping("memberManagement")
//	public String memberManagement() {
//		return "back-end/member/select_page";
//	}

	/*************************** 登出、登入功能 ******************/
	// 1. 登入
	@PostMapping("loginMem")
	public String loginMem(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session,
			@RequestParam("memAccount") String memAccount, @RequestParam("memPassword") String memPassword) {

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
			session.setAttribute("loggedInMember", member); // 將會員信息保存到 session
			return "redirect:" + oriURL; // 重導至欲前往的頁面

		} else {
			model.addAttribute("loginError", "無效的帳號或密碼");
			return "front-end/member/loginMem"; // 登入失敗，返回登入頁面並顯示錯誤信息
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
	public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam("memEmail") String memEmail, HttpSession session, HttpServletRequest req) {
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
	public String showResetPWPage(@RequestParam("token") String token, HttpSession session, Model model) {

//		TokenService tokSvc = new TokenService();

		// 取得存在redis的會員編號及email
		MemberVO resetMember = tokSvc.checkToken(token);

		if (resetMember == null) {
			System.out.println("fail");
			return "front-end/member/resetFail";
		} else {
			String resetMemEmail = resetMember.getMemEmail();
			session.setAttribute("resetMemEmail", resetMemEmail);
			return "front-end/member/updatePasswordPage";
		}
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