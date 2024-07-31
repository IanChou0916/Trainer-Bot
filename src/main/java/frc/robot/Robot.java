// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.hal.SimDevice;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


import static frc.robot.Constants.*;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  
  private final WPI_VictorSPX m_leftFront = new WPI_VictorSPX(LEFT_FRONT_MOTOR_ID);
  private final WPI_VictorSPX m_leftBack = new WPI_VictorSPX(LEFT_BACK_MOTOR_ID);
  private final WPI_VictorSPX m_rightFront = new WPI_VictorSPX(RIGHT_FRONT_MOTOR_ID);
  private final WPI_VictorSPX m_rightBack = new WPI_VictorSPX(RIGHT_BACK_MOTOR_ID);
  
  private final WPI_VictorSPX m_intakeMotor = new WPI_VictorSPX(INTAKE_MOTOR_ID);
  private final WPI_VictorSPX m_shooterMotor = new WPI_VictorSPX(SHOOTER_MOTOR_ID);
  
  private final DifferentialDrive drive = new DifferentialDrive(m_leftFront, m_rightFront);
  
  

  private final XboxController driveController = new XboxController(DRIVER_CONTROLLER_PORT);
  private final XboxController operatorController = new XboxController(OPERATOR_CONTROLLER_PORT);
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  

  
  

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);

    m_leftFront.configFactoryDefault();
    m_leftBack.configFactoryDefault();
    m_rightFront.configFactoryDefault();
    m_rightBack.configFactoryDefault();

    m_leftFront.setInverted(false);
    m_rightFront.setInverted(true);
    
    m_leftBack.setInverted(false);
    m_rightBack.setInverted(true);
   
    
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    m_leftBack.follow(m_leftFront);
    m_rightBack.follow(m_rightFront);
    SmartDashboard.putNumber("leftF",m_leftFront.get());
    SmartDashboard.putNumber("rightF", m_rightFront.get());
    SmartDashboard.putNumber("leftB",m_leftBack.get());
    SmartDashboard.putNumber("rightB", m_rightBack.get());
    SmartDashboard.putNumber("shooter",m_shooterMotor.get());
    SmartDashboard.putNumber("intake", m_intakeMotor.get());
    SmartDashboard.updateValues();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    
    drive.arcadeDrive(-driveController.getLeftY()*DRIVE_SPEED,-driveController.getRightX()*TURN_SPEED);
    m_leftBack.set(m_leftFront.get());
    m_rightBack.set(m_rightFront.get());

    if(operatorController.getAButton()){
      m_intakeMotor.set(INTAKE_SPEED);
    }
    if(operatorController.getBButton()){
      m_intakeMotor.set(-INTAKE_SPEED);
    }
    if(operatorController.getXButton()){
      m_shooterMotor.set(SHOOTER_SPEED);
    }
    if(operatorController.getYButton()){
      m_shooterMotor.set(-SHOOTER_SPEED);
    }
  
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
