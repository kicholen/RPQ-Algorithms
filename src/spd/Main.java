package spd;

import java.util.ArrayList;
import java.util.List;

import spd.utils.TasksManager;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Main main = new Main();
		
		main.test();
	}
	
	private void test() {
		List<String> list = new ArrayList<String>();
		//list.add("data/in07.txt");
		list.add("data/in50.txt");
		list.add("data/in100.txt");
		list.add("data/in200.txt");
		
		TasksManager tasksManager = new TasksManager();
		
		int totalTime = 0;
		for (String fileName : list) {
			tasksManager.setDataFromFile(fileName);
			//System.out.println(fileName + ": " + tasksManager.getTasksTime());
			//tasksManager.sortByR();
			tasksManager.sortBySchrage();
			System.out.println(fileName + ": " + tasksManager.getTasksTime());
			totalTime += tasksManager.getTasksTime();
		}
		System.out.println("total time: " + totalTime);
		//Schrage schrage = new Schrage("../data/in07.txt")
		//schrage.calculate();
		//schrage.print();
	}

}
