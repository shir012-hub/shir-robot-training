package frc.robot.sim;

import frc.robot.RobotState2026;
import org.ironmaple.simulation.drivesims.AbstractDriveTrainSimulation;

public class SimCoordinator {


	public SimCoordinator(
		RobotState2026 robotState2026,
		AbstractDriveTrainSimulation driveTrainSimulation
	) {
	}

	public void resetForAutoSim() {
		FieldVisualization.getInstance().resetFieldForAuto();
	}

	public void simulationPeriodic() {}

}