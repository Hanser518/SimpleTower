package frame.component.interaction;

import entity.Direction;
import frame.component.status.StanderStatusComponent;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static common.FrameConstant.UNIT_SIZE;

public abstract class StanderInteractionComponent extends JPanel {
    /**
     * 地图数据
     */
    protected static int[][] mapMatrix = null;

    /**
     * 数据标识id
     */
    protected String componentId;

    /**
     * 组件容器
     */
    protected JLayeredPane container;

    /**
     * 组件坐标
     */
    protected Direction compLocation;

    /**
     * 组件交互对象
     */
    protected final List<StanderInteractionComponent> interactionObjects = new ArrayList<>();

    /**
     * 状态列表
     */
    protected final List<StanderStatusComponent> statusList = new ArrayList<>();

    protected final List<Class<? extends StanderStatusComponent>> statusClassList = new ArrayList<>();

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
     * working?
     */
    protected boolean working = true;

    /**
     * 初始化方法<br>
     * 默认会在组件周围添加边框
     */
    public StanderInteractionComponent() {
        super(null);
        setBounds(0, 0, UNIT_SIZE, UNIT_SIZE);
        setBackground(Color.white);
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                width -= 1;
                height -= 1;
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(175, 175, 175));
                g2d.fillRoundRect(0, 0, width, height, width / 2, height / 2);

                g2d.setColor(new Color(111, 67, 67));
                int enduranceWidth = (int) ((width - 1) * ((double) endurance / enduranceLimit));
                g2d.fillRect(1, 1, enduranceWidth, 5);

                g2d.setColor(new Color(57, 29, 37));
                g2d.drawOval(width / 8, height / 8, width - width / 8 * 2, height - height / 8 * 2);
                g2d.drawOval(width / 8 * 2, height / 8 * 2, width - width / 8 * 4, height - height / 8 * 4);
                g2d.drawString(String.valueOf(interactionObjects.size()), width / 2 - 3, height / 2 + 5);
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
    }

    public static void setMatrix(int[][] sceneMatrix) {
        mapMatrix = sceneMatrix;
    }

    /**
     * 注册到管线中
     *
     * @param container
     */
    public abstract void register(JLayeredPane container);

    /**
     * 暴露组件容器
     *
     * @return
     */
    public JLayeredPane getContainer() {
        return container;
    }

    public Direction getCompLocation() {
        return compLocation;
    }

    /**
     * 触发交互事件
     */
    public void interaction() {
        // 标准状态处理
        for (StanderStatusComponent status : statusList) {
            status.invoke();
        }
        // 交互对象处理
        if (isSearchingForInteraction()) {
            searchInteraction();
        }
        // 触发交互事件
        triggerInteractionEvent();
    }

    public void addToStatus(StanderStatusComponent SSC){
        if (!statusClassList.contains(SSC.getClass())){
            statusList.add(SSC);
            statusClassList.add(SSC.getClass());
        }
    }

    protected int calcDistance(Point p1, Point p2) {
        return (int) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public boolean isAliveState() {
        return aliveState;
    }

    public int getRestEndurance(int value){
        endurance -= value;
        return endurance;
    }

    public void setWorking(){
        working = false;
    }

    public void resetWorking() {
        working = true;
    }

    /**
     * 搜索事件阈值
     *
     * @return
     */
    protected abstract boolean isSearchingForInteraction();

    /**
     * 搜索事件
     */
    protected abstract void searchInteraction();

    /**
     * 交互事件
     */
    protected abstract void triggerInteractionEvent();

}
