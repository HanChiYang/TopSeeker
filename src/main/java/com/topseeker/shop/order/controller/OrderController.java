package com.topseeker.shop.order.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.shop.order.model.OrderService;
import com.topseeker.shop.order.model.OrderVO;
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
	MemberService memSvc;
	@Autowired
	ShopProductService shopProdSvc;
	@Autowired	
	OrderDetailService orderDetailService;
	
	
//新增
	@GetMapping("addOrder")
	public String addOrder(ModelMap model) {
		OrderVO orderVO = new OrderVO();
		orderVO.getOrderDetails().add(new OrderDetailVO());
		model.addAttribute("orderVO" , orderVO);
		
		Timestamp orderDate = new Timestamp(System.currentTimeMillis());
		orderVO.setOrderDate(orderDate);
		return "back-end/shop/order/addOrder";
	}
	
	//在addOrder頁面執行insert
	@PostMapping("insert")
	public String insert(@Valid OrderVO orderVO, BindingResult result, ModelMap model
			)throws IOException {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			
		if (result.hasErrors()) {
			return "back-end/shop/order/addOrder";
		}
		/*************************** 2.開始新增資料 *****************************************/
		try {
			for (OrderDetailVO detail : orderVO.getOrderDetails()) {
	           detail.setOrderVO(orderVO);
	           ShopProductVO product = shopProdSvc.getOneShopProduct(detail.getCompositeDetail().getProdNo());
	           detail.setProductVO(product);
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		 	orderSvc.addOrder(orderVO);
		/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
		List<OrderVO> list = orderSvc.getAll();
		model.addAttribute("orderListData", list);
		return "redirect:/shop/order/listAllOrder";
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
	
	@ModelAttribute("saleListData")
	protected List<SaleVO> referenceListData(){
	
		List<SaleVO> list1 = saleSvc.getAll();
		return list1;
	}
	
	@ModelAttribute("memListData")
	protected List<MemberVO> referenceMapData(){
	
		List<MemberVO> list = memSvc.getAll();
		return list;
	}
	
	@ModelAttribute("orderListData")
	protected List<OrderVO> referenceListData1(){
	
		List<OrderVO> list = orderSvc.getAll();
		return list;
	}

	
	
	@PostMapping("listOrders_ByCompositeQuery")
	public String listAllOrder(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<OrderVO> list = orderSvc.getAll(map);
		model.addAttribute("orderListData", list); 
		return "back-end/shop/order/listAllOrder";
	}
	
}
