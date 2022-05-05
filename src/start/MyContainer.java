package start;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import UI.EnvirennementGui;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import util.agentData;
import util.SystemData;
import util.target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MyContainer extends Application {

	static GridPane root1 = new GridPane();
	public static Scene scene2;
	public static Group simulator = new Group();
	public static agentData[][] occupation = new agentData[700][700];
	int numberOfAgentSains;
	int numberOfAgentMalade;
	int numberOfAgentRetablis;
	public static ArrayList<agentData> healthyAgentInfos = new ArrayList<>();
	public static ArrayList<agentData> infectedAgentInfos = new ArrayList<>();
	public static ArrayList<agentData> recovredAgentInfos = new ArrayList<>();
	public static boolean fileimport = false;

	public static void main(String[] args) {
		launch(MyContainer.class);
	}

	public void startContainer() {
		try {
			mainContainer();
			Runtime runtime = Runtime.instance();
			Profile profile = new ProfileImpl(false);
			profile.setParameter(Profile.MAIN_HOST, "localhost");
			AgentContainer agentContainer = runtime.createAgentContainer(profile);
			agentContainer.createNewAgent("EnvirennementGui", "UI.EnvirennementGui", new Object[] {}).start();
			SystemData.startTimer();
			if (!fileimport) {
				for (int i = 1; i <= numberOfAgentSains; i++) {
					agentContainer.createNewAgent("individual" + i, "agentpack.individual", new Object[] { "Sain" })
							.start();
				}
				for (int i = 1; i <= numberOfAgentMalade; i++) {
					int p = i + numberOfAgentSains;
					agentContainer.createNewAgent("individual" + p, "agentpack.individual", new Object[] { "malade" })
							.start();
				}
				for (int i = 1; i <= numberOfAgentRetablis; i++) {
					int p = i + (numberOfAgentSains + numberOfAgentMalade);
					agentContainer.createNewAgent("individual" + p, "agentpack.individual", new Object[] { "Retablis" })
							.start();
				}
			} else {
				if (fileimport) {
					for (int i = 0; i < healthyAgentInfos.size(); i++) {
						agentContainer.createNewAgent("individual" + (i + 1), "agentpack.individual",
								new Object[] { healthyAgentInfos.get(i) }).start();
					}
					for (int i = 0; i < infectedAgentInfos.size(); i++) {
						int p = i + healthyAgentInfos.size();
						agentContainer.createNewAgent("individual" + (p + 1), "agentpack.individual",
								new Object[] { infectedAgentInfos.get(i) }).start();
					}
					for (int i = 0; i < recovredAgentInfos.size(); i++) {
						int p = i + (healthyAgentInfos.size() + infectedAgentInfos.size());
						agentContainer.createNewAgent("individual" + (p + 1), "agentpack.individual",
								new Object[] { recovredAgentInfos.get(i) }).start();
					}
				}
			}

		} catch (ControllerException e) {
			e.printStackTrace();
		}
	}
	private void mainContainer() {
		Runtime runtime = Runtime.instance();
		Properties properties = new ExtendedProperties();
		Profile profile = new ProfileImpl(properties);
		AgentContainer Container = runtime.createMainContainer(profile);
		try {
			Container.start();
		} catch (ControllerException e) {
	 		e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Simulation");
		stage.setResizable(false);
		Scene scene1 = new Scene(root1, Color.valueOf("#c8d6e5"));
		scene2 = new Scene(simulator, 800, 800, Color.valueOf("#747d8c"));
		root1.setAlignment(Pos.TOP_CENTER);
		root1.setHgap(100);
		root1.setVgap(15);
		root1.setPadding(new Insets(25, 25, 25, 25));
		Text scenetitle = new Text("Welcome");
		scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
		root1.add(scenetitle, 0, 0, 2, 1);
		Text simType = new Text("Please choose the type of simulation:");
		simType.setFont(Font.font(12));
		root1.add(simType, 0, 0, 2, 3);
		ToggleGroup answer = new ToggleGroup();
		RadioButton manual = new RadioButton("Manual");
		manual.setToggleGroup(answer);
		root1.add(manual, 0, 2);
		RadioButton importFile = new RadioButton("Import data from file");
		importFile.setToggleGroup(answer);
		root1.add(importFile, 0, 6);
		Label healthyLabel = new Label("Number Of the healthy individuals :");
		root1.add(healthyLabel, 0, 3);
		TextField healthy = new TextField();
		root1.add(healthy, 1, 3);
		Label infectedLabel = new Label("Number Of the infected individuals :");
		root1.add(infectedLabel, 0, 4);
		TextField infected = new TextField();
		root1.add(infected, 1, 4);
		Label RecoveredLabel = new Label("Number Of the Recovered individuals :");
		root1.add(RecoveredLabel, 0, 5);
		TextField recovered = new TextField();
		root1.add(recovered, 1, 5);
		Button button = new Button();
		button.setText("launch");
		HBox hbBtn = new HBox(10);
		hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
		hbBtn.getChildren().add(button);
		root1.add(hbBtn, 1, 7);
		button.setOnAction(actionEvent -> {
			if (manual.isSelected()) {
				fileimport = false;
				numberOfAgentSains = Integer.parseInt(healthy.getText());
				numberOfAgentMalade = Integer.parseInt(infected.getText());
				numberOfAgentRetablis = Integer.parseInt(recovered.getText());
				startContainer();
				stage.setScene(scene2);
				System.out.println("\n|||- Strating Simulation -|||\n");
			} else {
				if (importFile.isSelected()) {
					fileimport = true;
					FileChooser fileChooser = new FileChooser();
					File file = fileChooser.showOpenDialog(stage);
					JsonArray jsonArray;
					JsonArray jsonArray2;
					try {
						jsonArray = (JsonArray) JsonParser.parseReader(new FileReader(file));
					 	for (int i = 0; i < jsonArray.size(); i++) {
							agentData agent = new agentData();
							JsonObject jsonObject = (JsonObject) jsonArray.get(i);
							agent.setType(jsonObject.get("type").toString().replace("\"", ""));
							agent.setFamily(jsonObject.get("family").toString().replace("\"", ""));
							agent.setCompte(Integer.parseInt(jsonObject.get("compte").toString().replace("\"", "")));
							agent.setHomeX(Integer.parseInt(jsonObject.get("homeX").toString().replace("\"", "")));
							agent.setHomeY(Integer.parseInt(jsonObject.get("homeY").toString().replace("\"", "")));
							jsonArray2 = (JsonArray) jsonObject.get("targets");
							for (int j = 0; j < jsonArray2.size(); j++) {
								JsonObject jsonObject2 = (JsonObject) jsonArray2.get(j);
								target target = new target();
								target.setCordinateX(
										Integer.parseInt(jsonObject2.get("targetX").toString().replace("\"", "")));
								target.setCordinateY(
										Integer.parseInt(jsonObject2.get("targetY").toString().replace("\"", "")));
								agent.getTargets().add(target);
							}
							switch (agent.getType()) {
							case "Sain": {
								healthyAgentInfos.add(agent);
								break;
							}
							case "malade": {
								infectedAgentInfos.add(agent);
								break;
							}
							case "Retablis": {
								recovredAgentInfos.add(agent);
								break;
							}
							default: {
								break;
							}
							}
						}
						startContainer();
						stage.setScene(scene2);
						System.out.println("\n|||- Strating Simulation -|||\n");

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

		});
		stage.setScene(scene1);

		stage.show();
	}
}
