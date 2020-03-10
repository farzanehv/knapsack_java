package com.mobiquity.packer.service.pack;

import java.util.ArrayList;
import java.util.List;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.entity.pack.Pack;
import com.mobiquity.packer.repository.PackRepository;
import com.mobiquity.packer.repository.PackRepositoryImpl;
import com.mobiquity.packer.service.outputgenerator.OutputGenerator;
import com.mobiquity.packer.service.outputgenerator.OutputGeneratorImpl;

public class PackServiceImpl implements PackService {

	// use PackRespository to load Packs data, then apply knapsack dynamic algorithm
	@Override
	public String pack(String filePath) throws APIException {

		PackRepository packRepository = new PackRepositoryImpl();

		List<Pack> loadedPacks = packRepository.loadPacks(filePath);

		StringBuilder result = new StringBuilder();

		loadedPacks.forEach(pack -> {
			try {
				result.append(knapsack(pack));
			} catch (APIException e) {
				throw new RuntimeException(e.getMessage());
			}
		});
		return result.toString();
	}

	// Thing weights are in float format like XX.xx to avoid casting and rounding
	// problems, I normalize them with multiplying by 100,
	private int[] normalizeThingsWeight(float[] thingsWeight) {
		int[] normalizedThingsWeight = new int[thingsWeight.length];
		for (int i = 0; i < thingsWeight.length; i++)
			normalizedThingsWeight[i] = (int) (thingsWeight[i] * 100);
		return normalizedThingsWeight;
	}

	// To normalize the LimitedWeight of pack
	private int getNormalizedLimitedWeight(Pack pack) {
		return pack.getLimitedWeight() * 100;
	}

	private int[][] initializeDynamicArray(int numOfThings, int normalizedLimitedWeight) {
		int b[][] = new int[numOfThings + 1][normalizedLimitedWeight + 1];

		for (int i = 0; i <= numOfThings; i++)
			for (int j = 0; j <= normalizedLimitedWeight; j++)
				b[i][j] = 0;
		return b;
	}

	// 0/1 knapsack algorithm, building dynamic array.
	private int[][] buildDynamicArray(int numOfThings, int normalizedLimitedWeight, int[] thingsWeight,
			int[] costList) {
		int dynamicArray[][] = initializeDynamicArray(numOfThings, normalizedLimitedWeight);

		for (int i = 1; i <= numOfThings; i++) {
			for (int j = 0; j <= normalizedLimitedWeight; j++) {
				dynamicArray[i][j] = dynamicArray[i - 1][j];

				if ((j > thingsWeight[i - 1])
						&& (dynamicArray[i][j] < dynamicArray[i - 1][j - thingsWeight[i - 1]] + costList[i - 1])) {
					dynamicArray[i][j] = dynamicArray[i - 1][j - thingsWeight[i - 1]] + costList[i - 1];
				}
			}
		}
		return dynamicArray;
	}

	// 0/1 kanpsack algorithm, find the indexes of results
	private List<Integer> findReasultArray(int numOfThings, int[][] dynamicArray, int normalizedLimitedWeight,
			int[] indx, int[] thingsWeight) {
		List<Integer> resultArray = new ArrayList<Integer>();

		while (numOfThings != 0) {
			if (dynamicArray[numOfThings][normalizedLimitedWeight] != dynamicArray[numOfThings
					- 1][normalizedLimitedWeight]) {
				resultArray.add(indx[numOfThings - 1]);
				normalizedLimitedWeight = (int) (normalizedLimitedWeight - thingsWeight[numOfThings - 1]);
			}
			numOfThings--;
		}
		return resultArray;
	}

	// 0/1 kanpsack dynamic algorithm, get pack and its related data and call the
	// other methods to find result
	private String knapsack(Pack pack) throws APIException {
		int[] thingsWeight = normalizeThingsWeight(pack.getWeightList());

		int costList[] = pack.getCostList();
		int normalizedLimitedWeight = getNormalizedLimitedWeight(pack);
		int numOfThings = pack.getIndexList().length;
		int[] indx = pack.getIndexList();

		int dynamicArray[][] = buildDynamicArray(numOfThings, normalizedLimitedWeight, thingsWeight, costList);

		List<Integer> resultArray = findReasultArray(numOfThings, dynamicArray, normalizedLimitedWeight, indx,
				thingsWeight);

		OutputGenerator outputGenerator = new OutputGeneratorImpl();
		return outputGenerator.generateOutput(resultArray);

	}

}
