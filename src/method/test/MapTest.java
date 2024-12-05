package method.test;

import method.map.BuildMap;
import method.map.TransMap;

public class MapTest {
    public static void main(String[] args) {
        int[][] mapData = BuildMap.getMap(20, 20);
        int[][] matrix = TransMap.getTransMatrix(mapData);
        for(int i = 0;i < mapData.length; i++){
            for(int j = 0;j < mapData[0].length;j ++){
                int a = (mapData[i][j] >> 5) & 1;
                int b = (mapData[i][j] >> 4) & 1;
                int c = (mapData[i][j] >> 3) & 1;
                int d = (mapData[i][j] >> 2) & 1;
                System.out.printf("%d%d%d%d ", a, b, c, d);
            }
            System.out.println();
        }
        for(int i = 0;i < matrix.length; i++){
            for(int j = 0;j < matrix[0].length;j ++){
                int a = matrix[i][j];
                System.out.printf("%d ", a);
            }
            System.out.println();
        }
    }
}
