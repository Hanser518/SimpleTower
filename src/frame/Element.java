package frame;

import entity.Direction;
import frame.annotation.InitMethod;
import method.map.BuildMap;
import method.map.TransMap;
import method.way.BuildSolution;

import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;

import static common.FrameConstant.*;

public class Element {

    /**
     * 主容器，可能后面会调整为图层形式
     */
    public static JPanel MAIN_PANEL = new JPanel(null);

    @InitMethod
    public void initMainPanel() {
        MAIN_PANEL.setBackground(new Color(0xFFAEAEAE, true));
    }

    /**
     * 地图数据
     */
    public static int[][] ZERO_ONE_MATRIX;

    /**
     * 获取panel矩阵，将map数据映射到视图中
     */
    public static JPanel[][] getPanelMatrix(int width, int height) {
        // 原始地图数据
        int[][] mapData = BuildMap.getMap(width, height);
        // 转换为01矩阵
        ZERO_ONE_MATRIX = TransMap.getTransMatrix(mapData);
        // 映射到JPanel中
        JPanel[][] panelMatrix = new JPanel[ZERO_ONE_MATRIX.length][ZERO_ONE_MATRIX[0].length];

        for (int i = 0; i < panelMatrix.length; i++) {
            for (int j = 0; j < panelMatrix[i].length; j++) {
                if (i != 0 && j != 0 && i < panelMatrix.length - 1 && j < panelMatrix[0].length - 1) {
                    double r = Math.random();
                    if (r > 0.7) {
                        ZERO_ONE_MATRIX[i][j] = 0;
                    }
                }
                panelMatrix[i][j] = new JPanel(null);
                panelMatrix[i][j].setBounds(0, 0, 15, 15);
                panelMatrix[i][j].setBackground(ZERO_ONE_MATRIX[i][j] == 1 ? WALL_COLOR : ROAD_COLOR);

            }
        }
        return panelMatrix;
    }

    /**
     * 路线数据
     */
    public static List<Direction> MATRIX_SOLUTION;

    /**
     * 获取路线数据，并将其映射
     */
    public static List<JPanel> getSolutionList(int[][] matrix, Point start, Point end) {
        List<JPanel> result = new ArrayList<>();
        MATRIX_SOLUTION = BuildSolution.getMapWay(matrix, start, end);
        for (Point p : MATRIX_SOLUTION) {
            JPanel testPanel = new JPanel(null);
            testPanel.setBounds(p.x * 15, p.y * 15, 15, 15);
            testPanel.setBackground(new Color(0x84063251, true));
            result.add(testPanel);
        }
        return result;
    }

    public static JLabel componentCount = new JLabel("0");

    @InitMethod
    public void initComponentCount() {
        componentCount.setFont(MAIN_FONT);
    }

    /**
     * 示例方法
     */
    public static JPanel TEST_PANEL = new JPanel(null);

    @InitMethod
    public void initTESTPanel() {
        TEST_PANEL.setBackground(new Color(0x84E73E3E, true));
        TEST_PANEL.setBounds(0, 0, 50, 50);
    }

    public static JLabel TEST_LABEL = new JLabel("TEST");

    @InitMethod
    public void initTestLabel() {
        TEST_LABEL.setBounds(0, 0, 100, 50);
        TEST_LABEL.setBackground(new Color(0x8B6FAB9B, true));
        TEST_LABEL.setOpaque(true);
    }
}
