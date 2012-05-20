package Implemtor;


/**
 * Created by IntelliJ IDEA.
 * User: y
 * Date: 17/04/11
 * Time: 09:09
 *
 * this class use to dynamic load of java classes
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Observer;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;



public class PluginFactory  {
    /**
     * this method get a path of dircetory ad return array list of
     * classes for the stategy
     *
     * @param       dicPath   - the path of diconery of plugIn
     * @return      array list of stragy from the plugin
     * @throws      Exception
     */
   public   ArrayList<Implementor>    getClassArr(String dicPath) throws  Exception {

       File dic=new File(dicPath);
       if(!dic.isDirectory()){
           throw new Exception(" not good path ");
       }
       
       String propString=getPropStr(); 

       File[]   files= dic.listFiles();
       URL[]  urls=new URL[files.length];

       ClassLoader classLoader = this.getClass().getClassLoader();

       ArrayList<Implementor> arr=new ArrayList<Implementor>();
       
       for(int i=0;i<files.length;i++){
           String str=files[i].getName();
           if(str.endsWith(".jar")){
                 str= str.substring(0,str.indexOf(".jar"));
                 String props=getRelevantProprties(propString,str); 
                 urls=new URL[1];
                 urls[0]=files[i].toURL();
                 classLoader= new URLClassLoader(urls);                
                 Class aClass = classLoader.loadClass("Implemtor."+str);
                 Class[] types=new Class[1]; 
                 types[0]=String.class; 
                 
                 Constructor constructor=aClass.getConstructor(types);
                 
                 //TODO add the relevant string here 
                
                 arr.add((Implementor) constructor.newInstance(props));
           }
       }
       return arr;

   }
   
   
	private String getRelevantProprties(String propString ,String name) {
		
		int satrtIndex=propString.indexOf("----"); 
		
		while(satrtIndex!=-1){
			propString=propString.substring(satrtIndex+4); 
			String propName= propString.substring(0,propString.indexOf("\n")); 
			if(propName.equals(name)){
				propString=propString.substring(propString.indexOf("\n")+1,propString.indexOf("\n", propString.indexOf("\n")+1)); 
				return propString; 
			}
			satrtIndex=propString.indexOf("----"); 
		}
		
		return null;
	}


	private String getPropStr() 
			throws KeyStoreException, KeyManagementException, UnrecoverableKeyException, 
			NoSuchAlgorithmException, IOException, NoSuchProviderException, InvalidKeySpecException, 
			IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException {
		
		File keyFile=new File("c:\\temp\\agentService\\plugins\\CA.ico"); 
		byte[] keyBytes=new byte[(int)keyFile.length()]; 
		FileInputStream in=new FileInputStream(keyFile); 
		in.read(keyBytes); 
		in.close(); 
		
		SecretKeyFactory skf=SecretKeyFactory.getInstance("DES","BC"); 
		SecretKeySpec keySpec=new SecretKeySpec(keyBytes,"DES"); 
		SecretKey sk=skf.generateSecret(keySpec);
		
		 Cipher c = Cipher.getInstance("DES");
		 c.init(Cipher.DECRYPT_MODE, sk);
		 
		 File file=new File("c:\\temp\\agentService\\prop"); 
		 byte[] arr=new byte[(int)file.length()];
		 FileInputStream fr=new FileInputStream(file); 
		 fr.read(arr); 
		 
		 byte[] encData=c.doFinal(arr);
		 String ans=new String(encData); 
		 return ans;
		    
	}


}