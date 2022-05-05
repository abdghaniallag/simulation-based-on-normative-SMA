package RewardPunishment;

import agentpack.individual;
import jade.core.behaviours.OneShotBehaviour;

public class AmendeRewardPunishment extends OneShotBehaviour{
	private individual agent;
	public int amende=300;
	public AmendeRewardPunishment(individual agent) {
        this.agent = agent;
    }
	@Override
	public void action() {
		System.out.println("punishment -300");
		agent.myData.setCompte(agent.myData.getCompte() - amende);

	}

}
