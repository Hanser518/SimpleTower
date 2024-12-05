package method.way;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CalcWay {

    /**
     * 计算01矩阵两点之间的最短路径
     *
     * @param matrix   01矩阵
     * @param location 起始点
     * @param endPoint 终点
     * @return
     */
    public static List<Point> getMapWay(int[][] matrix, Point location, Point endPoint) {
        List<Point> roadCache = new ArrayList<>();
        roadCache.add(location);
        while (location.x != endPoint.x && location.y != endPoint.y) {

        }
        return roadCache;
    }

    public static Point getNextPoint(Point location, int dir) {
        int nx = location.x;
        int ny = location.y;
        switch (dir) {
            case 0 -> ny -= 1;
            case 1 -> nx += 1;
            case 2 -> ny += 1;
            case 3 -> nx -= 1;
            default -> {
                return getNextPoint8(location, dir);
            }
        }
        return new Point(nx, ny);
    }

    private static Point getNextPoint8(Point location, int dir) {
        int nx = location.x;
        int ny = location.y;
        switch (dir) {
            case 4 -> {
                nx -= 1;
                ny -= 1;
            }
            case 5 -> {
                nx += 1;
                ny -= 1;
            }
            case 6 -> {
                nx += 1;
                ny += 1;
            }
            case 7 -> {
                nx -= 1;
                ny += 1;
            }
            default -> {
                return null;
            }
        }
        return new Point(nx, ny);
    }
}
