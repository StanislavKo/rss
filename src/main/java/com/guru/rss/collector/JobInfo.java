package com.guru.rss.collector;

import java.util.HashSet;
import java.util.Set;

public class JobInfo {

	private String title;
	private String url;
	private String desc;
	private String paidMoney;
	private String rangeMoney;
	private Set<String> tags;
	public JobInfo() {
		super();
		tags = new HashSet<String>();
	}
	public JobInfo(String title, String url, String desc, String paidMoney, String rangeMoney, Set<String> tags) {
		super();
		this.title = title;
		this.url = url;
		this.desc = desc;
		this.paidMoney = paidMoney;
		this.rangeMoney = rangeMoney;
		this.tags = tags;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getPaidMoney() {
		return paidMoney;
	}
	public void setPaidMoney(String paidMoney) {
		this.paidMoney = paidMoney;
	}
	public String getRangeMoney() {
		return rangeMoney;
	}
	public void setRangeMoney(String rangeMoney) {
		this.rangeMoney = rangeMoney;
	}
	public Set<String> getTags() {
		return tags;
	}
	public void setTags(Set<String> tags) {
		this.tags = tags;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desc == null) ? 0 : desc.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobInfo other = (JobInfo) obj;
		if (desc == null) {
			if (other.desc != null)
				return false;
		} else if (!desc.equals(other.desc))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
}
