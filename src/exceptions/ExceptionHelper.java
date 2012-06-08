package exceptions;

public class ExceptionHelper {
	
	  /**
	  * Defines a custom format for the stack trace as String.
	  */
	  public static String getCustomStackTrace(Throwable aThrowable) {
	    
		 //add the class name and any message passed to constructor
	    StringBuilder result = new StringBuilder( "full stack trace: " );
	    result.append(aThrowable.toString());
	    String NEW_LINE = System.getProperty("line.separator");
	    result.append(NEW_LINE);

	    //add each element of the stack trace
	    for (StackTraceElement element : aThrowable.getStackTrace() ){
	      result.append( element );
	      result.append( NEW_LINE );
	    }
	    return result.toString();
	  }

}
