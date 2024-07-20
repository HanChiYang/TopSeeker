package com.topseeker.shop.cart.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.cart.model.CartService;
import com.topseeker.shop.cart.model.CartVO;
import com.topseeker.shop.order.model.OrderVO;
import com.topseeker.shop.orderdetail.model.OrderDetailVO;
import com.topseeker.shop.product.model.ShopProductService;
import com.topseeker.shop.product.model.ShopProductVO;


@Controller
@RequestMapping("/protected/cart")
public class CartController {
	
	@Autowired
	private CartService cartSvc;
	@Autowired
	private ShopProductService shopProductSvc;
	@Autowired
	private MemberService memberSvc;
	
	
//////////////////////////////////////////	
	//購物車顯示畫面
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
    	
    	MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
    	    	
    	 if (loggedInMember == null) {
           
             return "redirect:/member/loginMem";
         }
    	Integer loginMemNo = loggedInMember.getMemNo();
    	 
    	List<CartVO> cartDetail = cartSvc.getMemAllList(loginMemNo);
    	model.addAttribute("cartDetail", cartDetail);
    	model.addAttribute("loginMemNo", loginMemNo);
        return "front-end/shop/cart/cart";
    }
	
//新增or修改
    //加入購物車
    @PostMapping("/saveOrUpdateCart")
    @ResponseBody
    public Map<String, Object> saveOrUpdateCart(@RequestBody Map<String, Object> request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
        	/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
            System.out.println("接收儲存商品至購物車的請求");

            MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
            
            if (loggedInMember == null) {
                System.out.println("用戶未登入");
                response.put("success", false);
                response.put("message", "請先登入");
                return response;
            }

            Integer loginMemNo = loggedInMember.getMemNo();
            /*************************** 2.開始新增資料 *****************************************/
            //取得商品編號及數量
            	
            Integer prodNo = null;
            if (request.get("prodNo") instanceof String) {
                prodNo = Integer.valueOf((String) request.get("prodNo"));
            } else if (request.get("prodNo") instanceof Integer) {
                prodNo = (Integer) request.get("prodNo");
            }
            
            
            Integer prodQty = null;
            if(request.get("prodQty") instanceof String) {
            	prodQty = Integer.valueOf((String) request.get("prodQty"));
            }else if (request.get("prodNo") instanceof Integer) {
            	prodQty = (Integer) request.get("prodQty");
            }
            
            
            ShopProductVO shopProductVO = shopProductSvc.getOneShopProduct(prodNo);
            if (shopProductVO == null) {
                throw new RuntimeException("此商品不存在: " + prodNo);
            }
            
            //創建更新購物車
            CartVO cartVO = new CartVO();
            cartVO.setMemberVO(loggedInMember);
            cartVO.setShopProductVO(shopProductVO);
            cartVO.setProdQty(prodQty);

            cartSvc.saveOrUpdateCart(cartVO);

          /*************************** 3.新增完成,準備轉交(Send the Success view) **************/
            System.out.println("已完成建立或更新購物車");
            response.put("success", true);
            response.put("message", "商品已加入購物車");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            //有誤時
            response.put("success", false);
            response.put("message", "發生錯誤，請稍後再試。");
            return response;
        }
    }
	
//刪除單項商品
	@PostMapping("/deleteOne")
	public String deleteOne(
			HttpSession session,
			Integer memNo,
			@RequestParam("prodNo") Integer prodNo,
			ModelMap model
			)throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		Integer loginMemNo = loggedInMember.getMemNo();
		/*************************** 2.開始刪除資料 *****************************************/
	    cartSvc.deleteByMemNoAndProdNo(loginMemNo, prodNo);
	           
		/*************************** 3.完成,準備轉交(Send the Success view) **************/
        List<CartVO> cartDetail = cartSvc.getMemAllList(loginMemNo);
        model.addAttribute("cartDetail", cartDetail);
        return "redirect:/protected/cart/cart";
		
	}
	
//清空購物車
	@PostMapping("deleteMemAllCart")
	public String deleteMemAllCart(
			HttpSession session,
			Integer memNo,
			ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		Integer loginMemNo = loggedInMember.getMemNo();
		
		/*************************** 2.開始刪除資料 *****************************************/
		cartSvc.deleteMemAllCart(loginMemNo);
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<CartVO> cartDetail = cartSvc.getMemAllList(loginMemNo);
		model.addAttribute("cartDetail", cartDetail);
        return "redirect:/protected/cart/cart";
	}
	
//結帳
	@PostMapping("checkOut")
	public String checkOut(
			HttpSession session,
			Integer memNo,
			ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		
		 if (loggedInMember == null) {
		        return "redirect:/member/loginMem";
		    }
		
		Integer loginMemNo = loggedInMember.getMemNo();
		
		List<CartVO> cartDetails = cartSvc.getMemAllList(loginMemNo);
	    MemberVO memberVO = memberSvc.getOneMem(loginMemNo);
		
	
		/*************************** 2.開始新增資料 *****************************************/
		 
	    CartVO cartVO = new CartVO();
	    for (int i = 0; i < cartDetails.size(); i++) {
	        cartVO = cartDetails.get(i);
	        cartVO.getCartNo();
	        cartVO.getShopProductVO().getProdNo();
	        cartVO.getShopProductVO().getProdName();
	        cartVO.getShopProductVO().getProdPrice();
	        cartVO.getProdQty();
	      

	        model.addAttribute("cartVO", cartVO);

        }
	    OrderVO orderVO = new OrderVO();
	    orderVO.getOrderDetails().add(new OrderDetailVO());
	    /*************************** 3.準備轉交(Send the Success view) **************/
	    model.addAttribute("orderVO", orderVO);
	    model.addAttribute("cartDetails", cartDetails);
	    model.addAttribute("memberVO", memberVO);
		return "front-end/shop/cart/checkout";
	}
	
	

	
	
//購物車頁面更新數量
	@PostMapping("/updateQty")
	@ResponseBody
	public ResponseEntity<String> updateQty(@RequestParam("cartNo") Integer cartNo,
	                       	@RequestParam("prodNo") Integer prodNo,
	                        @RequestParam("prodQty")Integer prodQty) {
	    try {
	    	if (prodQty < 1) {
	    	  return new ResponseEntity<>("數量不得低於一件", HttpStatus.BAD_REQUEST);
	    	    				     }
	        cartSvc.updateCartQty(cartNo, prodNo, prodQty);
	        return new ResponseEntity<>("更新數量成功", HttpStatus.OK);
	    } catch (Exception e) {
	        return new ResponseEntity<>("資料有誤無法更新", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	
	
	
	
	//相關mapping

	@ModelAttribute("shopListData") 
	protected List<ShopProductVO> shopListData(Model model) {
		
    	List<ShopProductVO> list = shopProductSvc.getAll();
		return list;
	}
	@ModelAttribute("memListData") 
	protected List<MemberVO> memListData(Model model) {
		
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
}
