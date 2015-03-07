package spd.models.Task;


public class TaskModel {
	private int _r;
	private int _p;
	private int _q;
	
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
	
}
