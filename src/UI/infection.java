package UI;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import start.MyContainer;
import util.agentData;
import util.Heading;

import java.util.Objects;

public class infection extends CyclicBehaviour {
	EnvirennementGui gui;

	public infection(EnvirennementGui gui) {
		this.gui = gui;
	}

	@Override
	public void action() {
		if (!MyContainer.fileimport) {
			for (agentData p : gui.agentInfos) {
				for (agentData q : gui.agentInfos) {
					if (p != q && Heading.collide(p, q)) {
						ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
						if (q.getType().equals("malade") && p.getType().equals("Sain")) {
							p.setType("expose");
							msg2.setContent("expose");
							boolean exist = false;
							for (int i = 0; i < gui.exposedAgentInfos.size(); i++) {
								if (Objects.equals(gui.exposedAgentInfos.get(i), p)) {
									exist = true;
									break;
								}
							}
							if (!exist) {
								gui.exposedAgentInfos.add(p);
								gui.healthyAgentInfos.remove(p);
							}
							msg2.addReceiver(new AID(p.getAid().getLocalName(), AID.ISLOCALNAME));
						} else {
							if (q.getType().equals("Sain") && p.getType().equals("malade")) {
								q.setType("expose");
								msg2.setContent("expose");
								boolean exist = false;
								for (int i = 0; i < gui.exposedAgentInfos.size(); i++) {
									if (Objects.equals(gui.exposedAgentInfos.get(i), q)) {
										exist = true;
										break;
									}
								}
								if (!exist) {
									gui.exposedAgentInfos.add(q);
									gui.healthyAgentInfos.remove(q);
								}
								msg2.addReceiver(new AID(q.getAid().getLocalName(), AID.ISLOCALNAME));
							} else {
								msg2.setContent("contact");
							}
						}
						gui.send(msg2);
					}
				}
			}
			// senario
		} else {
			for (agentData p : gui.agentInfos) {
				for (agentData q : gui.agentInfos) {
					if (p != q && Heading.collide(p, q)) {
						ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
						if (q.getType().equals("malade") && p.getType().equals("Sain")) {
							p.setType("expose");
							if (p.getFamily().equals(q.getFamily()) || q.getFamily().equals(p.getFamily())) {
								msg2.setContent("exposeI");
							} else {
								msg2.setContent("exposeE");
							}
							msg2.addReceiver(new AID(p.getAid().getLocalName(), AID.ISLOCALNAME));
							gui.send(msg2);
							boolean exist = false;
							for (int i = 0; i < gui.exposedAgentInfos.size(); i++) {
								if (Objects.equals(gui.exposedAgentInfos.get(i), p)) {
									exist = true;
									break;
								}
							}
							if (!exist) {
								gui.exposedAgentInfos.add(p);
								gui.healthyAgentInfos.remove(p);
							}
						} else {
							if (q.getType().equals("Sain") && p.getType().equals("malade")) {
								q.setType("expose");
								if (p.getFamily().equals(q.getFamily()) || q.getFamily().equals(p.getFamily())) {
									msg2.setContent("exposeI");
								} else {
									msg2.setContent("exposeE");
								}
								msg2.addReceiver(new AID(q.getAid().getLocalName(), AID.ISLOCALNAME));
								gui.send(msg2);
								boolean exist = false;
								for (int i = 0; i < gui.exposedAgentInfos.size(); i++) {
									if (Objects.equals(gui.exposedAgentInfos.get(i), q)) {
										exist = true;
										break;
									}
								}
								if (!exist) {
									gui.exposedAgentInfos.add(q);
									gui.healthyAgentInfos.remove(q);
								}

							} else {
								if (!p.getFamily().equals(q.getFamily())) {
									msg2.setContent("contact");
									msg2.addReceiver(new AID(p.getAid().getLocalName(), AID.ISLOCALNAME));
									gui.send(msg2);
									msg2.addReceiver(new AID(q.getAid().getLocalName(), AID.ISLOCALNAME));
									gui.send(msg2);

								}

							}
						}
					}
				}
			}
		}

	}
}
