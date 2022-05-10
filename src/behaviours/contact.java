package behaviours;

import agentpack.individual;
import jade.core.behaviours.OneShotBehaviour;

public class contact extends OneShotBehaviour{
	private individual agent;
	public contact(individual agent) {
        this.agent = agent;
    }
	@Override
	public void action() {
		System.out.println("touche");
		// TODO Auto-generated method stub
		
	}

}
