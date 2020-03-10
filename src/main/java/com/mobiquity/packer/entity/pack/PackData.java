package com.mobiquity.packer.entity.pack;

import java.util.Comparator;

public class PackData {

	public PackData(int index, float weight, int cost) {
		super();
		this.index = index;
		this.weight = weight;
		this.cost = cost;
	}

	int index;
	float weight;
	int cost;

	public int getIndex() {
		return index;
	}

	public float getWeight() {
		return weight;
	}

	public int getCost() {
		return cost;
	}

}

class SortByWeightAscending implements Comparator<PackData> {

	@Override
	public int compare(PackData p1, PackData p2) {
		float difference = p1.weight - p2.weight;

		if (difference < 0)
			return -1;
		else if (difference == 0)
			return 0;
		else
			return 1;
	}

}
