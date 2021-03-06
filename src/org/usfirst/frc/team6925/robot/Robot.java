/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6925.robot;

import org.usfirst.frc.team6925.robot.components.basket;
import org.usfirst.frc.team6925.robot.components.intake;
import org.usfirst.frc.team6925.robot.components.drivetrain;
import org.usfirst.frc.team6925.robot.autonomous.autonomous;
import org.usfirst.frc.team6925.robot.OI;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	public static drivetrain driveTrain;
	public static basket basket;
	public static intake intake;
	public static OI input;
	public static autonomous auto;
	public static UsbCamera frontCamera;
	public static UsbCamera rearCamera;
    private static final String L_fullSpeed = "FULL SPEED AHEAD";
    private static final String Left = "Left";
    private static final String Right = "Right";
    private static final String Middle = "Middle";
    private static final String None = "None";
    private static final String Test = "Test";
	private String m_autoSelected;
	public static String gameData;
	
	private SendableChooser<String> m_chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		driveTrain = new drivetrain();
		basket = new basket();	
		intake = new intake();
		input = new OI();
		rearCamera = CameraServer.getInstance().startAutomaticCapture(0);
		frontCamera = CameraServer.getInstance().startAutomaticCapture(1);
		rearCamera.setFPS(15);
		rearCamera.setResolution(640, 640);
		frontCamera.setFPS(15);
		frontCamera.setResolution(640, 640);
	   	m_chooser.addObject("FULL SPEED AHEAD", L_fullSpeed);
	   	m_chooser.addObject("Left", Left);
	   	m_chooser.addObject("Middle", Middle);
	   	m_chooser.addObject("Right", Right);
	   	m_chooser.addObject("Test", Test);
	   	m_chooser.addDefault("None", None);
		SmartDashboard.putData("Auto choices", m_chooser);
		System.out.println("robot init");
	}

	@Override
	public void autonomousInit() {
		auto = new autonomous();
		m_autoSelected = m_chooser.getSelected();
	   	gameData = DriverStation.getInstance().getGameSpecificMessage();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
		System.out.println("auto init ran");
	}
	@Override
	public void autonomousPeriodic() {
		System.out.println("Inside of the auto");
		 switch(m_autoSelected) {
			 case Left:
				 if (gameData.charAt(0) == 'L') {
					 Robot.auto.run("left");
					 //Left placement and left switch
				 }
				 else if(gameData.charAt(0) == 'R') {
					 Robot.auto.run("left","right");
					 //Left placement and right switch
				 }
				 break;
			 case Middle:
				 if (gameData.charAt(0) == 'L') {
					 Robot.auto.run("middle", "left");
					 //Middle placement and left switch
				 }
				 else if(gameData.charAt(0) == 'R') {
					 //Middle placement and right switch
					 Robot.auto.run("middle","right");
				 }
			 	 break;
			 case Right:
				 if (gameData.charAt(0) == 'L') {
					 Robot.auto.run("right", "left");
					 //Right placement and left switch
				 }
				 else if(gameData.charAt(0) == 'R') {
					 Robot.auto.run("right");
					 //Right placement and right switch
				 }
				 break;
			 case L_fullSpeed:
				 Robot.auto.run("fullspeed");
				 break;
			 case Test:
				 System.out.println("Inside test");
				 Robot.basket.setBasket(.5);
				 Timer.delay(2);
				 Robot.basket.setBasket(-0.5);
				 Timer.delay(2);
				 Robot.basket.setBasket(0);
				 break;
			 case None:
				 break;
		 }
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		if(Robot.input.reverseControls.get()){
			Robot.driveTrain.leftMotorGroup.setInverted(false);
			Robot.driveTrain.rightMotorGroup.setInverted(true);
		}
		else{
			Robot.driveTrain.leftMotorGroup.setInverted(true);
			Robot.driveTrain.rightMotorGroup.setInverted(false);
		}
		System.out.println("Tele started");
		double inputSpeedLeft = Robot.input.driveJoystick.getRawAxis(1);
		double inputSpeedRight = -Robot.input.driveJoystick.getRawAxis(3);
		Robot.driveTrain.m_drive.tankDrive(inputSpeedLeft, inputSpeedRight);
		
    	//Gets the value of the button that controls the basket.
		if (Robot.input.getBasket.get() && !Robot.input.getBasketReload.get()){
			Robot.basket.setBasket(.9);
		}else if (Robot.input.getBasketReload.get()){
			Robot.basket.setBasket(-.8);
		}else{
			Robot.basket.setBasket(0);
		}
		
		if (Robot.input.getIntake.get()) {
			Robot.intake.setIntake(.8);
		}else if (Robot.input.getOuttake.get()) {
			Robot.intake.setIntake(-.8);
		}else {
			Robot.intake.setIntake(0);
		}
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
