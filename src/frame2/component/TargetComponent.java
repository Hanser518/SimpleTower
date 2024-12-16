package frame2.component;

import entity.Direction;
import frame2.component.effect.ResistEffect;
import frame2.pipeLine.TargetLine;
import frame2.pipeLine.TowerLine;
import method.way.BuildSolution;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    protected Integer resistCount = 1;

    /**
     * state
     */
    protected boolean alive = true;

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
    @SuppressWarnings("SuspiciousNameCombination")
    public TargetComponent() {
        super(null);
        setBackground(Color.WHITE);
        setBounds(0, 0, UNIT_WIDTH, UNIT_WIDTH);
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(new Color(245, 79, 88, 190));
                int enduranceWidth = (int) ((width - 1) * 0.8 * (endurance / (double) enduranceLimit));
                int enduranceHeight = height / 12 + 1;
                g.fillRect((int) (width * 0.1), 0, enduranceWidth, enduranceHeight);
                g.setColor(new Color(77, 29, 31, 190));
                g.drawRect((int) (width * 0.1), 0, enduranceWidth, enduranceHeight);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return null;
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });
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
            double split = MOVE_COUNT / (double) interval;
            for (int i = 0; i < interval; i++) {
                landscapeArr[(int) (i * split)] += 1;
            }
        }

        portraitArr = new int[MOVE_COUNT];
        int yValue = UNIT_HEIGHT / MOVE_COUNT;
        Arrays.fill(portraitArr, yValue);
        interval = UNIT_HEIGHT - yValue * MOVE_COUNT;
        if (interval > 0) {
            double split = MOVE_COUNT / (double) interval;
            for (int i = 0; i < interval; i++) {
                portraitArr[(int) (i * split)] += 1;
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
        compIndex = 0;
        compDirection = path.get(compIndex);
        setLocation(compDirection.x * UNIT_WIDTH, (compDirection.y + 1) * UNIT_HEIGHT - this.getHeight());
        container.add(this, COMPONENT_LAYER);
        TargetLine.addToPrepareComponents(this);
    }

    /**
     * 标准事件
     */
    public void incident() {
        if (endurance <= 0) {
            TargetLine.addToRemoveComponents(this);
            alive = false;
        }
        if (motionState) {
            motionEvent();
        }
    }

    protected void motionEvent() {
        // 检查阻挡情况
        checkResist();
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
            if (compIndex <= path.size() - 1 && compDirection.equals(path.get(compIndex))) {
                if (compIndex == path.size() - 1) {
                    TargetLine.addToRemoveComponents(this);
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

    protected void checkResist() {
        Map<Direction, TowerComponent> towerMap = TowerLine.getTowerComponentMap();
        if (towerMap.containsKey(compDirection)) {
            TowerComponent tc = towerMap.get(compDirection);
            ResistEffect RE = new ResistEffect(this, tc);
            RE.register(getContainer());
        } else {
            motionState = true;
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

    /**
     *
     */
    public Direction getCompDirection() {
        return compDirection;
    }

    public int getResidueEndurance(int atk) {
        endurance -= atk;
        return endurance;
    }

    /**
     * 设置组件外形
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setContent(g, getWidth(), getHeight());
    }

    protected abstract void setContent(Graphics g, int width, int height);

    public boolean isAlive() {
        return alive;
    }

    public Integer getResistCount(){
        return resistCount;
    }

    public void updateResistCount(int value){
        resistCount = value;
    }

    public void setMotionState(boolean motionState){
        this.motionState = motionState;
    }
}
