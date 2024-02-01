package com.dongbu.dsm.util.seed;

import android.util.Base64;

/**
 * 
 * @author 안정진
 * seed 128bit. CBC 모드. PKCS#5 패딩
 *
 */
public class SEED128Cipher {
	
	// seed 객체
	private static SEED128Cipher seed;
	
	// 블록 사이즈
	private static final int BLOCK_SIZE = 16;
	
	// 초기화 벡터
	byte[] DEFAULT_IV = { 77, 111, 98, 105, 108, 101, 84, 114, 97, 110, 115, 75, 101, 121, 49, 48 };
	
	// 라운드 키.
	int[] roundKey = new int[32];
	
	
	public static SEED128Cipher getInstance(byte[] cryptKey) {
		if (seed == null) {
			seed = new SEED128Cipher (cryptKey);
		}
		return seed;
	}
	
	private SEED128Cipher (byte[] cryptKey) { 
		SEED128CipherCBC.SeedRoundKey(roundKey, cryptKey);
	}
	
	/** 암호화해서 base64로 저장한다. */
	public static String encryptBase64Format(String cypherKey,String s)
	{
   	 	try
		{
   	 	    byte [] cypherKeyBytes = cypherKey.getBytes();
			byte [] encrypted = SEED128Cipher.getInstance(cypherKeyBytes).encrypt(s).getBytes();
	   	 	return Base64.encodeToString(encrypted, 0);
		}
		catch(Exception ex)
		{
			return "";
		}
	}
	
	/** base64로 저장된 스트링을 복호화한다. */
	public static String decryptBase64Format(String cypherKey,String s)
	{
		try
		{
			byte [] bytesDec = Base64.decode(s, 0);
			byte [] cypherKeyBytes = cypherKey.getBytes();
			return SEED128Cipher.getInstance(cypherKeyBytes).decrypt(new String(bytesDec,0,bytesDec.length,"UTF-8"));
		}
		catch(Exception ex)
		{
			return "";
		}
	}
	
	
	//SEED 128, CBC, PKCS#5 암호화
	public String encrypt(String sPlain)
	{

		byte[] bytesCyperBlock = new byte[BLOCK_SIZE];
		byte[] bytesCbcBlock = new byte[BLOCK_SIZE];
		byte[] bytesEncryptedBlock = new byte[BLOCK_SIZE];
			 
		byte[] bytesPlain = SEED128CipherCBC.stringToBytes(sPlain);  //sPlain.getBytes();
		int nPlainBytesLen = bytesPlain.length;
			 
		int nBlockCount = (nPlainBytesLen / BLOCK_SIZE) + 1;
		int nPad = BLOCK_SIZE - nPlainBytesLen % BLOCK_SIZE;
			
		int nResultSize = BLOCK_SIZE*nBlockCount;
		byte[] bytesEncryptedResult = new byte[nResultSize];
			
		System.arraycopy(DEFAULT_IV,0,bytesCbcBlock,0,BLOCK_SIZE);
			
		for(int i=0;i<nBlockCount;i++)
		{
			// PKCS#5 패딩에서 패딩이 0인경우라도 끝에 패딩을 16으로 채워야된다.
			if(i == nBlockCount -1 ) // 끝블록
			{
				if(nPad != BLOCK_SIZE)
					System.arraycopy(bytesPlain,i*BLOCK_SIZE,bytesCyperBlock,0, BLOCK_SIZE-nPad);
		
				for(int n=BLOCK_SIZE-nPad;n<BLOCK_SIZE;n++)
				{
				    bytesCyperBlock[n] =  (byte)nPad;  // PKCS#5 패딩
					//bytesCyperBlock[n] =  (byte)0;   // ZERO 패딩
				}
			}
			else
				System.arraycopy(bytesPlain,i*BLOCK_SIZE,bytesCyperBlock,0,BLOCK_SIZE);
				
			// cbc 운영모드로 새로운 암호 블록
			SEED128CipherCBC.SEEDXor(bytesCyperBlock, bytesCyperBlock,bytesCbcBlock, BLOCK_SIZE); 
				
			// 암호블록을 seed로 암호화
			SEED128CipherCBC.SeedEncrypt(bytesCyperBlock,roundKey,bytesEncryptedBlock); 
				
			// 다음블록에서 사용할 cbc 블록을 암호화된 블록으로 대체
			System.arraycopy(bytesEncryptedBlock, 0, bytesCbcBlock, 0, BLOCK_SIZE);
				
			// 결과 블록 복사.
			System.arraycopy(bytesEncryptedBlock,0,bytesEncryptedResult,i*BLOCK_SIZE,BLOCK_SIZE);
		}
		
		return SEED128CipherCBC.bytesToHexString(bytesEncryptedResult);
	}
	
	//SEED 128, CBC, PKCS#5 복호화 (* 항상 인크립트는 128비트 블록크기로 암호화되있어야한다. *)
	public String decrypt(String sEncrypted)
	{
		byte[] bytesEncrypted = SEED128CipherCBC.hexStringToBytes(sEncrypted);
		int nEncryptedBytesLen = bytesEncrypted.length;
			 
		int nBlockCount = (nEncryptedBytesLen / BLOCK_SIZE) ;
		
		byte[] bytesCyperBlock = new byte[BLOCK_SIZE];
		byte[] bytesCbcBlock = new byte[BLOCK_SIZE];
		byte[] bytesDecryptedBlock = new byte[BLOCK_SIZE];
			 
		int nResultSize = BLOCK_SIZE*nBlockCount;
		byte[] bytesDecryptedResult = new byte[nResultSize];
			
		System.arraycopy(DEFAULT_IV,0,bytesCbcBlock,0,BLOCK_SIZE);
			
		for(int i=0;i<nBlockCount;i++)
		{
			System.arraycopy(bytesEncrypted,i*BLOCK_SIZE,bytesCyperBlock,0,BLOCK_SIZE);
				
			// 암호블록을 seed로 복호화
			SEED128CipherCBC.SeedDecrypt(bytesCyperBlock,roundKey,bytesDecryptedBlock); 
				
			// cbc 운영모드로 복호화
			SEED128CipherCBC.SEEDXor(bytesDecryptedBlock, bytesDecryptedBlock,bytesCbcBlock, BLOCK_SIZE); 
				
			// 다음블록에서 사용할 cbc 블록을 암호화된 블록으로 대체
			System.arraycopy(bytesCyperBlock, 0, bytesCbcBlock, 0, BLOCK_SIZE);
				
			// 결과 블록 복사.
			System.arraycopy(bytesDecryptedBlock,0,bytesDecryptedResult,i*BLOCK_SIZE,BLOCK_SIZE);
				
		}
			
		// PKCS#5 unpadding
		int nPad = (int) bytesDecryptedResult[bytesDecryptedResult.length-1];
		//System.out.println(nPad);	
		byte[] bytesDecryptedResult_ExceptPadding = new byte[nResultSize-nPad];
		System.arraycopy(bytesDecryptedResult,0,bytesDecryptedResult_ExceptPadding,0,bytesDecryptedResult_ExceptPadding.length);
			
		return  SEED128CipherCBC.bytesToString(bytesDecryptedResult_ExceptPadding);
	}
}
