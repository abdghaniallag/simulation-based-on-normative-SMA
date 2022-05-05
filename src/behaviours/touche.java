package behaviours;

import agentpack.individual;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import start.MyContainer;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class touche extends CyclicBehaviour {
	individual agent;

	public touche(individual agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		if (!MyContainer.fileimport) {
			Timer timer = new Timer();
			ACLMessage msg = agent.receive();
			if (msg != null) {
				if (msg.getContent().equals("expose")) {
					agent.addBehaviour(new contact(agent));
					agent.myData.setType(msg.getContent());
					timer.scheduleAtFixedRate(new TimerTask() {
						int i = 15;

						public void run() {
							i--;
							if (i == 8) {
								agent.myData.setType("malade");
							}
							if (i == 5) {
								agent.myData.setType("Retablis");
							}
							if (i == 0) {
								agent.myData.setType("Sain");
								timer.cancel();
							}
						}
					}, 0, 1000);
				} else {
					agent.addBehaviour(new contact(agent));
				}

			}

		} else {
			Timer timer = new Timer();
			ACLMessage msg = agent.receive();
			if (msg != null) {
				switch (msg.getContent()) {
				case "exopseI": {
					agent.myData.setType("expose");
					// System.out.println("exposeI "+agent.getLocalName());
					timer.scheduleAtFixedRate(new TimerTask() {
						int i = 15;

						public void run() {
							i--;
							if (i == 8) {
								agent.myData.setType("malade");

							}
							if (i == 5) {

								agent.myData.setType("Retablis");

							}
							if (i == 0) {

								agent.myData.setType("Sain");

								timer.cancel();
							}
						}
					}, 0, 1000);
					break;
				}
				case "exposeE": {
					agent.addBehaviour(new contact(agent));
					// System.out.println("exposeE "+agent.getLocalName());
					agent.myData.setType("expose");
					timer.scheduleAtFixedRate(new TimerTask() {
						int i = 15;

						public void run() {
							i--;
							if (i == 8) {
								agent.myData.setType("malade");
							}
							if (i == 5) {
								agent.myData.setType("Retablis");
							}
							if (i == 0) {
								agent.myData.setType("Sain");
								timer.cancel();
							}
						}
					}, 0, 1000);
					break;
				}
				case "contact": {
					// System.out.println("contact "+agent.getLocalName());
					agent.addBehaviour(new contact(agent));
					break;
				}
				}
			}
		}

	}
}
