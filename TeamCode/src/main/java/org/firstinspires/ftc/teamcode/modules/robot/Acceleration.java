package org.firstinspires.ftc.teamcode.modules.robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.modules.Vec2;
@Config
public class Acceleration {
    public static double accelTime = 0.25;
    ElapsedTime elapsedTime;
    float prevTime;
    Vec2 prevOut;
    public Acceleration(){
        prevOut = new Vec2();
        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
    }
    public void resetTime(){
        elapsedTime.reset();
        prevOut.x = 0;
        prevOut.y = 0;
        prevTime = 0;
    }
    public Vec2 getNext(Vec2 in){
        float currentTime = (float)elapsedTime.seconds();
        float deltaTime = currentTime - prevTime;
        prevTime = currentTime;
        prevOut.x = lerp(prevOut.x, in.x, deltaTime / (float)accelTime);
        prevOut.y = lerp(prevOut.y, in.y, deltaTime / (float)accelTime);
        return prevOut;
    }
    float lerp(float a, float b, float t){
        return a + (b - a) * t;
    }
}
