/*
 * Copyright 2017 ltu.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://ltu.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.ltu.fm.dao;

public class Test {
	
	public static boolean checkP(String inputString) {
		int j=inputString.length()-1;
	    for (int i=0;i<inputString.length()/2;i++) {
	        if (inputString.charAt(i) != inputString.charAt(j--)){
	            return false;
	        }
	    }
	    return true;
	}
	
	public static int makeArrayConsecutive2(int[] statues) {

	    for (int i=0; i<statues.length; i++) {
	        for(int j=i+1;j<statues.length; j++) {
	            if (statues[i] > statues[j]) {
	                int swap = statues[i];
	                statues[i] = statues[j];
	                statues[j] = swap;
	            }
	        }
	    }
	    int count = 0;
	    for (int i=statues[0]+1; i<statues[statues.length-1]; i++) {
	    	System.out.println(i);
	        boolean flag = true;
	        for (int j=1; j<statues.length-1; j++) {
	        	//System.out.println(statues[j]);
	            if (i == statues[j]) {
	            	System.out.println(statues[j]);
	                flag = false;
	                break;
	            }
	        }
	        if (flag) {
	            count++;
	        }
	    }
	    return count;
	}
	
	public static boolean almostIncreasingSequence(int[] sequence) {

	    for(int i=0; i<sequence.length-1; i++) {
	        if (sequence[i] >= sequence[i+1]) {

	            int j = 0;
	            do {
	                if (i == j && i == 0 ) {
	                    j++;
	                }
	                if (j + 2 < sequence.length && i == j && i != 0 && i != sequence.length-1 ) {
	                    if (sequence[j-1] >= sequence[j+1]) {
	                        return false;
	                    }
	                } else {
	                    if (j + 1 < sequence.length && i != j && (i != j+1) && sequence[j] >= sequence[j+1]) {
	                        return false;
	                    }
	                }
	                j++;
	            } while (j<sequence.length-1);
	            
	        }
	    }
	    return true;
	}


	
	public static void main(String[] args) {
		//System.out.println(checkP("aabaa"));
		
		int[] a = {1, 2, 3, 4, 3, 6};
		System.out.println(almostIncreasingSequence(a));
	}
	

}
