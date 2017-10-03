package com.guru.rss.collector;

import java.util.ArrayList;
import java.util.List;

public class ThemeCollector {

	private String theme;
	private String url;
	private Integer lastHtmlSize;
	private List<JobInfo> jobs;

	public ThemeCollector(String theme, String url) {
		super();
		this.theme = theme;
		this.url = url;
		lastHtmlSize = 0;
		jobs = new ArrayList<JobInfo>();
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getLastHtmlSize() {
		return lastHtmlSize;
	}

	public void setLastHtmlSize(Integer lastHtmlSize) {
		this.lastHtmlSize = lastHtmlSize;
	}

	public List<JobInfo> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobInfo> jobs) {
		this.jobs = jobs;
	}
	
}
