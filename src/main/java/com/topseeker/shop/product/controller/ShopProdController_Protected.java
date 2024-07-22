package com.topseeker.shop.product.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.info.model.ShopInfoService;
import com.topseeker.shop.product.model.ShopProductService;
import com.topseeker.shop.productpic.model.ShopProductPicService;
import com.topseeker.shop.producttype.model.ShopProductTypeService;
import com.topseeker.shop.wishlist.model.ShopWishlistService;
import com.topseeker.shop.wishlist.model.ShopWishlistVO;


@Controller
@RequestMapping("/protected/shop")
public class ShopProdController_Protected {

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
	

	// 商品收藏頁面用 wishlist.html
	@GetMapping("/wishlist")
	public String showwhishlist(HttpSession session, ModelMap model, String memNo) {

		// 抓取seesion內已登入會員的編號
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
//		if (loggedInMember == null) {
//			// 如果未登入，重定向到登入頁面
//			return "redirect:/member/loginMem";
//		}

		Integer loggedInMemberNo = loggedInMember.getMemNo();
		// 搜尋該會員有收藏的商品編號
		List<ShopWishlistVO> shopWishlistVO = shopWishlistSvc.showMemWishlist(loggedInMemberNo);

		model.addAttribute("shopWishlistVO", shopWishlistVO);
		return "front-end/shop/wishlist";
	}
	
}