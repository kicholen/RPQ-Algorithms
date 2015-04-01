package spd.algorithms;

import java.awt.Point;
import java.util.List;

import spd.models.Disposable;
import spd.models.Carlier.CarlierModel;
import spd.models.Task.TaskRPQModel;

public class Carlier implements IAlgorithm, Disposable {
	private List<TaskRPQModel> _list;
	private CarlierModel _model;
	private Schrage _schrage;
	private SchragePrmtS _schragePrmts;
	
	public Carlier() {
		
	}
	
	public void setData(List<TaskRPQModel> list) {
		_list = list;
		_model = new CarlierModel();
		_model.setTasksList(list);
	}

	//http://dominik.zelazny.staff.iiar.pwr.wroc.pl/materialy/Algorytm_Carlier.pdf
	//http://mariusz.makuchowski.staff.iiar.pwr.wroc.pl/download/courses/sterowanie.procesami.dyskretnymi/lab.instrukcje/lab05.carlier/literatura/algorytm.C.%5bMBwZSZ%5d.pdf
	//http://mariusz.makuchowski.staff.iiar.pwr.wroc.pl/download/courses/sterowanie.procesami.dyskretnymi/lab.instrukcje/lab04.schrage/literatura/
	@Override
	public int calculate() {
		CarlierModel carlier = new CarlierModel();
		carlier = _model.getCopy();
		return calculateCarlier(carlier, Integer.MAX_VALUE);
	}
	
	public int calculateCarlier(CarlierModel carlier, int upperBoundValue) {
		int currentUpperBoundValue = getSchrageTotalTime(carlier.getTasksList());
		if (currentUpperBoundValue < upperBoundValue) {
			upperBoundValue = currentUpperBoundValue;
			_list.clear();
			for (TaskRPQModel model : carlier.getTasksList()) {
				_list.add(model.getCopy());
			}
		}
		carlier.setBlockRange(findBlockRange(carlier.getTasksList()));
		carlier.setReferenceTaskIndex(findReferenceTaskIndex(carlier.getTasksList(), carlier.getBlockRange()));
		
		if (carlier.getReferenceTaskIndex() != -1) {
			int minRInBlock = findMinRInRange(carlier.getTasksList(), carlier.getBlockRange());
			int minQInBlock = findMinQInRange(carlier.getTasksList(), carlier.getBlockRange());
			int sumPInBlock = getPSumInRange(carlier.getTasksList(),  carlier.getBlockRange());
			int oldR = carlier.getTasksList().get(carlier.getReferenceTaskIndex()).r();
			carlier.getTasksList().get(carlier.getReferenceTaskIndex()).setR(Math.max(carlier.getTasksList().get(carlier.getReferenceTaskIndex()).r(), minRInBlock + sumPInBlock));
			
			int lowerBoundValue = getSchragePrmtSTotalTime(carlier.getTasksList());
			if (lowerBoundValue < upperBoundValue) {
				CarlierModel copiedCarlier = carlier.getCopy();
				calculateCarlier(copiedCarlier.getCopy(), upperBoundValue);
				copiedCarlier.dispose();
				copiedCarlier = null;
				carlier.getTasksList().get(carlier.getReferenceTaskIndex()).setR(oldR);
			}
			
			minQInBlock = findMinQInRange(carlier.getTasksList(), carlier.getBlockRange());
			sumPInBlock = getPSumInRange(carlier.getTasksList(),  carlier.getBlockRange());
			int oldQ = carlier.getTasksList().get(carlier.getReferenceTaskIndex()).q();
			carlier.getTasksList().get(carlier.getReferenceTaskIndex()).setQ(Math.max(carlier.getTasksList().get(carlier.getReferenceTaskIndex()).q(), minQInBlock + sumPInBlock));
			lowerBoundValue = getSchragePrmtSTotalTime(carlier.getTasksList());
			if (lowerBoundValue < upperBoundValue) {
				CarlierModel copiedCarlier = carlier.getCopy();
				calculateCarlier(copiedCarlier, upperBoundValue);
				copiedCarlier.dispose();
				copiedCarlier = null;
				carlier.getTasksList().get(carlier.getReferenceTaskIndex()).setQ(oldQ);
			}
		}
		
		return upperBoundValue;
	}
	
	
	private int getSchrageTotalTime(List<TaskRPQModel> list) {
		if (_schrage == null) {
			_schrage = new Schrage();
		}
		_schrage.setData(list);
		
		return _schrage.calculate();
	}
	
	private int getSchragePrmtSTotalTime(List<TaskRPQModel> list) {
		if (_schragePrmts == null) {
			_schragePrmts= new SchragePrmtS();
		}
		_schragePrmts.setData(list);
		
		return _schragePrmts.calculate();
	}
	
	private int findMinRInRange(List<TaskRPQModel> list, Point blockRange) {
		int value = list.get(blockRange.x).r();
		for (int index = blockRange.x; index <= blockRange.y; index++) {
			value = Math.min(value, list.get(index).r());
		}
		
		return value;
	}
	
	private int findMinQInRange(List<TaskRPQModel> list, Point blockRange) {
		int value = list.get(blockRange.x).q();
		for (int index = blockRange.x; index <= blockRange.y; index++) {
			value = Math.min(value, list.get(index).q());
		}
		
		return value;
	}
	
	private int getPSumInRange(List<TaskRPQModel> list, Point blockRange) {
		int value = 0;
		for (int index = blockRange.x; index <= blockRange.y; index++) {
			value += list.get(index).p();
		}
		
		return value;
	}
	
	private int findReferenceTaskIndex(List<TaskRPQModel> list, Point blockRange) {
		for (int index = blockRange.y - 1; index >= blockRange.x; index--) {
			if (list.get(index).q() < list.get(blockRange.y).q()) {
				return index;
			}
		}
		return -1;
	}
	
	private Point findBlockRange(List<TaskRPQModel> list) {
		Point range = new Point();
		int maxTime = 0;
		
		for (int index = 0; index < list.size(); index++) {
			int taskCompleteTime = list.get(index).startTime() + list.get(index).p() + list.get(index).q();
			if (taskCompleteTime > maxTime) {
				range.y = index;
				maxTime = taskCompleteTime;
			}
		}
		range.x = range.y;
		
		while (range.x >= 1 && list.get(range.x - 1).startTime() + list.get(range.x - 1).p() >= list.get(range.x).r()) {
			range.x -= 1;
		}
		
		return range;
	}

	@Override
	public void dispose() {
		_model = null;
		if (_schrage != null) {
			_schrage.dispose();
			_schrage = null;
		}
		if (_schragePrmts != null) {
			_schragePrmts.dispose();
			_schragePrmts = null;
		}
	}
}
