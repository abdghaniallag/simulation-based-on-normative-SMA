package NorJadeAspects;

public class Event {
	
	private String TriggeredBy; 
	/*
	 * EventType = 0 : UpdatingVariable
	 * EventType = 1 : Method Execution
	 * EventType = 2 : Sending Message
	 * EventType = 3 : Receiving Message
	 */
	private int EventType ;
	private Object myObject ;
	public final int Start = 0;
	public final int End = 1;
	private String AssociatedLaw ;
	private String AssociatedBehaviour ;
	private String VariableName;
	private String ExecutedMethod ;
	private String MessagePerformative ;
	private int EventClass ;
	private long OccurrenceTime ;
	private String ImportantFor ;
	
	public Event(){
		this.OccurrenceTime = System.currentTimeMillis();
	}
	
	public long getOccurrenceTime(){
		return this.OccurrenceTime ;
	}
	
	public String getEventId(){
		String Id = null ;
		switch (this.EventType){
		
		case 0 : {
			 Id = this.VariableName ;
			 break ;
		}
		case 1: {
			Id = this.ExecutedMethod ;
			break ;
		}
		case 2:{
			Id = this.MessagePerformative;
			break;
		}
		}
		
		return Id ;
	}
	
	public String getTriggeredBy(){
		return this.TriggeredBy ;
	}
	
	public void setTriggeredBy(String TriggeredBy){
		 this.TriggeredBy = TriggeredBy ;
	}

	
	public String getAssociatedLaw(){
		return this.AssociatedLaw ;
	}
	
	public void setAssociatedLaw(String AssociatedLaw){
		 this.AssociatedLaw = AssociatedLaw ;
	}
	
	public String getImportantFor(){
		return this.ImportantFor ;
	}
	
	public void setImportantFor(String ImportantFor){
		 this.ImportantFor = ImportantFor ;
	}


	public String getAssociatedBehaviour(){
		return this.AssociatedBehaviour ;
	}
	
	public void setAssociatedBehaviour(String AssociatedBehaviour){
		 this.AssociatedBehaviour = AssociatedBehaviour ;
	}
	
	public String getVariableName(){
		return this.VariableName ;
	}
	
	public void setVariableName(String VariableName){
		 this.VariableName = VariableName ;
	}
	
	public String getExecutedMethod(){
		return this.ExecutedMethod ;
	}
	
	public void setExecutedMethod(String ExecutedMethod){
		 this.ExecutedMethod = ExecutedMethod ;
	}
	
	public Object getmyObject(){
		return this.myObject ;
	}
	
	public void setmyObject(Object myObject){
		 this.myObject = myObject ;
	}
	
	public int getEventType(){
		return this.EventType ;
	}
	
	public void setEventType(int EventType){
		 this.EventType = EventType ;
	}
	
	public void setStartEvent(){
		 this.EventClass = this.Start ;
	}
	
	public void setEndEvent(){
		 this.EventClass = this.End ;
	}
	
	public int getEventClass(){
		return this.EventClass ;
	}	

}
