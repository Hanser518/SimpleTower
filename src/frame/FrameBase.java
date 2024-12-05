package frame;

import frame.annotation.InitMethod;
import frame.component.ParticleComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import static common.FrameConstant.*;
import static frame.Element.*;

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
//        MAIN_PANEL.add(TEST_LABEL);
//        MAIN_PANEL.add(TEST_PANEL);
        add(MAIN_PANEL, BorderLayout.CENTER);
        add(componentCount, BorderLayout.NORTH);

//        JPanel[][] panelMatrix = getPanelMatrix(30, 15);
//        int w = panelMatrix[0][0].getWidth();
//        int h = panelMatrix[0][0].getHeight();
//        for(int i = 0;i < panelMatrix.length; i++){
//            for(int j = 0;j < panelMatrix[i].length; j++){
//                panelMatrix[i][j].setBounds(i * w, j * h, w, h);
//                MAIN_PANEL.add(panelMatrix[i][j]);
//            }
//        }
        setVisible(true);
        freshTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // example();
        ParticleComponent panel = new ParticleComponent();
        panel.setBounds((int) (Math.random() * frameWidth), (int) (Math.random() * 20), (int) (Math.random() * 100), (int) (Math.random() * 100));
        panel.setBackground(new Color((int) (Math.random() * 189 + 20), (int) (Math.random() * 169 + 20), (int) (Math.random() * 169 + 20), 110));
        MAIN_PANEL.add(panel);
        panel.register();
        componentCount.setText("Count:" + ParticleComponent.getComponentsCount());
    }

    private void example() {
        Point labelPoint = TEST_LABEL.getLocationOnScreen();
        Point testPoint = TEST_PANEL.getLocationOnScreen();
        Point panelPoint = MAIN_PANEL.getLocationOnScreen();
        int x = 6 - (int) (Math.random() * 10);
        int y = 6 - (int) (Math.random() * 10);
        TEST_LABEL.setLocation(labelPoint.x - panelPoint.x + x, labelPoint.y - panelPoint.y + y);
        TEST_PANEL.setLocation(testPoint.x - panelPoint.x + y, testPoint.y - panelPoint.y + x);
        this.repaint();
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
        ParticleComponent.initializeGlobalTimer();
    }
}
