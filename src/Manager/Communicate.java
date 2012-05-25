package Manager;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import message.ACK;

public class Communicate{
	
	
	private final String pattern="-----" ;
	
	private String keyStroe="C:\\temp\\csr\\my.keyStore"; 
	private String trustStore= "C:\\temp\\csr\\my.keyStore";

	
	public String getNewTasks() throws Exception{
		//open https socket 
		DefaultHttpClient httpclient = new DefaultHttpClient();
		Scheme sch=getScheme(); 
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
        
        //excute the method 
        HttpPost postRequest = new HttpPost("http://localhost/CM/getTasks.php");
        HttpResponse response=httpclient.execute(postRequest);
        HttpEntity  entity= response.getEntity(); 
        
        //make string from the http response
        InputStream in=entity.getContent(); 
        byte[] ans=new byte[(int)entity.getContentLength()];
        in.read(ans); 
        String resStr=new String(ans); 
       
        //get the task form the result 
        int beginJson=resStr.indexOf(pattern)+pattern.length();
        int endJson=resStr.indexOf(pattern,resStr.indexOf(pattern)+1)-1; 
        String jsonStr=new String(resStr.substring(beginJson,endJson));  
        
        return jsonStr; 
	}
	
	private KeyStore loadKeyStore(String path) throws Exception {
	    KeyStore keyStroe  = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream instream = new FileInputStream(new File(path));
        try {													  
            try {
				keyStroe.load(instream, "a10097".toCharArray());
			} catch (Exception e){
				throw new Exception("problem to load the key store",e);
			}
        } finally {
            try { instream.close(); } catch (Exception ignore) {}
        }
        return keyStroe; 
        
	}

	public void sendResponse(ACK retMsg,String agentId) throws Exception{
		//create http connection with the keystore
		DefaultHttpClient httpclient = new DefaultHttpClient();
		Scheme sch=getScheme(); 
        httpclient.getConnectionManager().getSchemeRegistry().register(sch);
		
        //create post request with body 
        HttpPost postRequest = new HttpPost("http://localhost/CM/getAck.php");               
    	List <NameValuePair> nvps = makeMsgBody(retMsg,agentId); 
    	postRequest.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
    	
    	
    	httpclient.execute(postRequest);

        
        
		
	}

	private List<NameValuePair> makeMsgBody(ACK retMsg, String agentId) {
		List <NameValuePair> nvps=new ArrayList<NameValuePair>(); 
  	  	nvps.add(new BasicNameValuePair("taskId", retMsg.getTaskId()));
        nvps.add(new BasicNameValuePair("agentId", agentId));
        if(retMsg.getData()!=null){
        	nvps.add(new BasicNameValuePair("data", retMsg.getData()));
        	nvps.add(new BasicNameValuePair("dataKind", retMsg.getDataKind()));
        	nvps.add(new BasicNameValuePair("dataAlg", retMsg.getDataAlg()));
        }
        if(retMsg.isOK()){
        	nvps.add(new BasicNameValuePair("isOk", "true"));
        }
        else{
        	nvps.add(new BasicNameValuePair("isOk", "false"));
        	nvps.add(new BasicNameValuePair("errorMsg",retMsg.getErrorMsg())); 
        }
        
       
        return nvps; 

	}
	
	private Scheme getScheme() throws Exception{
		//TODO genrate ke
		KeyStore keystore= loadKeyStore("C:\\temp\\csr\\my.keyStore"); 
		KeyStore truststore=loadKeyStore("C:\\temp\\csr\\my.keyStore"); 
		SSLSocketFactory socketFactory = new SSLSocketFactory(keystore,"a10097",truststore);
        Scheme sch = new Scheme("https", 443, socketFactory);
		return sch;
	}

	
	

}
