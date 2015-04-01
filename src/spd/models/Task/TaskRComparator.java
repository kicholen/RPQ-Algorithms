package spd.models.Task;

import java.util.Comparator;

public class TaskRComparator implements Comparator<TaskRPQModel> {
	public int compare(TaskRPQModel firstTask, TaskRPQModel secondTask) {
		return firstTask.r() - secondTask.r();
	}
}
