package com.topseeker.knowledge.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.knowledge.model.KnowledgeService;
import com.topseeker.knowledge.model.KnowledgeVO;
import com.topseeker.shop.info.model.ShopInfoVO;

@Controller
@RequestMapping("/knowledge")
public class KnowledgeController {

	// 新手知識用
	@Autowired
	KnowledgeService knowledgeSvc;

	// ============前端新手知識頁面============

	// 新手知識首頁 knowledgeInfo.html
	@GetMapping("/knowledgeInfo")
	public String showKnowledge(ModelMap model) {
		List<KnowledgeVO> knowListData = knowledgeSvc.getAllReleasedKnow();

		model.addAttribute("knowListData", knowListData);

		return "front-end/knowledge/knowledgeInfo";
	}


	// ============後端管理頁面============

	// 列出所有新手知識明細
	@GetMapping("/listAllKnow")
	public String listAllKnowledge(Model model) {
	
	List<KnowledgeVO> list = knowledgeSvc.getAll();
	model.addAttribute("knowListData", list);

	
		return "back-end/knowledge/listAllKnow";
	}
	

	// ============Ajax新增刪除功能============
	
	// 依照消息編號更改上下架狀態
	@PostMapping("/updateKnowStatus")
	public ResponseEntity<?> updateStatus(@RequestParam Integer knowNo, @RequestParam Integer knowStatus) {
		knowledgeSvc.updateKnowStatus(knowNo, knowStatus);
		return ResponseEntity.ok().build();
	}


	// ============後端最新消息CRUD============

	// 新手知識新增 addKnow.html
	@GetMapping("/addKnow")
	public String addKnow(ModelMap model) {
		KnowledgeVO knowledgeVO = new KnowledgeVO();

	    // 設定當下的日期
	    java.sql.Date knowPublishDate = new java.sql.Date(System.currentTimeMillis());
	    knowledgeVO.setKnowPublishDate(knowPublishDate);

		// 預設下架
	    knowledgeVO.setKnowStatus(0);

		model.addAttribute("knowledgeVO", knowledgeVO);
		return "back-end/knowledge/addKnow";
	}

	// 新手知識新增(含圖片)
	@PostMapping("/insertKnow")
	public String insert(@Valid KnowledgeVO knowledgeVO, BindingResult result, ModelMap model,
			@RequestParam("knowPics") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/


		// 使用者未選擇要上傳的圖片時
		if (parts[0].isEmpty()) { 
			model.addAttribute("errorMessage", "新手知識圖片: 請上傳圖片");
			knowledgeVO.setKnowPic(null);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				knowledgeVO.setKnowPic(buf);
			}
		}
		
		// 去除BindingResult中upFiles欄位的FieldError紀錄
		result = removeFieldError(knowledgeVO, result, "knowPics");
		
		if (result.hasErrors()) {
			return "back-end/knowledge/addKnow";
		}

			/*************************** 2.開始新增資料 *****************************************/
			knowledgeSvc.addKnowledge(knowledgeVO);

			/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
			List<KnowledgeVO> list = knowledgeSvc.getAll();
			model.addAttribute("knowListData", list);
			model.addAttribute("success", "新增成功");
			return "redirect:/knowledge/listAllKnow";
	}
	

	// 單一新手知識修改
	@PostMapping("/getOneKnow_For_Update")
	public String getOneKnow_For_Update(@RequestParam("knowNo") String KnowNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		/*************************** 2.開始查詢資料 *****************************************/
		KnowledgeVO knowledgeVO = knowledgeSvc.getOneKnowledge(Integer.valueOf(KnowNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("knowledgeVO", knowledgeVO);
		return "back-end/knowledge/update_know_input"; // 查詢完成後轉交update_prod_input.html
	}

	// 修改新手知識
	@PostMapping("/updateKnow")
	public String update(@Valid KnowledgeVO knowledgeVO, BindingResult result, ModelMap model,
			@RequestParam("knowPics") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
	    // 設定修改當下的日期
	    java.sql.Date knowDate = new java.sql.Date(System.currentTimeMillis());
	    knowledgeVO.setKnowPublishDate(knowDate);

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄
		result = removeFieldError(knowledgeVO, result, "KnowPics");

		// 使用者未選擇要上傳的新圖片時
		if (parts[0].isEmpty()) { 
			byte[] KnowPics = knowledgeSvc.getOneKnowledge(knowledgeVO.getKnowNo()).getKnowPic();
			knowledgeVO.setKnowPic(KnowPics);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				knowledgeVO.setKnowPic(buf);
			}
		}
		if (result.hasErrors()) {

			return "back-end/knowledge/update_know_input";
		}
		/*************************** 2.開始修改資料 *****************************************/

		knowledgeSvc.updateKnowledge(knowledgeVO);
		
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "修改成功");
		knowledgeVO = knowledgeSvc.getOneKnowledge(Integer.valueOf(knowledgeVO.getKnowNo()));
		model.addAttribute("knowledgeVO", knowledgeVO);
		return "back-end/knowledge/listOneKnow"; // 修改成功後轉交listOneEmp.html
	}
	
	// 刪除新手知識
		@PostMapping("/deleteKnow")
		public String delete(@RequestParam("knowNo") String knowNo, ModelMap model) {
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

			/*************************** 2.開始刪除資料 *****************************************/

			knowledgeSvc.deleteKnowledgeVO(Integer.valueOf(knowNo));
			/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
			List<KnowledgeVO> list = knowledgeSvc.getAll();
			model.addAttribute("knowListData", list);
			model.addAttribute("success", "刪除成功");
			return "back-end/knowledge/listAllKnow";
		}

	// ============圖片錯誤訊息刪除用===========
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(KnowledgeVO knowledgeVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(knowledgeVO, "knowledgeVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	// ============資料先行裝配(重複利用)============

	// 新手知識
	@ModelAttribute("KnowledgeListData")
	protected List<KnowledgeVO> referenceListData3() {
		List<KnowledgeVO> list = knowledgeSvc.getAll();
		return list;
	}

}
