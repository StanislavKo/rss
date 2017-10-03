package com.guru.rss.servlet;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guru.rss.consts.Consts;
import com.guru.rss.utils.ProcessXMLUtils;
import com.guru.rss.utils.UrlUtils;

public class UpworkServlet extends HttpServlet {

	private String urlBegin;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("UpworkServlet.init()");
		urlBegin = config.getInitParameter(Consts.INIT_PARAM_URL);
		System.out.println("UpworkServlet.init(), [urlBegin:" + urlBegin + "]");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println("UpworkServlet.doGet()");
			String area = req.getParameter("area");
			// replace " " with "+"
			area = area.replaceAll(" ", "+");

			String theUrl = urlBegin + area;
			String xmlStr = UrlUtils.getUrlContent2(theUrl);
			if (xmlStr.length() == 0) {
				return;
			}

			// 01 - item guid
//			System.out.println("pre-processed xml [theUrl:" + theUrl + "] [size:" + xmlStr.length() + "]");
//			try {
//				xmlStr = ProcessXMLUtils.addItemGuid(xmlStr);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}

			// 02 - trim initial space
			try {
				xmlStr = xmlStr.trim();
				while (xmlStr.charAt(0) == 10 || xmlStr.charAt(0) == 13) {
					xmlStr = xmlStr.substring(1, xmlStr.length());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 03 - copyright incorrect symbol
			try {
				Pattern pattern = Pattern.compile("<copyright>.*</copyright>");
				Matcher matcher = pattern.matcher(xmlStr);
				xmlStr = matcher.replaceAll("<copyright>2003-2015 Upwork Corporation</copyright>");
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("final xml [theUrl:" + theUrl + "] [size:" + xmlStr.length() + "]");

			// Set to expire far in the past.
			response.setHeader("Expires", "Sat, 6 May 1995 12:00:00 GMT");
			// Set standard HTTP/1.1 no-cache headers.
			response.setHeader("Cache-Control", "private, no-store, no-cache, must-revalidate");
			// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
			response.addHeader("Cache-Control", "post-check=0, pre-check=0");
			// Set standard HTTP/1.0 no-cache header.
			response.setHeader("Pragma", "no-cache");

			response.getOutputStream().write(xmlStr.getBytes());
//			
//			req.setAttribute("path", getServletContext().getRealPath("/"));
//			//exec("mkdir proj", req);
//			//exec("cd proj", req);
//			//exec("ls -l", req);
//			//exec("pwd", req);
//			if (req.getParameter("statement").trim().equals("asdf")) {
//				VirtoSpamSend.startSpam(getServletContext().getRealPath("/"));
//			}
//			if (req.getParameter("statement") == null)
//				exec("pwd", req);
//			else
//				exec(req.getParameter("statement"), req);
	//
//			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
//			dispatcher.forward(req, res);
//			System.out.println("end JFOHSEKSND");
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
