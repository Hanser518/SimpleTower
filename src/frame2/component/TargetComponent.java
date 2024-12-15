package frame2.component;

import entity.Direction;
import frame.pipeLine.GlobalInteractionLine;
import frame2.pipeLine.TargetLine;
import frame2.pipeLine.TowerLine;
import method.way.BuildSolution;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static frame2.common.ComponentConstant.*;
import static frame2.common.FrameConstant.COMPONENT_LAYER;

public abstract class TargetComponent extends JPanel {
    /**
     * 地图数据
     */
    protected static int[][] mapMatrix = null;

    /**
     * path
     */
    protected List<Direction> path;

    /**
     * 组件方向
     */
    protected int compIndex = 0;

    protected Direction compDirection;

    /**
     * 每单位移动量
     */
    protected int[] landscapeArr;

    /**
     * 每单位移动量
     */
    protected int[] portraitArr;

    /**
     * 移动次数
     */
    protected int motionCount = 0;

    /**
     * 移动状态
     */
    protected boolean motionState = true;

    /**
     * 耐久上限
     */
    protected Integer enduranceLimit = 50;

    /**
     * 耐力
     */
    protected Integer endurance = 50;

    /**
     * atk value
     */
    protected Integer atkValue = 5;

    /**
     * atk load
     */
    protected Integer atkLoad = 0;

    /**
     * atk interval
     */
    protected Integer atkInterval = 100;

    /**
     * atk range
     */
    protected Integer atkRange = 5;

    /**
     * state
     */
    protected boolean aliveState = true;

    /**
     * 组件容器
     */
    protected JLayeredPane container;

    /**
     * 组件交互对象
     */
    protected List<TowerComponent> interactorList = new ArrayList<>();

    /**
     * 初始化
     */
    public TargetComponent() {
        super(null);
        setBackground(Color.WHITE);
        initMoveValues();
    }

    /**
     * 初始化移动变量
     */
    protected void initMoveValues() {
        landscapeArr = new int[MOVE_COUNT];
        int xValue = UNIT_WIDTH / MOVE_COUNT;
        Arrays.fill(landscapeArr, xValue);
        int interval = UNIT_WIDTH - xValue * MOVE_COUNT;
        if (interval > 0) {
            int split = MOVE_COUNT / interval;
            for (int i = 1; i < MOVE_COUNT; i += split) {
                landscapeArr[i] += 1;
            }
        }

        portraitArr = new int[MOVE_COUNT];
        int yValue = UNIT_HEIGHT / MOVE_COUNT;
        Arrays.fill(portraitArr, yValue);
        interval = UNIT_HEIGHT - yValue * MOVE_COUNT;
        if (interval > 0) {
            int split = MOVE_COUNT / interval;
            for (int i = 1; i < MOVE_COUNT; i += split) {
                portraitArr[i] += 1;
            }
        }
    }

    /**
     * 注册组件
     *
     * @param container
     * @param path
     */
    public void register(JLayeredPane container, List<Direction> path) {
        this.container = container;
        this.path = path;
        compDirection = path.get(compIndex);
        setLocation(compDirection.x * UNIT_WIDTH, (compDirection.y + 1) * UNIT_HEIGHT - this.getHeight());
        container.add(this, COMPONENT_LAYER);
        TargetLine.addToPrepareComponents(this);
    }

    /**
     * 标准事件
     */
    public void incident() {
        if (motionState) {
            motionEvent();
        }
    }

    protected void motionEvent() {
        // 获取当前坐标
        Point location = getLocation();
        // 获取移动方向
        int dir = path.get(compIndex).direction;
        switch (dir) {
            case 0, 2 -> {
                int dist = (dir == 0 ? -1 : 1) * portraitArr[motionCount];
                this.setLocation(location.x, location.y + dist);
            }
            case 1, 3 -> {
                int dist = (dir == 3 ? -1 : 1) * landscapeArr[motionCount];
                this.setLocation(location.x + dist, location.y);
            }

        }
        // 计算移动步骤
        motionCount += 1;
        if (motionCount == MOVE_COUNT) {
            motionCount = 0;
            // 若路线未发生变化
            if (compIndex <= path.size() - 1 && compDirection.equal(path.get(compIndex))) {
                if (compIndex == path.size() - 1) {
                    TargetLine.addToRemoveComponents(this);
                    aliveState = false;
                    return;
                } else {
                    compIndex++;
                }
                compDirection = path.get(compIndex);
            } else {
                updatePath();
            }
        }
    }

    protected void updatePath() {
        path = BuildSolution.getMapWay(mapMatrix, compDirection, path.get(path.size() - 1));
        compIndex = 0;
    }

    /**
     * 获取组件容器
     */
    public JLayeredPane getContainer() {
        return container;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setContent(g, getWidth(), getHeight());
    }

    protected abstract void setContent(Graphics g, int width, int height);

}
