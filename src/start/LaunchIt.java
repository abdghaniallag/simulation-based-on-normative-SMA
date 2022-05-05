package start;

import java.util.Vector;

import NorJadeAspects.LoadNorJadeOntology;

import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import xmlDocument.CreatDocument;

public class LaunchIt {

	public static void main(String[] args) {

		String norm = "Corona";
		LoadNorJadeOntology.NorJadeOntology.InsertNorm(norm, 1);
		String[] behaviourList = { "contact", "creatAgent", "Move", "MoveNear", "MoveFar", "touche" };
		String[] rewardPunishmentList = { "Amende",

		};
		String[] consequenceList = { "consequenceAmende", };

		insertBehaviourList(behaviourList);
		insertRewardPunishmentList(rewardPunishmentList);
		insertConsequenceList(consequenceList, rewardPunishmentList);
		new CreatDocument(LoadNorJadeOntology.NorJadeOntology).insertActiveLaws();
	}

	private static void insertBehaviourList(String[] behaviourList) {
		for (int i = 0; i < behaviourList.length; i++) {
			LoadNorJadeOntology.NorJadeOntology.InsertBehaviour(behaviourList[i]);
		}

	}

	private static void insertConsequenceList(String[] consequenceList, String[] rewardPunishmentList) {
		for (int i = 0; i < consequenceList.length; i++) {
			LoadNorJadeOntology.NorJadeOntology.InsertConsequence(consequenceList[i],
					rewardPunishmentList[i] + "RewardPunishment");
		}

	}

	private static void insertRewardPunishmentList(String[] rewardPunishmentList) {
		for (int i = 0; i < rewardPunishmentList.length; i++) {
			LoadNorJadeOntology.NorJadeOntology.InsertRewardPunishment(rewardPunishmentList[i]);
		}

	}

}
