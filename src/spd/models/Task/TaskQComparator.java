package spd.models.Task;

import java.util.Comparator;

public class TaskQComparator implements Comparator<TaskModel> {
	public int compare(TaskModel firstTask, TaskModel secondTask) {
		return secondTask.q() - firstTask.q();
	}
}
