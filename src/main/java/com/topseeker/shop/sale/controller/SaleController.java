package com.topseeker.shop.sale.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.topseeker.shop.sale.model.SaleService;
import com.topseeker.shop.sale.model.SaleVO;

@Controller
@RequestMapping("/shop/sale")
public class SaleController {
	
	@Autowired
	SaleService saleSvc;
	
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

	
//	@PostMapping("listSales_ByCompositeQuery")
//	public String listAllSale(HttpServletRequest req, Model model) {
//		Map<String, String[]> map = req.getParameterMap();
//		List<SaleVO> list = saleSvc.getAll(map);
//		model.addAttribute("saleListData", list); 
//		return "back-end/shop/sale/listAllSale";
//	}
	
}
