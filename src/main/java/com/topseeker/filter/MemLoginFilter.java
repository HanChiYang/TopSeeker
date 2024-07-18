package com.topseeker.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns = "/protected/*")
public class MemLoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
//		System.out.println("filter init");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) res;
		HttpSession session = httpRequest.getSession();
//		System.out.println("dofilter");

		// 檢查是否已登入
		if (session.getAttribute("loggedInMember") != null) {
			chain.doFilter(req, res); // 已登入，繼續執行後續過濾器
		} else {
			// 未登入，將原始要求的 URL 儲存到 session
			String oriURL = httpRequest.getRequestURI();
			String queryString = httpRequest.getQueryString();
			
			if (queryString != null) {
				 oriURL += "?" + queryString;
			}
			session.setAttribute("oriURL", oriURL);
//			System.out.println("oriURL:" + oriURL);

			// 重導到登入頁面
			String alertMessage = "您尚未登入";
			String loginPage = httpRequest.getContextPath() + "/member/loginMem";
			String htmlResponse = "<html><body><script type='text/javascript'>" + "alert('" + alertMessage + "');"
					+ "window.location.href='" + loginPage + "';" + "</script></body></html>";

			httpResponse.setContentType("text/html; charset=UTF-8");
			httpResponse.getWriter().write(htmlResponse);
		}
	}

	@Override
	public void destroy() {
		// 可以在這裡清理資源
	}
}