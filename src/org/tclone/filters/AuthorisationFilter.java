package org.tclone.filters;

import com.sun.xml.internal.messaging.saaj.util.Base64;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

		System.out.println(httpServletRequest.getMethod());
		System.out.println(httpServletRequest.getRequestURI());
		String method = httpServletRequest.getMethod();
		String uri = httpServletRequest.getRequestURI();
        String auth = httpServletRequest.getHeader("Authorization");
		// if request is a create user request
		// authorization is not needed
		if(method.equals("POST") && uri.equals("/user/"))
		{
			chain.doFilter(req, resp);
		}
		else
		{
			if(auth == null)
			{
				// not auth header redirect
				HttpServletResponse httpResponse = (HttpServletResponse) resp;
				//httpResponse.sendRedirect("/index.jsp");
				httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
			else
			{
				String[] authHeader = auth.split(" ");
				String encodedValue = auth.split(" ")[1];
				System.out.println("authHeader Length: " + authHeader.length );
				for(String s : authHeader)
				{
					System.out.println("authHeader Contents: " + s );
				}
				if(authHeader.length > 1 || authHeader.length < 2 )
				{
					if(authHeader[0].toLowerCase().contains("basic"))
					{
						// do basic authentication
						System.out.println("Base64-encoded Authorization Value: <em>" + encodedValue);
						String decodedValue = Base64.base64Decode(encodedValue);
						System.out.println("</em><br/>Base64-decoded Authorization Value: <em>" + decodedValue);
						System.out.println("</em>");
						String username = decodedValue.split(":")[0];
						String password = decodedValue.split(":")[1];
					}
				}
			}
			System.out.println("Filter lol " + httpServletRequest.getSession().getId());
			chain.doFilter(req, resp);
		}
	}

	public void init(FilterConfig config) throws ServletException
	{

	}

}
