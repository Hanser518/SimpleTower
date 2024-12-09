package frame.pipeLine;

import javax.swing.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static common.FrameConstant.FRAME_REFRESH_INTERVAL;

public class GlobalMotionLine {
    /**
     * 设置全局刷新率
     */
    private static final int UPDATE_DELAY = 1000 / FRAME_REFRESH_INTERVAL;

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
            GLOBAL_TIMER = new Timer(UPDATE_DELAY, actionEvent -> {
                COUNT++;
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
                        JPanel container = (JPanel) getContainer.invoke(component);
                        container.remove(component);
                    } catch (Exception e) {
                        System.out.println(COUNT + "-ERROR: " + e.getMessage());
                    }
                    components.remove(component);
                }
                components.addAll(prepareComponents);
                removeComponents.clear();
                prepareComponents.clear();
                System.out.println(COUNT + ": GLOBAL TIMER");
            });
            GLOBAL_TIMER.start();
        }
    }

    public static void addToPrepareComponents(JPanel component) {
        prepareComponents.add(component);
    }

    public static void addToComponents(JPanel component) {
        components.add(component);
    }
}
