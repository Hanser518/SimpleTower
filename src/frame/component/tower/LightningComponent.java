package frame.component.tower;

import common.Element;
import entity.Direction;
import frame.component.StanderComponent;
import frame.component.target.TargetComponent;
import frame.pipeLine.GlobalMotionLine;
import frame.pipeLine.GlobalParticleLine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static common.Constant.FRAME_REFRESH_INTERVAL;
import static common.FrameConstant.*;
import static frame.pipeLine.GlobalMotionLine.components;

public class LightningComponent extends JPanel implements StanderComponent {

    /**
     * 组件消耗
     */
    private static final int cost = 5;

    /**
     * 点位标识
     */
    private Direction towerLocation;


    public Direction getTowerLocation() {
        return towerLocation;
    }

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
    private final Integer atkValue = 5;

    /**
     * atk load
     */
    private int atkLoad = 20;

    /**
     * atk interval
     */
    private final Integer atkInterval = FRAME_REFRESH_INTERVAL / 2;

    private JPanel locPanel;

    /**
     * atk Range
     */
    private final Integer atkRange = 5;

    public LightningComponent() {
        super(null);
        setBackground(new Color(70, 58, 140, 0));
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                int wSize = UNIT_SIZE / 8;
                int hSize = UNIT_SIZE / 8;
                Graphics2D g2d = (Graphics2D) g;

                g2d.setColor(new Color(212, 194, 242, 189));
                g2d.fillRoundRect(x, y, width - 1, height - 1, wSize * 4, hSize * 4);

                g2d.setColor(new Color(212, 194, 242, 255));
                g2d.drawRoundRect(x, y, width - 1, height - 1, wSize * 4, hSize * 4);

                g2d.setColor(new Color(63, 57, 154, 255));

                g2d.fillRect(wSize, hSize, wSize, hSize);
                g2d.fillRect(wSize * 2, hSize * 2, wSize * 3, hSize * 3);
                g2d.fillRect(wSize * 4, hSize * 4, wSize * 2, hSize * 2);
                g2d.fillRect(wSize * 6, hSize * 6, wSize, hSize);
                g2d.fillRect(wSize * 7, hSize * 7, wSize, hSize);
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
    public void register(JLayeredPane panel, Point location, JPanel locPanel) {
        container = panel;
        this.towerLocation = new Direction(location, -1);
        this.locPanel = locPanel;
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
                        int targetValue = target.getRestValue(atkValue);
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

    public static int getCost() {
        return cost;
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

    private JMenuItem getDeleteItem(JPanel panel) {
        // 创建删除菜单项
        JMenuItem deleteItem = new JMenuItem("删除");
        deleteItem.setFont(MAIN_FONT);
        deleteItem.setBackground(new Color(57, 72, 208, 176));
        deleteItem.setMargin(new Insets(2, 2, 2, 2));
        deleteItem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        deleteItem.addActionListener(ac -> {
            GlobalMotionLine.removeComponents.add(panel);
            container.remove(panel);
            container.add(locPanel, Element.COMPONENT_LAYER);
        });
        return deleteItem;
    }
}
