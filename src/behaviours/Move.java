package behaviours;

import agentpack.individual;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import start.MyContainer;
import util.Heading;

import java.io.IOException;
import java.util.Random;

public class Move extends OneShotBehaviour {
	individual agent;
	Random r = new Random();

	public Move(individual agent) {
		this.agent = agent;
	}


	@Override
	public void action() {
		if (!MyContainer.fileimport) {
			int coordinateX;
			int coordinateY;
			coordinateX = r.nextInt((int) (700 - agent.myData.getRaduis()));
			if (coordinateX < agent.myData.getRaduis()) {
				coordinateX = (int) (coordinateX + agent.myData.getRaduis());
			}
			coordinateY = r.nextInt((int) (700 - agent.myData.getRaduis()));
			if (coordinateY < agent.myData.getRaduis()) {
				coordinateY = (int) (coordinateY + agent.myData.getRaduis());
			}
			agent.myData.setTargetX(coordinateX);
			agent.myData.setTargetY(coordinateY);
			if (Heading.distance(agent.myData.getHomeX(), coordinateX, agent.myData.getHomeY(),
					coordinateY) <= agent.myData.getDistance()) {
				if (agent.myData.getCompte() > 500) {
					agent.addBehaviour(new MoveNear(agent));
				} else {
					System.out.println("agent can't do this behaviour");
				}
			} else {
				System.out.println("agent can't do MOVEFAR behaviour");
			}
		} else {
			if (agent.myData.getTargets().size() > 0) {
				agent.myData.setTargetX(agent.myData.getTargets().get(0).getCordinateX());
				agent.myData.setTargetY(agent.myData.getTargets().get(0).getCordinateY());
				agent.myData.getTargets().remove(0);
				if (Heading.distance(agent.myData.getHomeX(), agent.myData.getTargetX(), agent.myData.getHomeY(),
						agent.myData.getTargetY()) <= agent.myData.getDistance()) {
					if (agent.myData.getCompte() > 500) {
						
						agent.addBehaviour(new MoveNear(agent));
					} else {
						System.out.println(agent.getLocalName() + " agent can't do this behaviour");
						agent.addBehaviour(new Move(agent));
						 
					}
				} else {
					System.out.println(agent.getLocalName() + " agent can't do MOVEFAR behaviour");
					agent.addBehaviour(new Move(agent));

				}
			}

		}

	}
}
