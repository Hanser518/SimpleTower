package method.map;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class BuildMap {

    /**
     * 根据输入的数值生成地图
     *
     * @param width  宽
     * @param height 高
     * @return 地图数据
     */
    public static int[][] getMap(int width, int height) {
        int[][] mapData = new int[width][height];
        for (int[] arr : mapData) {
            Arrays.fill(arr, 4);
        }
        boolean[][] visitArr = new boolean[width][height];
        // 创建点缓存、初始化数据
        Stack<Point> pointStack = new Stack<>();
        pointStack.add(new Point(0, 0));
        visitArr[0][0] = true;
        // 遍历数组，创建地图
        while (!pointStack.isEmpty()) {
            Point p = pointStack.peek();
            int nextDir = getNextDirection(p, visitArr);
            if (nextDir == -1) {
                pointStack.pop();
            } else {
                buildPath(mapData, p, nextDir);
                p = getNextPoint(p, nextDir);
                pointStack.add(p);
                visitArr[p.x][p.y] = true;
            }
        }
        return mapData;
    }

    /**
     * 计算当前点位的可行动方向
     *
     * @param location 位置
     * @param visitArr 访问轨迹
     * @return
     */
    public static int getNextDirection(Point location, boolean[][] visitArr) {
        List<Integer> dirBackup = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Point p = getNextPoint(location, i);
            // System.out.println(p);
            if (p != null && p.x >= 0 && p.y >= 0 && p.x < visitArr.length && p.y < visitArr[0].length) {
                if (!visitArr[p.x][p.y]) {
                    dirBackup.add(i);
                }
            }
        }
        if (dirBackup.isEmpty()) {
            return -1;
        } else {
            return dirBackup.get((int) (Math.random() * dirBackup.size()));
        }
    }

    /**
     * 获取下一位置坐标
     *
     * @param location
     * @param dir
     * @return
     */
    public static Point getNextPoint(Point location, int dir) {
        int nx = location.x;
        int ny = location.y;
        switch (dir) {
            case 0 -> ny -= 1;
            case 1 -> nx += 1;
            case 2 -> ny += 1;
            case 3 -> nx -= 1;
            default -> {
                return null;
            }
        }
        return new Point(nx, ny);
    }

    /**
     * 在当前位点延dir方向创建通道
     *
     * @param map
     * @param location
     * @param dir
     */
    public static void buildPath(int[][] map, Point location, int dir) {
        int totalDirections = map[0][0] & 0x4;
        int antDir = (dir + totalDirections / 2) % totalDirections;
        // System.out.println(totalDirections + " " + dir + " " + antDir + " " + (totalDirections + 1 - dir) + " " + (totalDirections + 1 - antDir));
        Point nextPoint = getNextPoint(location, dir);
        map[location.x][location.y] = map[location.x][location.y] | (1 << (totalDirections + 1 - dir));
        map[nextPoint.x][nextPoint.y] = map[nextPoint.x][nextPoint.y] | (1 << (totalDirections + 1 - antDir));
    }
}
