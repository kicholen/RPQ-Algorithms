package spd.models.Task;

import java.util.Comparator;

public class TaskNehComparator implements Comparator<TaskNehModel> {
	public int compare(TaskNehModel firstTask, TaskNehModel secondTask) {
		return secondTask.getExecutionTimeSum() - firstTask.getExecutionTimeSum();
	}
}
