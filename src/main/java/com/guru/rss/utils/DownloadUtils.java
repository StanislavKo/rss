package com.guru.rss.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class DownloadUtils {

	public static String getPage(String urlPath) {
		BufferedReader in = null;
		try {
			URL myUrl = new URL(urlPath);
			in = new BufferedReader(new InputStreamReader(myUrl.openStream()));
			String line;
			StringBuffer sb = new StringBuffer("");
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	public static void showPage(String urlPath) {
		BufferedReader in = null;
		try {
			URL myUrl = new URL(urlPath);
			in = new BufferedReader(new InputStreamReader(myUrl.openStream()));
			String line;
			StringBuffer sb = new StringBuffer("");
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
