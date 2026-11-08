package frc.robot.autos;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.DELib26.bindings.Bindings;
import frc.DELib26.util.dashboard.DashboardUtils;
import org.littletonrobotics.junction.Logger;

import java.util.Map;
import java.util.Optional;

public class AutoBindings extends Bindings {
	private static final String DASHBOARD_FOLDER = "Auto";

	private static final String AUTO_KEY = DASHBOARD_FOLDER + "/Auto";
	private static final String WAIT_START_SEC_KEY = DASHBOARD_FOLDER + "/Start Wait Time (sec)";

	private final SendableChooser<String> autoChooser = new SendableChooser<>();
	private final Map<String, AutoProfile> autoProfilesMap;

	private String lastSelected = null;

	public AutoBindings() {
		this.autoProfilesMap = AutoProfiles.createAutosMap();
	}

	@Override
	public void binding() {

		// Put options first
		for (String key : autoProfilesMap.keySet()) {
			autoChooser.addOption(key, key);
		}

		// Then set default to a key that definitely exists in the map
		autoChooser.setDefaultOption(AutoProfiles.STAY_KEY, AutoProfiles.STAY_KEY);

		SmartDashboard.putData(AUTO_KEY, autoChooser);

		DashboardUtils.setData(WAIT_START_SEC_KEY, 0, true);
	}

	@Override
	public void periodic() {
		String selected = autoChooser.getSelected();
		if (selected == null || selected.equals(lastSelected)) return;
		lastSelected = selected;
	}

	public AutoProfile getAuto() {
		String autoName = autoChooser.getSelected();
		Logger.recordOutput("Auto/Selected Auto", autoName);
		SmartDashboard.putString("Auto/Selected Auto", autoName);

		// Fallback so you never return null
		AutoProfile profile = autoProfilesMap.get(autoName);
		if (profile == null) profile = autoProfilesMap.get(AutoProfiles.STAY_KEY);
		return profile;
	}

	public Optional<Pose2d> getAutoStartPose() {
		AutoProfile profile = getAuto();
		return (profile != null) ? Optional.of(profile.startPose().get()) : Optional.empty();
	}
}