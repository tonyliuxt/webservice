package com.nanjing.au.bookme;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nanjing.au.bookme.mail.MailTaskVO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

// NOTE:
// This class emulates a server for the purposes of this sample,
// but it's not meant to serve as an example for a production app server.
// This class should also not be included in the client (Android) application
// since it includes the server's API key. For information on GCM server
// implementation see: https://developers.google.com/cloud-messaging/server
public class GcmSender { 

	private final static Logger mylogger = LoggerFactory.getLogger(GcmSender.class);

	public static String API_TOKEN = "fE5NaYHypXU:APA91bH9NTycxSXy5Lr2HAFFZlP8AQd_X_yVbbKgKdoWj82MPGB50QL7EFx8Txwqf_FmsjGXVMC6OL0CTgvux-TKBuhthec-IIcUc4pXmcgUS2L3BRQCW00eXkm2XHQk0myh_zoStTN5";
	public static final String API_URL = "https://android.googleapis.com/gcm/send";
	public static final String API_KEY = "";
	
	public static void main(String args[]){
		MailTaskVO uvo = new MailTaskVO();
		uvo.setPhone("0452382276");
		uvo.setToWho("TonyLiu");
		uvo.setBody("can I book a lesson.");
		
		JSONObject nuvo = getNotification(uvo);
		sendGcmData(nuvo);

		//JSONObject muvo = getMessage(uvo);
		//sendGcmData(muvo);
	}

	/**
	 * Send notification to mobile
	 * @param uvo
	 */
    public static JSONObject getNotification(MailTaskVO uvo) {

    	if(uvo == null){
    		return null;
    	}
    	
        // Prepare JSON containing the GCM message content. What to send and where to send.
        JSONObject jGcmData = new JSONObject();
        JSONObject jData = new JSONObject();
        jData.put("title", uvo.getToWho() + ":"+ uvo.getPhone() +":"+uvo.getTo());
        jData.put("text",  uvo.getBody());
             
        // What to send in GCM message.
        jGcmData.put("notification", jData);
    	
        return jGcmData;
    }
    
    /**
     * Send message to mobile
     * @param uvo
     */
    public static JSONObject getMessage(MailTaskVO uvo){
    	
    	if(uvo == null){
    		return null;
    	}
    	
        // Prepare JSON containing the GCM message content. What to send and where to send.
        JSONObject jGcmData = new JSONObject();
        JSONObject jData = new JSONObject();
        jData.put("title", uvo.getToWho());
        jData.put("phone", uvo.getPhone());
        jData.put("text",  uvo.getBody());
             
        // What to send in GCM message.
        jGcmData.put("data", jData);
    	
        return jGcmData;
    }
    
    /**
     * Ref: https://developers.google.com/cloud-messaging/http
     * Send specific data
     * @param data
     */
    public static void sendGcmData(JSONObject data){
        try {
        	if(data != null){

                // Where to send GCM message.
        		data.put("to", API_TOKEN);
                
                // Create connection to send GCM Message request.
                URL url = new URL(API_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Authorization", "key=" + API_KEY);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                // Send GCM message content.
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(data.toString().getBytes());

                // Read GCM response.
                InputStream inputStream = conn.getInputStream();
                String resp = IOUtils.toString(inputStream);
                mylogger.info("response from Gcm:" + resp);
        	}else{
        		mylogger.warn("GcmSender warning: input parameters error.");
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
