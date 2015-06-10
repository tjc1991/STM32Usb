package com.cldxk.stm32.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cldxk.stm32usb.ui.base.EBaseActivity;
import com.ukmterm.stm32usb.R;

public class SettingActivity extends EBaseActivity implements OnClickListener {
	
	private static String defaultStr = "清新蓝";
	private Spinner fblSpinner = null;
	private Spinner countdirSpinner = null;
	private Spinner xtztSpinner = null;
	private Spinner dwmsSpinner = null;
	private Spinner zbmsSpinner = null;
	private Spinner zbjSpinner = null;
	private Spinner yyszSpinner = null;
	
	//private Button back_btn = null;
	private Button ok_btn = null;
	private Button cancel_btn = null;

	private TextView cur_axal_tv = null;
	private int recvalue =0;
	private int defaultvalue =0;
	
	private String[] fblArray = {"0.1um","0.2um","0.5um","1um","2um","2.5um","5um","10um"};
	private String[] jsfsArray = {"正向","反向"};
	private String[] dwmsArray = {"mm","cm"};
	private String[] zbmsArray = {"ABS","REF"};
	private String[] zbjArray = {"直径","半径"};
	private String[] xtyyArray = {"中文","English"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//初始化视图
		initMyView();
						
		//加载本地配置数据
		loadLocalData();
		
		Intent it = getIntent();
		recvalue = it.getIntExtra("x", defaultvalue);
		switch (recvalue) {
		case 1:
			cur_axal_tv.setText("X轴");
			break;
			
		case 2:
			cur_axal_tv.setText("Y轴");
			break;
			
		case 3:
			cur_axal_tv.setText("Z轴");
			break;

		default:
			break;
		}
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.setting_layout;
	}

	/**
	 * 初始化视图
	 */
	void initMyView(){
		
		ok_btn = findButtonById(R.id.btn_ok);
		cancel_btn = findButtonById(R.id.btn_cancel);
		ok_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);
		
		fblSpinner = (Spinner)findViewById(R.id.sp_fenbl);
		countdirSpinner = (Spinner)findViewById(R.id.sp_jsfx);
		xtztSpinner = (Spinner)findViewById(R.id.sp_ztfg);
		dwmsSpinner = (Spinner)findViewById(R.id.sp_dwms);
		zbmsSpinner = (Spinner)findViewById(R.id.sp_zbms);
		zbjSpinner = (Spinner)findViewById(R.id.sp_zbj);
		yyszSpinner = (Spinner)findViewById(R.id.sp_xtyy);
		
		cur_axal_tv = this.findTextViewById(R.id.cur_axal_tv);
		
	}
	
	/**
	 * 加载本地保存数据
	 */
	
	void loadLocalData(){
		
		fblSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("mfbl"),true);
		countdirSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("mjsfx"),true);
		dwmsSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("mdwms"),true);
		zbmsSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("mzbms"),true);
		zbjSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("mzbj"),true);
		yyszSpinner.setSelection(msharePreferenceUtil.loadIntSharedPreference("myysz"),true);
				
		//主题设置
		String pstr = msharePreferenceUtil.loadStringSharedPreference("curtheme", "");
		if(pstr.equals("淡青绿"))
		{
			xtztSpinner.setSelection(1,true);
		}else if(pstr.equals("清新蓝"))
		{
			xtztSpinner.setSelection(0,true);
		}	
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			
		case R.id.btn_ok:
			dealAllCheck();
			this.finish();
			break;
			
		case R.id.btn_cancel:
			
			break;

		default:
			break;
		}
	}
	
	
	public void dealAllCheck(){
		
		int pos = 0;
		
		//分辨率
		pos = fblSpinner.getSelectedItemPosition();
		msharePreferenceUtil.saveSharedPreferences("mfbl", pos);
		
		//技术方向
		pos = countdirSpinner.getSelectedItemPosition();
		msharePreferenceUtil.saveSharedPreferences("mjsfx", pos);
		
		//单位模式
		pos = dwmsSpinner.getSelectedItemPosition();
		msharePreferenceUtil.saveSharedPreferences("mdwms", pos);
		
		//坐标模式
		pos = zbmsSpinner.getSelectedItemPosition();
		msharePreferenceUtil.saveSharedPreferences("mzbms", pos);
		
		//直半径
		pos = zbjSpinner.getSelectedItemPosition();
		msharePreferenceUtil.saveSharedPreferences("mzbj", pos);
		
		//语言设置
		pos = yyszSpinner.getSelectedItemPosition();
		msharePreferenceUtil.saveSharedPreferences("myysz", pos);
		
		//主题设置
		String tmpstr = xtztSpinner.getSelectedItem().toString();
		if(tmpstr.equals(defaultStr))
		{
			msharePreferenceUtil.saveSharedPreferences("theme", true);
			msharePreferenceUtil.saveSharedPreferences("curtheme", "清新蓝");
		}else if(xtztSpinner.getSelectedItem().toString().equals("淡青绿")){
			msharePreferenceUtil.saveSharedPreferences("theme", false);
			msharePreferenceUtil.saveSharedPreferences("curtheme", "淡青绿");
		}
	}
}
