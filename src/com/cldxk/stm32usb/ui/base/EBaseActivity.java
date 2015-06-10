package com.cldxk.stm32usb.ui.base;

import com.cldxk.stm32.utils.ActivityStackUtil;
import com.cldxk.stm32.utils.SharePreferenceUtil;
import com.cldxk.stm32usb.task.AsyncTaskListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cldxk.app.EApplication;

public  abstract class EBaseActivity extends Activity implements AsyncTaskListener {
	
	public SharePreferenceUtil msharePreferenceUtil;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title  
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏  
		
		//getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);//隐藏底部任务栏代码		setContentView(setLayout());
		setContentView(setLayout());
		ActivityStackUtil.add(this);
		
		((EApplication)getApplication()).putActivity(getClass().getCanonicalName(), this);
		
		msharePreferenceUtil = ((EApplication)getApplication()).getSharePreferenceUtil();
	}
	
	
	protected void showToastMsg(int  msg)
	{
		showToastMsg(getString(msg));
	}
	
	protected void showToastMsg(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityStackUtil.remove(this);
	}
	
	
	///////////////////////////////////////////////
	
	
  public abstract	int setLayout();
	
	
	protected Dialog onCreateDialog(int id) {
		
		return ActivityHelper.createLoadingDialog(this);
	}


	public void onBeforeTask(Context context,int taskId) {
	}


	public Object onTask(Context context,int taskId,Object... params) throws Exception {
		return null;
	}


	public void onAfterTask(Context context,int taskId,Object result) {
		
	}


	@Override
	public void onTaskError(Context context,int taskId,Exception exception) {
		
	}

//////////////////////////View ////////////////////////////////

	protected Button findButtonById(int id)
	{
	return (Button) this.findViewById(id);
	}

	protected ListView findListViewById(int id)
	{
	return (ListView) this.findViewById(id);
	}

	protected ImageView findImageViewById(int id)
	{
	return (ImageView) this.findViewById(id);
	}

	protected SurfaceView findSurfaceViewById(int id)
	{
	return (SurfaceView) this.findViewById(id);
	}


	protected ProgressBar findProgressBarById(int id)
	{
	return (ProgressBar) this.findViewById(id);
	}

	protected TextView findTextViewById(int id)
	{
	return (TextView) this.findViewById(id);
	}

	protected EditText findEditTextById(int id)
	{
	return (EditText) this.findViewById(id);
	}


	protected LinearLayout findLinearLayoutById(int id)
	{
	return (LinearLayout) this.findViewById(id);
	}


	protected RelativeLayout findRelativeLayoutById(int id)
	{
	return (RelativeLayout) this.findViewById(id);
	}
	
	
	
}

