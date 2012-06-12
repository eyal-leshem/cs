package Manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import Logger.AgentServiceLogger;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import exceptions.AgentServiceException;

import message.ACK;

public class Communicate{
	
	
	private final String pattern="*****";
	
	static AgentServiceLogger logger= AgentServiceLogger.getInstance(); 

	/**
	 * ans the server for new task 
	 * 
	 * @param conf
	 * @return string that contain an json task description
	 * @throws AgentServiceException
	 */
	public String getNewTasks(AgentServiceConf conf) throws AgentServiceException{
		
		logger.info("comunicate:agent ask the server for new tasks");
		
		//open https socket 
		DefaultHttpClient httpclient = new DefaultHttpClient();
		Scheme sch=getScheme();
		httpclient.getConnectionManager().getSchemeRegistry().register(sch);
        
    	List <NameValuePair> nvps=new ArrayList<NameValuePair>(); 
  	  	nvps.add(new BasicNameValuePair("agentName",conf.getAgentName()));
  	  	 
        
        //create the post request 
        HttpPost postRequest = new HttpPost(conf.getUrlGetTask());
        
        //set hte entity in post request 
        try {
			postRequest.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new AgentServiceException("problem with post reqwest"); 
		} 
        
 
        String resStr=excutePost(postRequest,httpclient);
       
        //get the task form the result 
        int beginJson=resStr.indexOf(pattern)+pattern.length();
        int endJson=resStr.indexOf(pattern,resStr.indexOf(pattern)+1); 
        String jsonStr=new String(resStr.substring(beginJson,endJson));  
        
        return jsonStr; 
	}
	
	/**
	 * send ack message to the server 
	 * @param retMsg - the messge to rerun 
	 * @param conf - configuration of an agent service 
	 * @throws AgentServiceException
	 */
	public void sendResponse(ACK retMsg,AgentServiceConf conf) throws AgentServiceException{
		
		if(retMsg.isOK())
			logger.info("Comunicate : send ack to the server on task :"+retMsg.getTaskId()); 
		else 
			logger.info("Comunicate : send nack to the server on task :"+retMsg.getTaskId());
		
		//create http connection with the keystore
		DefaultHttpClient httpclient = new DefaultHttpClient();
		Scheme sch=getScheme(); 
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
		
        //create post request with body  
        HttpPost postRequest = new HttpPost(conf.getUrlSendAck());               
    	List <NameValuePair> nvps = makeMsgBody(retMsg,conf.getAgentName());
    	
    	
    	try {
			postRequest.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new AgentServiceException("post the message body in the post reqwest",e);
		}
    	
    	String resString=excutePost(postRequest, httpclient);
    	
		
	}

	public void newImpInform(String impId,AgentServiceConf conf) throws AgentServiceException{
		
	     logger.info("Comunicate : sen to server inform about new implementor -"+ impId); 
	
		
		//create http connection with the keystore
		DefaultHttpClient httpclient = new DefaultHttpClient();
		Scheme sch;
		
		try {
			sch = getScheme();
		} catch (AgentServiceException e) {
			throw new AgentServiceException("can't send the confirm message to server" ,e); 
		} 
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
		
        //create post request with body  
        HttpPost postRequest = new HttpPost(conf.getUrlSendAck()); 
        List <NameValuePair> nvps=new ArrayList<NameValuePair>(); 
  	  	nvps.add(new BasicNameValuePair("agentId",conf.getAgentName()));
  	  	nvps.add(new BasicNameValuePair("impId",impId));
    	    	    	
    	try {
			postRequest.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new AgentServiceException("post the message body in the post reqwest",e);
		}
    	
    	String resString=excutePost(postRequest, httpclient);
		
	}
	
	
	/**
	 * exucte post method 
	 * @param postRequest
	 * @param httpclient
	 * @return
	 * @throws AgentServiceException
	 */
	private String excutePost(HttpPost postRequest, DefaultHttpClient httpclient) throws AgentServiceException {
        //excute the methos 
        HttpResponse response;
		try {
			response = httpclient.execute(postRequest);
		} catch (Exception e){
			throw new AgentServiceException("problem while excuting post method", e);  
		}
        HttpEntity  entity= response.getEntity(); 
        
        //get the data from the response into inputstraem 
        InputStream in;
		try {
			in = entity.getContent();
		} catch (Exception e){
			throw new AgentServiceException("problem to get the data from the response", e);
		}
		
	     
		//read the answer into a byte array and make string from it 
		byte[] ans=new byte[(int)entity.getContentLength()];
	    try {
			in.read(ans);
		} catch (IOException e) {
			throw new AgentServiceException("can't read the nsg from the input stream of the response"); 
		} 
	    
        return  new String(ans);
	}

	/*
	 * load an keystore object 
	 */
	private KeyStore loadKeyStore(String path) throws AgentServiceException {
	    
		//get the keystore class 
		KeyStore keyStroe;
		try {
			keyStroe = KeyStore.getInstance(KeyStore.getDefaultType());
		} catch (KeyStoreException e) {
			throw new AgentServiceException("can't get istance of keystore class" ,e); 
		}
		
		//get the inputsteam fron the oath 
        FileInputStream instream;
		try {
			instream = new FileInputStream(new File(path));
		} catch (FileNotFoundException e) {
			throw new AgentServiceException("can't generate keystore file from :"+path, e);
		}
        
		//load the keysotre 
        try {													  
            try {
				keyStroe.load(instream, "a10097".toCharArray());
			} catch (Exception e){
				throw new AgentServiceException("problem to load the key store",e);
			}
        } finally {
            try { instream.close(); } catch (Exception ignore) {}
        }
        
        return keyStroe; 
        
	}

	
	/*
	 * build the message body 
	 */
	private List<NameValuePair> makeMsgBody(ACK retMsg, String agentId) {
		
		List <NameValuePair> nvps=new ArrayList<NameValuePair>(); 
  	  	
		//values for all  ack messege 
		nvps.add(new BasicNameValuePair("taskId", retMsg.getTaskId()));
        nvps.add(new BasicNameValuePair("agentId", agentId));
        nvps.add(new BasicNameValuePair("impId", retMsg.getImplemntorName()));
      
        //values when a data is set 
        if(retMsg.getData()!=null){
        	nvps.add(new BasicNameValuePair("data", retMsg.getData()));
        	nvps.add(new BasicNameValuePair("dataKind", retMsg.getDataKind()));
        	nvps.add(new BasicNameValuePair("dataAlg", retMsg.getDataAlg()));
        }
       
        //values for successes or fail 
        if(retMsg.isOK()){
        	nvps.add(new BasicNameValuePair("isOk", "true"));
        }
        else{
        	nvps.add(new BasicNameValuePair("isOk", "false"));
        	nvps.add(new BasicNameValuePair("errorMsg",retMsg.getErrorMsg())); 
        	nvps.add(new BasicNameValuePair("fullException",retMsg.getFullExceptionString()));
        }
        
       
        return nvps; 

	}
	
	/*
	 * return a Scheme for https connection with the keystore 
	 * and the trust store of the agent service 
	 */
	private Scheme getScheme() throws AgentServiceException{
		
		
		String here=new File(".").getAbsolutePath();
		String selesh= System.getProperty("file.separator"); 
		
		//path of keystore and truststore 
		String ksPath=here+selesh+"keystore"+selesh+"my.ks";
		String truststorePath=here+selesh+"keystore"+selesh+"my.ts"; 
	
	    //load the keystore and truststore 	 
		KeyStore keystore = loadKeyStore(ksPath);		
		KeyStore truststore=loadKeyStore(truststorePath); 
		
		//generate ssl socket 
		SSLSocketFactory socketFactory;
		try {
			socketFactory = new SSLSocketFactory(keystore,"a10097",truststore);
		} catch (Exception e){
			throw new AgentServiceException("can't generate ssl-socket",e); 
		}
		
		 //return the new scheme 
        Scheme sch = new Scheme("https", 443, socketFactory);
		return sch;
	}

	
	

}
