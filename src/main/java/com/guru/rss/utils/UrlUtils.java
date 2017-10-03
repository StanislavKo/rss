package com.guru.rss.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.util.EntityUtils;

public class UrlUtils {

	public static String getUrlContent(String theUrl) {
		StringBuilder content = new StringBuilder();
		// many of these calls can throw exceptions, so i've just
		// wrapped them all in one try/catch statement.
		try {
			// create a url object
			URL url = new URL(theUrl);
			// create a urlconnection object
			URLConnection urlConnection = url.openConnection();
			// wrap the urlconnection in a bufferedreader
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			String line;

			// read from the urlconnection via the bufferedreader
			while ((line = bufferedReader.readLine()) != null) {
				content.append(line + "\n");
			}
			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static String getUrlContent2(String url) {
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			httpclient.setRedirectStrategy(new DefaultRedirectStrategy());
//			HttpClient httpclient = HttpClientBuilder.
			System.out.println(new Date() + " getUrlContent2() [url:" + url + "]");
			HttpUriRequest httpRequest = null;
			httpRequest = new HttpGet(url); 

			HttpResponse response = httpclient.execute(httpRequest);
			
			if (response.getStatusLine().getStatusCode() == 429) {
				System.out.println(new Date() + " getUrlContent2() error 429/first time");
				Thread.currentThread().sleep(10*1000);
				EntityUtils.consume(response.getEntity());

				httpclient = new DefaultHttpClient();
				httpclient.setRedirectStrategy(new DefaultRedirectStrategy());
				response = httpclient.execute(httpRequest);
			}
			
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();  
//				System.out.println("getUrlContent2() [url:" + url + "] before toString() ");
				String content = getEntityString(entity.getContent());
//				System.out.println("getUrlContent2() [url:" + url + "] [content.length:" + content.length() + "]");
				return content;
			} else {
				System.out.println(new Date() + " getUrlContent2() error [StatusCode:" + response.getStatusLine().getStatusCode() + "]");
				try {
					HttpEntity entity = response.getEntity();  
//					System.out.println("getUrlContent2() 05");
					String content = getEntityString(entity.getContent());
//					System.out.println("getUrlContent2() 06 [content.length:" + content.length() + "]");
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return "";
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
	
	private static String getEntityString(InputStream is) {
		try {
			BufferedReader reader = null;
			reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8*1024); 
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			if (sb.length() > 0) {
				sb.delete(sb.length() - 1, sb.length());
			}
			is.close();
			return sb.toString();
//			res = new JSONArray(sb.toString());
		} catch (Exception e) {
//			Log.e("UrlUtils", "getEntityString() Error converting result " + e.toString());
		}
		return "";
	}

}
