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
		root.printTree(0, root);
		validateTree(dataSetTraining, "Training");
		validateTree(dataSetTest, "Testing");

	}

	public static void main(String[] args) {
		MyPacMan myPacMan= new MyPacMan();
	}


	/**
	 * Creates lists of attributes and adds them to hashmap
	 */
	private void initAttributes() {
		attributeMap = new HashMap<>();

		ArrayList<String> edible = new ArrayList<>();
		edible.add("true");
		edible.add("false");

		attributeMap.put("isBlinkyEdible",edible);
		attributeMap.put("isPinkyEdible",edible);
		attributeMap.put("isSueEdible",edible);
		attributeMap.put("isInkyEdible",edible);

		ArrayList<String> distance = new ArrayList<>();
		distance.add("VERY_LOW");
		distance.add("LOW");
		distance.add("MEDIUM");
		distance.add("VERY_HIGH");
		distance.add("HIGH");
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


		ArrayList<String> pills = new ArrayList<>();
		pills.add("VERY_HIGH");
		pills.add("HIGH");
		pills.add("MEDIUM");
		pills.add("LOW");
		pills.add("VERY_LOW");
		pills.add("NONE");
		attributeMap.put("numOfPillsLeft", pills);

		ArrayList<String> powerPills = new ArrayList<>();
		powerPills.add("VERY_HIGH");
		powerPills.add("HIGH");
		powerPills.add("MEDIUM");
		powerPills.add("LOW");
		powerPills.add("VERY_LOW");
		attributeMap.put("numOfPowerPillsLeft", powerPills);


		ArrayList<String> currentScore = new ArrayList<>();
		currentScore.add("VERY_HIGH");
		currentScore.add("HIGH");
		currentScore.add("MEDIUM");
		currentScore.add("LOW");
		currentScore.add("VERY_LOW");
		attributeMap.put("currentScore", currentScore);


		ArrayList<String> pacmanPosition = new ArrayList<>();
		pacmanPosition.add("VERY_HIGH");
		pacmanPosition.add("HIGH");
		pacmanPosition.add("MEDIUM");
		pacmanPosition.add("LOW");
		pacmanPosition.add("VERY_LOW");
		attributeMap.put("pacmanPosition", pacmanPosition);


		ArrayList<String> gameTime = new ArrayList<>();
		gameTime.add("VERY_HIGH");
		gameTime.add("HIGH");
		gameTime.add("MEDIUM");
		gameTime.add("LOW");
		gameTime.add("VERY_LOW");
		attributeMap.put("gameTime", gameTime);


		ArrayList<String> totalNumberOfPills = new ArrayList<>();
		totalNumberOfPills.add("220");
		totalNumberOfPills.add("240");
		attributeMap.put("totalNumberOfPills", totalNumberOfPills);

		ArrayList<String> totalNumberOfPowerPills = new ArrayList<>();
		totalNumberOfPowerPills.add("4");
		attributeMap.put("totalNumberOfPowerPills", totalNumberOfPowerPills);

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
	 * Recursively creates the decision tree
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
				//System.out.println(s);
				//System.out.println(dt.getAttributeValue(s));
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
	 * Checks if all entries in a data set are the same
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
	 * @param dataSet The data set
	 * @return Most common attribute
	 */
	public MOVE majorityClass(ArrayList<DataTuple> dataSet) {
		MOVE move = null;
		HashMap<MOVE, Integer> moveCounter = new HashMap<>(Map.of(MOVE.UP, 0, MOVE.LEFT, 0, MOVE.RIGHT, 0, MOVE.DOWN, 0, MOVE.NEUTRAL, 0));
		for (DataTuple dt : dataSet) {
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

	/**
	 * This method performs attribute selection and returns the selected attribute
	 * @param dataSetTrainingLocal Data set
	 * @param attributesList List of attributes
	 * @return
	 */
	public String attributeSelection(ArrayList<DataTuple> dataSetTrainingLocal, ArrayList<String> attributesList) {
		String returnAttribute = "";
		//System.out.println("Ok");
		double baseValue = 0.0;

		// Calculate the entropy of the original dataset
		double entropy = calculateEntropy(dataSetTraining);
		//System.out.println(entropy + " Entropy full set" + ", Dataset Size: " + dataSetTraining.size() + " Attributes: " + attributesList.size());

		// Iterate attributes
		for (String attribute : attributesList) {
			// Initialize informationGain to the entropy of the dataset
			double sumOfSubsetEntropy = 0;

			// Get the list of possible values for this attribute
			ArrayList<String> attributeValues = attributeMap.get(attribute);

			// Create HashMap to keep count of the number of occurrences of each attribute value
			HashMap<String, Integer> valueMap = new HashMap<>();

			// Iterate over the values of this attribute
			for (String attributeVal : attributeValues) {
				// Create a subset of the training data containing only tuples with this attribute value
				ArrayList<DataTuple> subSet = new ArrayList<>();
				// Count the number of tuples with this attribute value
				valueMap.put(attributeVal, 0);
				// Add tuples with this attribute value to the subset
				for (DataTuple dt : dataSetTrainingLocal) {
					if (Objects.equals(dt.getAttributeValue(attribute), attributeVal)) {
						valueMap.put(attributeVal, (valueMap.get(attributeVal) +1));
						subSet.add(dt);
					}
				}
				// Calculate the entropy of the subset (a value of an attribute)
				double subSetEntropy = calculateEntropy(subSet);
				// Calculate the sum of entropy for all subsets
				sumOfSubsetEntropy -= (((double) subSet.size() / (double)dataSetTrainingLocal.size()) * subSetEntropy);
			}
			//System.out.println("RESULT FOR ATTRIBUTE: " + attribute);
			//System.out.println("ENTROPY(S):" + entropy);
			//System.out.println("SUM OF ENTROPY(Subsets) " + attribute + "=" + sumOfSubsetEntropy);
			if(sumOfSubsetEntropy > 0) {
				System.out.println("ERROR: SUM OF ENTROPY(SUBSETS) IS GREATER THAN ENTROPY(S)");
			}
			double infoGain = entropy + sumOfSubsetEntropy;
			//System.out.println(attribute + " --> INFOGAIN:" + infoGain + " BASE:" + baseValue);
			if (infoGain >= baseValue) {
				baseValue = infoGain;
				returnAttribute = attribute;
			}
		}
		// Return the selected attribute
		return returnAttribute;
	}

	/**
	 *Calculates the entropy of the given dataset based on the number of occurrences of each move.
	 *@param dataSet The dataset to calculate the entropy of
	 *@return The entropy of the dataset
	 */
	public double calculateEntropy(ArrayList<DataTuple> dataSet){
		// Create sum of each move
		double up = 0, down = 0, left = 0, right = 0, neutral = 0;
		for (DataTuple dt : dataSet) {
			switch (dt.DirectionChosen) {
				case UP -> up++;
				case DOWN -> down++;
				case LEFT -> left++;
				case RIGHT -> right++;
				case NEUTRAL -> neutral++;
			}
		}
		// Put values in ArrayList to be iterable
		ArrayList<Double> counts = new ArrayList<>(Arrays.asList(up, down, left, right, neutral));
		double total = up + down + left + right + neutral;
		double entropy = 0.0;

		// e.g E = - (2/22 * lg2(2/22)) - (6/22 * lg2(6/22)) - (14/22 * lg2(14/22))
		for (double i : counts) {
			if (i == 0) continue; // 0 in log gives NaN
			double d = i / total;
			entropy -= d * Math.log(d) / Math.log(2);
		}
		return entropy;
	}

	/**
	 * Method to validate the decision tree by testing it against a given dataset
	 * and printing the accuracy of the predictions
	 * @param dataSet The dataset to test the decision tree on
	 * @param s String for which data set is being tested ("Training" or "Testing")
	 */
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
		// System.out.println("NODE: " + node.getLabel() + " IS LEAF: " + node.isLeaf() + " " + dt.getAttributeValue(node.getLabel()));
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

