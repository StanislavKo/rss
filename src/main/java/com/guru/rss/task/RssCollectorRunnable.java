package com.guru.rss.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.guru.rss.collector.JobInfo;
import com.guru.rss.collector.ThemeCollector;
import com.guru.rss.utils.DownloadUtils;
import com.guru.rss.utils.ParseUtils;

public class RssCollectorRunnable implements Runnable {

	private Boolean isUpToDate = true;
	
	private Map<String, ThemeCollector> themeCollectors = new HashMap<String, ThemeCollector>();
//	private Set<JobInfo> jobIds = new LinkedHashSet<JobInfo>();
	private Integer lastSize;
	
	public void run() {
		while (isUpToDate) {
			System.out.println("RssCollectorRunnable, LOOP [themes:" + themeCollectors.size() + "]");
			for (ThemeCollector collector : themeCollectors.values()) {
				String html = DownloadUtils.getPage(collector.getUrl());
				System.out.println("RssCollectorRunnable, [html:" + html.length() + "], [lastHtml:" + collector.getLastHtmlSize() + "]");
				if (html.length() != collector.getLastHtmlSize()) {
					collector.setLastHtmlSize(html.length());
					System.out.println("RssCollectorRunnable, [theme:" + collector.getTheme() + "], [html:" + html.length() + "]");
					List<JobInfo> jobs = ParseUtils.getJobs(html);
					System.out.println("RssCollectorRunnable, [theme:" + collector.getTheme() + "], [jobs:" + jobs.size() + "]");
					for (JobInfo job : jobs) {
						if (!collector.getJobs().contains(job)) {
							collector.getJobs().add(0, job);
							System.out.println("RssCollectorRunnable,      [title:" + job.getTitle() + "], [paid:" + job.getPaidMoney() + "], [range:" + job.getRangeMoney() + "], [desc:" + job.getDesc() + "], ");
//							for (String tag : job.getTags()) {
//								System.out.println("RssCollectorRunnable,              [tag:" + tag + "]");
//							}
//							jobIds.add(job);
						}
					}
				}
			}
			try {
				Thread.currentThread().sleep(60*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void addTheme(String theme, String url) {
		themeCollectors.put(theme, new ThemeCollector(theme, url));
	}
	
	public ThemeCollector getThemeCollector(String theme) {
		return themeCollectors.get(theme);
	}
	
	public void stopWorking() {
		isUpToDate = false;
	}
	
}
