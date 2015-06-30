package com.cldxk.stm32.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cldxk.app.USBConfig;
import com.cldxk.stm32.utils.AxialManager;
import com.cldxk.stm32usb.ui.base.EBaseActivity;
import com.ukmterm.stm32usb.R;

public class SettingChangeActivity extends EBaseActivity implements OnClickListener{
	
	private static String defaultStr = "清新蓝";
	private TextView cur_axal_tv = null;
	private Button ok_btn = null;
	private Button cancel_btn = null;
	private Button recover_btn = null;
	
	private int recvalue =0;
	private int defaultvalue =0;
	
	private Spinner fblSpinner = null;
	private ImageButton jsfx_btn = null;
	private ImageButton dwms_btn = null;
	private ImageButton zbms_btn = null;
	private ImageButton zbj_btn = null;
	private ImageButton qltx_btn = null;
	private ImageButton zs_btn = null;
	private ImageButton xxbc_btn = null;
	private ImageButton ssl_btn = null;
	private Spinner ztfgSpinner = null;
	private Spinner xtyySpinner = null;
	
	private SeekBar sb_ylsz = null;
	private SeekBar sb_ldsz = null;
	
	
	private boolean is_jsfx = true;
	private boolean is_dwms = true;
	private boolean is_zbms = true;
	private boolean is_zbj = true;
	private boolean is_qltx = true;
	private boolean is_zs = true;
	private boolean is_xxbc = true;
	private boolean is_ssl = true;
	
	//
	private TextView zhis_tv = null;
	private TextView xxbc_tv = null;
	private TextView suosl_tv = null;
	
	private Boolean is_setting_ok = false;
	
	

	@Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			is_setting_ok = false;
			
			Intent it = getIntent();
			recvalue = it.getIntExtra("x", defaultvalue);
			
			initMyView();
			
			switch (recvalue) {
			case 1:
				cur_axal_tv.setText("X轴");
				msharePreferenceUtil.saveSharedPreferences("cur_axial", "X");
				break;
				
			case 2:
				cur_axal_tv.setText("Y轴");
				msharePreferenceUtil.saveSharedPreferences("cur_axial", "Y");
				break;
				
			case 3:
				cur_axal_tv.setText("Z轴");
				msharePreferenceUtil.saveSharedPreferences("cur_axial", "Z");
				break;

			default:
				break;
			}
			
			//加载配置
			loadOldStatus();
			
			//设置状态
			setImgStatus();
			
		}
	
	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.setting_change_layout;
	}
	
	@SuppressLint("CutPasteId")
	public void initMyView(){
		
		cur_axal_tv = (TextView) this.findViewById(R.id.cur_axal_tv);
		ok_btn = findButtonById(R.id.btn_ok);
		cancel_btn = findButtonById(R.id.btn_cancel);
		ok_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
		
		recover_btn = findButtonById(R.id.btn_recover);
		recover_btn.setOnClickListener(this);
		
		fblSpinner = (Spinner)findViewById(R.id.sp_fenbl);
		jsfx_btn = (ImageButton) findViewById(R.id.jsfx_btn);
		jsfx_btn.setOnClickListener(this);
		dwms_btn = (ImageButton) findViewById(R.id.dwms_btn);
		dwms_btn.setOnClickListener(this);
		zbms_btn = (ImageButton) findViewById(R.id.zbms_btn);
		zbms_btn.setOnClickListener(this);
		zbj_btn = (ImageButton) findViewById(R.id.zhibj_btn);
		zbj_btn.setOnClickListener(this);
		qltx_btn = (ImageButton) findViewById(R.id.qltx_btn);
		qltx_btn.setOnClickListener(this);
		zs_btn = (ImageButton) findViewById(R.id.zhis_btn);
		zs_btn.setOnClickListener(this);
		xxbc_btn = (ImageButton) findViewById(R.id.xxbc_btn);
		xxbc_btn.setOnClickListener(this);
		ssl_btn = (ImageButton) findViewById(R.id.suosl_btn);
		ssl_btn.setOnClickListener(this);
		ztfgSpinner = (Spinner)findViewById(R.id.sp_xtzt);
		xtyySpinner = (Spinner)findViewById(R.id.sp_xtyy);
		
		zhis_tv = this.findTextViewById(R.id.zhis_tv);
		zhis_tv.setOnClickListener(this);
		
		xxbc_tv = this.findTextViewById(R.id.xxbc_tv);
		xxbc_tv.setOnClickListener(this);
		
		suosl_tv = this.findTextViewById(R.id.suosl_tv);
		suosl_tv.setOnClickListener(this);
		
		sb_ylsz = (SeekBar) this.findViewById(R.id.sb_ylsz);
		sb_ldsz = (SeekBar) this.findViewById(R.id.sb_ldsz);
		
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		
		case R.id.zhis_tv:
			startActivityForResult(new Intent(SettingChangeActivity.this, CalActivity.class) , USBConfig.RESULT_ZS);
			break;
			
		case R.id.xxbc_tv:
			startActivityForResult(new Intent(SettingChangeActivity.this, CalActivity.class) , USBConfig.RESULT_XXBC);
			break;
			
		case R.id.suosl_tv:
			startActivityForResult(new Intent(SettingChangeActivity.this, CalActivity.class) , USBConfig.RESULT_SSL);
			break;
				
		case R.id.btn_ok:
			dealAllCheck();
			SaveCurStatus();
			if(is_setting_ok == true){				
				this.finish();
			}
			break;
			
		case R.id.btn_cancel:
			this.finish();
			break;
			
		case R.id.btn_recover:
			AxialManager.resetAllAxial();
			this.finish();
			break;
			
		case R.id.jsfx_btn:
			is_jsfx = !is_jsfx;	
			if(is_jsfx == true){
				jsfx_btn.setImageResource(R.drawable.count_forward);
			}else{
				jsfx_btn.setImageResource(R.drawable.count_reverse);
			}		
			break;
			
		case R.id.dwms_btn:
			is_dwms = !is_dwms;	
			if(is_dwms == true){
				dwms_btn.setImageResource(R.drawable.unit_mm);
			}else{
				dwms_btn.setImageResource(R.drawable.unit_inch);
			}		
			break;
			
		case R.id.zbms_btn:
			is_zbms = !is_zbms;	
			if(is_zbms == true){
				zbms_btn.setImageResource(R.drawable.coordinate_abs);
			}else{
				zbms_btn.setImageResource(R.drawable.coordinate_inc);
			}		
			break;
			
		case R.id.zhibj_btn:
			is_zbj = !is_zbj;	
			if(is_zbj == true){
				zbj_btn.setImageResource(R.drawable.button_radius);
			}else{
				zbj_btn.setImageResource(R.drawable.button_diameter);
			}		
			break;
			
		case R.id.qltx_btn:
			is_qltx = !is_qltx;	
			if(is_qltx == true){
				qltx_btn.setImageResource(R.drawable.button_on);
			}else{
				qltx_btn.setImageResource(R.drawable.button_off);
			}		
			break;
			
		case R.id.zhis_btn:
			is_zs = !is_zs;	
			if(is_zs == true){
				zs_btn.setImageResource(R.drawable.button_on);
			}else{
				zs_btn.setImageResource(R.drawable.button_off);
			}		
			break;
			
		case R.id.xxbc_btn:
			is_xxbc = !is_xxbc;	
			if(is_xxbc == true){
				xxbc_btn.setImageResource(R.drawable.button_on);
			}else{
				xxbc_btn.setImageResource(R.drawable.button_off);
			}		
			break;
			
		case R.id.suosl_btn:
			is_ssl = !is_ssl;	
			if(is_ssl == true){
				ssl_btn.setImageResource(R.drawable.button_on);
			}else{
				ssl_btn.setImageResource(R.drawable.button_off);
			}		
			break;
						
		default:
			break;
		}
		
	}
	
	public void loadOldStatus(){
		
		//获得当前系统设置的轴X,Y,Z
		String cur_axial = msharePreferenceUtil.loadStringSharedPreference("cur_axial", "X");
		if(cur_axial.equals("X")){
			is_jsfx = msharePreferenceUtil.loadBooleanSharedPreference("x_isjsfx");
			is_dwms = msharePreferenceUtil.loadBooleanSharedPreference("x_is_dwms");
			is_zbms = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbms");
			is_zbj = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbj");
			is_qltx = msharePreferenceUtil.loadBooleanSharedPreference("x_is_qltx");
			is_zs = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zs");
			is_xxbc = msharePreferenceUtil.loadBooleanSharedPreference("x_is_xxbc");
			is_ssl = msharePreferenceUtil.loadBooleanSharedPreference("x_is_ssl");
			
			fblSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("x_mfbl"),true);	
			sb_ldsz.setProgress(msharePreferenceUtil.loadIntSharedPreference("x_sb_ldsz"));
			sb_ylsz.setProgress(msharePreferenceUtil.loadIntSharedPreference("x_sb_ylsz"));
			
			zhis_tv.setText(msharePreferenceUtil.loadStringSharedPreference("x_zhis_tv", "0"));
			xxbc_tv.setText(msharePreferenceUtil.loadStringSharedPreference("x_xxbc_tv", "0"));
			suosl_tv.setText(msharePreferenceUtil.loadStringSharedPreference("x_suosl_tv", "1"));									
		}else if(cur_axial.equals("Y")){
			is_jsfx = msharePreferenceUtil.loadBooleanSharedPreference("y_isjsfx");
			is_dwms = msharePreferenceUtil.loadBooleanSharedPreference("y_is_dwms");
			is_zbms = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbms");
			is_zbj = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbj");
			is_qltx = msharePreferenceUtil.loadBooleanSharedPreference("y_is_qltx");
			is_zs = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zs");
			is_xxbc = msharePreferenceUtil.loadBooleanSharedPreference("y_is_xxbc");
			is_ssl = msharePreferenceUtil.loadBooleanSharedPreference("y_is_ssl");
			
			fblSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("y_mfbl"),true);	
			sb_ldsz.setProgress(msharePreferenceUtil.loadIntSharedPreference("y_sb_ldsz"));
			sb_ylsz.setProgress(msharePreferenceUtil.loadIntSharedPreference("y_sb_ylsz"));
			
			zhis_tv.setText(msharePreferenceUtil.loadStringSharedPreference("y_zhis_tv", "0"));
			xxbc_tv.setText(msharePreferenceUtil.loadStringSharedPreference("y_xxbc_tv", "0"));
			suosl_tv.setText(msharePreferenceUtil.loadStringSharedPreference("y_suosl_tv", "1"));
			
		}else if(cur_axial.equals("Z")){
			is_jsfx = msharePreferenceUtil.loadBooleanSharedPreference("z_isjsfx");
			is_dwms = msharePreferenceUtil.loadBooleanSharedPreference("z_is_dwms");
			is_zbms = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbms");
			is_zbj = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbj");
			is_qltx = msharePreferenceUtil.loadBooleanSharedPreference("z_is_qltx");
			is_zs = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zs");
			is_xxbc = msharePreferenceUtil.loadBooleanSharedPreference("z_is_xxbc");
			is_ssl = msharePreferenceUtil.loadBooleanSharedPreference("z_is_ssl");
			
			fblSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("z_mfbl"),true);	
			sb_ldsz.setProgress(msharePreferenceUtil.loadIntSharedPreference("z_sb_ldsz"));
			sb_ylsz.setProgress(msharePreferenceUtil.loadIntSharedPreference("z_sb_ylsz"));
			
			zhis_tv.setText(msharePreferenceUtil.loadStringSharedPreference("z_zhis_tv", "0"));
			xxbc_tv.setText(msharePreferenceUtil.loadStringSharedPreference("z_xxbc_tv", "0"));
			suosl_tv.setText(msharePreferenceUtil.loadStringSharedPreference("z_suosl_tv", "1"));
		}
				
	}
	
	public void SaveCurStatus(){
		
		int pos = 0;	
		//分辨率
		pos = fblSpinner.getSelectedItemPosition();
		//获得当前系统设置的轴X,Y,Z
		String cur_axial = msharePreferenceUtil.loadStringSharedPreference("cur_axial", "X");
		if(cur_axial.equals("X")){			
			msharePreferenceUtil.saveSharedPreferences("x_isjsfx",is_jsfx);
			msharePreferenceUtil.saveSharedPreferences("x_is_dwms",is_dwms);
			msharePreferenceUtil.saveSharedPreferences("x_is_zbms",is_zbms);
			msharePreferenceUtil.saveSharedPreferences("x_is_zbj",is_zbj);			
			msharePreferenceUtil.saveSharedPreferences("x_is_qltx",is_qltx);			
			msharePreferenceUtil.saveSharedPreferences("x_is_zs",is_zs);
			msharePreferenceUtil.saveSharedPreferences("x_is_xxbc",is_xxbc);
			msharePreferenceUtil.saveSharedPreferences("x_is_ssl",is_ssl);
									
			msharePreferenceUtil.saveSharedPreferences("x_mfbl", pos);
			
			msharePreferenceUtil.saveSharedPreferences("x_sb_ylsz", sb_ylsz.getProgress());
			msharePreferenceUtil.saveSharedPreferences("x_sb_ldsz", sb_ldsz.getProgress());
						
			if(is_zs == true){				
				msharePreferenceUtil.saveSharedPreferences("x_zhis_tv",zhis_tv.getText().toString());
			}else{
				msharePreferenceUtil.saveSharedPreferences("x_zhis_tv","0");
			}
			
			if(is_xxbc == true){				
				msharePreferenceUtil.saveSharedPreferences("x_xxbc_tv",xxbc_tv.getText().toString());
			}else{
				msharePreferenceUtil.saveSharedPreferences("x_xxbc_tv","0");
			}
						
			if(is_ssl == true){				
				String sslstr = suosl_tv.getText().toString();
				if(Float.parseFloat(sslstr)!= 0){	
					is_setting_ok = true;
					msharePreferenceUtil.saveSharedPreferences("x_suosl_tv",sslstr);
				}else{
					is_setting_ok = false;
					Toast.makeText(getApplicationContext(), "缩水率不能为0", Toast.LENGTH_SHORT);
					return;
				}
			}else{
				is_setting_ok = true;
				msharePreferenceUtil.saveSharedPreferences("x_suosl_tv","1");
			}
			
//			String sslstr = suosl_tv.getText().toString();
//			if(Float.parseFloat(sslstr)!= 0){	
//				is_setting_ok = true;
//				msharePreferenceUtil.saveSharedPreferences("x_suosl_tv",sslstr);
//			}else{
//				is_setting_ok = false;
//				Toast.makeText(getApplicationContext(), "缩水率不能为0", Toast.LENGTH_SHORT);
//				return;
//			}
		}else if(cur_axial.equals("Y")){			
			msharePreferenceUtil.saveSharedPreferences("y_isjsfx",is_jsfx);
			msharePreferenceUtil.saveSharedPreferences("y_is_dwms",is_dwms);
			msharePreferenceUtil.saveSharedPreferences("y_is_zbms",is_zbms);
			msharePreferenceUtil.saveSharedPreferences("y_is_zbj",is_zbj);
			msharePreferenceUtil.saveSharedPreferences("y_is_qltx",is_qltx);
			msharePreferenceUtil.saveSharedPreferences("y_is_zs",is_zs);
			msharePreferenceUtil.saveSharedPreferences("y_is_xxbc",is_xxbc);
			msharePreferenceUtil.saveSharedPreferences("y_is_ssl",is_ssl);
			
			msharePreferenceUtil.saveSharedPreferences("y_mfbl", pos);
			msharePreferenceUtil.saveSharedPreferences("y_sb_ylsz", sb_ylsz.getProgress());
			msharePreferenceUtil.saveSharedPreferences("y_sb_ldsz", sb_ldsz.getProgress());
			
			if(is_zs == true){				
				msharePreferenceUtil.saveSharedPreferences("y_zhis_tv",zhis_tv.getText().toString());
			}else{
				msharePreferenceUtil.saveSharedPreferences("y_zhis_tv","0");
			}
			
			if(is_xxbc == true){				
				msharePreferenceUtil.saveSharedPreferences("y_xxbc_tv",xxbc_tv.getText().toString());
			}else{
				msharePreferenceUtil.saveSharedPreferences("y_xxbc_tv","0");
			}
						
			if(is_ssl == true){				
				String sslstr = suosl_tv.getText().toString();
				if(Float.parseFloat(sslstr)!= 0){	
					is_setting_ok = true;
					msharePreferenceUtil.saveSharedPreferences("y_suosl_tv",sslstr);
				}else{
					is_setting_ok = false;
					Toast.makeText(getApplicationContext(), "缩水率不能为0", Toast.LENGTH_SHORT);
					return;
				}
			}else{
				is_setting_ok = true;
				msharePreferenceUtil.saveSharedPreferences("y_suosl_tv","1");
			}
			
//			msharePreferenceUtil.saveSharedPreferences("y_xxbc_tv",xxbc_tv.getText().toString());
//			
//			String sslstr = suosl_tv.getText().toString();
//			if(Float.parseFloat(sslstr)!= 0){	
//				is_setting_ok = true;
//				msharePreferenceUtil.saveSharedPreferences("y_suosl_tv",sslstr);
//			}else{
//				is_setting_ok = false;
//				Toast.makeText(getApplicationContext(), "缩水率不能为0", Toast.LENGTH_SHORT);
//				return;
//			}
			//msharePreferenceUtil.saveSharedPreferences("y_suosl_tv",suosl_tv.getText().toString());
		}else if(cur_axial.equals("Z")){			
			msharePreferenceUtil.saveSharedPreferences("z_isjsfx",is_jsfx);
			msharePreferenceUtil.saveSharedPreferences("z_is_dwms",is_dwms);
			msharePreferenceUtil.saveSharedPreferences("z_is_zbms",is_zbms);
			msharePreferenceUtil.saveSharedPreferences("z_is_zbj",is_zbj);
			msharePreferenceUtil.saveSharedPreferences("z_is_qltx",is_qltx);
			msharePreferenceUtil.saveSharedPreferences("z_is_zs",is_zs);
			msharePreferenceUtil.saveSharedPreferences("z_is_xxbc",is_xxbc);
			msharePreferenceUtil.saveSharedPreferences("z_is_ssl",is_ssl);
			
			msharePreferenceUtil.saveSharedPreferences("z_mfbl", pos);
			msharePreferenceUtil.saveSharedPreferences("z_sb_ylsz", sb_ylsz.getProgress());
			msharePreferenceUtil.saveSharedPreferences("z_sb_ldsz", sb_ldsz.getProgress());
			
			if(is_zs == true){				
				msharePreferenceUtil.saveSharedPreferences("z_zhis_tv",zhis_tv.getText().toString());
			}else{
				msharePreferenceUtil.saveSharedPreferences("z_zhis_tv","0");
			}
			
			if(is_xxbc == true){				
				msharePreferenceUtil.saveSharedPreferences("z_xxbc_tv",xxbc_tv.getText().toString());
			}else{
				msharePreferenceUtil.saveSharedPreferences("z_xxbc_tv","0");
			}
						
			if(is_ssl == true){				
				String sslstr = suosl_tv.getText().toString();
				if(Float.parseFloat(sslstr)!= 0){	
					is_setting_ok = true;
					msharePreferenceUtil.saveSharedPreferences("z_suosl_tv",sslstr);
				}else{
					is_setting_ok = false;
					Toast.makeText(getApplicationContext(), "缩水率不能为0", Toast.LENGTH_SHORT);
					return;
				}
			}else{
				is_setting_ok = true;
				msharePreferenceUtil.saveSharedPreferences("z_suosl_tv","1");
			}
			
//			msharePreferenceUtil.saveSharedPreferences("z_xxbc_tv",xxbc_tv.getText().toString());
//			
//			String sslstr = suosl_tv.getText().toString();
//			if(Float.parseFloat(sslstr)!= 0){
//				is_setting_ok = true;
//				msharePreferenceUtil.saveSharedPreferences("z_suosl_tv",sslstr);
//			}else{
//				is_setting_ok = false;
//				Toast.makeText(getApplicationContext(), "缩水率不能为0", Toast.LENGTH_SHORT);
//				return;
//			}
			//msharePreferenceUtil.saveSharedPreferences("z_suosl_tv",suosl_tv.getText().toString());
		}
		 
		 
	}
	
	public void setImgStatus(){
				
//		fblSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("x_mfbl"),true);		
		xtyySpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("myysz"),true);
				
		//主题设置
		String pstr = msharePreferenceUtil.loadStringSharedPreference("curtheme", "");
		
		if(null !=pstr){
			
			if(pstr.equals("淡青绿"))
			{
				ztfgSpinner.setSelection(1,true);
			}else if(pstr.equals("清新蓝"))
			{
				ztfgSpinner.setSelection(0,true);
			}	
		}

		if(is_jsfx == true){
			jsfx_btn.setImageResource(R.drawable.count_forward);
		}else{
			jsfx_btn.setImageResource(R.drawable.count_reverse);
		}
		
		if(is_dwms == true){
			dwms_btn.setImageResource(R.drawable.unit_mm);
		}else{
			dwms_btn.setImageResource(R.drawable.unit_inch);
		}
		
		if(is_zbms == true){
			zbms_btn.setImageResource(R.drawable.coordinate_abs);
		}else{
			zbms_btn.setImageResource(R.drawable.coordinate_inc);
		}
		
		if(is_zbj == true){
			zbj_btn.setImageResource(R.drawable.button_radius);
		}else{
			zbj_btn.setImageResource(R.drawable.button_diameter);
		}
		
		if(is_qltx == true){
			qltx_btn.setImageResource(R.drawable.button_on);
		}else{
			qltx_btn.setImageResource(R.drawable.button_off);
		}	
		
		if(is_zs == true){
			zs_btn.setImageResource(R.drawable.button_on);
		}else{
			zs_btn.setImageResource(R.drawable.button_off);
		}

		if(is_xxbc == true){
			xxbc_btn.setImageResource(R.drawable.button_on);
		}else{
			xxbc_btn.setImageResource(R.drawable.button_off);
		}
		
		if(is_ssl == true){
			ssl_btn.setImageResource(R.drawable.button_on);
		}else{
			ssl_btn.setImageResource(R.drawable.button_off);
		}
		
	}
	
	public void dealAllCheck(){
		
		int pos = 0;
//		
//		//分辨率
//		pos = fblSpinner.getSelectedItemPosition();
//		msharePreferenceUtil.saveSharedPreferences("mfbl", pos);
				
		//语言设置
		pos = xtyySpinner.getSelectedItemPosition();
		msharePreferenceUtil.saveSharedPreferences("myysz", pos);
		
		//主题设置
		String tmpstr = ztfgSpinner.getSelectedItem().toString();
		if(tmpstr.equals(defaultStr))
		{
			msharePreferenceUtil.saveSharedPreferences("theme", true);
			msharePreferenceUtil.saveSharedPreferences("curtheme", "清新蓝");
		}else if(ztfgSpinner.getSelectedItem().toString().equals("淡青绿")){
			msharePreferenceUtil.saveSharedPreferences("theme", false);
			msharePreferenceUtil.saveSharedPreferences("curtheme", "淡青绿");
		}
	}
	
	 @Override  
	 public void onActivityResult(int requestCode, int resultCode, Intent data)  
	    {  		 
	        if (requestCode==USBConfig.RESULT_ZS)  
	        {  
	            if (resultCode==112)  
	            {  
	                Bundle bundle=data.getExtras();  
	                String str=bundle.getString("cal");  
	                zhis_tv.setText(str); 
	            }  
	        } 
	        
	        else if (requestCode==USBConfig.RESULT_XXBC)  
	        {  
	            if (resultCode==112)  
	            {  
	                Bundle bundle=data.getExtras();  
	                String str=bundle.getString("cal");  
	                xxbc_tv.setText(str); 
	            }  
	        } 
	        
	        else if (requestCode==USBConfig.RESULT_SSL)  
	        {  
	            if (resultCode==112)  
	            {  
	                Bundle bundle=data.getExtras();  
	                String str=bundle.getString("cal");  
	                suosl_tv.setText(str); 
	            }  
	        } 
	        
	        super.onActivityResult(requestCode, resultCode, data);
	    }  


}
