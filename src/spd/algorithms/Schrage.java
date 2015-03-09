package spd.algorithms;

import java.util.List;
import java.util.PriorityQueue;

import spd.models.Disposable;
import spd.models.Task.TaskModel;
import spd.models.Task.TaskQComparator;
import spd.models.Task.TaskRComparator;

public class Schrage implements IAlgorithm, Disposable {

	protected List<TaskModel> _list;
	protected PriorityQueue<TaskModel> _tasksN;
	protected PriorityQueue<TaskModel> _tasksG;
	
	public Schrage() {
		
	}

	@Override
	public void setData(List<TaskModel> list) {
		_list = list;
		
		_tasksN = new PriorityQueue<TaskModel>(list.size(), new TaskRComparator());
		for (TaskModel model : list) {
			_tasksN.add(model.getCopy());
		}
		_tasksG = new PriorityQueue<TaskModel>(list.size(), new TaskQComparator());
	}

	@Override
	public int calculate() {
		_list.clear();
		int currentTime = 0;
		int totalTime = 0;
		TaskModel taskE;
		
		while (!_tasksG.isEmpty() || !_tasksN.isEmpty()) {
			while (!_tasksN.isEmpty() && _tasksN.peek().r() <= currentTime) {
				taskE = _tasksN.poll();
				_tasksG.add(taskE);
			}
			
			if (!_tasksG.isEmpty()) {
				taskE = _tasksG.poll();
				taskE.setStartTime(currentTime);
				_list.add(taskE);
				currentTime += taskE.p();
				totalTime = Math.max(totalTime, currentTime + taskE.q());
			}
			else if (!_tasksN.isEmpty()){
				currentTime = _tasksN.peek().r();
			}
		}
		
		return totalTime;
	}
	
	@Override
	public void dispose() {
		if (_tasksN != null) {
			_tasksN.clear();
			_tasksN = null;
		}
		if (_tasksG != null) {
			_tasksG.clear();
			_tasksG = null;
		}
	}
}
