package spd.models.Task;

import java.util.Comparator;

public class TaskQComparator implements Comparator<TaskRPQModel> {
	public int compare(TaskRPQModel firstTask, TaskRPQModel secondTask) {
		return secondTask.q() - firstTask.q();
	}
}
