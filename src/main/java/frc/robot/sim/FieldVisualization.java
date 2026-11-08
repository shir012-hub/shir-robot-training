package frc.robot.sim;

import frc.DELib26.util.FieldUtil;
import frc.robot.constants.FieldConstants;
import org.ironmaple.simulation.seasonspecific.rebuilt2026.Arena2026Rebuilt;
import org.littletonrobotics.junction.Logger;

/**
 * Class to handle visualization of the field in simulation mode.
 */
public class FieldVisualization {

	private static FieldVisualization instance = new FieldVisualization();

	public static FieldVisualization getInstance() {
		return instance;
	}

	public FieldVisualization() {
		this.initializeViz();
	}

	public void initializeViz() {
		arena().placeGamePiecesOnField();
	}

	public void dump() {
		arena().outpostDump(FieldUtil.isBlueAllianceOrDefault());
	}

	public void updateViz() {
		Logger.recordOutput(
				"FieldSimulation/Fuel",
				arena().getGamePiecesArrayByType(FieldConstants.GamePieceConstants.GAME_PIECE_TYPE));
	}

	public void resetFieldForAuto() {
		arena().resetFieldForAuto();
	}

	private static Arena2026Rebuilt arena() {
		var inst = Arena2026Rebuilt.getInstance();
		if (inst instanceof Arena2026Rebuilt a)
			return a;
		throw new IllegalStateException("SimulatedArena instance is not Arena2026Rebuilt: " + inst);
	}
}
