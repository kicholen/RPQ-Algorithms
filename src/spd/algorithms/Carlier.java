package spd.algorithms;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import spd.models.Carlier.CarlierModel;
import spd.models.Task.TaskModel;
import spd.models.Task.TaskQComparator;

public class Carlier implements IAlgorithm {
	private List<TaskModel> _list;
	//private List<TaskModel> _currentList;
	//private List<TaskModel> _optimalList;
	
	public Carlier() {
		
	}
	
	@Override
	public void setData(List<TaskModel> list) {
		_list = list;
		//copyList(_originalList, _currentList);
	}

	//http://dominik.zelazny.staff.iiar.pwr.wroc.pl/materialy/Algorytm_Carlier.pdf
	//http://mariusz.makuchowski.staff.iiar.pwr.wroc.pl/download/courses/sterowanie.procesami.dyskretnymi/lab.instrukcje/lab05.carlier/literatura/algorytm.C.%5bMBwZSZ%5d.pdf
	//http://mariusz.makuchowski.staff.iiar.pwr.wroc.pl/download/courses/sterowanie.procesami.dyskretnymi/lab.instrukcje/lab04.schrage/literatura/
	//https://github.com/sparkzi/Carlier/blob/master/CarlierProgram/CarlierProgram.cpp
	@Override
	public int calculate() {
		CarlierModel startCarlier = new CarlierModel();
		CarlierModel currentCarlier, optimalCarlier = null, potomek1, potomek2;
		startCarlier.setTasksList(_list);
		int upperBoundValue = getSchrageTotalTime(startCarlier.getTasksList());//Integer.MAX_VALUE;//getSchrageTotalTime(_list);
		startCarlier.setTotalTime(upperBoundValue);
		int lowerBoundValue = getSchragePrmtSTotalTime(startCarlier.getTasksList());
		//int targetValue = getSchrageTotalTime();
		
		PriorityQueue<CarlierModel> carlierHeap = new PriorityQueue<CarlierModel>();
		carlierHeap.add(startCarlier);
		optimalCarlier = startCarlier;
		
		while (!carlierHeap.isEmpty()) {
			currentCarlier = carlierHeap.poll();
			
			if (getSchragePrmtSTotalTime(currentCarlier.getTasksList()) < upperBoundValue) {
				startCarlier.setTotalTime(getSchrageTotalTime(currentCarlier.getTasksList()));
				
				if (currentCarlier.getTotalTime() < upperBoundValue) {
					upperBoundValue = currentCarlier.getTotalTime();
					optimalCarlier = currentCarlier;
				}
				
				currentCarlier.setBlockRange(findBlockRange(currentCarlier.getTasksList()));
				currentCarlier.setReferenceTaskIndex(findReferenceTaskIndex(currentCarlier.getTasksList(), currentCarlier.getBlockRange()));
				
				if (currentCarlier.getReferenceTaskIndex() != -1) {
					// elimination tests
					eliminationTests(currentCarlier, upperBoundValue);
					potomek1 = potomek2 = currentCarlier;
					//copyList(currentCarlier.getTasksList(), potomek1.getTasksList());
					//copyList(currentCarlier.getTasksList(), potomek2.getTasksList());
					
					int minRInBlock = findMinRInRange(potomek1.getTasksList(), potomek1.getBlockRange());
					int pSumInBlock = getPSumInRange(potomek1.getTasksList(),  potomek1.getBlockRange());
					potomek1.getTasksList().get(potomek1.getReferenceTaskIndex()).setR(minRInBlock + pSumInBlock);
					
					int minQInBlock = findMinQInRange(potomek2.getTasksList(), potomek2.getBlockRange());
					pSumInBlock = getPSumInRange(potomek2.getTasksList(),  potomek2.getBlockRange());
					potomek2.getTasksList().get(potomek2.getReferenceTaskIndex()).setQ(minQInBlock + pSumInBlock);
					
					potomek1.setLowerBoundFixed(getLowerBoundFixed(potomek1.getTasksList(), potomek1.getReferenceTaskIndex(), potomek1.getBlockRange()));
					potomek2.setLowerBoundFixed(getLowerBoundFixed(potomek2.getTasksList(), potomek1.getReferenceTaskIndex(), potomek2.getBlockRange()));

					carlierHeap.add(potomek1);
					carlierHeap.add(potomek2);
				}
			}
		}
		
		//_list = optimalCarlier.getTasksList();
		System.out.println(optimalCarlier.getTasksList().size());
		//copyList(optimalCarlier.getTasksList(), _list);
		_list = optimalCarlier.getTasksList();
		System.out.println(optimalCarlier.getTasksList().size());
		System.out.println(_list.size());
		return 0;
	}
	
	private void copyList(List<TaskModel> source, List<TaskModel> destination) {
		if (destination != null) {
			destination.clear();
		}
		else {
			destination = new ArrayList<TaskModel>();
		}
		destination.addAll(source);
	}
	
	private int getSchrageTotalTime(List<TaskModel> list) {
		Schrage schrageAlgorithm = new Schrage();
		schrageAlgorithm.setData(list);
		int totalTime = schrageAlgorithm.calculate();
		schrageAlgorithm.dispose();
		schrageAlgorithm = null;
		
		return totalTime;
	}
	
	private int getSchragePrmtSTotalTime(List<TaskModel> list) {
		SchragePrmtS schrageAlgorithm = new SchragePrmtS();
		schrageAlgorithm.setData(list);
		int totalTime = schrageAlgorithm.calculate();
		schrageAlgorithm.dispose();
		schrageAlgorithm = null;
		
		return totalTime;
	}
	
	private void eliminationTests(CarlierModel model, int upperBoundValue) {
		List<TaskModel> list = model.getTasksList();
		int heightKula = getHeight(list, new Point(model.getReferenceTaskIndex() + 1, model.getBlockRange().y));
		
		for (int index = 0; index < model.getTasksList().size(); index++) {
			if ((index < model.getBlockRange().x || index > model.getBlockRange().y) && list.get(index).p() > upperBoundValue - heightKula) {
				if (list.get(index).r() + list.get(index).p() + list.get(model.getBlockRange().y).q() + getPSumInRange(list, new Point(model.getReferenceTaskIndex() + 1, model.getBlockRange().y)) >= upperBoundValue) { //c+1,b
					list.get(index).setR(Math.max(list.get(index).r(), findMinRInRange(list, new Point(model.getReferenceTaskIndex() + 1, model.getBlockRange().y)) + getPSumInRange(list, new Point(model.getReferenceTaskIndex() + 1, model.getBlockRange().y))));
				}
				else if (findMinRInRange(list, new Point(model.getReferenceTaskIndex() + 1, model.getBlockRange().y)) + list.get(index).p() + list.get(index).q() + getPSumInRange(list, new Point(model.getReferenceTaskIndex() + 1, model.getBlockRange().y)) >= upperBoundValue) { //c+1,b
					list.get(index).setQ(Math.max(list.get(index).q(), findMinQInRange(list, new Point(model.getReferenceTaskIndex() + 1, model.getBlockRange().y)) + getPSumInRange(list, new Point(model.getReferenceTaskIndex() + 1, model.getBlockRange().y))));
				}
			}
		}
	}
	
	private int findMinRInRange(List<TaskModel> list, Point blockRange) {
		int value = list.get(blockRange.x).r();
		for (int index = blockRange.x; index <= blockRange.y; index++) {
			value = Math.min(value, list.get(index).r());
		}
		
		return value;
	}
	
	private int findMinQInRange(List<TaskModel> list, Point blockRange) {
		int value = list.get(blockRange.x).q();
		for (int index = blockRange.x; index <= blockRange.y; index++) {
			value = Math.min(value, list.get(index).q());
		}
		
		return value;
	}
	
	private int getLowerBoundFixed(List<TaskModel> list, int referenceTaskIndex, Point blockRange) {
		return Math.max(getHeight(list, new Point(referenceTaskIndex + 1, blockRange.y)), getHeight(list, new Point(referenceTaskIndex, blockRange.y)));
	}
	
	private int getHeight(List<TaskModel> list, Point range) {
		return findMinRInRange(list, range) + findMinQInRange(list, range) + getPSumInRange(list, range);
	}
	
	private int getPSumInRange(List<TaskModel> list, Point blockRange) {
		int value = 0;
		for (int index = blockRange.x; index <= blockRange.y; index++) {
			value += list.get(index).p();
		}
		
		return value;
	}
	
	private int findReferenceTaskIndex(List<TaskModel> list, Point blockRange) {
		for (int index = blockRange.y - 1; index >= blockRange.x; index--) {
			if (list.get(index).q() < list.get(blockRange.y).q()) {
				return index;
			}
		}
		return -1;
	}
	
	private Point findBlockRange(List<TaskModel> list) {
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
}
