package frame;

import common.Element;
import frame.annotation.InitMethod;
import frame.component.CandidateComponent;
import frame.component.tower.LightningComponent;
import frame.pipeLine.GlobalParticleLine;
import frame.component.SceneComponent;
import frame.pipeLine.GlobalMotionLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;

import static common.Constant.*;
import static common.FrameConstant.*;

import static common.Element.*;

public class FrameBase extends JFrame implements ActionListener {

    //FRAME_REFRESH_INTERVAL
    private Timer freshTimer = new Timer(1000 / FRAME_REFRESH_INTERVAL, this);

    public static JPanel operationPanel;

    public static SceneComponent sceneComponent;

    public static CandidateComponent candidateComponent;

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
        frameHeight = (int) (screenHeight * HALF_SCREEN) + 50;
        // 窗口宽高
        setBounds(
                (int) (screenWidth * (1 - HALF_SCREEN) / 2),
                (int) (screenHeight * (1 - HALF_SCREEN) / 2),
                frameWidth,
                frameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        init();
        setVisible(true);
        freshTimer.start();
        baseFrame = this;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(this::repaint);
        componentCount.setText("Count: " + GlobalMotionLine.getComponentsCount() +
                " | " + GlobalParticleLine.getComponentsCount() +
                " | " + CandidateComponent.getCompPoint());
    }

    private void init() {
        // 初始化所有Element元素
        try {
            Method[] methods = Element.class.getDeclaredMethods();
            Element initElement = Element.class.getConstructor().newInstance();
            // 遍历所有方法，找到标记了 @InitMethod 注解的方法并调用
            for (Method method : methods) {
                if (method.isAnnotationPresent(InitMethod.class)) {
                    method.invoke(initElement);
                }
            }
            Method[] fMethods = FrameBase.class.getDeclaredMethods();
            // 遍历所有方法，找到标记了 @InitMethod 注解的方法并调用
            for (Method method : fMethods) {
                if (method.isAnnotationPresent(InitMethod.class)) {
                    method.invoke(this);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 初始化管线
        GlobalParticleLine.initializeGlobalTimer();
        GlobalMotionLine.initializeGlobalTimer();

        // 初始化组件
        sceneComponent = new SceneComponent(8, 6);
        candidateComponent = new CandidateComponent();
        candidateComponent.setBounds(0, sceneComponent.getHeight(), sceneComponent.getWidth(), 50);

        layerPanel.add(candidateComponent, SCENE_LAYER);

        GlobalMotionLine.addToPrepareComponents(sceneComponent);
        GlobalMotionLine.addToPrepareComponents(candidateComponent);

        add(operationPanel, BorderLayout.WEST);
        add(layerPanel, BorderLayout.CENTER);
        add(componentCount, BorderLayout.NORTH);

    }

    public static SceneComponent getSceneComponent(){
        return sceneComponent;
    }

    @InitMethod
    private void buildOperationPanel() {
        operationPanel = new JPanel();
        operationPanel.setLayout(new BoxLayout(operationPanel, BoxLayout.Y_AXIS));

        JButton editButton = new JButton("Edit Matrix");
        editButton.addActionListener(ac -> {
            int w = (int) (Math.random() * 20 + 7);
            int h = (int) (Math.random() * 10 + 7);
            sceneComponent.resetScene((w - 1) / 2, (h - 1) / 2);
            candidateComponent = new CandidateComponent();
            candidateComponent.setBounds(0, sceneComponent.getHeight(), sceneComponent.getWidth(), UNIT_SIZE);
            GlobalMotionLine.addToPrepareComponents(candidateComponent);
            layerPanel.add(candidateComponent, SCENE_LAYER);
            repaint();
        });

        JButton addButton = new JButton("BUY COMP");
        addButton.addActionListener(ac -> {
            if (CandidateComponent.getCompPoint() >= LightningComponent.getCost()){
                CandidateComponent.addToComponent(new LightningComponent());
                CandidateComponent.deleteCompPoint(LightningComponent.getCost());
                System.out.println(CandidateComponent.getComponentNum());
                revalidate();
                repaint();
            }
        });

        // 将组件添加到面板中
        operationPanel.add(editButton);
        operationPanel.add(addButton);
    }
}
