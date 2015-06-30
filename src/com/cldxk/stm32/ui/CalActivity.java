package com.cldxk.stm32.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cldxk.stm32usb.ui.base.EBaseActivity;
import com.ukmterm.stm32usb.R;

public class CalActivity extends EBaseActivity implements OnClickListener{
	
	private Button cal_back_btn = null;
	private Button cal_zf_btn = null;
	private Button cal_clear_btn = null;
	private Button btn7 = null;
	private Button btn8 = null;
	private Button btn9 = null;
	private Button btn4 = null;
	private Button btn5 = null;
	private Button btn6 = null;
	private Button cal_confirm = null;
	private Button btn1 = null;
	private Button btn2 = null;
	private Button btn3 = null;
	private Button cal_cancel = null;
	private Button btn0 = null;
	private Button btnPoint = null;
	
	private TextView cal_tv = null;
	
	private StringBuilder strbuild = null;
	
	private Boolean is_first_zero = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		is_first_zero = false;
		
		initMyView();
		strbuild = new StringBuilder("");
		
	}

	@Override
	public int setLayout() {
		// TODO Auto-generated method stub
		return R.layout.calculate_green_layout;
	}
	
	public void initMyView(){
		
		cal_tv = this.findTextViewById(R.id.cal_tv);
		
		cal_back_btn = this.findButtonById(R.id.cal_back_btn);
		cal_back_btn.setOnClickListener(this);
		
		cal_zf_btn = this.findButtonById(R.id.cal_zf_btn);
		cal_zf_btn.setOnClickListener(this);
		
		cal_clear_btn = this.findButtonById(R.id.cal_clear_btn);
		cal_clear_btn.setOnClickListener(this);
		
		btn7 = this.findButtonById(R.id.btn7);
		btn7.setOnClickListener(this);
		
		btn8 = this.findButtonById(R.id.btn8);
		btn8.setOnClickListener(this);
		
		btn9 = this.findButtonById(R.id.btn9);	
		btn9.setOnClickListener(this);
		
		btn4 = this.findButtonById(R.id.btn4);
		btn4.setOnClickListener(this);
		
		btn5 = this.findButtonById(R.id.btn5);
		btn5.setOnClickListener(this);
		
		btn6 = this.findButtonById(R.id.btn6);
		btn6.setOnClickListener(this);
		
		cal_confirm = this.findButtonById(R.id.cal_confirm);
		cal_confirm.setOnClickListener(this);
		
		btn1 = this.findButtonById(R.id.btn1);
		btn1.setOnClickListener(this);
		
		btn2 = this.findButtonById(R.id.btn2);
		btn2.setOnClickListener(this);
		
		btn3 = this.findButtonById(R.id.btn3);
		btn3.setOnClickListener(this);
		
		cal_cancel = this.findButtonById(R.id.cal_cancel);
		cal_cancel.setOnClickListener(this);
		
		btn0 = this.findButtonById(R.id.btn0);
		btn0.setOnClickListener(this);
		
		btnPoint = this.findButtonById(R.id.btnPoint);
		btnPoint.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
				
		switch (v.getId()) {
		case R.id.cal_back_btn:
			if(strbuild.length()>0){
				strbuild.deleteCharAt(strbuild.length()-1);
			}
			break;
			
		case R.id.cal_zf_btn:
			if(strbuild.length() == 0){
				return;
			}else{
				if(strbuild.length() == 1 && strbuild.toString().equals("0")){
					return;
				}else{					
						if(strbuild.charAt(0) == '-'){
							strbuild.deleteCharAt(0);
						}else{
							if(strbuild.length()>0){
								
								strbuild.insert(0, "-");
							}
						}
				}
			}
			break;
			
		case R.id.cal_clear_btn:
			strbuild.setLength(0);
			break;
			
		case R.id.btn7:
			strbuild.append("7");
			break;
			
		case R.id.btn8:
			strbuild.append("8");
			break;
			
		case R.id.btn9:
			strbuild.append("9");
			break;
			
		case R.id.btn6:
			strbuild.append("6");
			break;
			
		case R.id.btn5:
			strbuild.append("5");
			break;
			
		case R.id.btn4:
			strbuild.append("4");
			break;
			
		case R.id.btn3:
			strbuild.append("3");
			break;
			
		case R.id.btn2:
			strbuild.append("2");
			break;
			
		case R.id.btn1:
			strbuild.append("1");
			break;
			
		case R.id.btn0:
			if(strbuild.length() == 0){
				strbuild.append("0");
				cal_tv.setText(strbuild.toString());
				return;
			}
			if(strbuild.length() == 1){				
				if(strbuild.toString().contains("0")){
					
					return ;
				}
			}
			strbuild.append("0");
			break;
			
		case R.id.btnPoint:
			if(strbuild.length() == 0){
				return;
			}
			if(!strbuild.toString().contains(".")){				
				strbuild.append(".");
			}
			break;
			
		case R.id.cal_confirm:			
			Intent intent=new Intent();  
            intent.putExtra("cal", strbuild.toString());  
            setResult(112, intent);  
            finish();
			break;
			
		case R.id.cal_cancel:
			finish();
			break;

		default:
			break;
		}
		
		//更新UI
		if(strbuild.length()>0){
			
			if(strbuild.charAt(0) == '0'){
				//>=1
				if(strbuild.length()>1){
					if(strbuild.charAt(1)!= '.'){
						strbuild.deleteCharAt(strbuild.length()-1);
						return;
					}
				}
			}
		}
		cal_tv.setText(strbuild.toString());
		
	}

}
