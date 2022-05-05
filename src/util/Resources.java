package util;

import java.util.ArrayList;

public class Resources {
	public static Boolean  DONE=false; 
	public static long  HEALING_TIME=0; 
	public static long STARTING_TIME = 0;

	public static void terminateSystem(ArrayList<agentData> agentInfos) {
		Resources.DONE = true;
		for (int j = 0; j <  agentInfos.size(); j++) {
			System.out.println(agentInfos.get(j).getType());
			if (!agentInfos.get(j).getType().equalsIgnoreCase("Sain"))
				Resources.DONE = false;
		}
	}
	public static void calculateHealingTimeSystem( ) {
		Resources.HEALING_TIME=System.currentTimeMillis()-Resources.STARTING_TIME;
	}
	public static void startTimer( ) {
		Resources.STARTING_TIME=System.currentTimeMillis();
	}
}
