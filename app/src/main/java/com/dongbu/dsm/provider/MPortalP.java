package com.dongbu.dsm.provider;
import com.dongbu.dsm.common.CommonData;
import com.dongbu.dsm.util.CipherUtil;

import org.json.JSONObject;

public class MPortalP
{
	/** 연동 암호 키 256 비트 */
	public String sCypherKey256 = CommonData.EXTRA_APP_SECURITY_KEY;

	private static MPortalP instance = null;

	public static MPortalP getInstance() {
		if(instance == null) {
			instance = new MPortalP();
		}
		return instance;
	}
	/** 암호화된 데이타를 복호화한다. */
	public String decrypt(String s)
	{
		return CipherUtil.urlDecode_After_AES256_Base64(sCypherKey256, s);

	}

	public String encrypt(String cypherType, JSONObject obj, String CypherKey) throws Exception {
		return CipherUtil.buildUrl_CypherEnc_Base64_Param(cypherType, obj, CypherKey);
	}
	
	
}
