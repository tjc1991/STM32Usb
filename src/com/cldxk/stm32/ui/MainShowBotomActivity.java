package com.cldxk.stm32.ui;

import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cldxk.stm32.utils.AxialManager;
import com.cldxk.stm32.utils.NumberValidationUtils;
import com.cldxk.stm32.utils.StmUsbCommond;
import com.cldxk.stm32usb.task.StmUsbTask;
import com.cldxk.stm32usb.ui.base.EBaseActivity;
import com.ukmterm.stm32usb.Config;
import com.ukmterm.stm32usb.R;
import com.ukmterm.stm32usb.Usb;
import com.viewtool.USBDriver.ErrorType;
import com.viewtool.USBDriver.UsbDriver;
import com.viewtool.USBDriver.UsbDriver.UsbFace;

@SuppressLint("ResourceAsColor")
public class MainShowBotomActivity extends EBaseActivity implements OnClickListener{
	
	//数值配置类
	String x_fblstr = "";
	Boolean x_jsfxnum = false;
	Boolean x_dwmsnum = false;
	Boolean x_zbjnum = false;
	Boolean x_zbmsnum = false;
	String x_xxbcstr = "";
	String x_sslstr = "";
	
	String y_fblstr = "";
	Boolean y_jsfxnum = false;
	Boolean y_dwmsnum = false;
	Boolean y_zbjnum = false;
	Boolean y_zbmsnum = false;
	String y_xxbcstr = "";
	String y_sslstr = "";
	
	String z_fblstr = "";
	Boolean z_jsfxnum = false;
	Boolean z_dwmsnum = false;
	Boolean z_zbjnum = false;
	Boolean z_zbmsnum = false;
	String z_xxbcstr = "";
	String z_sslstr = "";
	
	//相对坐标下记录的XYZ轴数值
	float x_tmpvalu = 0.0f;
	float y_tmpvalu = 0.0f;
	float z_tmpvalu = 0.0f;
	
	//当前计数值
	float x_setnum = 0.0f;
	float y_setnum = 0.0f;
	float z_setnum = 0.0f;
	
	//XYZ轴显示1和显示2设置值
	float dshow_xvalue = 0.0f;
	float dshow_two_xvalue = 0.0f;
	
	float dshow_yvalue = 0.0f;
	float dshow_two_yvalue = 0.0f;
	
	float dshow_zvalue = 0.0f;
	float dshow_two_zvalue = 0.0f;
	
	
	
	
	
	String[] fblarray = {"0.1um","0.2um","0.25um","0.5um","1um","2um","5um","10um","20um"};
	
	//布局类
	private LinearLayout lv = null;
	private Button sys_btn = null;
	private Button mm_btn = null;
	private Button abs_btn = null;
	private Button ref_btn = null;
	private Button zs_btn = null;
	private Button rd_btn = null;
	private Button kz_btn = null;
	private Button help_btn = null;
	private Button clearx_btn = null;
	private Button cleary_btn = null;
	private Button clearz_btn = null;
	private TextView tv_x =null;
	private TextView tv_y =null;
	private TextView tv_z =null;
	
	private boolean is_x = false;
	private boolean is_y = false;
	private boolean is_z = false;
	
	private boolean is_mm = true;
	private boolean is_abs = false;
	private boolean is_rd = false;
	
	private Boolean cur_theme = false;
	private boolean isrun;
	
	//USB操作类
	// 权限
	private static final String ACTION_USB_PERMISSION = "com.viewtool.ginkgotest.USB_PERMISSION";
	// 接口类
	UsbDriver mUsbDriver;
	UsbManager mUsbManager;
	UsbDevice mUsbDevice;
	// 界面控件
	PendingIntent pendingIntent;
	
	// usb监听
	public MyHandler mHandler;
	Usb usbstates;
	
	byte[] ReadDataEP1 = new byte[24];
	
	private boolean isopenUsb;
	
	//mm/inc/ref/r/d
	private boolean other_isok = false;
	
	private int current_axial = 0;
	
	
	public class MyHandler extends Handler {
		public MyHandler() {
		};

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.arg1 == 0x21) {
				showMsg("USB 连接");
			} else if (msg.arg1 == 0x22) {
				showMsg("USB 断开");
			}else if (msg.arg1 == 0x23) {
				
			}else if (msg.arg1 == 0x25) {
				//showMsg("ok");
				byte[] tmpbyte =(byte[])msg.obj;
				ParaseByte2Xyz(tmpbyte);
			}else if (msg.arg1 == 0x51) {
				showMsg("发现设备");
			}else if (msg.arg1 == 0x52) {
				showMsg("未发现设备");
			}	
			
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//初始化标志位
		initFlagSetting();
		
		//初始化视图
		initMyView();
		
		//初始化USB设置
		initUsbSetting();
		
		//检测USB连接
		checkUsbConnect();
		
		
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.mainshow_botom_layout;
	}
	
	/**
	 * 初始化标志位
	 */
	public void initFlagSetting(){
//		is_mm = msharePreferenceUtil.loadBooleanSharedPreference("is_mm");
//		is_abs = msharePreferenceUtil.loadBooleanSharedPreference("is_abs");
//		is_rd = msharePreferenceUtil.loadBooleanSharedPreference("is_rd");
		
		is_mm = false;
		is_abs = false;
		is_rd = false;
		
		String tx = msharePreferenceUtil.loadStringSharedPreference("x_zhis_tv", "0");
		String ty = msharePreferenceUtil.loadStringSharedPreference("y_zhis_tv", "0");
		String tz = msharePreferenceUtil.loadStringSharedPreference("z_zhis_tv", "0");
		if(NumberValidationUtils.isRealNumber(tx) == true){
			x_setnum = Float.parseFloat(tx);
		}else{
			x_setnum = 0.0f;	
		}
		
		if(NumberValidationUtils.isRealNumber(ty) == true){
			y_setnum = Float.parseFloat(ty);
		}else{
			y_setnum = 0.0f;	
		}
		
		if(NumberValidationUtils.isRealNumber(tz) == true){
			z_setnum = Float.parseFloat(tz);
		}else{
			z_setnum = 0.0f;	
		}
		
//		y_setnum = msharePreferenceUtil.loadFloatSharedPreference("x_zhis_tv");
//		z_setnum = msharePreferenceUtil.loadFloatSharedPreference("x_zhis_tv");
		
		//usb
		isopenUsb = false;
		isrun = false;
		
		//mm/inc/rd
		other_isok = false;
		
		current_axial = 0;
		
		//X轴显示设置
		x_tmpvalu = 0.0f;
		dshow_xvalue = 0.0f;
		dshow_two_xvalue = 0.0f;
		
		//Y轴显示设置
		y_tmpvalu = 0.0f;
		dshow_yvalue = 0.0f;
		dshow_two_yvalue = 0.0f;
		
		//Z轴显示设置
		z_tmpvalu = 0.0f;
		dshow_zvalue = 0.0f;
		dshow_two_zvalue = 0.0f;
		
		
		
	}

	/**
	 * 初始化视图
	 */
	public void initMyView(){
		sys_btn = findButtonById(R.id.xtsz_btn);
		sys_btn.setOnClickListener(this);
		mm_btn = findButtonById(R.id.mm_btn);
		mm_btn.setOnClickListener(this);
		abs_btn = findButtonById(R.id.abs_btn);
		abs_btn.setOnClickListener(this);
		ref_btn = findButtonById(R.id.ref_btn);
		ref_btn.setOnClickListener(this);
		zs_btn = findButtonById(R.id.zs_btn);
		zs_btn.setOnClickListener(this);
		rd_btn = findButtonById(R.id.rd_btn);
		rd_btn.setOnClickListener(this);
		kz_btn = findButtonById(R.id.kz_btn);
		kz_btn.setOnClickListener(this);
		help_btn = findButtonById(R.id.help_btn);
		help_btn.setOnClickListener(this);
		clearx_btn = findButtonById(R.id.btn_clearx);
		clearx_btn.setOnClickListener(this);
		cleary_btn = findButtonById(R.id.btn_cleary);
		cleary_btn.setOnClickListener(this);
		clearz_btn = findButtonById(R.id.btn_clearz);
		clearz_btn.setOnClickListener(this);
						
		tv_x = findTextViewById(R.id.set_x);
		tv_y = findTextViewById(R.id.set_y);
		tv_z = findTextViewById(R.id.set_z);
		tv_x.setOnClickListener(this);
		tv_y.setOnClickListener(this);
		tv_z.setOnClickListener(this);
				
		lv = findLinearLayoutById(R.id.main_lv);
		
	}
	
	/**
	 * 初始化USB设置
	 */
	public void initUsbSetting(){
		mHandler = new MyHandler();
		usbstates = new Usb(MainShowBotomActivity.this);
		usbstates.registerReceiver();

		UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		registerReceiver(mUsbReceiver, filter);

		
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
							UsbManager.EXTRA_PERMISSION_GRANTED, true)) {
						System.out.println("Authorization");
						if (mUsbDevice != null) {
							// run_set();
							showMsg("连接OK");
							run_set();
						}
					} else {
						System.out.println("不给权限");
						return;
					}
				}
			}
		}
	};
	
	/**
	 * 检测USB是否连接
	 */
	public void checkUsbConnect(){
		
		StartConnectUSBThread th = new StartConnectUSBThread();
		th.start();
	}
	
	/**
	 * 配置usb
	 */
	private void config_usb() {
		// TODO Auto-generated method stub
		mUsbManager = (UsbManager) getSystemService(MainShowBotomActivity.USB_SERVICE);
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);

		mUsbDriver = new UsbDriver(mUsbManager, pendingIntent,new UsbFace() {
			
			@Override
			public void ReceiveMsg(String msg) {
				// TODO Auto-generated method stub
				
			}
		});
		if(mUsbDriver!=null)
		mUsbDevice = mUsbDriver.ScanDevices();

		if (mUsbDevice != null) {
			//Toast.makeText(this, "发现设备", Toast.LENGTH_SHORT);
			mHandler.sendEmptyMessage(0x51);
			//run_set();
		} else {
			mHandler.sendEmptyMessage(0x52);
			//Toast.makeText(this, "无设备连接", Toast.LENGTH_SHORT);
			return;
		}
	}
	
	
	/**
	 * USB初始化连接线程
	 * @author apple
	 *
	 */
	class StartConnectUSBThread extends Thread{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			
			config_usb();
			
		}
	}
	
	private void run_set() {
		int ret;
		// TODO Auto-generated method stub

		ret = mUsbDriver.OpenDevice();
		if (ret != ErrorType.ERR_SUCCESS) {
			return;
		}else {
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
	
	public void deal_EP1task(){
		
	int i_USBReadData_ep1 = mUsbDriver.USBReadData(Config.EP1_IN, ReadDataEP1,
			24, 500);
	if (i_USBReadData_ep1 != 24) {
		System.out.println("USBReadData error");
	} else {
		System.out.println("USBReadData sucessful");
		Message message = mHandler.obtainMessage();
		message.arg1 = 0x25;
		//message.obj = mStrRec.toString();
		message.obj = ReadDataEP1;
		mHandler.sendMessage(message);
		
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
		
		float xvalue = ParaseXtoNumMsg(bytesToInt(x_byte));
		float yvalue = ParaseYtoNumMsg(bytesToInt(y_byte));
		float zvalue = ParaseZtoNumMsg(bytesToInt(z_byte));
		
		dshow_xvalue = xvalue - x_tmpvalu;
		float dfinally_value = dshow_xvalue + x_setnum - dshow_two_xvalue;
		
		dshow_yvalue = yvalue - y_tmpvalu;
		float dfinally_yvalue = dshow_yvalue + y_setnum - dshow_two_yvalue;
		
		dshow_zvalue = zvalue - z_tmpvalu;
		float dfinally_zvalue = dshow_zvalue + z_setnum - dshow_two_zvalue;
		
		DecimalFormat decimalFormat=new DecimalFormat("0.000");
		
		tv_x.setText(decimalFormat.format(dfinally_value)+"");
		tv_y.setText(decimalFormat.format(dfinally_yvalue)+"");
		tv_z.setText(decimalFormat.format(dfinally_zvalue)+"");
		
	}
	
    public int bytesToInt(byte[] bytes) {

        int addr = bytes[0] & 0xFF;

        addr |= ((bytes[1] << 8) & 0xFF00);

        addr |= ((bytes[2] << 16) & 0xFF0000);

        addr |= ((bytes[3] << 24) & 0xFF000000);

        return addr;

    }
    
    /**
     * Inch/MM
     * @param setflag
     */
    public void ChangInchAndMm(int axial,Boolean setflag){
    	
    		switch (axial) {
			case 1:
				//保存
				msharePreferenceUtil.saveSharedPreferences("x_is_dwms", setflag);
				//加载
	    			x_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("x_is_dwms");	
				break;
				
			case 2:
				//保存
				msharePreferenceUtil.saveSharedPreferences("y_is_dwms", setflag);
				//加载
				y_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("y_is_dwms");
				break;
				
			case 3:
				//保存
				msharePreferenceUtil.saveSharedPreferences("z_is_dwms", setflag);
				//加载
				z_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("z_is_dwms");	
				break;

			default:
				break;
			}
    	
    	
//    		//保存
//    		msharePreferenceUtil.saveSharedPreferences("x_is_dwms", setflag);
//    		msharePreferenceUtil.saveSharedPreferences("y_is_dwms", setflag);
//    		msharePreferenceUtil.saveSharedPreferences("z_is_dwms", setflag);
//    		
//    		//加载
//    		x_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("x_is_dwms");
//    		y_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("y_is_dwms");
//    		z_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("z_is_dwms");
    		
	}
    
    /**
     * ABS/INC
     * @param setflag
     */
    public void ChangAbsAndInc(int axial,Boolean setflag){
    	
		switch (axial) {
		case 1:
			//保存
			msharePreferenceUtil.saveSharedPreferences("x_is_zbms", setflag);
			//加载
			x_zbmsnum = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbms");	
			break;
			
		case 2:
			//保存
			msharePreferenceUtil.saveSharedPreferences("y_is_zbms", setflag);
			//加载
			y_zbmsnum = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbms");
			break;
			
		case 3:
			//保存
			msharePreferenceUtil.saveSharedPreferences("z_is_zbms", setflag);
			//加载
			z_zbmsnum = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbms");	
			break;

		default:
			break;
		}
    	
//    		//保存
//    		msharePreferenceUtil.saveSharedPreferences("x_is_zbms", setflag);
//    		msharePreferenceUtil.saveSharedPreferences("y_is_zbms", setflag);
//    		msharePreferenceUtil.saveSharedPreferences("z_is_zbms", setflag);
//    		
//    		//加载
//    		x_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbms");
//    		y_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbms");
//    		z_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbms");
    		
	}
    
    /**
     * R/D
     * @param setflag
     */
    public void ChangRAndD(int axial,Boolean setflag){
    	
		switch (axial) {
		case 1:
			//保存
			msharePreferenceUtil.saveSharedPreferences("x_is_zbj", setflag);
			//加载
    			x_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbj");	
			break;
			
		case 2:
			//保存
			msharePreferenceUtil.saveSharedPreferences("y_is_zbj", setflag);
			//加载
			y_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbj");
			break;
			
		case 3:
			//保存
			msharePreferenceUtil.saveSharedPreferences("z_is_zbj", setflag);
			//加载
			z_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbj");	
			break;

		default:
			break;
		}
    	
//    		//保存
//    		msharePreferenceUtil.saveSharedPreferences("x_is_zbj", setflag);
//    		msharePreferenceUtil.saveSharedPreferences("y_is_zbj", setflag);
//    		msharePreferenceUtil.saveSharedPreferences("z_is_zbj", setflag);
//    		
//    		//加
//    		x_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbj");
//    		y_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbj");
//    		z_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbj");
    		
	}
    
    public void loadNumSetting(){
    	
	    	x_fblstr = fblarray[msharePreferenceUtil.loadIntSharedPreference("x_mfbl")];
	    	Log.i("tjc", "x_fblstr="+x_fblstr+"");
	    	
	    x_jsfxnum = msharePreferenceUtil.loadBooleanSharedPreference("x_isjsfx");
	    Log.i("tjc", "x_jsfxnum="+x_jsfxnum+"");
	    x_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("x_is_dwms");
	    Log.i("tjc", "x_dwmsnum="+x_dwmsnum+"");
	    x_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbj");
	    Log.i("tjc", "x_zbjnum="+x_zbjnum+"");
	    x_xxbcstr = msharePreferenceUtil.loadStringSharedPreference("x_xxbc_tv", "0");
	    Log.i("tjc", "x_xxbcstr="+x_xxbcstr+"");
	    x_sslstr = msharePreferenceUtil.loadStringSharedPreference("x_suosl_tv", "1");
	    Log.i("tjc", "x_sslstr="+x_sslstr+"");
	    
		y_fblstr = fblarray[msharePreferenceUtil.loadIntSharedPreference("y_mfbl")];
	 	Log.i("tjc", "x_fblstr="+y_fblstr+"");
	 	
	 	String	newfblstr = x_fblstr.substring(0, x_fblstr.length()-2);
	 	Log.i("tjc", "newfblstr="+newfblstr);
	 	
	 	Log.i("tjc", "float="+Float.parseFloat(newfblstr)+"");
	 	
	    y_jsfxnum = msharePreferenceUtil.loadBooleanSharedPreference("y_isjsfx");
	    y_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("y_is_dwms");
	    y_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbj");
	    y_xxbcstr = msharePreferenceUtil.loadStringSharedPreference("y_xxbc_tv", "0");
	    y_sslstr = msharePreferenceUtil.loadStringSharedPreference("y_suosl_tv", "1");
    
		z_fblstr = fblarray[msharePreferenceUtil.loadIntSharedPreference("z_mfbl")];
	 	Log.i("tjc", "z_fblstr="+x_fblstr+"");
	 	
		z_jsfxnum = msharePreferenceUtil.loadBooleanSharedPreference("z_isjsfx");
		z_dwmsnum = msharePreferenceUtil.loadBooleanSharedPreference("z_is_dwms");
		z_zbjnum = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbj");
		z_xxbcstr = msharePreferenceUtil.loadStringSharedPreference("z_xxbc_tv", "0");
		z_sslstr = msharePreferenceUtil.loadStringSharedPreference("z_suosl_tv", "1");
		
		String tx = msharePreferenceUtil.loadStringSharedPreference("x_zhis_tv", "0");
		String ty = msharePreferenceUtil.loadStringSharedPreference("y_zhis_tv", "0");
		String tz = msharePreferenceUtil.loadStringSharedPreference("z_zhis_tv", "0");
		if(NumberValidationUtils.isRealNumber(tx) == true){
			x_setnum = Float.parseFloat(tx);
		}else{
			x_setnum = 0.0f;	
		}
		
		if(NumberValidationUtils.isRealNumber(ty) == true){
			y_setnum = Float.parseFloat(ty);
		}else{
			y_setnum = 0.0f;	
		}
		
		if(NumberValidationUtils.isRealNumber(tz) == true){
			z_setnum = Float.parseFloat(tz);
		}else{
			z_setnum = 0.0f;	
		}
		
		
	    	    	    		    		    	
    }
    
    /**
     * X轴
     * @param num
     * @return
     */
    
    public float ParaseXtoNumMsg(int num){
   	
    	int jsfxvalue = 1;
    float dwmsvalue = 1.0f;
    float zbjvalue = 1.0f;   	
    	
    String	newfblstr = x_fblstr.substring(0, x_fblstr.length()-2);
    	
    	if(x_jsfxnum == true){
    		jsfxvalue = 1;
    	}else if(x_jsfxnum == false){
    		jsfxvalue = -1;
    	}
    	
    if(x_dwmsnum == true){
    		dwmsvalue = 1.0f;
    	}else if(x_dwmsnum == false){
    		dwmsvalue = (float) (1/25.4);
    	}
    
    if(x_zbjnum == true){
    		zbjvalue = 0.5f;
	}else if(x_zbjnum == false){
		zbjvalue = 1.0f;
	}
    
    float xxbcvalue = Float.parseFloat(x_xxbcstr);
    float sslvalue = Float.parseFloat(x_sslstr);
    float fblvalue = Float.parseFloat(newfblstr);
    
    return (num*fblvalue*jsfxvalue*dwmsvalue*zbjvalue)/((1-xxbcvalue)*sslvalue);
    //return 1.0f;
    	  	
    }
    
    /**
     * Y轴
     * @param num
     * @return
     */
    public float ParaseYtoNumMsg(int num){
       	
    	int jsfxvalue = 1;
    float dwmsvalue = 1.0f;
    float zbjvalue = 1.0f;   	
    	
    String	newfblstr = y_fblstr.substring(0, y_fblstr.length()-2);
    	
    	if(y_jsfxnum == true){
    		jsfxvalue = 1;
    	}else if(y_jsfxnum == false){
    		jsfxvalue = -1;
    	}
    	
    if(y_dwmsnum == true){
    		dwmsvalue = 1.0f;
    	}else if(y_dwmsnum == false){
    		dwmsvalue = (float) (1/25.4);
    	}
    
    if(y_zbjnum == true){
    		zbjvalue = 1.0f;
	}else if(y_zbjnum == false){
		zbjvalue = 0.5f;
	}
    
    float xxbcvalue = Float.parseFloat(y_xxbcstr);
    float sslvalue = Float.parseFloat(y_sslstr);
    float fblvalue = Float.parseFloat(newfblstr);
    
    return (num*fblvalue*jsfxvalue*dwmsvalue*zbjvalue)/((1-xxbcvalue)*sslvalue);
    //return 1.0f;
    	   	
    }
    
    /**
     * Z轴
     * @param num
     * @return
     */
    public float ParaseZtoNumMsg(int num){
       	
    	int jsfxvalue = 1;
    float dwmsvalue = 1.0f;
    float zbjvalue = 1.0f;   	
    	
    String	newfblstr = z_fblstr.substring(0, z_fblstr.length()-2);
    	
    	if(z_jsfxnum == true){
    		jsfxvalue = 1;
    	}else if(z_jsfxnum == false){
    		jsfxvalue = -1;
    	}
    	
    if(z_dwmsnum == true){
    		dwmsvalue = 1.0f;
    	}else if(z_dwmsnum == false){
    		dwmsvalue = (float) (1/25.4);
    	}
    
    if(z_zbjnum == true){
    		zbjvalue = 1.0f;
	}else if(z_zbjnum == false){
		zbjvalue = 0.5f;
	}
    
    float xxbcvalue = Float.parseFloat(z_xxbcstr);
    float sslvalue = Float.parseFloat(z_sslstr);
    float fblvalue = Float.parseFloat(newfblstr);
    
    return (num*fblvalue*jsfxvalue*dwmsvalue*zbjvalue)/((1-xxbcvalue)*sslvalue);
    //return 1.0f;   	
    	
    }
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.xtsz_btn:
			if(is_x || is_y || is_z)
			{
				Intent it = new Intent(MainShowBotomActivity.this, SettingChangeActivity.class);
				if(is_x){
					it.putExtra("x", 1);
				}				
				else if(is_y){
					it.putExtra("x", 2);
				}				
				else if(is_z){
					it.putExtra("x", 3);
				}
				
				startActivity(it);
			}else{
				
				ShowDlgMsg();				
			}
			break;
			
		case R.id.set_x:
			current_axial = 1;
			other_isok = true;			
			changeBotomImg(current_axial);
			tv_x.setBackgroundResource(R.color.select_color);
			tv_y.setBackgroundResource(R.color.white);
			tv_z.setBackgroundResource(R.color.white);
			is_x = true;
			is_y = false;
			is_z = false;
			break;
			
		case R.id.set_y:
			current_axial = 2;
			other_isok = true;
			changeBotomImg(current_axial);
			tv_y.setBackgroundResource(R.color.select_color);
			tv_x.setBackgroundResource(R.color.white);
			tv_z.setBackgroundResource(R.color.white);
			is_y = true;
			is_x = false;
			is_z = false;
			break;
			
		case R.id.set_z:
			current_axial = 3;
			other_isok = true;			
			changeBotomImg(current_axial);
			tv_z.setBackgroundResource(R.color.select_color);
			tv_y.setBackgroundResource(R.color.white);
			tv_x.setBackgroundResource(R.color.white);
			is_z = true;
			is_y = false;
			is_x = false;
			break;
			
		case R.id.btn_clearx:
				ShowClearDlgMsg(1);
			break;
			
		case R.id.btn_cleary:
			ShowClearDlgMsg(2);
			break;
			
		case R.id.btn_clearz:
			ShowClearDlgMsg(3);
			break;
			
		case R.id.zs_btn:
			if(other_isok == false){				
				ShowDlgMsg();
				
			}else if(other_isok == true){
				AxialManager.setAxialzs(current_axial, true);
				startActivityForResult(new Intent(this, CalActivity.class),111);	
			}
		break;	
		
		case R.id.mm_btn:
			if(cur_theme == false){
				if(other_isok == true){	
					is_mm = CheckMm_Abs_Rd(current_axial, 1);
					is_mm = !is_mm;
					if(is_mm){
						mm_btn.setBackgroundResource(R.drawable.mm_green_btn);
//						msharePreferenceUtil.saveSharedPreferences("is_mm", is_mm);
						ChangInchAndMm(current_axial,is_mm);
						msharePreferenceUtil.saveSharedPreferences("is_mm", is_mm);
						//msharePreferenceUtil.saveSharedPreferences(cur_axial.toLowerCase()+"_is_dwms", false);
					}else{
						mm_btn.setBackgroundResource(R.drawable.inch_green_btn);
//						msharePreferenceUtil.saveSharedPreferences("is_mm", is_mm);
						ChangInchAndMm(current_axial,is_mm);
						msharePreferenceUtil.saveSharedPreferences("is_mm", is_mm);
						//msharePreferenceUtil.saveSharedPreferences(cur_axial.toLowerCase()+"_is_dwms", true);
					}
				}else if(other_isok == false){				
					ShowDlgMsg();
					
				}
			}
			break;
			
		case R.id.abs_btn:
			if(cur_theme == false){
				
				if(other_isok == true){	
					is_abs = CheckMm_Abs_Rd(current_axial, 2);
					is_abs = !is_abs;
					if(is_abs){
						abs_btn.setBackgroundResource(R.drawable.abs_green_btn);
//						msharePreferenceUtil.saveSharedPreferences("is_abs", is_abs);
						ChangAbsAndInc(current_axial, is_abs);
						msharePreferenceUtil.saveSharedPreferences("is_abs", is_abs);
						//msharePreferenceUtil.saveSharedPreferences(cur_axialy.toLowerCase()+"_is_zbms", false);
					}else{						
						abs_btn.setBackgroundResource(R.drawable.inc_green_btn);
//						msharePreferenceUtil.saveSharedPreferences("is_abs", is_abs);
						ChangAbsAndInc(current_axial, is_abs);
						msharePreferenceUtil.saveSharedPreferences("is_abs", is_abs);
						//msharePreferenceUtil.saveSharedPreferences(cur_axialy.toLowerCase()+"_is_zbms", true);
					}
				}else if(other_isok == false){				
					ShowDlgMsg();
					
				}
			}
			break;
			
		case R.id.rd_btn:
			if(cur_theme == false){
				if(other_isok == true){	
					is_rd = CheckMm_Abs_Rd(current_axial, 3);
					is_rd = !is_rd;
					if(is_rd){
						rd_btn.setBackgroundResource(R.drawable.rd_green_btn);
//						msharePreferenceUtil.saveSharedPreferences("is_rd", is_mm);
						ChangRAndD(current_axial,is_rd);
						msharePreferenceUtil.saveSharedPreferences("is_rd", is_rd);
						//msharePreferenceUtil.saveSharedPreferences(cur_axialz.toLowerCase()+"_is_zbj", true);
					}else{
						rd_btn.setBackgroundResource(R.drawable.rd_d_green_btn);
//						msharePreferenceUtil.saveSharedPreferences("is_rd", is_mm);
						ChangRAndD(current_axial,is_rd);
						msharePreferenceUtil.saveSharedPreferences("is_rd", is_rd);
						//msharePreferenceUtil.saveSharedPreferences(cur_axialz.toLowerCase()+"_is_zbj", true);
					}
				}else if(other_isok == false){				
					ShowDlgMsg();
					
				}
			}
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * 检测MM/ABS/RD标志位
	 * @param axial
	 * @return
	 */
	Boolean CheckMm_Abs_Rd(int axial, int category){
		Boolean cur_flag = false;
		
		switch (axial) {
		case 1:
			{
				switch (category) {
				case 1:
						//mm/inch
				cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("x_is_dwms"); 
					break;
				case 2:
					//abs/inc
				 cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbms"); 
				break;
				case 3:
					//r/d
				 cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbj"); 
				break;

				default:
					break;
				}
			}
			break;
			
		case 2:
		{
			switch (category) {
			case 1:
					//mm/inch
			cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("y_is_dwms"); 
				break;
			case 2:
				//abs/inc
			 cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbms"); 
			break;
			case 3:
				//r/d
			 cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbj"); 
			break;

			default:
				break;
			}
		}
			break;
		case 3:
		{
			switch (category) {
			case 1:
					//mm/inch
			cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("z_is_dwms"); 
				break;
			case 2:
				//abs/inc
			 cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbms"); 
			break;
			case 3:
				//r/d
			 cur_flag = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbj"); 
			break;

			default:
				break;
			}
		}
			break;

		default:
			break;
		}
		
		return cur_flag;
	}
	
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		tv_x.setBackgroundResource(R.color.white);
		tv_y.setBackgroundResource(R.color.white);
		tv_z.setBackgroundResource(R.color.white);
		is_x = false;
		is_y = false;
		is_z = false;
	}
	
	public void setThemeImage(){
		
		String cur_axial = msharePreferenceUtil.loadStringSharedPreference("cur_axial", "X");
		
		Boolean cur_dwms = msharePreferenceUtil.loadBooleanSharedPreference(cur_axial.toLowerCase()+"_is_dwms");
		Boolean cur_zbms = msharePreferenceUtil.loadBooleanSharedPreference(cur_axial.toLowerCase()+"_is_zbms");
		Boolean cur_zbj = msharePreferenceUtil.loadBooleanSharedPreference(cur_axial.toLowerCase()+"_is_zbj");
		
		if(msharePreferenceUtil.loadBooleanSharedPreference("theme") == true)
		{
			//加载蓝色背景主题
			lv.setBackgroundResource(R.drawable.background_start_blue);
			clearx_btn.setBackgroundResource(R.drawable.x_clear_blue_btn);
			cleary_btn.setBackgroundResource(R.drawable.y_clear_blue_btn);
			clearz_btn.setBackgroundResource(R.drawable.z_clear_blue_btn);
			sys_btn.setBackgroundResource(R.drawable.sys_seting_blue_btn);
			
//			mm_btn.setBackgroundResource(R.drawable.mm_blue_btn);
//			abs_btn.setBackgroundResource(R.drawable.abs_blue_btn);
//			rd_btn.setBackgroundResource(R.drawable.rd_blue_btn);
			if(cur_dwms == true){				
				mm_btn.setBackgroundResource(R.drawable.mm_green_btn);
			}else{
				mm_btn.setBackgroundResource(R.drawable.inch_green_btn);
			}
			
			if(cur_zbms == true){				
				abs_btn.setBackgroundResource(R.drawable.abs_green_btn);
			}else{
				abs_btn.setBackgroundResource(R.drawable.inc_green_btn);
			}
			
			if(cur_zbj == true){				
				rd_btn.setBackgroundResource(R.drawable.rd_green_btn);
			}else{
				rd_btn.setBackgroundResource(R.drawable.rd_d_green_btn);
			}
			ref_btn.setBackgroundResource(R.drawable.ref_blue_btn);
			zs_btn.setBackgroundResource(R.drawable.zhishu_blue_btn);
//			rd_btn.setBackgroundResource(R.drawable.rd_blue_btn);
			kz_btn.setBackgroundResource(R.drawable.extension_blue_btn);
			help_btn.setBackgroundResource(R.drawable.help_blue_btn);

			
		}else if(msharePreferenceUtil.loadBooleanSharedPreference("theme") == false){
			
			//加载绿色主题
			lv.setBackgroundResource(R.drawable.background_green);
			clearx_btn.setBackgroundResource(R.drawable.x_clear_green_btn);
			cleary_btn.setBackgroundResource(R.drawable.y_clear_green_btn);
			clearz_btn.setBackgroundResource(R.drawable.z_clear_green_btn);
			sys_btn.setBackgroundResource(R.drawable.sys_seting_green_btn);
			
			if(cur_dwms == true){				
				mm_btn.setBackgroundResource(R.drawable.mm_green_btn);
			}else{
				mm_btn.setBackgroundResource(R.drawable.inch_green_btn);
			}
			
			if(cur_zbms == true){				
				abs_btn.setBackgroundResource(R.drawable.abs_green_btn);
			}else{
				abs_btn.setBackgroundResource(R.drawable.inc_green_btn);
			}
			
			if(cur_zbj == true){				
				rd_btn.setBackgroundResource(R.drawable.rd_green_btn);
			}else{
				rd_btn.setBackgroundResource(R.drawable.rd_d_green_btn);
			}
			
			ref_btn.setBackgroundResource(R.drawable.ref_green_btn);
			zs_btn.setBackgroundResource(R.drawable.zhishu_green_btn);
//			rd_btn.setBackgroundResource(R.drawable.rd_green_btn);
			kz_btn.setBackgroundResource(R.drawable.extension_green_btn);
			help_btn.setBackgroundResource(R.drawable.help_green_btn);
		}
		
	}
	
	
	public void ShowDlgMsg() {
		final AlertDialog.Builder builder = new AlertDialog.Builder(
				this);
		final Dialog dialog = builder.show();
		dialog.setCancelable(false);
		Window window = dialog.getWindow();
		window.setContentView(R.layout.axal_dialog);
		Button logout = (Button) window.findViewById(R.id.confirm_btn);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				dialog.dismiss();

			}
		});

	}
	
	public void ShowClearDlgMsg(final int index) {
	
		if(AxialManager.getAxialQltx(index) == false){
			ClearAxialToZero(index);
			return;
		}
		
	final AlertDialog.Builder builder = new AlertDialog.Builder(
				this);
		final Dialog dialog = builder.show();
		//不可触摸取消
		dialog.setCancelable(false);
		
		Window window = dialog.getWindow();
		window.setContentView(R.layout.clear_dialog);
		CheckBox showcheck = (CheckBox) window.findViewById(R.id.qltx_checkbox);
		showcheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				
				if(isChecked == true){
					AxialManager.setAxialQltx(index, false);
				}
			}
		});
		Button logout = (Button) window.findViewById(R.id.confirm_btn);
		logout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				dialog.dismiss();
				ClearAxialToZero(index);

			}
		});
		
		Button cancelout = (Button) window.findViewById(R.id.cancel_btn);
		cancelout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				dialog.dismiss();

			}
		});

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		other_isok = false;
		//current_axial = 0;
		cur_theme = msharePreferenceUtil.loadBooleanSharedPreference("theme");
		
		tv_x.setBackgroundResource(R.color.white);
		tv_y.setBackgroundResource(R.color.white);
		tv_z.setBackgroundResource(R.color.white);
		setThemeImage();
		
		
		Log.i("tjc", "haha");
		
		//加载配置
		loadNumSetting();
		
	}
	
	 @Override  
	 public void onActivityResult(int requestCode, int resultCode, Intent data)  
	    {  
		 
		 	//Log.i("tjc","-->requestCode="+requestCode+"resultCode="+resultCode);
	        if (requestCode==111)  
	        {  
	            if (resultCode==112)  
	            {  
	                Bundle bundle=data.getExtras();  
	                String str=bundle.getString("cal"); 
	                
	                switch (current_axial) {
					case 1:
						{							
							//置数值
							dshow_two_xvalue = dshow_xvalue;
							
							x_setnum = Float.parseFloat(str);
							AxialManager.setAxialzsValue(current_axial, str);
							
							Toast.makeText(this, x_setnum+"", Toast.LENGTH_SHORT).show();  
						}
						break;
						
					case 2:
					{							
						//置数值
						dshow_two_yvalue = dshow_yvalue;
						
						y_setnum = Float.parseFloat(str);
						AxialManager.setAxialzsValue(current_axial, str);
						
						Toast.makeText(this, y_setnum+"", Toast.LENGTH_SHORT).show();  
					}
					break;
					
					case 3:
					{							
						//置数值
						dshow_two_zvalue = dshow_zvalue;
						
						z_setnum = Float.parseFloat(str);
						AxialManager.setAxialzsValue(current_axial, str);
						Toast.makeText(this, z_setnum+"", Toast.LENGTH_SHORT).show();  
					}
					break;

					default:
						break;
					}
	                
	            }  
	        } 
	        
	        super.onActivityResult(requestCode, resultCode, data);
	    }  
	 
	 public void showMsg(String msg){
		 Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();;
	 }
	 
	 /**
	  * 对XYZ轴清零
	  * @param index
	  */
	 public void ClearAxialToZero(int index){
		 
			switch (index) {
			case 1:
				if(msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbms") == false){
					//相对模式下,记录当前显示值
					String xstr = tv_x.getText().toString();
					Log.i("tjc", "xstr="+xstr);
					if(NumberValidationUtils.isRealNumber(xstr)){
						
						x_tmpvalu = Float.parseFloat(xstr);
						Log.i("tjc", "msg="+x_tmpvalu+"");
						
//						//clear to 0
//						x_setnum = 0.0f;
//						dshow_two_xvalue = 0.0f;
//						AxialManager.setAxialzsValue(1,"0");
						
						}	
					}else{	
						//绝对坐标下清0
						x_tmpvalu = 0.0f;
						tv_x.setText("0");
						StmUsbTask taskclearx = new StmUsbTask(mUsbDriver);
						taskclearx.execute(StmUsbCommond.CMD_CLEAR_X_AXIAL);
					}
				//clear to 0
				x_setnum = 0.0f;
				dshow_two_xvalue = 0.0f;
				AxialManager.setAxialzsValue(1,"0");
				break;
				
			case 2:
				if(msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbms") == false){
					//相对模式下,记录当前显示值
					String xstr = tv_y.getText().toString();
					Log.i("tjc", "ystr="+xstr);
					if(NumberValidationUtils.isRealNumber(xstr)){
						
						y_tmpvalu = Float.parseFloat(xstr);
						Log.i("tjc", "msg="+y_tmpvalu+"");
						
//						//clear to 0
//						y_setnum = 0.0f;
//						dshow_two_yvalue = 0.0f;
						
						}	
					}else{	
						//绝对坐标下清0
						y_tmpvalu = 0.0f;
						tv_y.setText("0");
						StmUsbTask taskcleary = new StmUsbTask(mUsbDriver);
						taskcleary.execute(StmUsbCommond.CMD_CLEAR_Y_AXIAL);
					}
				//clear to 0
				y_setnum = 0.0f;
				dshow_two_yvalue = 0.0f;
				AxialManager.setAxialzsValue(2,"0");
				break;
				
			case 3:
				if(msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbms") == false){
					//相对模式下,记录当前显示值
					String xstr = tv_z.getText().toString();
					Log.i("tjc", "zstr="+xstr);
					if(NumberValidationUtils.isRealNumber(xstr)){
						
						z_tmpvalu = Float.parseFloat(xstr);
						Log.i("tjc", "msg="+z_tmpvalu+"");
						
						//clear to 0
						//z_setnum = 0.0f;
						//dshow_two_zvalue = 0.0f;
						
						}	
					}else{	
						//绝对坐标下清0
						z_tmpvalu = 0.0f;
						tv_z.setText("0");
						StmUsbTask taskclearz = new StmUsbTask(mUsbDriver);
						taskclearz.execute(StmUsbCommond.CMD_CLEAR_Z_AXIAL);
					}	
				//clear to 0
				z_setnum = 0.0f;
				dshow_two_zvalue = 0.0f;
				AxialManager.setAxialzsValue(3,"0");
					break;
			case 4:
//				StmUsbTask taskclearc = new StmUsbTask(mUsbDriver);
//				taskclearc.execute(StmUsbCommond.CMD_CLEAR_C_AXIAL);
				break;

			default:
				break;
			} 
	 }
	 
	//改变模式图片
		public void changeBotomImg(int axial){
			
			Boolean bdwms = false;
			Boolean bzbms = false;
			Boolean bzbj = false;
			
			switch (axial) {
				case 1:
					bdwms = msharePreferenceUtil.loadBooleanSharedPreference("x_is_dwms");
					bzbms = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbms");
					bzbj = msharePreferenceUtil.loadBooleanSharedPreference("x_is_zbj");
					if(bdwms){
						mm_btn.setBackgroundResource(R.drawable.mm_green_btn);						
					}else{
						mm_btn.setBackgroundResource(R.drawable.inch_green_btn);						
					}
					
					if(bzbms){
						abs_btn.setBackgroundResource(R.drawable.abs_green_btn);					
					}else{
						abs_btn.setBackgroundResource(R.drawable.inc_green_btn);					
					}
					
					if(bzbj){
						rd_btn.setBackgroundResource(R.drawable.rd_green_btn);					
					}else{
						rd_btn.setBackgroundResource(R.drawable.rd_d_green_btn);						
					}
					
					break;
				case 2:
					bdwms = msharePreferenceUtil.loadBooleanSharedPreference("y_is_dwms");
					bzbms = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbms");
					bzbj = msharePreferenceUtil.loadBooleanSharedPreference("y_is_zbj");
					if(bdwms){
						mm_btn.setBackgroundResource(R.drawable.mm_green_btn);						
					}else{
						mm_btn.setBackgroundResource(R.drawable.inch_green_btn);						
					}
					
					if(bzbms){
						abs_btn.setBackgroundResource(R.drawable.abs_green_btn);					
					}else{
						abs_btn.setBackgroundResource(R.drawable.inc_green_btn);					
					}
					
					if(bzbj){
						rd_btn.setBackgroundResource(R.drawable.rd_green_btn);					
					}else{
						rd_btn.setBackgroundResource(R.drawable.rd_d_green_btn);						
					}
					break;
				case 3:
					bdwms = msharePreferenceUtil.loadBooleanSharedPreference("z_is_dwms");
					bzbms = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbms");
					bzbj = msharePreferenceUtil.loadBooleanSharedPreference("z_is_zbj");
					if(bdwms){
						mm_btn.setBackgroundResource(R.drawable.mm_green_btn);						
					}else{
						mm_btn.setBackgroundResource(R.drawable.inch_green_btn);						
					}
					
					if(bzbms){
						abs_btn.setBackgroundResource(R.drawable.abs_green_btn);					
					}else{
						abs_btn.setBackgroundResource(R.drawable.inc_green_btn);					
					}
					
					if(bzbj){
						rd_btn.setBackgroundResource(R.drawable.rd_green_btn);					
					}else{
						rd_btn.setBackgroundResource(R.drawable.rd_d_green_btn);						
					}
					break;

			default:
				break;
			}
						
		}
	 
	 
	 

//	@Override
//	public void afterDoTask(int taskid) {
//		// TODO Auto-generated method stub
//		
//		switch (taskid) {
//		case StmUsbCommond.CMD_INIT_USB:
//			
//			break;
//
//		default:
//			break;
//		}
//		
//	}
	 
	 


}
