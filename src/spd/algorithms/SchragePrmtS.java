package spd.algorithms;

import spd.models.Task.TaskRPQModel;

public class SchragePrmtS extends Schrage {
	private int _totalTime;
	
	public SchragePrmtS() {
		super();
		_totalTime = 0;
	}
	
	public int getTotalTime() {
		System.out.println(_totalTime);
		return _totalTime;
	}
	
	@Override
	public int calculate() {
		int currentTime = 0;
		
		TaskRPQModel taskE;
		TaskRPQModel taskL = new TaskRPQModel(0, 0, Integer.MAX_VALUE);
		
		while (!_tasksG.isEmpty() || !_tasksN.isEmpty()) {
			while (!_tasksN.isEmpty() && _tasksN.peek().r() <= currentTime) {
				taskE = _tasksN.poll();
				_tasksG.add(taskE);
				if (taskE.q() > taskL.q()) {
					taskL.setP(currentTime - taskE.r());
					currentTime = taskE.r();
					if (taskL.p() > 0) {
						_tasksG.add(taskL);
					}
				}
			}
			
			if (!_tasksG.isEmpty()) {
				taskE = _tasksG.poll();
				taskL = taskE;
				currentTime += taskE.p();
				_totalTime = Math.max(_totalTime, currentTime + taskE.q());
			}
			else if (!_tasksN.isEmpty()){
				currentTime = _tasksN.peek().r();
			}
		}
		
		return _totalTime;
	}
	
}
