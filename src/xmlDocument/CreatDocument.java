package xmlDocument;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException; 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer; 
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element; 
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import NorJadeOntology.NorJadeOntologyBase;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class CreatDocument { 
	public  NorJadeOntologyBase ontology;
	  public CreatDocument(NorJadeOntologyBase ontology) {
			super();
			this.ontology =ontology;
		}
	  	public   Vector<String> getActivelaws() {
		Vector<String> lawIds=new Vector<String>();
		File inputFile = new File("LawBase.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
	        Document doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();
	    
	        NodeList LawList = doc.getElementsByTagName("law");      
	       
	        for (int temp = 0; temp < LawList.getLength(); temp++) {
	        	 Element law=	(Element) LawList.item(temp);
	         if( law.getElementsByTagName("State").item(0).getTextContent().equalsIgnoreCase("Active")) {
		    	 	lawIds.add(law.getElementsByTagName("LawId").item(0).getTextContent());     
		    	 
			        	
	        	 }
		    	
		        } 

} catch (ParserConfigurationException | SAXException | IOException   e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
 		return lawIds;
		
	}
	 
	
	public   Vector<String> getlawEffects(String LawId) {
		Vector<String> lawEffects=new Vector<String>();
		File inputFile = new File("LawBase.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(inputFile);
		        doc.getDocumentElement().normalize();
		           
		         NodeList IdList = doc.getElementsByTagName("LawId");     
		         Element eElement = null ;
		        
				for (int temp = 0; temp < IdList.getLength(); temp++) {
		        Element id=	(Element) IdList.item(temp);
		       
		        if( id.getTextContent().equalsIgnoreCase(LawId)) 
		    		   eElement =(Element) id.getParentNode().getParentNode();
		           }   
	               
				NodeList effectList =   eElement.getElementsByTagName("effects");
		       	for (int temp = 0; temp < effectList.getLength(); temp++) {
			        Element ef=	(Element) effectList.item(temp);
			        switch (ef.getTextContent()) {
			        case "+":
					         lawEffects.add("increase");
							break;
			        case "-":
					        lawEffects.add("decrease");
							break;
			        

					default: 
						lawEffects.add("nothing");
					
						break;
					}
			         
					}   
		                } catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return lawEffects;
		
	}	
	
	public   Vector<String> getlawAttributes(String LawId) {
		Vector<String> lawAttributes=new Vector<String>();
		File inputFile = new File("LawBase.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(inputFile);
		        doc.getDocumentElement().normalize();
		           
		         NodeList IdList = doc.getElementsByTagName("LawId");     
		         Element eElement = null ;
		        
				for (int temp = 0; temp < IdList.getLength(); temp++) {
		        Element id=	(Element) IdList.item(temp);
		       
		        if( id.getTextContent().equalsIgnoreCase(LawId)) 
		    		   eElement =(Element) id.getParentNode().getParentNode();
		           }   
	               
				NodeList effectList =   eElement.getElementsByTagName("attributes");
		       	for (int temp = 0; temp < effectList.getLength(); temp++) {
			        Element ef=	(Element) effectList.item(temp);
			        lawAttributes.add(ef.getTextContent());
			         
					}   
		                } catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return lawAttributes;
		
	}
	public    String getlawstate(String LawId) {
		 String lawstate="";
		File inputFile = new File("LawBase.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(inputFile);
		        doc.getDocumentElement().normalize();
		           
		         NodeList IdList = doc.getElementsByTagName("LawId");     
		         Element eElement = null ;
		        
				for (int temp = 0; temp < IdList.getLength(); temp++) {
		        Element id=	(Element) IdList.item(temp);
		       
		        if( id.getTextContent().equalsIgnoreCase(LawId)) 
		    		   eElement =(Element) id.getParentNode().getParentNode();
		           }   
	               
				NodeList effectList =   eElement.getElementsByTagName("State");
		       	for (int temp = 0; temp < effectList.getLength(); temp++) {
			        Element ef=	(Element) effectList.item(temp);
			      if(ef.getTextContent().equalsIgnoreCase("Active"))
			            lawstate="1";
			      else if(ef.getTextContent().equalsIgnoreCase("InActive"))
				        lawstate="0";
				     
			         
					}   
		                } catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return lawstate;
		
	}
	public   Vector<String> getlawProperties(String LawId) {
		Vector<String> lawProperties=new Vector<String>();
		File inputFile = new File("LawBase.xml");
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
		        Document doc = dBuilder.parse(inputFile);
		        doc.getDocumentElement().normalize();
		          
		         Element nNode = null;
		         NodeList IdList = doc.getElementsByTagName("LawId");     
			     
		        
				for (int temp = 0; temp < IdList.getLength(); temp++) {
		        Element id=	(Element) IdList.item(temp);
		    	if( id.getTextContent().equalsIgnoreCase(LawId)) {
		    		nNode=(Element) id.getParentNode().getParentNode();
		    		//name :
		    		lawProperties.add(nNode.getAttribute("name"));     
		    	  }
		    	
		        }
		       
		        	   {
		                 Element eElement = (Element) nNode;   
		                 //Enforcer :            " 
                		 lawProperties.add(eElement
 		                    .getElementsByTagName("Enforcer")
 		                    .item(0)
 		                    .getTextContent());
                		//Norm
    		        	 lawProperties.add( nNode.getParentNode().getAttributes().item(0).getTextContent());
    		        	 //Behaviour :           " 
                		 lawProperties.add( eElement
 		                    .getElementsByTagName("Behaviour")
 		                    .item(0)
 		                    .getTextContent());
                       //RegulationMechanism : " 
                		 lawProperties.add(eElement
 		                    .getElementsByTagName("RegulationMechanism")
 		                    .item(0)
 		                    .getTextContent());
            
            
		               //Consequence :         " 
                		 lawProperties.add( eElement
 		                    .getElementsByTagName("Consequence")
 		                    .item(0)
 		                    .getTextContent()); 
                       //LawId :               " 
                		 lawProperties.add(LawId)   ;
                		 //Validity :            " 
                		 lawProperties.add( eElement
 		                    .getElementsByTagName("Validity")
 		                    .item(0)
 		                    .getTextContent());
                		 //DeonticOperator :    " 
		                		 lawProperties.add(eElement
		 		                    .getElementsByTagName("DeonticOperator")
		 		                    .item(0)
		 		                    .getTextContent());
		                     
		                
		              
		              }
		      
			} catch (ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return lawProperties;
		
	}
	public   void deletAllLawsFromOntology() {
		Vector<String[]> lawList = ontology.getLawList();
		for(int a=0;a<lawList.size();a++)
		{
			ontology.DeleteLaw(lawList.get(a)[0]);
		}
	}
	 
	public   void insertToOntology(Vector<String> LawIds) {

		Vector<String> propr= new Vector<>();
	for(int i=0;i<LawIds.size();i++) {
		propr=getlawProperties(LawIds.get(i));
		String name=propr.get(0);
		propr.remove(0);	

		String []proprties=new String [propr.size()];
		for(int s=0;s<propr.size();s++) {
			System.out.println(propr.get(i));
			proprties[s]=propr.get(s);}
	 
		ontology.InsertLaw(name, proprties); 
		 
	}
	}
	
	public   void insertActiveLaws() {
		Vector<String> ActiveLaws=getActivelaws();
		deletAllLawsFromOntology();
		 insertToOntology(ActiveLaws);
		}
	public   void changeState(String LawId,int Active) {
	try { File inputFile = new File("LawBase.xml");
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;
    
	 
		dBuilder = dbFactory.newDocumentBuilder();
		 Document  doc = dBuilder.parse(inputFile);
	        doc.getDocumentElement().normalize();  
	        NodeList IdList = doc.getElementsByTagName("LawId");     
	     
	         Element Law = null;
			for (int temp = 0; temp < IdList.getLength(); temp++) {
	        Element id=	(Element) IdList.item(temp);
	    	if( id.getTextContent().equalsIgnoreCase(LawId))
	    	  Law=(Element) id.getParentNode().getParentNode();
	        }
	        
	          Element nodeAttr =    (Element) Law.getElementsByTagName("State").item(0);
	       
	         if(Active==0)
	        	nodeAttr.setTextContent("InActive");
	        else

	        	nodeAttr.setTextContent("Active");
     TransformerFactory transformerFactory = TransformerFactory.newInstance();
     Transformer transformer;
			
				transformer = transformerFactory.newTransformer();
				   DOMSource source = new DOMSource(doc);
			         StreamResult result = new StreamResult(inputFile);
			         transformer.transform(source, result);
		 	} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
}

	/*insert new law with all attribute 
	 *0 :  Law
	 *1 :  Dop
	 *2 :  Behaviour
	 *3 :  enforcer
	 *4 :  Consequence 
	 *5 :  Regulation
	 *6 :  Validity
	 *7 :  Law Id
	 *8 :  Norm 
	 */
	
	public   void InsertLaw( String LawProperties[] ,String []effects,String []attributes) {
		try { File inputFile = new File("LawBase.xml");
	       DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	       DocumentBuilder dBuilder;
	       
		 
			dBuilder = dbFactory.newDocumentBuilder();
			 Document  doc = dBuilder.parse(inputFile);
		        doc.getDocumentElement().normalize();
		         NodeList nList = doc.getElementsByTagName("law");      
		 
        Element Properties = doc.createElement("properties");
        {  
        Element lawId = doc.createElement("LawId");
	     lawId.appendChild(doc.createTextNode(LawProperties[7]));
	     Properties.appendChild(lawId);
 
	     Element DeonticOperator = doc.createElement("DeonticOperator");
	     DeonticOperator.appendChild(doc.createTextNode(LawProperties[1]));
		 Properties.appendChild(DeonticOperator);
		
		 Element Consequence = doc.createElement("Consequence");
		 Consequence.appendChild(doc.createTextNode(LawProperties[4]));
	     Properties.appendChild(Consequence);
 
	     Element Behaviour = doc.createElement("Behaviour");
		 Behaviour.appendChild(doc.createTextNode(LawProperties[2]));
	     Properties.appendChild(Behaviour);
 
	     Element RegulationMechanism = doc.createElement("RegulationMechanism");
	     RegulationMechanism.appendChild(doc.createTextNode(LawProperties[5]));
		 Properties.appendChild(RegulationMechanism);
		
		 Element Enforcer = doc.createElement("Enforcer");
		 Enforcer.appendChild(doc.createTextNode(LawProperties[3]));
	     Properties.appendChild(Enforcer);

	     Element Validity = doc.createElement("Validity");
	     Validity.appendChild(doc.createTextNode(LawProperties[6]));
		 Properties.appendChild(Validity);
	

	     Element State = doc.createElement("State");
	     State.appendChild(doc.createTextNode("Active"));
		 Properties.appendChild(State);
        }
        
        Element description = doc.createElement("description");
        for(int i =0;i<effects.length;i++) {
        Element listattributes = doc.createElement("attributes");
        listattributes.appendChild(doc.createTextNode(attributes[i]));
        description.appendChild(listattributes);  
        
        Element listeffects = doc.createElement("effects");
        listeffects.appendChild(doc.createTextNode(effects[i]));
        description.appendChild(listeffects);
        }
		 Element Norme =doc.getDocumentElement();
	        Element law = doc.createElement("law");
	        Attr attr = doc.createAttribute("name");
	        attr.setValue(LawProperties[0]);
	        law.setAttributeNode(attr);
	        law.appendChild(Properties);
	        law.appendChild(description);
	        
	        Norme.appendChild(law);
	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer;
				
					transformer = transformerFactory.newTransformer();
					   DOMSource source = new DOMSource(doc);
				         StreamResult result = new StreamResult(inputFile);
				         transformer.transform(source, result);
			 	} catch (TransformerException | ParserConfigurationException | SAXException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      
	}
	

}
