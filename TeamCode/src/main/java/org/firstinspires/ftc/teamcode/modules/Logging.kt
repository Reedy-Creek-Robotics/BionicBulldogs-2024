package org.firstinspires.ftc.teamcode.modules

import kotlin.text.String

class Logging
{
  lateinit var logger: Datalogger;
  val posX = Datalogger.GenericField("posX");
  val posY = Datalogger.GenericField("posY");
  val posH = Datalogger.GenericField("posH");

  fun init()
  {
    val builder = Datalogger.Builder();
    builder.setFilename("ftcLogs/log.csv");
    builder.setAutoTimestamp(Datalogger.AutoTimestamp.DECIMAL_SECONDS);
    builder.setFields(posX, posY, posH);
    logger = builder.build();
  }
  
  fun update()
  {
    logger.writeLine();
  }
}
