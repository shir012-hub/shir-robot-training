package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.DELib26.util.RunMode;
import frc.robot.autos.AutoBindings;
import frc.robot.sim.SimBindings;

import java.util.Optional;

public class BindingsManager {
	private static BindingsManager instance = new BindingsManager();

	public static BindingsManager getInstance() {
		return instance;
	}

	// test mode chooser
	private SendableChooser<TestModeBindings> chooser;
	public final Optional<SimBindings> simBindings;
	public final AutoBindings autoBindings;
	public final MatchBindings matchBindings;

	public BindingsManager() {
		if (RunMode.isSim()) {
			this.simBindings = Optional.of(new SimBindings());
		} else {
			this.simBindings = Optional.empty();
		}

		this.matchBindings = new MatchBindings();

		this.autoBindings = new AutoBindings();

		this.chooser = new SendableChooser<>();
		for (TestModeBindings mode : TestModeBindings.values()) {
			if (mode.isBindingsAvailable()) {
				chooser.addOption(mode.name(), mode);
			}
		}
		chooser.setDefaultOption(TestModeBindings.NONE.name(), TestModeBindings.NONE);
	}

	public void configureTestMode() {
		SmartDashboard.putData("Test Mode", chooser);
	}

	public void testModeInit() {
		RobotContainer.getInstance().driverController.assignControllerType();
		this.chooser.getSelected().getBindings().bind();
	}

	public void testModePeriodic() {
		this.chooser.getSelected().getBindings().periodic();
	}

	public void bindAutos() {
		this.autoBindings.bind();
	}

	public void periodicAutos() {
		this.autoBindings.periodic();
	}

	/**
	 * code for chusing modes and binding hard coded ones
	 */
	public void init() {
		this.configureTestMode();
		this.simBindings.ifPresent(SimBindings::bind);
		this.bindAutos();
	}

	public void periodic() {
		this.matchBindings.periodic();
	}
}