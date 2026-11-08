package frc.robot.autos;

import com.pathplanner.lib.path.PathPlannerPath;
import frc.DELib26.autos.PathPlannerAutoBuilder;

public enum AutoPaths {
	START_FAR_LEFT_TO_LEFT_PIT("START_FAR_LEFT_TO_LEFT_PIT"),
	START_FAR_RIGHT_TO_RIGHT_PIT("START_FAR_LEFT_TO_LEFT_PIT", true),
	RIGHT_TRENCH_FAR_TO_RIGHT_SHOOT("RIGHT_TRENCH_FAR_TO_RIGHT_SHOOT"),
	LEFT_TRENCH_FAR_TO_LEFT_SHOOT("LEFT_TRENCH_FAR_TO_LEFT_SHOOT"),
	RIGHT_TRENCH_FAR_TO_LEFT_TOWER_ALIGN("RIGHT_TRENCH_FAR_TO_LEFT_TOWER_ALIGN"),
	ALLIANCE_CENTER_TO_LEFT_TOWER_ALIGN("ALLIANCE_CENTER_TO_LEFT_TOWER_ALIGN");

	private final PathPlannerPath path;

	AutoPaths(String pathName) {
		this(pathName, false);
	}

	AutoPaths(String pathName, boolean mirror) {
		if (mirror) {
			this.path = PathPlannerAutoBuilder.loadPath(pathName).mirrorPath();
		} else {
			this.path = PathPlannerAutoBuilder.loadPath(pathName);
		}
	}

	public PathPlannerPath getPath() {
		return this.path;
	}
}