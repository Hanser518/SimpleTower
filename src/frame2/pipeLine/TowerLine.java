package frame2.pipeLine;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static frame2.common.LineConstant.FRAME_UPDATE_FREQUENCY;

public class TowerLine {
    /**
     * 设置全局刷新率
     */
    private static int UPDATE_DELAY = 1000 / FRAME_UPDATE_FREQUENCY;

    /**
     * 设置全局定时器
     */
    private static Timer GLOBAL_TIMER;

    /**
     * 执行次数
     */
    private static long COUNT = 0;

    /**
     * 设置组件列表
     */
    public static volatile ArrayList<JPanel> components = new ArrayList<>();

    /**
     * 待载入组件列表
     */
    public static volatile ArrayList<JPanel> prepareComponents = new ArrayList<>();

    /**
     * 待移除组件列表
     */
    public static volatile ArrayList<JPanel> removeComponents = new ArrayList<>();

    public static void initializeGlobalTimer() {
        if (GLOBAL_TIMER == null) {
            Thread lineThread = new Thread(() -> {
                GLOBAL_TIMER = new Timer(UPDATE_DELAY, actionEvent -> {
                    motion();
                    COUNT++;
                });
                GLOBAL_TIMER.start();
            });
            lineThread.start();
        }
    }

    public static void motion() {
        // 统一更新所有组件的位置
        for (JPanel component : components) {
            try {
                Method componentMotion = component.getClass().getMethod("incident");
                componentMotion.invoke(component);
            } catch (Exception e) {
                System.out.println(COUNT + "_TOWER-ERROR: " + e);
            }
        }
        for (JPanel component : removeComponents) {
            try {
                Method getContainer = component.getClass().getMethod("getContainer");
                JLayeredPane container = (JLayeredPane) getContainer.invoke(component);
                container.remove(component);
            } catch (Exception e) {
                System.out.println(COUNT + "_TOWER-ERROR: " + e);
            }
            components.remove(component);
        }
        components.addAll(prepareComponents);
        removeComponents.clear();
        prepareComponents.clear();
    }

    public static void addToPrepareComponents(JPanel component) {
        prepareComponents.add(component);
    }

    public static void addToRemoveComponents(JPanel component) {
        removeComponents.add(component);
    }

    public static List<JPanel> getComponents() {
        return components;
    }

    public static int getComponentsCount() {
        return components.size();
    }

    public static void pauseTimer() {
        GLOBAL_TIMER.stop();
    }
}
