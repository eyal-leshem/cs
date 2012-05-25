package Commands;

public class CommandFactory {
	
 	private static CommandFactory inst=null;
 	
 	private CommandFactory(){
 		
 	}
 	
 	public static CommandFactory getInstance(){
 		if(inst==null){
 			return new CommandFactory(); 
 		}
 		return inst; 
 	}
 	
 	public  Command getCommand(String commandName) throws Exception{
		if(commandName.equals("generate key Pair"))	    return new GenrateKeyPairCommand(); 
		if(commandName.equals("install cert")) 			return new InstallCertCommand(); 
		if(commandName.equals("generate secret"))		return new GenrateSecertCommand();
		if(commandName.equals("install secret"))		return new InstallSecertCommand();
		
		throw new Exception("no such command "+commandName); 
 	
 	}

}
