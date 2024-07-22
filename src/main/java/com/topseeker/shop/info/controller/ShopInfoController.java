package com.topseeker.shop.info.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import com.topseeker.shop.info.model.ShopInfoService;
import com.topseeker.shop.info.model.ShopInfoVO;
import com.topseeker.shop.product.model.ShopProductVO;
import com.topseeker.shop.productpic.model.ShopProductPicVO;

@Controller
@RequestMapping("/shop")
public class ShopInfoController {

	// 商城最新消息用
	@Autowired
	ShopInfoService shopInfoSvc;

	// ============前端商城頁面============

	// 商城最新消息頁面 shopInfo.html
	@GetMapping("/shopInfo")
	public String showShopInfo(ModelMap model) {
		List<ShopInfoVO> shopInfoListData = shopInfoSvc.getAll();

		model.addAttribute("shopInfoListData", shopInfoListData);

		return "front-end/shop/info/shopInfo";
	}


	// ============後端管理頁面============

	// 列出所有最新消息明細
	@GetMapping("/shopManagement/listAllShopInfo")
	public String listAllShopInfo(Model model) {
	
	List<ShopInfoVO> list = shopInfoSvc.getAll();
	model.addAttribute("shopInfoListData", list);

	
		return "back-end/shop/info/listAllShopInfo";
	}
	
	

	// ============Ajax新增刪除功能============
	
	
	// 依照商品編號更改上下架狀態
	@PostMapping("/shopManagement/updateInfoStatus")
	public ResponseEntity<?> updateStatus(@RequestParam Integer infoNo, @RequestParam Integer infoStatus) {
		shopInfoSvc.updateInfoStatus(infoNo, infoStatus);
		return ResponseEntity.ok().build();
	}

//	// 依照最新消息編號更改上下架狀態
//	@PostMapping("/shopManagement/updateInfoStatus")
//	public ResponseEntity<?> updateStatus(@RequestParam Integer infoNo, @RequestParam Integer InfoStatus) {
//		shopInfoSvc.updateInfoStatus(infoNo, InfoStatus);
//		return ResponseEntity.ok().build();
//	}

	// ============後端最新消息CRUD============

	// 最新消息新增 addInfo.html
	@GetMapping("/shopManagement/addInfo")
	public String addShopInfo(ModelMap model) {
		ShopInfoVO shopInfoVO = new ShopInfoVO();

	    // 設定當下的日期
	    java.sql.Date infoDate = new java.sql.Date(System.currentTimeMillis());
	    shopInfoVO.setInfoDate(infoDate);

		// 預設下架
		shopInfoVO.setInfoStatus(0);

		model.addAttribute("shopInfoVO", shopInfoVO);
		return "back-end/shop/info/addInfo";
	}

	// 最新消息新增(含圖片)
	@PostMapping("/shopManagement/insertInfo")
	public String insert(@Valid ShopInfoVO shopInfoVO, BindingResult result, ModelMap model,
			@RequestParam("infoPics") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/


		// 使用者未選擇要上傳的圖片時
		if (parts[0].isEmpty()) { 
			model.addAttribute("errorMessage", "最新消息圖片: 請上傳圖片");
			shopInfoVO.setInfoPic(null);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				shopInfoVO.setInfoPic(buf);
			}
		}
		
		// 去除BindingResult中upFiles欄位的FieldError紀錄
		result = removeFieldError(shopInfoVO, result, "infoPics");
		
		if (result.hasErrors()) {
			return "back-end/shop/info/addInfo";
		}

			/*************************** 2.開始新增資料 *****************************************/
			shopInfoSvc.addShopInfo(shopInfoVO);

			/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
			List<ShopInfoVO> list = shopInfoSvc.getAll();
			model.addAttribute("shopInfoListData", list);
			model.addAttribute("success", "新增成功");

			return "redirect:/shop/shopManagement/listAllShopInfo";
	}
	

	// 單一最新消息修改
	@PostMapping("/shopManagement/getOneInfo_For_Update")
	public String getOneInfo_For_Update(@RequestParam("infoNo") String infoNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		/*************************** 2.開始查詢資料 *****************************************/
		ShopInfoVO shopInfoVO = shopInfoSvc.getOneShopInfo(Integer.valueOf(infoNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("shopInfoVO", shopInfoVO);
		return "back-end/shop/info/update_Info_input"; // 查詢完成後轉交update_prod_input.html
	}

	// 修改最新消息
	@PostMapping("/shopManagement/updateInfo")
	public String update(@Valid ShopInfoVO shopInfoVO, BindingResult result, ModelMap model,
			@RequestParam("infoPics") MultipartFile[] parts) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
	    // 設定修改當下的日期
	    java.sql.Date infoDate = new java.sql.Date(System.currentTimeMillis());
	    shopInfoVO.setInfoDate(infoDate);

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄
		result = removeFieldError(shopInfoVO, result, "infoPics");

		// 使用者未選擇要上傳的新圖片時
		if (parts[0].isEmpty()) { 
			byte[] infoPics = shopInfoSvc.getOneShopInfo(shopInfoVO.getInfoNo()).getInfoPic();
			shopInfoVO.setInfoPic(infoPics);
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				shopInfoVO.setInfoPic(buf);
			}
		}
		if (result.hasErrors()) {
			return "back-end/shop/info/update_info_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		
		shopInfoSvc.updateShopInfo(shopInfoVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "修改成功");
		shopInfoVO = shopInfoSvc.getOneShopInfo(Integer.valueOf(shopInfoVO.getInfoNo()));
		model.addAttribute("shopInfoVO", shopInfoVO);
		return "back-end/shop/info/listOneInfo";
	}

	// ============圖片錯誤訊息刪除用===========
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ShopInfoVO shopInfoVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(shopInfoVO, "shopInfoVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	// ============資料先行裝配(重複利用)============

	// 商城最新消息
	@ModelAttribute("shopInfoListData")
	protected List<ShopInfoVO> referenceListData3() {
		List<ShopInfoVO> list = shopInfoSvc.getAll();
		return list;
	}

}
