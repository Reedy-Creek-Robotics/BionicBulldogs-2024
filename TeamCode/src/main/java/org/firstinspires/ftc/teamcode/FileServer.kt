package org.firstinspires.ftc.teamcode

import android.content.Context
import android.util.Log
import com.minerkid08.dynamicopmodeloader.FileServer
import org.firstinspires.ftc.ftccommon.external.OnCreate

@OnCreate
fun startFileserver(ctx: Context)
{
	Log.d("fileServer", "starting server");
	FileServer.start();
	Log.d("fileServer", "server started");
}