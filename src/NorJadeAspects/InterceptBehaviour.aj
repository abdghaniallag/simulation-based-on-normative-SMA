package NorJadeAspects;

import java.util.Vector;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public aspect InterceptBehaviour {
	
	public pointcut BehaviourExecuting() : execution(void *.action());

	Object around() : BehaviourExecuting() {
		Object ExectuteInterceptedMethod = null ;
		
		String BehaviourNameClass = thisJoinPoint.getTarget().getClass().getName();
		String [] RegulatedBehaviour = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLaw(BehaviourNameClass);
		Agent agent = ((Behaviour) thisJoinPoint.getTarget()).getAgent();

		if(RegulatedBehaviour != null) {
			String Law = RegulatedBehaviour[1] ;
			String BehaviourExecutedBy = RegulatedBehaviour[0] ;
			String DeonticOperator = RegulatedBehaviour[2] ;
			String [] TheLawProperties = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLawProperties(Law) ;
			String ClassName = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getAgentClass(BehaviourExecutedBy) ;
			//The behaviour is controled by a norm
    	   if(TheLawProperties[4].equals("Regimentation")){
    		    //The norm mechanism is Regimentation
    		   if(agent.IsConcernedByLaw(BehaviourExecutedBy, ClassName)){
    			   if(agent.IsValidLaw(Law)){
    				   ExectuteInterceptedMethod = null ;
    				   ((Behaviour)thisJoinPoint.getTarget()).BehaviourAborted = true ;
    				   System.out.println("[NorJADE Framework] : The agent : " + agent.getLocalName() + "  Can not execute the behaviour  " + BehaviourNameClass + "  because it is regulated by a regimentation mechanism. Law : " + Law  );
    				   System.out.println("[NorJADE Framework] : The behaviour : " + BehaviourNameClass + " is aborted " );	   
    			   }
    			   else{
    				   //The law is not valid
    				   ExectuteInterceptedMethod = proceed() ; 
    			   }
      		   }
    		   else{
    			   //The agent is not concerned by the law
				   ExectuteInterceptedMethod = proceed() ;    
    		   }
    	   }
    	   else{
    		   //The norm mechanism is Enforcement
			   ExectuteInterceptedMethod = proceed() ; 
			// We will process the behaviour according to the deontic operator
    	   switch (DeonticOperator){
    	           
    	   case "Prohibition":{
    		   
    		   if(agent.IsConcernedByLaw(BehaviourExecutedBy, ClassName)){
    			   
    			   if(agent.IsValidLaw(Law)){
    				   if(TheLawProperties[2].equals("SelfEnforcer")){
        				   System.out.println("[NorJADE Framework] : The behaviour : " + BehaviourNameClass + " is regulated by the law :" + Law + "  with the deontic operator : Prohibition"  );
        				   System.out.println("[NorJADE Framework] : A self punishment will be executed");
        				   agent.SelfEnforcementApplication(TheLawProperties[1], TheLawProperties[3]) ;
    				   }
    				   else{
        				   System.out.println("[NorJADE Framework] : The behaviour : " + BehaviourNameClass + " is regulated by the law :" + Law + "  with the deontic operator : Prohibition"  );
        				   System.out.println("[NorJADE Framework] : A third party punishment will be executed");
    					   String EnforcerName = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEnforcer(Law);
    					   Agent Enforcer = ListOfAgents.getAgent(EnforcerName);
    					   Enforcer.ThirdPartyEnforcement(agent,TheLawProperties[1] );
    				   }
    			   }
    			   
    		   }
    		   
    	   break;
    	   }
    	   case "Obligation":{
			   System.out.println("[NorJADE Framework] : The behaviour : " + BehaviourNameClass + " is regulated by the law :" + Law + "  with the deontic operator : Obligation"  );
			   System.out.println("[NorJADE Framework] : The behaviour" + BehaviourNameClass +" will be saved for further processing");
			   String TheEnforcerName = TheLawProperties[2] ; 
    		   SaveObligatoryBehaviours(BehaviourNameClass, agent, TheEnforcerName) ;
    		 
    		break;   
    	   }
    	   case "Recommendation":{
    		   
    		   if(agent.IsConcernedByLaw(BehaviourExecutedBy, ClassName)){
    			   
    			   if(agent.IsValidLaw(Law)){
    				   if(TheLawProperties[2].equals("SelfEnforcer")){
        				   System.out.println("[NorJADE Framework] : The behaviour : " + BehaviourNameClass + " is regulated by the law :" + Law + "  with the deontic operator : Recommendation"  );
        				   System.out.println("[NorJADE Framework] : A self reward will be executed");

    					   agent.SelfEnforcementApplication(TheLawProperties[1], TheLawProperties[3]) ;
    				   }
    				   else{
        				   System.out.println("[NorJADE Framework] : The behaviour : " + BehaviourNameClass + " is regulated by the law :" + Law + "  with the deontic operator : Recommendation"  );
        				   System.out.println("[NorJADE Framework] : A third party reward will be executed");

    					   String EnforcerName = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEnforcer(Law);
    					   Agent Enforcer = ListOfAgents.getAgent(EnforcerName);
    					   Enforcer.ThirdPartyEnforcement(agent,TheLawProperties[1] );
    				   }
    			   }
    			   
    		   }

    		   
    	   break;
    	   }    	   
    	   //End switch regulatedBehaviour[1]
    	   }
    	   // End else TheLawProperties[4].equals("Regimentation")
    	  }
       // End if of RegultedBehaviour != null
       }
		else{
			
			//The behaviour is not guided by norms
			   ExectuteInterceptedMethod = proceed() ; 
			
		}
		
		return ExectuteInterceptedMethod ;
       		
		
	}

	
	public void SaveObligatoryBehaviours(String B, Agent agent, String enforcer){
		
		if (enforcer.equals("SelfEnforcer")){
			agent.ExecutedBehaviours.add(new String[] { B, agent.getLocalName() });

		}
		else{
			
			Agent TheEnforcer = ListOfAgents.getAgent(enforcer);
			TheEnforcer.ExecutedBehaviours.add(new String[] { B, agent.getLocalName() });

		}
		
	}

}
