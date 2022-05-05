package util;

import java.io.Serializable;

public class step implements Serializable {

    int cordinateX;
    int cordinateY;
    public step(){}

    public step(int cordinateX, int cordinateY) {
        this.cordinateX = cordinateX;
        this.cordinateY = cordinateY;
    }

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
