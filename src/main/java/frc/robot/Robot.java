package frc.robot;

import com.ctre.phoenix6.SignalLogger;
import com.ctre.phoenix6.Utils;
import com.pathplanner.lib.commands.PathfindingCommand;
import com.pathplanner.lib.pathfinding.Pathfinding;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.DELib26.autos.LocalADStarAK;
import frc.DELib26.subsystems.vision.visionUtil.VisionFieldPoseEstimate;
import frc.DELib26.util.LoggerUtil;
import frc.DELib26.util.MacAddressUtil;
import frc.DELib26.util.RunMode;
import frc.DELib26.util.VirtualSubsystem;
import frc.robot.constants.Constants;
import frc.robot.sim.SimBindings;
import org.ironmaple.simulation.SimulatedArena;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class Robot extends LoggedRobot {
	private Command autonomousCommand;

	// Only call robot container after you start the logger!!!
	public Robot() {
		// Record metadata
		Logger.recordMetadata("ProjectName", BuildInfo.MAVEN_NAME);
		Logger.recordMetadata("BuildDate", BuildInfo.BUILD_DATE);
		Logger.recordMetadata("GitSHA", BuildInfo.GIT_SHA);
		Logger.recordMetadata("GitDate", BuildInfo.GIT_DATE);
		Logger.recordMetadata("GitBranch", BuildInfo.GIT_BRANCH);
		Logger.recordMetadata("Mac Address", MacAddressUtil.getMACAddress());
		String dirtyMes;

		switch (BuildInfo.DIRTY) {
			case 0:
				dirtyMes = "All changes committed";
				break;
			case 1:
				dirtyMes = "Uncommitted changes";
				break;
			default:
				dirtyMes = "Unknown";
				break;
		}
		Logger.recordMetadata("GitDirty", dirtyMes);

		Path base = LoggerUtil.getDefaultLogDirectory();
		if (RunMode.isReplay()) {
			super.setUseTiming(false);
			String logPath = LogFileUtil.findReplayLog();
			Logger.setReplaySource(new WPILOGReader(logPath));
			Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
		} else {
			Path logDir = base.resolve("advantage");
			try {
				Files.createDirectories(logDir);
			} catch (Exception ignored) {
				DriverStation.reportError("Could not create log directory: " + logDir, false);
			}
			Logger.addDataReceiver(new WPILOGWriter(logDir.toString()));
			Logger.addDataReceiver(new NT4Publisher());
		}

		// CTRE SignalLogger to USB when present (e.g., /u/sysid)
		SignalLogger.enableAutoLogging(false);
		Path sysidDir = base.resolve("sysid");

		try {
			Files.createDirectories(sysidDir);
		} catch (Exception ignored) {
			DriverStation.reportError("Could not create sysid directory: " + sysidDir.toString(), false);
		}

		SignalLogger.setPath(sysidDir.toString());
		Logger.start();
		// Create the RobotContainer AFTER starting the logger!
		RobotContainer.getInstance();
	}

	@Override
	public void robotInit() {
		Pathfinding.setPathfinder(new LocalADStarAK());
		// Notify the FMS (Field Management System) connected at robot initialization.
		// This ensures the FMS at the time of initialization is used, as it may differ
		// from the one present during robot construction.
		BindingsManager.getInstance().init();
		for (int i = 0; i < UsbCamera.enumerateUsbCameras().length; i++) {
			UsbCamera cam = CameraServer.startAutomaticCapture(i);
			try {
				cam.setFPS(Constants.RobotConstants.USB_CAMERA_FPS);
			} catch (Exception e) {
				DriverStation.reportWarning(
						"Failed to initialize camera " + i + ": " + e.getMessage(),
						false
				);
			}

		}
		CommandScheduler.getInstance().schedule(PathfindingCommand.warmupCommand());
		// In match, this is is overridden to false by the FMS so no problems there.
		DriverStation.silenceJoystickConnectionWarning(true);
	}


	@Override
	public void robotPeriodic() {
		// 1. we run the periodic of all subsystems
		// 2. we update the robot state with the latest swerve and pigeon inputs (this
		// is used for vision pose estimation)
		// 3. we run the periodic of all virtual subsystems (this is where vision pose
		// estimation updates happen)
		// 4. if we have a vision pose estimate, we add it as a vision measurement to
		// the swerve subsystem (this is used for pose fusion)

		CommandScheduler.getInstance().run();
		RobotContainer.getInstance().robotState2026.setLatestFieldRelativeSpeeds(
				RobotContainer.getInstance().swerveSubsystem.getFieldSpeeds()
			);

		VirtualSubsystem.runAllPeriodic();

		RobotContainer.getInstance().robotState2026.forEachPendingMegatagEstimate(
				est -> RobotContainer.getInstance().swerveSubsystem.addVisionMeasurement(est));

		Logger.runEveryN(4, RobotContainer.getInstance()::logTelemetry);
		BindingsManager.getInstance().periodic();
	}

	@Override
	public void disabledInit() {
		CommandScheduler.getInstance().cancelAll();
		CommandScheduler.getInstance().getActiveButtonLoop().clear();
		RobotContainer.getInstance().disableAll();
	}

	@Override
	public void disabledPeriodic() {
		BindingsManager.getInstance().periodicAutos();
	}

	@Override
	public void disabledExit() {
	}

	@Override
	public void autonomousInit() {
		// if we have a vision pos resently then dosnt reset,
		RobotContainer rc = RobotContainer.getInstance();
		rc.simCoordinator.ifPresent(coord -> coord.resetForAutoSim());

		this.autonomousCommand = rc.getAutonomousCommand();

		double now = Utils.getCurrentTimeSeconds();

		double sampleTime = now - Constants.AutonomousConstants.AUTONOMOUS_VISION_LOOKBACK_TIME_SECONDS;

		Optional<VisionFieldPoseEstimate> latestEstimate = rc.robotState2026.getLatestMegatagEstimate();

		boolean hasResentVisionPose = latestEstimate.isPresent()
				&& latestEstimate.get().getTimestampSeconds() >= sampleTime;
		if (RunMode.isSim() || !hasResentVisionPose) {
			Pose2d autoStartPose = BindingsManager.getInstance().autoBindings.getAutoStartPose()
					.orElse(rc.robotState2026.getLatestRobotPose());
			rc.swerveSubsystem.resetPose(autoStartPose);
		}

		if (this.autonomousCommand != null) {
			CommandScheduler.getInstance().schedule(this.autonomousCommand);
		}
	}

	@Override
	public void autonomousPeriodic() {
	}

	@Override
	public void autonomousExit() {

	}

	@Override
	public void teleopInit() {
		CommandScheduler.getInstance().cancelAll();
		CommandScheduler.getInstance().getActiveButtonLoop().clear();
		RobotContainer.getInstance().configureTeleopBindings();
		if (RunMode.isSim()) {
			new SimBindings().bind();
		}
		if (this.autonomousCommand != null) {
			this.autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void teleopExit() {
		CommandScheduler.getInstance().cancelAll();
		CommandScheduler.getInstance().getActiveButtonLoop().clear();
	}

	@Override
	public void testInit() {
		CommandScheduler.getInstance().cancelAll();
		CommandScheduler.getInstance().getActiveButtonLoop().clear();
		BindingsManager.getInstance().testModeInit();
	}

	@Override
	public void testPeriodic() {
		BindingsManager.getInstance().testModePeriodic();
	}

	@Override
	public void testExit() {
	}

	@Override
	public void simulationInit() {
	}

	@Override
	public void simulationPeriodic() {
		SimulatedArena.getInstance().simulationPeriodic();

		RobotContainer.getInstance().simCoordinator.ifPresent(simCoordinator -> simCoordinator.simulationPeriodic());

		BindingsManager.getInstance().simBindings.ifPresent(SimBindings::periodic);
	}

}