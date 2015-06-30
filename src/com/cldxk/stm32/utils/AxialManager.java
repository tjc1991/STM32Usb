package com.cldxk.stm32.utils;

import com.cldxk.app.EApplication;

/**
 * 用于管理XYZC轴的设置信息
 * @author apple
 *
 */
public class AxialManager {
	
	private static SharePreferenceUtil msharePreferenceUtil = EApplication.getSharePreferenceUtil();
	
	/**
	 * 所有轴恢复默认设置
	 */
	public static void resetAllAxial(){
		
		//X轴
		msharePreferenceUtil.saveSharedPreferences("x_isjsfx",true);
		msharePreferenceUtil.saveSharedPreferences("x_is_dwms",true);
		msharePreferenceUtil.saveSharedPreferences("x_is_zbms",true);
		msharePreferenceUtil.saveSharedPreferences("x_is_zbj",false);
		msharePreferenceUtil.saveSharedPreferences("x_is_qltx",true);
		msharePreferenceUtil.saveSharedPreferences("x_is_zs",true);
		msharePreferenceUtil.saveSharedPreferences("x_is_xxbc",true);
		msharePreferenceUtil.saveSharedPreferences("x_is_ssl",true);					
		msharePreferenceUtil.saveSharedPreferences("x_mfbl", 6);		
		msharePreferenceUtil.saveSharedPreferences("x_zhis_tv","0");
		msharePreferenceUtil.saveSharedPreferences("x_xxbc_tv","0");
		msharePreferenceUtil.saveSharedPreferences("x_suosl_tv","1");
		msharePreferenceUtil.saveSharedPreferences("x_sb_ylsz", 0);
		msharePreferenceUtil.saveSharedPreferences("x_sb_ldsz", 0);
		
		//Y轴
		msharePreferenceUtil.saveSharedPreferences("y_isjsfx",true);
		msharePreferenceUtil.saveSharedPreferences("y_is_dwms",true);
		msharePreferenceUtil.saveSharedPreferences("y_is_zbms",true);
		msharePreferenceUtil.saveSharedPreferences("y_is_zbj",false);
		msharePreferenceUtil.saveSharedPreferences("y_is_qltx",true);
		msharePreferenceUtil.saveSharedPreferences("y_is_zs",true);
		msharePreferenceUtil.saveSharedPreferences("y_is_xxbc",true);
		msharePreferenceUtil.saveSharedPreferences("y_is_ssl",true);					
		msharePreferenceUtil.saveSharedPreferences("y_mfbl", 6);		
		msharePreferenceUtil.saveSharedPreferences("y_zhis_tv","0");
		msharePreferenceUtil.saveSharedPreferences("y_xxbc_tv","0");
		msharePreferenceUtil.saveSharedPreferences("y_suosl_tv","1");
		msharePreferenceUtil.saveSharedPreferences("y_sb_ylsz", 0);
		msharePreferenceUtil.saveSharedPreferences("y_sb_ldsz", 0);
		
		//Z轴
		msharePreferenceUtil.saveSharedPreferences("z_isjsfx",true);
		msharePreferenceUtil.saveSharedPreferences("z_is_dwms",true);
		msharePreferenceUtil.saveSharedPreferences("z_is_zbms",true);
		msharePreferenceUtil.saveSharedPreferences("z_is_zbj",false);
		msharePreferenceUtil.saveSharedPreferences("z_is_qltx",true);
		msharePreferenceUtil.saveSharedPreferences("z_is_zs",true);
		msharePreferenceUtil.saveSharedPreferences("z_is_xxbc",true);
		msharePreferenceUtil.saveSharedPreferences("z_is_ssl",true);					
		msharePreferenceUtil.saveSharedPreferences("z_mfbl", 6);		
		msharePreferenceUtil.saveSharedPreferences("z_zhis_tv","0");
		msharePreferenceUtil.saveSharedPreferences("z_xxbc_tv","0");
		msharePreferenceUtil.saveSharedPreferences("z_suosl_tv","1");
		msharePreferenceUtil.saveSharedPreferences("z_sb_ylsz", 0);
		msharePreferenceUtil.saveSharedPreferences("z_sb_ldsz", 0);
		
		//语言设置
		msharePreferenceUtil.saveSharedPreferences("myysz", 0);
		msharePreferenceUtil.saveSharedPreferences("theme", false);
		msharePreferenceUtil.saveSharedPreferences("curtheme", "淡青绿");
				
	}
	
	/**
	 * 清零提醒
	 * @param curaxial
	 * @param isflag
	 */
	public static void setAxialQltx(int curaxial,Boolean isflag){
		
		switch (curaxial) {
		case 1:
			msharePreferenceUtil.saveSharedPreferences("x_is_qltx",isflag);
			break;
			
		case 2:
			msharePreferenceUtil.saveSharedPreferences("y_is_qltx",isflag);
			break;
			
		case 3:
			msharePreferenceUtil.saveSharedPreferences("z_is_qltx",isflag);
			break;

		default:
			break;
		}
	}
	
	public static Boolean getAxialQltx(int curaxial){
		
		switch (curaxial) {
		case 1:
			return msharePreferenceUtil.loadBooleanSharedPreference("x_is_qltx");			
		case 2:
			return msharePreferenceUtil.loadBooleanSharedPreference("y_is_qltx");			
		case 3:
			return msharePreferenceUtil.loadBooleanSharedPreference("z_is_qltx");
		default:
			break;
		}
		return false;
	}
	
	/**
	 * 置数
	 * @param curaxial
	 * @param isflag
	 */
	public static void setAxialzs(int curaxial,Boolean isflag){
		
		switch (curaxial) {
		case 1:
			msharePreferenceUtil.saveSharedPreferences("x_is_zs",isflag);
			break;
			
		case 2:
			msharePreferenceUtil.saveSharedPreferences("y_is_zs",isflag);
			break;
			
		case 3:
			msharePreferenceUtil.saveSharedPreferences("z_is_zs",isflag);
			break;

		default:
			break;
		}
	}
	
	public static Boolean getAxialzs(int curaxial){
		
		switch (curaxial) {
		case 1:
			return msharePreferenceUtil.loadBooleanSharedPreference("x_is_zs");			
		case 2:
			return msharePreferenceUtil.loadBooleanSharedPreference("y_is_zs");			
		case 3:
			return msharePreferenceUtil.loadBooleanSharedPreference("z_is_zs");
		default:
			break;
		}
		return false;
	}
	
	/**
	 * 置数数值
	 * @param curaxial
	 * @param isflag
	 */
	public static void setAxialzsValue(int curaxial,String value){
		
		switch (curaxial) {
		case 1:
			msharePreferenceUtil.saveSharedPreferences("x_zhis_tv",value);
			break;
			
		case 2:
			msharePreferenceUtil.saveSharedPreferences("y_zhis_tv",value);
			break;
			
		case 3:
			msharePreferenceUtil.saveSharedPreferences("z_zhis_tv",value);
			break;

		default:
			break;
		}
	}
	
	public static String getAxialzsValue(int curaxial){
		
		switch (curaxial) {
		case 1:
			return msharePreferenceUtil.loadStringSharedPreference("x_zhis_tv", "0");		
		case 2:
			return msharePreferenceUtil.loadStringSharedPreference("y_zhis_tv", "0");				
		case 3:
			return msharePreferenceUtil.loadStringSharedPreference("z_zhis_tv", "0");	
		default:
			break;
		}
		return "0";
	}
	
	
	
}
