package frame;

import frame.annotation.InitMethod;

import javax.swing.*;
import java.awt.*;

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
