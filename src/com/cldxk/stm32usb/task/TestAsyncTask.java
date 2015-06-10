package com.cldxk.stm32usb.task;

import android.app.Activity;

public class TestAsyncTask extends EBaseAsyncTask<String, Void, String> {

	public TestAsyncTask(Activity activity) {
		super(activity);
		
		disDialog();
		
	}

	@Override
	void onBeforeTask() {
		
		System.out.println("onBeforeTask");
	}

	@Override
	String onTask(String... params) throws Exception {
		
	      Thread.sleep(5000);
		
		System.out.println("onTask");
		Integer.parseInt(params[0]);
		
		return "I am "  + params[0];
	}

	@Override
	void onAfterTask(String result) {
	
		System.out.println("onAfterTask =" + result);
		
	}

	@Override
	void onTaskError(Exception exception)
	{
		System.out.println("onTaskError : " + exception);
	}

}
