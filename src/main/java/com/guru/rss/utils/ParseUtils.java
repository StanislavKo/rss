package com.guru.rss.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.guru.rss.collector.JobInfo;

public class ParseUtils {

	private static String PAID = "<span class=\"paid\">";
	private static String TITLE = "strong id=\"cxtmenu\"";
	private static String DESCRIPTION = "class=\"projectDescription";
	private static String LINK = "<a href=\"";
	private static String PROPOSAL = "Invited</a>";
	private static String TAG ="buttonGrayDisabled\">";
	
	public static List<JobInfo> getJobs(String html) {
		List<JobInfo> jobs = new ArrayList<JobInfo>();
		while (html.contains(PAID)) {
			JobInfo job = new JobInfo();
			html = html.substring(html.indexOf(PAID) + PAID.length());
			job.setPaidMoney(html.substring(0, html.indexOf("<")));
			html = html.substring(html.indexOf(TITLE) + TITLE.length());
			html = html.substring(html.indexOf(">") + 1);
			job.setTitle(html.substring(0, html.indexOf("<")));
			html = html.substring(html.indexOf(DESCRIPTION) + DESCRIPTION.length());
			html = html.substring(html.indexOf(">") + 1);
			job.setDesc(html.substring(0, html.indexOf("<")).replaceAll("<\\!\\[CDATA\\[", ""));
			html = html.substring(html.indexOf(LINK) + LINK.length());
			job.setUrl(html.substring(0, html.indexOf("\">")));
			html = html.substring(html.indexOf(PROPOSAL) + PROPOSAL.length());
			html = html.substring(html.indexOf("<span>") + 6);
			job.setRangeMoney(html.substring(0, html.indexOf("<")));
			html = html.substring(html.indexOf(TAG));
			while (html.indexOf(TAG) >= 0 && html.indexOf(TAG) < html.indexOf("</div>")) {
				html = html.substring(html.indexOf(TAG) + TAG.length());
				html = html.substring(html.indexOf(">") + 1);
				job.getTags().add(html.substring(0, html.indexOf("<")));
			}
			jobs.add(0, job);
		}
		return jobs;
	}
	
}
