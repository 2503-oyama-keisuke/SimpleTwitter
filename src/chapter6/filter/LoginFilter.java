package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

@WebFilter(urlPatterns = { "/setting", "/edit" })
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest HttpRequest = (HttpServletRequest) request;
		HttpServletResponse HttpResponse = (HttpServletResponse) response;
		HttpSession session = HttpRequest.getSession();
		List<String> errorMessages = new ArrayList<>();

		if (session.getAttribute("loginUser") != null) {
			chain.doFilter(request, response);

		} else {
			errorMessages.add("ログインをしてください");

			session.setAttribute("errorMessages", errorMessages);
			HttpResponse.sendRedirect("./login");
		}
	}

	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}

}