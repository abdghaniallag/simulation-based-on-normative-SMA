package behaviours;

import agentpack.individual;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import start.MyContainer;
import util.Heading;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class createAgent extends OneShotBehaviour {
	individual agent;

	public createAgent(individual agent) {
		this.agent = agent;
	}

	@Override
	public void action() {
		Timer timer = new Timer();
		System.out.println(
				"|||- Im person :" + this.getAgent().getLocalName() + "  type : " + agent.myData.getType() + " -|||");
		if (!MyContainer.fileimport) {
			Heading heading = new Heading();
			boolean verified = false;
			boolean vr1 = false;
			boolean vr2 = false;
			boolean vr3 = false;
			boolean vr4 = false;
			while (!verified) {
				if (heading.getCordinateX() - agent.myData.getRaduis() < 0
						|| heading.getCordinateY() - agent.myData.getRaduis() < 0
						|| heading.getCordinateX() + agent.myData.getRaduis() > 700
						|| heading.getCordinateY() + agent.myData.getRaduis() > 700) {
					heading = new Heading();
				} else {
					for (int i = heading.getCordinateX(); i >= heading.getCordinateX()
							- agent.myData.getRaduis(); i--) {
						for (int j = heading.getCordinateY(); j >= heading.getCordinateY()
								- agent.myData.getRaduis(); j--) {
							if (MyContainer.occupation[i][j] != null) {
								heading = new Heading();
								break;
							}
							if (i == heading.getCordinateX() - agent.myData.getRaduis()
									&& j == heading.getCordinateY() - agent.myData.getRaduis()
									&& MyContainer.occupation[i][j] == null) {
								vr1 = true;
							}
						}
					}
					for (int i = heading.getCordinateX(); i <= heading.getCordinateX()
							+ agent.myData.getRaduis(); i++) {
						for (int j = heading.getCordinateY(); j <= heading.getCordinateY()
								+ agent.myData.getRaduis(); j++) {
							if (MyContainer.occupation[i][j] != null) {
								heading = new Heading();
								break;
							}
							if (i == heading.getCordinateX() + agent.myData.getRaduis()
									&& j == heading.getCordinateY() + agent.myData.getRaduis()
									&& MyContainer.occupation[i][j] == null) {
								vr2 = true;
							}
						}
					}
					for (int i = heading.getCordinateX(); i <= heading.getCordinateX()
							+ agent.myData.getRaduis(); i++) {
						for (int j = heading.getCordinateY(); j >= heading.getCordinateY()
								- agent.myData.getRaduis(); j--) {
							if (MyContainer.occupation[i][j] != null) {
								heading = new Heading();
								break;
							}
							if (i == heading.getCordinateX() + agent.myData.getRaduis()
									&& j == heading.getCordinateY() - agent.myData.getRaduis()
									&& MyContainer.occupation[i][j] == null) {
								vr3 = true;
							}
						}
					}
					for (int i = heading.getCordinateX(); i >= heading.getCordinateX()
							- agent.myData.getRaduis(); i--) {
						for (int j = heading.getCordinateY(); j <= heading.getCordinateY()
								+ agent.myData.getRaduis(); j++) {
							if (MyContainer.occupation[i][j] != null) {
								heading = new Heading();
								break;
							}
							if (i == heading.getCordinateX() - agent.myData.getRaduis()
									&& j == heading.getCordinateY() + agent.myData.getRaduis()
									&& MyContainer.occupation[i][j] == null) {
								vr4 = true;
							}
						}
					}
					if (vr1 && vr2 && vr3 && vr4) {
						verified = true;
					}
				}

			}
			agent.myData.setCoordinateX(heading.getCordinateX());
			agent.myData.setHomeX(agent.myData.getCoordinateX());
			agent.myData.setCoordinateY(heading.getCordinateY());
			agent.myData.setHomeY(agent.myData.getCoordinateY());
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID("EnvirennementGui", AID.ISLOCALNAME));
			for (int i = agent.myData.getCoordinateX(); i >= agent.myData.getCoordinateX()
					- agent.myData.getRaduis(); i--) {
				for (int j = agent.myData.getCoordinateY(); j >= agent.myData.getCoordinateY()
						- agent.myData.getRaduis(); j--) {
					MyContainer.occupation[i][j] = agent.myData;
				}
			}
			for (int i = agent.myData.getCoordinateX(); i <= agent.myData.getCoordinateX()
					+ agent.myData.getRaduis(); i++) {
				for (int j = agent.myData.getCoordinateY(); j <= agent.myData.getCoordinateY()
						+ agent.myData.getRaduis(); j++) {
					MyContainer.occupation[i][j] = agent.myData;
				}
			}
			for (int i = agent.myData.getCoordinateX(); i <= agent.myData.getCoordinateX()
					+ agent.myData.getRaduis(); i++) {
				for (int j = agent.myData.getCoordinateY(); j >= agent.myData.getCoordinateY()
						- agent.myData.getRaduis(); j--) {
					MyContainer.occupation[i][j] = agent.myData;
				}
			}
			for (int i = agent.myData.getCoordinateX(); i >= agent.myData.getCoordinateX()
					- agent.myData.getRaduis(); i--) {
				for (int j = agent.myData.getCoordinateY(); j <= agent.myData.getCoordinateY()
						+ agent.myData.getRaduis(); j++) {
					MyContainer.occupation[i][j] = agent.myData;
				}
			}
			try {
				msg.setContentObject(agent.myData);
				agent.send(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (agent.myData.getType().equals("malade")) {
				timer.scheduleAtFixedRate(new TimerTask() {
					int i = 30;

					public void run() {
						i--;
						if (i == 10) {
							agent.myData.setType("Retablis");
						}
						if (i == 0) {
							agent.myData.setType("Sain");
							timer.cancel();
						}
					}
				}, 0, 1000);
			} else {
				if (agent.myData.getType().equals("Retablis")) {
					timer.scheduleAtFixedRate(new TimerTask() {
						int i = 10;

						public void run() {
							i--;
							if (i == 0) {
								agent.myData.setType("Sain");
								timer.cancel();
							}
						}
					}, 0, 1000);
				}
			}
		} else {
			agent.myData.setAid(agent.getAID());
			agent.myData.setCoordinateX(agent.myData.getHomeX());
			agent.myData.setCoordinateY(agent.myData.getHomeY());
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(new AID("EnvirennementGui", AID.ISLOCALNAME));
			for (int i = agent.myData.getCoordinateX(); i >= agent.myData.getCoordinateX()
					- agent.myData.getRaduis(); i--) {
				for (int j = agent.myData.getCoordinateY(); j >= agent.myData.getCoordinateY()
						- agent.myData.getRaduis(); j--) {
					MyContainer.occupation[i][j] = agent.myData;
				}
			}
			for (int i = agent.myData.getCoordinateX(); i <= agent.myData.getCoordinateX()
					+ agent.myData.getRaduis(); i++) {
				for (int j = agent.myData.getCoordinateY(); j <= agent.myData.getCoordinateY()
						+ agent.myData.getRaduis(); j++) {
					MyContainer.occupation[i][j] = agent.myData;
				}
			}
			for (int i = agent.myData.getCoordinateX(); i <= agent.myData.getCoordinateX()
					+ agent.myData.getRaduis(); i++) {
				for (int j = agent.myData.getCoordinateY(); j >= agent.myData.getCoordinateY()
						- agent.myData.getRaduis(); j--) {
					MyContainer.occupation[i][j] = agent.myData;
				}
			}
			for (int i = agent.myData.getCoordinateX(); i >= agent.myData.getCoordinateX()
					- agent.myData.getRaduis(); i--) {
				for (int j = agent.myData.getCoordinateY(); j <= agent.myData.getCoordinateY()
						+ agent.myData.getRaduis(); j++) {
					MyContainer.occupation[i][j] = agent.myData;
				}
			}
			try {
				msg.setContentObject(agent.myData);
				agent.send(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (agent.myData.getType().equals("malade")) {
				timer.scheduleAtFixedRate(new TimerTask() {
					int i = 8;

					public void run() {
						i--;
						if (i == 5) {
							agent.myData.setType("Retablis");
							ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
							msg2.addReceiver(new AID("EnvirennementGui", AID.ISLOCALNAME));

							try {
								msg2.setContentObject(agent.myData);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							agent.send(msg2);

						}
						if (i == 0) {
							agent.myData.setType("Sain");
							ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
							msg2.addReceiver(new AID("EnvirennementGui", AID.ISLOCALNAME));

							try {
								msg2.setContentObject(agent.myData);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							agent.send(msg2);

							timer.cancel();
						}
					}
				}, 0, 1000);
			} else {
				if (agent.myData.getType().equals("Retablis")) {
					timer.scheduleAtFixedRate(new TimerTask() {
						int i = 5;

						public void run() {
							i--;
							if (i == 0) {
								ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
								msg2.addReceiver(new AID("EnvirennementGui", AID.ISLOCALNAME));

								try {
									msg2.setContentObject(agent.myData);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								agent.send(msg2);
								agent.myData.setType("Sain");
								timer.cancel();
							}
						}
					}, 0, 1000);
				}
			}
		}

	}
}
