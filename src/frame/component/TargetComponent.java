package frame.component;

import entity.Direction;
import frame.pipeLine.GlobalMotionLine;
import method.way.BuildSolution;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static common.FrameConstant.*;
import static frame.pipeLine.GlobalMotionLine.removeComponents;

public class TargetComponent extends JPanel {

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

    /**
     * 每单位移动量
     */
    private int frameMotionValue = 0;

    /**
     * 移动次数
     */
    private int motionCount = 0;

    /**
     * 承载容器
     */
    private static JPanel container = null;

    /**
     * who focus
     */
    private TowerComponent fouceComponent = null;

    /**
     * focus
     */
    private boolean focus = false;

    /**
     * state
     */
    private boolean aliveState = true;

    /**
     * blood
     */
    private int blood = 10;

    /**
     *
     */
    public TargetComponent() {
        super(null);
    }

    /**
     * 获取容器
     *
     * @return
     */
    public static JPanel getContainer() {
        return container;
    }


    public static void setMatrix(int[][] sceneMatrix) {
        mapMatrix = sceneMatrix;
    }

    /**
     * 注册组件并添加到全局动作管线
     */
    public void register(JPanel panel, Point start, Point end) {
        container = panel;
        // 计算路径、初始化点位
        motionPath = BuildSolution.getMapWay(mapMatrix, start, end);
        targetLocation = motionPath.get(0);
        // 获取帧位移量
        frameMotionValue = UNIT_SIZE / UNIT_MOVE_COUNT;
        GlobalMotionLine.addToPrepareComponents(this);
    }

    public void motion() {
        // 获取当前坐标
        Point location = getLocation();
        // 获取移动方向
        int dir = targetLocation.direction;
        switch (dir) {
            case 0, 2 -> {
                int dist = (dir == 0 ? -1 : 1) * frameMotionValue;
                this.setLocation(location.x, location.y + dist);
            }
            case 1, 3 -> {
                int dist = (dir == 3 ? -1 : 1) * frameMotionValue;
                this.setLocation(location.x + dist, location.y);
            }

        }
        if (focus) {
            int damageValue = fouceComponent.getAtkValue();
            blood -= damageValue;
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>" + blood);
            if (blood < 0) {
                removeComponents.add(this);
                aliveState = false;
            }
        }
        // 计算移动步骤
        motionCount += 1;
        if (motionCount == UNIT_MOVE_COUNT) {
            motionCount = 0;
            int index = motionPath.indexOf(this.targetLocation);
            if (index == motionPath.size() - 1) {
                removeComponents.add(this);
                aliveState = false;
            } else {
                this.targetLocation = motionPath.get(index + 1);
            }
        }
    }

    /**
     * 获取矩阵坐标
     *
     * @return
     */
    public Direction getTargetLocation() {
        return targetLocation;
    }

    public void setFocus(TowerComponent component) {
        this.fouceComponent = component;
        focus = true;
    }

    public boolean isFocus() {
        return focus;
    }

    public boolean isAliveState() {
        return aliveState;
    }
}
