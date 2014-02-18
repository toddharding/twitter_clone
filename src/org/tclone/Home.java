package org.tclone;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Servlet implementation class LoginServlet
 */

@WebServlet(name = "HomeServlet", urlPatterns = { "/home" })
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String LoginForm = "/login_form.jsp";
       
    /**
     * @see javax.servlet.http.HttpServlet#HttpServlet()
     */
    public Home() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();

		if (session.isNew()) {
			System.out.println("This is a new session");
			} else {
			System.out.println("Welcome back!");
			}

		if(session.getAttribute("isUserLoggedIn") != null)
		{
			System.out.println("Enter A");
			if(!(boolean)session.getAttribute("isUserLoggedIn"))
			{
				loadLoginForm(request, response);
			}
			else
			{
				System.out.println("Enter C");
				response.getOutputStream().println("<p>user is logged in</p>");
			}
		}
		else
		{
			loadLoginForm(request, response);
		}

	}

	private void loadLoginForm(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Enter B");
		ServletContext context = getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(LoginForm);
		dispatcher.forward(request, response);
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
