package frame;

import frame.annotation.InitMethod;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import static common.FrameConstant.*;
import static frame.Element.MAIN_PANEL;

public class FrameBase extends JFrame implements ActionListener {

    private static Integer screenWidth;
    private static Integer screenHeight;

    private static Integer frameWidth;
    private static Integer frameHeight;


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

        add(MAIN_PANEL, BorderLayout.CENTER);

        setVisible(true);
        freshTimer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // System.out.println("?");
    }

    private void init(){
        try{
            Method[] methods = InitElement.class.getDeclaredMethods();
            InitElement initElement = InitElement.class.getConstructor().newInstance();
            // 遍历所有方法，找到标记了 @InitMethod 注解的方法并调用
            for (Method method : methods) {
                if (method.isAnnotationPresent(InitMethod.class)) {
                    method.invoke(initElement);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
