package com.cldxk.stm32.ui;

import com.cldxk.stm32usb.ui.base.EBaseActivity;
import com.ukmterm.stm32usb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class StartActivity extends EBaseActivity implements OnClickListener{
	
	private Button mCNButton=null;
	private Button mENButton=null;
	private LinearLayout lv = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		mCNButton = findButtonById(R.id.cn_btn);
		mENButton = findButtonById(R.id.en_btn);
		mCNButton.setOnClickListener(this);
		mENButton.setOnClickListener(this);
		lv = findLinearLayoutById(R.id.start_lv);
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.start_layout;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId() == R.id.cn_btn)
		{
			//chinese
			Intent it = new Intent(StartActivity.this, MainShowBotomActivity.class);
			startActivity(it);
			
		}
		else if(v.getId() == R.id.en_btn)
		{
			//english758 480
			
		}
	}
	
	public void setThemeImage(){
		
		if(msharePreferenceUtil.loadBooleanSharedPreference("theme") == true)
		{
			//加载蓝色背景主题
			lv.setBackgroundResource(R.drawable.background_start_blue);
			mCNButton.setBackgroundResource(R.drawable.login_cn_blue_btn);
			mENButton.setBackgroundResource(R.drawable.login_en_blue_btn);
			
			
		}else if(msharePreferenceUtil.loadBooleanSharedPreference("theme") == false){
			
			//加载绿色主题
			lv.setBackgroundResource(R.drawable.background_green);
			mCNButton.setBackgroundResource(R.drawable.login_cn_green_btn);
			mENButton.setBackgroundResource(R.drawable.login_en_green_btn);
		}
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		setThemeImage();
	}
	
}
