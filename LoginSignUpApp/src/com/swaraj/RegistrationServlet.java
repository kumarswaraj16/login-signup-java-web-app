package com.swaraj;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.GenericServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RegistrationServlet extends GenericServlet{
	
	private Connection con;
	private PreparedStatement ps = null;
	
	public void init() throws ServletException {
		System.out.println("In Init Method!");
		try {
			ServletContext ctxt = getServletContext();
			String driverClassName = ctxt.getInitParameter("driverClassName");
			Class.forName(driverClassName);
			
			String dbType = ctxt.getInitParameter("dbType");
			String url = ctxt.getInitParameter("url");
			String dbName = ctxt.getInitParameter("dbName");
			String dbuser = getInitParameter("dbuser");
			String dbpass = getInitParameter("dbpass");
			String sqlst = getInitParameter("sqlstatement");
			
			con = DriverManager.getConnection(dbType+url+dbName,dbuser,dbpass);
			ps = con.prepareStatement(sqlst);
		}catch(Exception e) {
			e.printStackTrace();
			throw new ServletException("Initialization Failed, Unable to get DB Connection!");
		}
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("In Service Method!");
		res.setContentType("text/html");
		PrintWriter out = res.getWriter();
		try {
			String uname = req.getParameter("uname");
			String pass = req.getParameter("pass");
			String repass = req.getParameter("repass");
			
			if(uname==null || uname.equals("") || pass==null || pass.equals("") || !pass.equals(repass)) {
				out.println("<html><body><center>");
				out.println("<li><i>");
				out.println("Given Details are not valid to register</i></li><br/>");
				out.println("<li><i>Please try again later</i>");
				out.println("</center></body></html>");
				return;
			}
			
			String addr = req.getParameter("addr");
			String phno = req.getParameter("phno");
			String email = req.getParameter("email");
			
			ps.setString(1, uname);
			ps.setString(2, pass);
			ps.setString(3, addr);
			ps.setString(4, phno);
			ps.setString(5, email);
			
			int count = ps.executeUpdate();
			
			if(count==1 || count==Statement.SUCCESS_NO_INFO) {
				out.println("<html><body>");
				out.println("<center><h1>XYZ Company Ltd.</h1></center>");
				out.println("<table border=\"1\" width=\"100%\" height=\"100%\">");
				out.println("<tr>");
				out.println("<td width=\"15%\" valign=\"top\" align=\"center\">");
				out.println("<br/><a href=\"Login.html\">Login</a><br/>");
				out.println("<br/><a href=\"Register.html\">Register</a><br/>");
				out.println("</td>");
				out.println("<td valign=\"top\" align=\"center\"><br/>");
				out.println("<h3> welcome, "+uname+"</h3><br/>");
				out.println("<h2>Enjoy Browsing the Site</h2>");
				out.println("</td></tr></table>");
				out.println("</body></html>");
			}else {
				out.println("<html><body><center>");
				out.println("Given Details are incorrect<br/>");
				out.println("<li><i>Please try again later</i>");
				out.println("</center></body></html>");
			}
		}catch(Exception e) {
			out.println("<html><body><center>");
			out.println("<h2>Unable to the process the request try after some times</h2>");
			out.println("</center></body></html>");
		}
	}
	
	public void destroy() {
		System.out.println("In Destroy Method!");
		try {
			con.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
