package spd.algorithms;

import java.util.List;
import java.util.Vector;

import spd.models.Disposable;
import spd.models.Task.TaskRPQModel;

public class SchrageOnVector implements IAlgorithm, Disposable {
	protected List<TaskRPQModel> _list;
	protected Vector<TaskRPQModel> _tasksN;
	protected Vector<TaskRPQModel> _tasksG;
	
	public SchrageOnVector() {
		
	}

	public void setData(List<TaskRPQModel> list) {
		_list = list;
		
		_tasksN = new Vector<TaskRPQModel>(list.size());//, new TaskRComparator());
		for (TaskRPQModel model : list) {
			_tasksN.add(model.getCopy());
		}
		_tasksG = new Vector<TaskRPQModel>(list.size());//, new TaskQComparator());
	}

	@Override
	public int calculate() {
		_list.clear();
		int currentTime = 0;
		int totalTime = 0;
		TaskRPQModel taskE;
		
		while (!_tasksG.isEmpty() || !_tasksN.isEmpty()) {
			while (!_tasksN.isEmpty() && _tasksN.get(getMinRIndex(_tasksN)).r() <= currentTime) {
				taskE = _tasksN.remove(getMinRIndex(_tasksN));
				_tasksG.add(taskE);
			}
			
			if (!_tasksG.isEmpty()) {
				taskE = _tasksG.remove(getMaxQIndex(_tasksG));
				taskE.setStartTime(currentTime);
				_list.add(taskE);
				currentTime += taskE.p();
				totalTime = Math.max(totalTime, currentTime + taskE.q());
			}
			else if (!_tasksN.isEmpty()){
				currentTime = _tasksN.get(getMinRIndex(_tasksN)).r();
			}
		}
		
		return totalTime;
	}
	
	private int getMinRIndex(Vector<TaskRPQModel> vector) {
		int minR = Integer.MAX_VALUE;
		int index = 0;
		
		for (int i = 0; i < vector.size(); i++) {
			if (vector.get(i).r() < minR) {
				minR = vector.get(i).r();
				index = i;
			}
		}
		
		return index;
	}
	
	private int getMaxQIndex(Vector<TaskRPQModel> vector) {
		int maxQ = -Integer.MAX_VALUE;
		int index = 0;
		
		for (int i = 0; i < vector.size(); i++) {
			if (vector.get(i).q() > maxQ) {
				maxQ = vector.get(i).q();
				index = i;
			}
		}
		
		return index;
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
