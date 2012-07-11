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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

import Logger.AgentServiceLogger;

import exceptions.AgentServiceException;





public class PluginFactory
{
	
	AgentServiceLogger logger= AgentServiceLogger.getInstance(); 
	
	URL[]  urls;
	
	File[]   files;
	
	ClassLoader classLoader;
	
	String propString;
	
	static private PluginFactory inst=null; 
	
	private PluginFactory() {}
	
	public static PluginFactory getInstance(){
		if(inst==null){
			return new PluginFactory(); 
		}
		return inst;
	}
	
	
    /**
     * this method get a path of a plugin directory ad return array list of
     * implementor that loaded plugin that in that dir 
     *
     * @param       dicPath   - the path of directory of plugIn
     * @return      array list of implementors
     * @throws      Exception
     */
   public   ArrayList<Implementor> getClassArr(String dicPath) throws  AgentServiceException
   {

       File dic = new File(dicPath);
       
       if(!dic.isDirectory())
       {
           throw new AgentServiceException(" not good path - no such dir :"+dicPath);
       }
       
       //all the properties are encrypted  in file 
       //here we decrypte that file
       try{
    	   propString = getPropStr();
       }
       catch (AgentServiceException e) {
    	   throw new AgentServiceException("can't read propeties string from file",e); 
       }
       
       //get the file list 
	   files = dic.listFiles();

	   //create new empty arraylist of imlemntors 
       ArrayList<Implementor> arr = new ArrayList<Implementor>();
       
       //run over all the files in this dir 
       for(int i=0; i < files.length; i++)
       {
    	   //get the name of file 
           String fileName = files[i].getName();
           //only jar files can contain a plugin 
           if(fileName.endsWith(".jar"))
           {      
        	   try{
        		   //get the constructor of the jar and insert into array of c-tors
        		   arr.add((Implementor) getImplementorClass(fileName , files[i].toURL()));
        	   }
        	   catch (Exception e) {
        		 //System.out.println(e.getMessage());
				 logger.error("problem to add plugin from file" + fileName+ "problem is : " + e.getMessage());
			}
           }
       }
       
       return arr;

   }
   
   /*
    * get the json string that contain the relevant properties to the "name"
    */
	private String getRelevantProprties(String propString ,String name) 
	{
		//any block of properties will statrt by "----"
		//than will con the name 
		//then "\n" 
		//than the properties json string 
		//examle "----yosi\n{"prop1":"1","prop2":"2"}
		int satrtIndex=propString.indexOf("----"); 
		
		//run over all the properties block 
		while(satrtIndex!=-1){
			
			//get the satrt of this properties block 
			propString=propString.substring(satrtIndex+4); 
			String propName= propString.substring(0,propString.indexOf("\n")); 
			
			//bingo 
			if(propName.equals(name)){
				propString=propString.substring(propString.indexOf("\n")+1,propString.indexOf("\n", propString.indexOf("\n")+1)); 
				return propString; 
			}
			//next propeties block 
			satrtIndex=propString.indexOf("----"); 
		}
		
		return null;
	}


	private String getPropStr() 	throws AgentServiceException {
		
		String here=new File(".").getAbsolutePath(); 
		 
	
		
		File keyFile = new File(here+"\\plugins\\CA.ico"); 
		byte[] keyBytes = new byte[(int)keyFile.length()]; 
		
		FileInputStream in;
		try {
			in = new FileInputStream(keyFile);
		} catch (FileNotFoundException e) {
			throw new AgentServiceException("can't genrate input steam from key file", e); 			
		} 
		
		try {
			in.read(keyBytes);
		} catch (IOException e) {
			throw new AgentServiceException("read the bytes from keyfile ", e); 	
		} 
		
		try {
			in.close();
		} catch (IOException e) {} 
		
		SecretKeySpec keySpec;
		try{
			keySpec = new SecretKeySpec(keyBytes,"AES"); 
		}catch (Exception e) {
			throw new AgentServiceException("can't genrate create the secert key spec ", e); 	
		}
		
		
		Cipher c;
		try {
			c = Cipher.getInstance("AES");
		} catch (Exception e){
			throw new AgentServiceException("can't get istance of cipher ", e); 	
		}
		
		
		try {
			c.init(Cipher.DECRYPT_MODE, keySpec);
		} catch (InvalidKeyException e) {
			throw new AgentServiceException("can't init the chiper ", e); 
		}
		
		 
		 
		//the file of the that contain the properties 
		File file = new File(here+"\\prop"); 
		
		if(!file.exists()){
			return ""; 
		}
		
		byte[] arr = new byte[(int)file.length()];
		
		
		try{
			FileInputStream fr = new FileInputStream(file); 
			fr.read(arr); 
		}
		catch (IOException e) {
			throw new AgentServiceException("can't get content of the prop string", e); 
		}
		 
		byte[] encData;
		try {
			encData = c.doFinal(arr);
		} catch (Exception e){
			throw new AgentServiceException("can't decrypt the propeties file ", e); 
		}
		
		String ans = new String(encData); 
		return ans;
		    
	}

	
	/**
	 * Method for getting the constructor instance of the jar file sending him the
	 * 
	 * properties that we got.
	 * @param str - string with url
	 * @param i
	 * @return
	 * @throws AgentServiceException 
	 * @throws Exception
	 */
	public Object getImplementorClass(String str, URL url) throws AgentServiceException 
	{
		//get the name of jar
		str = str.substring(0, str.indexOf(".jar"));
		
		//get the properties for constructor
        String props = getRelevantProprties(propString, str); 
        urls = new URL[1];
        urls[0] = url;
        classLoader = new URLClassLoader(urls); 
        
        //get the class object
        Class aClass;
		try {
			aClass = classLoader.loadClass("Implemtor."+str);
		} catch (ClassNotFoundException e) {
			System.out.println(e);
			throw new AgentServiceException("cann't load the class Implemtor."+str,e); 
		}
        
        Class[] types = new Class[1]; 
        types[0] = String.class; 
        
        Constructor constructor;
        
        //get the constracrtor 
		try {
			constructor = aClass.getConstructor(types);
		} catch (Exception e){
			throw new AgentServiceException("problem get the class c-tor",e); 
		}
		
        try {
			return constructor.newInstance(props);
		} catch (Exception e){
			System.out.println(e.getCause());
			throw new AgentServiceException("problem to get new intance of the class",e); 
		}
	}
	
	
	
	/**
	 * Method for loading one implementor according the path we got
	 * 
	 * @param dicPath
	 * @return
	 * @throws Exception
	 */
	public Implementor getOneClass(String path) throws Exception
	{
		File fileObj = new File(path);
	     
		propString=getPropStr();
		
		if(!(path.endsWith(".jar")))
		{
			return null;
           //throw new CMnotFound(" The file in the path " + dicPath " is not a correct jar ", null);
		}
		
		String fileName=new File(path).getName();
		//if it is a correct jar - return the constructor of the class
		return (Implementor) getImplementorClass(fileName, fileObj.toURL());
		
	}
	
	
}