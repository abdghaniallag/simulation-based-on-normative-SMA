package NorJadeOntology;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;  
import java.util.Vector;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.update.UpdateAction;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
 

public class NorJadeOntologyBase {
	OntModel model;

	
	public NorJadeOntologyBase(String base){
		model = ModelFactory.createOntologyModel();
		model.read(base);		
	}
	
	/* executer une requête sparql */
	String[] QueryExecuting(String Query, int nb_para, int afficher) {
		Query query = QueryFactory.create(Query);
		QueryExecution qe = QueryExecutionFactory.create(query, model);
		ResultSet results = qe.execSelect();
		String s = "";
		while (results.hasNext()) {
			QuerySolution so = results.nextSolution();
			s += so.toString() + "\n";
		}
//		System.out.println(s);
		if (s.indexOf("=") == -1) {
			return null;
		} else {
			s = s.replaceAll("<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5", "");
			s = s.replaceAll("> | \\?| \\#| ", "");
			s = s.replaceAll(String.valueOf('"'), "");
			int index = s.indexOf("(");
			//System.out.println("s = "+ s);
			String para[] = new String[nb_para];
				for (int i = 0; i < nb_para; i++) {
					para[i] = s.substring(s.indexOf("=", index) + 1, s.indexOf(")", index));
					index = s.indexOf(")", index) + 2;
					if (afficher == 1)
						System.out.println("para["+i+"] = "+ para[i]);
			}
			return para;
		}

	}
	/* this function return the enforcer of specific law */
	public String getEnforcer(String law ) {
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?Enforcer "
				+ "WHERE { "
					+ "Base:" + law + " Base:AppliedBy ?Enforcer ."
				+ "}";
		String para = QueryExecuting(Query, 1, 0)[0];
		if(para.equals("SelfEnforcer") ) return para.replaceAll("_", " ");
		else{
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Enforcer_Name "
					+ "WHERE { "
						+ "Base:"+para+" Base:EnforcerNameIs ?Enforcer_Name ."
					+ "}";
			para = QueryExecuting(Query, 1, 0)[0];
			return para.replaceAll("_", " ");
		}
	}
	/*this function have one parameter : the behavior and return :
	 * 0 : The agent that execute the behaviour
	 * 1 : law that control the behavior
	 * 2 : the deontic operator of the law
	 * */
	public String[] getLaw(String behavior) {
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?law ?Dop ?agent " + "WHERE { "
						+ "?Behavior Base:BehaviourName '" + behavior + "' ."
						+ "?law Base:Control ?Behavior ."
						+"?Behavior Base:BehaviourExecutedBy ?agent ."
						+ "?law Base:SpecifiedBy ?Dop ."
						+ "filter (?Dop = Base:Prohibition || ?Dop = Base:Obligation ||?Dop = Base:Recommendation) ."
				+ "}";

		String para[] = QueryExecuting(Query, 3,0);
		return para;
	}
	/*this function return the law as follow :
	 *0 : Law
	 *1 : Dop
	 *2 : Behaviour
	 *3 : enforcer
	 *4 : Consequence 
	 *5 : Regulation
	 *6 : Validity
	 *7 :  Law Id
	 *8 :  Norm */
	
	public String[] getLaw(int id) {
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?LId ?Law ?Dop ?Behaviour ?agent ?Norm ?Consequence ?Regulation "
				+ "?Validity " + "WHERE { "
				+"?Law Base:LawId "+id+" ."
				+"?Law Base:SpecifiedBy ?Dop ."
				+"?Law Base:Control ?Behaviour ."
				+"?Law Base:AppliedBy  ?agent ."
				+"?Law Base:RewardedPunishedBy ?Consequence ."
				+"?Law Base:ImplementedBy ?Regulation ."
				+"?Law Base:ValidDuring ?Validity ."
				+"?Law Base:LawId ?LId ."    
				+"?Norm Base:ComposedOf ?Law ."		
				+ "}";

		String para[] = QueryExecuting(Query, 9,0);
		return para;
	}
	public Vector<String[]> getLawList() {
	 
Vector <String[]> list=new Vector<>();
int i=0;
		String para[] =null;
		while (i<10) {
			para=getLaw(i);
			if(para!=null) { 
			list.add(para);
			 
		}
			i++;}
		return list;
	}
	
	/*this function return the property of law as follow :
	 *0 : Deontic Operator 
	 *1 : Consequence Name
	 *2 : Enforcer Name
	 *3 : Type of consequence (Method or behavior)
	 *4 : Type of Regulation Mechanism(Enforcement or Regimentation) */
	public String[] getLawProperties(String law) {
		//System.out.println("==> NorJadeOntologyBase.getLawProperties" + law);
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?DeonticOperator ?consequence_Name ?type_Cons ?Enforcer ?RegMec_type "
				+ "WHERE { "
				    + "Base:" + law + " Base:SpecifiedBy ?DeonticOperator ."
					+ "Base:" + law + " Base:RewardedPunishedBy ?consequence ."
					+ "?consequence Base:CanBe ?Cons ."
					+ "?Cons Base:MethodName | Base:BehaviourName ?consequence_Name ."
					+ "?Cons rdf:type ?type_Cons ."
					+ "Base:" + law + " Base:AppliedBy ?Enforcer ."
					+ "Base:" + law + " Base:ImplementedBy ?RegMec ."
					+ "?RegMec rdf:type ?RegMec_type ."
				+ "}";
		System.out.println("law "+law);
		String para[] = QueryExecuting(Query, 5, 0);
			  
		Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?Enforcer_class "
				+ "WHERE { "
				+ "Base:"+para[2] 
						+ " rdf:type ?Enforcer_class ."
						+ "}";
		String para1[] = QueryExecuting(Query, 1, 0 );
		
		if(!para[2].equals("SelfEnforcer")) {
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Enforcer_Name "
					+ "WHERE { "
						+ "Base:"+para[2]+" Base:EnforcerNameIs ?Enforcer_Name ."
					+ "}";
			para[2] = QueryExecuting(Query, 1, 0)[0];
			para[2].replaceAll("_", " ");
		};
		return para;
	}

	/*this function return the sending or receiving message event*/
	public String getMsg(String sender , String performative , String receiver) {
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "SELECT ?even "
				+ "WHERE { "
				+ "?even Base:Sender ?Sender ."
				+ "?even Base:Performative '"+performative+"' ."
				+ "?even Base:Receiver ?Receiver ."
				+ "filter (?Receiver = 'ALL' || ( contains(?Receiver,'.c') && contains(?Receiver ,'"+receiver+"') ) || ?Receiver = '"+receiver+"') ."
				+ "filter (?Sender = 'ALL' || ( contains(?Sender,'.c') && contains(?Sender ,'"+sender+"') ) || ?Sender = '"+sender+"') ."
				+ "}";
		String para[] = QueryExecuting(Query, 1, 0);
		
		if(para != null ) return para[0];
		else return null;
	}
	
	/*
	 * This function return the class of an agent
	 */
	
	public String getAgentClass(String agent) {
		String para[]  ;
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?agent_class "
				+ "WHERE { "
				+ "Base:"+agent+" rdf:type ?agent_class ."
						+ "}";
		para = QueryExecuting(Query, 1, 0 );
		return para[0] ;
	}

	
	/*this Function return the property of an event as follow :
	 * 0 : The agent how triggered the event
	 * 1 : The event type
	 * 2 : Event Important for
	 * 3:  Class of the event (start or end)
	 * 4 : The law associated to the event
	 * 5 : The Behaviour 
	 * 6 : limitation 
	*/
	public String[] getEventProperties(String Event) {
		String para[],para1[],Limit_Dur;
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?Event_class ?Trigger ?Event_type ?Interest "
				+ "WHERE { "
				+ "Base:"+Event+" rdf:type ?Event_class ."
				+ "Base:"+Event+" Base:TriggeredBy ?Trigger ."
				+ "Base:"+Event+" Base:EventType ?Event_type ."
				+ "Base:"+Event+" Base:ImportantFor ?Interest ."
				+ "}";
			para = QueryExecuting(Query, 7, 0 );
			if(para[3].equals("StartEvent")){
				Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ "SELECT ?Limit_Dur "
						+ "WHERE { "
						+ "?Limit_Dur Base:StartedBy Base:"+Event+" ."
						+ "}";
				Limit_Dur = QueryExecuting(Query, 1, 0)[0];
			}else{

				Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ "SELECT ?Limit_Dur "
						+ "WHERE { "
						+ "?S_Limit Base:LimitationSpecifiedBy Base:"+Event+" ."
						+ "?Limit_Dur Base:LimitedBy ?S_Limit ."
						+ "}";
				Limit_Dur = QueryExecuting(Query, 1, 0)[0];
			}
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Law ?Behavior "
					+ "WHERE { "
					+ "?Validity Base:HoldDuring Base:"+Limit_Dur+" ."
					+ "?Law Base:ValidDuring ?Validity ."
					+ "?Law Base:Control ?Behavior ."
					+ "}";
			para1 = QueryExecuting(Query, 2, 0);
			para[4]= para1[0];
			para[5]= para1[1];
			para[6]= Limit_Dur ;
		return para;
	}

	/*this function return the behavior of an Goal
	 * 0: the Behavior
	 * 1: the type of the Goal (Individual or Collective) 
	*/
	public String[] getBehaviour(String Goal_Name){
		String para[];
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?Goal ?Goal_Type "
				+ "WHERE { "
				+ "?Goal Base:GoalName '"+Goal_Name+"' ."
				+ "?Goal rdf:type ?Goal_Type ."
				+ "}";
		para = QueryExecuting(Query, 2, 0);
		if (para[1].equals("IndividualGoal")){
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Behavior "
					+ "WHERE { "
					+ "Base:"+para[0]+" Base:ReachedBy ?Behavior ."
					+ "}";
			para[0] = QueryExecuting(Query, 1, 0)[0];
		}
		return para;
	}
	
	public String getBehaviourName(String Behaviour) {
		
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?BehaviourName " + "WHERE { "
						+"Base:"+Behaviour+" Base:BehaviourName ?BehaviourName ."
				+ "}";
		String para[] = QueryExecuting(Query, 1,0);
		return para[0];

	}
	
	public String getExecuterAgent(String Behaviour) {
		
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?ExecuterAgent " + "WHERE { "
						+"Base:"+Behaviour+" Base:BehaviourExecutedBy ?ExecuterAgent ."
				+ "}";
		String para[] = QueryExecuting(Query, 1,0);
		return para[0];

	}

	
	
	public String getEventId(String Event) {
		String Query = null;
		Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?Type " + "WHERE { "
						+"Base:"+Event+" Base:EventType ?Type ."
				+ "}";
		
		String para1[] = QueryExecuting(Query, 1,0);
		
		int i = Integer.parseInt(para1[0]);
		switch (i){	
		case 0 :{
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?EventId " + "WHERE { "
							+"Base:"+Event+" Base:VariableName ?EventId ."
					+ "}";

			break;
		}
		case 1 :{
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?EventId " + "WHERE { "
							+"Base:"+Event+" MethodExecuted ?EventId ."
					+ "}";

			break;
		}
		case 2 :{
			break;
		}

		}
		
		String para[] = QueryExecuting(Query, 1,0);
		return para[0];

	}
	
	public String getEvent(String EventId, int EventType) {
		String Query = null;
		
		switch (EventType){
		case 0 :{
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Event " + "WHERE { "
						    + "?Event Base:VariableName '" + EventId + "' ."

					+ "}";
			
			break;
		}
		case 1 :{
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Event " + "WHERE { "
							+"?Event Base:ExecutedMethod Base:" +EventId+" ."
					+ "}";

			
			break;
		}
		case 2 :{
			
			break;
		}

		
		}				
		String para[] = QueryExecuting(Query, 1,0);
		return para[0];	
	
	}


	/* this function return the property of validity of a law
	 * this function have 3 different out put : 
	 * 	1 out put : when the Validity is tested by a method it return the name of this method
	 * 	2 out put : when the validity is started by a start event and does not have a end, it return
	 * 				0 : Start Event
	 * 				1 : Indefinitely
	 *	3 out put : when the law is valid during a certain time, it return 
	 *				0 : Start Event
	 *				1 : End Event or scope 
	 *				2 : the Time if it end by a scope 
	 *
	 */
	public String[] getLawValidity(String Law){
		String para[];
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?Tester ?Tester_Type "
				+ "WHERE { "
				+ "Base:"+Law+" Base:ValidDuring ?Validity ."
				+ "?Validity Base:TestedBy | Base:HoldDuring ?Tester ."
				+ "?Tester rdf:type ?Tester_Type ."
				+ "}";
		para = QueryExecuting(Query, 2, 0);
		if(para[1].equals("ConditionalTest")){
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Method_Name "
					+ "WHERE { "
					+ "Base:"+para[0]+" Base:MethodName ?Method_Name ."
					+ "}";
			para = QueryExecuting(Query, 1, 0);
			return para;
		}else{
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Limit_type "
					+ "WHERE { "
					+ "Base:"+para[0]+" rdf:type ?Limit_type."
					+ "}";
			para[1] = QueryExecuting(Query, 1, 0)[0];
			if(para[1].equals("UnlimitedDuration")){
				return para;
			}else{
				Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
						+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ "SELECT ?Start_Event ?limit "
						+ "WHERE { "
						+ "Base:"+para[0]+" Base:StartedBy ?Start_Event ."
						+ "Base:"+para[0]+" Base:LimitedBy ?limit ."
						+ "}";
				String para1[] = QueryExecuting(Query, 2, 0);
				if(para1[1].equals("Indefinitely")){
					return para1;
				}else{
					para = new String[3];
					para[0] = para1[0];
					Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
							+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
							+ "SELECT ?S_limit ?Limit_Type "
							+ "WHERE { "
							+ "Base:"+para1[1]+" Base:LimitationSpecifiedBy ?S_limit ."
							+ "?S_limit rdf:type ?Limit_Type ."
							+ "}";
						para1 = QueryExecuting(Query, 2, 0);
						para[1]=para1[0];
						if(para1[1].equals("Scope")){
							Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
									+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
									+ "SELECT ?Time "
									+ "WHERE { "
									+ "Base:"+para[1]+" Base:Time ?Time ."
									+ "}";
							para[2] = QueryExecuting(Query, 1, 0)[0];
						}
						return para ;
				}
			}
		}
	}
	
	/*
	 * This Function return the properties of constitutive mechanism
	 * In this version, this function gives properties of only SocialPowerMechanism
	 * 0: Constitutive Mechainsm
	 * 1: Enforcer (in the case of Social Power
	 * 2: Enforcer Name (in the case of Social Power) 
	 * Function to complete with voting mechanism
	 */
	
	public String[] getConstitutiveProperties(String law) {
		
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?ConstitutiveMechanism "
				+ "WHERE { "
				    + "?ConstitutiveNorm Base:AppliedTo Base:"+law+" ."
				    + "?ConstitutiveNorm Base:Use ?ConstitutiveMechanism ."
	
				+ "}";
		String para[] = QueryExecuting(Query, 1, 1);
		String para1[] ;
		if(para[0].equals("SocialPower")) {
			Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
					+ "SELECT ?Enforcer ?Enforcer_Name "
					+ "WHERE { "
					    + "Base:"+para[0]+" Base:RealizedBy ?Enforcer ."
						+ "?Enforcer Base:EnforcerNameIs ?Enforcer_Name ."
					+ "}";

			}
		para1 = QueryExecuting(Query, 2, 1);				


        String para2[] = {"x", "x", "x"};
        para2[0] = para[0] ;
        para2[1] = para1[0] ;
        para2[2] = para1[1] ;

		return para2;
	}
	
	/*execute an update of the ontology (add or remove)*/
	void UpdateExecuting(String Query) {
		UpdateRequest query = UpdateFactory.create(Query);
		UpdateAction.execute(query, model);
		try {
			FileOutputStream out = new FileOutputStream("NorJadeOntology.owl");
			model.write(out, "RDF/XML");
//			System.out.println("updated......");
		} catch (FileNotFoundException ex) {
		}
	}
	
	/* This function supress an individual from the ontology*/
	void SimpleDelete(String Indiv){
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "DELETE where{Base:"+Indiv+" ?p ?o}";
		UpdateExecuting(Query);
	}

	
	/* this function return if the individual exist in the ontology or not*/
	
	public	boolean IsExistIndividual(String Indiv){
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "SELECT ?type {"
				+ "Base:"+Indiv+" rdf:type ?type . }";
		try{
			Indiv = QueryExecuting(Query, 1, 0)[0];
			return true;
		}catch(Exception e){
			return false;
		}
	}	
	/*Insert new Norm 
	 * NormName 
	 * NormType : 
	 * (0 ProceduralNorm )
	 * (1 RegulativeNorm  ) 
	 * (2 AddingLaw  )
	 * (3 SuppressingLaw  )
	 * (4 UpdatingLaws  )
	 */
	 
		public void InsertNorm(String NormName , int NormType) {
		 { String NormCl="";
		 switch(NormType) {
		 case 0:
		 { NormCl="ProceduralNorm";break;				 }
		 case 1:
		 { NormCl="RegulativeNorm";break;				 }
		 case 2:
		 { NormCl="AddingLaw";break;				 }
		 case 3:
		 { NormCl="SuppressingLaw";break;				 }
		 case 4:
		 { NormCl="UpdatingLaws";break;				 }
		 }
				System.out.println(NormName+" is inserted succesfully ....");
			String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "INSERT DATA {" 
					+ " Base:" + NormName+ " a Base:"+NormCl+" ." 
					+ "}";
			UpdateExecuting(Query);
			}
		}
	
	/*insert new law with all attribute 
	 * parameter of this function is the law and an array contain all attribute of the law as follow :
	 * 0: The Enforcer / 1: The Norm /2: The Behavior /3:The Regulation Mechanism
	 * 4: The consequence /5: Law Id /6: Validity /7:Deontic operator 
	 */
	public void InsertLaw(String Law ,String LawProperties[]) {
		boolean All_exist = true;
		String Property = null;
		for(int i = 0; i<LawProperties.length;i++){
			if(i!=5)
				All_exist = IsExistIndividual(LawProperties[i]);
			if(!All_exist){
				switch(i){
				case 0: Property = "The Enforcer"; break;
				case 1: Property = "The Norm"; break;
				case 2: Property = "The Behavior"; break;
				case 3: Property = "The Regulation Mechanism"; break;
				case 4: Property = "The consequence "; break;
				case 6: Property = "The Validity"; break;
				case 7: Property = "The Deontic operator"; break;
				}
				i= LawProperties.length;
			}
		}
		if(!All_exist){
			System.err.println("The property : "+ Property + " does not existe in NorJADE ontology");
		}else{
		String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "INSERT DATA {" 
				+ " Base:" + Law+ " a Base:Law ." 
				+ " Base:" + Law + " Base:AppliedBy Base:"+LawProperties[0]+" ."
				+ " Base:" + LawProperties[1] + " Base:ComposedOf Base:"+Law+" ."
				+ " Base:" + Law + " Base:Control Base:"+LawProperties[2]+" ."
				+ " Base:" + Law + " Base:ImplementedBy Base:"+LawProperties[3]+" ."
				+ " Base:" + Law + " Base:RewardedPunishedBy Base:"+LawProperties[4]+" ."
				+ " Base:" + Law + " Base:LawId "+LawProperties[5]+" ."
				+ " Base:" + Law + " Base:ValidDuring Base:"+LawProperties[6]+" ." 
				+ " Base:" + Law + " Base:SpecifiedBy Base:"+LawProperties[7]+" ."
			    + "}";
		UpdateExecuting(Query);
		}
	}
	/*insert new Agent
	 * AgentName 
	 * AgentClass : 
	 * (1 Simple agent)
	 * (2 specific agent)
	 */
	 
		public void InsertAgent(String AgentName , int AgentClass) {
		 { String AgentCl="";
		 if(AgentClass==1)
		 {AgentCl="ClassOfAgent";
			 
		 }else {
			 AgentCl="SpecificAgent";	 
		 }
				System.out.println(AgentName+" is inserted succesfully ....");
			String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
					+ "INSERT DATA {" 
					+ " Base:" + AgentName+ " a Base:"+AgentCl+" ." 
					+ "}";
			UpdateExecuting(Query);
			}
		}

		/*insert new Enforcer
		 * AgentEnforcer 
		 */
		 
			public void InsertEnforcer(String AgentEnforcer ) {
			 { 
				 String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
						+ "INSERT DATA {" 
						+ " Base:" + AgentEnforcer+ " a Base:ThirdPartyEnforcer ." 
						+ "}";
				UpdateExecuting(Query);
				}
			}
		/*insert new Behaviour
		 * Behaviour Name 
		 * Behaviour Executed By Agent
		 */
			public void InsertBehaviour(  String BehaviourName, String BehaviourExecutedBy) {
				BehaviourName+="Behaviour";
				boolean All_exist = IsExistIndividual(BehaviourExecutedBy);
				
				if(!All_exist){
					System.err.println("The property : BehaviourExecutedBy does not existe in NorJADE ontology");
				}else{
					System.out.println(BehaviourName+" !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!is inserted succesfully ....");
				String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
						+ "INSERT DATA {" 
						+ " Base:" + BehaviourName+ " a Base:Behaviour ." 
						+ " Base:" + BehaviourName +  " Base:BehaviourExecutedBy Base:"+BehaviourExecutedBy+" ."
						+ " Base:" + BehaviourName +  " Base:BehaviourName 'behaviours."+BehaviourName+"' ."
					    + "}";
				UpdateExecuting(Query);
				}
			}

			public void InsertBehaviour(String BehaviourName) {
				String BehaviourExecutedBy="AnyAgent";
//				BehaviourName+="Behaviour"; 
			 {
					System.out.println(BehaviourName+" is inserted succesfully ....");
				String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
						+ "INSERT DATA {" 
						+ " Base:" + BehaviourName+ " a Base:Behaviour ." 
						+ " Base:" + BehaviourName +  " Base:BehaviourExecutedBy Base:"+BehaviourExecutedBy+" ."
						+ " Base:" + BehaviourName +  " Base:BehaviourName 'behaviours."+BehaviourName+"' ."
					    + "}";
				UpdateExecuting(Query);
				}
			}
			public void InsertRewardPunishment(String BehaviourName) {
				String BehaviourExecutedBy="AnyAgent";
//				BehaviourName+="RewardPunishment";
				boolean All_exist = IsExistIndividual(BehaviourExecutedBy);
				
				if(!All_exist){
					System.err.println("The property : BehaviourExecutedBy does not existe in NorJADE ontology");
				}else{
					System.out.println(BehaviourName+" is inserted succesfully ");
				String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
						+ "INSERT DATA {" 
						+ " Base:" + BehaviourName+ " a Base:Behaviour ." 
						+ " Base:" + BehaviourName +  " Base:BehaviourExecutedBy Base:"+BehaviourExecutedBy+" ."
						+ " Base:" + BehaviourName +  " Base:BehaviourName 'RewardPunishment."+BehaviourName+"' ."
					    + "}";
				UpdateExecuting(Query);
				}
			}
			
	public void InsertValidity(  String Validity) {
	 	
		 String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "INSERT DATA {" 
				+ " Base:" + Validity+ " a Base:Validity ." 
				    + "}";
		UpdateExecuting(Query);
		
	}
	/*Insert Consequence
	 *  ConsequenceName
	 *  Behaviour
	 */
public void InsertConsequence(  String ConsequenceName, String Behaviour) {
	
	boolean All_exist = IsExistIndividual(Behaviour);
 
	if(!All_exist){
		System.err.println("The property : "+Behaviour+" does not existe in NorJADE ontology");
	}else{
		System.out.println(ConsequenceName+" is inserted succesfully ....");
	String Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
			+ "INSERT DATA {" 
			+ " Base:" + ConsequenceName+ " a Base:Consequence ." 
			+ " Base:" + ConsequenceName +  " Base:CanBe Base:"+Behaviour+" ."
			 + "}";
	UpdateExecuting(Query);
	}
}
	
	public void DeleteLaw(int LawId){
		String Query;
		
		String Law_Indiv = null ;
		Query = "PREFIX Base:<http://www.semanticweb.org/acer/ontologies/2018/4/untitled-ontology-5#>"
				+ "SELECT ?Law "
				+ "WHERE {"
				+ "?Law Base:LawId "+LawId+" ." 
				+ "}";
		try{
			 Law_Indiv = QueryExecuting(Query, 1, 0)[0];
			}catch(NullPointerException e){
				System.err.println("The Law does not exist in the ontology");
			
		}
		SimpleDelete(Law_Indiv);

	} 
	
	public void DeleteLaw(String Law_Indiv){
		SimpleDelete(Law_Indiv);
	}



}
