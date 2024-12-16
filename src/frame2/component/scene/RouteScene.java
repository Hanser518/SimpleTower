package frame2.component.scene;

import entity.Direction;
import frame2.component.SceneComponent;
import frame2.component.TargetComponent;
import frame2.component.TowerComponent;
import frame2.component.effect.ParticleLightingEffect;
import frame2.pipeLine.SceneLine;
import frame2.pipeLine.TargetLine;
import frame2.pipeLine.TowerLine;
import method.way.BuildSolution;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;
import static frame2.common.FrameConstant.MAIN_LAYER;
import static frame2.common.FrameConstant.SCENE_LAYER;

public class RouteScene extends SceneComponent {

    /**
     * target list<br>
     * target will be put by the interval list
     */
    private List<TargetComponent> targetList = new ArrayList<>();

    /**
     * interval list<br>
     * if interval list is null, 30 will be the default value
     */
    private List<Integer> intervalList = new ArrayList<>();

    /**
     * 当前序号
     */
    private int serial = 0;

    /**
     * scheduleValue<br>
     * if scheduleValue equals the interval value, the target will be register
     */
    private int scheduleValue;

    /**
     * loop<br>
     * the target will be registered in a cyclic list of intervals
     */
    private boolean loop = true;

    /**
     * the start point of target
     */
    private JPanel startPanel = new JPanel(null);

    /**
     * the end point of target
     */
    private JPanel endPanel = new JPanel(null);

    /**
     * matrix data
     */
    private int[][] sceneMatrix;

    /**
     * start
     */
    private Point startPoint;

    /**
     * end
     */
    private Point endPoint;

    /**
     * path list<br>
     * target will follow the path
     */
    private List<Direction> path = new ArrayList<>();

    /**
     * init class
     *
     * @param sceneMatrix map data
     */
    public RouteScene(int[][] sceneMatrix) {
        super();
        this.sceneMatrix = sceneMatrix;
    }

    /**
     * 注册组件
     *
     * @param container  容器
     * @param startPoint 起点
     * @param endPoint   终点
     */
    public void register(JLayeredPane container, Point startPoint, Point endPoint) {
        startPanel.setBackground(new Color(255, 0, 36, 127));
        startPanel.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.RED);
                g.drawRect(10, 10, width - 21, height - 21);
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
        this.startPoint = startPoint;
        container.add(startPanel, SCENE_LAYER);
        startPanel.setBounds(startPoint.x * UNIT_WIDTH, startPoint.y * UNIT_HEIGHT, UNIT_WIDTH, UNIT_HEIGHT);

        endPanel.setBackground(new Color(0, 102, 255, 127));
        endPanel.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(Color.BLUE);
                g.drawRect(10, 10, width - 21, height - 21);
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
        this.endPoint = endPoint;
        container.add(endPanel, SCENE_LAYER);
        endPanel.setBounds(endPoint.x * UNIT_WIDTH, endPoint.y * UNIT_HEIGHT, UNIT_WIDTH, UNIT_HEIGHT);

        updatePath();
        // container.add(this, SCENE_LAYER);
        super.register(container);
    }

    /**
     * 更新路径
     */
    public void updatePath() {
        path = BuildSolution.getMapWay(sceneMatrix, startPoint, endPoint);
    }

    /**
     * 直接设置路径
     *
     * @param path 路径
     */
    public void setPath(List<Direction> path) {
        this.path = path;
    }

    /**
     * add target
     *
     * @param target TargetComponent
     * @param index  index in targetList
     */
    public void addTarget(TargetComponent target, Integer index) {
        if (index == null) {
            targetList.add(target);
        } else if (index < targetList.size()) {
            targetList.add(index, target);
        }
    }

    /**
     * remove target
     *
     * @param target TargetComponent
     */
    public void removeTarget(TargetComponent target) {
        remove(target);
    }

    @Override
    public void incident() {
        // 检测是否重置serial
        if (loop && serial >= targetList.size()) {
            serial = 0;
        }
        // 获取间隔
        if (serial < targetList.size()) {
            int interval = intervalList.isEmpty() ? 30 : intervalList.get(serial);
            if (scheduleValue >= interval) {
                TargetComponent TC = targetList.get(serial);
                if (!TargetLine.getComponents().contains(TC) && TC.isAlive()) {
                    TC.register(container, path);
                }
                scheduleValue = 0;
                serial++;
            } else {
                scheduleValue++;
            }
        }
    }

    @Override
    protected void setContent(Graphics g, int width, int height) {
        // g.fillOval(endPoint.x * UNIT_WIDTH, endPoint.y * UNIT_HEIGHT, UNIT_WIDTH, UNIT_HEIGHT);
    }

}
