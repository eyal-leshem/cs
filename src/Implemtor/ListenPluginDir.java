package Implemtor;


import java.util.Observable;

import Logger.AgentServiceLogger;

import exceptions.AgentServiceException;
import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;


public class ListenPluginDir extends Observable implements Runnable  
{

	private AgentServiceLogger logger=AgentServiceLogger.getInstance(); 
	
	String folderPath = "";
	volatile boolean pause;
	volatile boolean stop;
	
	/**
	 * Constructor
	 */
	public ListenPluginDir()
	{
		pause = false;
		stop = false;
	}
	
	
	/**
	 * Setting folder path for watching
	 * @param path
	 */
	public void setFolderPath(String path)
	{
		folderPath = new String(path);
	}
	
	
	/**
	 * Method for notifying all the observers about change
	 * @param changedObj 
	 */
	public void notifyAllObj(PluginChange changedObj)
	{
		this.setChanged();
		
		//System.out.println("the number is "+this.countObservers());
		this.notifyObservers(changedObj);
	}
	
	
	/**
	 * Listener class for the folder in the path specified.
	 * Using jar of jnotify. Event driven system for the events occured
	 * in folder.
	 */
	class Listener implements JNotifyListener
    {

		@Override
		public void fileCreated(int arg0, String arg1, String arg2) 
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {}
			
			logger.info("the listen where service interupt - new file cerated  : " +arg0+" "+arg1+" "+arg2); 
			System.out.println("created "+arg0+" "+arg1+" "+arg2);
			//create the object with changes
			PluginChange changedObj = new PluginChange(arg1+"\\"+arg2, PluginChange.changeType.CREATED);
			//call the method that will notify to all observers about the changes
			notifyAllObj(changedObj);
		}

		@Override
		public void fileDeleted(int arg0, String arg1, String arg2) 
		{
			
			System.out.println("deleted "+arg0);
			PluginChange changedObj = new PluginChange(arg1+"\\"+arg2, PluginChange.changeType.DELETED);
			//call the method that will notify to all observers about the changes
			notifyAllObj(changedObj);
		}

		@Override
		public void fileModified(int arg0, String arg1, String arg2) 
		{
			
			System.out.println("modified "+arg1+" "+arg2);
			//DirDataChanges changedObj = new DirDataChanges(arg1+"\\"+arg2, "Modified", new Date());
			//call the method that will notify to all observers about the changes
			//notifyAllObj(changedObj);
		}

		@Override
		public void fileRenamed(int arg0, String arg1, String arg2, String arg3) 
		{
			
			System.out.println("renamed "+arg2+" "+arg3);
			//DirDataChanges changedObj = new DirDataChanges(arg1+"\\"+arg2, "Renamed", arg1+"\\"+arg3);
			//call the method that will notify to all observers about the changes
			//notifyAllObj(changedObj);
			
		}
		
    }
	
	/**
	 * Thread method for watching the folder until someone stop or pause
	 */
	@Override
	public void run() 
	{
		
		int mask = JNotify.FILE_CREATED
	       | JNotify.FILE_DELETED
	       | JNotify.FILE_MODIFIED
	       | JNotify.FILE_RENAMED;


		try {
			JNotify.addWatch(folderPath, mask, true, new Listener());
		} catch (JNotifyException e) {
			//TODO write to log the problem  
		}
	    while(stop == false) 
	    {
	    	allowPause();	// allow this loop to be paused
	    }
	}

	/**
	 * Check the pause flag and insert the thread to the waiting if the pause is true
	 */
	protected void allowPause()
	{
		if(pause)
		{
			System.out.format("paused>\n");
			synchronized (this) 
			{
				try 
				{ 
					wait();
				} catch (InterruptedException e) {}
			}
		}
	}
	
	
	/**
	 * method for set the pause true
	 */
	public void pause(){ pause = true; }
	
	/**
	 * set the stop true
	 */
	public void stop(){ stop = true; }
		
	/**
	 * change the stop flag to false in order we can continue run
	 */
	public void resume() { pause = false; }
}

