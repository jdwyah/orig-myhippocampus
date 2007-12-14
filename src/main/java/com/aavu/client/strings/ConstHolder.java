package com.aavu.client.strings;

import com.aavu.client.Interactive;
import com.aavu.client.images.Images;
import com.google.gwt.core.client.GWT;

public class ConstHolder {

	public static final String UPLOAD_PATH = "service/upload.html";// "site/secure/upload.html";
	public static final String FILE_PATH = "service/showFile.html?key=";

	public static Consts myConstants;
	public static Images images;

	public static String getImgLoc(String img_postfix) {
		if (GWT.isScript()) {
			return Interactive.getRelativeURL("img/" + img_postfix);
		} else {
			return "file://C:/workspace/RealHippo/src/main/webapp/img/" + img_postfix;
		}

	}

}
