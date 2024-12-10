package frame.component.impl;

import common.Element;
import entity.Direction;
import frame.component.StanderComponent;
import frame.pipeLine.GlobalMotionLine;
import frame.pipeLine.GlobalParticleLine;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static common.FrameConstant.MAIN_FONT;
import static common.FrameConstant.UNIT_SIZE;
import static frame.pipeLine.GlobalMotionLine.components;

public class LightningTowerComponent extends JPanel implements StanderComponent {

    /**
     * 类型标识
     */
    public static final Integer TOWER_TYPE_TYPE1 = 1;

    /**
     * 点位标识
     */
    private Direction towerLocation;


    public Direction getTowerLocation() {
        return towerLocation;
    }

    /**
     * 类型
     */
    private Integer type = null;

    /**
     * 容器
     */
    private static JLayeredPane container = null;

    /**
     * 目标
     */
    private TargetComponent target = null;

    /**
     * atk value
     */
    private Integer atkValue;

    /**
     * atk load
     */
    private int atkLoad = 0;

    /**
     * atk interval
     */
    private Integer atkInterval;

    /**
     * atk Range
     */
    private Integer atkRange;

    public LightningTowerComponent(Integer type) {
        super(null);
        setBackground(new Color(70, 58, 140, 0));
        atkRange = 5;
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                int wSize = UNIT_SIZE / 8;
                int hSize = UNIT_SIZE / 8;
                g.setColor(new Color(70, 58, 140, 127));
                g.fillRect(wSize, hSize * 3, wSize, hSize);
                g.fillRect(wSize * 2, hSize * 5, wSize, hSize);
                g.fillRect(wSize * 4, hSize, wSize, hSize);
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
        addOperationMenu(this);
    }

    /**
     * 注册组件
     */
    public void register(JLayeredPane panel, Point location, int dir) {
        container = panel;
        this.towerLocation = new Direction(location, dir);
        GlobalParticleLine.registerBrokenParticle(container, this, this.getWidth());
        GlobalMotionLine.addToPrepareComponents(this);
    }

    /**
     * 获取目标组件
     *
     * @return
     */
    public TargetComponent getTargetComponent() {
        return target;
    }

    /**
     * 事务（暂定，后面可能会根据type执行不同的事务）
     */
    @Override
    public void motion() {
        if (components.isEmpty()) {
            return;
        }
        if (target == null) {
            TargetComponent prepare = null;
            int dist = 115411;
            for (JPanel rawComponent : components) {
                if (rawComponent instanceof TargetComponent tar) {
                    Direction tarLocation = tar.getTargetLocation();
                    int distFromTower = (int) Math.sqrt(Math.pow(tarLocation.x - towerLocation.x, 2) + Math.pow(tarLocation.y - towerLocation.y, 2));
                    if (distFromTower > atkRange) {
                        continue;
                    }
                    prepare = distFromTower < dist ? tar : prepare;
                    dist = Math.min(distFromTower, dist);
                }
            }
            if (prepare != null) {
                target = prepare;
                atkInterval = atkInterval == null ? 10 : atkInterval;
                atkLoad = 0;
            }
        } else {
            if (!target.isAliveState()) {
                target = null;
            } else {
                Direction tarLocation = target.getTargetLocation();
                int distFromTower = (int) Math.sqrt(Math.pow(tarLocation.x - towerLocation.x, 2) + Math.pow(tarLocation.y - towerLocation.y, 2));
                if (distFromTower > atkRange) {
                    target = null;
                } else {
                    if (atkLoad > atkInterval) {
                        int targetValue = target.getRestValue(getAtkValue());
                        Point towerLocation = getLocation();
                        Point targetLocation = target.getLocation();
                        GlobalParticleLine.registerLineParticle(Element.layerPanel, towerLocation, targetLocation, 10);
                        if (targetValue < 0) {
                            target = null;
                        }
                        atkLoad = 0;
                    } else {
                        atkLoad++;
                    }
                }
            }
        }
    }

    /**
     * atkValue计算
     *
     * @return
     */
    public int getAtkValue() {
        if (atkValue == null) {
            return 1;
        } else {
            return atkValue;
        }
    }

    private void addOperationMenu(JPanel panel) {
        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        popupMenu.setBackground(new Color(22, 22, 122, 10));

        JMenuItem deleteItem = getDeleteItem(panel);

        popupMenu.add(deleteItem);

        // 为 JLabel 添加右键点击事件
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.show(panel, e.getX(), e.getY()); // 显示右键菜单
                }
            }
        });
    }

    private static JMenuItem getDeleteItem(JPanel panel) {
        // 创建删除菜单项
        JMenuItem deleteItem = new JMenuItem("删除");
        deleteItem.setFont(MAIN_FONT);
        deleteItem.setBackground(new Color(57, 72, 208, 176));
        deleteItem.setMargin(new Insets(2, 2, 2, 2));
        deleteItem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        deleteItem.addActionListener(ac -> {
            GlobalMotionLine.removeComponents.add(panel);
            container.remove(panel);
        });
        return deleteItem;
    }
}