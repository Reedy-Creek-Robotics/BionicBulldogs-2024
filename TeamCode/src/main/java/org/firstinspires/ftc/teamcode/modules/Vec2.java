package org.firstinspires.ftc.teamcode.modules;

public class Vec2 {
    public float x;
    public float y;

    public Vec2(float _x, float _y) {
        x = _x;
        y = _y;
    }

    public Vec2(float _x) {
        x = _x;
        y = _x;
    }

    public Vec2() {
        x = 0;
        y = 0;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public Vec2 mul(Vec2 other) {
        return new Vec2(x * other.x, y * other.y);
    }

    public Vec2 div(Vec2 other) {
        return new Vec2(x / other.x, y / other.y);
    }

    public float length() {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vec2 neg() {
        return new Vec2(-x, -y);
    }

    public Vec2 getVel(Vec2 target, Vec2 start, float velStep) {
        if (start.sub(target).length() < velStep) {
            return target;
        }
        Vec2 a = target.sub(start);
        Vec2 b = a.div(new Vec2(a.neg().length()));
        Vec2 c = b.mul(new Vec2(velStep));
        Vec2 d = c.add(start);
        return d;
    }
}
