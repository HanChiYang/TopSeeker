package com.topseeker.shopinfo.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.topseeker.shop.product.model.ShopProductVO;
import com.topseeker.shopinfo.model.ShopInfoService;
import com.topseeker.shopinfo.model.ShopInfoVO;

@Controller
@RequestMapping("/shop")
public class ShopInfoController {

	// 商城最新消息用
	@Autowired
	ShopInfoService shopInfoSvc;

	// ============前端商城頁面============

//	// 商城首頁 hompage.html
//	@GetMapping("/homepage")
//	public String showShopProduct(ModelMap model) {
//		List<ShopProductVO> shopListData = shopProductSvc.getAllReleasedProd();
//
//		model.addAttribute("shopListData", shopListData);
//
//		return "front-end/shop/homepage";
//	}

//	// 商城商品分類頁面 listProdByType.html
//	@GetMapping("/category/{categoryName}")
////    public String showShopProductByType(@PathVariable("prodTypeNo") int prodTypeNo, ModelMap model) {
//	public String showShopProductByType(@PathVariable String categoryName, @RequestParam int prodTypeNo,
//			ModelMap model) {
//
//		List<ShopProductVO> shopProdTypeListData = shopProductSvc.findByProdTypeNo(prodTypeNo);
//		model.addAttribute("shopProdTypeListData", shopProdTypeListData);
//
//		ShopProductTypeVO productType = shopProductTypeSvc.getOneShopProductType(prodTypeNo);
//		model.addAttribute("productType", productType);
//
//		return "front-end/shop/listProdByType";
//	}



	// ============Ajax新增刪除功能============


	

	// ============後端管理頁面============

	// 列出所有最新消息明細
	@GetMapping("/shopManagement/listAllShopInfo")
	public String listAllShopInfo(Model model) {
	
    System.out.println("有經過controller!");
	List<ShopInfoVO> list = shopInfoSvc.getAll();
	model.addAttribute("shopInfoListData", list);

	
		return "back-end/shop/info/listAllShopInfo";
	}
	
	
//	/shopInfo/getOne_Info_For_Update
	

	// ============Ajax新增刪除功能============

//	// 依照最新消息編號更改上下架狀態
//	@PostMapping("/shopManagement/updateInfoStatus")
//	public ResponseEntity<?> updateStatus(@RequestParam Integer infoNo, @RequestParam Integer InfoStatus) {
//		shopInfoSvc.updateInfoStatus(infoNo, InfoStatus);
//		return ResponseEntity.ok().build();
//	}

	// ============後端最新消息CRUD============
	
	
	

	
	
	// ============後端商品CRUD============

//	// 商品新增 addProd.html
//	@GetMapping("/shopManagement/addProd")
//	public String addShopProduct(ModelMap model) {
//		ShopProductVO shopProductVO = new ShopProductVO();
//
//		// 設定建立時間
//		Timestamp prodDate = new Timestamp(System.currentTimeMillis());
//		shopProductVO.setProdDate(prodDate);
//
//		// 預設下架
//		shopProductVO.setProdStatus(0);
//
//		model.addAttribute("shopProductVO", shopProductVO);
//		return "back-end/shop/addProd";
//	}
//
//	// 商品新增(含圖片)
//	@PostMapping("/shopManagement/insert")
//	public String insert(@Valid ShopProductVO shopProductVO, BindingResult result, ModelMap model,
//			@RequestParam("picSet") MultipartFile[] parts) throws IOException {
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
////		result = removeFieldError(shopProductVO, result, "prodPic");
//
//		// 使用者未選擇要上傳的圖片時
//		if (parts[0].isEmpty()) { 
////			model.addAttribute("errorMessage", "商品圖片: 請上傳圖片");
//
//			shopProductSvc.addShopProduct(shopProductVO);
//
//			List<ShopProductVO> list = shopProductSvc.getAll();
//			model.addAttribute("shopListData", list);
//			model.addAttribute("success", "新增成功");
//			return "redirect:/shop/shopManagement/listAllProd";
//
//		} else {
//			List<ShopProductPicVO> picSet = new ArrayList<>();
//
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//
//				ShopProductPicVO shopProductPicVO = new ShopProductPicVO();
//				shopProductPicVO.setProdPic(buf);
//				shopProductPicVO.setShopProductVO(shopProductVO); // 設置關聯到商品
//
//				picSet.add(shopProductPicVO); // 添加到集合中
////		    }
//				shopProductVO.setShopProductPics(picSet); // 設置商品的圖片集合
//			}
////		if (result.hasErrors() || parts[0].isEmpty()) {
////			return "back-end/shop/addProd";
////		}
//
////		if (result.hasErrors()) {
////	        // 如果有驗證錯誤，處理錯誤
////	        model.addAttribute("errorMessage", "資料未填寫完整，請重新檢查");
////	        return "back-end/shop/addProd"; // 返回錯誤頁面或者其他處理方式
////	    }
//			/*************************** 2.開始新增資料 *****************************************/
//			shopProductSvc.addShopProduct(shopProductVO);
//
//			/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
//			List<ShopProductVO> list = shopProductSvc.getAll();
//			model.addAttribute("shopListData", list);
//			model.addAttribute("success", "新增成功");
//			return "redirect:/shop/shopManagement/listAllProd"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/shop/listShopProduct")
//
//		}
//	}
//
//	// 單項商品修改
//	@PostMapping("/shopManagement/getOne_For_Update")
//	public String getOne_For_Update(@RequestParam("prodNo") String prodNo, ModelMap model) {
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//
//		/*************************** 2.開始查詢資料 *****************************************/
//		ShopProductVO shopProductVO = shopProductSvc.getOneShopProduct(Integer.valueOf(prodNo));
//
//		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("shopProductVO", shopProductVO);
//		return "back-end/shop/update_prod_input"; // 查詢完成後轉交update_prod_input.html
//	}
//
//	// 修改商品
//	@PostMapping("/shopManagement/update")
//	public String update(@Valid ShopProductVO shopProductVO, BindingResult result, ModelMap model,
//			@RequestParam("picSet") MultipartFile[] parts) throws IOException {
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		// 設定修改時間
//		Timestamp prodDate = new Timestamp(System.currentTimeMillis());
//		shopProductVO.setProdDate(prodDate);
//
//		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(shopProductVO, result, "prodPic");
//
//		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
//			model.addAttribute("errorMessage", "商品圖片: 請上傳圖片");
//		} else {
//			List<ShopProductPicVO> picSet = new ArrayList<>();
//
//			for (MultipartFile multipartFile : parts) {
//				byte[] buf = multipartFile.getBytes();
//
//				ShopProductPicVO shopProductPicVO = new ShopProductPicVO();
//				shopProductPicVO.setProdPic(buf);
//				shopProductPicVO.setShopProductVO(shopProductVO); // 設置關聯到商品
//
//				picSet.add(shopProductPicVO); // 添加到集合中
//			}
//			shopProductVO.setShopProductPics(picSet); // 設置商品的圖片集合
//		}
//
//		/*************************** 2.開始修改資料 *****************************************/
//		shopProductSvc.updateShopProduct(shopProductVO);
//
//		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
//		model.addAttribute("success", "修改成功");
//		shopProductVO = shopProductSvc.getOneShopProduct(Integer.valueOf(shopProductVO.getProdNo()));
//		model.addAttribute("shopProductVO", shopProductVO);
//		return "back-end/shop/listOneProd"; // 修改成功後轉交listOneProd.html
//	}
//
//	// 刪除商品
//	@PostMapping("/shopManagement/delete")
//	public String delete(@RequestParam("prodNo") String prodNo, ModelMap model) {
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//
//		/*************************** 2.開始刪除資料 *****************************************/
//		// EmpService empSvc = new EmpService();
//		shopProductSvc.deletShopProduct(Integer.valueOf(prodNo));
//		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
//		List<ShopProductVO> list = shopProductSvc.getAll();
//		model.addAttribute("shopListData", list);
//		model.addAttribute("success", "刪除成功");
//		return "back-end/shop/listAllProd"; // 刪除完成後轉交listAllProd.html
//	}
	

	// ============圖片錯誤訊息刪除用===========
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ShopProductVO shopProductVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(shopProductVO, "shopProductVO");
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
