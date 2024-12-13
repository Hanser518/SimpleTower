package frame.pipeLine;

import frame.component.interaction.StanderInteractionComponent;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static common.Constant.FRAME_REFRESH_INTERVAL;

public class GlobalInteractionLine {
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
    public static volatile ArrayList<StanderInteractionComponent> tower = new ArrayList<>();

    /**
     * 待载入组件列表
     */
    public static volatile ArrayList<StanderInteractionComponent> prepareTower = new ArrayList<>();

    /**
     * 待移除组件列表
     */
    public static volatile ArrayList<StanderInteractionComponent> removeTower = new ArrayList<>();



    /**
     * 设置组件列表
     */
    public static volatile ArrayList<StanderInteractionComponent> target = new ArrayList<>();

    /**
     * 待载入组件列表
     */
    public static volatile ArrayList<StanderInteractionComponent> prepareTarget = new ArrayList<>();

    /**
     * 待移除组件列表
     */
    public static volatile ArrayList<StanderInteractionComponent> removeTarget = new ArrayList<>();


    public static void initializeGlobalTimer() {
        Thread motionThread = new Thread(() -> {
            if (GLOBAL_TIMER == null) {
                GLOBAL_TIMER = new Timer(UPDATE_DELAY, ac -> {
                    updateTower();
                    updateTarget();
                });
                GLOBAL_TIMER.start();
            }
        });
        motionThread.start();
    }

    private static void updateTower() {
        // 统一更新所有组件的位置
        COUNT ++;
        if (tower != null) {
            for (StanderInteractionComponent component : tower) {
                try {
                    Method componentMotion = component.getClass().getMethod("interaction");
                    componentMotion.invoke(component);
                } catch (Exception e) {
                    System.out.println(COUNT + "_TOWER-INTERACTION_ERROR: " + e);
                }
            }
            for (StanderInteractionComponent component : removeTower) {
                try {
                    Method getContainer = component.getClass().getMethod("getContainer");
                    JLayeredPane container = (JLayeredPane) getContainer.invoke(component);
                    container.remove(component);
                } catch (Exception e) {
                    System.out.println(COUNT + "_TOWER-INTERACTION_ERROR: " + e);
                }
                tower.remove(component);
            }
            tower.addAll(prepareTower);
            removeTower.clear();
            prepareTower.clear();
        }
    }

    private static void updateTarget(){
        // 统一更新所有组件的位置
        COUNT ++;
        if (target != null) {
            for (StanderInteractionComponent component : target) {
                try {
                    Method componentMotion = component.getClass().getMethod("interaction");
                    componentMotion.invoke(component);
                } catch (Exception e) {
                    System.out.println(COUNT + "_TARGET-INTERACTION_ERROR: " + e);
                }
            }
            for (StanderInteractionComponent component : removeTarget) {
                try {
                    Method getContainer = component.getClass().getMethod("getContainer");
                    JLayeredPane container = (JLayeredPane) getContainer.invoke(component);
                    container.remove(component);
                } catch (Exception e) {
                    System.out.println(COUNT + "_TARGET-INTERACTION_ERROR: " + e);
                }
                target.remove(component);
            }
            target.addAll(prepareTarget);
            removeTarget.clear();
            prepareTarget.clear();
        }
    }

    public static void addToPrepareTower(StanderInteractionComponent component) {
        prepareTower.add(component);
    }

    public static void addToPrepareTarget(StanderInteractionComponent component) {
        prepareTarget.add(component);
    }

    public static void addToRemoveTower(StanderInteractionComponent component) {
        removeTower.add(component);
    }

    public static void addToRemoveTarget(StanderInteractionComponent component) {
        removeTarget.add(component);
    }
}
