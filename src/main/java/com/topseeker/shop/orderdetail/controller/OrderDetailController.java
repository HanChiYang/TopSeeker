package com.topseeker.shop.orderdetail.controller;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.topseeker.shop.order.model.OrderService;
import com.topseeker.shop.order.model.OrderVO;
import com.topseeker.shop.orderdetail.model.CompositeDetail;
import com.topseeker.shop.orderdetail.model.OrderDetailService;
import com.topseeker.shop.orderdetail.model.OrderDetailVO;
import com.topseeker.shop.product.model.ShopProductService;
import com.topseeker.shop.product.model.ShopProductVO;

@Controller
@RequestMapping("/shop/orderDetail")
public class OrderDetailController {
	
	@Autowired
	OrderDetailService orderDetailSvc;
	@Autowired
	ShopProductService prodSvc;
	@Autowired
	OrderService orderSvc;
	
//新增
	@GetMapping("addOrderDetail")
	public String addOrderDetail(ModelMap model) {
			OrderDetailVO orderDetailVO = new OrderDetailVO();
			model.addAttribute("orderDetailVO" , orderDetailVO);
			return "back-end/shop/order/addOrderDetail";
		}
		
		//在addOrderDetail頁面執行insert
		@PostMapping("insert")
		public String insert(@Valid  @ModelAttribute OrderDetailVO orderDetailVO, BindingResult result, ModelMap model
				)throws IOException {
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			Integer orderNo = orderDetailVO.getCompositeDetail().getOrderNo();
            Integer prodNo = orderDetailVO.getProductVO().getProdNo();
                     
            
//			if(orderDetail != null) {
//					model.addAttribute("errorMessage", "已存在相同明細，請直接修改該筆訂單明細");
//					model.addAttribute("orderDetailVO" , orderDetailVO);
//					return "back-end/shop/order/select_page";
//				
//			}
			
			
			if (result.hasErrors()) {
				return "back-end/shop/order/addOrder";
			}
			/*************************** 2.開始新增資料 *****************************************/
			
			   if (orderDetailVO.getCompositeDetail() == null) {
	                orderDetailVO.setCompositeDetail(new CompositeDetail());
	            }
		
            
            // 驗證
            if (orderNo == null) {
                throw new IllegalArgumentException("OrderNo cannot be null");
            }else if (prodNo == null){
                throw new IllegalArgumentException("ProdNo cannot be null");
            }

            OrderVO orderVO = orderSvc.getOneOrder(orderNo);
            ShopProductVO productVO = prodSvc.getOneShopProduct(prodNo);
            
            if (orderVO == null) {
                throw new IllegalArgumentException("Order not found for id: " + orderNo);
            }
            if (productVO == null) {
                throw new IllegalArgumentException("Product not found for id: " + prodNo);
            }

            orderDetailVO.setOrderVO(orderVO);
            orderDetailVO.setProductVO(productVO);
			
			orderDetailSvc.addOrderDetail(orderDetailVO);
			/*************************** 3.新增完成,準備轉交(Send the Success view) **************/
			List<OrderDetailVO> list = orderDetailSvc.getAll();
			model.addAttribute("orderDetailListData", list);
			return "back-end/shop/order/listAllOrderDetail";
		}

	
//修改
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("orderNo") String orderNo, @RequestParam("prodNo") String prodNo, ModelMap model) {
		
		CompositeDetail compositeDetail = new CompositeDetail(Integer.valueOf(orderNo), Integer.valueOf(prodNo));
		OrderDetailVO orderDetailVO = orderDetailSvc.getOneOrderDetail(compositeDetail);  
		model.addAttribute("orderDetailVO", orderDetailVO);
		return "back-end/shop/order/updateOrderDetail";// 查詢完成後轉交updateOrderDetail.html
	}
	
	@PostMapping("update")
	public String update(@Valid OrderDetailVO orderDetailVO, BindingResult result, ModelMap model
			) throws IOException {
	
		if (result.hasErrors()) {
			return "back-end/shop/order/updateOrderDetail";
		}
		orderDetailSvc.updateOrderDetail(orderDetailVO);
		CompositeDetail compositeDetail = orderDetailVO.getCompositeDetail();
		orderDetailVO = orderDetailSvc.getOneOrderDetail(compositeDetail);
		List<OrderDetailVO> orderDetailList = orderDetailSvc.getAll();
		model.addAttribute("orderDetailListData", orderDetailList);
		return "back-end/shop/order/listAllOrderDetail";
		
	}


	//刪除
		@PostMapping("delete")
		public String delete(@RequestParam("orderNo") String orderNo, @RequestParam("prodNo") String prodNo, ModelMap model) {
			/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
			/*************************** 2.開始刪除資料 *****************************************/
			CompositeDetail compositeDetail = new CompositeDetail(Integer.valueOf(orderNo), Integer.valueOf(prodNo));
			orderDetailSvc.deleteOrderDetail(compositeDetail);
			/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
			List<OrderDetailVO> list = orderDetailSvc.getAll();
			model.addAttribute("orderDetailListData", list);
			return "back-end/shop/order/listAllOrderDetail";// 刪除完成後轉交listAllOrder.html
		}
	

	@ModelAttribute("prodListData")
	protected List<ShopProductVO> referenceListData(){
	
		List<ShopProductVO> list = prodSvc.getAll();
		return list;
	}
	
	@ModelAttribute("orderListData")
	protected List<OrderVO> referenceListData1(){
	
		List<OrderVO> list = orderSvc.getAll();
		return list;
	}

	
	
	@ExceptionHandler(value = { ConstraintViolationException.class })
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ModelAndView handleError(HttpServletRequest req,ConstraintViolationException e,Model model) {
	    Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
	    StringBuilder strBuilder = new StringBuilder();
	    for (ConstraintViolation<?> violation : violations ) {
	          strBuilder.append(violation.getMessage() + "<br>");
	    }
	    //==== 以下是當前面返回 /src/main/resources/templates/back-end/shop/order/select_page.html用的 ====   

		List<OrderDetailVO> list = orderDetailSvc.getAll();
		model.addAttribute("orderDetailListData", list); 
		String message = strBuilder.toString();
	    return new ModelAndView("back-end/shop/order/select_page", "errorMessage", "請修正以下錯誤:<br>"+message);
	}
	

}
