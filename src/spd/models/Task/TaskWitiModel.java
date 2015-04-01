package spd.models.Task;

public class TaskWitiModel {
	private int _index;
	private int _executionTime;
	private int _weight;
	private int _deadlineTime;
	
	public TaskWitiModel(int index, int executionTime, int weight, int deadlineTime) {
		_index = index;
		_executionTime = executionTime;
		_weight = weight;
		_deadlineTime = deadlineTime;
	}
	
	public int getIndex() {
		return _index;
	}
	
	public int getExecutionTime() {
		return _executionTime;
	}
	
	public int getWeight() {
		return _weight;
	}
	
	public int getDeadlineTime() {
		return _deadlineTime;
	}
}
