package method.way;

import entity.Direction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BuildSolution {

    /**
     * 计算01矩阵两点之间的最短路径
     * <p>
     * 基本思路和创建迷宫一致，获取当前点位坐标、获取下一步动作，判断是否回退/标记当前点位
     *
     * @param matrix   01矩阵
     * @param location 起始点
     * @param endPoint 终点
     * @return
     */
    public static List<Direction> getMapWay(int[][] matrix, Point location, Point endPoint) {
        // 获取地图数据
        int[][] initMap = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < initMap.length; i++) {
            System.arraycopy(matrix[i], 0, initMap[i], 0, initMap[0].length);
        }
        // 创建路劲缓存并初始化
        Stack<Direction> roadCache = new Stack<>();
        roadCache.add(new Direction(location.x, location.y, -1));
        // 标记当前点位
        initMap[location.x][location.y] += 1;
        while (!roadCache.isEmpty() && (roadCache.peek().x != endPoint.x || roadCache.peek().y != endPoint.y)) {
            Point p = roadCache.peek();
            int nextAction = getNextAction(p, endPoint, initMap);
            if (nextAction == -1) {
                roadCache.pop();
            } else {
                Direction nextPoint = getNextPoint(p, nextAction);
                initMap[nextPoint.x][nextPoint.y] += 1;
                roadCache.add(nextPoint);
            }
        }
        return roadCache;
    }

    /**
     * 计算下一行动方向
     *
     * @param location
     * @param endPoint
     * @param mapData
     * @return
     */
    private static int getNextAction(Point location, Point endPoint, int[][] mapData) {
        List<Integer> dirBack = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Point nextPoint = getNextPoint(location, i);
            // 首先判断是否出界
            if (nextPoint.x >= 0 && nextPoint.y >= 0 && nextPoint.x < mapData.length && nextPoint.y < mapData[0].length) {
                // 判断是否为 未探索 的 路径（value = 0）
                if (mapData[nextPoint.x][nextPoint.y] == 0) {
                    dirBack.add(i);
                }
            }
        }
        // 无可选方向，返回-1
        if (dirBack.isEmpty()) {
            return -1;
        } else if (dirBack.size() == 1) {
            return dirBack.get(0);
        } else {
            return getBetterDirection(location, endPoint, dirBack);
        }
    }

    /**
     * 贪心法获取最短方向
     *
     * @param location
     * @param endPoint
     * @param dirBack
     * @return
     */
    private static int getBetterDirection(Point location, Point endPoint, List<Integer> dirBack) {
        int betterDir = dirBack.get(0);
        int distance = 1145112;
        for (int dir : dirBack) {
            Point dirPoint = getNextPoint(location, dir);
            int dirDist = Math.abs(endPoint.x - dirPoint.x) + Math.abs(endPoint.y - dirPoint.y);
            betterDir = dirDist < distance ? dir : betterDir;
            distance = Math.min(distance, dirDist);
        }
        return betterDir;
    }

    private static int getBetterDirection2(Point location, Point endPoint, List<Integer> dirBack) {
        int betterDir = dirBack.get(0);
        int distance = 1145112;
        for (int dir : dirBack) {
            Point dirPoint = getNextPoint(location, dir);
            int dirDist = Math.abs(endPoint.x - dirPoint.x) + Math.abs(endPoint.y - dirPoint.y);
            betterDir = dirDist < distance ? dir : betterDir;
            distance = Math.min(distance, dirDist);
        }
        return betterDir;
    }

    /**
     * 获取对应方向点位，支持拓展，在getNextPointDefault方法中扩充
     *
     * @param location 当前坐标
     * @param dir      方向
     * @return 方向移动一单位距离对应坐标
     */
    public static Direction getNextPoint(Point location, int dir) {
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
        return new Direction(nx, ny, dir);
    }

    private static Direction getNextPoint8(Point location, int dir) {
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
                return getNextPointDefault(location, dir);
            }
        }
        return new Direction(nx, ny, dir);
    }

    public static Direction getNextPointDefault(Point location, int dir){
        return null;
    }
}
