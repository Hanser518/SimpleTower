package frame2.component.scene;

import entity.Direction;
import frame2.component.SceneComponent;
import frame2.component.tower.LightningTower;
import frame2.pipeLine.SceneLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;
import static frame2.common.FrameConstant.MAIN_LAYER;

public class PlatformScene extends SceneComponent {

    private Direction compLocation = new Direction(0, 0, -1);

    public PlatformScene() {
        super();
        setBackground(new Color(71, 74, 84));
        setBounds(0, 0, UNIT_WIDTH, UNIT_HEIGHT);
        addRightClickMenu(this);
    }

    public PlatformScene(int i, int j) {
        super();
        setBackground(new Color(71, 74, 84));
        setBounds(0, 0, UNIT_WIDTH, UNIT_HEIGHT);
        addRightClickMenu(this);
        compLocation = new Direction(i, j, -1);
    }

    @Override
    public void incident() {

    }

    @Override
    protected void setContent(Graphics g, int width, int height) {
        width -= 1;
        height -= 1;
        Graphics2D g2d = (Graphics2D) g;
        int steps = Math.min(width / 5, height / 5);
        g2d.setColor(new Color(166, 170, 181));
        for (int i = 0; i < 3; i++) {
            g2d.drawRect(i * steps, i * steps, width - 2 * i * steps, height - 2 * i * steps);
        }
    }

    public Direction getCompLocation() {
        return compLocation;
    }


    private static void addRightClickMenu(SceneComponent comp) {
        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem skillItem = new JMenuItem("SKILL");

        JMenuItem deleteItem = new JMenuItem("REMOVE");
        deleteItem.addActionListener(e -> {
            // 从面板中移除 JLabel
            SceneLine.addToRemoveComponents(comp);
            comp.getContainer().remove(comp);
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
}
