package com.twitter.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpVersion;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.guru.rss.collector.JobInfo;
import com.guru.rss.consts.Consts;
import com.guru.rss.task.RssCollectorRunnable;

public class TwitterSearchServlet extends HttpServlet {

	private static String TWITTER_CONSUMER_KEY = "Dftlo0o4aH5iR2Bl1qvI4Q";//"Dftlo0o4aH5iR2Bl1qvI4Q";
	private static String TWITTER_CONSUMER_SECRET = "UFQ35mxEdy1YQeiem2Y8C6HUrdxVBVaSqWTKG0AlZY";//"UFQ35mxEdy1YQeiem2Y8C6HUrdxVBVaSqWTKG0AlZY";
	private static String TWITTER_OAUTH_SIGNATURE_METHOD = "HMAC-SHA1";
	private static String TWITTER_OAUTH_VERSION = "1.0";
	private static String TWITTER_ACCESS_TOKEN = "198503054-187uPc7YzPy7RAeXaCG1K9f0ZSLnWDJi1ngvPLvS";
	private static String TWITTER_ACCESS_TOKEN_SECRET = "O51eFgIt260Qy3j63SY5scE18y0IVonzSzIorXLI4yg";

	private static Object syncObject = new Object();
	private static DefaultHttpClient httpclient;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		System.out.println("TwitterSearchServlet.init()");
		if (httpclient == null || httpclient.getConnectionManager() == null) {
			synchronized (syncObject) {
				System.out.println("RunTwitterSearch init httpClient");
				SchemeRegistry supportedSchemes = new SchemeRegistry();
				SocketFactory sf = PlainSocketFactory.getSocketFactory();
				supportedSchemes.register(new Scheme("http", sf, 80));
				supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));

				HttpParams params = new BasicHttpParams();
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, "UTF-8");
				HttpProtocolParams.setUseExpectContinue(params, true);
//				params.setParameter("Host", "www.facebook.com");
//				params.setParameter("Referer", "http://www.facebook.com/inbox/?compose&id=100001690578122");
//				params.setParameter("", "");
//				params.setParameter("", "");
//				String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 6.1; ru; rv:1.9.2.13) Gecko/20101203 MRA 5.7 (build 03755) Firefox/3.6.13";
				//HttpProtocolParams.setUserAgent(params, userAgent);

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, supportedSchemes);
				httpclient = new DefaultHttpClient(ccm, params);

//				CookieSyncManager.createInstance(context);
//				CookieManager cookieManager = CookieManager.getInstance();
//				String cookies = cookieManager.getCookie("https://login.facebook.com/login.php");
//				addCookies(cookies);
			}
		}
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		System.out.println("TwitterSearchServlet.doGet()");
		
		String question = req.getParameter("question");

		String oauth_nonce = UUID.randomUUID().toString().replaceAll("-", "");
		String timestamp = Long.valueOf(((new Date().getTime() + new Date().getTimezoneOffset()) / 1000)).toString();
		String parameter_string = "count=10&" + "oauth_consumer_key=" + TWITTER_CONSUMER_KEY + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + TWITTER_OAUTH_SIGNATURE_METHOD
				+ "&oauth_timestamp=" + timestamp + "&oauth_token=" + URLEncoder.encode(TWITTER_ACCESS_TOKEN) + "&oauth_version=1.0" + "&q=" + URLEncoder.encode(question).replaceAll("\\+", "%20");
		System.out.println("parameter_string=" + parameter_string);
		String signature_base_string = "GET&https%3A%2F%2Fapi.twitter.com%2F1.1%2Fsearch%2Ftweets.json&" + URLEncoder.encode(parameter_string, "UTF-8");
//		String signature_base_string = "POST&https%3A%2F%2Fapi.twitter.com%2F1%2Fstatuses%2Fupdate.json?include_entities=true&" + URLEncoder.encode(parameter_string, "UTF-8");
		System.out.println("signature_base_string=" + signature_base_string);
		String oauth_signature = "";
		try {
			oauth_signature = computeSignature(signature_base_string, TWITTER_CONSUMER_SECRET + "&" + URLEncoder.encode(TWITTER_ACCESS_TOKEN_SECRET)); // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
			System.out.println("oauth_signature=" + URLEncoder.encode(oauth_signature, "UTF-8"));
		} catch (GeneralSecurityException e) {
			e.printStackTrace(); 
		}
		String authorization_header_string = "OAuth oauth_token=\"" + TWITTER_ACCESS_TOKEN + "\",oauth_consumer_key=\"" + TWITTER_CONSUMER_KEY + "\",oauth_signature_method=\""
				+ TWITTER_OAUTH_SIGNATURE_METHOD + "\",oauth_timestamp=\"" + timestamp + "\",oauth_nonce=\"" + oauth_nonce + "\",oauth_version=\"1.0\",oauth_signature=\""
				+ URLEncoder.encode(oauth_signature, "UTF-8") + "\"";
		System.out.println("authorization_header_string=" + authorization_header_string);

		Map<String, String> responseMap = new HashMap<String, String>();
		HttpGet httpGet = new HttpGet("https://api.twitter.com/1.1/search/tweets.json?q=" + URLEncoder.encode(question) + "&count=10");
		httpGet.setHeader("Authorization", authorization_header_string);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
//		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//		nvps.add(new BasicNameValuePair("q", "potter"));
//		nvps.add(new BasicNameValuePair("include_entities", URLEncoder.encode("true")));
//		httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
//		HttpParams params = new BasicHttpParams();
//		httpGet.setParams(params); 
		String responseBody = httpclient.execute(httpGet, responseHandler);
//		for (String responseParamValue : responseBody) {
//			String[] responseParamValueArr = responseParamValue.split("=");
//			responseMap.put(responseParamValueArr[0], responseParamValueArr[1]);
//			System.out.println("  [" + responseParamValueArr[0] + ":" + responseParamValueArr[1] + "]");
//		}
		System.out.println(responseBody);

		res.getOutputStream().write(responseBody.getBytes());
	}

	private static String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
		SecretKey secretKey = null;
		byte[] keyBytes = keyString.getBytes();
		secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(secretKey);
		byte[] text = baseString.getBytes();
		return new String(Base64.encodeBase64(mac.doFinal(text))).trim();
	}

}
