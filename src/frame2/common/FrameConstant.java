package frame2.common;

import javax.swing.*;

public class FrameConstant {

    /**
     * main
     */
    public static JFrame MAIN_FRAME;

    /**
     * 缩放系数
     */
    public static Double SCREEN_RATE = 0.35;

    /**
     * 屏幕宽度
     */
    public static Integer SCREEN_WIDTH;

    /**
     * 屏幕宽度
     */
    public static Integer SCREEN_HEIGHT;

    /**
     * main width
     */
    public static Integer FRAME_WIDTH;

    /**
     * main height
     */
    public static Integer FRAME_HEIGHT;

    /**
     * layer
     */
    public static JLayeredPane MAIN_LAYER = new JLayeredPane();

    /**
     * scene component layer
     */
    public static int SCENE_LAYER = 1;

    /**
     * tower&target component layer
     */
    public static int COMPONENT_LAYER = 3;

    /**
     * effect component layer
     */
    public static int EFFECT_LAYER = 5;
}
