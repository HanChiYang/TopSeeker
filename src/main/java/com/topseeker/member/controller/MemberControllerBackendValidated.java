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
@RequestMapping("/backend_protected/member")
public class MemberControllerBackendValidated {

	@Autowired
	MemberService memSvc;

	@Autowired
	private SessionFactory sessionFactory;

	@PostMapping("updatePage")
	public String getOne_For_Update(@RequestParam("memNo") String memNo, ModelMap model) {

		MemberVO memberVO = memSvc.getOneMem(Integer.valueOf(memNo));

		model.addAttribute("memberVO", memberVO);
		return "back-end/member/update_mem_input";
	}

	/******************** 後台修改會員資料 ******************/

	@PostMapping("update")
	public String update(@Valid MemberVO memberVO, BindingResult result, ModelMap model,
			@RequestParam("memImg") MultipartFile[] parts) throws IOException {

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
		memSvc.updateMem(memberVO);

		model.addAttribute("success", "- (修改成功)");
		memberVO = memSvc.getOneMem(Integer.valueOf(memberVO.getMemNo()));
		model.addAttribute("MemberVO", memberVO);

		return "back-end/member/listOneMem";
	}

	/******************** 後台會員管理頁面 ******************/

	@GetMapping("memberManagement")
	public String memberManagement() {
		return "back-end/member/select_page";
	}

	/******************** 後台會員資料複合查詢 ******************/

	@PostMapping("listMems_ByCompositeQuery")
	public String listAllMems(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		Map<String, String[]> queryParams = new HashMap<>(map);

		// 處理日期區間查詢條件
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");

		if (startDate != null && !startDate.trim().isEmpty() && endDate != null && !endDate.trim().isEmpty()) {
			queryParams.put("memDateRange", new String[] { startDate + "," + endDate });
		}

		Session session = sessionFactory.openSession();
		List<MemberVO> list = HibernateUtil_CompositeQuery_Mem.getAllC(queryParams, session);
		model.addAttribute("memListData", list);
		return "back-end/member/listAllMem";
	}

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