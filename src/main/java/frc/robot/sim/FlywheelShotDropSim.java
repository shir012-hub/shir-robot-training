package frc.robot.sim;

import java.util.Random;

public class FlywheelShotDropSim {
	private final Random random = new Random();

	private final double minDropPercent;
	private final double maxDropPercent;

	public FlywheelShotDropSim(double minDropPercent, double maxDropPercent) {
		this.minDropPercent = minDropPercent;
		this.maxDropPercent = maxDropPercent;
	}

	public double getDroppedVelocityRPS(double currentFlywheelRPS) {
		double newMagnitude = Math.max(0.0, Math.abs(currentFlywheelRPS) * (1.0 - getShotDropPercent()));
		return Math.copySign(newMagnitude, currentFlywheelRPS);
	}

	public double getShotDropPercent() {
		return randomRange(minDropPercent, maxDropPercent);
	}

	private double randomRange(double min, double max) {
		return min + (max - min) * random.nextDouble();
	}
}