package Implemtor;


import java.security.cert.Certificate;

import javax.crypto.SecretKey;

public abstract class Implementor {
	
	protected String name;
	public Implementor() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * build the im
	 * 
	 * @param params - json string that contain parameters for this key Store 
	 * @throws Exception 
	 */
	public Implementor(String params) throws Exception {
		throw new Exception("unimplement"); 
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	/**
	 * the implementor will generate a key pair - for asymmetric  cryptography 
	 * he will save his private key in his keystore
	 * and return the public key via certificate
	 * 
	 * @param dName - the dname that needed to create the certificate
	 * @return certificate contain the new public key 
	 * @throws ImplementorExcption 
	 */
	public abstract Certificate genrateKeyPair(String dName) throws ImplementorExcption; 
	
	
	/**
	 * the implementor will generate a new secret key -for symmetric cryptography
	 *   
	 * @param alg - the algorithm of the secret key 
	 * @return the new  secretkey 
	 * @throws ImplementorExcption
	 */
	public abstract SecretKey   genrateSecertKey(String alg) throws ImplementorExcption; 
	
	
	/**
	 * store the key in the implementor keystore 
	 * 
	 * @param key key to store 
	 * @return true - in success  , false in fail 
	 * @throws ImplementorExcption
	 */
	public abstract boolean		installSecertKey(SecretKey key) throws ImplementorExcption ; 
	
	/**
	 * store the trust certificate in the keystore of the implemtor 
	 * 
	 * @param cert - certifcate to store 
	 * @return true - in success  , false in fail 
	 * @throws ImplementorExcption
	 */
	public abstract boolean		installTrustCert(Certificate cert) throws ImplementorExcption ;	
		
}
