package com.topseeker.message.controller;

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
import com.topseeker.message.model.MessageVO;
import com.topseeker.message.model.MessageService;
 
@Controller
@Validated
@RequestMapping("/message")
public class MessageController {
	
	@Autowired
	MessageService messageSvc;
	
	@Autowired  // 要先增actsvc 跟 membersvc
	ActService actSvc;
	
	@Autowired  // 要先增actsvc 跟 membersvc
	MemberService memberSvc;
	
	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addMessage")
	public String addMessage(ModelMap model) {
		MessageVO messageVO = new MessageVO();
		model.addAttribute("messageVO", messageVO);
		return "front-end/message/addMessage";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid MessageVO messageVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(messageVO, result, "upFiles");

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
		messageSvc.addMessage(messageVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<MessageVO> list = messageSvc.getAll();
		model.addAttribute("messageListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/message/listAllMessage"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("actMsgNo") String actMsgNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		MessageVO messageVO = messageSvc.getOneMessage(Integer.valueOf(actMsgNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("messageVO", messageVO);
		return "front-end/message/update_Message_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid MessageVO messageVO, BindingResult result, ModelMap model,
			 MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(messageVO, result, "upFiles");

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
			return "front-end/message/update_Message_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		messageSvc.updateMessage(messageVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		messageVO = messageSvc.getOneMessage(Integer.valueOf(messageVO.getActMsgNo()));
		model.addAttribute("messageVO", messageVO);
		return "front-end/message/listOneMessage"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("actMsgNo") String actMsgNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		messageSvc.deleteMessage(Integer.valueOf(actMsgNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<MessageVO> list = messageSvc.getAll();
		model.addAttribute("messageListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/message/listAllMessage"; // 刪除完成後轉交listAllEmp.html
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("messageListData")
	protected List<MessageVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<MessageVO> list = messageSvc.getAll();
		return list;
	}
	
	@ModelAttribute("memberListData")
	protected List<MemberVO> referenceListData2() {
		// DeptService deptSvc = new DeptService();
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
		
	
	@ModelAttribute("actListData")
	protected List<ActVO> referenceListData4() {
		// DeptService deptSvc = new DeptService();
		List<ActVO> list = actSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
	@ModelAttribute("actMapData") //
	protected Map<Integer, String> referenceMapData() {
		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
		map.put(1, "阿里山趴趴走");
		map.put(2, "來去草嶺古道看芒草");
		map.put(3, "阿朗壹古道探險之旅部");
		return map;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(MessageVO messageVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(messageVO, "messageVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listMessages_ByCompositeQuery")
	public String listAllMessage(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<MessageVO> list = messageSvc.getAll(map);
		model.addAttribute("messageListData", list); // for listAllEmp.html 第85行用
		return "front-end/message/listAllMessage";
	}


}
