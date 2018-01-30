/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6925.robot;

import org.usfirst.frc.team6925.robot.commands.DriveWithJoystick;
import org.usfirst.frc.team6925.robot.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot 
{
	
	public Joystick controller;
	public int port;
	public boolean triggerPressed = false;
	public static OI oi;
	//Subsystem
	public static DriveTrain drivetrain = new DriveTrain();
	public static String gameData;
	private static final String kRightDefaultAuto = "Right Default";
	private static final String kRightCustomAuto = "Right Auto";
	private static final String kRightCustomTest = "Test Right Auto";
	private static final String kLeftDefaultAuto = "Left Default";
	private static final String kLeftCustomAuto = "Left Auto";
	private static final String kLeftCustomTest = "Test Right Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	public VictorSP m_frontLeftMotor = new VictorSP(1);
	public VictorSP m_frontRightMotor = new VictorSP(2);
	
	public VictorSP m_rearLeftMotor = new VictorSP(3);
	public VictorSP m_rearRightMotor = new VictorSP(4);
	SpeedControllerGroup m_Left = new SpeedControllerGroup(m_frontLeftMotor, m_rearLeftMotor);
	SpeedControllerGroup m_Right = new SpeedControllerGroup(m_rearRightMotor, m_rearRightMotor);
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() 
	{
		
		m_chooser.addDefault("Default Right Auto", kRightDefaultAuto);
		m_chooser.addObject("Right Auto", kRightCustomAuto);
		m_chooser.addObject("Test Right Auto", kRightCustomTest);
		m_chooser.addDefault("Default Right Auto", kLeftDefaultAuto);
		m_chooser.addObject("Left custom Auto", kLeftCustomAuto);
		m_chooser.addObject("Left Custom Test", kLeftCustomTest);
		

		SmartDashboard.putData("Auto choices", m_chooser);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 * @return 
	 */
	
	@Override
	public void autonomousInit() 
	{

		String GameData;
		GameData = DriverStation.getInstance().getGameSpecificMessage();
		gameData = GameData;	

		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}
	public String getGameData()
	{
		return gameData;
	}
	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() 
	{

		if (gameData.charAt(0)== 'L')
		{
			switch(m_autoSelected)
			{
			case kLeftCustomAuto:
				break; 
			case kLeftCustomTest: 
				break; 
			case kLeftDefaultAuto:

				break;
			}
		}
		
		else 
		{
			switch (m_autoSelected) 
			{
				case kRightCustomAuto:
					// Put custom auto code here
					break;
				case kRightCustomTest: 
					//place test code
					break; 
				case kRightDefaultAuto:
				default:
					// Put default auto code here
					break;
			}
		}
		
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		
		public void Joystick(int port)
		{
			this.port = port;
			this.controller = new Joystick(port);
			double throttle = Math.abs(controller.getThrottle()-1)/1.5;
			double speed = controller.getY();
			double power = (Math.sin(Math.PI*(speed - 0.5)) + 1 ) /2; 
	 
			if(speed < 0)
			{
				speed = speed*-1; 
			}	
			double turnPower = ((controller.getTwist()));
			double turn = (Math.sin(Math.PI*(turnPower - 0.5)) +1) /2;;
		
			if(controller.getY() > 0 ) 
			{
				m_frontLeftMotor.set((throttle * (power - turn)));
				m_rearLeftMotor.set((throttle * (power - turn)));
				m_frontRightMotor.set((throttle * (power + turn)));
				m_rearRightMotor.set((throttle * (power + turn)));
			}
			else
			{
				m_frontLeftMotor.set((throttle * (power + turn)));
				m_rearLeftMotor.set((throttle * (power + turn)));
				m_frontRightMotor.set((throttle * (power - turn)));
				m_rearRightMotor.set((throttle * (power - turn)));
			}
		
		}
	}
		

		

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() 
	{
		
	}
}
