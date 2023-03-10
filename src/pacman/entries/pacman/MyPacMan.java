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

		return null;
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
}