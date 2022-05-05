package UI;

import java.io.IOException;

import agentpack.individual;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import start.MyContainer;

public class DataSending extends TickerBehaviour {

	individual agent;

	public DataSending(Agent agent, long period) {

		super(agent, period);
		this.agent = (individual) agent;

	}

	@Override
	protected void onTick() {

		ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
		msg2.addReceiver(new AID("EnvirennementGui", AID.ISLOCALNAME));

		if (agent != null)
			try {

				msg2.setContentObject(agent.myData);
				agent.send(msg2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
}
