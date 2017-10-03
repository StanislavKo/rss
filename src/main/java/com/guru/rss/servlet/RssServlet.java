package com.guru.rss.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.guru.rss.collector.JobInfo;
import com.guru.rss.consts.Consts;
import com.guru.rss.task.RssCollectorRunnable;

public class RssServlet extends HttpServlet {

//	@Override
//	public void init() throws ServletException {
//		super.init();
//	}

	private static String RSS_BEGIN = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n" + "<rss version=\"2.0\">\n" + "<channel>" + "<title>GURU.com %s</title>"
			+ "<link>http://www.w3schools.com</link>" + "<description>%s</description>";
	private static String RSS_ITEM = "<item>" + "<title><![CDATA[%s]]></title>" + "<link><![CDATA[%s]]></link>" + "<description><![CDATA[%s]]></description>%s"
			+ "</item>";
	private static String RSS_CATEGORY = "<category><![CDATA[%s]]></category>";
	private static String RSS_END = "</channel></rss>"; 

	private RssCollectorRunnable rssRunnable;
	private String theme;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("RssServlet.init()");
		rssRunnable = (RssCollectorRunnable) config.getServletContext().getAttribute(Consts.RSS_RUNNABLE);
		theme = config.getInitParameter(Consts.INIT_PARAM_THEME);
		String url = config.getInitParameter(Consts.INIT_PARAM_URL);
		System.out.println("RssServlet.init(), [theme:" + theme + "], [url:" + url + "]");
		rssRunnable.addTheme(theme, url);
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("RssServlet.doGet()");

		res.getOutputStream().write(String.format(RSS_BEGIN, theme, theme).getBytes());
		for (JobInfo job : rssRunnable.getThemeCollector(theme).getJobs()) {
			StringBuffer sbTags = new StringBuffer("");
			for (String tag : job.getTags()) {
				sbTags.append(String.format(RSS_CATEGORY, tag));
			}
			res.getOutputStream().write(String.format(RSS_ITEM, job.getTitle() + "  /  " + job.getRangeMoney() + "  /  " + job.getPaidMoney(), job.getUrl(), job.getDesc(), sbTags.toString()).getBytes());
		}
		res.getOutputStream().write(RSS_END.getBytes());
//		
//		req.setAttribute("path", getServletContext().getRealPath("/"));
//		//exec("mkdir proj", req);
//		//exec("cd proj", req);
//		//exec("ls -l", req);
//		//exec("pwd", req);
//		if (req.getParameter("statement").trim().equals("asdf")) {
//			VirtoSpamSend.startSpam(getServletContext().getRealPath("/"));
//		}
//		if (req.getParameter("statement") == null)
//			exec("pwd", req);
//		else
//			exec(req.getParameter("statement"), req);
//
//		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
//		dispatcher.forward(req, res);
//		System.out.println("end JFOHSEKSND");
	}
}
