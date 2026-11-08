package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.function.Supplier;

public record AutoProfile(
	Supplier<Command> commandFactory,
	Supplier<Pose2d> startPose
) {}