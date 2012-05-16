package Implemtor;

import java.security.cert.Certificate;

import javax.crypto.SecretKey;

public abstract class Implementor {
	
	private String name;
	

	
	public abstract Certificate genrateKeyPair(); 
	
	public abstract SecretKey   genrateSecertKey(); 
	
	public abstract boolean		installSecertKey(SecretKey key); 
	
	public abstract boolean		installTrustCert(Certificate cert);	
		
}
