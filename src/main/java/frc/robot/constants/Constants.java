package frc.robot.constants;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.MountPoseConfigs;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.swerve.SwerveDrivetrainConstants;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.DELib26.motors.PIDControllerContainer;
import frc.DELib26.subsystems.swerve.swerveUtil.COTSTalonFXSwerveConstants.SDS.MK4i;
import frc.DELib26.subsystems.swerve.swerveUtil.SwerveConfig;
import frc.DELib26.subsystems.vision.visionUtil.VisionSubsystemConfiguration;
import frc.DELib26.util.RunMode;
import frc.DELib26.util.vision.CameraConfiguration;
import frc.DELib26.util.vision.CameraModel;

import java.util.Optional;

public final class Constants {

	public static final class AutonomousConstants {
		private AutonomousConstants() {}

		public static final double MAX_START_TRANS_ERROR_METERS = 0.8;
		public static final double MAX_START_HEADING_ERROR_DEGREES = 180;
		public static final double AUTONOMOUS_VISION_LOOKBACK_TIME_SECONDS = 0.5;
	}

	public static final class RobotConstants {
		private RobotConstants() {}

		public static enum LoggingMode {
			NONE,
			BASIC,
			EXTENDED
		}

		public static final LoggingMode SIM_MODE = LoggingMode.EXTENDED;
		public static final LoggingMode REAL_MODE = LoggingMode.EXTENDED;

		public static final int TELEMETRY_INTERVAL_CYCLES = 4;

		public static final int USB_CAMERA_FPS = 15;

	}

	public static final class SwerveConstants {
		private SwerveConstants() {}

		public static final SwerveConfig SWERVE_CONFIG = new RobotSwerveConfig();

		private static final class RobotSwerveConfig extends SwerveConfig {
			public RobotSwerveConfig() {
				MAC_ADDRESS = "00-80-2F-39-64-5B";

				CANBUS_NAME = "Canivore";

				MODULE_HARDWARE_CONSTANTS = MK4i.KrakenX60(MK4i.DriveRatios.L2);
				MODULE_HARDWARE_CONSTANTS.setWheelDiameter(Units.Inches.of(3.64673614));
				// Ports and IDs
				GYRO_ID = 44;

				// Swerve Modules
				FRONT_LEFT_DRIVE_MOTOR_ID = 10;
				FRONT_LEFT_STEER_MOTOR_ID = 11;
				FRONT_LEFT_STEER_ENCODER_ID = 12;

				FRONT_RIGHT_DRIVE_MOTOR_ID = 20;
				FRONT_RIGHT_STEER_MOTOR_ID = 21;
				FRONT_RIGHT_STEER_ENCODER_ID = 22;

				BACK_LEFT_DRIVE_MOTOR_ID = 30;
				BACK_LEFT_STEER_MOTOR_ID = 31;
				BACK_LEFT_STEER_ENCODER_ID = 32;

				BACK_RIGHT_DRIVE_MOTOR_ID = 40;
				BACK_RIGHT_STEER_MOTOR_ID = 41;
				BACK_RIGHT_STEER_ENCODER_ID = 42;

				WHEELBASE_LENGTH = Units.Centimeters.of(53.2);
				WHEEL_TRACK_WIDTH = Units.Centimeters.of(53.2);

				ROBOT_MASS = Units.Kilograms.of(49);
				BUMPER_WIDTH = Units.Centimeters.of(68);
				BUMPER_LENGTH = Units.Centimeters.of(68);
				WHEEL_COEFFICIENT_OF_FRICTION = Units.Value.of(0.24);
				// 1/12 * BUMPER_MASS * (BUMPER_WIDTH^2 + BUMPER_LENGTH^2)
				// 1/12 * ROBOT_MASS * (robot_width^2 + length^2)
				//
				ROBOT_MOMENT_OF_INERTIA = Units.KilogramSquareMeters.of(3.78);
				SIM_DRIVE_MOMENT_OF_INERTIA = Units.KilogramSquareMeters.of(0.3);
				DRIVE_SLOT0_CONFIGS = new Slot0Configs()
						.withKP(0.07)
						.withKS(0.04)
						.withKV(0.12)
						.withKA(0.06);

				STEER_SLOT0_CONFIGS = new Slot0Configs().withKP(80);

				TRANSLATION_CONTROLLER = new PIDControllerContainer().withKP(1.9).withKD(0.2);
				ROTATION_CONTROLLER = new PIDControllerContainer().withKP(7);

				PATH_PLANNER_TRANSLATION_CONTROLLER = new PIDControllerContainer().withKP(5).withKD(0.1);
				PATH_PLANNER_ROTATION_CONTROLLER = new PIDControllerContainer().withKP(7);

				DRIVE_STATOR_CURRENT_LIMIT = Units.Amps.of(40.0);
				STEER_STATOR_CURRENT_LIMIT = Units.Amps.of(30.0);

				// tuned by deiving as fast as possible in a straight line and recording the max
				// speed reached
				MAX_LINEAR_SPEED = Units.MetersPerSecond.of(4.5);
				INITIAL_MAX_VELOCITY_FOR_DRIVE_TO_POINT = MAX_LINEAR_SPEED;
				MAX_LINEAR_ACCELERATION = Units.MetersPerSecondPerSecond.of(6);
				MAX_ANGULAR_ACCELERATION = Units.RadiansPerSecondPerSecond.of(20);

				// TO R OF BOT
				FRONT_LEFT_STEER_OFFSET = Units.Rotations.of(-1 * -0.473145);
				FRONT_RIGHT_STEER_OFFSET = Units.Rotations.of(-1 * -0.124023);
				BACK_LEFT_STEER_OFFSET = Units.Rotations.of(-1 * 0.251953);
				BACK_RIGHT_STEER_OFFSET = Units.Rotations.of(-1 * 0.381836);

				SKEW_COMPENSATION_SCALAR = Units.Value.of(0);

				GYRO_MOUNTING_ANGLE = Units.Degrees.of(90);

				// SysId configs
				TRANSLATION_SYS_ID_CONFIG = new SysIdRoutine.Config(
						null,
						Units.Volts.of(6),
						Units.Seconds.of(3),
						null);
				ROTATION_SYS_ID_CONFIG = new SysIdRoutine.Config(

						Units.Volts.of(Math.PI / 6).per(Units.Second),
						Units.Volts.of(Math.PI),
						Units.Seconds.of(5),
						null);

				STEER_SYS_ID_CONFIG = new SysIdRoutine.Config(
						null,
						Units.Volts.of(6),
						Units.Seconds.of(3),
						null);

				SWERVE_MODULE_CONSTANTS = this.getDefaultSwerveModuleConstantsArray();
				if (!RunMode.isSim()) {
					SWERVE_MODULE_CONSTANTS[1].DriveMotorInverted = true;
					SWERVE_MODULE_CONSTANTS[3].DriveMotorInverted = true;
				}

				SWERVE_DRIVETRAIN_CONSTANTS = new SwerveDrivetrainConstants()
						.withCANBusName(CANBUS_NAME)
						.withPigeon2Id(GYRO_ID)
						.withPigeon2Configs(
								new Pigeon2Configuration()
										.withMountPose(
												new MountPoseConfigs()
														.withMountPoseYaw(GYRO_MOUNTING_ANGLE)));
			}

			@Override
			protected TalonFXConfiguration getDefaultDriveTalonFXConfig() {
				return new TalonFXConfiguration()
						.withCurrentLimits(new CurrentLimitsConfigs()
								.withSupplyCurrentLimitEnable(true)
								.withStatorCurrentLimitEnable(true)
								.withSupplyCurrentLimit(30.0)
								.withStatorCurrentLimit(DRIVE_STATOR_CURRENT_LIMIT))
						.withMotorOutput(new MotorOutputConfigs()
								.withNeutralMode(NeutralModeValue.Brake));
			}

			@Override
			protected TalonFXConfiguration getDefaultSteerTalonFXConfig() {
				return new TalonFXConfiguration()
						.withCurrentLimits(new CurrentLimitsConfigs()
								.withSupplyCurrentLimitEnable(true)
								.withStatorCurrentLimitEnable(true)
								.withSupplyCurrentLimit(30.0)
								.withStatorCurrentLimit(STEER_STATOR_CURRENT_LIMIT))
						.withMotorOutput(new MotorOutputConfigs()
								.withNeutralMode(NeutralModeValue.Brake));
			}
		}
	}

	public static final class VisionConstants {
		private VisionConstants() {}

		public static final VisionSubsystemConfiguration VISION_CONFIG = new RobotVisionConfig();
		public static final double VISION_POSE_ESTIMATE_MAX_AGE_SECONDS = 0.5;

		private static final class RobotVisionConfig extends VisionSubsystemConfiguration {
			public RobotVisionConfig() {
				super.cameraConfigurations = new CameraConfiguration[] {
					new CameraConfiguration(
							"limelight-front",
							CameraModel.LIMELIGHT_4,
							0.25,
							0,
							0.5,
							0.0,
							17.96,
							0.0
						),
				};
				this.filteredAprilTagDataForGyro = Optional.empty();
				this.aprilTagFieldLayout = FieldConstants.FIELD_LAYOUT;
			}
		}
	}

}