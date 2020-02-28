package com.tmqt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmqt.dto.RestResultBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Component
public class ManageInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
		System.out.println("-------------");
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String url = requestUri.substring(contextPath.length());
		if (url.equals("/login.html") || url.equals("/loginApi") || url.endsWith("js") || url.endsWith("css")
				|| url.endsWith("png") || url.endsWith("png") || url.endsWith("jpeg") || url.endsWith("gif")
				|| url.endsWith("error") || url.endsWith("/verifyCode")) {
			return true;
		} else {
			String managerId = (String) request.getSession().getAttribute("userId");
			System.out.println("------> userId："  + managerId);
			if (managerId == null) {
				System.out.println("++++++++++++++++++++++++");
//				response.sendRedirect("#/login");
				return false;
			} else {
				return true;
			}
		}
	}

	public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
			ModelAndView modelAndView) throws Exception {

	}

	public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			Object o, Exception e) throws Exception {

	}
}
