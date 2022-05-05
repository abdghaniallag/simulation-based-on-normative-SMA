package NorJadeAspects;

import java.util.Vector;

import org.apache.jena.ontology.Individual;

import agentpack.individual;
import jade.core.Agent;

import java.io.IOException;
import java.lang.reflect.Method;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour; 
import jade.lang.acl.ACLMessage; 


public aspect ExtendingAgent {
	
	// This aspect is used to extend the Jade's Agent by some specific structure and functions
	
	// This vector is used to save a list of Executed behaviours for each agent 
	public Vector <String[]> Agent.ExecutedBehaviours = new Vector<String[]>();
	public Vector <Event> Agent.InterceptedEvent = new Vector();
	
	public void Agent.SelfEnforcementApplication (String Punishment , String type){
		//This method is used for self punishment for both cases (punishment is method or a behaviour)
		try {	
			/* Case of punishment is method */
			if (type.equals("Method")) {
				System.out.println("[NorJADE Framework] : The method : " + Punishment + "  will be executed by the agent "+this.getLocalName()+ " as Reward/Punishment");
				Class c = Class.forName(this.getClass().getName());
				Method m = c.getDeclaredMethod(Punishment);
				m.invoke(this);
			} else { /*Case of punishment is behaviour  */
				System.out.println("[NorJADE Framework] : The behaviour : " + Punishment  + "  will be scheduled by the agent "+this.getLocalName()+ "  as Reward/Punishment");
				Behaviour b = (Behaviour) Class.forName(Punishment).getConstructor(individual.class ).newInstance((individual) this);
				this.addBehaviour(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void Agent.ThirdPartyEnforcement(Agent agent ,String Punishment){
		// In this method the enforcer request the execution of a behaviour by an agent
//		System.out.println("The enforcer : " + this.getLocalName() + " I will request the execution of the punishment : " + Punishment + " by  the agent : " + agent.getLocalName());
		Agent Enforcer = (Agent) this;
		Enforcer.addBehaviour(new OneShotBehaviour(){
			public void action(){
					ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
					message.setSender(Enforcer.getAID());
					message.setOntology(new String("NorJadeOntology"));
					Behaviour b = null ;
					try { 
						b = (Behaviour) Class.forName(Punishment).newInstance();
					}
			          catch (Exception e) {
				     e.printStackTrace();
			          }
					
					try {message.setContentObject(b);
					
					}catch(IOException ie){
						ie.printStackTrace();
						
					}	
			        message.addReceiver(new AID(agent.getLocalName(), AID.ISLOCALNAME)); 
			        this.myAgent.send(message);
			        System.out.println("[NorJADE Framework] : The agent : " + this.myAgent.getLocalName() + " has requested the execution of the behaviour " + Punishment + " by the agent " + agent.getLocalName() + " as a Reward/Punishment");
				 
			 }
		});
		
	}
	
	
	
	public void Agent.AddingEvent(Event E){
		this.InterceptedEvent.addElement(E) ; 
	}
	
	public void Agent.RemovingEvent(Event E){
         this.InterceptedEvent.remove(E);
		
	}
	
	public int Agent.IsExistEvent(String EventId){
		int i = 0 ; 
		boolean found = false ;
		
		while (i < this.InterceptedEvent.size() && !found){
			if(! this.InterceptedEvent.elementAt(i).getEventId().equals(EventId))
				i++ ;
			else
				found = true;
		}
		
		if(! found )
			i = -1 ;
		
		return i ;
	}
	
	public boolean Agent.IsExecutedBehaviour(String B){
		boolean found = false ;
		int i = 0 ; 
		while (i < this.ExecutedBehaviours.size() && !found) {
			if (this.ExecutedBehaviours.elementAt(i)[0].equals(B)) 
				found = true;
			else 
				i++ ;
		}
		return found; 
	}
	
	public int Agent.IsExecutedBehaviour(String BehaviourName, String AgentName){
		int i = 0 ; 
		boolean found = false ;
		BehaviourName = "behaviours."+BehaviourName; 
		while (i < this.ExecutedBehaviours.size() && !found){
			if (this.ExecutedBehaviours.elementAt(i)[0].equals(BehaviourName) && this.ExecutedBehaviours.elementAt(i)[1].equals(AgentName))
				found = true ;
			else 
				i++ ;
		}
		
		if(! found){
			i = -1;
		}
		
		return i ;
	}
	public boolean Agent.IsConcernedByLaw(String AgentName, String ClassName){
		boolean result = false; 
		switch(ClassName){
		case "AnyAgent" :{
			result = true; 
			break;
		}
		case "ClassOfAgent" :{
			if (this.getClass().getSimpleName().equals(AgentName))
				result = true;
			break;
		}
		case "SpecificAgent" :{
			if (this.getLocalName().equals(AgentName))
				result = true ;
			break;
		}
		}
		return result; 
	}
	
	public boolean Agent.IsValidLaw(String Law){
		boolean result = false ;
		String[] ValidityProperties = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLawValidity(Law);
		
		if(ValidityProperties.length == 1 ) {
			try {
			Class c = Class.forName(this.getClass().getName());
			Method m = c.getDeclaredMethod(ValidityProperties[0]);
			result = (Boolean) m.invoke(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		else{
			if((ValidityProperties.length == 2)){
				 if (ValidityProperties[0].equals("UnlimitedDuration")) {
					 // case of UnlimitedDuration Validity
				result = true ;
				 }
				 else{
					 //Case of bounded validity (exist start event but the end event is indefinitely
					 if (this.IsExistEvent(NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEventId(ValidityProperties[0])) != -1 )
					 result = true ;
					 else
					 result = false ;	 
				 }
			}
			else{
				//Case of validityProperties.length > 2. In this case there are start event and end event or scope
				 if(ValidityProperties[2] != null){
					 //case of end limitation specified by a scope
					 int j = this.IsExistEvent(NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEventId(ValidityProperties[0]))  ;
					 if(j != -1 ){
						long T = System.currentTimeMillis() - this.InterceptedEvent.elementAt(j).getOccurrenceTime() ;
						if (T < Integer.parseInt(ValidityProperties[2]) )
							result = true ;
						else
							result = false ;
					 }
					 else{
						 result = false;
					 }

				 }
				 else{
					 int j = this.IsExistEvent(NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEventId(ValidityProperties[0]))  ;
					 if(j != -1){
					     int k = this.IsExistEvent(NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEventId(ValidityProperties[1]))  ;
                       if(k != -1)
                    	   result = false ;
                       else
                    	   result = true;
					 }
					 else
						 result = false ;
					 
				 }
				
				
			}
				
		}
		
		return result ;
	}

	
	public synchronized  void Agent.BehaviourExecutionTesting(String BehaviourName, String AgentName, String AgentClass, String [] Law, Agent Enforcer){
	  
		switch(AgentClass){
		case "AnyAgent" :{
			for(int i = 0 ; i < ListOfAgents.Agents.size(); i ++){
				int j = this.IsExecutedBehaviour(BehaviourName, ((Agent)ListOfAgents.Agents.elementAt(i)).getLocalName()) ;
				if (  j != -1){
					//This agent executed the behaviour
					this.ExecutedBehaviours.remove(j);
				}
				else{
					//This agent did not execute the behaviour, we will execute the punishment
				    if (Law[2].equals("SelfEnforcer")){
				    	((Agent)ListOfAgents.Agents.elementAt(i)).SelfEnforcementApplication(Law[1], Law[3]);
				   
				    }
				    else{
				    	Enforcer.ThirdPartyEnforcement((Agent)ListOfAgents.Agents.elementAt(i), Law[1]);
				    }
				    }
			}
			
			break;
		}
		case "SpecificAgent":{
			int j = this.IsExecutedBehaviour(BehaviourName, AgentName) ;
			if (  j != -1){
				//This agent executed the behaviour
				this.ExecutedBehaviours.remove(j);
			}
			else{
				//This agent did not execute the behaviour, we will execute the punishment
		    	Agent agent = ListOfAgents.getAgent(AgentName);
				if (Law[2].equals("SelfEnforcer")){
					agent.SelfEnforcementApplication(Law[1], Law[3]);
				}
			    else{
			    	Enforcer.ThirdPartyEnforcement(agent, Law[1]);
			    }
			}
			break ;
		}
		case "ClassOfAgent" :{
			
			for(int i = 0 ; i < ListOfAgents.Agents.size(); i ++){
				if (((Agent)ListOfAgents.Agents.elementAt(i)).getClass().getSimpleName().equals(AgentName)){
				int j = this.IsExecutedBehaviour(BehaviourName, ((Agent)ListOfAgents.Agents.elementAt(i)).getLocalName()) ;
				if (  j != -1){
					//This agent executed the behaviour
					this.ExecutedBehaviours.remove(j);
				}
				else{
					//This agent did not execute the behaviour, we will execute the punishment
				    if (Law[2].equals("SelfEnforcer")){
				    	((Agent)ListOfAgents.Agents.elementAt(i)).SelfEnforcementApplication(Law[1], Law[3]);
				    }
				    else{
				    	Enforcer.ThirdPartyEnforcement((Agent)ListOfAgents.Agents.elementAt(i), Law[1]);
				    }
				  }
				}
			}
			break ;
		}
		}
	
	}
	

}
