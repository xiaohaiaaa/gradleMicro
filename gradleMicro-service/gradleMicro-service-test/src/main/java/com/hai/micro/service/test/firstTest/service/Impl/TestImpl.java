package com.hai.micro.service.test.firstTest.service.Impl;

import java.util.regex.Pattern;

/**
 * 记一次笔试
 */
public class TestImpl {

    public int compare(int[] A, int[] B) {
        int length = B.length;
        int count = 0;
        for (int i=1; i<= length; i++) {
            for (int j=1; j<= A.length; j++) {
                if (A[j] == B[i]) {
                    count++;
                    break;
                }
            }
        }
        if (count >= length) {
            return 1;
        } else {
            return -1;
        }
    }

    public String add(String a, String b) {
        String pattern = "[0-9]";
        boolean ismatchOne = Pattern.matches(pattern, a);
        boolean ismatchTwo = Pattern.matches(pattern, b);
        if (ismatchOne && ismatchTwo) {
            int response =  Integer.parseInt(a) + Integer.parseInt(b);
            return String.valueOf(response);
        } else {
            return "ERROR";
        }
    }

}
