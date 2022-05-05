package util;

import jade.core.AID;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class agentData extends Shape  implements Serializable {

	private AID aid;
	private String type;
	private String family;
	private int compte;
	private int coordinateX;
	private int coordinateY;
	private int homeX;
	private int homeY;
	private int targetX;
	private int targetY;
	private ArrayList<target> targets = new ArrayList<>();
	private step step;
	private String pType;
	private Circle circle;
	private boolean running = false;
	private final double distance = 300;
	private final double raduis = 8;

	public agentData() {
	}

	public agentData(AID aid, String type) {
		this.aid = aid;
		this.type = type;
	}

	public AID getAid() {
		return aid;
	}

	public void setAid(AID aid) {
		this.aid = aid;
	}

	public String getType() {
		return type;
	}

	public String getFamily() {
		return family;
	}

	public void setFamily(String family) {
		this.family = family;
	}

	public int getCompte() {
		return compte;
	}

	public void setCompte(int compte) {
		this.compte = compte;
	}

	public int getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(int coordinateX) {
		this.coordinateX = coordinateX;
	}

	public int getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(int coordinateY) {
		this.coordinateY = coordinateY;
	}

	public int getHomeX() {
		return homeX;
	}

	public void setHomeX(int homeX) {
		this.homeX = homeX;
	}

	public int getHomeY() {
		return homeY;
	}

	public void setHomeY(int homeY) {
		this.homeY = homeY;
	}

	public int getTargetX() {
		return targetX;
	}

	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}

	public int getTargetY() {
		return targetY;
	}

	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}

	public ArrayList<target> getTargets() {
		return targets;
	}

	public util.step getStep() {
		return step;
	}

	public void setStep(util.step step) {
		this.step = step;
	}

	public String getpType() {
		return pType;
	}

	public void setType(String type) {
		if (this.type != null) {
			this.pType = this.type;
			this.type = type;
		} else {
			this.pType = type;
			this.type = type;
		}

	}

	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}

	public double getRaduis() {
		return raduis;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public double getDistance() {
		return distance;
	}

	public void update() {
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				if (running) {
					AnimationTimer anim = new AnimationTimer() {
						@Override
						public void handle(long l) {
							if (circle.getCenterX() == step.getCordinateX()
									&& circle.getCenterY() == step.getCordinateY()) {
								this.stop();
								running = false;
							} else {
								circle.setCenterX(step.getCordinateX());
								circle.setCenterY(step.getCordinateY());
								coordinateX = (int) circle.getCenterX();
								coordinateY = (int) circle.getCenterY();
							}

						}
					};
					anim.start();
				}
			}
		};
		Objects.requireNonNull(runnable, "runnable");
		if (Platform.isFxApplicationThread()) {
			runnable.run();
		} else {
			Platform.runLater(runnable);
		}

	}

	@Override
	public com.sun.javafx.geom.Shape impl_configShape() {
		// TODO Auto-generated method stub
		return null;
	}

}
