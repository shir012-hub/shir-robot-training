package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.PrintCommand;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;


public class AutoProfiles {

	// -------------------- Timing --------------------
	public static final double AUTO_DURATION_SEC = 20.0;


	// -------------------- Keys --------------------
	public static final String STAY_KEY = "Stay in place";


	public static Map<String, AutoProfile> createAutosMap() {
		// LinkedHashMap and not a HashMap to preserve insertion order
		Map<String, AutoProfile> map = new LinkedHashMap<>();

		map.put(STAY_KEY, stayInPlace());

		return map;
	}

	private static AutoProfile stayInPlace() {
		Supplier<Command> cmdSup = () -> new PrintCommand("Staying in place (auto)");
		Supplier<Pose2d> start = () -> new Pose2d();
		return new AutoProfile(cmdSup, start);
	}
}