package spd.models.Task;

import java.util.Comparator;

public class TaskRComparator implements Comparator<TaskModel> {
	public int compare(TaskModel firstTask, TaskModel secondTask) {
		return firstTask.r() - secondTask.r();
	}
}
