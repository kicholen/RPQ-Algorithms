package spd.models.Carlier;

import java.awt.Point;
import java.util.List;

import spd.models.Task.TaskModel;

public class CarlierModel implements Comparable<CarlierModel> {
	private List<TaskModel> _tasksList;
	private int _totalTime;
	private int _loweBound;
	private int _lowerBoundFixed;
	private Point _blockRange;
	private int _referenceTaskIndex;
	
	public CarlierModel() {
		
	}
	
	public void setTasksList(List<TaskModel> list) {
		_tasksList = list;
	}
	
	public List<TaskModel> getTasksList() {
		return _tasksList;
	}

	@Override
	public int compareTo(CarlierModel model) {
		if (getLowerBoundFixed() == model.getLowerBoundFixed()) {
			return 0;
		}
		else {
			return getLowerBoundFixed() < model.getLowerBoundFixed() ? 1 : -1;			
		}
	}
	
	public void setReferenceTaskIndex(int value) {
		_referenceTaskIndex = value;
	}
	
	public int getReferenceTaskIndex() {
		return _referenceTaskIndex;
	}
	
	public void setBlockRange(Point value) {
		_blockRange = value;
	}
	
	public Point getBlockRange() {
		return _blockRange;
	}
	
	public int getLowerBoundFixed() {
		return _lowerBoundFixed;
	}
	
	public void setLowerBoundFixed(int value) {
		_lowerBoundFixed = value;
	}
	
	public void setTotalTime(int value) {
		_totalTime = value;
	}
	
	public int getTotalTime() {
		return _totalTime;
	}
}
