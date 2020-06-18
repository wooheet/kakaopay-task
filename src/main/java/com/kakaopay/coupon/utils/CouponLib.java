package com.kakaopay.coupon.utils;

import java.util.Arrays;
import java.util.Random;

public class CouponLib {

	static final int GENRATE_COUPON_LENGTH = 8;
	static final int ID_LENGTH = 6;
	static final int COUPON_CODE_LENGTH = GENRATE_COUPON_LENGTH + ID_LENGTH;
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

	// restoreSeq
	public static String decodeSeq(String idStr) {
		StringBuilder sb = new StringBuilder();
		for (int i = 1; i < COUPON_CODE_LENGTH - 1; i += 2) {
			sb.append(idStr.charAt(i));
		}
		return sb.toString();
	}

	// restoreCode
	public static String decodeCode(String code) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < code.length() - HASH_CODE_LENGTH; i++) {
			if (i % 2 == 0 || COUPON_CODE_LENGTH - 2 < i) {
				sb.append(code.charAt(i));
			}

		}
		return sb.toString();
	}

	public static boolean isInvalidCode(String code) {
		String restoreCode = decodeCode(code);
		String hashStr = code.substring(GENRATE_COUPON_LENGTH + ID_LENGTH, code.length());
		int hashcode = Base62.toBase10(hashStr);

		int sum = 0;
		for (int i = 0; i < restoreCode.length(); i++) {
			sum += Base62.toBase10(restoreCode.charAt(i));
		}
		return hashcode == sum;
	}

	// getSeq
	public static long restoreSeq(String code) {
		String base62HashCode = code.substring(code.length() - HASH_CODE_LENGTH, code.length());
		int hashCode = Base62.toBase10(base62HashCode);
		String rotationSeq = decodeSeq(code);
		String bas62Seq = leftCyclicRotation(rotationSeq, hashCode);

		return Base62.toBase10(bas62Seq);
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


	public static String leftCyclicRotation(String str, int count) {
		int length = str.length();

		if (count < 1 || length < 1) {
			return str;
		}

		count = count % length;
		char[] rotationStr = str.toCharArray();
		char buffer;

		for (int i = 0; i < count; i++) {
			for (int j = length - 1; j >= 0; j--) {
				buffer = rotationStr[length - 1];
				rotationStr[length - 1] = rotationStr[j];
				rotationStr[j] = buffer;
			}
		}
		return new StringBuffer().append(rotationStr).toString();
	}

}