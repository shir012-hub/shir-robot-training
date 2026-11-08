package frc.robot.autos;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.path.PathConstraints;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.DELib26.subsystems.swerve.SwerveSubsystem;
import frc.DELib26.subsystems.swerve.swerveCommands.DriveToPoint;
import frc.robot.RobotContainer;
import frc.robot.constants.Constants;

import java.util.Set;
import java.util.function.Supplier;

public class AutoCommands {
	public static Command pathfindTo(Supplier<Pose2d> poseSupplier) {
		return pathfindTo(
					poseSupplier,
					RobotContainer.getInstance().autoBuilder.getDefaultPathConstraints(
							Constants.SwerveConstants.SWERVE_CONFIG
						)
				);
	}

	public static Command pathfindTo(Supplier<Pose2d> poseSupplier, PathConstraints constraints) {
		return pathfindThenDriveToPose(
				RobotContainer.getInstance().swerveSubsystem,
				poseSupplier,
				constraints);
	}

	public static Command pathfindThenDriveToPose(
			SwerveSubsystem swerve,
			Supplier<Pose2d> targetPoseSupplier,
			PathConstraints constraints) {
		return Commands.defer(() -> {
			Pose2d targetPose = targetPoseSupplier.get();

			Command pp = AutoBuilder.pathfindToPose(
					targetPose,
					constraints,
					Units.MetersPerSecond.of(0));
			return pp.andThen(new DriveToPoint(swerve, () -> targetPose));
		}, Set.of(swerve));
	}

}
