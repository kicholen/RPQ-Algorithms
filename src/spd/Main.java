package spd;

import java.util.ArrayList;
import java.util.List;

import spd.utils.TasksManager;

public class Main {
	private final static String R_ALGORITHM = "R";
	private final static String SCHRAGE_ALGORITHM = "SCHRAGE";
	private final static String CARLIER_ALGORITHM = "CARLIER";
	
	public static void main(String[] args) {
		Main main = new Main();
		
		main.calculate(R_ALGORITHM, true);
		main.calculate(SCHRAGE_ALGORITHM, true);
		main.calculate(CARLIER_ALGORITHM, true);
	}
	
	private void calculate(String algorithmType, Boolean shouldPrintResult) {
		List<String> list = new ArrayList<String>();
		list.add("data/in50.txt");
		list.add("data/in100.txt");
		list.add("data/in200.txt");
		
		TasksManager tasksManager = new TasksManager();
		String algorithmResults = algorithmType + " algorithm:\n";
		
		int totalTime = 0;
		for (String fileName : list) {
			tasksManager.setDataFromFile(fileName);
			calculateUsingAlgorithmType(tasksManager, algorithmType);
			totalTime += tasksManager.getTasksTime();
			algorithmResults += fileName + ": " + tasksManager.getTasksTime() + "\n";
		}
		
		algorithmResults += "total time: " + totalTime + "\n\n";
		
		if (shouldPrintResult) {
			System.out.print(algorithmResults);
		}
	}
	
	private void calculateUsingAlgorithmType(TasksManager tasksManager, String type) {
		switch (type) {
		case R_ALGORITHM:
			tasksManager.sortByR();
			break;
		case SCHRAGE_ALGORITHM:
			tasksManager.sortBySchrage();
			break;
		case CARLIER_ALGORITHM:
			tasksManager.sortByCarlier();
			break;
		default:
			System.out.println("Wrong algorithm type: " + type);
			break;
		}
	}

}
