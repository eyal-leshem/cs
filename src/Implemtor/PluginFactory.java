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
  
	public   ArrayList<Class> getClassArr(String dicPath) throws  AgentServiceException
   {

       File dic = new File(dicPath);
       
       if(!dic.isDirectory())
       {
           throw new AgentServiceException(" not good path - no such dir :"+dicPath);
       }
       

       
       //get the file list 
	   files = dic.listFiles();

	   //create new empty arraylist of imlemntors 
       ArrayList<Class> arr = new ArrayList<Class>();
       
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
        		  Class aClass=(Class)getImplementorClass(fileName , files[i].toURL());        		         		    		  
        		  arr.add(aClass);
        	   }
        	   catch (Exception e) {
        		 //System.out.println(e.getMessage());
				 logger.error("problem to add plugin from file" + fileName+ "problem is : " + e.getMessage());
			}
           }
       }
       
       return arr;

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
		
	
		
		return aClass; 
		

	}
	
	
	
	/**
	 * Method for loading one implementor according the path we got
	 * 
	 * @param dicPath
	 * @return
	 * @throws Exception
	 */
	public Class getOneClass(String path) throws Exception
	{
		File fileObj = new File(path);
	     
				
		if(!(path.endsWith(".jar")))
		{
			return null;
           //throw new CMnotFound(" The file in the path " + dicPath " is not a correct jar ", null);
		}
		
		String fileName=new File(path).getName();
		//if it is a correct jar - return the constructor of the class
		return (Class)getImplementorClass(fileName, fileObj.toURL());
		
	}
	
	
}