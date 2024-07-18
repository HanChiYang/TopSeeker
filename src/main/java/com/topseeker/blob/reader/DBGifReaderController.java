package com.topseeker.blob.reader;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.topseeker.member.model.MemberService;

@Controller
@RequestMapping("/member")
public class DBGifReaderController {

	@Autowired
	MemberService memSvc;

	/*
	 * This method will serve as listOneEmp.html , listAllEmp.html handler.
	 */
	@GetMapping("DBGifReader")
	public void dBGifReader(@RequestParam("memNo") String memNo, HttpServletRequest req, HttpServletResponse res)
			throws IOException {
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();

		try {
//			EmpService empSvc = new EmpService();
			out.write(memSvc.getOneMem(Integer.valueOf(memNo)).getMemImg());
		} catch (Exception e) {
			byte[] buf = java.util.Base64.getDecoder().decode(
					"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");
			out.write(buf);
		}
	}
}