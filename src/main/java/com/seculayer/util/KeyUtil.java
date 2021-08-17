package com.seculayer.util;

import org.apache.commons.codec.digest.DigestUtils;

public class KeyUtil {
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Invalid Parameter!");
			System.out.println("ex> KeyUtil passwd");
			return;
		}
		System.out.println(DigestUtils.shaHex(args[0]));
	}
}
