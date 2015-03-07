package spd.algorithms;

import java.util.List;

import spd.models.Task.TaskModel;

public interface IAlgorithm {
	void setData(List<TaskModel> list);
	void calculate();
}
