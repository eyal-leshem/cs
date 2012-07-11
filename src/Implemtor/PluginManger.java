package Implemtor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import Logger.AgentServiceLogger;
import Manager.AgentServiceConf;
import Manager.Communicate;

import exceptions.AgentServiceException;

import message.Message;

public class PluginManger implements Observer {
	
	PluginFactory 						pluginFactory=PluginFactory.getInstance();
	ListenPluginDir 					watcher;//listener for the changes in folder
	Map<String,Implementor> 			implemtors=new HashMap<String,Implementor>();
	static PluginManger 				inst=null;
	AgentServiceConf					conf=null; 
	
	
	public PluginManger() throws AgentServiceException, ImplementorExcption {
		
		//plugins path 
		String pluginDirPath=new File(".").getAbsolutePath()+System.getProperty("file.separator")+"plugins"; 
			
		ArrayList<Implementor> implemtorArr;
		
		conf=AgentServiceConf.getInstance(); 
		
		//load the implemtor array 
		try {
			implemtorArr = pluginFactory.getClassArr(pluginDirPath);
		} catch (AgentServiceException e) {
			throw new AgentServiceException("can't load the arraylist of plugins", e);
		}
			
		Communicate net= new Communicate(); 
		//put the implementors into an hash-map  
		for (Implementor implementor : implemtorArr) 
		{
			    if(implemtors.containsKey(implementor.getName())){
			    	 ImplemntorNameNotAvailable();
			   }	
			    else{
			    	implemtors.put(implementor.getName(),implementor); 
			    	net.newImpInform(implementor.getName(), conf, implementor.getAlgorithms()); 
			    }
		}	
			
		 
		
		
	}
	

	
	public static PluginManger getInstance() throws Exception{
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
					Implementor implementor = pluginFactory.getOneClass(changeObj.path);
					
					//if the file was a correct jar
					if(implementor != null)
					{
						if(implemtors.containsKey(implementor.getName()))
						{
						  ImplemntorNameNotAvailable();
						}
						else{
							//add the object to implementors map
							implemtors.put(implementor.getName(), implementor);
							Communicate net= new Communicate(); 
							net.newImpInform(implementor.getName(),conf, implementor.getAlgorithms()); 
						}
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
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	private void ImplemntorNameNotAvailable() {
		// TODO Auto-generated method stub
		
	}



	
	

}
