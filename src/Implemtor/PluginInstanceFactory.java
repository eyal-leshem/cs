package Implemtor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import exceptions.AgentServiceException;

public class PluginInstanceFactory {
	
	private static PluginInstanceFactory inst=null;  
	
	public static PluginInstanceFactory getInstance(){
		
		if(inst==null){
			return new PluginInstanceFactory(); 
		}
		return inst;
		
	}
	
	private PluginInstanceFactory (){
		//TODO generate constractor 
	}
	
	public Map<String, Implementor> getImps(Map<String, Class> plugins) throws AgentServiceException{
		
		File 						dic=new File("inst"); 
		ArrayList<Implementor>		impArr=new ArrayList<Implementor>(); 
		Map<String, Implementor> 	impMap=new ConcurrentHashMap<String, Implementor>(); 
		
		//bad path
		if(!dic.isDirectory()){
			throw new AgentServiceException("bad path to directory of instancesses"); 
		}
		
		//list of all the files with intance properties; 
		String[] files= dic.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				if(name.endsWith(".inst")){
					return true; 
				}
				return false; 
			}
		});
		
		for(int i=0;i<files.length;i++){
			try{
				impArr.add(createInst(files[i],plugins)); 
			}
			//problem to generate implemtor from this conf file
			catch (AgentServiceException e) {}
		}
		
		for (Implementor imp : impArr) {
			impMap.put(imp.getName(), imp); 
		}
		
		
		
		return impMap; 
		
	}
	
	private Implementor createInst(String fileName, Map<String, Class> plugins) throws AgentServiceException {
		
		String here=new File(".").getAbsolutePath(); 
		File propFile=new File(here+"/inst/"+fileName); 
		
		if(!propFile.exists())
			throw new AgentServiceException("file -"+fileName+"does not exist"); 
		
		String fileContent=decrypteFile(propFile); 
		
		String propString;
		String pluginName;
		try{
			pluginName=fileContent.substring(0,fileContent.indexOf("*")); 
			propString=fileContent.substring(fileContent.indexOf("{"),fileContent.indexOf("#"));
		}
		catch(Exception e){
			throw new AgentServiceException("problem get prametrs from the decrypted string"); 
		}
		
		//problem with /r or other line sperator from other system; 
		
		
		//get the revant class of this plugin 
		Class impCalss=plugins.get(pluginName); 
		if(impCalss==null){
			throw new AgentServiceException("no plugin with name -"+pluginName); 
		}
		
		//get the constractor of this class 
		Constructor constructor;
		try {
			constructor=impCalss.getConstructor(String.class);
		} catch (Exception e){
			throw new AgentServiceException("can not genrate constctractor for string to plugin -"+pluginName, e);
		}
		
		//create implementor instance
		Implementor imp;
		try {
			imp=(Implementor) constructor.newInstance(propString);
		} catch (Exception e){
			throw new AgentServiceException("can not genrate new intance of contractor", e); 
		}
		
		return imp;  
			
		
		
		
	}

	private String decrypteFile(File propFile)  throws AgentServiceException{
		
		SecretKey key=getKey(); 
		
		//create cipher
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (Exception e){
			throw new AgentServiceException("can not get istance of cipher ", e); 	
		}
		
		//init cipher 
		try {
			cipher.init(Cipher.DECRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			throw new AgentServiceException("can not init the chiper ", e); 
		}
		
		//read content form file
		byte[] encByteData;
		try {
			encByteData=new byte[(int)propFile.length()];
			FileInputStream fis=new FileInputStream(propFile);
			fis.read(encByteData); 
			fis.close();	
		} catch (IOException e) {
			throw new AgentServiceException("problem to read from file -  "+ propFile.getAbsolutePath(), e); 
		} 
		
		//decrypt the data from the the file 
		byte[] decDataBytes;
		try {
			decDataBytes = cipher.doFinal(encByteData);
		} catch (Exception e){
			throw new AgentServiceException("problem to decrypte the file" );
		}
		
		return new String(decDataBytes); 
		 
		
		
		
		
	}

	public Implementor installNewImplementorInstance(Class plugin, String parms) throws AgentServiceException {
		
		//get the constractor of this class 
		Constructor constructor;
		try {
			constructor=plugin.getConstructor(String.class);
		} catch (Exception e){
			throw new AgentServiceException("problem to genarate constractor with type String", e); 
		}
		
		//create an instance of plugin 
		Implementor newImp=null; 
		try {
			newImp=(Implementor)constructor.newInstance(parms);
		} catch (Exception e){
			throw new AgentServiceException("can not instace of the class from the constracror with parmters"+ parms,e); 
		} 
			
		//Encrypt and save the data for this intance 
		try{
			encrypteAndSaveParms(parms,newImp.getName(),plugin.getName().substring("Implemtor.".length())); 
		}catch (AgentServiceException e) {
			throw new AgentServiceException("can not save and encrypte the pameters of this instance");
		}
		
		return newImp; 
		
		
		
	}

	private void encrypteAndSaveParms(String parms,String impName, String pluginName)  throws AgentServiceException  {
		
		String instData=pluginName+"*"+parms+"#"; 
		
		SecretKey key=getKey(); 
		
		//create cipher
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES");
		} catch (Exception e){
			throw new AgentServiceException("can not get istance of cipher ", e); 	
		}
		
		//init cipher 
		try {
			cipher.init(Cipher.ENCRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			throw new AgentServiceException("can not init the chiper ", e); 
		}
		
		//encrypt data 
		byte[] encBytes;
		try {
			encBytes=cipher.doFinal(instData.getBytes());
		} catch (Exception e){
			throw new AgentServiceException("can not encrypte the mmasssege",e); 
		}
		
		//create properties file and check that he is uniqe 
		
		String here=new File(".").getAbsolutePath();
		File f=new File(here+"/inst/"+impName+".inst"); 
		if(f.exists()){
			throw new AgentServiceException("implemtor id need to b uniqe , id-"+impName+" already exist"); 
		}
		
		//write the encoded data to file
		try {
			f.createNewFile();
			FileOutputStream fos=new FileOutputStream(f); 
			fos.write(encBytes); 
			fos.flush(); 
			fos.close();
			   
		} catch (IOException e) {
			throw new AgentServiceException("can not write the propeteis string to file");
		} 
		
	
		
		
		
		
		
	}
	
	private SecretKey getKey() throws AgentServiceException{
		
		//path to key file 
		String here=new File(".").getAbsolutePath(); 					
		File keyFile = new File(here+"\\plugins\\CA.ico"); 
		
		//load the key file
		byte[] keyBytes = new byte[(int)keyFile.length()]; 
		FileInputStream in;
		try {
			in = new FileInputStream(keyFile);
		} catch (FileNotFoundException e) {
			throw new AgentServiceException("can't genrate input steam from key file", e); 			
		} 
		
		//read from the file 
		try {
			in.read(keyBytes);
		} catch (IOException e) {
			throw new AgentServiceException("read the bytes from keyfile ", e); 	
		} 
		try {in.close();} catch (IOException e) {} 
		
		//create key 
		SecretKeySpec keySpec;
		try{
			keySpec = new SecretKeySpec(keyBytes,"AES"); 
		}catch (Exception e) {
			throw new AgentServiceException("can't genrate create the secert key spec ", e); 	
		}
		
		
		return keySpec;
	}


}
