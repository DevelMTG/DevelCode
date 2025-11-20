/*
 * Copyright ⓒ 2025 DAHAMI COMMUNICATIONS
 * All rights reserved.
 * -----------------------------------------------------------------------------
 * Modify History
 * date            author         comment
 * ----------      ---------      ----------------------------------------------
 * 2025. 4. 21.     LEE,JAEWOO		최초작성
 * 2025. 4. 21.     
 */

package com.dahami.oncm.api_web.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @see	 
 * @author	LEE,JAEWOO
 * @date	2025. 4. 21. 오전 10:57:39
 * @since	
 */
public class HttpConnUtil {

	private static String HttpsConn(
			URL url, 
			Map<String, Object[]> params, 
			boolean isPost
	) { 
		return HttpsConn(url, params, isPost, null); 
	};
	/**
	 * 전달할 파라미터가 시리얼라이즈 형식일때  
	 * @mAuthor	LEE,JAEWOO
	 * @date	2025. 4. 22. 오전 8:50:02
	 * @param url
	 * @param params
	 * @param isPost
	 * @param requestHeader
	 * @return
	 */
	private static String HttpsConn(
			URL url, 
			Map<String, Object[]> params, 
			boolean isPost, 
			Map<String,String> requestHeader
	) {
		byte[] postDataBytes = null; 
		try {
			StringBuilder postData = new StringBuilder();
			for(Map.Entry<String,Object[]> param : params.entrySet()) {
				for( Object value : param.getValue() ) {
					if (postData.length() != 0) postData.append("&");
		            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
		            postData.append("=");
		            postData.append(URLEncoder.encode(String.valueOf(value), "UTF-8"));
				}
			}
			postDataBytes = postData.toString().getBytes("UTF-8");
		} catch(Exception e) {}
		
		return HttpsConn(url, postDataBytes, isPost, requestHeader);
	}
	/**
	 * 전달할 파라미터가 JSON 형식일때 
	 * @mAuthor	LEE,JAEWOO
	 * @date	2025. 4. 22. 오전 8:50:23
	 * @param url
	 * @param params
	 * @param isPost
	 * @param requestHeader
	 * @return
	 */
	private static String HttpsConnJsonParam(
			URL url, 
			Map<String, Object[]> params, 
			boolean isPost, 
			Map<String,String> requestHeader
	) {
		Map<String,Object> singleParam = new HashMap<>();
		for(Map.Entry<String, Object[]> param : params.entrySet()) {
			singleParam.put(param.getKey(), param.getValue()[0]);
		}
		String postData;
		byte[] postDataBytes = null;
		try {
			postData = new ObjectMapper().writeValueAsString(singleParam);
			postDataBytes = postData.getBytes("UTF-8");
		} catch (Exception e) {}
		
		return HttpsConn(url, postDataBytes, isPost, requestHeader);
	}
	/**
	 * SSL,TLS RestAPI 호출, 실패시 총 2번의 호출을 시도한다. HTTP 버전에 따라 SSL이 안먹힐때가 있어 TLS로 한번더 시도
	 * @mAuthor	LEE,JAEWOO
	 * @date	2025. 4. 22. 오전 8:52:09
	 * @param url
	 * @param postDataBytes
	 * @param isPost
	 * @param requestHeader
	 * @return
	 */
	private static String HttpsConn(
			URL url, 
			byte[] postDataBytes, 
			boolean isPost, 
			Map<String,String> requestHeader
	) {
		// http 통신일 경우, 주석을 풀고 아래 SSL 관련 사항 제거
		//HttpURLConnection conn = null;
		// https 통신
		HttpsURLConnection conn = null;
		String line = null;
		InputStream in = null;
		BufferedReader reader = null; 
		StringBuilder buf = new StringBuilder();
		
		try {
			conn = (HttpsURLConnection)url.openConnection();
			// Set Hostname verification
			conn.setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) { return false; }
			});
			//ssl 인증 우회하는 임시방편이다. 추후에는 ssl인증서를 불러와서 하는 형식으로 하는걸 추천한다. 
			//이 의미는 난 여기에 접속하여 아무것도 하지 않겠다는 동의서 같은것이다. 
			TrustManager[] trustAllCerts = new TrustManager[] { 
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null;	}
					public void checkClientTrusted(X509Certificate[] certs, String authType){ }
					public void checkServerTrusted(X509Certificate[] certs, String authType) { }
				} 
			};

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			
			conn.setDoInput(true);	//기본값이 true지만 확인하기 좋도록 넣어줌
			conn.setUseCaches(false);
			// Read Timeout Setting
			conn.setReadTimeout(3000);
			// Connection Timeout setting
			conn.setConnectTimeout(1000);
			// Method 선택 
			conn.setRequestMethod(isPost ? "POST" : "GET");
			// Header 셋팅
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	        // 추가할 헤더가 있다면
	        if( requestHeader != null ) {
	        	for( String hKey : requestHeader.keySet() ) {
	        		conn.setRequestProperty(hKey, requestHeader.get(hKey));
	        	}
	        }
	        conn.setDoOutput(true);
	        conn.getOutputStream().write(postDataBytes);
	        
			int responseCode = conn.getResponseCode();
//			System.out.println("응답코드 : " + responseCode);
//			System.out.println("응답메세지 : " + conn.getResponseMessage());
			
			// SSL setting
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, null, null); // No validation for now
			conn.setSSLSocketFactory(context.getSocketFactory());
			
			conn.connect();
			conn.setInstanceFollowRedirects(true);	//접속한 사이트에서 보내오는 3xx code일때 redirect 요청을 수락할 것인가를 의미한다.
			//
			if(responseCode >= HttpURLConnection.HTTP_OK 
					&& responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
				in = conn.getInputStream();
			} else { // 에러 발생
				in = conn.getErrorStream();
			}
			
			reader = new BufferedReader(new InputStreamReader(in));
			while ((line = reader.readLine()) != null) {
				buf.append(line)
					.append("\n");
			}
		
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try { reader.close(); } catch (Exception e) {}
            }
            if (conn != null) {
                conn.disconnect(); 
            }
		}
		
		return buf.toString();
	}
}
