package spd.models.Task;

import java.util.Vector;

import spd.models.Copyable;

public class TaskNehModel implements Copyable<TaskNehModel> {
	public static int MACHINES_COUNT;
	private Vector<Integer> _executionTimes;
	private int _index;
	
	public TaskNehModel() {
		
	}
	
	public TaskNehModel(int index, Vector<Integer> executionTimes) {
		_index = index;
		_executionTimes = executionTimes;
	}
	
	public int getIndex() {
		return _index;
	}
	
	public int getExecutionTimeSum() {
		int sum = 0;
		for (Integer value : _executionTimes) {
			sum += value;
		}
		
		return sum;
	}
	
	public Vector<Integer> getExecutionTimes() {
		return _executionTimes;
	}
	
	public int getExecutionTime(int index) {
		return _executionTimes.get(index);
	}
	
	public void setExecutionTime(int index, int value) {
		_executionTimes.set(index, value);
	}
	
	@Override
	public TaskNehModel getCopy() {
		TaskNehModel copy = createNewForCopy();
		copy.fromObject(this);
		return copy;
	}

	@Override
	public TaskNehModel createNewForCopy() {
		return new TaskNehModel();
	}

	@Override
	public void fromObject(TaskNehModel object) {
		if (_executionTimes == null) {
			_executionTimes = new Vector<Integer>();
		}
		for (Integer value : object.getExecutionTimes()) {
			_executionTimes.add(new Integer(value));
		}
		_index = object.getIndex();
	}
}
