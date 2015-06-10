package com.cldxk.stm32.utils;

import com.cldxk.app.EApplication;

/**
 * 用于管理XYZC轴的设置信息
 * @author apple
 *
 */
public class AxialManager {
	
	private static SharePreferenceUtil msharepreference = EApplication.getSharePreferenceUtil();
	
	public static void ChangInchAndMm(Boolean setflag){
		msharepreference.saveSharedPreferences("", setflag);
	}
	
	public int getCurrentAxial(){
		
		//String curaxial = 
		
		
		return 0;
		
	}

}
