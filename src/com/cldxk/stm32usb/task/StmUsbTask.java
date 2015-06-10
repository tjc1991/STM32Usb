package com.cldxk.stm32usb.task;

import com.cldxk.stm32.utils.StmUsbCommond;
import com.ukmterm.stm32usb.Config;
import com.viewtool.USBDriver.UsbDriver;

import android.os.AsyncTask;
import android.widget.Switch;

/**
 * 用于实现USB对XYZC轴的清零操作
 * @author apple
 *
 */
public class StmUsbTask extends AsyncTask<Integer, Void, Void>{
	
	UsbDriver mUsbDriver = null;
	AfterAsyncTaskCallback callback = null;
	int taskid = 0;
	
	public StmUsbTask(UsbDriver mUsbDriver) {
		// TODO Auto-generated constructor stub
		super();
		
		this.mUsbDriver = mUsbDriver;
		
	}
	
	public StmUsbTask(UsbDriver mUsbDriver,AfterAsyncTaskCallback callback,int taskid) {
		// TODO Auto-generated constructor stub
		super();
		
		this.mUsbDriver = mUsbDriver;
		this.callback = callback;
		this.taskid = taskid;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		
		int index = params[0];
		switch (index) {
		case StmUsbCommond.CMD_CLEAR_X_AXIAL:
			//对X轴进行清零操作
			ClearXyzc(StmUsbCommond.CMD_CLEAR_X_AXIAL);
			break;
			
		case StmUsbCommond.CMD_CLEAR_Y_AXIAL:
			//对Y轴进行清零操作
			ClearXyzc(StmUsbCommond.CMD_CLEAR_Y_AXIAL);
			break;
			
		case StmUsbCommond.CMD_CLEAR_Z_AXIAL:
			//对Z轴进行清零操作
			ClearXyzc(StmUsbCommond.CMD_CLEAR_Z_AXIAL);
			break;

		default:
			break;
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		//callback
		//this.callback.afterDoTask(taskid);
		
	}
	
	/**
	 * 实现对XYZC轴的清零操作
	 * @param index
	 */
	public void ClearXyzc(int index)
	{
		byte[] WriteData = new byte[3];
		WriteData[0]=(byte)0X7E;
		WriteData[1]=(byte)index;
		WriteData[2]=(byte)0XE7;
		
		int i_USBWriteData_ep1 = mUsbDriver.USBWriteData(Config.EP1_OUT,
				WriteData, 3, 500);
		if (i_USBWriteData_ep1 != 3) {
			System.out.println("USBWriteData error");
		} else {
			System.out.println("USBWriteData sucessful");
		}	
	}
	
	public interface AfterAsyncTaskCallback{
		
		public void afterDoTask(int taskid);
	}
	
	

}
