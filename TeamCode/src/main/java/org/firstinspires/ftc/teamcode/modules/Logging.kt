package org.firstinspires.ftc.teamcode.modules

class Logging
{
  private val logger: Datalogger;
  val state = Datalogger.GenericField("state");
  val posX = Datalogger.GenericField("posX");
  val posY = Datalogger.GenericField("posY");
  val posH = Datalogger.GenericField("posH");

  init
  {
    val builder = Datalogger.Builder();
    builder.setFilename("ftcLogs/log.csv");
    builder.setAutoTimestamp(Datalogger.AutoTimestamp.DECIMAL_SECONDS);
    builder.setFields(state, posX, posY, posH);
    logger = builder.build();
  }
  
  fun update()
  {
    logger.writeLine();
  }
}
