package spd.models.Task;


public class TaskModel {
	private int _r;
	private int _p;
	private int _q;
	private int _startTime;
	
	public TaskModel(int r, int p, int q) {
		_r = r;
		_p = p;
		_q = q;
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
}
