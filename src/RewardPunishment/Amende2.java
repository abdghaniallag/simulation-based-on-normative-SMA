package RewardPunishment;

import agentpack.individual;
import jade.core.behaviours.OneShotBehaviour;

public class Amende2 extends OneShotBehaviour{
	private individual agent;
	public int amende=600;
	public Amende2(individual agent) {
        this.agent = agent;
    }
	@Override
	public void action() {
		System.out.println("punishment -600");
		agent.myData.setCompte(agent.myData.getCompte() - amende);

	}

}
