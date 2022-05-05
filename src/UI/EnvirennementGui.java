package UI;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import util.agentData;
import java.util.ArrayList;

public class EnvirennementGui extends Agent {

	public ArrayList<agentData> agentInfos = new ArrayList<>();

	public ArrayList<agentData> healthyAgentInfos = new ArrayList<>();
	public ArrayList<agentData> exposedAgentInfos = new ArrayList<>();
	public ArrayList<agentData> infectedAgentInfos = new ArrayList<>();
	public ArrayList<agentData> recovredAgentInfos = new ArrayList<>();

	@Override
	protected void setup() {
		ParallelBehaviour parallelBehaviour = new ParallelBehaviour();
		parallelBehaviour.addSubBehaviour((new GUI(this)));
		parallelBehaviour.addSubBehaviour(new infection(this));
		addBehaviour(parallelBehaviour);

	}
}
