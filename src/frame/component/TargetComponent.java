package frame.component;

import entity.Direction;
import method.way.BuildSolution;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static common.FrameConstant.FRAME_REFRESH_INTERVAL;
import static common.FrameConstant.UNIT_MOVE_COUNT;

public class TargetComponent extends JPanel {
    /**
     * 设置全局刷新率
     */
    private static final int UPDATE_DELAY = 1000 / FRAME_REFRESH_INTERVAL;

    /**
     * 设置全局定时器
     */
    private static Timer globalTimer;

    /**
     * 设置组件列表
     */
    public static final ArrayList<TargetComponent> components = new ArrayList<>();

    /**
     * 待移除组件列表
     */
    private static final ArrayList<TargetComponent> removeComponents = new ArrayList<>();

    /**
     * 地图数据
     */
    private static int[][] mapMatrix = null;

    /**
     * 移动路径
     */
    private List<Direction> motionPath = new ArrayList<>();

    /**
     * 点位标识
     */
    private Direction targetLocation;

    public Direction getTargetLocation() {
        return targetLocation;
    }

    /**
     * 帧位移量
     */
    private int frameMotionValue = 0;

    /**
     *
     */
    private int motionValue = 0;

    /**
     *
     */
    private static JPanel container = null;

    /**
     * focus
     */
    private boolean focus = false;

    /**
     * state
     */
    private boolean aliveState = true;

    /**
     *
     */
    public TargetComponent() {
        super(null);
    }

    /**
     *
     */
    public void register(JPanel panel, Point start, Point end) {
        container = panel;
        // 计算路径、初始化点位
        motionPath = BuildSolution.getMapWay(mapMatrix, start, end);
        targetLocation = motionPath.get(0);
        // 获取帧位移量
        frameMotionValue = FRAME_REFRESH_INTERVAL / UNIT_MOVE_COUNT;
        components.add(this);
    }

    public static void initializeGlobalTimer(int[][] matrix) {
        mapMatrix = matrix;
        if (globalTimer == null) {
            globalTimer = new Timer(UPDATE_DELAY, e -> {
                // 统一更新所有组件的位置
                for (TargetComponent component : components) {
                    component.updatePosition();
                }
                for (TargetComponent component : removeComponents) {
                    components.remove(component);
                    container.remove(component);
                }
                removeComponents.clear();
            });
            globalTimer.start();
        }
    }

    private void updatePosition() {
        // 获取当前坐标
        Point location = getLocation();
        // 获取移动方向
        int dir = targetLocation.direction;
        int moveDist;
        switch (dir) {
            case 0, 2 -> {
                moveDist = (dir == 0 ? -1 : 1) * this.getHeight() / UNIT_MOVE_COUNT;
                this.setLocation(location.x, location.y + moveDist);
            }
            case 1, 3 -> {
                moveDist = (dir == 3 ? -1 : 1) * this.getWidth() / UNIT_MOVE_COUNT;
                this.setLocation(location.x + moveDist, location.y);
            }

        }
        // 计算移动步骤
        motionValue += frameMotionValue;
        if (motionValue == FRAME_REFRESH_INTERVAL) {
            motionValue = 0;
            int index = motionPath.indexOf(this.targetLocation);
            if (index == motionPath.size() - 1) {
                removeComponents.add(this);
                aliveState = false;
            } else {
                this.targetLocation = motionPath.get(index + 1);
            }
        }
    }

    public void setFocus() {
        focus = true;
    }

    public boolean isFocus() {
        return focus;
    }

    public boolean isAliveState() {
        return aliveState;
    }
}
