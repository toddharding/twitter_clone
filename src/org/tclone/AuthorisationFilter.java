package org.tclone;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Todd on 18/02/14.
 */
@WebFilter(urlPatterns = {"/user"}, servletNames = {"UserServlet", "TweetServlet"})
public class AuthorisationFilter implements Filter
{
	public void destroy()
	{
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException
	{
		HttpServletRequest httpServletRequest = (HttpServletRequest)req;
		System.out.println("Filter lol " + httpServletRequest.getSession().getId());
		chain.doFilter(req, resp);
	}

	public void init(FilterConfig config) throws ServletException
	{

	}

}
