package com.cldxk.stm32usb.task;

import android.app.Activity;

public class EDefaultAsyncTask extends EBaseAsyncTask<Object,Void , Object> 
{

	
	private static final int DEFAULT_TASK_ID=10000;
	
	private int mTaskID;
	
	private AsyncTaskListener mAsyncTaskListener;
	
	
	public int getTaskID() {
		return mTaskID;
	}
	
	public void setTaskID(int taskID)
	{
		this.mTaskID = taskID;
	}
	
	
	public EDefaultAsyncTask(Activity activity,AsyncTaskListener asyncTaskListener) 
	{
		this(activity, true,asyncTaskListener);
	}
	
	public EDefaultAsyncTask(Activity activity, boolean isShowDilg,AsyncTaskListener asyncTaskListener) 
	{
//		this(activity,DEFAULT_TASK_ID, true,asyncTaskListener);
		this(activity,DEFAULT_TASK_ID, isShowDilg,asyncTaskListener);
	}
	public EDefaultAsyncTask(Activity activity, int taskID, boolean isShowDilg,AsyncTaskListener asyncTaskListener) 
	{
		super(activity);
		
		this.mAsyncTaskListener = asyncTaskListener;
	    setTaskID(taskID);
		if(!isShowDilg)
			disDialog();
	}
	
	
	@Override
	void onBeforeTask() {
		
		mAsyncTaskListener.onBeforeTask(getActivity(),mTaskID);
	}

	@Override
	Object onTask(Object... params) throws Exception {
		// TODO Auto-generated method stub
		return mAsyncTaskListener.onTask(getActivity(),mTaskID,params);
	}

	@Override
	void onAfterTask(Object result) {
		
		mAsyncTaskListener.onAfterTask(getActivity(),mTaskID,result);
	}

	void onTaskError(Exception exception) {
		
		mAsyncTaskListener.onTaskError(getActivity(),mTaskID,exception);
		
	}

}
