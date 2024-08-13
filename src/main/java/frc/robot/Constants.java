package frc.robot;

public class Constants {

    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    // Motor IDs of Victor SPXs
    public static final int LEFT_FRONT_MOTOR_ID = 1;
    public static final boolean LEFT_FRONT_MOTOR_Inverted = false;
    
    public static final int LEFT_BACK_MOTOR_ID = 2;
    public static final boolean LEFT_BACK_MOTOR_Inverted = false;

    public static final int RIGHT_FRONT_MOTOR_ID = 3;
    public static final boolean RIGHT_FRONT_MOTOR_Inverted = true;

    public static final int RIGHT_BACK_MOTOR_ID = 4;
    public static final boolean RIGHT_BACK_MOTOR_Inverted = true;

    public static final int INTAKE_MOTOR_ID = 5;
    public static final boolean INTAKE_MOTOR_Inverted = false;

    public static final int SHOOTER_MOTOR_ID = 6;
    public static final boolean SHOOTER_MOTOR_Inverted = true;

    public static final double DEADBAND = 0.05;

    
    public static final double DRIVE_SPEED = 0.8;
    public static final double TURN_SPEED = 0.6;

    public static final double INTAKE_SPEED = 1.0;

    public static final double SHOOTER_INCREASE_VALUE = 0.01;

    public static class AutoConstants {
        public static final double kTimeToDriveForward = 2.0;
        public static final double kTimeToShoot = 1.0;
        public static final double kTimeToIntake = 4.0;
        
    }
    
    public static enum autoStatus {
        PRELOAD,
        GETNOTE,
        BACKSHOOT,
        LEAVE
    }
    
}
