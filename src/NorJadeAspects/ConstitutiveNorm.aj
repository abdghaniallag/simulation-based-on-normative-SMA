package NorJadeAspects;

import jade.core.behaviours.*;
import jade.core.Agent;
import java.io.IOException;


public aspect ConstitutiveNorm {
	
	public void Agent.SupressLaw(String Law){
		
		String[] ConstitutiveProperties = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getConstitutiveProperties(Law);
        if (ConstitutiveProperties[0].equals("SocialPower")) {
        	if (ConstitutiveProperties[2].equals(this.getLocalName())) {
            	NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.DeleteLaw(Law);
            	System.out.println("[NorJADE Framework] : The law : " + Law + "  is deleted");
        	}
        	else{
            	System.out.println("[NorJADE Framework] : The agent : " + this.getLocalName() + "  can not supress the law : " + Law);
        	}
        }
	}
		
	public void Agent.AddLaw(String Law, String LawProperties[] ){
		
		String[] ConstitutiveProperties = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getConstitutiveProperties(Law);
        if (ConstitutiveProperties[0].equals("SocialPower")) {
        	if (ConstitutiveProperties[2].equals(this.getLocalName())) {
            	NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.InsertLaw(Law, LawProperties);	
            	System.out.println("[NorJADE Framework] : The law : " + Law + "  is added");
        	}
        	else{
            	System.out.println("[NorJADE Framework] : The agent : " + this.getLocalName() + "  can not add the law : " + Law);
        	}
        }
		
	}

	
	public void Agent.UpdateLaw(String Law, String NewLaw, String LawProperties[]){
	
			
			String[] ConstitutiveProperties = NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.getConstitutiveProperties(Law);
	        if (ConstitutiveProperties[0].equals("SocialPower")) {
	        	if (ConstitutiveProperties[2].equals(this.getLocalName())) {
	            	NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.DeleteLaw(Law);	
	            	NorJadeAspects.LoadNorJadeOntology.NorJadeOntology.InsertLaw(NewLaw, LawProperties);	
	            	System.err.println("[NorJADE Framework] : The law : " + Law + "  is updated");
	        	}
	        	else{
	            	System.err.println("[NorJADE Framework] : The agent : " + this.getLocalName() + "  can not update the law : " + Law);
	        	}
	        }
			
		}

		
		

}
