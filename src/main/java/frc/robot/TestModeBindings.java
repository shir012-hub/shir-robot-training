package frc.robot;

import frc.DELib26.bindings.Bindings;
import frc.DELib26.bindings.ControllerDiagnosticsBindings;
import frc.DELib26.subsystems.swerve.swerveBindings.SwerveGeneralBindings;
import frc.DELib26.subsystems.swerve.swerveBindings.SwervePidCalibrationsBindings;
import frc.robot.constants.Constants;

import java.util.function.Function;
public enum TestModeBindings {
	NONE(new Bindings() {
	}),

	CONTROLLER_DIAGNOSTICS((RobotContainer container) -> new ControllerDiagnosticsBindings(container.driverController)),

	SWERVE_PID_CALIBRATIONS((RobotContainer container) -> new SwervePidCalibrationsBindings(
			container.swerveSubsystem,
			Constants.SwerveConstants.SWERVE_CONFIG
		)
	),

	SWERVE_GENERAL((RobotContainer container) -> new SwerveGeneralBindings(
			container.swerveSubsystem,
			container.robotState2026
		)
	);

	private Bindings bindings;

	private TestModeBindings(Function<RobotContainer, Bindings> bindingsFunction) {
		this.bindings = bindingsFunction.apply(RobotContainer.getInstance());
	}

	private TestModeBindings(Bindings bindings) {
		this.bindings = bindings;
	}

	public boolean isBindingsAvailable() {
		return this.bindings != null;
	}

	public Bindings getBindings() {
		if (!this.isBindingsAvailable()) {
			throw new IllegalStateException("Bindings not available for this TestModeBindings");
		}
		return this.bindings;
	}
}