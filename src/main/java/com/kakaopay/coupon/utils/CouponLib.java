package com.kakaopay.coupon.utils;

import java.util.Arrays;
import java.util.Random;

public class CouponLib {

	static final int GENRATE_COUPON_LENGTH = 8;
	static final int ID_LENGTH = 6;
	static final int HASH_CODE_LENGTH = 2;

	public static String fillZero(String str, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append(0);
		}
		return sb.append(str).toString();
	}

	public static int[] generateSeed(int length) {
		int[] seed = new int[length];
		Random random = new Random();

		for (int i = 0; i < length; i++) {
			seed[i] = random.nextInt(Base62.BASE);
		}
		return seed;
	}

	public static String generateCode(int seq) {
		int[] seed = generateSeed(GENRATE_COUPON_LENGTH);

		int sum = Arrays.stream(seed).sum();

		String hashCode = Base62.fromBase10(sum);
		if (hashCode.length() < HASH_CODE_LENGTH) {
			hashCode = CouponLib.fillZero(hashCode, HASH_CODE_LENGTH - hashCode.length());
		}

		String id = Base62.fromBase10(seq);
		String fillZeroId = fillZero(id, ID_LENGTH - id.length());
		String rotatedId = rightCyclicRotation(fillZeroId, sum);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < GENRATE_COUPON_LENGTH; i++) {
			sb.append(Base62.ALPHABET.charAt(seed[i]));
			if (i < rotatedId.length()) {
				sb.append(rotatedId.charAt(i));
			}
		}

		sb.append(hashCode);
		return sb.toString();
	}

	public static String rightCyclicRotation(String str, int count) {
		int length = str.length();

		if (count < 1 || length < 1) {
			return str;
		}

		count = count % length;
		char[] rotationStr = str.toCharArray();
		char buffer;

		for (int i = 0; i < count; i++) {
			for (int j = 0; j < length; j++) {
				buffer = rotationStr[0];
				rotationStr[0] = rotationStr[j];
				rotationStr[j] = buffer;
			}
		}
		return new StringBuffer().append(rotationStr).toString();
	}
}