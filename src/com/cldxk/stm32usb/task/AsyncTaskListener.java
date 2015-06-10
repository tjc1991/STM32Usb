package com.cldxk.stm32usb.task;

import android.content.Context;

public interface AsyncTaskListener {

	
	void onBeforeTask(Context context,int taskId);

	Object onTask(Context context,int taskId,Object... params) throws Exception;

	void onAfterTask(Context context,int taskId,Object result) ;

	void onTaskError(Context context,int taskId,Exception exception);
	
}
