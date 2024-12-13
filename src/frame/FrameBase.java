package frame;

import common.Element;
import frame.annotation.InitMethod;
import frame.component.incident.CandidateComponent;
import frame.component.interaction.StanderInteractionComponent;
import frame.component.interaction.tower.LandResist;
import frame.component.interaction.tower.Lightning;
import frame.pipeLine.GlobalInteractionLine;
import frame.pipeLine.GlobalParticleLine;
import frame.component.incident.SceneComponent;
import frame.pipeLine.GlobalIncidentLine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
        componentCount.setText("Count: " + GlobalIncidentLine.getComponentsCount() +
                " | " + GlobalInteractionLine.tower.size() +
                " | " + GlobalInteractionLine.target.size() +
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
        GlobalIncidentLine.initializeGlobalTimer();
        GlobalInteractionLine.initializeGlobalTimer();

        // 初始化组件
        sceneComponent = new SceneComponent(8, 6);
        candidateComponent = new CandidateComponent();
        candidateComponent.setBounds(0, sceneComponent.getHeight(), sceneComponent.getWidth(), UNIT_SIZE);

        StanderInteractionComponent.setMatrix(sceneComponent.getSceneMatrix());


        layerPanel.add(candidateComponent, SCENE_LAYER);

        GlobalIncidentLine.addToPrepareComponents(sceneComponent);
        GlobalIncidentLine.addToPrepareComponents(candidateComponent);

        add(operationPanel, BorderLayout.WEST);
        add(layerPanel, BorderLayout.CENTER);
        add(componentCount, BorderLayout.NORTH);

    }

    public static SceneComponent getSceneComponent() {
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
            GlobalIncidentLine.addToPrepareComponents(candidateComponent);
            layerPanel.add(candidateComponent, SCENE_LAYER);
            repaint();
        });

        List<StanderInteractionComponent> compList = new ArrayList<>();
        compList.add(new Lightning());
        compList.add(new LandResist());

        // 将组件添加到面板中
        operationPanel.add(editButton);
        initShopButton(compList);
    }

    private void initShopButton(List<StanderInteractionComponent> compList) {
        for (JPanel comp : compList) {
            try {
                Class<?> compClass = comp.getClass();
                Constructor<?> compConstructor = compClass.getConstructor();
                Method getCost = compClass.getMethod("getCost");

                String compName = compClass.getSimpleName();
                JButton addButton = new JButton("BUY " + compName);
                addButton.addActionListener(ac -> {
                    try {
                        StanderInteractionComponent compPanel = (StanderInteractionComponent) compConstructor.newInstance();
                        if (CandidateComponent.getCompPoint() >= (int) getCost.invoke(compPanel)) {
                            CandidateComponent.addToComponent(compPanel);
                            CandidateComponent.deleteCompPoint((int) getCost.invoke(compPanel));
                            revalidate();
                            repaint();
                        }
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                });
                operationPanel.add(addButton);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}
