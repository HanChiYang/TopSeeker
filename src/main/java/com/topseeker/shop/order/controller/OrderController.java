package com.topseeker.shop.order.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.cart.model.CartService;
import com.topseeker.shop.cart.model.CartVO;
import com.topseeker.shop.order.model.OrderService;
import com.topseeker.shop.order.model.OrderVO;
import com.topseeker.shop.orderdetail.model.CompositeDetail;
import com.topseeker.shop.orderdetail.model.OrderDetailService;
import com.topseeker.shop.orderdetail.model.OrderDetailVO;
import com.topseeker.shop.product.model.ShopProductService;
import com.topseeker.shop.product.model.ShopProductVO;
import com.topseeker.shop.sale.model.SaleService;
import com.topseeker.shop.sale.model.SaleVO;

@Controller
@RequestMapping("/shop/order")
public class OrderController {
	
	@Autowired
	OrderService orderSvc;
	@Autowired
	SaleService saleSvc;
	@Autowired
	MemberService memberSvc;
	@Autowired
	ShopProductService shopProductSvc;
	@Autowired	
	OrderDetailService orderDetailService;
	@Autowired
	CartService cartSvc;
	
	
	
	@GetMapping("select_page")
	public String select_page_order(Model model) {
		model.addAttribute("compositeDetail", new CompositeDetail());
		return "back-end/shop/order/select_page";
	}
	
	
	
////////////////////////////////////////////////	
 
	@PostMapping("insert")
	public String insert(@Valid 
			@ModelAttribute("orderVO") OrderVO orderVO,
            BindingResult result,
            HttpSession session,
			@RequestParam("saleNo") Integer saleNo,
			@RequestParam("deliveryMethod") Integer deliveryMethod,
			@RequestParam(value = "orderNameth", required = false) String orderNameth,
            @RequestParam(value = "orderPhoneth", required = false) String orderPhoneth,
            @RequestParam(value = "orderAddressth", required = false) String orderAddressth,
            @RequestParam(value = "orderNamecvs", required = false) String orderNamecvs,
            @RequestParam(value = "orderPhonecvs", required = false) String orderPhonecvs,
            @RequestParam(value = "orderAddresscvs", required = false) String orderAddresscvs,
           
			ModelMap model
			)throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		
		if (loggedInMember == null) {
	        return "redirect:/member/loginMem"; 
	    }
		
		Integer loginMemNo = loggedInMember.getMemNo();
		
		if (orderVO.getOrderDate() == null) {
	        orderVO.setOrderDate(new Timestamp(System.currentTimeMillis()));  
	        }
	
		if (result.hasErrors()) {
			System.out.println(result);
			MemberVO memberVO = memberSvc.getOneMem(loginMemNo);
            List<CartVO> cartDetails = cartSvc.getMemAllList(loginMemNo);
            model.addAttribute("memberVO", memberVO);
            model.addAttribute("cartDetails", cartDetails);
            return "front-end/shop/cart/checkout";
		}
		/*************************** 2.開始新增資料 *****************************************/
		try {
		
			MemberVO memberVO = memberSvc.getOneMem(loginMemNo);
	        orderVO.setMemberVO(memberVO);
	        
	        
	        if (saleNo != null) {
	            SaleVO saleVO = saleSvc.getOneSale(saleNo);
	            if (saleVO != null) {
	                orderVO.setSaleVO(saleVO);
	            }
	        }
	        
	        if (deliveryMethod == 0) {
	            orderVO.setOrderName(orderNameth);
	            orderVO.setOrderPhone(orderPhoneth);
	            orderVO.setOrderAddress(orderAddressth);
	        } else if (deliveryMethod == 1) {
	            orderVO.setOrderName(orderNamecvs);
	            orderVO.setOrderPhone(orderPhonecvs);
	            orderVO.setOrderAddress(orderAddresscvs);
	        }
	
	      
			List<CartVO> cartItems = cartSvc.getMemAllList(loginMemNo);
            for (CartVO cartItem : cartItems) {
                OrderDetailVO detail = new OrderDetailVO();
                ShopProductVO product = shopProductSvc.getOneShopProduct(cartItem.getShopProductVO().getProdNo());
                if (product == null) {
                    throw new RuntimeException("無此商品: " + cartItem.getShopProductVO().getProdNo());
                }
                detail.setShopProductVO(product);
                detail.setOrderQty(cartItem.getProdQty());
                detail.setOrderPrice(product.getProdPrice());
                detail.setOrderVO(orderVO);
                orderVO.addOrderDetail(detail);
              
            }
            
			orderSvc.addOrderWithOrderDetail(orderVO);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		model.addAttribute("orderVO", orderVO);
		
		cartSvc.deleteMemAllCart(loginMemNo);//新增訂單時同時刪除購物車內容
		return "redirect:/shop/order/checkoutResult?orderNo=" + orderVO.getOrderNo();
	}
	
	//交易結果畫面
	@GetMapping("checkoutResult")
    public String checkoutResult(@RequestParam("orderNo") Integer orderNo, ModelMap model) {
        OrderVO orderVO = orderSvc.getOneOrder(orderNo);
        List<OrderDetailVO> orderDetails = orderDetailService.findOrderDetailsByOrderNo(orderNo);

        model.addAttribute("orderVO", orderVO);
        model.addAttribute("orderDetails", orderDetails);

        return "front-end/shop/cart/checkoutResult";
    }

//修改
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("orderNo") String orderNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		OrderVO orderVO = orderSvc.getOneOrder(Integer.valueOf(orderNo));
		
		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("orderVO", orderVO);
		return "back-end/shop/order/updateOrder";// 查詢完成後轉交updateOrder.html
	}
	
	@PostMapping("update")
	public String update(@Valid OrderVO orderVO, BindingResult result, ModelMap model
			) throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		if (result.hasErrors()) {
			return "back-end/shop/order/updateOrder";
		}
		/*************************** 2.開始修改資料 *****************************************/
		orderSvc.updateOrder(orderVO);
		
		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		orderVO = orderSvc.getOneOrder(Integer.valueOf(orderVO.getOrderNo()));
		model.addAttribute("orderVO", orderVO);
		model.addAttribute("orderDetails", orderVO.getOrderDetails());
		return "back-end/shop/order/listOneOrder";
		
	}
	
//刪除
	@PostMapping("delete")
	public String delete(@RequestParam("orderNo") String orderNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		orderSvc.deleteOrder(Integer.valueOf(orderNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<OrderVO> list = orderSvc.getAll();
		model.addAttribute("orderListData", list);
		return "back-end/shop/order/listAllOrder";// 刪除完成後轉交listAllOrder.html
	}
	
	//顯示全部訂單
	@GetMapping("listAllOrder")
	public String listAllOrder(Model model) {
		return "back-end/shop/order/listAllOrder";
	}
	
	
	//顯示會員訂單
	@GetMapping("memOrders")
	public String listMemAllOrder(HttpSession session, Model model) {
		MemberVO loggedInMember = (MemberVO) session.getAttribute("loggedInMember");
		
		if (loggedInMember == null) {
          
            return "redirect:/member/loginMem";
        }
		
		Integer loginMemNo = loggedInMember.getMemNo();
		
		List<OrderVO> memOrderList = orderSvc.getMemAllOrder(loginMemNo);
		model.addAttribute("loginMemNo", loginMemNo);
		model.addAttribute("memOrderList", memOrderList);
		return "front-end/shop/order/memOrderList";
	}
	
	

	@PostMapping("listOrders_ByCompositeQuery")
	public String listAllOrder(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<OrderVO> list = orderSvc.getAll(map);
		model.addAttribute("orderListData", list); 
		return "back-end/shop/order/listAllOrder";
	}
	
	

	//商品顯示用
    @ModelAttribute("shopListData")
	protected List<ShopProductVO> referenceListData(Model model) {
    	List<ShopProductVO> list = shopProductSvc.getAll();
    	
		return list;
	}
    
	@ModelAttribute("saleListData")
	protected List<SaleVO> referenceListData(){
	
		List<SaleVO> list1 = saleSvc.getAll();
		return list1;
	}
	
	@ModelAttribute("memListData")
	protected List<MemberVO> referenceMapData(){
	
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
	
	@ModelAttribute("orderListData")
	protected List<OrderVO> orderListData(Model model) {
		
		List<OrderVO> list = orderSvc.getAll();
		return list;
	}
	


	
	
}
