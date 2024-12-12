package frame.pipeLine;

import javax.swing.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static common.Constant.FRAME_REFRESH_INTERVAL;

public class GlobalMotionLine {
    /**
     * 设置全局刷新率
     */
    private static int UPDATE_DELAY = 1000 / FRAME_REFRESH_INTERVAL;

    /**
     * 设置全局定时器
     */
    private static Timer GLOBAL_TIMER;

    /**
     * 执行次数
     */
    private static int COUNT = 0;

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
                Method componentMotion = component.getClass().getDeclaredMethod("motion");
                componentMotion.invoke(component);
            } catch (Exception e) {
                System.out.println(COUNT + "-ERROR: " + e.getMessage());
            }
        }
        for (JPanel component : removeComponents) {
            try {
                Method getContainer = component.getClass().getDeclaredMethod("getContainer");
                JLayeredPane container = (JLayeredPane) getContainer.invoke(component);
                container.remove(component);
            } catch (Exception e) {
                System.out.println(COUNT + "-ERROR: " + e.getMessage());
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

    public static void addToComponents(JPanel component) {
        components.add(component);
    }

    public static int getComponentsCount() {
        return components.size();
    }

    public static void pauseTimer() {
        GLOBAL_TIMER.stop();
    }

    public static void continueTimer() {
        GLOBAL_TIMER.start();
    }

    public static void slowTimer(){
        GLOBAL_TIMER.setDelay(UPDATE_DELAY * 4);
    }
}
