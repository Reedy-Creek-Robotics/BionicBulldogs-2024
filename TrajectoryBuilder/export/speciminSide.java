drive.trajectorySequenceBuilder(new Pose2d(6, -66, Math.toRadians(-90)))
	.lineToConstantHeading(new Vector2d(6, -30))
	.lineToLinearHeading(new Pose2d(48, -60, Math.toRadians(90)))
	.lineToLinearHeading(new Pose2d(12, -30, Math.toRadians(-90)))
	.lineToConstantHeading(new Vector2d(48, -60))
	.build();