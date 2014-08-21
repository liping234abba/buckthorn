package com.yaokan.sdk.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.http.AndroidHttpClient;

public class HttpUtil {

	private  String username = "suncam";

    private  String password = "1234321";

    private  String strRealm = "realm";

    private  String userAgent = "(Liunx; u; Android ; en-us; ZTE)";
    
    private String imei ;
    
    public HttpUtil(Context ctx){
    	imei = Utility.getIMEI(ctx);
    }

    public String getMethod(String url) {
    	url = url + "&f="+imei;
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance(userAgent);
        try {
            URL urlObj = new URL(url);
            AuthScope scope = new AuthScope(urlObj.getHost(), urlObj.getPort(), strRealm);
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username, password);
            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(scope, creds);
            HttpContext credContext = new BasicHttpContext();
            credContext.setAttribute(ClientContext.CREDS_PROVIDER, cp);
            HttpGet request = new HttpGet(url);
            request.addHeader("accept-encoding", "gzip,deflate");
            HttpResponse response = httpClient.execute(request, credContext);
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                if (!Utility.isEmpty(entity.getContentEncoding())) {
                    byte[] srcData = EntityUtils.toByteArray(entity);
					byte[] nData = unzip(srcData); //解压
					String key = "12.4567j1234567a";
					String iv = "ztekjlwejlrjwewe";
					Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
					SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(),
							"AES");
					IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
					cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
					byte[] original = cipher.doFinal(nData);
					String originalString = new String(original, "UTF-8");
					originalString = originalString.trim();  //源文
					return originalString ;
                }
            }
            return "";
        }
        catch (Exception e) {
            return "";
        }
        finally {
            if (httpClient != null) {
                httpClient.close();
            }
        }
    }
    
    public byte[] unzip(byte[] srcData) throws IOException {
		InputStream inputStream = new GZIPInputStream(new ByteArrayInputStream(
				srcData));
		ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
		byte[] temp = new byte[1024];
		int len = 0;
		while ((len = inputStream.read(temp, 0, 1024)) != -1) {
			arrayOutputStream.write(temp, 0, len);
		}
		arrayOutputStream.close();
		inputStream.close();
		return arrayOutputStream.toByteArray();
	}

}
