package com.topseeker.replymessage.controller;

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
import com.topseeker.message.model.MessageService;
import com.topseeker.message.model.MessageVO;
import com.topseeker.replymessage.model.ReplyMessageService;
import com.topseeker.replymessage.model.ReplyMessageVO;
 
@Controller
@Validated
@RequestMapping("/replymessage")
public class ReplyMessageController {
	
	
	@Autowired  // 要先增actsvc 跟 membersvc
	ReplyMessageService replymessageSvc;
	
	
	@Autowired
	MessageService messageSvc;
	
	
	@Autowired  // 要先增actsvc 跟 membersvc
	MemberService memberSvc;
	
	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addReplyMessage")
	public String addReplyMessage(ModelMap model) {
		ReplyMessageVO replymessageVO = new ReplyMessageVO();
		model.addAttribute("replymessageVO", replymessageVO);
		return "front-end/replymessage/addReplyMessage";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid ReplyMessageVO replymessageVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(replymessageVO, result, "upFiles");

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
		replymessageSvc.addReplyMessage(replymessageVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ReplyMessageVO> list = replymessageSvc.getAll();
		model.addAttribute("replymessageListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/replymessage/listAllReplyMessage"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("actMsgRpNo") String actMsgRpNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		ReplyMessageVO replymessageVO = replymessageSvc.getOneReplyMessage(Integer.valueOf(actMsgRpNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("replymessageVO", replymessageVO);
		return "front-end/replymessage/update_ReplyMessage_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ReplyMessageVO replymessageVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(replymessageVO, result, "upFiles");

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
			return "front-end/replymessage/update_ReplyMessage_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		replymessageSvc.updateReplyMessage(replymessageVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		replymessageVO = replymessageSvc.getOneReplyMessage(Integer.valueOf(replymessageVO.getActMsgRpNo()));
		model.addAttribute("replymessageVO", replymessageVO);
		return "front-end/replymessage/listOneReplyMessage"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("actMsgRpNo") String actMsgRpNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		replymessageSvc.deleteReplyMessage(Integer.valueOf(actMsgRpNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ReplyMessageVO> list = replymessageSvc.getAll();
		model.addAttribute("replymessageListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/replymessage/listAllReplyMessage"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("replymessageListData")
	protected List<ReplyMessageVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<ReplyMessageVO> list = replymessageSvc.getAll();
		return list;
	}
	
	@ModelAttribute("memberListData")
	protected List<MemberVO> referenceListData2() {
		// DeptService deptSvc = new DeptService();
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
		
	
	@ModelAttribute("messageListData")
	protected List<MessageVO> referenceListData3() {
		// DeptService deptSvc = new DeptService();
		List<MessageVO> list = messageSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
	@ModelAttribute("messageMapData") //
	protected Map<Integer, String> referenceMapData() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "我覺得這個行程怪怪的");
		map.put(2, "金天天氣其實不錯");
		map.put(3, "可是我覺得今天天氣不好");
		return map;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ReplyMessageVO replymessageVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(replymessageVO, "replymessageVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listReplyMessages_ByCompositeQuery")
	public String listAllReplyMessage(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<ReplyMessageVO> list = replymessageSvc.getAll(map);
		model.addAttribute("replymessageListData", list); // for listAllEmp.html 第85行用
		return "front-end/replymessage/listAllReplyMessage";
	}

}
