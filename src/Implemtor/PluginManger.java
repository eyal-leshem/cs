package Implemtor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import Commands.newInstanceCommand;
import Logger.AgentServiceLogger;
import Manager.AgentServiceConf;
import Manager.Communicate;

import exceptions.AgentServiceException;

import message.Message;

public class PluginManger implements Observer {
	
	PluginFactory 						pluginFactory=PluginFactory.getInstance();
	ListenPluginDir 					watcher;//listener for the changes in folder
	Map<String,Implementor> 			implemtors=new  ConcurrentHashMap<String,Implementor>();
	Map<String,Class>					plugins=new ConcurrentHashMap<String, Class>(); 
	static PluginManger 				inst=null;
	AgentServiceConf					conf=null; 
	
	
	public PluginManger() throws AgentServiceException {
		
		//plugins path 
		String pluginDirPath=new File(".").getAbsolutePath()+System.getProperty("file.separator")+"plugins"; 
			
		ArrayList<Class> pluginsArray;
		
		conf=AgentServiceConf.getInstance(); 
		
		//load the implemtor array 
		try {
			pluginsArray = pluginFactory.getClassArr(pluginDirPath);
		} catch (AgentServiceException e) {
			throw new AgentServiceException("can't load the arraylist of plugins", e);
		}
		
					
		Communicate net= new Communicate(); 
	
		//put the implementors into an hash-map  
		for (Class aClass : pluginsArray) 
		{
			//get class name 
			String pluginName=aClass.getName().substring("Implemtor.".length());
			
			//put in the plugin table and update the server
			plugins.put( pluginName , aClass ); 
			net.newImpInform(pluginName, conf); 
		}	
		
		
		//load the all install instances of that plugin factory   
		try {
			PluginInstanceFactory pif=PluginInstanceFactory.getInstance(); 
			implemtors=pif.getImps(plugins); 
			
		} catch (AgentServiceException e) {
			throw new AgentServiceException("problem to ge the implementors from plugin factory"); 
		}
			
		 
		
		
	}
	

	public static PluginManger getInstance() throws AgentServiceException{
		if(inst==null){
			inst= new PluginManger();
		}
		return inst;
		
	}
	
	public Implementor getImplementor(Message msg) {
		return  implemtors.get(msg.getImplementorID());
	}
	
	//_________________________________________________________________________________
	/**
	 * Method that starts observing the changes in folder specified in folderPath
	 * @param folderPath
	 */
	public void startObserving(String folderPath)
	{
		watcher = new ListenPluginDir();
		watcher.addObserver(this);
		watcher.setFolderPath(folderPath); 
		
		//start watcher thread
		new Thread(new Runnable(){
			@Override
			public void run() {
				watcher.run();
			}			
		}).start();
	}
	
	/**
	 * pause for watching folder
	 */
	public void pauseWatcher()
	{ 
		watcher.pause(); 
	}
	
	/**
	 * Resuming the thread that watch to the user folder
	 */
	public void resumeWatcher()
	{
		synchronized (watcher)
		{
			watcher.notify();
			watcher.resume();
		}
	}
	
	
	/**
	 * Method called from listenPluginDir object when a change occurred
	 * arg1 - the object with the change of the type PluginChange. Contains
	 * 			path - string with the path of jar that was removed/created
	 * 			type - CREATED/DELETED
	 */
	@Override
	public void update(Observable arg0, Object arg1) 
	{
		try 
		{	
			PluginChange changeObj = (PluginChange)arg1;
			
			//if the change was creation of new jar - add to implementors map new Implementor
			if(changeObj.change == PluginChange.changeType.CREATED)
			{
				synchronized (inst)
				{
					Class aClass = pluginFactory.getOneClass(changeObj.path);
					
					Communicate net= new Communicate();
					//if the file was a correct jar
					if(aClass != null)
					{
						//get class name 
						String pluginName=aClass.getName().substring("Implemtor.".length());
						
						//put in the plugin table and update the server
						plugins.put( pluginName , aClass ); 
						net.newImpInform(pluginName, conf); 
					}
				}
			}
			
			//if the change was the removal of existed jar - remove from implementors map
			if(changeObj.change == PluginChange.changeType.DELETED)
			{
				synchronized (inst)
				{
					String path = changeObj.path;
					implemtors.remove(path.substring(0, path.indexOf(".jar")));
				}
			}
			
		} 
		catch (Exception e) {/*a bad plugin*/	}
		
	}

	public String addNewInstance(String plugin,String parms) throws AgentServiceException{
		
		PluginInstanceFactory pif=PluginInstanceFactory.getInstance(); 
		
		Implementor imp;
		try {
			Class pluginClass=plugins.get(plugin); 
			imp=pif.installNewImplementorInstance(pluginClass,parms);
		} catch (AgentServiceException e) {
			throw new AgentServiceException("can not install new instance of this implemntor implemntor",e); 
		} 
		
		implemtors.put(imp.getName(),imp); 
		
		String algs=getAlgString(imp); 
		
		return imp.getName()+","+algs; 
	
	}

	private String getAlgString(Implementor imp) throws AgentServiceException {
	
		ArrayList<String> algs;
		try {
			algs = imp.getAlgorithms();
		} catch (ImplementorExcption e) {
			throw  new AgentServiceException("can not get the algorithems of the implemntor"); 
		} 
		
		StringBuilder ret=new StringBuilder(""); 
		
		for(String alg:algs){
			ret.append(alg+","); 
		}
		
		if(ret.toString().length()>0)
			return ret.toString().substring(0,ret.length()-1); 
		return ""; 
	}


	private void ImplemntorNameNotAvailable() {
		
		
	}
	
	



	
	

}
