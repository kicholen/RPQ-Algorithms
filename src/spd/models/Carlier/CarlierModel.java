package spd.models.Carlier;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import spd.models.Copyable;
import spd.models.Disposable;
import spd.models.Task.TaskRPQModel;

public class CarlierModel implements Comparable<CarlierModel>, Copyable<CarlierModel>, Disposable {
	private List<TaskRPQModel> _tasksList;
	private int _totalTime;
	private int _lowerBound;
	private int _lowerBoundFixed;
	private Point _blockRange;
	private int _referenceTaskIndex;
	
	public CarlierModel() {
		_blockRange = new Point(0, 0);
	}
	
	public void setTasksList(List<TaskRPQModel> list) {
		_tasksList = list;
	}
	
	public List<TaskRPQModel> getTasksList() {
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
	
	public int getLowerBound() {
		return _lowerBound;
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

	@Override
	public CarlierModel getCopy() {
		CarlierModel copy = createNewForCopy();
		copy.fromObject(this);
		return copy;
	}

	@Override
	public CarlierModel createNewForCopy() {
		return new CarlierModel();
	}

	@Override
	public void fromObject(CarlierModel object) {
		if (_tasksList == null) {
			_tasksList = new ArrayList<TaskRPQModel>();
		}
		for (TaskRPQModel model : object.getTasksList()) {
			_tasksList.add(model.getCopy());
		}
		_totalTime = object.getTotalTime();
		_lowerBound = object.getLowerBound();
		_lowerBoundFixed = object.getLowerBoundFixed();
		_blockRange = new Point(object.getBlockRange().x, object.getBlockRange().y);
		_referenceTaskIndex = object.getReferenceTaskIndex();
	}

	@Override
	public void dispose() {
		if (_tasksList != null) {
			_tasksList.clear();
			_tasksList = null;
		}
		
		_blockRange = null;
	}
}
