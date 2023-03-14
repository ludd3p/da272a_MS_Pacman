package pacman.entries.pacman;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import java.util.*;

/*
 * This is the class you need to modify for your entry. In particular, you need to
 * fill in the getAction() method. Any additional classes you write should either
 * be placed in this package or sub-packages (e.g., game.entries.pacman.mypackage).
 */
public class MyPacMan extends Controller<MOVE>
{
	private MOVE myMove=MOVE.NEUTRAL;
	private ArrayList<DataTuple> savedData, dataSetTraining, dataSetTest;
	private HashMap<String, ArrayList<String>> attributeMap;
	private Node root;

	public MyPacMan(){
		// Get saved data from file
		savedData = new ArrayList<>(List.of(DataSaverLoader.LoadPacManData()));
		// Initiate attributes
		initAttributes();
		// Create test and training data sets
		createDataSets();
		// Start the process of building tree
		ArrayList<String> attributelist = new ArrayList<>(attributeMap.keySet());
		root = buildTree(dataSetTraining, attributelist);
		validateTree(dataSetTraining, "Training");
		validateTree(dataSetTest, "Testing");
	}


	/**
	 * Creates lists of attributes and adds them to hashmap
	 */
	private void initAttributes() {
		attributeMap = new HashMap<>();
		ArrayList<String> edible = new ArrayList<>();
		edible.add("true");
		edible.add("false");
		attributeMap.put("blinkyEdible",edible);
		attributeMap.put("pinkyEdible",edible);
		attributeMap.put("sueEdible",edible);
		attributeMap.put("inkyEdible",edible);
		ArrayList<String> distance = new ArrayList<>();
		distance.add("VERY_HIGH");
		distance.add("HIGH");
		distance.add("MEDIUM");
		distance.add("LOW");
		distance.add("VERY_LOW");
		distance.add("NONE");
		attributeMap.put("blinkyDist", distance);
		attributeMap.put("pinkyDist", distance);
		attributeMap.put("sueDist", distance);
		attributeMap.put("inkyDist", distance);
		ArrayList<String> direction = new ArrayList<>();
		direction.add("UP");
		direction.add("DOWN");
		direction.add("RIGHT");
		direction.add("NEUTRAL");
		direction.add("LEFT");
		attributeMap.put("inkyDir", direction);
		attributeMap.put("pinkyDir", direction);
		attributeMap.put("blinkyDir", direction);
		attributeMap.put("sueDir", direction);
	}

	/**
	 * Splits the sata into sets for training and testing.
	 * Picks random entries for the training set and the rest is left for testing.
	 * Uses pre-defined split 80/20 for the sets
	 */
	private void createDataSets() {
		Random rnd = new Random();
		int nbrOfDataSetsTraining = (int)(savedData.size() * 0.8);
		dataSetTest = new ArrayList<>();
		dataSetTraining = new ArrayList<>();


		for (int i = 0; i < nbrOfDataSetsTraining; i++) {
			int randomSample = rnd.nextInt(savedData.size());
			dataSetTraining.add(savedData.remove(randomSample));
		}
		dataSetTest.addAll(savedData);
	}



	/**
	 * Creates the tree
	 * @param dataSetTraining Data set for training
	 * @param attributeList List of different attributes
	 * @return The root node
	 */
	public Node buildTree(ArrayList<DataTuple> dataSetTraining, ArrayList<String> attributeList) {
		// Create Node N
		Node n = new Node();
		// Get first
		MOVE move = dataSetTraining.get(0).DirectionChosen;

		// Check if there is only one class represented
		if (checkSameClass(dataSetTraining)) {
			n.setLabel(move.toString());
			return n;
		}

		// If attribute list is empty
		if (attributeList.isEmpty()) {
			n.setLabel(majorityClass(dataSetTraining).toString());
			return n;
		}
		// Call the attribute selection method on D and the attribute list, in order to choose the current attribute A:
		String s = attributeSelection(dataSetTraining, attributeList);
		// Label N as A and remove A from the attribute list.
		n.setLabel(s);
		attributeList.remove(s);

		// For each value in subset of A
		// Separate all tuples in D so that attribute A takes the value aj, creating the subset Dj
		ArrayList<String> valuesInA = attributeMap.get(s);
		for (String valueOfA : valuesInA) {
			ArrayList<DataTuple> TS = new ArrayList<>();
			for (DataTuple dt : dataSetTraining) {
				if (dt.getAttributeValue(s).equals(valueOfA)) {
					TS.add(dt);
				}
			}
			// If Dj is empty, add a child node to N labeled with the majority class in D
			if (TS.isEmpty()) {
				n.addChild(valueOfA, new Node(majorityClass(dataSetTraining).toString()));
			}
			// Otherwise, add the resulting node from calling generateTree(Dj,attribute) as a child node to N.
			else {
				n.addChild(valueOfA, buildTree(TS, (ArrayList<String>) attributeList.clone()));

			}
		}
		// Return N
		return n;

	}


	/**
	 * Checks if all entries are the same
	 * @param dataSetTraining The data set
	 * @return True / False
	 */
	public boolean checkSameClass(ArrayList<DataTuple> dataSetTraining) {
		MOVE move = savedData.get(0).DirectionChosen;
		for (DataTuple dt : dataSetTraining) {
			if (dt.DirectionChosen != move) return false;
		}
		return true;
	}

	/**
	 * Checks for the dominant attribute in a  data set
	 * @param dataSetTraining The data set
	 * @return Most common attribute
	 */
	public MOVE majorityClass(ArrayList<DataTuple> dataSetTraining) {
		MOVE move = null;
		HashMap<MOVE, Integer> moveCounter = new HashMap<>(Map.of(MOVE.UP, 0, MOVE.LEFT, 0, MOVE.RIGHT, 0, MOVE.DOWN, 0, MOVE.NEUTRAL, 0));
		for (DataTuple dt : dataSetTraining) {
			MOVE key = dt.DirectionChosen;
			moveCounter.put(key, (moveCounter.get(key)+1));
		}
		int max = Collections.max(moveCounter.values());
		for (Map.Entry<MOVE, Integer> entry : moveCounter.entrySet()) {
			if (entry.getValue() == max) {
				move = entry.getKey();
			}
		}
		return move;
	}

	public String attributeSelection(ArrayList<DataTuple> dataSetTraining, ArrayList<String> attributesList) {
		String returnAttribute = "";
		double baseValue = Integer.MAX_VALUE;

		// Iterate attributes
		for (String attribute : attributesList) {
			double compareValue = 0.0;
			ArrayList<String> attributeValues = attributeMap.get(attribute);

			//Create HashMap to keep count of
			HashMap<String, Integer> valueMap = new HashMap<>();

			// Iterate values
			for (String aValue : attributeValues) {
				ArrayList<DataTuple> subSet = new ArrayList<>();
				valueMap.put(aValue, 0);

				//
				for (DataTuple dt : dataSetTraining) {
					if (Objects.equals(dt.getAttributeValue(attribute), aValue)) {
						valueMap.put(aValue, valueMap.get(aValue) +1 );
						subSet.add(dt);
					}
				}

				// Create sum of each move
				int up = 0, down = 0, left = 0, right = 0, neutral = 0;
				for (DataTuple dt : subSet) {
					switch (dt.DirectionChosen) {
						case UP -> up++;
						case DOWN -> down++;
						case LEFT -> left++;
						case RIGHT -> right++;
						case NEUTRAL -> neutral++;
					}
				}
				HashMap<String, Integer> counts = new HashMap<>(Map.of("up", up, "down", down, "left", left, "right", right, "neutral", neutral));
				for (int i : counts.values()) {
					if (i == 0) continue; // 0 in log gives NaN
					compareValue -= i * Math.log(i) / Math.log(2);
				}
			}
			// Select the best move
			if (compareValue < baseValue) {
				baseValue = compareValue;
				returnAttribute = attribute;
			}
		}
		return returnAttribute;
	}

	public static double log2Calculator(double d) {
		if (d <= 0) return 0;
		return Math.log(d) / Math.log(2);
	}

	public void validateTree(ArrayList<DataTuple> dataSet, String s){
		MOVE should, generated;
		double correct = 0;
		for (DataTuple dataTuple : dataSet) {
			should = dataTuple.DirectionChosen;
			generated = findMove(root, dataTuple);
			if (should.toString().equals(generated.toString())) {
				correct++;
			}
		}
		System.out.println(s + " accuracy: " + (correct / dataSet.size()));
	}

	/**
	 * Called to calculate move
	 * @param game A copy of the current game
	 * @param timeDue The time the next move is due
	 * @return
	 */
	public MOVE getMove(Game game, long timeDue)
	{
		//Place your game logic here to play the game as Ms Pac-Man
		myMove = findMove(root, new DataTuple(game, null));
		return myMove;
	}

	public MOVE findMove(Node node, DataTuple dt) {
		MOVE move;
		// Return move if leaf
		if (node.isLeaf()) return MOVE.valueOf(node.getLabel());
		else {
			String att = dt.getAttributeValue(node.getLabel());
			HashMap<String, Node> children = node.getChildren();
			Node child = children.get(att);
			move = findMove(child, dt);
		}
		return move;
	}
}

