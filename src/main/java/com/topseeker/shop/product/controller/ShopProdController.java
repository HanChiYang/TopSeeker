package com.topseeker.shop.product.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.info.model.ShopInfoService;
import com.topseeker.shop.info.model.ShopInfoVO;
import com.topseeker.shop.product.model.ShopProductService;
import com.topseeker.shop.product.model.ShopProductVO;
import com.topseeker.shop.productpic.model.ShopProductPicService;
import com.topseeker.shop.productpic.model.ShopProductPicVO;
import com.topseeker.shop.producttype.model.ShopProductTypeService;
import com.topseeker.shop.producttype.model.ShopProductTypeVO;
import com.topseeker.shop.wishlist.model.ShopWishlistService;
import com.topseeker.shop.wishlist.model.ShopWishlistVO;

@Controller
@RequestMapping("/shop")
public class ShopProdController {

	// 商品用
	@Autowired
	ShopProductService shopProductSvc;

	// 商品類別用
	@Autowired
	ShopProductTypeService shopProductTypeSvc;

	// 商品圖片用
	@Autowired
	ShopProductPicService shopProductPicSvc;

	// 會員資料用
	@Autowired
	MemberService memSvc;

	// 商品收藏用
	@Autowired
	ShopWishlistService shopWishlistSvc;
	
	// 商城最新消息用
	@Autowired
	ShopInfoService shopInfoSvc;

	// ============前端商城頁面============

	// 商城首頁 hompage.html
	@GetMapping("/homepage")
	public String showShopProduct(ModelMap model) {
		List<ShopProductVO> shopListData = shopProductSvc.getAllReleasedProd();
		List<ShopInfoVO> shopInfoListData = shopInfoSvc.getAllReleasedInfo();
	
		model.addAttribute("shopListData", shopListData);
		model.addAttribute("shopInfoListData", shopInfoListData);

		return "front-end/shop/homepage";
	}

	// 商城商品分類頁面 listProdByType.html
	@GetMapping("/category/{categoryName}")
//    public String showShopProductByType(@PathVariable("prodTypeNo") int prodTypeNo, ModelMap model) {
	public String showShopProductByType(@PathVariable String categoryName, @RequestParam int prodTypeNo,
			ModelMap model) {

		List<ShopProductVO> shopProdTypeListData = shopProductSvc.findByProdTypeNo(prodTypeNo);
		model.addAttribute("shopProdTypeListData", shopProdTypeListData);

		ShopProductTypeVO productType = shopProductTypeSvc.getOneShopProductType(prodTypeNo);
		model.addAttribute("productType", productType);

		return "front-end/shop/listProdByType";
	}

	// 商城商品詳細頁面 hompage.html
	@GetMapping("/listOneProdDetail")
	public String showShopProductDetail(@RequestParam String prodNo, ModelMap model) {

		ShopProductVO shopProductVO = shopProductSvc.getOneShopProduct(Integer.valueOf(prodNo));

		model.addAttribute("shopProductVO", shopProductVO);

		return "front-end/shop/listOneProdDetail";
	}
	
	
	// 商品搜尋結果用 searchResult.html
	@PostMapping("/searchResult")
	 public String showShopSearchResult(@RequestParam("keyword") String keyword, ModelMap model) {
        // 加入模糊搜尋符號
        String searchKeyword = "%" + keyword + "%";
        List<ShopProductVO> shopListData = shopProductSvc.findByProdNameOrInfo(searchKeyword);

        model.addAttribute("shopListData", shopListData);

		return "front-end/shop/searchResult";
	}
	
	

	// ============Ajax新增刪除功能============

	// 商品頁面，檢查商品是否已被收藏
	@PostMapping("/checkWishlistStatus")
	@ResponseBody
	public Map<String, Object> checkWishlistStatus(@RequestBody Map<String, Object> request, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {
			Integer prodNo = null;
			if (request.get("prodNo") instanceof String) {
				prodNo = Integer.valueOf((String) request.get("prodNo"));
			} else if (request.get("prodNo") instanceof Integer) {
				prodNo = (Integer) request.get("prodNo");
			}

			if (prodNo == null) {
				response.put("isInWishlist", false);
				response.put("message", "無效的商品編號");
				return response;
			}

			MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");

			if (loggedInMember == null) {
				response.put("isInWishlist", false);
				response.put("message", "請先登入");
				return response;
			}

			Integer memNo = loggedInMember.getMemNo();
			ShopWishlistVO wishlistItem = shopWishlistSvc.findByMemNoAndProdNo(memNo, prodNo);
			response.put("isInWishlist", wishlistItem != null);

//            System.out.println("會員編號是:" + memNo + ", 收藏狀態是: " + (wishlistItem != null));

		} catch (Exception e) {
			response.put("isInWishlist", false);
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// 商品頁面，收藏清單中新增或收藏該商品
	@PostMapping("/toggleWishlist")
	@ResponseBody
	public Map<String, Object> toggleWishlist(@RequestBody Map<String, Object> request, HttpSession session) {
		Map<String, Object> response = new HashMap<>();
		try {

			// 獲取商品編號並進行類型轉換
			Integer prodNo = null;
			if (request.get("prodNo") instanceof String) {
				prodNo = Integer.valueOf((String) request.get("prodNo"));
			} else if (request.get("prodNo") instanceof Integer) {
				prodNo = (Integer) request.get("prodNo");
			}

			// 確認商品編號是否成功解析
			if (prodNo == null) {
				response.put("success", false);
				response.put("message", "無效的商品編號");
				return response;
			}
			// 確認會員是否登入
			MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
			if (loggedInMember == null) {
				response.put("success", false);
				response.put("message", "請先登入");
				return response;
			}

			Integer memNo = loggedInMember.getMemNo();

			// 檢查是否已經收藏
			ShopWishlistVO existingWishlistItem = shopWishlistSvc.findByMemNoAndProdNo(memNo, prodNo);

			if (existingWishlistItem != null) {
				// 已存在，執行刪除
				shopWishlistSvc.deletShopWishlist(existingWishlistItem.getWishlistNo());
				response.put("action", "removed");
			} else {
				// 不存在，執行新增
				// 不存在，執行新增
				ShopWishlistVO newWishlistItem = new ShopWishlistVO();

				// 假設你有相應的服務層來獲取MemberVO和ShopProductVO對象
				MemberVO memberVO = memSvc.getOneMem(memNo);
				ShopProductVO shopProductVO = shopProductSvc.getOneShopProduct(prodNo);

				newWishlistItem.setMemberVO(memberVO);
				newWishlistItem.setShopProductVO(shopProductVO);

				shopWishlistSvc.addShopWishlist(newWishlistItem);
				response.put("action", "added");
			}

			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
		}
		return response;
	}

	// 商品收藏頁面，刪除收藏商品 whishlist.html
	@PostMapping("/removeWishlistProd")
	@ResponseBody
	public Map<String, Object> removeWishlistProd(@RequestBody Map<String, Object> request) {
		Map<String, Object> response = new HashMap<>();
		try {
			Integer wishlistNo = (Integer) request.get("wishlistNo");
			// 呼叫服務層方法來移除收藏的商品
			shopWishlistSvc.deletShopWishlist(wishlistNo);
			response.put("success", true);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
		}
		return response;
	}

	// ============後端管理頁面============

	// 後台首頁 management_index.html
	@GetMapping("shopManagement")
	public String showShopManagement(ModelMap model) {
		return "back-end/shop/management_index";
	}

	// 商品查詢選單
	@GetMapping("shopManagement/select_page")
	public String select_page(Model model) {
		return "back-end/shop/select_page_prod";
	}

	// 列出所有商品明細
	@GetMapping("/shopManagement/listAllProd")
	public String listAllProd(Model model) {
		return "back-end/shop/listAllProd";
	}
	

	// ============Ajax新增刪除功能============

	// 依照商品編號更改上下架狀態
	@PostMapping("/shopManagement/updateStatus")
	public ResponseEntity<?> updateStatus(@RequestParam Integer prodNo, @RequestParam Integer prodStatus) {
		shopProductSvc.updateProdStatus(prodNo, prodStatus);
		return ResponseEntity.ok().build();
	}

	// ============後端商品CRUD============

	// 商品新增 addProd.html
	@GetMapping("/shopManagement/addProd")
	public String addShopProduct(ModelMap model) {
		ShopProductVO shopProductVO = new ShopProductVO();

		// 設定建立時間
		Timestamp prodDate = new Timestamp(System.currentTimeMillis());
		shopProductVO.setProdDate(prodDate);

		// 預設下架
		shopProductVO.setProdStatus(0);

		model.addAttribute("shopProductVO", shopProductVO);
		return "back-end/shop/addProd";
	}

	// 商品新增(含圖片)
	@PostMapping("/shopManagement/insert")
	public String insert(@Valid ShopProductVO shopProductVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
//		result = removeFieldError(shopProductVO, result, "prodPic");

		// 使用者未選擇要上傳的圖片時
		if (parts[0].isEmpty()) { 
//			model.addAttribute("errorMessage", "商品圖片: 請上傳圖片");

			shopProductSvc.addShopProduct(shopProductVO);

			List<ShopProductVO> list = shopProductSvc.getAll();
			model.addAttribute("shopListData", list);
			model.addAttribute("success", "新增成功");
			return "redirect:/shop/shopManagement/listAllProd";

		} else {
			List<ShopProductPicVO> picSet = new ArrayList<>();

			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();

				ShopProductPicVO shopProductPicVO = new ShopProductPicVO();
				shopProductPicVO.setProdPic(buf);
				shopProductPicVO.setShopProductVO(shopProductVO); // 設置關聯到商品

				picSet.add(shopProductPicVO); // 添加到集合中
//		    }
				shopProductVO.setShopProductPics(picSet); // 設置商品的圖片集合
			}
//		if (result.hasErrors() || parts[0].isEmpty()) {
//			return "back-end/shop/addProd";
//		}

//		if (result.hasErrors()) {
//	        // 如果有驗證錯誤，處理錯誤
//	        model.addAttribute("errorMessage", "資料未填寫完整，請重新檢查");
//	        return "back-end/shop/addProd"; // 返回錯誤頁面或者其他處理方式
//	    }
			/*************************** 2.開始新增資料 *****************************************/
			shopProductSvc.addShopProduct(shopProductVO);

			/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
			List<ShopProductVO> list = shopProductSvc.getAll();
			model.addAttribute("shopListData", list);
			model.addAttribute("success", "新增成功");
			return "redirect:/shop/shopManagement/listAllProd"; // 新增成功後重導至IndexController_inSpringBoot.java的第58行@GetMapping("/shop/listShopProduct")

		}
	}

	// 單項商品修改
	@PostMapping("/shopManagement/getOne_For_Update")
	public String getOne_For_Update(@RequestParam("prodNo") String prodNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		/*************************** 2.開始查詢資料 *****************************************/
		ShopProductVO shopProductVO = shopProductSvc.getOneShopProduct(Integer.valueOf(prodNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("shopProductVO", shopProductVO);
		return "back-end/shop/update_prod_input"; // 查詢完成後轉交update_prod_input.html
	}

	// 修改商品
	@PostMapping("/shopManagement/update")
	public String update(@Valid ShopProductVO shopProductVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		// 設定修改時間
		Timestamp prodDate = new Timestamp(System.currentTimeMillis());
		shopProductVO.setProdDate(prodDate);

		// 去除BindingResult中upFiles欄位的FieldError紀錄 --> 見第172行
		result = removeFieldError(shopProductVO, result, "prodPic");

		if (parts[0].isEmpty()) { // 使用者未選擇要上傳的圖片時
			model.addAttribute("errorMessage", "商品圖片: 請上傳圖片");
		} else {
			List<ShopProductPicVO> picSet = new ArrayList<>();

			for (MultipartFile multipartFile : parts) {
				byte[] buf = multipartFile.getBytes();

				ShopProductPicVO shopProductPicVO = new ShopProductPicVO();
				shopProductPicVO.setProdPic(buf);
				shopProductPicVO.setShopProductVO(shopProductVO); // 設置關聯到商品

				picSet.add(shopProductPicVO); // 添加到集合中
			}
			shopProductVO.setShopProductPics(picSet); // 設置商品的圖片集合
		}

		/*************************** 2.開始修改資料 *****************************************/
		shopProductSvc.updateShopProduct(shopProductVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "修改成功");
		shopProductVO = shopProductSvc.getOneShopProduct(Integer.valueOf(shopProductVO.getProdNo()));
		model.addAttribute("shopProductVO", shopProductVO);
		return "back-end/shop/listOneProd"; // 修改成功後轉交listOneProd.html
	}

	// 刪除商品
	@PostMapping("/shopManagement/delete")
	public String delete(@RequestParam("prodNo") String prodNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		shopProductSvc.deletShopProduct(Integer.valueOf(prodNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ShopProductVO> list = shopProductSvc.getAll();
		model.addAttribute("shopListData", list);
		model.addAttribute("success", "刪除成功");
		return "back-end/shop/listAllProd"; // 刪除完成後轉交listAllProd.html
	}
	
	// ============後端最新消息CRUD============
	
	
	

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

	// 商品
	@ModelAttribute("shopListData")
	protected List<ShopProductVO> referenceListData(Model model) {
		List<ShopProductVO> list = shopProductSvc.getAll();

		return list;
	}

	// 商品類型
	@ModelAttribute("shopProdTypeListData")
	protected List<ShopProductTypeVO> referenceListData() {
		List<ShopProductTypeVO> list = shopProductTypeSvc.getAll();

		return list;
	}

	// 商品圖片
	@ModelAttribute("shopProdPicListData")
	protected List<ShopProductPicVO> referenceListData2() {
		List<ShopProductPicVO> list = shopProductPicSvc.getAll();

		return list;
	}


}
