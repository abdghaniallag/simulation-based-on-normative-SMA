package NorJadeAspects;

import jade.core.behaviours.*;
import jade.core.Agent;
import java.io.IOException;


public aspect ProceduralNorm {
	
	public void Agent.ReachGoal(String GoalName){

		String[] ProceduralBehaviour = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getBehaviour(GoalName);
		String ExecuterAgent = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getExecuterAgent(ProceduralBehaviour[0]);
		String ClassOfExecuterAgent = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getAgentClass(ExecuterAgent);
	if(this.IsConcernedByLaw(ExecuterAgent, ClassOfExecuterAgent)){
		if(ProceduralBehaviour[1].equals("IndividualGoal")){
		try {
			String TheBehaviourName = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getBehaviourName(ProceduralBehaviour[0]);
			Behaviour b = (Behaviour) Class.forName(TheBehaviourName).newInstance();
            System.out.println("[NorJADE Framework] : The agent : "+ this.getLocalName() + "  will execute the behaviour : " + TheBehaviourName + " to reach the goal : "+ GoalName);
			this.addBehaviour(b);
		}
	    catch (Exception e) {
		e.printStackTrace();
	     }
       }
	}
	else{
		
		System.out.println("[NorJADE Framework] : The agent : "+ this.getLocalName() + "  can not reach the goal "+ GoalName +" because it is associated to a behaviour that can not execute");
	}
   }
}
