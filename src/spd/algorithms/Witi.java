package spd.algorithms;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import spd.models.Disposable;
import spd.models.Task.TaskQComparator;
import spd.models.Task.TaskRPQModel;
import spd.models.Task.TaskWitiComparator;
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
		for (int i = 0; i < _stepsAmount; i++) {
			_permutationVector.add(i, new Permutation(0,0));
		}
		
		/*_priorityQueue = new PriorityQueue<TaskWitiModel>(list.size(), new TaskWitiComparator());
		for (TaskWitiModel model : _list) {
			_priorityQueue.add(new TaskWitiModel(model.getIndex(), model.getExecutionTime(), model.getWeight(), model.getDeadlineTime()));
		}
		while(!_priorityQueue.isEmpty()) {
			System.out.print(_priorityQueue.poll().getIndex() + " ");
		}
		//for (TaskWitiModel value : _priorityQueue) {
		//}
		System.out.print("\n");*/
	}
	
	// http://dominik.zelazny.staff.iiar.pwr.wroc.pl/materialy/Algorytm_programowania_dynamicznego_-_wiTi.pdf
	
	// http://staff.iiar.pwr.wroc.pl/wojciech.bozejko/papers/2009/Automatyka_2009_2.pdf - insa
	// http://www.ijceronline.com/papers/Vol2_issue6/R0260950100.pdf - neh
	// http://www.ioz.pwr.wroc.pl/pracownicy/kuchta/Marek%20Sobolewski_FlowShop.pdf - neh
	
	// http://www.ptzp.org.pl/files/konferencje/kzz/artyk_pdf_2013/p051.pdf
	public int calculate() {
		int tardinessMin = Integer.MAX_VALUE;
		int tardiness = 0;
		int combinationSeries = 0;
		int power = 0;
		int currentTime = 0;
		int lastTaskIndex = 0;
		
		for (int i = 1; i <= _stepsAmount; i++) {
			combinationSeries = (int) (Math.log(i) / Math.log(2));
			tardinessMin = Integer.MAX_VALUE;
			System.out.println(combinationSeries);
			for (int j = 0; j <= combinationSeries; j++) {
				power = (int) Math.pow(2, j);
				tardiness = 0;
				currentTime = countTime(i);
				int asd = i & power;
				if (asd > 0) {
					if (i - power > 0) {
						tardiness += _permutationVector.get(i - power - 1).getTardiness();
					}
					tardiness += calculateTardiness(j, currentTime);
					if (tardinessMin > tardiness) {
						tardinessMin = tardiness;
						lastTaskIndex = power;
					}
				}
			}
			
			_permutationVector.get(i - 1).setTardiness(tardinessMin);
			_permutationVector.get(i - 1).setLastTaskIndex(lastTaskIndex);
		}
		
		sortList(lastTaskIndex);
		
		return 0;
	}
	
	private void sortList(int lastTaskIndex) {
		int step = _stepsAmount + 1;
		int taskIndex = 0;
		Vector<Integer> kolejnosc = new Vector<Integer>();
		kolejnosc.setSize(_list.size());
		for (int i = _list.size(); i > 0; i--) {
			taskIndex = _permutationVector.get(step - 2).getLastTaskIndex();
			kolejnosc.set(i - 1, (int) (Math.log(taskIndex) / Math.log(2) + 1));
			step = step - taskIndex;
		}
		
		for (Integer value : kolejnosc) {
	        System.out.print(value + " ");
		}
		System.out.print("\n");
	}
	
	private int calculateTardiness(int taskIndex, int currentTime) {
		return Math.max(0, currentTime - _list.get(taskIndex).getDeadlineTime()) * _list.get(taskIndex).getWeight();
	}
	
	private int countTime(int index) {
		int time = 0;
		int taskIndex;
		for (int i = 0; i <= Math.log(index) / Math.log(2); i++) {
			taskIndex = 1 << i;
			int asd = index & taskIndex;
			if (asd > 0) {
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
	
	public Permutation(int tardiness, int lastTaskIndex) {
		_tardiness = tardiness;
		_lastTaskIndex = lastTaskIndex;
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
}
