package NorJadeAspects;

import java.util.Vector;
import jade.core.Agent;

public aspect ListOfAgents {
	
	public static Vector<Agent> Agents = new Vector<Agent>();
	
	public pointcut AddAgent() : execution(void setup());
	public pointcut DeleteAgent() : call (* *.doDelete());

	before() : AddAgent() {
		Agent a = (Agent) thisJoinPoint.getTarget();
		if (a.getLocalName().indexOf("ams") == -1 && a.getLocalName().indexOf("df") == -1 ) {
				Agents.addElement(a);
		}
	}
	after() : DeleteAgent(){
		String agent = ((Agent) thisJoinPoint.getTarget()).getLocalName();
		 for(int i = 0 ; i < Agents.size() ; i++){
			 if(Agents.get(i).getLocalName().equals(agent)){
				 Agents.remove(i);
			 }
		 }		 
	}

   static public Agent getAgent(String Name){
	   int i = 0 ; 
	   Agent TheAgent = null ;
	   
	   while (i < Agents.size() && !Agents.elementAt(i).getLocalName().equals(Name))
		   i ++ ;
	   
	   if (i < Agents.size()) 
		   TheAgent = Agents.elementAt(i) ;
	   
	   
	   return TheAgent ;
   }

}
