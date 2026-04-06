package kaptainwutax.tungsten.helpers;

public class StringProcessorHelper {
	public static int findClosestCharIndex(String str, char target, int index) {
        if (str == null || str.length() == 0) {
            return -1;
        }

        int leftIndex = -1;
        int rightIndex = -1;

        for (int i = index; i >= 0; i--) {
            if (str.charAt(i) == target) {
                leftIndex = i;
                break;
            }
        }

        for (int i = index; i < str.length(); i++) {
            if (str.charAt(i) == target) {
                rightIndex = i;
                break;
            }
        }

        if (leftIndex == -1 && rightIndex == -1) {
            return -1; 
        } else if (leftIndex == -1) {
            return rightIndex;
        } else if (rightIndex == -1) {
            return leftIndex;
        } else {
            return (Math.abs(index - leftIndex) <= Math.abs(index - rightIndex)) ? leftIndex : rightIndex;
        }
	}
}
