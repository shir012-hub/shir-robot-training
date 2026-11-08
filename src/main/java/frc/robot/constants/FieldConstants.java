package frc.robot.constants;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import frc.DELib26.util.FieldUtil;
import org.photonvision.estimation.TargetModel;

import java.util.EnumMap;

import static frc.DELib26.util.FieldUtil.*;

public class FieldConstants {
	public static final AprilTagFieldLayout FIELD_LAYOUT;

	static {
		FIELD_LAYOUT = AprilTagFieldLayout.loadField(AprilTagFields.k2026RebuiltWelded);
	}

	public static final double FIELD_WIDTH = 8.069;
	public static final double FIELD_LENGTH = 16.540988;

	// 2.899 =
	// 3.102 = 4.967
	/**
	 * Officially defined and relevant vertical lines found on the field (defined by X-axis offset)
	 */
	public static final class LinesVertical {
		public static final double CENTER = FIELD_LENGTH / 2.0;
		public static final double ROBOT_STARTING_LINE = 4.03;
		public static final double HUB_END = 5.229174199999999;
	}

	/**
	 * Officially defined and relevant horizontal lines found on the field (defined by Y-axis offset)
	 */
	public static final class LinesHorizontal {
		public static final double CENTER = FIELD_WIDTH / 2.0;
		public static final double RIGHT_TRENCH_OPEN_START = 1.005;
		public static final double RIGHT_TRENCH_OPEN_END = 2.705;
		public static final double LEFT_TRENCH_OPEN_START = FIELD_WIDTH - RIGHT_TRENCH_OPEN_END;
		public static final double LEFT_TRENCH_OPEN_END = FIELD_WIDTH - RIGHT_TRENCH_OPEN_START;
	}

	public static enum Side {
		LEFT,
		RIGHT
	}

	public static class StartingPositions {
		public static enum StartingPosition {
			RIGHT,
			CENTER,
			LEFT,
			FAR_LEFT,
			FAR_RIGHT;

			public Translation2d getBlueTranslation(double bumperWidthMeters) {
				return switch (this) {
					case RIGHT -> getStartingTranslationRight(bumperWidthMeters);
					case CENTER -> getStartingTranslationCenter(bumperWidthMeters);
					case LEFT -> getStartingTranslationLeft(bumperWidthMeters);
					case FAR_LEFT -> BLUE_STARTING_TRANSLATION_FAR_LEFT;
					case FAR_RIGHT -> BLUE_STARTING_TRANSLATION_FAR_RIGHT;
				};
			}
		}

		private static Translation2d BLUE_STARTING_TRANSLATION_RIGHT = new Translation2d(
			LinesVertical.ROBOT_STARTING_LINE,
				2.4384
		);

		private static Translation2d BLUE_STARTING_TRANSLATION_CENTER = new Translation2d(
			LinesVertical.ROBOT_STARTING_LINE,
			LinesHorizontal.CENTER
		);

		private static Translation2d BLUE_STARTING_TRANSLATION_LEFT = new Translation2d(
			LinesVertical.ROBOT_STARTING_LINE,
			FIELD_WIDTH - 2.4384
		);

		public static Translation2d BLUE_STARTING_TRANSLATION_FAR_LEFT = new Translation2d(
			LinesVertical.ROBOT_STARTING_LINE,
			FIELD_WIDTH - 0.644398
		);

		public static Translation2d BLUE_STARTING_TRANSLATION_FAR_RIGHT = new Translation2d(
			LinesVertical.ROBOT_STARTING_LINE,
			0.644398
		);

		public static Translation2d getStartingTranslationRight(double bumperWidthMeters) {
			return BLUE_STARTING_TRANSLATION_RIGHT.minus(
					new Translation2d(bumperWidthMeters / 2.0, 0)
				);
		}

		public static Translation2d getStartingTranslationCenter(double bumperWidthMeters) {
			return BLUE_STARTING_TRANSLATION_CENTER.minus(
					new Translation2d(bumperWidthMeters / 2.0, 0)
				);
		}

		public static Translation2d getStartingTranslationLeft(double bumperWidthMeters) {
			return BLUE_STARTING_TRANSLATION_LEFT.minus(
					new Translation2d(bumperWidthMeters / 2.0, 0)
				);
		}
	}

	public static class FulePit {

		public static final Translation2d FUEL_PIT_CENTER = new Translation2d(
			LinesVertical.CENTER,
			LinesHorizontal.CENTER
		);

		public static final Translation2d BLUE_FUEL_PIT_RIGHT_TRANSLATION = new Translation2d(
			LinesVertical.CENTER,
			LinesHorizontal.CENTER - 2.31013
		);

		public static final Translation2d BLUE_FUEL_PIT_LEFT_TRANSLATION = new Translation2d(
			LinesVertical.CENTER,
			LinesHorizontal.CENTER + 2.31013
		);
	}

	public static Pose2d blueToRed(Pose2d bluePose) {
		return FieldUtil.mirrorBothAxes(bluePose, FIELD_LENGTH, FIELD_WIDTH);
	}

	public static Translation2d blueToRed(Translation2d blueTranslation) {
		return FieldUtil.mirrorBothAxes(blueTranslation, FIELD_LENGTH, FIELD_WIDTH);
	}

	public static Rotation2d blueToRed(Rotation2d blueRotation) {
		return FieldUtil.mirrorBothAxes(blueRotation, FIELD_LENGTH, FIELD_WIDTH);
	}

	public static Translation2d getAllianceTranslation(Translation2d blueTranslation) {
		return FieldUtil.isBlueAllianceOrDefault()
				? blueTranslation
				: blueToRed(blueTranslation);
	}

	public static Pose2d getAlliancePose(Pose2d bluePose) {
		return FieldUtil.isBlueAllianceOrDefault()
				? bluePose
				: blueToRed(bluePose);
	}

	public static Rotation2d getAllianceRotation(Rotation2d blueRotation) {
		return FieldUtil.isBlueAllianceOrDefault()
				? blueRotation
				: blueToRed(blueRotation);
	}

	public static Pose2d getAllianceRightCornerPosition() {
		return isBlueAllianceOrDefault()
				? new Pose2d(0, 0, Rotation2d.kZero)
				: new Pose2d(FIELD_LENGTH, FIELD_WIDTH, Rotation2d.k180deg);
	}


	public static final class GamePieceConstants {
		public static final String GAME_PIECE_TYPE = "Fuel";
		public static final double FUEL_RADIUS_METERS = 0.075;
		public static final TargetModel MAPLE_FUEL_TARGET_MODEL = new TargetModel(
			FUEL_RADIUS_METERS * 2.0
		);
	}

	public static final class Hub {
		// Dimensions
		public static final double WIDTH = Units.inchesToMeters(47.0);
		public static final double HEIGHT =
			Units.inchesToMeters(72.0); // includes the catcher at the top
		public static final double INNER_WIDTH = Units.inchesToMeters(41.7);
		public static final double INNER_HEIGHT = Units.inchesToMeters(56.5);

		// Relevant reference points on alliance side
		public static final Translation2d BLUE_HUB_CENTER = new Translation2d(
			4.0218614 + WIDTH / 2.0,
			FIELD_WIDTH / 2.0
		);

		public static final Translation2d RED_HUB_CENTER = blueToRed(BLUE_HUB_CENTER);

		public static final Translation3d BLUE_TOP_CENTER_POINT = new Translation3d(
			BLUE_HUB_CENTER.getX(),
			BLUE_HUB_CENTER.getY(),
			HEIGHT
		);

		public static final Translation3d BLUE_INNER_CENTER_POINT = new Translation3d(
			BLUE_HUB_CENTER.getX(),
			BLUE_HUB_CENTER.getY(),
			INNER_HEIGHT
		);

		public static final Translation2d BLUE_NEAR_LEFT_CORNER = new Translation2d(
			BLUE_HUB_CENTER.getX() - WIDTH / 2.0,
			FIELD_WIDTH / 2.0 + WIDTH / 2.0
		);

		public static final Translation2d BLUE_NEAR_RIGHT_CORNER = new Translation2d(
			BLUE_HUB_CENTER.getX() - WIDTH / 2.0,
			FIELD_WIDTH / 2.0 - WIDTH / 2.0
		);

		public static final Translation2d BLUE_FAR_LEFT_CORNER = new Translation2d(
			BLUE_HUB_CENTER.getX() + WIDTH / 2.0,
			FIELD_WIDTH / 2.0 + WIDTH / 2.0
		);

		public static final Translation2d BLUE_FAR_RIGHT_CORNER = new Translation2d(
			BLUE_HUB_CENTER.getX() + WIDTH / 2.0,
			FIELD_WIDTH / 2.0 - WIDTH / 2.0
		);

		public static final Translation2d getAllianceHubCenter() {
			return FieldUtil.isBlueAllianceOrDefault() ? BLUE_HUB_CENTER : RED_HUB_CENTER;
		}
	}

	public static final class Trench {

		public enum TrenchPoint {
			NEAR,
			FAR;

			public TrenchPoint opposite() {
				return this == NEAR ? FAR : NEAR;
			}
		}

		public static final double DEPTH = Units.inchesToMeters(47.0);
		public static final double OPENING_WIDTH = Units.inchesToMeters(50.34);
		public static final double OPENING_HEIGHT = Units.inchesToMeters(22.25);

		private static final EnumMap<Side, EnumMap<TrenchPoint, Translation2d>> BLUE =
			new EnumMap<>(Side.class);

		// Opening centers in Y (right opening is near y=0, left opening is near y=fieldWidth)
		private static final double rightOpeningCenterY =
			(0 + OPENING_WIDTH) / 2.0;

		private static final double leftOpeningCenterY =
			(FIELD_WIDTH - OPENING_WIDTH) / 2.0;

		static {
			for (Side s : Side.values()) BLUE.put(s, new EnumMap<>(TrenchPoint.class));

			BLUE.get(Side.RIGHT).put(TrenchPoint.NEAR, new Translation2d(LinesVertical.ROBOT_STARTING_LINE, rightOpeningCenterY));
			BLUE.get(Side.RIGHT).put(TrenchPoint.FAR,  new Translation2d(LinesVertical.HUB_END,  rightOpeningCenterY));

			BLUE.get(Side.LEFT).put(TrenchPoint.NEAR,  new Translation2d(LinesVertical.ROBOT_STARTING_LINE, leftOpeningCenterY));
			BLUE.get(Side.LEFT).put(TrenchPoint.FAR,   new Translation2d(LinesVertical.HUB_END,  leftOpeningCenterY));
		}

		public static Translation2d getTrenchPoint(Alliance alliance, Side side, TrenchPoint point) {
			Translation2d base = BLUE.get(side).get(point);
			return (alliance == Alliance.Blue) ? base : blueToRed(base);
		}

		public static Translation2d getTrenchPointWithXOffset(Alliance alliance, Side side, TrenchPoint point, double xOffsetMeters) {
			double signed = (point == TrenchPoint.FAR) ? +xOffsetMeters : -xOffsetMeters;
			return getTrenchPoint(alliance, side, point).plus(new Translation2d(signed, 0.0));
		}
	}

	public static final class Tower {
		public static final double RIGHT_UPRIGHT_Y = Units.inchesToMeters(123.97);
		public static final double LEFT_UPRIGHT_Y = FIELD_WIDTH - Units.inchesToMeters(146.72);

		public static final double UPRIGHTS_X = Units.inchesToMeters(67.06);

		public static final Pose2d BLUE_RIGHT_CLIMB_LATCH_POSE = new Pose2d(UPRIGHTS_X, RIGHT_UPRIGHT_Y, Rotation2d.k180deg);

		public static final Pose2d BLUE_LEFT_CLIMB_LATCH_POSE = new Pose2d(UPRIGHTS_X, LEFT_UPRIGHT_Y, Rotation2d.kZero);

		public static Pose2d getClimbLatchPose(Side side) {
			if (side == Side.RIGHT) {
			return FieldUtil.isBlueAllianceOrDefault()
				? BLUE_RIGHT_CLIMB_LATCH_POSE
				: blueToRed(BLUE_RIGHT_CLIMB_LATCH_POSE);
			} else {
			return FieldUtil.isBlueAllianceOrDefault()
				? BLUE_LEFT_CLIMB_LATCH_POSE
				: blueToRed(BLUE_LEFT_CLIMB_LATCH_POSE);
			}
		}

		/** Compute align pose using a robot-provided offset (keeps FieldConstants robot-agnostic). */
		public static Pose2d getClimbAlignPose(Side side, double alignXOffsetMeters) {
			Pose2d latch = getClimbLatchPose(side);

			double signed = latch.getRotation().equals(Rotation2d.k180deg) ? +alignXOffsetMeters : -alignXOffsetMeters;

			return new Pose2d(
				latch.getX() + signed,
				latch.getY(),
				latch.getRotation()
			);
		}


	}


	public static final class Outpost {
		//public static final
	}

	public static class Depot {
		// Dimensions
		public static final double WIDTH = Units.inchesToMeters(42.0);
		public static final double DEPTH = Units.inchesToMeters(27.0);
		public static final double HEIGHT = Units.inchesToMeters(1.125);
		public static final double DISTANCE_FROM_CENTER_Y = Units.inchesToMeters(75.93);

		// Relevant reference points on alliance side
		public static final Translation3d DEPOT_CENTER =
			new Translation3d(DEPTH, (FIELD_WIDTH / 2) + DISTANCE_FROM_CENTER_Y, HEIGHT);
		public static final Translation3d LEFT_CORNER =
			new Translation3d(DEPTH, (FIELD_WIDTH / 2) + DISTANCE_FROM_CENTER_Y + (WIDTH / 2), HEIGHT);
		public static final Translation3d RIGHT_CORNER =
			new Translation3d(DEPTH, (FIELD_WIDTH / 2) + DISTANCE_FROM_CENTER_Y - (WIDTH / 2), HEIGHT);
	}
}