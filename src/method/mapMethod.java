package method;

import java.lang.reflect.Array;
import java.util.Arrays;

public class mapMethod {

    public static int[][] buildMap(int width, int height) {
        int[][] result = new int[width][height];
        for (int[] arr : result) {
            Arrays.fill(arr, 1);
        }
        return result;
    }
}
