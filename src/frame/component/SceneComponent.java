package frame.component;

import method.map.BuildMap;
import method.map.TransMap;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static common.FrameConstant.*;
import static frame.Element.MAIN_PANEL;

public class SceneComponent extends JPanel {

    private int sceneWidth, sceneHeight;

    private int[][] sceneMatrix;

    private JPanel[][] panelMatrix;

    private Point sp = null;

    public SceneComponent(int width, int height){
        super(null);
        initScene(width, height);
        initContent();
    }

    private void initScene(int width, int height){
        sceneMatrix = BuildMap.getMap(width, height);
        sceneMatrix = TransMap.getTransMatrix(sceneMatrix);
        sceneWidth = sceneMatrix.length;
        sceneHeight = sceneMatrix[0].length;
        this.setBounds(0, 0, sceneWidth * UNIT_SIZE, sceneHeight * UNIT_SIZE);
    }

    private void initContent(){
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

    private void addFunctionClickTarget(JPanel panel, int x, int y){
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (sceneMatrix[x][y] != 1){
                        if (sp == null){
                            sp = new Point(x, y);
                            panelMatrix[x][y].setBackground(new Color(69, 109, 169, 129));
                            // 非常狠毒的一行代码，使我方块来回跳跃
                            // sceneMatrix[x][y] = -1;
                        } else {
                            panelMatrix[x][y].setBackground(new Color(211, 10, 10, 142));
                            TargetComponent.initializeGlobalTimer(sceneMatrix);
                            TargetComponent TC = new TargetComponent();
                            TC.setBounds(sp.x * UNIT_SIZE, sp.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                            TC.setBackground(new Color(169, 69, 89));
                            TC.register(sp, new Point(x, y));
                            add(TC);
                            sp = null;
                        }
                    }
                }
            }
        });
    }
}
