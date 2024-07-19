package com.topseeker.tourOrder.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.sql.Date;
import java.time.LocalDate;

import com.topseeker.member.model.MemberService;
import com.topseeker.member.model.MemberVO;
import com.topseeker.tourGroup.model.TourGroupService;
import com.topseeker.tourGroup.model.TourGroupVO;
import com.topseeker.tourOrder.model.TourOrderService;
import com.topseeker.tourOrder.model.TourOrderVO;

@Controller
@RequestMapping("/tourOrder")
public class TourOrderController {

	@Autowired
	TourOrderService tourOrderSvc;
	
	@Autowired
	MemberService memberSvc;
	
	@Autowired
	TourGroupService tourGroupSvc;

	/*
	 * This method will serve as addEmp.html handler.
	 */
	@GetMapping("/select_page")
	public String select_page(Model model) {
		return "back-end/tourOrder/select_page";
	}
	
	@GetMapping("/select_root")
	public String select_root(Model model) {
		return "front-end/tourOrder/select_root";
	}
	
	@GetMapping("/listAllOrder")
	public String listAllOrder(Model model) {
		return "back-end/tourOrder/listAllOrder";
	}
	
    @ModelAttribute("tourOrderListData")  // for select_page.html 第97 109行用 // for listAllEmp.html 第85行用
	protected List<TourOrderVO> referenceListData(Model model) {
		
    	List<TourOrderVO> list = tourOrderSvc.getAll();
		return list;
	}
    
	@ModelAttribute("tourGroupListData") // for select_page.html 第135行用
	protected List<TourGroupVO> referenceListData_Group(Model model) {
		model.addAttribute("TourGroupVO", new TourGroupVO()); // for select_page.html 第133行用
		List<TourGroupVO> list = tourGroupSvc.getAll();
		return list;
	}
	
	
	
	@GetMapping("/comment")
	public String showCommentPage(@RequestParam(value = "orderNo", required = false) Integer orderNo, Model model) {
	    if (orderNo != null) {
	        TourOrderVO tourOrderVO = tourOrderSvc.getOneOrder(orderNo);
	        model.addAttribute("tourOrderVO", tourOrderVO);
	    } else {
	        // 如果沒有orderNo，可以做一些預設或錯誤處理的操作
	        model.addAttribute("errorMessage", "Order number is required.");
	    }
	    return "front-end/tourOrder/commentPage";
	}


	

	

	

	
	
	
	/*
	 * This method will be called on addEmp.html form submission, handling POST request It also validates the user input
	 */
	
	
//	@PostMapping("/confirm")
//    public String confirmOrder(@RequestParam("orderNo") Integer orderNo,
//                               @RequestParam("orderPay") Byte orderPay,
//                               Model model) {
//		boolean success = tourOrderSvc.confirmOrder(orderNo, orderPay);
//        
//
//		if (success) {
//	        TourOrderVO tourOrderVO = tourOrderSvc.getOneOrder(orderNo);
//	        model.addAttribute("tourOrderVO", tourOrderVO);
//	        if (orderPay == 1) {
//	            return "front-end/tourOrder/creditCardPayment";
//	        } else if (orderPay == 2) {
//	            return "front-end/tourOrder/bankTransferPayment";
//	        } else {
//	            model.addAttribute("error", "不支持的支付方式。");
//	            return "front-end/tourOrder/confirmOrder";
//	        }
//	    } else {
//	        model.addAttribute("error", "订单确认失败，请重试。");
//	        return "front-end/tourOrder/confirmOrder";
//	    }
//    }

	
	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("orderNo") String orderNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始查詢資料 *****************************************/
		// EmpService empSvc = new EmpService();
		TourOrderVO tourOrderVO = tourOrderSvc.getOneOrder(Integer.valueOf(orderNo));

		/*************************** 3.查詢完成,準備轉交(Send the Success view) **************/
		model.addAttribute("tourOrderVO", tourOrderVO);
		return "back-end/tourOrder/update_order_input"; // 查詢完成後轉交update_emp_input.html
	}

	/*
	 * This method will be called on update_emp_input.html form submission, handling POST request It also validates the user input
	 */
	@PostMapping("update")
	public String update(@Valid TourOrderVO tourOrderVO, BindingResult result, ModelMap model) throws IOException {

		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/

		if (result.hasErrors()) {
			return "back-end/tourOrder/update_order_input";
		}
		/*************************** 2.開始修改資料 *****************************************/
		// EmpService empSvc = new EmpService();
		System.out.println(tourOrderVO.getOrderNo());
		tourOrderSvc.updateOrder(tourOrderVO);

		/*************************** 3.修改完成,準備轉交(Send the Success view) **************/
		model.addAttribute("success", "- (修改成功)");
		tourOrderVO = tourOrderSvc.getOneOrder(Integer.valueOf(tourOrderVO.getOrderNo()));
		model.addAttribute("tourOrderVO", tourOrderVO);
		return "back-end/tourOrder/listOneOrder"; // 修改成功後轉交listOneOrder.html
	}

	
	


	/*
	 * This method will be called on listAllEmp.html form submission, handling POST request
	 */
	@PostMapping("delete")
	public String delete(@RequestParam("orderNo") String orderNo, ModelMap model) {
		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 ************************/
		/*************************** 2.開始刪除資料 *****************************************/
		// EmpService empSvc = new EmpService();
		tourOrderSvc.deleteOrder(Integer.valueOf(orderNo));
		/*************************** 3.刪除完成,準備轉交(Send the Success view) **************/
		List<TourOrderVO> list = tourOrderSvc.getAll();
		model.addAttribute("tourOrderListData", list);
		model.addAttribute("success", "- (刪除成功)");
		return "back-end/tourOrder/listAllOrder"; // 刪除完成後轉交listAllOrder.html
	}
	
	@PostMapping("/submitComment")
    public String submitComment(@RequestParam("orderNo") Integer orderNo, @RequestParam("orderComment") String orderComment) {
        tourOrderSvc.addComment(orderNo, orderComment);
        return "redirect:/tourOrder/historical_Order";
    }

	/*
	 * 第一種作法 Method used to populate the List Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${deptListData}" itemValue="deptno" itemLabel="dname" />
	 */
	@ModelAttribute("memberListData")
	protected List<MemberVO> referenceListData_member() {
		// DeptService deptSvc = new DeptService();
		List<MemberVO> list = memberSvc.getAll();
		return list;
	}
	
	@ModelAttribute("tourGroupListData")
	protected List<TourGroupVO> referenceListData_Group() {
		// DeptService deptSvc = new DeptService();
		List<TourGroupVO> list = tourGroupSvc.getAll();
		return list;
	}

	/*
	 * 【 第二種作法 】 Method used to populate the Map Data in view. 如 : 
	 * <form:select path="deptno" id="deptno" items="${depMapData}" />
	 */
//	@ModelAttribute("memberMapData") //
//	protected Map<Integer, String> referenceMapData() {
//		Map<Integer, String> map = new LinkedHashMap<Integer, String>();
//		map.put(1001, "財務部");
//		map.put(1002, "研發部");
//		map.put(1003, "業務部");
//		map.put(1004, "生管部");
//		map.put(1005, "財務部");
//		map.put(1006, "研發部");
//		map.put(1007, "業務部");
//		map.put(1008, "生管部");
//		map.put(1009, "財務部");
//		map.put(1010, "研發部");
//
//		return map;
//	}

	// 去除BindingResult中某個欄位的FieldError紀錄
	public BindingResult removeFieldError(TourOrderVO tourOrderVO, BindingResult result, String removedFieldname) {
		List<FieldError> errorsListToKeep = result.getFieldErrors().stream()
				.filter(fieldname -> !fieldname.getField().equals(removedFieldname))
				.collect(Collectors.toList());
		result = new BeanPropertyBindingResult(tourOrderVO, "tourOrderVO");
		for (FieldError fieldError : errorsListToKeep) {
			result.addError(fieldError);
		}
		return result;
	}
	
	/*
	 * This method will be called on select_page.html form submission, handling POST request
	 */
	@PostMapping("listOrders_ByCompositeQuery")
	public String listAllOrder(HttpServletRequest req, Model model) {
		Map<String, String[]> map = req.getParameterMap();
		List<TourOrderVO> list = tourOrderSvc.getAll(map);
		model.addAttribute("tourOrderListData", list); // for listAllOrder.html 第85行用
		return "back-end/tourOrder/listAllOrder";
	}

}