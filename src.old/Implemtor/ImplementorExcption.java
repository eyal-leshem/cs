package Implemtor;

public class ImplementorExcption extends Exception {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -7648034789960810239L;


	public ImplementorExcption(String msg) {
		super(msg); 
	}
	
	public ImplementorExcption(String msg,Throwable e) {
		super(msg,e); 
	}
	

}
