package frc.robot;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.DELib26.util.RobotState;
import frc.robot.constants.Constants;

public class RobotState2026 extends RobotState {

	private ChassisSpeeds latestFieldRelativeSpeeds;
	private boolean kickerHasFuel = false;
	private int fuelShotCount = 0;


	public RobotState2026() {
		super(Constants.VisionConstants.VISION_CONFIG);
	}

	public boolean kickerHasFuel() {
		return this.kickerHasFuel;
	}

	public void setKickerHasFuel(boolean newState) {
		this.kickerHasFuel = newState;
	}

	public int getFuelShotCount() {
		return fuelShotCount;
	}

	public void incrementFuelShotCount() {
		this.fuelShotCount++;
	}

	public void setLatestFieldRelativeSpeeds(ChassisSpeeds latestFieldRelativeSpeeds) {
		this.latestFieldRelativeSpeeds = latestFieldRelativeSpeeds;
	}

	public ChassisSpeeds getLatestFieldRelativeSpeeds() {
		return latestFieldRelativeSpeeds;
	}
}
