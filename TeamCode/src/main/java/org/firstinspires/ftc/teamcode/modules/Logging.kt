package org.firstinspires.ftc.teamcode.modules

class Logging
{
  lateinit var logger: Datalogger;
  var posX = Datalogger.GenericField("posX");
  var posY = Datalogger.GenericField("posY");
  var posH = Datalogger.GenericField("posH");

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
