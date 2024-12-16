package frame2.component;

import entity.Direction;
import frame2.pipeLine.TowerLine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;
import static frame2.common.FrameConstant.COMPONENT_LAYER;

public abstract class TowerComponent extends JPanel {

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
    protected boolean alive = true;

    /**
     * 组件容器
     */
    protected JLayeredPane container;

    /**
     * 组件交互对象
     */
    protected List<TargetComponent> interactorList = new ArrayList<>();

    /**
     * component location and direction
     */
    protected Direction compLocation;

    /**
     * select range list
     */
    protected List<Direction> selectRange = new ArrayList<>();

    /**
     * select type
     */
    protected int selectType = 0;

    /**
     * init
     */
    public TowerComponent() {
        super(null);
        setBackground(new Color(190, 190, 190));
        setBounds(0, 0, UNIT_WIDTH, UNIT_WIDTH);
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(new Color(71, 137, 168, 190));
                int enduranceWidth = (int) ((width - 1) * 0.8 * (endurance / (double) enduranceLimit));
                int enduranceHeight = height / 12 + 1;
                g.fillRect((int) (width * 0.1), 0, enduranceWidth, enduranceHeight);
                g.setColor(new Color(21, 55, 58, 190));
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
        addRightClickMenu(this);
    }

    /**
     * register component
     *
     * @param container    组件容器
     * @param compLocation 组件方位
     */
    public void register(JLayeredPane container, Direction compLocation) {
        this.container = container;
        this.compLocation = compLocation;
        setLocation(compLocation.x * UNIT_WIDTH, compLocation.y * UNIT_HEIGHT);
        updateSelectRangeList();
        container.add(this, COMPONENT_LAYER);
        TowerLine.addToPrepareComponents(this);
    }

    /**
     * 组件事件
     */
    public void incident() {
        // 状态判断
        if (!alive) {
            container.remove(this);
            TowerLine.addToRemoveComponents(this);
        } else {
            // 交互对象处理
            if (isSearchingForInteraction()) {
                searchInteraction();
            }
            // 触发交互事件
            if (!interactorList.isEmpty()) {
                triggerInteractionEvent();
            }
        }
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

    /**
     * get container
     *
     * @return container
     */
    public JLayeredPane getContainer() {
        return container;
    }

    /**
     * 重置组件外形
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        setContent(graphics, getWidth() - 1, getHeight() - 1);
    }

    /**
     * 设置组件内容
     *
     * @param g2d    绘柄
     * @param width  宽
     * @param height 高
     */
    protected abstract void setContent(Graphics2D g2d, int width, int height);

    /**
     * 添加右键菜单
     *
     * @param comp 组件
     */
    private static void addRightClickMenu(TowerComponent comp) {
        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem skillItem = new JMenuItem("SKILL");

        JMenuItem deleteItem = new JMenuItem("REMOVE");
        deleteItem.addActionListener(e -> {
            // 从面板中移除 JLabel
            TowerLine.addToRemoveComponents(comp);
            comp.container.remove(comp);
        });

        // 将菜单项添加到右键菜单
        popupMenu.add(skillItem);
        popupMenu.add(deleteItem);

        // 为 JLabel 添加右键点击事件
        comp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(comp, comp.getWidth(), 0); // 显示右键菜单
                }
            }
        });
    }

    private void updateSelectRangeList() {
        switch (selectType) {
            case 0:
                for (int index = 0; index < 5; index++) {
                    switch (compLocation.direction) {
                        case 0:
                            selectRange.add(new Direction(new Point(compLocation.x + 1, compLocation.y - index)));
                            selectRange.add(new Direction(new Point(compLocation.x - 1, compLocation.y - index)));
                            selectRange.add(new Direction(new Point(compLocation.x, compLocation.y - index)));
                            break;
                        case 1:
                            selectRange.add(new Direction(new Point(compLocation.x + index, compLocation.y + 1)));
                            selectRange.add(new Direction(new Point(compLocation.x + index, compLocation.y - 1)));
                            selectRange.add(new Direction(new Point(compLocation.x + index, compLocation.y)));
                            break;
                        case 2:
                            selectRange.add(new Direction(new Point(compLocation.x + 1, compLocation.y + index)));
                            selectRange.add(new Direction(new Point(compLocation.x - 1, compLocation.y + index)));
                            selectRange.add(new Direction(new Point(compLocation.x, compLocation.y + index)));
                            break;
                        case 3:
                            selectRange.add(new Direction(new Point(compLocation.x - index, compLocation.y + 1)));
                            selectRange.add(new Direction(new Point(compLocation.x - index, compLocation.y - 1)));
                            selectRange.add(new Direction(new Point(compLocation.x - index, compLocation.y)));
                            break;
                    }
                }
                break;
            default:
                selectRange.add(compLocation);
        }
    }

}
