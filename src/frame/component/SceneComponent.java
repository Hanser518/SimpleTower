package frame.component;

import entity.Direction;
import method.map.BuildMap;
import method.map.TransMap;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static common.FrameConstant.*;

public class SceneComponent extends JPanel {
    /**
     * 设置全局刷新率
     */
    private static final int UPDATE_DELAY = 2 * 1000;

    private int sceneWidth, sceneHeight;

    private int[][] sceneMatrix;

    private JPanel[][] panelMatrix;

    private Point sp = null, ep = null;

    /**
     * 设置全局定时器
     */
    private Timer sceneTimer;

    public SceneComponent(int width, int height) {
        super(null);
        initScene(width, height);
        initContent();
        initTimer();
    }

    private void initTimer() {
        JPanel scene = this;
        if (sceneTimer == null) {
            sceneTimer = new Timer(UPDATE_DELAY, e -> {
                if (ep != null && sp != null){
                    TargetComponent.initializeGlobalTimer(sceneMatrix);
                    TargetComponent TC = new TargetComponent();
                    TC.setBounds(sp.x * UNIT_SIZE, sp.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    TC.setBackground(new Color(242, 26, 69, 135));
                    TC.register(scene, sp, ep);
                    add(TC);
                }
            });
        }
        sceneTimer.start();
    }

    private void initScene(int width, int height) {
        sceneMatrix = BuildMap.getMap(width, height);
        sceneMatrix = TransMap.getTransMatrix(sceneMatrix);
        sceneWidth = sceneMatrix.length;
        sceneHeight = sceneMatrix[0].length;
        this.setBounds(0, 0, sceneWidth * UNIT_SIZE, sceneHeight * UNIT_SIZE);
    }

    private void initContent() {
        panelMatrix = new JPanel[sceneWidth][sceneHeight];
        for (int i = 0; i < panelMatrix.length; i++) {
            for (int j = 0; j < panelMatrix[i].length; j++) {
                if (i != 0 && j != 0 && i < panelMatrix.length - 1 && j < panelMatrix[0].length - 1) {
                    double r = Math.random();
                    if (r > 0.8) {
                        sceneMatrix[i][j] = 0;
                    }
                }
                panelMatrix[i][j] = new JPanel(null);
                panelMatrix[i][j].setBounds(i * UNIT_SIZE, j * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                panelMatrix[i][j].setBackground(sceneMatrix[i][j] == 1 ? WALL_COLOR : ROAD_COLOR);
                addFunctionClickTarget(panelMatrix[i][j], i, j);

                this.add(panelMatrix[i][j]);
            }
        }
    }

    private void addFunctionClickTarget(JPanel panel, int x, int y) {
        JPanel scene = this;
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    if (sceneMatrix[x][y] != 1) {
                        if (sp == null && ep == null) {
                            sp = new Point(x, y);
                            panelMatrix[x][y].setBackground(new Color(69, 109, 169, 129));
                            // 非常狠毒的一行代码，使我方块来回跳跃
                            // sceneMatrix[x][y] = -1;
                        } else if (sp != null && ep == null){
                            ep = new Point(x, y);
                            panelMatrix[x][y].setBackground(new Color(211, 10, 10, 142));
                        } else {
                            sp = new Point(x, y);
                            panelMatrix[x][y].setBackground(new Color(69, 109, 169, 129));
                        }
                    }
                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    if (sceneMatrix[x][y] == 1) {
                        System.out.println("TOWER!");
                        TowerComponent.initializeGlobalTimer();
                        TowerComponent TC = new TowerComponent(0);
                        TC.setBounds(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                        // TC.setBackground(new Color(169, 69, 89));
                        TC.register(scene, new Point(x, y), 1);
                        add(TC);
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!TowerComponent.components.isEmpty()) {
            for (TowerComponent component : TowerComponent.components) {
                if (component.getTargetComponent() != null && component.getTargetComponent().isAliveState()) {
                    Point towerLocation = component.getLocation();
                    Point targetLocation = component.getTargetComponent().getLocation();

                    int towerW = component.getWidth();
                    int towerH = component.getHeight();
                    int targetW = component.getTargetComponent().getWidth();
                    int targetH = component.getTargetComponent().getHeight();

                    g.setColor(new Color(28, 54, 124, 255));
                    g.drawLine(towerLocation.x + towerW / 2, towerLocation.y + towerH / 2, targetLocation.x + targetW / 2, targetLocation.y + targetH / 2);
                }
            }
        }
    }

}
