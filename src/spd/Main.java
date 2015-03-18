package spd;

import java.util.ArrayList;
import java.util.List;

import spd.utils.AlgorithmTypes;
import spd.utils.TasksManager;

public class Main {
	private final static String R_ALGORITHM = "R";
	private final static String SCHRAGE_ALGORITHM = "SCHRAGE";
	private final static String CARLIER_ALGORITHM = "CARLIER";
	private final static String WITI_ALGORITHM = "WITI";
	private final static String INSA_ALGORITHM = "INSA";
	private final static String NEH_ALGORITHM = "NEH";
	
	private static TasksManager _tasksManager;
	
	public static void main(String[] args) {
		Main main = new Main();
		_tasksManager = new TasksManager();
		/*
		main.calculate(R_ALGORITHM, AlgorithmTypes.RPQ, true);
		main.calculate(SCHRAGE_ALGORITHM, AlgorithmTypes.RPQ, true);
		main.calculate(CARLIER_ALGORITHM, AlgorithmTypes.RPQ, true);
		
		main.calculate(WITI_ALGORITHM, AlgorithmTypes.WITI, true);
		*/
		
		main.calculate(NEH_ALGORITHM, AlgorithmTypes.NEH, true);
	}

	
	
	private void calculate(String algorithmType, AlgorithmTypes type, Boolean shouldPrintResult) {
		_tasksManager.setAlgorithmType(type);
		List<String> list = new ArrayList<String>();
		switch (type) {
		case RPQ:
			list.add("data/in50.txt");
			list.add("data/in100.txt");
			list.add("data/in200.txt");
			break;
		case WITI:
			list.add("data/witi/data10.txt");
			/*list.add("data/witi/data11.txt");
			list.add("data/witi/data12.txt");
			list.add("data/witi/data13.txt");
			list.add("data/witi/data14.txt");
			list.add("data/witi/data15.txt");
			list.add("data/witi/data16.txt");
			list.add("data/witi/data17.txt");
			list.add("data/witi/data18.txt");
			list.add("data/witi/data19.txt");
			list.add("data/witi/data20.txt");*/
			break;
		case INSA:
			
			break;
		case NEH:
			for (int i = 1; i <= 10; i++) {
				list.add(getFormattedNehString(i));
			}
			
			break;
		}
		calculate(list, algorithmType, shouldPrintResult);
	}
	
	private String getFormattedNehString(int i) {
		String value = "";
		if (i <= 9) {
			value = "00" + i;
		}
		else if (i <= 99) {
			value = "0" + i;
		}
		else {
			value += i;
		}
		return "data/neh/data" + value + ".txt";
	}
	
	private void calculate(List<String> list, String algorithmType, Boolean shouldPrintResult) {
		String algorithmResults = algorithmType + " algorithm:\n";
		
		int totalTime = 0;
		for (String fileName : list) {
			_tasksManager.setDataFromFile(fileName);
			calculateUsingAlgorithmType(algorithmType);
			totalTime += _tasksManager.getTasksTime();
			algorithmResults += fileName + ": " + _tasksManager.getTasksTime() + "\n";
		}
		
		algorithmResults += "total time: " + totalTime + "\n\n";
		
		if (shouldPrintResult) {
			System.out.print(algorithmResults);
		}
	}
	
	private void calculateUsingAlgorithmType(String type) {
		switch (type) {
		case R_ALGORITHM:
			_tasksManager.sortByR();
			break;
		case SCHRAGE_ALGORITHM:
			_tasksManager.sortBySchrage();
			break;
		case CARLIER_ALGORITHM:
			_tasksManager.sortByCarlier();
			break;
		case WITI_ALGORITHM:
			_tasksManager.sortByWiti();
			break;
		case NEH_ALGORITHM:
			_tasksManager.sortByNeh();
			break;
		case INSA_ALGORITHM:
			//_tasksManager.sortByWiti();
			break;
		default:
			System.out.println("Wrong algorithm type: " + type);
			break;
		}
	}

}
