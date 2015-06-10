package com.cldxk.stm32.utils;

public class StmUsbCommond {
	
	//对四个通道的数据同时清零
	public static final int CMD_CLEAR_ALL_AXIAL = 0X00;
	
	//对第X通道数据清零
	public static final int CMD_CLEAR_X_AXIAL = 0X01;
	
	//对第Y通道数据清零
	public static final int CMD_CLEAR_Y_AXIAL = 0X02;
	
	//对第Z通道数据清零
	public static final int CMD_CLEAR_Z_AXIAL = 0X03;
	
	//对第C通道数据清零
	public static final int CMD_CLEAR_C_AXIAL = 0X04;
	
	//接收数据的应答命令
	public static final int CMD_CLEAR_ANOCK = 0X05;
	
	//寻X通道的零位数据
	public static final int CMD_SEARCH_XAXIAL_ZERO = 0X06;
	
	//已经接受X通道零位数据
	public static final int CMD_FIND_XAXIAL_ZERO = 0X07;
	
	//寻Y通道的零位数据
	public static final int CMD_SEARCH_YAXIAL_ZERO = 0X08;
	
	//已经接受Y通道零位数据
	public static final int CMD_FIND_YAXIAL_ZERO = 0X09;
	
	//寻Z通道的零位数据
	public static final int CMD_SEARCH_ZAXIAL_ZERO = 0X0A;
	
	//已经接受Z通道零位数据
	public static final int CMD_FIND_ZAXIAL_ZERO = 0X0B;
	
	//寻C通道的零位数据
	public static final int CMD_SEARCH_CAXIAL_ZERO = 0X0C;
	
	//已经接受C通道零位数据
	public static final int CMD_FIND_CAXIAL_ZERO = 0X0D;
	
	//包头
	public static final int CMD_PACKAGE_HEADER = 0X7E;
	
	//包尾
	public static final int CMD_PACKAGE_FOOTER = 0XE7;
	
	/**
	 * 任务命令
	 */
	//任务X轴清零
//	public static final int CMD_TASK_CLEAR_XAXIAL = 0XA1;
//	
//	//任务Y轴清零
//	public static final int CMD_TASK_CLEAR_YAXIAL = 0XA2;
//	
//	//任务Z轴清零
//	public static final int CMD_TASK_CLEAR_ZAXIAL = 0XA3;
//	
//	//任务C轴清零
//	public static final int CMD_TASK_CLEAR_CAXIAL = 0XA4;
	
	//USB初始化配置ID
	public static final int CMD_INIT_USB = 0XA1;
	
	/**
	 * USB端点数据
	 */
	public static final int EP1_IN = 0x81;  
    public static final int EP1_OUT = 0x01;
    public static final int EP2_IN = 0x82;  
    public static final int EP2_OUT = 0x02;
    
	public static final int EP3_IN = 0x83;  
    public static final int EP3_OUT = 0x03;
    public static final int EP4_IN = 0x84;  
    public static final int EP4_OUT = 0x04;
    
	public static final int EP5_IN = 0x85;  
    public static final int EP5_OUT = 0x05;
    public static final int EP6_IN = 0x86;  
    public static final int EP6_OUT = 0x06;
    
	public static final int EP7_IN = 0x87;  
    public static final int EP7_OUT = 0x07;
	
	
		

}
