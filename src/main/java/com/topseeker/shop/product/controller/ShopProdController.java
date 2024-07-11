package com.topseeker.shop.product.controller;


import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
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
	
	//商品用
	@Autowired
	ShopProductService shopProductSvc;
	
	//商品類別用	
	@Autowired
	ShopProductTypeService shopProductTypeSvc;
	
	//商品圖片用	
	@Autowired
	ShopProductPicService shopProductPicSvc;
	
	//會員資料用
	@Autowired
	MemberService memSvc;
	
	@Autowired
	ShopWishlistService shopWishlistSvc;
	
	
	//============前端商城頁面============
	
	//商城首頁 hompage.html
    @GetMapping("/homepage")
	public String showShopProduct(ModelMap model) {
	List<ShopProductVO> shopListData = shopProductSvc.getAll();
	
	model.addAttribute("shopListData", shopListData);
	
	return "front-end/shop/homepage";
    }
    
	//商城商品詳細頁面 hompage.html
    @GetMapping("/listOneProdDetail")
	public String showShopProductDetail(@RequestParam String prodNo, ModelMap model) {
    		
	ShopProductVO shopProductVO = shopProductSvc.getOneShopProduct(Integer.valueOf(prodNo));
	
	model.addAttribute("shopProductVO", shopProductVO);
	
	return "front-end/shop/listOneProdDetail";
    }
    
    
     //商品收藏頁面用 wishlist.html
    @GetMapping("/wishlist")
	public String showwhishlist(HttpSession session, ModelMap model, String memNo) {
    
    //抓取seesion內已登入會員的編號
    MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
	Integer loggedInMemberNo = loggedInMember.getMemNo();
	//搜尋該會員有收藏的商品編號
	List<ShopWishlistVO> shopWishlistVO = shopWishlistSvc.showMemWishlist(loggedInMemberNo);
	
	model.addAttribute("shopWishlistVO", shopWishlistVO);
	return "front-end/shop/wishlist";
    }
    
  

	//============後端管理頁面============
	
	//後台首頁 management_index.html
    @GetMapping("shopManagement")
	public String showShopManagement(ModelMap model) {
	return "back-end/shop/management_index";
    }
	
    //商品查詢選單
    @GetMapping("shopManagement/select_page")
	public String select_page(Model model) {
		return "back-end/shop/select_page_prod";
	}
	
    //列出所有商品明細
    @GetMapping("/shopManagement/listAllProd")
	public String listAllProd(Model model) {
		return "back-end/shop/listAllProd";
	}
    
    //============後端商品CRUD============
    
	//商品新增 addProd.html
	@GetMapping("/shopManagement/addProd")
	public String addShopProduct(ModelMap model) {
		ShopProductVO  shopProductVO = new ShopProductVO();
		
		//設定建立時間
		Timestamp prodDate = new Timestamp(System.currentTimeMillis());
		shopProductVO.setProdDate(prodDate);
		
		//預設下架
		shopProductVO.setProdStatus(0);
		
		
		model.addAttribute("shopProductVO", shopProductVO);
		return "back-end/shop/addProd";
	}
	

	
	//商品新增(含圖片)
	@PostMapping("/shopManagement/insert")
	public String insert(@Valid ShopProductVO shopProductVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts) throws IOException {
		
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
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
		if (result.hasErrors() || parts[0].isEmpty()) {
			return "back-end/shop/addProd";
		}
		
		
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
		
	//單項商品修改
	@PostMapping("/shopManagement/getOne_For_Update")
	public String getOne_For_Update(@RequestParam("prodNo") String prodNo, ModelMap model) {
	/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
	/*************************** 2.開始查詢資料 *****************************************/
	ShopProductVO shopProductVO = shopProductSvc.getOneShopProduct(Integer.valueOf(prodNo));
	
	/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
	model.addAttribute("shopProductVO", shopProductVO);
	return "back-end/shop/update_prod_input"; // 查詢完成後轉交update_prod_input.html
	}
	
	//修改商品
	@PostMapping("/shopManagement/update")
	public String update(@Valid ShopProductVO shopProductVO, BindingResult result, ModelMap model,
			@RequestParam("picSet") MultipartFile[] parts) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
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
	
	//刪除商品
	@PostMapping("/shopManagement/delete")
	public String delete(@RequestParam("prodNo") String prodNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		shopProductSvc.deletShopProduct(Integer.valueOf(prodNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<ShopProductVO> list =shopProductSvc.getAll();
		model.addAttribute("shopListData", list);
		model.addAttribute("success", "刪除成功");
		return "back-end/shop/listAllProd"; // 刪除完成後轉交listAllProd.html
	}
	


    
    
	//============圖片錯誤訊息刪除用===========
	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(ShopProductVO shopProductVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(shopProductVO, "shopProductVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
    
	//============資料先行裝配(重複利用)============
	
	//商品
    @ModelAttribute("shopListData")
	protected List<ShopProductVO> referenceListData(Model model) {
    	List<ShopProductVO> list = shopProductSvc.getAll();
    	
		return list;
	}
	
	//商品類型
	@ModelAttribute("shopProdTypeListData")
	protected List<ShopProductTypeVO> referenceListData() {
		List<ShopProductTypeVO> list = shopProductTypeSvc.getAll();
		
		return list;
	}
	
	//商品圖片
	@ModelAttribute("shopProdPicListData")
	protected List<ShopProductPicVO> referenceListData2() {
		List<ShopProductPicVO> list = shopProductPicSvc.getAll();
		
		return list;
	}
	

}
