package com.cldxk.stm32usb.task;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.cldxk.app.EApplication;
import com.cldxk.stm32usb.ui.base.EBaseActivity;

public abstract class EBaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	
	private int mDialogID;
	
	private boolean isBaseActivity;
	
	private String clzName ;
	
	private EApplication eapplication;
	
	private Exception exception;
	
	
	public EBaseAsyncTask(Activity activity)
	{
		
		clzName = activity.getClass().getCanonicalName();
		Application application = activity.getApplication();
		if(application instanceof EApplication)
		{
			eapplication = ((EApplication)application);
			eapplication.putActivity(clzName, activity);
		}
		
		isBaseActivity = activity instanceof EBaseActivity;
		
	}
	
		
	public   void  disDialog()
	{
		mDialogID =-1;
	}
	
	/**
	 * 获取执行Task的Activity
	 * @return
	 */
	protected Context getActivity()
	{
	     Activity activity = 	eapplication.getActivity(clzName);
		
	     if(null == activity || activity.isFinishing())
	    	return null;
	     
	     return activity;
	}
	
	
	
	/**
	 * 判断有没有异常
	 * @return
	 */
	private boolean hasException()
	{
		
		return exception != null;
	}
	
	
	@Override
	protected void onPreExecute() {
		
		
		Context activity = getActivity();
		
		if(null == activity)
		{
			throw new RuntimeException("activity is null");
		}
		if(isBaseActivity)
		{
			
			if(mDialogID > -1 )
		      ((Activity)activity).showDialog(mDialogID);
		}
		
		onBeforeTask();
	}
	
	
	
	protected Result doInBackground(Params... params) {
		
		try {
			
			return onTask(params);
		} catch (Exception e) {
		
			exception = e;
			return null;
		}
	}
	
	
	protected void onPostExecute(Result result) 
	{
		
		Context activity = getActivity();
		
	  
		if(isBaseActivity)
		{
			if(mDialogID > -1 )
				((Activity)activity).removeDialog(mDialogID);
		}
		
		if(hasException())
		{
			onTaskError(exception);
		}
		else
		{
		onAfterTask(result);
		}
	};
	
	abstract void onBeforeTask();
	
	abstract Result onTask(Params... params) throws Exception;
	
	abstract void onAfterTask(Result result);
	
	abstract void onTaskError(Exception exception);
	
	
}
