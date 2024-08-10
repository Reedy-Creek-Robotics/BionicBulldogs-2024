drive.trajectorySequenceBuilder(new Pose2d(-36, -66, Math.toRadians(90)))
	.lineToLinearHeading(new Pose2d(-54, -36, Math.toRadians(-0)))
	.lineToLinearHeading(new Pose2d(-66, -36, Math.toRadians(180)))
	.addDisplacementMarker(() -> {
		System.out.println(grab);
	})
	.waitSeconds(1)
	.lineToConstantHeading(new Vector2d(-60, -24))
	.splineToConstantHeading(new Vector2d(-48, -12), Math.toRadians(-0))
	.splineToConstantHeading(new Vector2d(24, -12), Math.toRadians(-0))
	.splineToConstantHeading(new Vector2d(39, -24), Math.toRadians(-35))
	.splineToConstantHeading(new Vector2d(54, -36), Math.toRadians(-0))
	.addDisplacementMarker(() -> {
		System.out.println(drop);
	})
	.waitSeconds(1)
	.lineToConstantHeading(new Vector2d(39, -24))
	.splineToConstantHeading(new Vector2d(24, -12), Math.toRadians(180))
	.splineToConstantHeading(new Vector2d(-66, -12), Math.toRadians(180))
	.addDisplacementMarker(() -> {
		System.out.println(grab);
	})
	.waitSeconds(1)
	.lineToConstantHeading(new Vector2d(24, -12))
	.splineToConstantHeading(new Vector2d(39, -24), Math.toRadians(-45))
	.splineToConstantHeading(new Vector2d(54, -36), Math.toRadians(-0))
	.addDisplacementMarker(() -> {
		System.out.println(drop);
	})
	.build();