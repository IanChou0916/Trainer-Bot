// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


import static frc.robot.Constants.*;
import static frc.robot.Constants.AutoConstants.*;

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
  private final Timer timer = new Timer();

  private boolean shooterhasOpened = false;
  private boolean intakehasOpened = false;

  private double shooterSpeed = 1.0;
  private double intakeSpeed = INTAKE_SPEED;
  private double autoDriveSpeed = 0.0;
  
  
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);

    resetMotors();
    /*
     * m_leftFront.configFactoryDefault();
      m_leftBack.configFactoryDefault();
      m_rightFront.configFactoryDefault();
      m_rightBack.configFactoryDefault();
      m_intakeMotor.configFactoryDefault();
      m_shooterMotor.configFactoryDefault();
     */

    m_leftFront.setInverted(LEFT_FRONT_MOTOR_Inverted);
    
    
    m_rightFront.setInverted(RIGHT_FRONT_MOTOR_Inverted);
    
    m_leftBack.setInverted(LEFT_BACK_MOTOR_Inverted);
    m_rightBack.setInverted(RIGHT_BACK_MOTOR_Inverted);

    m_intakeMotor.setInverted(INTAKE_MOTOR_Inverted);
    m_shooterMotor.setInverted(SHOOTER_MOTOR_Inverted);

    drive.setDeadband(DEADBAND);
    
   
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
    SmartDashboard.putNumber("speed ", shooterSpeed);
    SmartDashboard.putBoolean("shooter Enabled " , shooterhasOpened);
    SmartDashboard.putBoolean("intake Enabled " , intakehasOpened);
  
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
    timer.reset();
    timer.start();
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
        if((timer.get() < kTimeToDriveForward) || timerReleased(7.0, 2.0) ) autoDriveSpeed = 0.5;
        else if(timer.get()>9.0 && timer.get()<11.0) autoDriveSpeed = -0.5;
        else if(timer.get()>13.5) autoDriveSpeed = 0.66;
        else autoDriveSpeed = 0.0;
        drive.arcadeDrive(autoDriveSpeed, 0.0);
        m_shooterMotor.set(((timer.get() > kTimeToShoot && timer.get()<6.0) ||(timer.get() > 9.0 && timer.get()<13.0)) ? 1.0 : 0.0);
        m_intakeMotor.set(((timer.get() > kTimeToIntake && timer.get()<6.0)|| (timer.get()>7.0 && timer.get()<12.0)) ? 1.0 : 0.0);
        
        System.out.println("running");
        
        
        //new WaitCommand(2.0);
        //drive.arcadeDrive(0.0, 0.0);
        //System.out.println("compelete");
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

    if(operatorController.getLeftBumper() && shooterSpeed >= 0.0){
     shooterSpeed-=SHOOTER_INCREASE_VALUE;
     shooterSpeed = Math.max(shooterSpeed, 0.0);
      
    }
    if(operatorController.getRightBumper() && shooterSpeed <= 1.0){
      shooterSpeed+=SHOOTER_INCREASE_VALUE;
      shooterSpeed = Math.min(shooterSpeed, 1.0);
    }
    
    if(operatorController.getYButton()){
      //timer.start();
      if(operatorController.getYButtonReleased()){
        shooterhasOpened = !shooterhasOpened;
      }
    }
     m_shooterMotor.set(shooterhasOpened ? shooterSpeed : 0.0 );


    if(operatorController.getPOV() == 0){
      intakeSpeed = INTAKE_SPEED;

    }

    else if(operatorController.getPOV() == 180){
      intakeSpeed = -INTAKE_SPEED;
    }
    
    if(operatorController.getBButton()){
      //timer.start();
      if(operatorController.getBButtonReleased()){
        intakehasOpened = !intakehasOpened;
      }
    }

    m_intakeMotor.set(intakehasOpened ? intakeSpeed : 0.0 );
    

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

  private void resetMotors(){
    m_leftFront.configFactoryDefault();
    m_leftBack.configFactoryDefault();
    m_rightFront.configFactoryDefault();
    m_rightBack.configFactoryDefault();
    m_intakeMotor.configFactoryDefault();
    m_shooterMotor.configFactoryDefault();
  }
  private boolean timerReleased( double startTime, double releaseTime){
    return (timer.get()>startTime && timer.get()<(startTime+releaseTime));
  } // function to check autonomous period. 
}
