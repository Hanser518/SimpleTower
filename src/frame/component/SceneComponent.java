package frame.component;

import common.Element;
import frame.component.target.TargetComponent;
import frame.component.tower.LightningComponent;
import frame.pipeLine.GlobalMotionLine;
import frame.pipeLine.GlobalParticleLine;
import method.map.BuildMap;
import method.map.TransMap;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static common.Constant.UNIT_MOVE_COUNT;
import static common.Element.*;
import static common.FrameConstant.*;

public class SceneComponent extends JPanel implements StanderComponent {

    private static final int interval = UNIT_MOVE_COUNT * 2;

    private static int count = 0;

    private int sceneWidth, sceneHeight;

    private int[][] sceneMatrix;

    private JPanel[][] panelMatrix;

    private Point sp = null, ep = null;

    public SceneComponent(int width, int height) {
        super(null);
        this.setDoubleBuffered(true);
        this.setBackground(new Color(0, 0, 0, 0));
        initScene(width, height);
        initContent();
    }

    private void initScene(int width, int height) {
        sceneMatrix = BuildMap.getMap(width, height);
        sceneMatrix = TransMap.getTransMatrix(sceneMatrix);
        sceneWidth = sceneMatrix.length;
        sceneHeight = sceneMatrix[0].length;
        TargetComponent.setMatrix(sceneMatrix);
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
                layerPanel.add(panelMatrix[i][j], SCENE_LAYER);
            }
        }
        layerPanel.add(this);
    }

    public void resetScene(int width, int height) {
        sceneMatrix = BuildMap.getMap(width, height);
        sceneMatrix = TransMap.getTransMatrix(sceneMatrix);
        sceneWidth = sceneMatrix.length;
        sceneHeight = sceneMatrix[0].length;
        TargetComponent.setMatrix(sceneMatrix);
        this.setBounds(0, 0, sceneWidth * UNIT_SIZE, sceneHeight * UNIT_SIZE);
        Element.layerPanel.removeAll();
        GlobalMotionLine.components.clear();
        GlobalMotionLine.components.add(this);
        initContent();
        sp = null;
        ep = null;
        TargetComponent.setMatrix(sceneMatrix);
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
//                    if (sceneMatrix[x][y] == 1) {
//                        LightningComponent TC = new LightningComponent();
//                        TC.setBounds(x * UNIT_SIZE, y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
//                        sceneMatrix[x][y] = 1;
//                        TC.register(layerPanel, new Point(x, y), 1);
//                        layerPanel.add(TC, COMPONENT_LAYER);
//                    }
                    GlobalParticleLine.registerBrokenParticle(layerPanel, panel, panel.getWidth());
                }
            }
        });
    }

    public JPanel[][] getPanelMatrix(){
        return panelMatrix;
    }

    public int[][] getSceneMatrix(){
        return sceneMatrix;
    }

    @Override
    public void motion() {
        if (count < interval)
            count++;
        else {
            if (ep != null && sp != null) {
                TargetComponent TC = new TargetComponent();
                TC.setBounds(sp.x * UNIT_SIZE, sp.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                TC.setBackground(new Color(147, 44, 44, 255));
                TC.register(layerPanel, sp, ep);
                this.add(TC);
                layerPanel.add(TC, COMPONENT_LAYER);
            }
            count = 0;
        }
    }
}
