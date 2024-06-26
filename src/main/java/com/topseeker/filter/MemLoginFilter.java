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

@WebFilter(urlPatterns = { "/protected/*" })
public class MemLoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) req;
		HttpServletResponse httpResponse = (HttpServletResponse) res;
		HttpSession session = httpRequest.getSession();

		// 已登入
		if (session.getAttribute("loggedInMember") != null) {
			chain.doFilter(req, res);
		
		// 未登入
		} else {
			//儲存欲前往的頁面於session
			String oriURL = httpRequest.getRequestURI();
			session.setAttribute("oriURL", oriURL);
			
			//重導到登入頁面
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/member/loginMem");
		}
	}

}