package org.firstinspires.ftc.teamcode

import android.content.Context
import com.minerkid08.dynamicopmodeloader.FileServer
import org.firstinspires.ftc.ftccommon.external.OnCreate

@OnCreate
fun startFileserver(ctx: Context)
{
	FileServer.start();
}