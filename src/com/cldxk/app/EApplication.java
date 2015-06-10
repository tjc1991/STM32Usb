package com.cldxk.app;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import com.cldxk.stm32.utils.SharePreferenceUtil;

import android.app.Activity;
import android.app.Application;

public class EApplication extends Application {
	
	private static  SharePreferenceUtil sharePreferenceUtil;
	
	private HashMap<String, WeakReference<Activity>> activities = new HashMap<String, WeakReference<Activity>>();
		
	@Override
	public void onCreate() {
		super.onCreate();	
		
		sharePreferenceUtil = new SharePreferenceUtil(this);
		
	}
		
	@Override
	public void onTerminate() {		 
		super.onTerminate();
	}
	
	///////////////////////////////////////////////
	public synchronized void putActivity(String clzName,Activity activity)
	{
		WeakReference<Activity> rf = new WeakReference<Activity>(activity);
		activities.put(clzName, rf);
		
	}
	
	
	public synchronized Activity getActivity(String clzName)
	{
		
		WeakReference<Activity> rf = activities.get(clzName);
		if(null == rf)
		{
			return null;
		}
		Activity activity = rf.get();
		
		if(null == activity)
		{
			activities.remove(clzName);
		}
		
		return activity;
	}
	
	public static SharePreferenceUtil getSharePreferenceUtil()
	{
		return sharePreferenceUtil;
	}
	
}
