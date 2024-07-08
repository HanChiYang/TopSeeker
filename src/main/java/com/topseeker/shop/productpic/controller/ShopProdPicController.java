package com.topseeker.shop.productpic.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.shop.product.model.ShopProductService;
import com.topseeker.shop.product.model.ShopProductVO;
import com.topseeker.shop.productpic.model.ShopProductPicService;
import com.topseeker.shop.productpic.model.ShopProductPicVO;

@Controller
@RequestMapping("/shop/prodpic")
public class ShopProdPicController {

	// 商品圖片用
	@Autowired
	ShopProductPicService shopProductPicSvc;

	// 商品用
	@Autowired
	ShopProductService shopProductSvc;

	/*
	 * addProdPic.html頁面用
	 */
	@GetMapping("addProdPic")
	public String addShopProductPic(ModelMap model) {
		ShopProductPicVO shopProductPicVO = new ShopProductPicVO();

		model.addAttribute("shopProductPicVO", shopProductPicVO);
		return "back-end/shop/addProdPic";
	}

	/*
	 * This method will be called on addShopProduct.html form submission, handling
	 * POST request It also validates the user input
	 */

	@PostMapping("insert")
	public String insert(@Valid ShopProductPicVO shopProductPicVO, BindingResult result, ModelMap model,
			@RequestParam("prodPic") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(shopProductPicVO, result, "prodPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "員工照片: 請上傳照片"); // --> 由messages.properties 第五行處理錯誤信息
		} else {
			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();
				shopProductPicVO.setProdPic(buf);
			}
		}
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/shop/addProdPic";
		}
		if (result.hasErrors()) {
			// 如果有驗證錯誤，處理錯誤
			model.addAttribute("errorMessage", "資料未填寫完整，請重新檢查");
			return "back-end/shop/addProd"; // 返回錯誤頁面或者其他處理方式
		}
		/*************************** 2.開始新增資料 *****************************************/
		shopProductPicSvc.addShopProductPic(shopProductPicVO);

		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/

		List<ShopProductPicVO> list = shopProductPicSvc.getAll();
		model.addAttribute("shopProdPicListData", list);
		model.addAttribute("success", "- (新增成功)");
		return "redirect:/shop/listAllProdPic"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/shop/listShopProduct")
	}

	/*
	 * This method will be called on listAllProd.html form submission, handling POST
	 * request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("prodPicNo") String prodPicNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		List<ShopProductPicVO> shopProductPicVO = shopProductPicSvc.getAllProductPic(Integer.valueOf(prodPicNo));
		// System.out.println("getOneForUpdateeeee");
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("shopProductPicVO", shopProductPicVO);
		return "back-end/shop/update_prodpic_input"; // 查詢完成後轉交update_prod_input.html
	}

	/*
	 * This method will be called on update_Prod_input.html form submission,
	 * handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid ShopProductPicVO shopProductPicVO, ModelMap model) {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始修改資料 *****************************************/
		shopProductPicSvc.updateShopProductPic(shopProductPicVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		shopProductPicVO = (@Valid ShopProductPicVO) shopProductPicSvc
				.getOneShopProductPic(Integer.valueOf(shopProductPicVO.getProdPicNo()));
		model.addAttribute("shopProductPicVO", shopProductPicVO);
		return "back-end/shop/listOneProdPic"; // 修改成功後轉交listOneProd.html
	}

	/*
	 * This method will be called on listAllProd.html form submission, handling POST
	 * request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("prodPicNo") String prodPicNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		shopProductPicSvc.deleteShopProductPic(Integer.valueOf(prodPicNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ShopProductPicVO> list = shopProductPicSvc.getAll();
		model.addAttribute("shopProdPicListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/shop/listAllProdPic"; // 刪除完成後轉交listAllProd.html
	}

	// 刪除特定圖片
//	@PostMapping("delete")
//	public String delete(@RequestParam("prodPicNo") String prodPicNo, ModelMap model) {
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
//		
//		/*************************** 2.開始刪除資料 *****************************************/
//		// EmpService empSvc = new EmpService();
//		shopProductPicSvc.deletShopProductPic(Integer.valueOf(prodPicNo));
//		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
//		List<ShopProductPicVO> list =shopProductPicSvc.getAll();
//		model.addAttribute("shopProdPicListData", list);
//		model.addAttribute("success", "- (刪除成功)");
//		return "back-end/shop/listAllProdPic"; // 刪除完成後轉交listAllProd.html
//	}

	@PostMapping("/deleteProductImage")
	public Map<String, Object> deleteProductImage(@RequestBody Map<String, String> payload) {
		Map<String, Object> response = new HashMap<>();
		try {
			String prodPicNo = payload.get("prodPicNo");
			System.out.println(payload);
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			if (prodPicNo == null || prodPicNo.isEmpty()) {
				response.put("success", false);
				response.put("message", "商品圖片編號無效");
				return response;
			}
			/*************************** 2.開始刪除資料 *****************************************/
			boolean success = shopProductPicSvc.deleteShopProductPic(Integer.valueOf(prodPicNo));

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
		System.out.println("Response: " + response); // 調試輸出
		return response;
	}

	@ModelAttribute("shopProdPicListData")
	protected List<ShopProductPicVO> referenceListData2() {
		// DeptService deptSvc = new DeptService();
		List<ShopProductPicVO> list = shopProductPicSvc.getAll();
		return list;
	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ShopProductPicVO shopProductPicVO, BindingResult result,
			String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname)).collect(Collectors.toList());
		result = new BeanPropertyBindingResult(shopProductPicVO, "shopProductPicVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}

	// =========== 以下第63~75行是提供給
	// /src/main/resources/templates/back-end/emp/select_page.html 與 listAllEmp.html
	// 要使用的資料 ===================
	@GetMapping("/select_page")
	public String select_page(Model model) {
		return "back-end/shop/select_page";
	}

	@GetMapping("/listAllProdPic")
	public String listAllProdPic(Model model) {
		return "back-end/shop/listAllProd";
	}

	@ModelAttribute("shopListData")
	protected List<ShopProductVO> referenceListData(Model model) {

		List<ShopProductVO> list = shopProductSvc.getAll();
		return list;
	}

}
