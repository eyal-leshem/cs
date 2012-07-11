package Implemtor;

/**
 * Class for changes in plugins jars files in folder that is being watched
 *
 */
public class PluginChange
{
	
	public String path = "";
	
	public changeType change;
	
	
	static public enum changeType
	{
	    CREATED, DELETED
	}
	
	/**
	 * Constructor for deleting/creating event
	 * @param oldPath
	 * @param changeStr
	 */
	public PluginChange(String oldPath, changeType type)
	{
		//initialize the name
		path = new String(oldPath);
		change = type;
	}
}
