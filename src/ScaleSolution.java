import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScaleSolution {
	
	// Created by Sathya Prakash on May 23rd, 2018.
	// Application reads scale values from text file and then produces balanced scale.

	static Map<String, Integer> ScaleValueHolder = new HashMap<String, Integer>();
    static ArrayList<ArrayList<String>> ScaleInputParamters = new ArrayList<ArrayList<String>>();
	static ArrayList<ArrayList<String>> ScaleOutputParamters = new ArrayList<ArrayList<String>>();
	// static ArrayList<String> TempScaleParameters = new ArrayList<String>();

	static void RemoveCommentsAndOrganizeInput(File file) throws IOException {
		ArrayList<String> SingleRowOfScales = new ArrayList<String>();
		BufferedReader fileData = new BufferedReader(new FileReader(file));
		String row = null;
		while ((row = fileData.readLine()) != null) {
			if (!row.substring(0, 1).equals("#")) {
				ScaleInputParamters.add(ObtainInduvidualScaleValuesRemoveCommas(row));
			}
		}

	}
	
	// Method to display values within a Array List

	static void DisplayValuesFromList(ArrayList<ArrayList<String>> AllScaleValues) {
		ArrayList<String> TempRowValue;
		Iterator iterator = AllScaleValues.iterator();
		while (iterator.hasNext()) {
			System.out.println((ArrayList) iterator.next());
		}
	}

	// Sort values within text file for processing, remove commas, create ArrayList
	
	static ArrayList ObtainInduvidualScaleValuesRemoveCommas(String RowOfScales) {
		String RowOfScaleValues = RowOfScales;
		ArrayList<String> OneRowOfScales = new ArrayList<String>();
		for (int i = 0; i < 2; i++) {

			OneRowOfScales.add(RowOfScaleValues.substring(0, RowOfScaleValues.indexOf(",")));
			RowOfScaleValues = RowOfScaleValues.substring(RowOfScaleValues.indexOf(",") + 1, RowOfScaleValues.length());
		}

		OneRowOfScales.add(RowOfScaleValues);
		return OneRowOfScales;

	}

	// Perform balancing logic
	
	static ArrayList GenerateBalancingAct(ArrayList<String> BalanceRows) {

		ArrayList<String> TempScaleParameters = new ArrayList<String>();
		int LeftValue = 0, RightValue = 0;
		boolean fixed = false;
		String scaleName = BalanceRows.get(0);

		if (isInteger(BalanceRows.get(1))) {
			LeftValue = Integer.parseInt(BalanceRows.get(1));
		} else {
			LeftValue = 1 + ScaleValueHolder.get(BalanceRows.get(1));
		}

		if (isInteger(BalanceRows.get(2))) {
			RightValue = Integer.parseInt(BalanceRows.get(2));
		} else {
			RightValue = 1 + ScaleValueHolder.get(BalanceRows.get(2));
		}

		int total = 0;

		if (LeftValue > RightValue) {
			total = LeftValue + RightValue + (LeftValue - RightValue);
			RightValue = LeftValue - RightValue;
			LeftValue = 0;
			fixed = true;
		}
		if (LeftValue < RightValue && fixed == false) {
			total = RightValue + LeftValue + (RightValue - LeftValue);
			LeftValue = RightValue - LeftValue;
			RightValue = 0;
			fixed = true;
		}
		if (LeftValue == RightValue && fixed == false) {
			total = RightValue + LeftValue;
			LeftValue = 0;
			RightValue = 0;

		}

		TempScaleParameters.add(scaleName.toString());
		TempScaleParameters.add(String.valueOf(LeftValue));
		TempScaleParameters.add(String.valueOf(RightValue));
		ScaleValueHolder.put(scaleName, total);
		return TempScaleParameters;

	}

	static boolean isInteger(String input) {
		try {
			Integer.parseInt(input);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	static void BalanceWeightScaleValues() {

		int index = ScaleInputParamters.size();
		while (index > 0) {
			ScaleOutputParamters.add(GenerateBalancingAct(ScaleInputParamters.get(index - 1)));
			index--;
		}
		Collections.reverse(ScaleOutputParamters);
	}

	public static void main(String args[]) throws IOException {

		// Read the scale input file. Change path value accordingly.
		BufferedReader readUserInput = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Type scale text file path:");
		File file = new File(readUserInput.readLine().toString());

		try {
			RemoveCommentsAndOrganizeInput(file);
			System.out.println("Scale values from text file, after removing comments");
			DisplayValuesFromList(ScaleInputParamters);
			BalanceWeightScaleValues();
			System.out.println("Scale Result values after applying balancing logic");
			DisplayValuesFromList(ScaleOutputParamters);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
