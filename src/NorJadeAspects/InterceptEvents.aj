package NorJadeAspects;

import jade.core.Agent;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public aspect InterceptEvents {
	
	private pointcut VariableUpdating(boolean arg) : ( set(boolean ressources.Conference.DeadlineReached ) || 
	                                   set(boolean ressources.Conference.NotificationReached) ||
	                                   set(boolean ressources.Conference.DispatchingReached) ||
	                                   set(boolean ressources.Conference.ReviewingReached) )
	                                    && args(arg);

	private pointcut ReceivingMessage() : call(* Agent.receive()) ;
	
	after(boolean arg): VariableUpdating(arg) {
			if( arg == true ){
		   System.out.println("[NorJADE Framework] : The event : " + thisJoinPoint.getSignature().getName() + " is intercepted " );
		   Event InterceptedEvent = new Event();
		   InterceptedEvent.setVariableName(new String(thisJoinPoint.getSignature().getDeclaringTypeName()+"."+thisJoinPoint.getSignature().getName()));
		   InterceptedEvent.setmyObject(thisJoinPoint.getTarget());
		   InterceptedEvent.setEventType(0);
		   EventProcessing(InterceptedEvent);
			}
	}
	
	private void SavingEvents(Event E){
		
		String EventIsImportantFor = E.getImportantFor() ;
		String ClassIs = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getAgentClass(EventIsImportantFor);
		
		if(ClassIs.equals("AnyAgent")){
			
			for(int i = 0; i < ListOfAgents.Agents.size(); i++)
				ListOfAgents.Agents.elementAt(i).AddingEvent(E);
		}
		else{
			if(ClassIs.equals("ClassOfAgent")){
				for(int i = 0 ; i < ListOfAgents.Agents.size(); i++){
					if(ListOfAgents.Agents.elementAt(i).getClass().getSimpleName().equals(E.getImportantFor())){
						ListOfAgents.Agents.elementAt(i).AddingEvent(E);
					}
				}
				
			}
			else{
			    Agent Agent1 = ListOfAgents.getAgent(E.getImportantFor());
			    Agent1.AddingEvent(E);
			}
		    Agent Trigger = ListOfAgents.getAgent(E.getTriggeredBy());
		    
		    Trigger.AddingEvent(E);

		}
		
	}
	
	private void EventProcessing(Event InterceptedEvent) {
		
		String ResultEvent = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEvent(InterceptedEvent.getEventId(), InterceptedEvent.getEventType()) ;
		
		if (ResultEvent != null) {
		   String[] ResultEventProperties = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEventProperties(ResultEvent);
		   Agent Enforcer = ListOfAgents.getAgent(NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEnforcer(ResultEventProperties[4]));
		   //if the intercepted Event is a start one
		   if(ResultEventProperties[3].equals("StartEvent")){
			    InterceptedEvent.setTriggeredBy(ResultEventProperties[0]);
			    InterceptedEvent.setImportantFor(ResultEventProperties[2]);
			    InterceptedEvent.setAssociatedLaw(ResultEventProperties[4]);
			    InterceptedEvent.setAssociatedBehaviour(ResultEventProperties[5]);
			    String[] LawValidityPropoerties = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLawValidity(InterceptedEvent.getAssociatedLaw());
			    SavingEvents(InterceptedEvent);
			    Agent Trigger = ListOfAgents.getAgent(InterceptedEvent.getTriggeredBy());

			     // Test if the validity is related to a time duration specified by a scope
			     if((LawValidityPropoerties.length > 2) && (LawValidityPropoerties[2] != null) ){
               	      try {
				         if (Integer.parseInt(LawValidityPropoerties[2]) >= 0) {
				         	ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
				         	scheduler.schedule(new Runnable() {
						     @Override
						        public void run() {
			                     String BehaviourName = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getBehaviourName(InterceptedEvent.getAssociatedBehaviour()); 
			         			 String[] Law =  NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLaw(BehaviourName); 
						    	 String[] LawPropoerties =  NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLawProperties(InterceptedEvent.getAssociatedLaw());
						    	 if (Law[2].equals("Obligation")){
						    		 System.out.println("[NorJADE Framework] : The specified scope to execute the behaviour : " + BehaviourName + " is finished. We will test either this behaviour is executed or not.");
						    		 String AgentName = Law[0] ;
						    		 String AgentClassName = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getAgentClass(AgentName);
						    		 Trigger.BehaviourExecutionTesting(InterceptedEvent.getAssociatedBehaviour(), AgentName, AgentClassName, LawPropoerties, Enforcer);
						    	 }

						     }
				       	}, Integer.parseInt(LawValidityPropoerties[2]) * 1000, TimeUnit.MILLISECONDS);
			       	}
			     } catch (NumberFormatException e) {
		    	}
		      }
				
			// end if intercepted event is start one
		}
		else{
			//The intercepted event is end event
			 InterceptedEvent.setTriggeredBy(ResultEventProperties[0]);
			 InterceptedEvent.setImportantFor(ResultEventProperties[2]);
			 InterceptedEvent.setAssociatedLaw(ResultEventProperties[4]);
			 InterceptedEvent.setAssociatedBehaviour(ResultEventProperties[5]);
			 String[] LawValidityPropoerties = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLawValidity(InterceptedEvent.getAssociatedLaw());
			 SavingEvents(InterceptedEvent);
			 Agent Trigger = ListOfAgents.getAgent(InterceptedEvent.getTriggeredBy());
		     if((LawValidityPropoerties.length > 2) && (NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEventId(LawValidityPropoerties[1]).equals(InterceptedEvent.getEventId())) ){
                  if(Trigger.IsExistEvent(NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getEventId(LawValidityPropoerties[0])) != -1){
                     String BehaviourName = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getBehaviourName(InterceptedEvent.getAssociatedBehaviour()); 
         			 String[] Law =  NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLaw(BehaviourName); 
        			 String[] LawPropoerties =  NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getLawProperties(InterceptedEvent.getAssociatedLaw());
        			 if (Law[2].equals("Obligation")){				    		 
    		    		 String AgentName = Law[0] ;
    		    		 String AgentClassName = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getAgentClass(AgentName);
			    		 System.out.println("[NorJADE Framework] : The end event : " + InterceptedEvent.getEventId() + " is intercepted. We will test either the obligate behaviour :  "+BehaviourName+ " is executed or not.");
    		    		 Trigger.BehaviourExecutionTesting(InterceptedEvent.getAssociatedBehaviour(), AgentName, AgentClassName, LawPropoerties, Enforcer);
    		    	 //end case of obligation deontic operator 
        			 }
        			// end case of existence of start event 
                  }
		     }		 
	    
			//end of processing end event			
		}
		
		// end case event != null
		}
		
	}
		


}
