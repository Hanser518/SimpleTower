package frame2.pipeLine;

import entity.Direction;
import frame2.component.TargetComponent;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static frame2.common.LineConstant.FRAME_UPDATE_FREQUENCY;

public class TargetLine {
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
    public static volatile ArrayList<TargetComponent> components = new ArrayList<>();

    /**
     * 待载入组件列表
     */
    public static volatile ArrayList<TargetComponent> prepareComponents = new ArrayList<>();

    /**
     * 待移除组件列表
     */
    public static volatile ArrayList<TargetComponent> removeComponents = new ArrayList<>();

    /**
     * 组件位图
     */
    public static volatile Map<Direction, TargetComponent> componentsMap = new HashMap<>();

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
                System.out.println(COUNT + "_TARGET-ERROR: " + e);
            }
        }
        for (JPanel component : removeComponents) {
            try {
                Method getContainer = component.getClass().getMethod("getContainer");
                JLayeredPane container = (JLayeredPane) getContainer.invoke(component);
                container.remove(component);
            } catch (Exception e) {
                System.out.println(COUNT + "_TARGET-ERROR: " + e);
            }
            components.remove(component);
        }
        components.addAll(prepareComponents);
        removeComponents.clear();
        prepareComponents.clear();
        componentsMap = components.stream()
                .collect(Collectors.toMap(TargetComponent::getCompDirection, Function.identity(), (item1, item2) -> item1));
    }

    public static void addToPrepareComponents(TargetComponent component) {
        prepareComponents.add(component);
    }

    public static void addToRemoveComponents(TargetComponent component) {
        removeComponents.add(component);
    }

    public static List<TargetComponent> getComponents() {
        return components;
    }

    public static int getComponentsCount() {
        return components.size();
    }

    public static void pauseTimer() {
        GLOBAL_TIMER.stop();
    }

    public static Map<Direction, TargetComponent> getTargetComponentMap() {
        return componentsMap;
    }
}
