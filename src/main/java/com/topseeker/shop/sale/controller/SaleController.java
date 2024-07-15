package com.topseeker.shop.sale.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.topseeker.shop.sale.model.SaleService;
import com.topseeker.shop.sale.model.SaleVO;

@Controller
@RequestMapping("/shop/sale")
public class SaleController {
	
	@Autowired
	SaleService saleSvc;

//促銷方案相關mapping
	
    @GetMapping("/shop/sale/select_page")
	public String select_page_sale(Model model) {
		return "back-end/shop/sale/select_page";
	}
	
	    
	@GetMapping("/shop/sale/listAllSale")
	public String listAllSale(Model model) {
		return "back-end/shop/sale/listAllSale";
	}
	    
	@ModelAttribute("saleListData")
	protected List<SaleVO> referenceListData(Model model) {
			
	  	List<SaleVO> list = saleSvc.getAll();
		return list;
	}
	

////////////////////////////////////////////////////////////////////////	
//新增
	@GetMapping("addSale")
	public String addSale(ModelMap model) {
		SaleVO saleVO = new SaleVO();
		model.addAttribute("saleVO" , saleVO);
		return "back-end/shop/sale/addSale";
	}
	
	//在addSale頁面執行insert
	@PostMapping("insert")
	public String insert(@Valid SaleVO saleVO, BindingResult result, ModelMap model
			)throws IOException {
		
		if (result.hasErrors()) {
			return "back-end/shop/sale/addSale";
		}
		
		saleSvc.addSale(saleVO);
		List<SaleVO> list = saleSvc.getAll();
		model.addAttribute("saleListData", list);
		return "redirect:/shop/sale/listAllSale";
		// 新增成功後重導至IndexController的@GetMapping("/sale/listAllSale")
	}

//修改
	@PostMapping("getOne_For_Update")
	public String getOne_For_Update(@RequestParam("saleNo") String saleNo, ModelMap model) {
		SaleVO saleVO = saleSvc.getOneSale(Integer.valueOf(saleNo));  
		model.addAttribute("saleVO", saleVO);
		return "back-end/shop/sale/updateSale";// 查詢完成後轉交updateSale.html
	}
	
	@PostMapping("update")
	public String update(@Valid SaleVO saleVO, BindingResult result, ModelMap model
			) throws IOException {
	
		if (result.hasErrors()) {
			return "back-end/shop/sale/updateSale";
		}
		saleSvc.updateSale(saleVO);
		saleVO = saleSvc.getOneSale(Integer.valueOf(saleVO.getSaleNo()));
		model.addAttribute("saleVO", saleVO);
		return "back-end/shop/sale/listOneSale";
		
	}
	
//刪除
	@PostMapping("delete")
	public String delete(@RequestParam("saleNo") String saleNo, ModelMap model) {
		saleSvc.deleteSale(Integer.valueOf(saleNo));
		List<SaleVO> list = saleSvc.getAll();
		model.addAttribute("saleListData", list);
		return "back-end/shop/sale/listAllSale";
	}

//取出促銷方案(購物車用)
	@GetMapping("/applicableSales")
	@ResponseBody
	public ResponseEntity<List<SaleVO>> applicableSales(@RequestParam("totalAmount") Integer totalAmount, @RequestParam("currentTime") Timestamp currentTime) {
		
		List<SaleVO> sales = saleSvc.getApplicableSales(totalAmount, currentTime);
		System.out.println(sales);
		
		return sales.isEmpty() ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(sales, HttpStatus.OK);
	} 
	
	
}
