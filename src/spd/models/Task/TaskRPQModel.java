package spd.models.Task;

import spd.models.Copyable;


public class TaskRPQModel implements Copyable<TaskRPQModel> {
	private int _r;
	private int _p;
	private int _q;
	private int _startTime;
	
	public TaskRPQModel() {
		
	}
	
	public TaskRPQModel(int r, int p, int q) {
		_r = r;
		_p = p;
		_q = q;
		_startTime = 0;
	}
	
	public int r() {
		return _r;
	}
	
	public int p() {
		return _p;
	}
	
	public int q() {
		return _q;
	}
	
	public int startTime() {
		return _startTime;
	}
	
	public void setStartTime(int value) {
		_startTime = value;
	}
	
	/**
	 * Caution advised, used only by SchragePrmtS
	 * @param value
	 */
	public void setP(int value) {
		_p = value;
	}
	
	/**
	 * Caution advised, used only by Carlier
	 * @param value
	 */
	public void setR(int value) {
		_r = value;
	}
	
	public void setQ(int value) {
		_q = value;
	}

	@Override
	public TaskRPQModel getCopy() {
		TaskRPQModel copy = createNewForCopy();
		copy.fromObject(this);
		return copy;
	}

	@Override
	public TaskRPQModel createNewForCopy() {
		return new TaskRPQModel();
	}
	
	@Override
	public void fromObject(TaskRPQModel object) {
		_r = object.r();
		_p = object.p();
		_q = object.q();
		_startTime = object.startTime();
	}
}
