package com.topseeker.actpicture.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


import com.topseeker.act.model.ActService;
import com.topseeker.act.model.ActVO;
import com.topseeker.actpicture.model.ActPictureService;
import com.topseeker.actpicture.model.ActPictureVO;





@Controller
@RequestMapping("/actpicture")
public class ActpictureController {
	
	@Autowired
	ActPictureService actPictureSvc;

	@Autowired
	ActService actSvc;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addActPic")
	public String addAct(ModelMap model) {
		ActPictureVO actPictureVO = new ActPictureVO();
		model.addAttribute("actPictureVO", actPictureVO);
		return "front-end/actpicture/addActPic";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid ActPictureVO actPictureVO, BindingResult result, ModelMap model,
			@RequestParam("actPic") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(actPictureVO, result, "actPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "活動照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				actPictureVO.setActPic(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "front-end/actpicture/addActPic";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actPictureSvc.addActPicture(actPictureVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<ActPictureVO> list = actPictureSvc.getAll();
		model.addAttribute("actPictureListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/actpicture/listAllActPic"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("actPicNo") String actPicNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		List<ActPictureVO> actPictureVO = actPictureSvc.getOneActPicture(Integer.valueOf(actPicNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("actPictureVO", actPictureVO);
		return "front-end/actpicture/update_actpic_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ActPictureVO actPictureVO, BindingResult result, ModelMap model) 
			throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/		
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actPictureSvc.updateActPicture(actPictureVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		actPictureVO = (@Valid ActPictureVO)actPictureSvc.getOneActPicture(Integer.valueOf(actPictureVO.getActPicNo()));
		model.addAttribute("actPictureVO", actPictureVO);
		return "front-end/actpicture/listOneActPic"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("actPicNo") String actPicNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		actPictureSvc.deleteActPicture(Integer.valueOf(actPicNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ActPictureVO> list = actPictureSvc.getAll();
		model.addAttribute("actPicListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/actpicture/listAllActPic"; // 刪除完成後轉交listAllEmp.html
	}
	
	@PostMapping("deleteActImage")
	@ResponseBody
	public Map<String, Object> deleteActImage(@RequestParam("actPicNo") Integer actPicNo) {
		Map<String, Object> response = new HashMap<>();
		try {
			
			/*************************** 2.開始刪除資料 *****************************************/
			boolean success = actPictureSvc.deleteActPicture(actPicNo);

			if (success) {
				response.put("success", true);
				
				System.out.println("圖片刪除成功");
			} else {
				response.put("success", false);
				response.put("message", "圖片刪除失敗");
				System.out.println("圖片刪除失敗");
			}

		} catch (Exception e) {
			response.put("success", false);
			response.put("message", "圖片刪除過程中發生錯誤");
			e.printStackTrace();
		}
		System.out.println("Response: " + response);
		return response;
	}

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	
	//for addActPic.html 活動編號取值
	@ModelAttribute("actListData")
	protected List<ActVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<ActVO> list = actSvc.getAll();
		return list;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ActPictureVO actPictureVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(actPictureVO, "actPictureVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
			
		}
		return result;
	}
	
	@GetMapping("/select_page")
	public String select_page_pic(Model model) {
		return "front-end/actpicture/select_page";
	}
    
    @GetMapping("/listAllActPic")
	public String listAllActPic(Model model) {
		return "front-end/actpicture/listAllActPic";
	}
    
    @ModelAttribute("actPicListData")
	protected List<ActPictureVO> referenceActPicListData(Model model) {
		
    	List<ActPictureVO> list = actPictureSvc.getAll();
		return list;
	}

}