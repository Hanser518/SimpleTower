package frame.component;

import common.Element;
import entity.Direction;
import frame.pipeLine.GlobalMotionLine;
import frame.pipeLine.GlobalParticleLine;
import method.map.BuildMap;
import method.map.TransMap;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

import static common.FrameConstant.*;
import static frame.pipeLine.GlobalMotionLine.addToPrepareComponents;

public class SceneComponent extends JPanel {

    private static final int interval = UNIT_MOVE_COUNT * 2;

    private static int count = 0;

    private int sceneWidth, sceneHeight;

    private int[][] sceneMatrix;

    private JPanel[][] panelMatrix;

    private Point sp = null, ep = null;

    public SceneComponent(int width, int height) {
        super(null);
        initScene(width, height);
        initContent();
        TargetComponent.setMatrix(sceneMatrix);
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

                add(panelMatrix[i][j]);
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
                        } else if (sp != null && ep == null) {
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
                        TowerComponent TC = new TowerComponent(0);
                        TC.setBounds(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                        // TC.setBackground(new Color(169, 69, 89));
                        sceneMatrix[x][y] = 1;
                        TC.register(scene, new Point(x, y), 1);
                        Element.layerPanel.add(TC, Element.COMPONENT_LAYER);
                        // scene.add(TC);
                    }
                    GlobalParticleLine.createParticle(scene, panel, panel.getWidth());
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!GlobalMotionLine.components.isEmpty()) {
            for (JPanel component : GlobalMotionLine.components) {
                if (component instanceof TowerComponent TC) {

                    if (TC.getTargetComponent() != null && TC.getTargetComponent().isAliveState()) {
                        Point towerLocation = component.getLocation();
                        Point targetLocation = TC.getTargetComponent().getLocation();

                        int towerW = component.getWidth();
                        int towerH = component.getHeight();
                        int targetW = TC.getTargetComponent().getWidth();
                        int targetH = TC.getTargetComponent().getHeight();

                        g.setColor(new Color(28, 54, 124, 255));
                        g.drawLine(towerLocation.x + towerW / 2, towerLocation.y + towerH / 2, targetLocation.x + targetW / 2, targetLocation.y + targetH / 2);
                    }
                }
            }
        }
    }

    public void motion(){
        JPanel scene = this;
        if(count < interval)
            count++;
        else{
            if (ep != null && sp != null) {
                TargetComponent TC = new TargetComponent();
                TC.setBounds(sp.x * UNIT_SIZE, sp.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                TC.setBackground(new Color(147, 44, 44, 255));
                TC.register(scene, sp, ep);
                scene.add(TC);
            }
            count = 0;
        }
        repaint();
    }
}
