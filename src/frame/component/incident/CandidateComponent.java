package frame.component.incident;

import common.Constant;
import common.Element;
import frame.FrameBase;
import frame.component.scene.RoadComponent;
import frame.component.scene.WallComponent;
import frame.component.interaction.discard.LandComponent;
import frame.component.interaction.discard.LightningComponent;
import frame.pipeLine.GlobalIncidentLine;
import frame.pipeLine.GlobalInteractionLine;
import frame.pipeLine.GlobalParticleLine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static common.Element.COMPONENT_LAYER;
import static common.Element.layerPanel;
import static common.FrameConstant.UNIT_SIZE;

public class CandidateComponent extends JPanel implements StanderIncidentComponent {

    /**
     * 组件点数
     */
    private static int compPoint = 10;

    /**
     * 进度
     */
    private static int scheduleValue = 0;

    /**
     * 待选组件列表
     */
    private static List<JPanel> componentList = new ArrayList<>();


    private static boolean isChange = false;

    /**
     *
     */
    private static Point initialClick;

    private static Point compLocation;

    public CandidateComponent() {
        super(null);
        setBackground(new Color(13, 56, 73, 127));
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(new Color(77, 22, 25));
                g.drawRect(x + 1, y + 1, width - 2, height - 2);
                g.setColor(new Color(13, 56, 73, 127));
                g.fillRect(x + 1, y + 1, (width - 2) * scheduleValue / Constant.FRAME_REFRESH_INTERVAL, height - 2);
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return null;
            }

            @Override
            public boolean isBorderOpaque() {
                return false;
            }
        });
    }

    public static void addToComponent(JPanel component) {
        try {
            for (MouseListener ml : component.getMouseListeners()){
                component.removeMouseListener(ml);
            }
            addDragFunction(component);
            componentList.add(component);
            isChange = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void addDragFunction(JPanel component) {
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    initialClick = e.getPoint(); // 记录初始点击位置
                    GlobalIncidentLine.pauseTimer();
                    GlobalParticleLine.pauseTimer();
                    GlobalInteractionLine.pauseTimer();
                    // Constant.FRAME_REFRESH_INTERVAL *= 3;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    registerTower(component);
                    GlobalIncidentLine.continueTimer();
                    GlobalParticleLine.continueTimer();
                    GlobalInteractionLine.continueTimer();
                    //  Constant.FRAME_REFRESH_INTERVAL /= 3;
                    isChange = true;
                }
            }

        });

        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    // 获取当前鼠标在面板中的位置
                    int mouseX = e.getXOnScreen();
                    int mouseY = e.getYOnScreen();
                    // 计算 JLabel 的新位置
                    Point panelLocation = Element.layerPanel.getLocationOnScreen();
                    int newX = mouseX - panelLocation.x - initialClick.x;
                    int newY = mouseY - panelLocation.y - initialClick.y;
                    component.setLocation(newX, newY);

                    Point sceneLocation = FrameBase.getSceneComponent().getLocationOnScreen();
                    Point componentLocation = component.getLocationOnScreen();
                    int xIndex = (componentLocation.x - sceneLocation.x) / UNIT_SIZE;
                    int yIndex = (componentLocation.y - sceneLocation.y) / UNIT_SIZE;
                    compLocation = new Point(xIndex, yIndex);
                    // System.out.println(panelLocation + " " + componentLocation + " | " + xIndex + "," + yIndex);
                }
            }
        });
    }

    private static void registerTower(JPanel component) {
        SceneComponent sc = FrameBase.getSceneComponent();
        JPanel[][] panelMatrix = sc.getPanelMatrix();
        if (compLocation.x < panelMatrix.length && compLocation.y < panelMatrix[0].length) {
            if (panelMatrix[compLocation.x][compLocation.y] != null) {
                if (panelMatrix[compLocation.x][compLocation.y] instanceof WallComponent wc){
                    if (wc.isDeploymentEnabled(component)){
                        Class<?> compClass = component.getClass();
                        try {
                            Constructor<?> constructor = compClass.getConstructor();
                            Method setLocation = compClass.getMethod("setLocation", Point.class, JPanel.class);
                            Method register = compClass.getMethod("register", JLayeredPane.class);
                            Object item = constructor.newInstance();
                            setLocation.invoke(item, compLocation, panelMatrix[compLocation.x][compLocation.y]);
                            register.invoke(item, layerPanel);
                            componentList.remove(component);
                            layerPanel.remove(component);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else if (panelMatrix[compLocation.x][compLocation.y] instanceof RoadComponent rc){
                    if (rc.isDeploymentEnabled(component)){
                        Class<?> compClass = component.getClass();
                        try {
                            Constructor<?> constructor = compClass.getConstructor();
                            Method setLocation = compClass.getMethod("setLocation", Point.class, JPanel.class);
                            Method register = compClass.getMethod("register", JLayeredPane.class);
                            Object item = constructor.newInstance();
                            setLocation.invoke(item, compLocation, panelMatrix[compLocation.x][compLocation.y]);
                            register.invoke(item, layerPanel);
                            componentList.remove(component);
                            layerPanel.remove(component);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public static int getComponentNum() {
        return componentList.size();
    }

    public static int getCompPoint() {
        return compPoint;
    }

    public static void deleteCompPoint(int compPoint) {
        CandidateComponent.compPoint -= compPoint;
    }


    @Override
    public void incident() {
        scheduleValue++;
        if (isChange) {
            for (Component c : this.getComponents()) {
                Element.layerPanel.remove(c);
            }
            int space = 2;
            for (int index = 0; index < componentList.size(); index++) {
                JPanel panel = componentList.get(index);
                Point location = this.getLocation();
                panel.setBounds((index) * (this.getHeight() + space) + location.x, location.y, this.getHeight(), this.getHeight());
                panel.setBackground(new Color(120, 105, 105, 127));
                Element.layerPanel.add(panel, Element.SCENE_LAYER);
            }
            isChange = false;
        }
        if (scheduleValue >= Constant.FRAME_REFRESH_INTERVAL) {
            compPoint++;
            scheduleValue = 0;
        }
    }
}
