package frame;

import frame.annotation.InitMethod;
import method.map.BuildMap;
import method.map.TransMap;

import javax.swing.*;
import java.awt.*;

import static common.FrameConstant.*;

public class Element {

    /**
     * 主容器，可能后面会调整为图层形式
     */
    public static JPanel MAIN_PANEL = new JPanel(null);

    @InitMethod
    public void initMainPanel(){
        MAIN_PANEL.setBackground(new Color(0xFFAEAEAE, true));
    }

    /**
     * 获取panel矩阵，将map数据映射到视图中
     */
    public static JPanel[][] getPanelMatrix(int width, int height){
        // 原始地图数据
        int[][] mapData = BuildMap.getMap(width, height);
        // 转换为01矩阵
        int[][] matrix = TransMap.getTransMatrix(mapData);
        // 映射到JPanel中
        JPanel[][] panelMatrix = new JPanel[matrix.length][matrix[0].length];

        for(int i = 0;i < panelMatrix.length; i++){
            for(int j = 0;j < panelMatrix[i].length; j++){
                panelMatrix[i][j] = new JPanel(null);
                panelMatrix[i][j].setBounds(0, 0, 15, 15);
                panelMatrix[i][j].setBackground(matrix[i][j] == 1 ? WALL_COLOR : ROAD_COLOR);

            }
        }
        return panelMatrix;
    }

    public static JLabel componentCount = new JLabel("0");

    @InitMethod
    public void initComponentCount(){
        componentCount.setFont(MAIN_FONT);
    }

    /**
     * 示例方法
     */
    public static JPanel TEST_PANEL = new JPanel(null);

    @InitMethod
    public void initTESTPanel(){
        TEST_PANEL.setBackground(new Color(0x84E73E3E, true));
        TEST_PANEL.setBounds(0, 0, 50, 50);
    }

    public static JLabel TEST_LABEL = new JLabel("TEST");

    @InitMethod
    public void initTestLabel(){
        TEST_LABEL.setBounds(0, 0, 100, 50);
        TEST_LABEL.setBackground(new Color(0x8B6FAB9B, true));
        TEST_LABEL.setOpaque(true);
    }
}
