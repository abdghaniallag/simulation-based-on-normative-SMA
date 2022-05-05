package NorJadeAspects;

import jade.core.behaviours.Behaviour;

public aspect ExtendingBehaviour {
	
	public boolean Behaviour.BehaviourAborted = false ;
	
	public pointcut BehaviourDone() : execution(* *.done());
	
	
	boolean around() : BehaviourDone() {
		
		boolean ret = false ;
		if(((Behaviour)thisJoinPoint.getTarget()).BehaviourAborted ){
			
			ret = true;
			System.out.println("[NorJADE Framework] : The behaviour : " + ((Behaviour)thisJoinPoint.getTarget()).getBehaviourName() + " is aborted ");
		}
		else{
			ret = proceed();
		}
		return ret ;

	}


}
