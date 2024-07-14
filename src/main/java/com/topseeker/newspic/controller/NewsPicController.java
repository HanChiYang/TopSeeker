package com.topseeker.newspic.controller;

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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;



import com.topseeker.newspic.model.NewsPicVO;
import com.topseeker.newspic.model.NewsPicService;
import com.topseeker.news.model.NewsVO;
import com.topseeker.news.model.NewsService;



@Controller
@RequestMapping("/newspic")
public class NewsPicController {
	
	@Autowired
	NewsPicService newsPicSvc;

	@Autowired
	NewsService newsSvc;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("addNewsPic")
	public String addNewsPic(ModelMap model) {
		NewsPicVO newsPicVO = new NewsPicVO();
		model.addAttribute("newsPicVO", newsPicVO);
		return "front-end/newspic/addNewsPic";
	}

	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("insert")
	public String insert(@Valid NewsPicVO newsPicVO, BindingResult result, ModelMap model,
			@RequestParam("newsImg") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(newsPicVO, result, "newsImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "活動照片: 請上傳照片");
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				newsPicVO.setNewsImg(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "front-end/newspic/addNewsPic";
		}
		/*************************** 2.開始新增資料 *****************************************/
		// EmpService empSvc = new EmpService();
		newsPicSvc.addNewsPic(newsPicVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<NewsPicVO> list = newsPicSvc.getAll();
		model.addAttribute("newsPicListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/newspic/listAllNewsPic"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/emp/listAllEmp")
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("newsImgNo") String newsImgNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		NewsPicVO newsPicVO = newsPicSvc.getOneNewsPic(Integer.valueOf(newsImgNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("newsPicVO", newsPicVO);
		return "front-end/newspic/update_newspic_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid NewsPicVO newsPicVO, BindingResult result, ModelMap model,
			@RequestParam("newsImg") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		System.out.println("update");
		
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(newsPicVO, result, "newsImg");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的新圖片時
			// EmpService empSvc = new EmpService();
			byte[] newsPic = newsPicSvc.getOneNewsPic(newsPicVO.getNewsImgNo()).getNewsImg();
			newsPicVO.setNewsImg(newsPic);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] newsPic = multipartFile.getBytes();
				newsPicVO.setNewsImg(newsPic);
			}
		}
		if (result.hasErrors()) {
			System.out.println("hasError");
			return "front-end/newspic/update_newspic_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		newsPicSvc.updateNewsPic(newsPicVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		newsPicVO = newsPicSvc.getOneNewsPic(Integer.valueOf(newsPicVO.getNewsImgNo()));
		model.addAttribute("newsPicVO", newsPicVO);
		return "front-end/newspic/listOneNewsPic"; // 修改成功後轉交listOneEmp.html
	}

	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("newsImgNo") String newsImgNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		newsPicSvc.deleteNewsPic(Integer.valueOf(newsImgNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<NewsPicVO> list = newsPicSvc.getAll();
		model.addAttribute("newsPicListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "front-end/newspic/listAllNewsPic"; // 刪除完成後轉交listAllEmp.html
	}
	
	@PostMapping("deleteNewsImage")
	@ResponseBody
	public Map<String, Object> deleteNewsImage(@RequestParam("newsImgNo") Integer newsImgNo) {
		Map<String, Object> response = new HashMap<>();
		try {
			
			/*************************** 2.開始刪除資料 *****************************************/
			boolean success = newsPicSvc.deleteNewsPic(newsImgNo);

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
	@ModelAttribute("newsListData")
	protected List<NewsVO> referenceListData() {
		// DeptService deptSvc = new DeptService();
		List<NewsVO> list = newsSvc.getAll();
		return list;
	}
	

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(NewsPicVO newsPicVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(newsPicVO, "newsPicVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
			
		}
		return result;
	}
	
	@GetMapping("/select_page")
  	public String select_page_newspic(Model model) {
  		return "front-end/newspic/select_page";
  	}
      
    @GetMapping("/listAllNewsPic")
  	public String listAllNewsPic(Model model) {
  		return "front-end/newspic/listAllNewsPic";
  	}
      
    @ModelAttribute("newsPicListData")
  	protected List<NewsPicVO> referenceNewsPicListData(Model model) { 		
      	List<NewsPicVO> list = newsPicSvc.getAll();
  		return list;
  	}

}