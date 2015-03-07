package spd.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import spd.algorithms.Schrage;
import spd.models.Task.TaskModel;
import spd.models.Task.TaskRComparator;

public class TasksManager {
	private List<TaskModel> _tasksList;
	private int _tasksCount;
	private int _whatsThatFor;
	
	public TasksManager() {
		reset();
	}
	
	public void setDataFromFile(String fileName) {
		reset();
		
		List<Integer> dataList = FileUtils.loadFile(fileName);
		parseDataFromFile(dataList);
	}
	
	public int getTasksTime() {
		int time = _tasksList.get(0).r();
		int totalTime = 0;
		for (int i = 0; i < _tasksCount; i++) {
			time += _tasksList.get(i).p();
			totalTime = Math.max(totalTime, time + _tasksList.get(i).q());
			if (i < _tasksCount - 1) {
				time = Math.max(time, _tasksList.get(i + 1).r());
			}
		}
		
		return totalTime;
	}
	
	public void sortByR() {	
		Collections.sort(_tasksList, new TaskRComparator());
	}
	
	public void sortBySchrage() {
		Schrage schrageAlgorithm = new Schrage();
		schrageAlgorithm.setData(_tasksList);
		schrageAlgorithm.calculate();
		schrageAlgorithm.dispose();
	}
	
	private void parseDataFromFile(List<Integer> list) {
		Iterator<Integer> listIterator = list.iterator();
		_tasksCount = listIterator.next();
		_whatsThatFor = listIterator.next();
		
		while (listIterator.hasNext()) {
			TaskModel model = new TaskModel(listIterator.next(), listIterator.next(), listIterator.next());
			_tasksList.add(model);
		}
	}
	
	private void reset() {
		if (_tasksList != null) {
			_tasksList.clear();
			_tasksList = null;
		}
		_tasksList = new ArrayList<TaskModel>();
	}
	
	public List<TaskModel> getTaskVector() {
		return _tasksList;
	}
}
