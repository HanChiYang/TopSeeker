package com.topseeker.follow.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
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

import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.follow.model.FollowService;
import com.topseeker.follow.model.FollowVO;
 
@Controller
@Validated
@RequestMapping("/follow")
public class FollowController {
	
	@Autowired
	FollowService followSvc;
	
	@Autowired  // 要先增actsvc 跟 membersvc
	MemberService memberSvc;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addFollow")
	public String addFollow(ModelMap model) {
		FollowVO followVO = new FollowVO();
		model.addAttribute("followVO", followVO);
		return "back-end/follow/addFollow";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid FollowVO followVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(followVO, result, "upFiles");

//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "員工照片: 請上傳照片");
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//				participantVO.setUpFiles(buf);
//			}
//		}
		
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		followSvc.addFollow(followVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<FollowVO> list = followSvc.getAll();
		model.addAttribute("followListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/follow/listAllFollow"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("memNoA") String memNoA, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		FollowVO followVO = followSvc.getOneFollow(Integer.valueOf(memNoA));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("followVO", followVO);
		return "back-end/follow/update_Follow_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid FollowVO followVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(followVO, result, "upFiles");

//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
//			// EmpService empSvc = new EmpService();
//			byte[] upFiles = participantSvc.getOneParticipant(participantVO.getActPartNo()).getUpFiles();
//			participantVO.setUpFiles(upFiles);
//		} else {
//			for (MultipartFile multipartFile : parts) {
//				byte[] upFiles = multipartFile.getBytes();
//				participantVO.setUpFiles(upFiles);
//			}
//		}
		if (result.hasErrors()) {
			return "back-end/follow/update_Follow_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		followSvc.updateFollow(followVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		followVO = followSvc.getOneFollow(Integer.valueOf(followVO.getMemNoA()));
		model.addAttribute("followVO", followVO);
		return "back-end/follow/listOneFollow"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("memNoA") String memNoA ,@RequestParam("memNoB") String memNoB, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		followSvc.deleteFollow(Integer.valueOf(memNoA), Integer.valueOf(memNoB));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<FollowVO> list = followSvc.getAll();
		model.addAttribute("followListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/follow/listAllFollow"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("followListData")
	protected List<FollowVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<FollowVO> list = followSvc.getAll();
		return list;
	}
	
	@ModelAttribute("memberListData")
	protected List<MemberVO> referenceListData2() {
		// DeptService deptSvc = new DeptService();
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
	
	

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
//	@ModelAttribute("actMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(1, "阿里山趴趴走");
//		map.put(2, "來去草嶺古道看芒草");
//		map.put(3, "阿朗壹古道探險之旅部");
//		return map;
//	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(FollowVO followVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(followVO, "followVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listFollow_ByCompositeQuery")
	public String listAllFollow(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<FollowVO> list = followSvc.getAll(map);
		model.addAttribute("followListData", list); // for listAllEmp.html 第85行用
		return "back-end/follow/listAllFollow";
	}

	
	 public FollowController(MemberService memberService) {
	        this.memberSvc = memberService;
	    }

//	    @GetMapping("/follow/add")
//	    public String showAddFollowForm(Model model) {
//	      
//	        List<MemberVO> members = memberSvc.getAllMembers();
//	        
//	        model.addAttribute("members", members);
//
//	      
//	        model.addAttribute("followVO", new FollowVO());
//
//	        return "back-end/follow/addFollow";
//	    }
 

}
