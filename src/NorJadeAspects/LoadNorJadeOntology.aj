package NorJadeAspects;

import NorJadeOntology.NorJadeOntologyBase;

public aspect LoadNorJadeOntology {
	public static NorJadeOntologyBase NorJadeOntology;

	public pointcut ApplicationLunching() : execution (public static void main(..));
	
	before() : ApplicationLunching(){
		
		NorJadeOntology = new NorJadeOntologyBase("NorJadeOntology.owl");
	 
	}


}
