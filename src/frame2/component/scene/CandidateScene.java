package frame2.component.scene;

import entity.Direction;
import frame2.common.ComponentConstant;
import frame2.component.SceneComponent;
import frame2.component.TargetComponent;
import frame2.component.TowerComponent;
import frame2.component.effect.ParticleBrokenEffect;
import frame2.pipeLine.EffectLine;
import frame2.pipeLine.SceneLine;
import frame2.pipeLine.TargetLine;
import frame2.pipeLine.TowerLine;
import method.way.BuildSolution;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static frame2.common.ComponentConstant.*;
import static frame2.common.FrameConstant.*;

public class CandidateScene extends SceneComponent {

    private SceneComponent registerComponent;

    public CandidateScene(StageScene SC) {
        super();
        setBackground(new Color(47, 130, 234, 0));
        setBounds(0, 0, SC.getWidth(), SC.getHeight());
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                width -= 1;
                height -= 1;
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int intervalValue = UNIT_WIDTH;
                g2d.setStroke(new BasicStroke(intervalValue));
                for (int i = 0; i < width / intervalValue + 3; i++) {
                    if (i % 2 == 1) {
                        g2d.setColor(new Color(225, 224, 224, 127));
                    } else {
                        g2d.setColor(new Color(138, 138, 138, 127));
                    }
                    g2d.drawLine(i * intervalValue, 0, (i - 2) * intervalValue, height);
                }

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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    logout();
                }
            }
        });
    }

    /**
     * 暂停控制器
     */
    public void stopTimer() {
        EffectLine.pauseTimer();
        TargetLine.pauseTimer();
        TowerLine.pauseTimer();
        SceneLine.pauseTimer();
    }

    /**
     * 恢复控制器
     */
    public void resetTimer() {
        EffectLine.resetTimer();
        TargetLine.resetTimer();
        TowerLine.resetTimer();
        SceneLine.resetTimer();
    }

    /**
     * 注销组件
     */
    public void logout() {
        container.remove(this);
        // container.add(registerComponent, SCENE_LAYER);
        for (TowerComponent tower : CANDIDATE_LIST) {
            container.remove(tower);
        }
        resetTimer();
    }

    /**
     * 注册组件
     */
    public void register(JLayeredPane container, SceneComponent SC) {
        this.container = container;
        container.add(this, SPECIAL_LAYER);
        container.repaint();
        registerComponent = SC;
        visible();
        stopTimer();
    }

    /**
     * 内容计算
     */
    private void visible() {
        Direction compLocation;
        try {
            Class<?> compClass = registerComponent.getClass();
            Method getCompLocation = compClass.getMethod("getCompLocation");
            compLocation = (Direction) getCompLocation.invoke(registerComponent);
            // container.add(registerComponent, SPECIAL_LAYER);
        } catch (Exception e) {
            System.out.println("CANDIDATE VISIBLE ERROR: NO SUCH METHOD");
            return;
        }
        int index = 0;
        for (TowerComponent component : CANDIDATE_LIST){
            Direction towerLocation = BuildSolution.getNextPoint(compLocation, index);
            int tw = (int) (UNIT_WIDTH * 0.8);
            int th = (int) (UNIT_WIDTH * 0.8);
            component.setBounds(towerLocation.x * UNIT_WIDTH + (UNIT_WIDTH - tw) / 2, towerLocation.y * UNIT_HEIGHT + (UNIT_HEIGHT - th) / 2, tw, th);
            container.add(component, SPECIAL_LAYER);
            addInteraction(component, compLocation);
            index ++;
        }
    }

    private void addInteraction(TowerComponent tc, Direction compLocation){
        tc.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                tc.register(getContainer(), new Direction(compLocation.x, compLocation.y, 1));
                CANDIDATE_LIST.remove(tc);
                logout();
            }
        });
    }

    @Override
    public void incident() {

    }

    @Override
    protected void setContent(Graphics g, int width, int height) {

    }
}
