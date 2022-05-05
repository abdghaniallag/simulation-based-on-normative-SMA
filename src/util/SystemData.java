package util;

import java.util.ArrayList;

public class SystemData {
	public static Boolean  IS_TERMINATE=false; 
	public static long  HEALING_TIME=0; 
	public static long STARTING_TIME = 0;

	public static void terminateSystem(ArrayList<agentData> agentInfos) {
		SystemData.IS_TERMINATE = true;
		for (int j = 0; j <  agentInfos.size(); j++) {
			System.out.println(agentInfos.get(j).getType());
			if (!agentInfos.get(j).getType().equalsIgnoreCase("Sain"))
				SystemData.IS_TERMINATE = false;
		}
	}
	public static void calculateHealingTimeSystem( ) {
		SystemData.HEALING_TIME=System.currentTimeMillis()-SystemData.STARTING_TIME;
	}
	public static void startTimer( ) {
		SystemData.STARTING_TIME=System.currentTimeMillis();
	}
}
