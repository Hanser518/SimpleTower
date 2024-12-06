package method.test;

import entity.Direction;
import method.map.BuildMap;
import method.map.TransMap;
import method.way.BuildSolution;

import java.awt.*;
import java.util.List;

public class MapTest {
    public static void main(String[] args) {
        int[][] mapData = BuildMap.getMap(20, 20);
        int[][] matrix = TransMap.getTransMatrix(mapData);
        List<Direction> result = BuildSolution.getMapWay(matrix, new Point(1, 1), new Point(matrix.length - 2, matrix[0].length - 2));
        for (int[] subMap : mapData) {
            for (int j = 0; j < mapData[0].length; j++) {
                int a = (subMap[j] >> 5) & 1;
                int b = (subMap[j] >> 4) & 1;
                int c = (subMap[j] >> 3) & 1;
                int d = (subMap[j] >> 2) & 1;
                System.out.printf("%d%d%d%d ", a, b, c, d);
            }
            System.out.println();
        }
        for (int[] subMatrix : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                int a = subMatrix[j];
                System.out.printf("%d ", a);
            }
            System.out.println();
        }
        for (Direction p : result){
            System.out.println(p.toString());
        }
    }
}
