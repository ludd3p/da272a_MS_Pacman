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
	 * todo add input parameter
	 * todo calculate entropy for each of the target classes in data set
	 * // Entropy(S) = - ∑ pᵢ * log₂(pᵢ) ; i = 4 (UP,DOWN,LEFT,RIGHT)
	 * @return
	 */
	public static double calculateEntropy(){
		String[][] dataset = generateDataSet();
		// 26 entries
		// 8 attributes = 4 ghosts * 2 (edible, distance)
		// 4 classes = up, down, left, right
		double entropy = 0;
		double left = 8;
		double total = 14;
		double down = 6;
		entropy = -(left/total) * log2(left/total) - ((down/total) * log2(down/total));
		return entropy;
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
		HashMap<MOVE, Integer> moveCounter = new HashMap<>(Map.of(MOVE.UP, 0, MOVE.LEFT, 0, MOVE.RIGHT, 0, MOVE.DOWN, 0, MOVE.NEUTRAL, 0));
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
		System.out.println(calculateEntropy());
	}
}