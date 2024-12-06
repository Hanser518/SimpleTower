package frame.component;

import entity.Direction;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static common.FrameConstant.FRAME_REFRESH_INTERVAL;

public class TowerComponent extends JPanel {

    /**
     * 类型标识
     */
    public static final Integer TOWER_TYPE_TYPE1 = 1;

    /**
     * 设置全局刷新率
     */
    private static final int UPDATE_DELAY = 1000 / FRAME_REFRESH_INTERVAL;

    /**
     * 设置全局定时器
     */
    private static Timer globalTimer;

    /**
     * 设置组件列表
     */
    public static final ArrayList<TowerComponent> components = new ArrayList<>();

    /**
     * 待移除组件列表
     */
    private static final ArrayList<TowerComponent> removeComponents = new ArrayList<>();

    /**
     * 点位标识
     */
    private Direction towerLocation;

    public Direction getTowerLocation() {
        return towerLocation;
    }

    /**
     * 类型
     */
    private Integer type = null;

    /**
     * 容器
     */
    private static JPanel container = null;

    /**
     * 目标
     */
    private TargetComponent target = null;

    public TargetComponent getTargetComponent() {
        return target;
    }

    public TowerComponent(Integer type) {
        super(null);
        if (type.equals(TOWER_TYPE_TYPE1)) {
            this.type = TOWER_TYPE_TYPE1;
            setBackground(new Color(228, 47, 47));
        } else {
            this.type = 0;
            setBackground(new Color(255, 0, 0, 127));
        }
    }

    /**
     * 注册组件
     */
    public void register(JPanel panel, Point location, int dir) {
        container = panel;
        this.towerLocation = new Direction(location, dir);
        components.add(this);
    }

    public static void initializeGlobalTimer() {
        if (globalTimer == null) {
            globalTimer = new Timer(UPDATE_DELAY, e -> {
                // 统一更新所有组件的位置
                for (TowerComponent component : components) {
                    component.executeTransaction();
                }
                for (TowerComponent component : removeComponents) {
                    components.remove(component);
                    container.remove(component);
                }
                removeComponents.clear();
            });
            globalTimer.start();
        }
    }

    /**
     * 事务（暂定，后面可能会根据type执行不同的事务）
     */
    private void executeTransaction() {
        if (TargetComponent.components.isEmpty()) {
            return;
        }
        if (target != null && !target.isAliveState()) {
            target = null;
        }
        if (target == null) {
            TargetComponent prepare = null;
            int dist = 115411;
            for (TargetComponent tar : TargetComponent.components) {
                if (!tar.isFocus()) {
                    Direction tarLocation = tar.getTargetLocation();
                    int distFromTower = (int) Math.sqrt(Math.pow(tarLocation.x - towerLocation.x, 2) + Math.pow(tarLocation.y - towerLocation.y, 2));
                    prepare = distFromTower < dist ? tar : prepare;
                    dist = Math.min(distFromTower, dist);
                }
            }
            if (prepare != null) {
                prepare.setFocus();
                target = prepare;
            }
        } else {
            container.repaint();
        }
    }
}
