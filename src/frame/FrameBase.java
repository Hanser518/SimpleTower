package frame;

import common.Element;
import frame.annotation.InitMethod;
import frame.pipeLine.GlobalParticleLine;
import frame.component.SceneComponent;
import frame.pipeLine.GlobalMotionLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import static common.FrameConstant.*;

import static common.Element.*;
import static frame.pipeLine.GlobalMotionLine.components;

public class FrameBase extends JFrame implements ActionListener {

    private static Integer screenWidth;
    private static Integer screenHeight;

    private static Integer frameWidth;
    private static Integer frameHeight;

    //FRAME_REFRESH_INTERVAL
    private Timer freshTimer = new Timer(1000 / FRAME_REFRESH_INTERVAL, this);


    public static void main(String[] args) {
        new FrameBase();
    }

    public FrameBase() {
        setTitle("Simple Tower");
        setFont(MAIN_FONT);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = screenSize.height;
        frameWidth = (int) (screenWidth * HALF_SCREEN);
        frameHeight = (int) (screenHeight * HALF_SCREEN);
        // 窗口宽高
        setBounds(
                (int) (screenWidth * (1 - HALF_SCREEN) / 2),
                (int) (screenHeight * (1 - HALF_SCREEN) / 2),
                frameWidth,
                frameHeight + 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        init();
//        TargetComponent target = new TargetComponent();
//        target.setBounds(10, 10, 30, 30);
//        target.setBackground(new Color(167, 56, 80));
//        MAIN_PANEL.add(target);
//        target.register(new Point(1, 1), new Point(ZERO_ONE_MATRIX.length - 2, ZERO_ONE_MATRIX[0].length - 2));

        SceneComponent sc = new SceneComponent(8, 6);

//        MAIN_PANEL.add(sc);
        layerPanel.add(sc, SCENE_LAYER);
        GlobalMotionLine.addToPrepareComponents(sc);

        add(layerPanel, BorderLayout.CENTER);
        add(componentCount, BorderLayout.NORTH);


        setVisible(true);
        freshTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        componentCount.setText("COUNT: " + components.size());
    }

    private void init() {
        try {
            Method[] methods = Element.class.getDeclaredMethods();
            Element initElement = Element.class.getConstructor().newInstance();
            // 遍历所有方法，找到标记了 @InitMethod 注解的方法并调用
            for (Method method : methods) {
                if (method.isAnnotationPresent(InitMethod.class)) {
                    method.invoke(initElement);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        GlobalParticleLine.initializeGlobalTimer();

        // getPanelMatrix(10, 10);

        GlobalMotionLine.initializeGlobalTimer();
    }
}
