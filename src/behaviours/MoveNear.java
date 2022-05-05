package behaviours;

import agentpack.MyContainer;
import agentpack.individual;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import util.step;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MoveNear extends OneShotBehaviour {
	individual agent;

	public MoveNear(individual agent) {
		this.agent = agent;
	}

	Random r = new Random();
	ArrayList<step> path;

	@Override
	public void action() {
		path = path(agent.myData.getCoordinateX(), agent.myData.getTargetX(), agent.myData.getCoordinateY(),
				agent.myData.getTargetY());

		int x = 0;
		while (x < path.size()) {
			step step = path.get(x);
			boolean vr1 = false;
			boolean vr2 = false;
			boolean vr3 = false;
			boolean vr4 = false;
			for (int i = step.getCordinateX(); i >= step.getCordinateX() - agent.myData.getRaduis(); i--) {
				for (int j = step.getCordinateY(); j >= step.getCordinateY() - agent.myData.getRaduis(); j--) {
					if (MyContainer.occupation[i][j] != null && MyContainer.occupation[i][j] != agent.myData) {
						break;
					}
					if (i == step.getCordinateX() - agent.myData.getRaduis()
							&& j == step.getCordinateY() - agent.myData.getRaduis()
							&& (MyContainer.occupation[i][j] == null || MyContainer.occupation[i][j] == agent.myData)) {
						vr1 = true;
						break;
					}
				}
			}
			for (int i = step.getCordinateX(); i <= step.getCordinateX() + agent.myData.getRaduis(); i++) {
				for (int j = step.getCordinateY(); j <= step.getCordinateY() + agent.myData.getRaduis(); j++) {
					if (MyContainer.occupation[i][j] != null && MyContainer.occupation[i][j] != agent.myData) {
						break;
					}
					if (i == step.getCordinateX() + agent.myData.getRaduis()
							&& j == step.getCordinateY() + agent.myData.getRaduis()
							&& (MyContainer.occupation[i][j] == null || MyContainer.occupation[i][j] == agent.myData)) {
						vr2 = true;
						break;
					}
				}
			}
			for (int i = step.getCordinateX(); i <= step.getCordinateX() + agent.myData.getRaduis(); i++) {
				for (int j = step.getCordinateY(); j >= step.getCordinateY() - agent.myData.getRaduis(); j--) {
					if (MyContainer.occupation[i][j] != null && MyContainer.occupation[i][j] != agent.myData) {
						break;
					}
					if (i == step.getCordinateX() + agent.myData.getRaduis()
							&& j == step.getCordinateY() - agent.myData.getRaduis()
							&& (MyContainer.occupation[i][j] == null || MyContainer.occupation[i][j] == agent.myData)) {
						vr3 = true;
						break;
					}
				}
			}
			for (int i = step.getCordinateX(); i >= step.getCordinateX() - agent.myData.getRaduis(); i--) {
				for (int j = step.getCordinateY(); j <= step.getCordinateY() + agent.myData.getRaduis(); j++) {
					if (MyContainer.occupation[i][j] != null && MyContainer.occupation[i][j] != agent.myData) {
						break;
					}
					if (i == step.getCordinateX() - agent.myData.getRaduis()
							&& j == step.getCordinateY() + agent.myData.getRaduis()
							&& (MyContainer.occupation[i][j] == null || MyContainer.occupation[i][j] == agent.myData)) {
						vr4 = true;
						break;
					}
				}
			}
			if (vr1 && vr2 && vr3 && vr4) {
				for (int i = agent.myData.getCoordinateX(); i >= agent.myData.getCoordinateX()
						- agent.myData.getRaduis(); i--) {
					for (int j = agent.myData.getCoordinateY(); j >= agent.myData.getCoordinateY()
							- agent.myData.getRaduis(); j--) {
						MyContainer.occupation[i][j] = null;
					}
				}
				for (int i = agent.myData.getCoordinateX(); i <= agent.myData.getCoordinateX()
						+ agent.myData.getRaduis(); i++) {
					for (int j = agent.myData.getCoordinateY(); j <= agent.myData.getCoordinateY()
							+ agent.myData.getRaduis(); j++) {
						MyContainer.occupation[i][j] = null;
					}
				}
				for (int i = agent.myData.getCoordinateX(); i <= agent.myData.getCoordinateX()
						+ agent.myData.getRaduis(); i++) {
					for (int j = agent.myData.getCoordinateY(); j >= agent.myData.getCoordinateY()
							- agent.myData.getRaduis(); j--) {
						MyContainer.occupation[i][j] = null;
					}
				}
				for (int i = agent.myData.getCoordinateX(); i >= agent.myData.getCoordinateX()
						- agent.myData.getRaduis(); i--) {
					for (int j = agent.myData.getCoordinateY(); j <= agent.myData.getCoordinateY()
							+ agent.myData.getRaduis(); j++) {
						MyContainer.occupation[i][j] = null;
					}
				}
				// -----------------------------------------------------------------------------------------------------------------------------------//
				for (int i = step.getCordinateX(); i >= step.getCordinateX() - agent.myData.getRaduis(); i--) {
					for (int j = step.getCordinateY(); j >= step.getCordinateY() - agent.myData.getRaduis(); j--) {
						MyContainer.occupation[i][j] = agent.myData;
					}
				}
				for (int i = step.getCordinateX(); i <= step.getCordinateX() + agent.myData.getRaduis(); i++) {
					for (int j = step.getCordinateY(); j <= step.getCordinateY() + agent.myData.getRaduis(); j++) {
						MyContainer.occupation[i][j] = agent.myData;
					}
				}
				for (int i = step.getCordinateX(); i <= step.getCordinateX() + agent.myData.getRaduis(); i++) {
					for (int j = step.getCordinateY(); j >= step.getCordinateY() - agent.myData.getRaduis(); j--) {
						MyContainer.occupation[i][j] = agent.myData;
					}
				}
				for (int i = step.getCordinateX(); i >= step.getCordinateX() - agent.myData.getRaduis(); i--) {
					for (int j = step.getCordinateY(); j <= step.getCordinateY() + agent.myData.getRaduis(); j++) {
						MyContainer.occupation[i][j] = agent.myData;
					}
				}
				agent.myData.setCoordinateX(step.getCordinateX());
				agent.myData.setCoordinateY(step.getCordinateY());
				agent.myData.setStep(step);
				agent.myData.setRunning(true);
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				msg.addReceiver(new AID("EnvirennementGui", AID.ISLOCALNAME));
				try {
					msg.setContentObject(agent.myData);
					agent.send(msg);
					x = x + 1;
					TimeUnit.MILLISECONDS.sleep(15);
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			} else {
				double sleep = (r.nextDouble() * 3);
				try {
					TimeUnit.SECONDS.sleep((long) sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				for (int i = x; i < path.size(); i++) {
					path.remove(i);
				}
				Random r2 = new Random();
				int direction = r2.nextInt(3);
				switch (direction) {
				case 0: {
					for (int y = 0; y < 5; y++) {
						if (step.getCordinateX() < 700) {
							step step1 = new step();
							step1.setCordinateX(step.getCordinateX() + 1);
							step1.setCordinateY(step.getCordinateY());
							path.add(x, step1);
							x++;
						}
					}
					ArrayList<step> path2 = path(path.get(path.size() - 1).getCordinateX(), agent.myData.getTargetX(),
							path.get(path.size() - 1).getCordinateY(), agent.myData.getTargetY());
					path.addAll(path2);
					break;
				}
				case 1: {
					for (int y = 0; y < 5; y++) {
						if (step.getCordinateX() > agent.myData.getRaduis()) {
							step step1 = new step();
							step1.setCordinateX(step.getCordinateX() - 1);
							step1.setCordinateY(step.getCordinateY());
							path.add(x, step1);
							x++;
						}

					}
					ArrayList<step> path2 = path(path.get(path.size() - 1).getCordinateX(), agent.myData.getTargetX(),
							path.get(path.size() - 1).getCordinateY(), agent.myData.getTargetY());
					path.addAll(path2);
					break;
				}
				case 2: {
					for (int y = 0; y < 5; y++) {
						if (step.getCordinateY() < 700) {
							step step1 = new step();
							step1.setCordinateX(step.getCordinateX());
							step1.setCordinateY(step.getCordinateY() + 1);
							path.add(x, step1);
							x++;
						}
					}
					ArrayList<step> path2 = path(path.get(path.size() - 1).getCordinateX(), agent.myData.getTargetX(),
							path.get(path.size() - 1).getCordinateY(), agent.myData.getTargetY());
					path.addAll(path2);
					break;
				}
				case 3: {
					for (int y = 0; y < 5; y++) {
						if (step.getCordinateY() > agent.myData.getRaduis()) {
							step step1 = new step();
							step1.setCordinateX(step.getCordinateX());
							step1.setCordinateY(step.getCordinateY() - 1);
							path.add(x, step1);
							x++;
						}
					}
					ArrayList<step> path2 = path(path.get(path.size() - 1).getCordinateX(), agent.myData.getTargetX(),
							path.get(path.size() - 1).getCordinateY(), agent.myData.getTargetY());
					path.addAll(path2);
					break;
				}
				}
			}
		}
		double sleep = (r.nextDouble() * 3);
		try {
			TimeUnit.SECONDS.sleep((long) sleep);
			agent.addBehaviour(new Move(agent));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<step> path(int x, int tx, int y, int ty) {
		path = new ArrayList<>();
		int distanceX;
		int distanceY;
		int actualX = x;
		int actualY = y;
		if (tx < x && ty < y) {
			distanceX = x - tx;
			distanceY = y - ty;
			int max = Math.max(distanceX, distanceY);
			if (max == distanceX) {
				for (int i = 0; i < distanceX; i++) {
					step traget = new step();
					actualX -= 1;
					traget.setCordinateX(actualX);
					path.add(traget);
				}
				for (int i = 0; i < path.size(); i++) {
					if (distanceY > 0) {
						actualY -= 1;
						path.get(i).setCordinateY(actualY);
						distanceY -= 1;
					} else {
						path.get(i).setCordinateY(path.get(i - 1).getCordinateY());
					}

				}
			}
			if (max == distanceY) {
				for (int i = 0; i < distanceY; i++) {
					step traget = new step();
					actualY -= 1;
					traget.setCordinateY(actualY);
					path.add(traget);
				}
				for (int i = 0; i < path.size(); i++) {
					if (distanceX > 0) {
						actualX -= 1;
						path.get(i).setCordinateX(actualX);
						distanceX -= 1;
					} else {
						path.get(i).setCordinateX(path.get(i - 1).getCordinateX());
					}

				}
			}
		} else {
			if (tx < x && ty > y) {
				distanceX = x - tx;
				distanceY = ty - y;
				int max = Math.max(distanceX, distanceY);
				if (max == distanceX) {
					for (int i = 0; i < distanceX; i++) {
						step traget = new step();
						actualX -= 1;
						traget.setCordinateX(actualX);
						path.add(traget);
					}
					for (int i = 0; i < path.size(); i++) {
						if (distanceY > 0) {
							actualY += 1;
							path.get(i).setCordinateY(actualY);
							distanceY -= 1;
						} else {
							path.get(i).setCordinateY(path.get(i - 1).getCordinateY());
						}

					}
				}
				if (max == distanceY) {
					for (int i = 0; i < distanceY; i++) {
						step traget = new step();
						actualY += 1;
						traget.setCordinateY(actualY);
						path.add(traget);
					}
					for (int i = 0; i < path.size(); i++) {
						if (distanceX > 0) {
							actualX -= 1;
							path.get(i).setCordinateX(actualX);
							distanceX -= 1;
						} else {
							path.get(i).setCordinateX(path.get(i - 1).getCordinateX());
						}

					}
				}
			} else {
				if (tx > x && ty > y) {
					distanceX = tx - x;
					distanceY = ty - y;
					int max = Math.max(distanceX, distanceY);
					if (max == distanceX) {
						for (int i = 0; i < distanceX; i++) {
							step step = new step();
							actualX += 1;
							step.setCordinateX(actualX);
							path.add(step);
						}
						for (int i = 0; i < path.size(); i++) {
							if (distanceY > 0) {
								actualY += 1;
								path.get(i).setCordinateY(actualY);
								distanceY -= 1;
							} else {
								path.get(i).setCordinateY(path.get(i - 1).getCordinateY());
							}

						}
					}
					if (max == distanceY) {
						for (int i = 0; i < distanceY; i++) {
							step step = new step();
							actualY += 1;
							step.setCordinateY(actualY);
							path.add(step);
						}
						for (int i = 0; i < path.size(); i++) {
							if (distanceX > 0) {
								actualX += 1;
								path.get(i).setCordinateX(actualX);
								distanceX -= 1;
							} else {
								path.get(i).setCordinateX(path.get(i - 1).getCordinateX());
							}

						}
					}
				} else {
					if (tx > x && ty < y) {
						distanceX = tx - x;
						distanceY = y - ty;
						int max = Math.max(distanceX, distanceY);
						if (max == distanceX) {
							for (int i = 0; i < distanceX; i++) {
								step step = new step();
								actualX += 1;
								step.setCordinateX(actualX);
								path.add(step);
							}
							for (int i = 0; i < path.size(); i++) {
								if (distanceY > 0) {
									actualY -= 1;
									path.get(i).setCordinateY(actualY);
									distanceY -= 1;
								} else {
									path.get(i).setCordinateY(path.get(i - 1).getCordinateY());
								}

							}
						}
						if (max == distanceY) {
							for (int i = 0; i <= distanceY; i++) {
								step step = new step();
								actualY -= 1;
								step.setCordinateY(actualY);
								path.add(step);
							}
							for (int i = 0; i < path.size(); i++) {
								if (distanceX > 0) {
									actualX += 1;
									path.get(i).setCordinateX(actualX);
									distanceX -= 1;
								} else {
									path.get(i).setCordinateX(path.get(i - 1).getCordinateX());
								}

							}
						}

					} else {
						if (tx == x && ty < y) {
							distanceY = y - ty;
							for (int i = 0; i <= distanceY; i++) {
								step step = new step();
								actualY -= 1;
								step.setCordinateY(actualY);
								path.add(step);
							}
							for (util.step step : path) {
								step.setCordinateX(tx);
							}
						} else {
							if (tx == x && ty > y) {
								distanceY = ty - y;
								for (int i = 0; i <= distanceY; i++) {
									step step = new step();
									actualY += 1;
									step.setCordinateY(actualY);
									path.add(step);
								}
								for (util.step step : path) {
									step.setCordinateX(tx);
								}
							} else {
								if (tx > x && ty == y) {
									distanceX = tx - x;
									for (int i = 0; i <= distanceX; i++) {
										step step = new step();
										actualX += 1;
										step.setCordinateX(actualX);
										path.add(step);
									}
									for (util.step step : path) {
										step.setCordinateY(ty);
									}
								} else {
									if (tx < x && ty == y) {
										distanceX = x - tx;
										for (int i = 0; i <= distanceX; i++) {
											step step = new step();
											actualX -= 1;
											step.setCordinateX(actualX);
											path.add(step);
										}
										for (util.step step : path) {
											step.setCordinateY(ty);
										}
									}
								}
							}
						}
					}
				}

			}
		}
		return path;
	}
}
