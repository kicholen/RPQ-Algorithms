package spd.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import spd.algorithms.Carlier;
import spd.algorithms.Neh;
import spd.algorithms.Schrage;
import spd.algorithms.SchrageOnVector;
import spd.algorithms.SchragePrmtS;
import spd.algorithms.Witi;
import spd.models.Task.TaskNehModel;
import spd.models.Task.TaskRPQModel;
import spd.models.Task.TaskRComparator;
import spd.models.Task.TaskWitiModel;

public class TasksManager {
	private List<TaskRPQModel> _tasksListRPQ;
	private List<TaskWitiModel> _tasksListWiti;
	private List<TaskNehModel> _tasksListNeh;
	
	private int _tasksCount;
	private AlgorithmTypes _algorithmType;
	
	private Neh _nehAlgorithm;
	
	public TasksManager() {
		reset();
	}
	
	public void setAlgorithmType(AlgorithmTypes type) {
		_algorithmType = type;
	}
	
	public void setDataFromFile(String fileName) {
		reset();
		
		List<Integer> dataList = FileUtils.loadFile(fileName);
		parseDataFromFile(dataList);
	}
	
	public int getTasksTime() {
		switch (_algorithmType) {
			case RPQ:
				return getRPQTaskTime();
			case WITI:
				return getWitiTaskTime();
			case NEH:
				return getNehTaskTime();
			default:
				System.out.println("Don't forget to set algorithmType");
				return 0;
		}
	}
	
	public void sortByR() {	
		Collections.sort(_tasksListRPQ, new TaskRComparator());
	}
	
	public void sortBySchrage() {
		Schrage schrageAlgorithm = new Schrage();
		schrageAlgorithm.setData(_tasksListRPQ);
		schrageAlgorithm.calculate();
		schrageAlgorithm.dispose();
		schrageAlgorithm = null;
	}
	
	public void sortBySchrageOnVector() {
		SchrageOnVector schrageAlgorithm = new SchrageOnVector();
		schrageAlgorithm.setData(_tasksListRPQ);
		schrageAlgorithm.calculate();
		schrageAlgorithm.dispose();
		schrageAlgorithm = null;
	}
	
	public void sortBySchragePrmtS() {
		SchragePrmtS schrageAlgorithm = new SchragePrmtS();
		schrageAlgorithm.setData(_tasksListRPQ);
		System.out.println(schrageAlgorithm.calculate());
		schrageAlgorithm.dispose();
		schrageAlgorithm = null;
	}
	
	public void sortByCarlier() {
		Carlier carlierAlgorithm = new Carlier();
		carlierAlgorithm.setData(_tasksListRPQ);
		carlierAlgorithm.calculate();
		carlierAlgorithm.dispose();
		carlierAlgorithm = null;
	}
	
	public void sortByWiti() {
		Witi witiAlgorithm = new Witi();
		witiAlgorithm.setData(_tasksListWiti);
		witiAlgorithm.calculate();
		witiAlgorithm.dispose();
		witiAlgorithm = null;
	}
	
	public void sortByNeh() {
		if (_nehAlgorithm == null) {
			_nehAlgorithm = new Neh();
		}
		else {
			_nehAlgorithm.dispose();
		}
		_nehAlgorithm.setData(_tasksListNeh);
		_nehAlgorithm.calculate();
	}
	
	private int getNehTaskTime() {
		if (_nehAlgorithm != null) {
			return _nehAlgorithm.getMinSpan();
		}
		else {
			return 0;	
		}
	}
	
	private int getWitiTaskTime() {
		return 0;
	}
	
	private int getRPQTaskTime() {
		int time = _tasksListRPQ.get(0).r();
		int totalTime = 0;
		for (int i = 0; i < _tasksCount; i++) {
			time += _tasksListRPQ.get(i).p();
			totalTime = Math.max(totalTime, time + _tasksListRPQ.get(i).q());
			if (i < _tasksCount - 1) {
				time = Math.max(time, _tasksListRPQ.get(i + 1).r());
			}
		}
		
		return totalTime;
	}
	
	private void parseDataFromFile(List<Integer> list) {
		switch (_algorithmType) {
			case RPQ:
				parseRPQData(list);
				break;
			case WITI:
				parseWitiData(list);
				break;
			case NEH:
				parseNehData(list);
				break;
			default:
				System.out.println("Don't forget to set algorithmType");
		}
	}
	
	private void parseRPQData(List<Integer> list) {
		Iterator<Integer> listIterator = list.iterator();
		_tasksCount = listIterator.next();
		listIterator.next();
		
		while (listIterator.hasNext()) {
			TaskRPQModel model = new TaskRPQModel(listIterator.next(), listIterator.next(), listIterator.next());
			_tasksListRPQ.add(model);
		}
	}
	
	private void parseWitiData(List<Integer> list) {
		Iterator<Integer> listIterator = list.iterator();
		_tasksCount = listIterator.next();
		int index = 1;
		
		while (listIterator.hasNext()) {
			TaskWitiModel model = new TaskWitiModel(index, listIterator.next(), listIterator.next(), listIterator.next());
			_tasksListWiti.add(model);
			index++;
		}
	}
	
	private void parseNehData(List<Integer> list) {
		Iterator<Integer> listIterator = list.iterator();
		_tasksCount = listIterator.next();
		int machineCount = listIterator.next();
		int index = 1;
		
		for (int j = 0; j < _tasksCount; j++) {
			Vector<Integer> executionTimes = new Vector<Integer>();
			for (int i = 0; i < machineCount; i++) {
				executionTimes.add(listIterator.next());
			}
			TaskNehModel model = new TaskNehModel(index, executionTimes);
			_tasksListNeh.add(model);
			index++;
		}
	}
	
	private void reset() {
		if (_tasksListRPQ != null) {
			_tasksListRPQ.clear();
			_tasksListRPQ = null;
		}
		_tasksListRPQ = new ArrayList<TaskRPQModel>();
		
		if (_tasksListWiti != null) {
			_tasksListWiti.clear();
			_tasksListWiti = null;
		}
		_tasksListWiti = new ArrayList<TaskWitiModel>();
		
		if (_tasksListNeh != null) {
			_tasksListNeh.clear();
			_tasksListNeh = null;
		}
		_tasksListNeh = new ArrayList<TaskNehModel>();
	}
}