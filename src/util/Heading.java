package util;

import java.util.Random;

public class Heading {
    private int cordinateX;
    private int cordinateY;

    public Heading() {
        Random r = new Random();
        cordinateX = r.nextInt(700);
        cordinateY = r.nextInt(700);
    }

    public int getCordinateX() {
        return cordinateX;
    }

    public int getCordinateY() {
        return cordinateY;
    }

    public static double distance(agentData me, agentData other) {
        return Math.sqrt(Math.pow(me.getCircle().getCenterX() - other.getCircle().getCenterX(), 2) +
                Math.pow(me.getCircle().getCenterY() - other.getCircle().getCenterY(), 2));
    }
    public static double distance(int hx,int x,int hy,int y) {
        return Math.sqrt(Math.pow(hx - x, 2) +
                Math.pow(hy - y, 2));
    }

    public static boolean collide(agentData me, agentData other) {
        if (distance(me, other) <= (2*me.getRaduis())+5) {
            return true;
        }
        return false;
    }
}
