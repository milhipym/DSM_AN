package com.dongbu.dsm.webview.jsbridge;

import com.dongbu.dsm.model.web.BCResultWebItem;
import com.dongbu.dsm.model.web.Contract;
import com.dongbu.dsm.model.web.Info;
import com.dongbu.dsm.model.web.OCRItem;
import com.minerva.magicbcreaderlib.ui.item.BCResultItem;

import org.json.JSONObject;

import java.util.ArrayList;

import minerva.magicidreaderlib.card.CardDTO;

public interface CallBackFunctionListener {
	
	public void onResultDoOcrCall(OCRItem data, int resultCode);
	public void onResultDoIdCall(BCResultWebItem data, int resultCode);
	public void onResultDoPaperCall(String data, int resultCode);
	public void onCallSetSessionTime(String sessionTime);
	public void onResultGetInfo(Info data, int resultCode);
	public void onResultLogout(int resultCode);
	public void onResultAppExit(int resultCode);
	public void onResultDoStartExternalApp(int resultCode);
	public void onResultDoCameraGalleryCall(String data, int resultCode);
	public void onResultGetContractCall(Contract data, int resultCode);
	public void onResultDefault(int resultCode);
	public void onResultSetValue(int resultCode);
	public void onResultGetValue(String value, int resultCode);
}
