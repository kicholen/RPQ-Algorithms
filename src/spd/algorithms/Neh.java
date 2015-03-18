package spd.algorithms;

import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Vector;

import spd.models.Disposable;
import spd.models.Task.TaskNehComparator;
import spd.models.Task.TaskNehModel;

public class Neh implements IAlgorithm, Disposable {
	private PriorityQueue<TaskNehModel> _queue;
	private int _minSpan;

	public void setData(List<TaskNehModel> list) {
		_queue = new PriorityQueue<TaskNehModel>(list.size(), new TaskNehComparator());
		for (TaskNehModel model : list) {
			_queue.add(model);
		}
	}
	
	// http://www.ijceronline.com/papers/Vol2_issue6/R0260950100.pdf - neh
	// http://www.ioz.pwr.wroc.pl/pracownicy/kuchta/Marek%20Sobolewski_FlowShop.pdf - neh
	@Override
	public int calculate() {
		Vector<TaskNehModel> minSpanVector = null;
		Vector<TaskNehModel> mehVector = new Vector<TaskNehModel>();
		mehVector.add(_queue.poll());
		mehVector.add(_queue.poll());
		Vector<TaskNehModel> copiedVector = copyVector(mehVector);

		if (makeSpan(copyVector(mehVector)) > makeSpan(swap(copiedVector, 0, 1))) {
			swap(mehVector, 0, 1);
		}
		
		while(!_queue.isEmpty()) {
			Vector<TaskNehModel> clonedVector = copyVector(mehVector);
			clonedVector.add(0, _queue.poll());
			_minSpan = makeSpan(copyVector(clonedVector));
			minSpanVector = copyVector(clonedVector);
            for (int j = 0; j < clonedVector.size() - 1; j++) {
            	int swapedSpan = makeSpan(copyVector(swap(clonedVector, j, j + 1)));
                if (_minSpan > swapedSpan) {
                	minSpanVector = copyVector(clonedVector);
                	_minSpan = swapedSpan;
                }
            }
            mehVector = minSpanVector;
        }
		
		return _minSpan;
	}
	
	public int getMinSpan() {
		return _minSpan;
	}
	
	private Vector<TaskNehModel> swap(Vector<TaskNehModel> vector, int i, int j) {
		Collections.swap(vector, i, j);
		return vector;
	}

	private int makeSpan(Vector<TaskNehModel> vector) {
		int i = 0;
		int j = 0;
		for (TaskNehModel model : vector) {
			j = 0;
			for (int executionTime : model.getExecutionTimes()) {
				int maxValue = Math.max(i - 1 < 0 ? 0 : vector.get(i - 1).getExecutionTime(j), j - 1 < 0 ? 0 : vector.get(i).getExecutionTime(j - 1));
				model.setExecutionTime(j, maxValue + executionTime);
				j++;
			}
			i++;
		}
		return vector.get(vector.size() - 1).getExecutionTime(TaskNehModel.MACHINES_COUNT - 1);
	}

	private Vector<TaskNehModel> copyVector(Vector<TaskNehModel> vector) {
		Vector<TaskNehModel> copy = new Vector<TaskNehModel>(vector.size());
		for (TaskNehModel model : vector) {
			copy.add(model.getCopy());
		}
		
		return copy;
	}

	@Override
	public void dispose() {
		if (_queue != null) {
			_queue.clear();
			_queue = null;
		}
	}

}
