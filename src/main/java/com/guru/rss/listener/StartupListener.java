package com.guru.rss.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.guru.rss.consts.Consts;
import com.guru.rss.task.RssCollectorRunnable;
import com.guru.rss.utils.DownloadUtils;

public class StartupListener implements ServletContextListener {

	private Thread rssCollector;
	private RssCollectorRunnable rssCollectorTask;
	
	public void contextInitialized(ServletContextEvent sce) {
//		DownloadUtils.showPage("http://www.freelancer.com/rss/notify_StanislavKo.xml");
		rssCollectorTask = new RssCollectorRunnable();
		rssCollector = new Thread(rssCollectorTask);
		rssCollector.start();
		
		sce.getServletContext().setAttribute(Consts.RSS_RUNNABLE, rssCollectorTask);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		rssCollectorTask.stopWorking();
	}
}
