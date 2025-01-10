package org.firstinspires.ftc.teamcode.modules;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerImpl;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;

import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class Datalogger
{
    private LoggableField[] fields;
    private BufferedCsvWriter bufferedCsvWriter;

    /*
     * NOTE: We cannot simply pass `new OpModeNotifications()` inline to the call
     * to register the listener, because the SDK stores the list of listeners in
     * a WeakReference set. This causes the object to be garbage collected because
     * nothing else is holding a reference to it.
     */
    private OpModeNotifications opModeNotifications = new OpModeNotifications();

    private Datalogger(BufferedCsvWriter bufferedCsvWriter, LoggableField[] fields)
    {
        this.bufferedCsvWriter = bufferedCsvWriter;
        this.fields = fields;

        OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).registerListener(opModeNotifications);

        writeHeader();
    }

    private class OpModeNotifications implements OpModeManagerNotifier.Notifications
    {
        @Override
        public void onOpModePostStop(OpMode opMode)
        {
            close();
            OpModeManagerImpl.getOpModeManagerOfActivity(AppUtil.getInstance().getActivity()).unregisterListener(this);
        }

        @Override
        public void onOpModePreInit(OpMode opMode) {}

        @Override
        public void onOpModePreStart(OpMode opMode) {}
    }

    private void writeHeader()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < fields.length; i++)
        {
            stringBuilder.append(fields[i].name);
            if (i < fields.length-1)
            {
                stringBuilder.append(",");
            }
        }

        try
        {
            bufferedCsvWriter.writeLine(stringBuilder.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Unable to initialize datalogger");
        }
    }

    public void writeLine()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < fields.length; i++)
        {
            fields[i].writeToBuffer(stringBuilder);
            if (i < fields.length-1)
            {
                stringBuilder.append(",");
            }
        }

        try
        {
            bufferedCsvWriter.writeLine(stringBuilder.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error writing datalog line");
        }
    }

    private void close()
    {
        try
        {
            bufferedCsvWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static abstract class LoggableField
    {
        protected final String name;

        public LoggableField(String name)
        {
            this.name = name;
        }

        public abstract void writeToBuffer(StringBuilder out);
    }

    public static class GenericField extends LoggableField
    {
        private String str = "";
        private static final String STR_FALSE = "false";
        private static final String STR_TRUE = "true";

        public GenericField(String name)
        {
            super(name);
        }

        @Override
        public void writeToBuffer(StringBuilder out)
        {
            out.append(str);
        }

        public void set(String string)
        {
            str = string;
        }

        public void set(String format, Object... args)
        {
            str = String.format(format, args);
        }

        public void set(int val)
        {
            str = Integer.toString(val);
        }

        public void set(boolean val)
        {
            str = val ? STR_TRUE : STR_FALSE;
        }

        public void set(byte val)
        {
            str = String.format("0x%x", val);
        }

        public void set(float val)
        {
            str = String.format("%.3f", val);
        }

        // 6-7-22 Add overloaded method with optional format parameter.
        public void set(String valFormat, float val)
        {
            str = String.format(valFormat, val);
        }
        
        public void set(double val)
        {
            str = String.format("%.3f", val);
        }
        
        // 6-7-22 Add overloaded method with optional format parameter.
        public void set(String valFormat, double val)
        {
            str = String.format(valFormat, val);
        }
                
        // 6-7-22  Added this method so user OpMode telemetry can display 
        // field contents (instead of memory address).
        @Override
        public String toString()
        {
            return str;
        }
    }

    private static class TimestampField extends LoggableField
    {
        private long tRef;
        private final DecimalFormat timeFmt = new DecimalFormat("000.000");

        public TimestampField(String name)
        {
            super(name);
            tRef = System.currentTimeMillis();
        }

        public void resetRef()
        {
            tRef = System.currentTimeMillis();
        }

        @Override
        public void writeToBuffer(StringBuilder out)
        {
            long deltaMs = System.currentTimeMillis() - tRef;
            float delta = deltaMs / 1000f;
            out.append(timeFmt.format(delta));
        }
    }

    public enum AutoTimestamp
    {
        DECIMAL_SECONDS,
        NONE
    }

    public static class Builder
    {
        private String filename;
        private LoggableField[] fields;
        private AutoTimestamp autoTimestamp;

        public Builder setFilename(String filename)
        {
            this.filename = filename;
            return this;
        }

        public Builder setFields(LoggableField... fields)
        {
            this.fields = fields;
            return this;
        }

        public Builder setAutoTimestamp(AutoTimestamp autoTimestamp)
        {
            this.autoTimestamp = autoTimestamp;
            return this;
        }

        public Datalogger build()
        {
            if (filename == null) throw new RuntimeException("Filename must not be null!");
            if (filename.endsWith(".csv")) filename = filename.replace(".csv", "");
            if (fields == null) throw new RuntimeException("Fields must not be null!");
            if (fields.length == 0) throw new RuntimeException("Fields must be non-zero length!");
            if (autoTimestamp == null) throw new RuntimeException("AutoTimestamp must not be null!");

            if (autoTimestamp == AutoTimestamp.DECIMAL_SECONDS)
            {
                LoggableField[] tmp = new LoggableField[fields.length+1];
                tmp[0] = new TimestampField("Timestamp");
                System.arraycopy(fields, 0, tmp, 1, fields.length);
                fields = tmp;
            }

            try
            {
                BufferedCsvWriter bufferedCsvWriter = new BufferedCsvWriter();
                return new Datalogger(bufferedCsvWriter, fields);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                throw new RuntimeException("Unable to create output file handle :(");
            }
        }
    }

    private static class BufferedCsvWriter
    {
        private FileWriter fileWriter;
        private BufferedWriter bufferedWriter;

        public BufferedCsvWriter() throws IOException
        {
            String path = "/sdcard/ftcLogs/latest.txt";

            String timePath = "/sdcard/ftcLogs/time.txt";
            File timeFile = new File(timePath);
            if(timeFile.exists())
            {
                byte[] encoded = Files.readAllBytes(Paths.get(timePath));
                String str = new String(encoded, StandardCharsets.UTF_8);
                Files.move(Paths.get(path), Paths.get(String.format("/sdcard/ftcLogs/%s.txt", str)));
            }

            File tmp = new File(path);
            if (!tmp.exists())
            {
                tmp.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(timePath, false);
            writer.write(LocalDateTime.now().toString());
            writer.close();

            fileWriter = new FileWriter(path, false);
            bufferedWriter = new BufferedWriter(fileWriter);
        }

        public void writeLine(String line) throws IOException
        {
            bufferedWriter.write(line);
            bufferedWriter.newLine();
        }

        public void close() throws IOException
        {
            bufferedWriter.close();
        }
    }
}
