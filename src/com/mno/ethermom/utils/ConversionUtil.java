package com.mno.ethermom.utils;

public class ConversionUtil {

	public static double convertToMHs(double input) {
		return Math.round(input / 10000) / 100;
	}

	public static double convertFromMHs(double input) {
		return input * 1000000;
	}

}
