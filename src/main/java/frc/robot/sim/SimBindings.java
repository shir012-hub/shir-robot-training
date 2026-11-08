package frc.robot.sim;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.DELib26.bindings.Bindings;
import frc.DELib26.util.FieldUtil;
import frc.DELib26.util.dashboard.DashboardUtils;
import frc.robot.RobotContainer;
import frc.robot.constants.FieldConstants;

public class SimBindings extends Bindings {
	private static final String DASHBOARD_FOLDER = "SimBindings";
	private static final String COMMANDS_FOLDER = DASHBOARD_FOLDER + "/Commands/";

	private static final String RESET_POSE_TO_CENTER_KEY = COMMANDS_FOLDER + "ResetPoseToCenter";


	@Override
	public void binding() {
		DashboardUtils.setData(
			RESET_POSE_TO_CENTER_KEY,
			new InstantCommand(() ->
				RobotContainer.getInstance().swerveSubsystem
					.resetPose(new Pose2d(
							FieldConstants.FIELD_LENGTH / 2,
							FieldConstants.FIELD_WIDTH / 2,
							FieldUtil.getAllianceForwardHeading()
						)
					)
			)
		);

	}

	@Override
	public void periodic() {}
}
