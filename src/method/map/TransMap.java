package method.map;

import java.util.Arrays;

public class TransMap {

    /**
     * 将地图数据转换为01矩阵
     * @param map
     * @return
     */
    public static int[][] getTransMatrix(int[][] map) {
        int[][] matrix = new int[map.length * 2 + 1][map[0].length * 2 + 1];
        // 顶侧填充
        for (int i = 0; i < matrix.length; i++) {
            matrix[i][0] = 1;
        }
        Arrays.fill(matrix[0], 1);
        // 转换数据，取右下（index: 1、2）
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int right = (map[i][j] >> 4) & 0x1;
                int down = (map[i][j] >> 3) & 0x1;
                matrix[i * 2 + 1][j * 2 + 1] = 0;
                matrix[i * 2 + 2][j * 2 + 1] = 1 - right;
                matrix[i * 2 + 1][j * 2 + 2] = 1 - down;
                matrix[i * 2 + 2][j * 2 + 2] = 1;
                // System.out.printf("%d%d ", right, down);
            }
            // System.out.println();
        }
        return matrix;
    }
}
