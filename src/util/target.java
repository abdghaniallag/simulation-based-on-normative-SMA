package util;

import java.io.Serializable;

public class target implements Serializable {
    int cordinateX;
    int cordinateY;
    public target(){};

    public int getCordinateX() {
        return cordinateX;
    }

    public void setCordinateX(int cordinateX) {
        this.cordinateX = cordinateX;
    }

    public int getCordinateY() {
        return cordinateY;
    }

    public void setCordinateY(int cordinateY) {
        this.cordinateY = cordinateY;
    }
}
