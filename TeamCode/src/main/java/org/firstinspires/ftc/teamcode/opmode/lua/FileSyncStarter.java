package org.firstinspires.ftc.teamcode.opmode.lua;

import android.content.Context;

import org.firstinspires.ftc.ftccommon.external.OnCreate;

public class FileSyncStarter {
    static FileSync fs;
    @OnCreate
    public static void start(Context context){
        fs = new FileSync();
    }
}
