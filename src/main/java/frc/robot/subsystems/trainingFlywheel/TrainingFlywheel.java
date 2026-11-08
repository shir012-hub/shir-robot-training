package frc.robot.subsystems.trainingFlywheel;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import frc.DELib26.motors.MasterMotorConstants;
import frc.DELib26.subsystems.motorSubsystems.motorBase.MotorSubsystemConfiguration;
import frc.DELib26.subsystems.motorSubsystems.motorBase.MotorSubsystemTalon;
import frc.DELib26.subsystems.motorSubsystems.motorBase.mechanismSim.DCMotorMechanismSim.DCMotorMechanismSimConfig;

public final class TrainingFlywheel extends MotorSubsystemTalon {

	public TrainingFlywheel() {
		super(createConfiguration());
	}

	private static MotorSubsystemConfiguration createConfiguration() {
		return new MotorSubsystemConfiguration()
				.withName("TrainingFlywheel")
				.withMaster(
					new MasterMotorConstants(50, "rio", true, new TalonFXConfiguration())
				).withMechanismType(MotorSubsystemConfiguration.MechanismType.ANGULAR)
				.withMechanismSimConfig(
					new DCMotorMechanismSimConfig(0.002, 0.1)
				);
	}
}