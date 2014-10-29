package rl.linetracer;



import lejos.hardware.Button;
import lejos.utility.Delay;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.*;
import lejos.hardware.sensor.*;
import lejos.robotics.Color;

public class MachineControl
{
	public static final int LED_PATTERN_OFF                  =0;
	public static final int LED_PATTERN_STATIC_GREEN         =1;
	public static final int LED_PATTERN_STATIC_RED           =2;
	public static final int LED_PATTERN_STATIC_YELLOW        =3;
	public static final int LED_PATTERN_NORMALBLINKING_GREEN =4;
	public static final int LED_PATTERN_NORMALBLINKING_RED   =5;
	public static final int LED_PATTERN_NORMALBLINKING_YELLOW=6;
	public static final int LED_PATTERN_FASTBLINKING_GREEN   =7;
	public static final int LED_PATTERN_FASTBLINKING_RED     =8;
	public static final int LED_PATTERN_FASTBLINKING_YELLOW  =9;
	private NXTRegulatedMotor LMotor;
	private NXTRegulatedMotor RMotor;
	private EV3ColorSensor LineSensor;
	private SensorMode LineSensorMode;
	private EV3ColorSensor SubSensor;
	
	public MachineControl()
	{
		//portBに接続のモータをLMotorに関連付ける
		LMotor=Motor.B;
		//portCに接続のモータをRMotorに関連付ける
		RMotor=Motor.C;
		//速度を0に初期化
		LMotor.setSpeed(0);
		RMotor.setSpeed(0);
		
		
		//LineSensorの初期化
		LineSensor=new EV3ColorSensor(LocalEV3.get().getPort("S3"));
		//Redモード(反射光モード)に設定
		LineSensorMode=LineSensor.getRedMode();
		
		//SubSensorの初期化
		SubSensor=new EV3ColorSensor(LocalEV3.get().getPort("S4"));
	}
	//指定したスピードで前進
	public void GoForward(int lspeed, int rspeed)
	{
		LMotor.setSpeed(lspeed);
		RMotor.setSpeed(rspeed);
		LMotor.forward();
		RMotor.forward();
	}
	//停止する
	public void Stop()
	{
		LMotor.stop();
		RMotor.stop();
		LMotor.flt();
		RMotor.flt();
		
	}
	//SubSensorから色を検知する
	public int GetColor()
	{
		return SubSensor.getColorID();
	}
	public float GetReflection()
	{
		float r[]=new float[1];
		LineSensorMode.fetchSample(r, 0);
		return r[0];
	}
	//後退する
	public void GoBack(int lspeed, int rspeed)
	{
		LMotor.setSpeed(lspeed);
		RMotor.setSpeed(rspeed);
		LMotor.backward();
		RMotor.backward();
	}
	//LineSensorで黒線を検知する
	public boolean OnBlackLine()
	{
		//return this.GetReflection()<0.2;
		return Color.BLACK==LineSensor.getColorID();
	}
	public void OnLED(int pattern)
	{
		lejos.hardware.Button.LEDPattern(pattern);
	}
	public boolean IsEscapeButtonDown()
	{
		return Button.getButtons()==Button.ID_ESCAPE;
	}
	public void Delay(int period)
	{
		Delay.msDelay(period);
		
	}
}
