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

	private void initAttributes() {
		ArrayList<String> edible = new ArrayList<>();
		edible.add("YES");
		edible.add("NO");
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

	public String attributeSelection(ArrayList<DataTuple> dataSetTraining, ArrayList<String> attributesList) {
		return null;
	}

	public static void main(String[] args) {
		MyPacMan myPacMan = new MyPacMan();
		generateDataSet();
		System.out.println(calculateEntropy(UP));
	}
}
