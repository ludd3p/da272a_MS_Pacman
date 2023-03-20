package pacman.entries.pacman;

import dataRecording.DataSaverLoader;
import dataRecording.DataTuple;
import pacman.controllers.Controller;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.*;

import static pacman.game.Constants.MOVE.*;


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
	private int maximumDistance = 150;
	public MyPacMan(){
		savedData = new ArrayList<>(List.of(DataSaverLoader.LoadPacManData()));
		createDataSets();
	}

<<<<<<< Updated upstream
	public MOVE getMove(Game game, long timeDue)
	{
		//Place your game logic here to play the game as Ms Pac-Man
		
		return myMove;
	}

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
=======
	public static void main(String[] args) {
		MyPacMan myPacMan= new MyPacMan();
	}

>>>>>>> Stashed changes

	private void initAttributes() {
<<<<<<< Updated upstream
		ArrayList<String> edible = new ArrayList<>();
		edible.add("YES");
		edible.add("NO");
		attributeMap.put("blinkyEdible",edible);
		attributeMap.put("pinkyEdible",edible);
		attributeMap.put("sueEdible",edible);
		attributeMap.put("inkyEdible",edible);
=======
		attributeMap = new HashMap<>();

		ArrayList<String> edible = new ArrayList<>();
		edible.add("true");
		edible.add("false");

		attributeMap.put("isBlinkyEdible",edible);
		attributeMap.put("isPinkyEdible",edible);
		attributeMap.put("isSueEdible",edible);
		attributeMap.put("isInkyEdible",edible);

>>>>>>> Stashed changes
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

		/*
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
		*/
		/*
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
		*/
		/*
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
		
		 */

	}

<<<<<<< Updated upstream
=======
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
>>>>>>> Stashed changes
	public Node buildTree(ArrayList<DataTuple> dataSetTraining, ArrayList<String> attributeList) {
		// Create Node N
		Node n = new Node();
		MOVE move = savedData.get(0).DirectionChosen;
		// Check if there is only one class represented
		if (!checkSameClass()) {
			n.setLabel(move.toString());
			return n;
		}
		// If attribute list is empty
		if (attributeList.isEmpty()) {
			n.setLabel(majorityClass(savedData).toString());
			return n;
		}
		// Supposedly ID3 algo goes here
		String s = attributeSelection(dataSetTraining, attributeList);
<<<<<<< Updated upstream

		// matrix representation of the data set containing 9 columns,
		// 1 for each attribute [0-8] where [8] is target class
		String[][] dataset = generateDataSet();
		return null;
	}
	public static String[][] generateDataSet(){
		DataTuple[] arr = DataSaverLoader.LoadPacManData();
		String[][] preProcessed = new String[arr.length][9];
		for (int i = 0; i < arr.length; i++) {
			String[] processed = new String[9];
			processed[0] = arr[i].discretizeDistance(arr[i].blinkyDist).toString();
			processed[1] = arr[i].blinkyDir.toString();
			processed[2] = arr[i].discretizeDistance(arr[i].inkyDist).toString();
			processed[3] = arr[i].inkyDir.toString();
			processed[4] = arr[i].discretizeDistance(arr[i].pinkyDist).toString();
			processed[5] = arr[i].pinkyDir.toString();
			processed[6] = arr[i].discretizeDistance(arr[i].sueDist).toString();
			processed[7] = arr[i].sueDir.toString();
			processed[8] = arr[i].DirectionChosen.toString();
			preProcessed[i] = processed;
=======
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
>>>>>>> Stashed changes
		}
		for (int i = 0; i <= 25 ; i++) {
			System.out.println(preProcessed[i][0] + " " + preProcessed[i][1] + " " + preProcessed[i][2] + " " + preProcessed[i][3] + " " + preProcessed[i][4] + " " + preProcessed[i][5] + " " + preProcessed[i][6] + " " + preProcessed[i][7] + " " + preProcessed[i][8]);
		}
		return preProcessed;
	}

	/**
	 * calculate entropy for the data set
	 * wip - boilerplate works.
	 * // Entropy(S) = - ∑ pᵢ * log₂(pᵢ) ; i = 4 (UP,DOWN,LEFT,RIGHT)
	 * @return
	 */
	public static double calculateEntropy(Enum<MOVE> targetClass){
		String[][] dataset = generateDataSet();

		double total = dataset.length;
		double upCount = 0, downcount = 0, leftCount = 0, rightCount = 0, neutralCount = 0;

		// count the number of each target class
		for (String[] row : dataset){
			String targetClassStr = row[8];
			switch (targetClassStr){
				case "UP" -> upCount++;
				case "DOWN" -> downcount++;
				case "LEFT" -> leftCount++;
				case "RIGHT" -> rightCount++;
				case "NEUTRAL" -> neutralCount++;
			}
		}
		System.out.println("TOTAL: " + dataset.length +
				"\n	UP: " + upCount +
				"\n	DOWN: " + downcount +
				"\n	LEFT: " + leftCount +
				"\n	RIGHT: " + rightCount +
				"\n	NEUTRAL: " + neutralCount);

		// calculate entropy for given targetClass (parameter) and return it
		if (targetClass.equals(UP)) {
			return -(upCount / total) * log2(upCount / total) - ((total - upCount) / total) * log2((total - upCount) / total);
		}
		else if (targetClass.equals(DOWN)) {
			return -(downcount / total) * log2(downcount / total) - ((total - downcount) / total) * log2((total - downcount) / total);
		}
		else if (targetClass.equals(LEFT)) {
			return -(leftCount / total) * log2(leftCount / total) - ((total - leftCount) / total) * log2((total - leftCount) / total);
		}
		else if (targetClass.equals(RIGHT)) {
			return -(rightCount / total) * log2(rightCount / total) - ((total - rightCount) / total) * log2((total - rightCount) / total);
		}
		else if (targetClass.equals(NEUTRAL)) {
			return -(neutralCount / total) * log2(neutralCount / total) - ((total - neutralCount) / total) * log2((total - neutralCount) / total);
		}
		else {
			return -1;
		}
	}

	/**
	 * calculate log2 N indirectly
	 * using log() method
	 * abstraction layer for readability
	 * @param N - number to calculate log2 of
	 * @return log2 N
	 */
	public static double log2(double N)
	{
		// calculate log2 N indirectly
		// using log() method
		double result = (Math.log(N) / Math.log(2));
		return result;
	}

	public boolean checkSameClass() {
		MOVE move = savedData.get(0).DirectionChosen;
		for (DataTuple dt : savedData) {
			if (dt.DirectionChosen != move) return false;
		}
		return true;
	}

	public MOVE majorityClass(ArrayList<DataTuple> tuples) {
		MOVE move = null;
		HashMap<MOVE, Integer> moveCounter = new HashMap<>(Map.of(UP, 0, LEFT, 0, MOVE.RIGHT, 0, DOWN, 0, MOVE.NEUTRAL, 0));
		for (DataTuple dt : tuples) {
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

<<<<<<< Updated upstream
	public String attributeSelection(ArrayList<DataTuple> dataSetTraining, ArrayList<String> attributesList) {
		return null;
	}

	public static void main(String[] args) {
		MyPacMan myPacMan = new MyPacMan();
		generateDataSet();
		System.out.println(calculateEntropy(UP));
=======
	/**
	 * This method performs attribute selection and returns the selected attribute
	 * @param dataSetTrainingLocal Data set
	 * @param attributesList List of attributes
	 * @return
	 */
	public String attributeSelection(ArrayList<DataTuple> dataSetTrainingLocal, ArrayList<String> attributesList) {
		String returnAttribute = "";
		double baseValue = 0.0;

		// Calculate the entropy of the original dataset
		double entropy = calculateEntropy(dataSetTraining);
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
>>>>>>> Stashed changes
	}
}
