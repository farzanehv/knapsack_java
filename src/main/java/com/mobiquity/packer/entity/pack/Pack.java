package com.mobiquity.packer.entity.pack;

import java.util.Arrays;

public class Pack {

	// Things we could select for each pack
	private PackData[] packDataList;

	private int limitedWeight;
	private int[] indexList;
	private float[] weightList;
	private int[] costList;

	public Pack(int limitedWeight, PackData[] packDataList) {
		super();

		this.limitedWeight = limitedWeight;

		int packDataListSize = packDataList.length;

		this.packDataList = packDataList;

		Arrays.sort(this.packDataList, new SortByWeightAscending());

		this.indexList = new int[packDataListSize];
		this.weightList = new float[packDataListSize];
		this.costList = new int[packDataListSize];

		for (int i = 0; i < packDataListSize; i++) {

			PackData packData = this.packDataList[i];
			this.indexList[i] = packData.getIndex();
			this.weightList[i] = packData.getWeight();
			this.costList[i] = packData.getCost();
		}
	}

	public int getLimitedWeight() {
		return limitedWeight;
	}

	public int[] getIndexList() {
		return indexList;
	}

	public float[] getWeightList() {
		return weightList;
	}

	public int[] getCostList() {
		return costList;
	}
}
