package UI;

import agentpack.MyContainer;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import util.agentData;
import util.SystemData;

import java.util.ArrayList;
import java.util.Objects;

public class GUI extends CyclicBehaviour {
	EnvirennementGui gui;

	public GUI(EnvirennementGui gui) {
		this.gui = gui;
	}

	@Override
	public void action() {
		ACLMessage msg = gui.receive();
		if (msg != null)
			if (SystemData.IS_TERMINATE&&SystemData.HEALING_TIME==0) {
				// terminate system
				SystemData.calculateHealingTimeSystem();
				System.out.println("................terminate system.........................");
				System.out.println("....   " + (SystemData.HEALING_TIME) + "   ....");
 			gui.doSuspend();
			} else {

				boolean exist = false;
				int i;
				agentData agentdata = new agentData();
				try {
					agentdata.setAid(((agentData) msg.getContentObject()).getAid());
					agentdata.setType(((agentData) msg.getContentObject()).getType());
					agentdata.setFamily(((agentData) msg.getContentObject()).getFamily());
					agentdata.setCompte(((agentData) msg.getContentObject()).getCompte());
					agentdata.setRunning(((agentData) msg.getContentObject()).isRunning());
					agentdata.setCoordinateX(((agentData) msg.getContentObject()).getCoordinateX());
					agentdata.setCoordinateY(((agentData) msg.getContentObject()).getCoordinateY());
					agentdata.setStep(((agentData) msg.getContentObject()).getStep());
					System.out.println(msg.getSender().getLocalName() + " : " + agentdata.getType());
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				for (i = 0; i < gui.agentInfos.size(); i++) {
					if (gui.agentInfos.get(i).getAid().equals(agentdata.getAid())) {
						exist = true;
						break;
					}
				}
				if (exist) {
					// "Exist"

					SystemData.terminateSystem(gui.agentInfos);

					gui.agentInfos.get(i).setType(agentdata.getType());
					gui.agentInfos.get(i).setRunning(agentdata.isRunning());
					gui.agentInfos.get(i).setCoordinateX(agentdata.getCoordinateX());
					gui.agentInfos.get(i).setCoordinateY(agentdata.getCoordinateY());
					gui.agentInfos.get(i).setStep(agentdata.getStep());
					switch (gui.agentInfos.get(i).getType()) {
					case "Sain": {
						gui.agentInfos.get(i).getCircle().setFill(Color.valueOf("#1dd1a1"));
						gui.agentInfos.get(i).getCircle().setStroke(Color.valueOf("#7bed9f"));
						if (!Objects.equals(gui.agentInfos.get(i).getpType(), gui.agentInfos.get(i).getType())) {
							gui.recovredAgentInfos.remove(gui.agentInfos.get(i));
							gui.healthyAgentInfos.add(gui.agentInfos.get(i));
						}
						break;
					}
					case "expose": {
						gui.agentInfos.get(i).getCircle().setFill(Color.valueOf("#f39c12"));
						gui.agentInfos.get(i).getCircle().setStroke(Color.valueOf("#7bed9f"));
						break;
					}
					case "malade": {
						gui.agentInfos.get(i).getCircle().setFill(Color.valueOf("#ff6b6b"));
						gui.agentInfos.get(i).getCircle().setStroke(Color.valueOf("#ee5253"));
						if (!Objects.equals(gui.agentInfos.get(i).getpType(), gui.agentInfos.get(i).getType())) {
							gui.exposedAgentInfos.remove(gui.agentInfos.get(i));
							gui.infectedAgentInfos.add(gui.agentInfos.get(i));
						}
						break;
					}
					case "Retablis": {
						gui.agentInfos.get(i).getCircle().setFill(Color.valueOf("#18dcff"));
						gui.agentInfos.get(i).getCircle().setStroke(Color.valueOf("#7bed9f"));
						if (!Objects.equals(gui.agentInfos.get(i).getpType(), gui.agentInfos.get(i).getType())) {
							gui.infectedAgentInfos.remove(gui.agentInfos.get(i));
							gui.recovredAgentInfos.add(gui.agentInfos.get(i));
						}
						break;
					}
					default: {
						break;
					}
					}
					gui.agentInfos.get(i).update();
				} else {
					// "Not Exist"
					Circle circle;
					switch (agentdata.getType()) {
					case "Sain": {
						gui.healthyAgentInfos.add(agentdata);
						circle = new Circle(agentdata.getCoordinateX(), agentdata.getCoordinateY(),
								agentdata.getRaduis(), Color.valueOf("#1dd1a1"));
						circle.setStroke(Color.valueOf("#7bed9f"));
						circle.setStrokeWidth(10);
						break;
					}
					case "malade": {
						gui.infectedAgentInfos.add(agentdata);
						circle = new Circle(agentdata.getCoordinateX(), agentdata.getCoordinateY(),
								agentdata.getRaduis(), Color.valueOf("#ff6b6b"));
						circle.setStroke(Color.valueOf("#ee5253"));
						circle.setStrokeWidth(10);
						break;
					}
					case "Retablis": {
						gui.recovredAgentInfos.add(agentdata);
						circle = new Circle(agentdata.getCoordinateX(), agentdata.getCoordinateY(),
								agentdata.getRaduis(), Color.valueOf("#18dcff"));
						circle.setStroke(Color.valueOf("#7bed9f"));
						circle.setStrokeWidth(10);
						break;
					}
					default: {
						throw new IllegalStateException("Unexpected value: " + agentdata.getType());
					}
					}
					agentdata.setCircle(circle);
					gui.agentInfos.add(agentdata);
					System.out.println(agentdata.getAid().getLocalName() + " is added");
					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							MyContainer.simulator.getChildren().add(agentdata.getCircle());
						}
					};
					Objects.requireNonNull(runnable, "runnable");
					if (Platform.isFxApplicationThread()) {
						runnable.run();
					} else {
						Platform.runLater(runnable);
					}
				}
				/**
				 * System.out.println("\n---------------------------------------------------------------------------\n");
				 * System.out.println("|||- Number of Healthy agents are: " +
				 * gui.healthyAgentInfos.size() + " -|||");
				 * System.out.println("|||- Number of Exposed agents are: " +
				 * gui.exposedAgentInfos.size() + " -|||");
				 * System.out.println("|||- Number of Infected agents are: " +
				 * gui.infectedAgentInfos.size() + " -|||");
				 * System.out.println("|||- Number of Recovered agents are: " +
				 * gui.recovredAgentInfos.size() + " -|||");
				 * System.out.println("\n---------------------------------------------------------------------------\n");
				 **/
			}

	}

}
