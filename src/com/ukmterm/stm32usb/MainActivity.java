package com.ukmterm.stm32usb;

import android.R.bool;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.viewtool.USBDriver.ErrorType;
import com.viewtool.USBDriver.UsbDriver;
import com.viewtool.USBDriver.UsbDriver.UsbFace;

public class MainActivity extends Activity {
	StringBuffer mStringBuffer_Console_Text = new StringBuffer("Show Info:\n");
	StringBuffer mStrRec = new StringBuffer("");
	
	// 权限
	private static final String ACTION_USB_PERMISSION = "com.viewtool.ginkgotest.USB_PERMISSION";
	// 接口类
	UsbDriver mUsbDriver;
	UsbManager mUsbManager;
	UsbDevice mUsbDevice;
	// 界面控件
	PendingIntent pendingIntent;
	Button mButtonStar;
	Button mbtnClcx;
	Button mbtnClcy;
	Button mbtnClcz;
	Button mbtnClcc;
	Button mbtnConnect;
	Button mbtnStop;
	Button mbtnQuit;
	Button mbtnEp1;
	Button mbtnEp2;
	Button mbtnEp3;
	Button mbtnEp4;
	
	TextView mtx_x;
	TextView mtx_y;
	TextView mtx_z;
	TextView mtx_c;
	
	TextView mTextView_ShowConsole;
	
	// usb监听
	MyHandler mHandler;
	Usb usbstates;
	
	MyListener mylistener = null;
	
	StringBuilder strbuld=null;
	
	byte[] ReadDataEP1 = new byte[24];
	
	private boolean isopenUsb;
	private boolean isrun;

	class MyHandler extends Handler {
		public MyHandler() {
		};

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 0x21) {
				System.out.println("USB 连接");
			} else if (msg.arg1 == 0x22) {
				System.out.println("USB 断开");
			}else if (msg.arg1 == 0x23) {
				showMsgDlg((String)msg.obj);
			}else if (msg.arg1 == 0x25) {
				mtx_x.setText("");
				byte[] tmpbyte =(byte[])msg.obj;
				ParaseByte2Xyz(tmpbyte);
			}	
			
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		isopenUsb = false;
		isrun = false;
		mHandler = new MyHandler();
		usbstates = new Usb(MainActivity.this);
		usbstates.registerReceiver();

		UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		registerReceiver(mUsbReceiver, filter);
		initall();

	}
	
	public void initall(){
		mTextView_ShowConsole = (TextView) findViewById(R.id.ShowConsole);
		mtx_x = (TextView)findViewById(R.id.tv_x);
		mtx_y = (TextView)findViewById(R.id.tv_y);
		mtx_z = (TextView)findViewById(R.id.tv_z);
		mtx_c = (TextView)findViewById(R.id.tv_c);
		
		mbtnClcx = (Button)findViewById(R.id.btn_clcx);
		mbtnClcy = (Button)findViewById(R.id.btn_clcy);
		mbtnClcz = (Button)findViewById(R.id.btn_clcz);
		mbtnClcc = (Button)findViewById(R.id.btn_clcc);
		
		mbtnConnect = (Button)findViewById(R.id.btn_ct);
		mbtnStop = (Button)findViewById(R.id.btn_disct);
		mbtnQuit = (Button)findViewById(R.id.btn_quit);
		
		mbtnEp1 = (Button)findViewById(R.id.btn_ep2);
		mbtnEp2 = (Button)findViewById(R.id.btn_ep3);
		mbtnEp3 = (Button)findViewById(R.id.btn_ep4);
		mbtnEp4 = (Button)findViewById(R.id.btn_ep5);
		
		mylistener = new MyListener();
		mbtnClcx.setOnClickListener(mylistener);
		mbtnClcy.setOnClickListener(mylistener);
		mbtnClcz.setOnClickListener(mylistener);
		mbtnClcc.setOnClickListener(mylistener);
		
		mbtnConnect.setOnClickListener(mylistener);
		mbtnStop.setOnClickListener(mylistener);
		mbtnQuit.setOnClickListener(mylistener);
		
		mbtnEp1.setOnClickListener(mylistener);
		mbtnEp2.setOnClickListener(mylistener);
		mbtnEp3.setOnClickListener(mylistener);
		mbtnEp4.setOnClickListener(mylistener);
	
				
	}

	/**
	 * 配置usb
	 */
	private void config_usb() {
		// TODO Auto-generated method stub
		mUsbManager = (UsbManager) getSystemService(MainActivity.USB_SERVICE);
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		
		//mUsbDriver = new UsbDriver(mUsbManager, pendingIntent);
		mUsbDriver = new UsbDriver(mUsbManager, pendingIntent,new UsbFace() {
			
			@Override
			public void ReceiveMsg(String msg) {
				// TODO Auto-generated method stub
				//mTextView_ShowConsole.setText(msg);
			}
		});
		if(mUsbDriver!=null)
		mUsbDevice = mUsbDriver.ScanDevices();

		if (mUsbDevice != null) {
			//mStringBuffer_Console_Text.append("Find device sucessful.\n");
			//mTextView_ShowConsole.setText(mStringBuffer_Console_Text);
			//mTextView_ShowConsole.setText("Find device sucessful.\n");
			run_set();
		} else {
			//mStringBuffer_Console_Text.append("No device connected.\n");
			//mTextView_ShowConsole.setText(mStringBuffer_Console_Text);
			//mTextView_ShowConsole.setText("No device connected.\n");
			return;
		}
		// run_set();
	}

	private void run_set() {
		int ret;
		// TODO Auto-generated method stub

		ret = mUsbDriver.OpenDevice();
		if (ret != ErrorType.ERR_SUCCESS) {
			//System.out.println("Open device error.");
//			mStringBuffer_Console_Text.append("Open device error.\n");
//			mTextView_ShowConsole.setText(mStringBuffer_Console_Text);
			//mTextView_ShowConsole.setText("Open device error.");
			return;
		}else {
			System.out.println("Open device sucessful");
			//mStringBuffer_Console_Text.append("Open device sucessful.\n");
			//mTextView_ShowConsole.setText(mStringBuffer_Console_Text);
			//mTextView_ShowConsole.setText("Open device sucessful");
			isopenUsb = true;
			isrun = true;
			Thread th = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(isrun == true){
						deal_EP1task();
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			});
			th.start();
		}
	}
	
	public void ParaseByte2Xyz(byte[] msgbyte)
	{		
		byte[] x_byte = new byte[4];
		byte[] y_byte = new byte[4];
		byte[] z_byte = new byte[4];
		byte[] c_byte = new byte[4];
		x_byte[0]=msgbyte[2];
		x_byte[1]=msgbyte[3];
		x_byte[2]=msgbyte[4];
		x_byte[3]=msgbyte[5];
		
		y_byte[0]=msgbyte[6];
		y_byte[1]=msgbyte[7];
		y_byte[2]=msgbyte[8];
		y_byte[3]=msgbyte[9];
		
		z_byte[0]=msgbyte[10];
		z_byte[1]=msgbyte[11];
		z_byte[2]=msgbyte[12];
		z_byte[3]=msgbyte[13];
		
		c_byte[0]=msgbyte[14];
		c_byte[1]=msgbyte[15];
		c_byte[2]=msgbyte[16];
		c_byte[3]=msgbyte[17];
		
		mtx_x.setText(bytesToInt(x_byte)+"");
		mtx_y.setText(bytesToInt(y_byte)+"");
		mtx_z.setText(bytesToInt(z_byte)+"");
		mtx_c.setText(bytesToInt(c_byte)+"");
		
	}
	
    public int bytesToInt(byte[] bytes) {

        int addr = bytes[0] & 0xFF;

        addr |= ((bytes[1] << 8) & 0xFF00);

        addr |= ((bytes[2] << 16) & 0xFF0000);

        addr |= ((bytes[3] << 24) & 0xFF000000);

        return addr;

    }
	
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
	
	public void deal_EP1task(){
//		byte[] ReadData = new byte[24];
		
	int i_USBReadData_ep1 = mUsbDriver.USBReadData(Config.EP1_IN, ReadDataEP1,
			24, 500);
	if (i_USBReadData_ep1 != 24) {
		System.out.println("USBReadData error");
		//mTextView_ShowConsole.setText("error code = "
		//		+ i_USBReadData_ep2 + ".\n");
	} else {
		System.out.println("USBReadData sucessful");
		//mStringBuffer_Console_Text.append("USBReadData sucessful.\n");
		//mTextView_ShowConsole.setText(mStringBuffer_Console_Text);
		//mStringBuffer_Console_Text.append("Data : \n");
		//mStrRec.append(Integer.toHexString(ReadData.length));
//		for (int i = 0; i < ReadDataEP1.length; i++) {
//			Integer.toHexString(ReadDataEP1[i]);
//			//mStringBuffer_Console_Text.append(Integer
//			//		.toHexString(ReadData[i]) + " ");
//			//mTextView_ShowConsole.setText(mStringBuffer_Console_Text);
//			//showMsgDlg(mStringBuffer_Console_Text.toString());
//			mStrRec.append(Integer.toHexString(ReadDataEP1[i]));
//		}	
		//showMsgDlg(mStringBuffer_Console_Text.toString());
		Message message = mHandler.obtainMessage();
		message.arg1 = 0x25;
		//message.obj = mStrRec.toString();
		message.obj = ReadDataEP1;
		mHandler.sendMessage(message);
//		mStrRec.setLength(0);
		
	}
		
		
	}
	
	public void deal_EP2task(){
		byte[] WriteData = new byte[64];
		for (int i = 0; i < WriteData.length; i++) {
			WriteData[i] = (byte) (i + 1);
		}
		byte[] ReadData = new byte[64];
		
	int i_USBWriteData_ep2 = mUsbDriver.USBWriteData(Config.EP2_OUT,
			WriteData, 64, 500);
	if (i_USBWriteData_ep2 != 64) {
		System.out.println("USBWriteData error");
		//mTextView_ShowConsole.setText("USBWriteData error.\n");
	} else {
		System.out.println("USBWriteData sucessful");
		//mTextView_ShowConsole.setText("USBWriteData sucessful");
	}
	
	int i_USBReadData_ep2 = mUsbDriver.USBReadData(Config.EP2_IN, ReadData,
			64, 500);
	if (i_USBReadData_ep2 != 64) {
		System.out.println("USBReadData error");
		//mTextView_ShowConsole.setText("error code = "
		//		+ i_USBReadData_ep2 + ".\n");
	} else {
		System.out.println("USBReadData sucessful");
		mStringBuffer_Console_Text.append("USBReadData sucessful.\n");
		//mTextView_ShowConsole.setText(mStringBuffer_Console_Text);
		mStringBuffer_Console_Text.append("Data : \n");
		for (int i = 0; i < ReadData.length; i++) {
			Integer.toHexString(ReadData[i]);
			mStringBuffer_Console_Text.append(Integer
					.toHexString(ReadData[i]) + " ");
			//mTextView_ShowConsole.setText(mStringBuffer_Console_Text);
			//showMsgDlg(mStringBuffer_Console_Text.toString());
		}	
		//showMsgDlg(mStringBuffer_Console_Text.toString());
		Message message = mHandler.obtainMessage();
		message.arg1 = 0x23;
		message.obj = mStringBuffer_Console_Text.toString();
		mHandler.sendMessage(message);
		mStringBuffer_Console_Text.setLength(0);
	}
		
	}
	
	public void showMsgDlg(String msg){
		
        // 先创建对话框构造器
        AlertDialog.Builder builder =
            new AlertDialog.Builder(MainActivity.this);
        // 创建完后设置对话框的属性
        // 标题
        builder.setMessage(msg)
               // 不可取消（即返回键不能取消此对话框）
               .setCancelable(false)
               // 设置第一个按钮的标签及其事件监听器
               .setPositiveButton("确定",
                      new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog,
                                    int id) {
                      dialog.dismiss();
                  }
               });
        // 用对话框构造器创建对话框
        AlertDialog alert = builder.create();
        // 显示对话框
        alert.show();
	}

	// private static final String ACTION_USB_PERMISSION =
	// "com.android.example.USB_PERMISSION";
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					UsbDevice device = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						System.out.println("Authorization");
						if (mUsbDevice != null) {
							// run_set();
						}
					} else {
						System.out.println("不给权限");
						return;
					}
				}
			}
		}
	};
	
	class MyListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.btn_ct:
				Thread thread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						config_usb();
					}
				});
				thread.start();
				break;
			case R.id.btn_disct:

				break;
			case R.id.btn_quit:

				break;
			case R.id.btn_clcx:
				if(isopenUsb == true)
				{
					Thread thread1 = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ClearXyzc(1);
						}
					});
					thread1.start();
//					deal_EP2task();
				}else{
					showMsgDlg("请先点击连接按钮!");
				}
				break;
			case R.id.btn_clcy:
				if(isopenUsb == true)
				{
					Thread thread1 = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ClearXyzc(2);
						}
					});
					thread1.start();
//					deal_EP2task();
				}else{
					showMsgDlg("请先点击连接按钮!");
				}
				break;
			case R.id.btn_clcz:
				if(isopenUsb == true)
				{
					Thread thread1 = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ClearXyzc(3);
						}
					});
					thread1.start();
//					deal_EP2task();
				}else{
					showMsgDlg("请先点击连接按钮!");
				}
				break;
				
			case R.id.btn_clcc:
				if(isopenUsb == true)
				{
					Thread thread1 = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							ClearXyzc(4);
						}
					});
					thread1.start();
//					deal_EP2task();
				}else{
					showMsgDlg("请先点击连接按钮!");
				}
				break;
				
			case R.id.btn_ep5:

				break;
			case R.id.btn_ep2:
				if(isopenUsb == true)
				{
					Thread thread1 = new Thread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							deal_EP2task();
						}
					});
					thread1.start();
//					deal_EP2task();
				}else{
					showMsgDlg("请先点击连接按钮!");
				}
				break;
			case R.id.btn_ep3:

				break;
			case R.id.btn_ep4:

				break;

			default:
				break;
			}
			
		}
		
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isrun =false;
	}
	
	//on
}
