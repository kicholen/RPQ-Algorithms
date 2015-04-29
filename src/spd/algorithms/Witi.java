package spd.algorithms;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import spd.models.Disposable;
import spd.models.Task.TaskWitiModel;

public class Witi implements IAlgorithm, Disposable {
	private List<TaskWitiModel> _list;
	private Vector<Permutation> _permutationVector;
	private int _stepsAmount;
	protected PriorityQueue<TaskWitiModel> _priorityQueue;
	
	public void setData(List<TaskWitiModel> list) {
		_list = list;
		_stepsAmount = (int) Math.pow(2, list.size()) - 1;
		_permutationVector = new Vector<Permutation>();
		_permutationVector.setSize(_stepsAmount);
		for (int i = 1; i <= _stepsAmount; i++) {
			_permutationVector.add(i, new Permutation(0, 0, 0));
		}
	}
	
	// http://dominik.zelazny.staff.iiar.pwr.wroc.pl/materialy/Algorytm_programowania_dynamicznego_-_wiTi.pdf
	public int calculate() {
		int tardinessMin = Integer.MAX_VALUE;
		int tardiness = 0;
		int combinationSeries = 0;
		int power = 0;
		int currentTime = 0;
		int lastTaskIndex = 0;
		int taskIndex = 0;
		
		for (int i = 1; i <= _stepsAmount; i++) {
			combinationSeries = (int) (Math.log(i) / Math.log(2));
			tardinessMin = Integer.MAX_VALUE;
			for (int j = 0; j <= combinationSeries; j++) {
				power = (int) Math.pow(2, j);
				tardiness = 0;
				currentTime = countTime(i);
				int isInside = i & power;
				if (isInside > 0) {
					if (i - power > 0) {
						tardiness += _permutationVector.get(i - power).getTardiness();
					}
					tardiness += calculateTardiness(j, currentTime) * _list.get(j).getWeight();
					if (tardinessMin > tardiness) {
						tardinessMin = tardiness;
						lastTaskIndex = i - power;
						taskIndex = j;
					}
				}
			}
			
			_permutationVector.get(i).setTardiness(tardinessMin);
			_permutationVector.get(i).setLastTaskIndex(lastTaskIndex);
			_permutationVector.get(i).setTaskIndex(taskIndex);
		}
		
		printResult(tardinessMin);
		
		return 0;
	}
	
	private void printResult(int tardinessMin) {
		System.out.println("Optimum " + tardinessMin);
		int lastIndex = _stepsAmount;
		int resultArray[] = new int[_list.size()];
		
		for (int z = _list.size(); z > 0; z--) {
			resultArray[z - 1] = _permutationVector.get(lastIndex).getTaskIndex() + 1; 
			lastIndex = _permutationVector.get(lastIndex).getLastTaskIndex();
		}
		
		for (int i = 0; i < resultArray.length; i++) {
			System.out.print(resultArray[i] + " ");
		}
		System.out.print("\n");
	}
	
	private int calculateTardiness(int taskIndex, int currentTime) {
		return Math.max(0, currentTime - _list.get(taskIndex).getDeadlineTime());
	}
	
	private int countTime(int index) {
		int time = 0;
		int taskIndex;
		for (int i = 0; i <= Math.log(index) / Math.log(2); i++) {
			taskIndex = 1 << i;
			int isInside = index & taskIndex;
			if (isInside > 0) {
				time += _list.get(i).getExecutionTime();
			}
		}
		
		return time;
	}

	@Override
	public void dispose() {
		if (_list != null) {
			_list.clear();
			_list = null;
		}
		if (_permutationVector != null) {
			_permutationVector.clear();
			_permutationVector = null;
		}
	}
}

class Permutation {
	private int _tardiness;
	private int _lastTaskIndex;
	private int _taskIndex;
	
	public Permutation(int tardiness, int lastTaskIndex, int taskIndex) {
		_tardiness = tardiness;
		_lastTaskIndex = lastTaskIndex;
		_taskIndex = taskIndex;
	}
	
	public int getTardiness() {
		return _tardiness;
	}
	
	public void setTardiness(int value) {
		_tardiness = value;
	}
	
	public int getLastTaskIndex() {
		return _lastTaskIndex;
	}
	
	public void setLastTaskIndex(int value) {
		_lastTaskIndex = value;
	}
	
	public int getTaskIndex() {
		return _taskIndex;
	}
	
	public void setTaskIndex(int value) {
		_taskIndex = value;
	}
}
