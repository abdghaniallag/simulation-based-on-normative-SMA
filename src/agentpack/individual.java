package agentpack;

import UI.DataSending;
import behaviours.*;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import start.MyContainer;
import util.agentData;


public class individual extends Agent {
    public agentData myData;
    @Override
    protected void setup() {

        Object argument = getArguments()[0];
        if (!MyContainer.fileimport){
            myData=new agentData(this.getAID(),(String) argument);
        }else {
            myData= (agentData) argument;
        }
        SequentialBehaviour sequentialBehaviour=new SequentialBehaviour();
        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
        sequentialBehaviour.addSubBehaviour(new createAgent(this));
        parallelBehaviour.addSubBehaviour(new DataSending(this,1000));
        parallelBehaviour.addSubBehaviour(new Move(this));
        parallelBehaviour.addSubBehaviour(new touche(this));
        sequentialBehaviour.addSubBehaviour(parallelBehaviour);
        addBehaviour(sequentialBehaviour);

    }
}
