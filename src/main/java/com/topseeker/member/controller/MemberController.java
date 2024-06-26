package com.topseeker.member.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;

@Controller
@RequestMapping("/member")
public class MemberController {

	@Autowired
	MemberService memSvc;

	@GetMapping("addMem")
	public String addMem(ModelMap model) {
		MemberVO memberVO = new MemberVO();
		model.addAttribute("memberVO", memberVO);
		return "back-end/member/addMem";
	}

	/*
	 * This method will be called on addEmp.jsp form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("memImg") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ******************/
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
		memberVO.setMemStatus((byte) 1); // 預設1

		if (result.hasErrors()) {
			return "front-end/member/registrationMem";
		}
		/*************************** 2.開始新增資料 ***************************************/
//		EmpService empSvc = new EmpService();
		memSvc.addMem(memberVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
		List<MemberVO> list = memSvc.getAll();
		model.addAttribute("memListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.jsp form submission, handling POST
	 * request It also validates the user input
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("memNo") String memNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ******************/
		/*************************** 2.開始查詢資料 ***************************************/
//		EmpService empSvc = new EmpService();
		MemberVO memberVO = memSvc.getOneMem(Integer.valueOf(memNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) ***********/
		model.addAttribute("memberVO", memberVO);
		return "back-end/member/update_mem_input"; // 查詢完成後轉交update_emp_input.jsp
	}

	/*
	 * This method will be called on update_emp_input.jsp form submission, handling
	 * POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("memImg") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ******************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(memberVO, result, "memImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			EmpService empSvc = new EmpService();
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
//		EmpService empSvc = new EmpService();
		memSvc.updateMem(memberVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) ***********/
		model.addAttribute("success", "- (修改成功)");
		memberVO = memSvc.getOneMem(Integer.valueOf(memberVO.getMemNo()));
		model.addAttribute("memberVO", memberVO);
		return "back-end/member/listOneMem"; // 修改成功後轉交listOneEmp.jsp
	}

	/*
	 * This method will be called on listAllEmp.jsp form submission, handling POST
	 * request It also validates the user input
	 */
//	@PostMapping("delete")
//	public String delete(@RequestParam("memNo") String memNo, ModelMap model) {
	/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 *******************/

	/*************************** 2.開始刪除資料 ***************************************/
//		EmpService empSvc = new EmpService();
//		memSvc.deleteMem(Integer.valueOf(memNo));
	/*************************** 3.刪除完成,準備轉交(Send the Success view) ***********/
//		List<MemberVO> list = memSvc.getAll();
//		model.addAttribute("memListData", list);
//		model.addAttribute("success", "- (刪除成功)");
//		return "back-end/member/listAllMem"; // 刪除完成後轉交listAllEmp.jsp
//	}

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

	/*
	 * This method will be called on select_page.jsp form submission, handling POST
	 * request
	 */
	@PostMapping("listMems_ByCompositeQuery")
	public String listAllMems(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<MemberVO> list = memSvc.getAll(map);
		model.addAttribute("memListData", list); // for listAllEmp.jsp 第85行用
		return "back-end/member/listAllMem";
	}

	/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ******************/

	@PostMapping("loginMem")
	public String loginMem(Model model, HttpServletRequest req, HttpServletResponse res, HttpSession session,
			@RequestParam("memAccount") String memAccount, @RequestParam("memPassword") String memPassword) {

    	String oriURL = session.getAttribute("oriURL") != null ? session.getAttribute("oriURL").toString() : "/";

		Map<String, String> errorMsgs = new LinkedHashMap<String, String>();
		model.addAttribute("errorMsgs", errorMsgs);

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
//			return "redirect:/"; // 重導至欲前往的頁面
			return "redirect:" + oriURL; // 重導至欲前往的頁面

		} else {
			model.addAttribute("loginError", "無效的帳號或密碼");
			return "front-end/member/loginMem"; // 登入失敗，返回登入頁面並顯示錯誤信息
		}
	}

	/*************************** 登出功能 **************************/
	@GetMapping("logout")
	public String logoutMem(HttpSession session) {
		session.invalidate();
		//		Optional<MemberVO> memberOpt = memSvc.memLogout(memAccount, memPassword);
		return "redirect:/";
	}

}