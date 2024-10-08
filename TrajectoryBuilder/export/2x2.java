drive.trajectorySequenceBuilder(new Pose2d(48, 48, Math.toRadians(90)))
	.lineToConstantHeading(new Vector2d(-0, 48))
	.lineToConstantHeading(new Vector2d(0, 0))
	.lineToConstantHeading(new Vector2d(48, 0))
	.lineToConstantHeading(new Vector2d(48, 48))
	.build();