package frame.component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static common.FrameConstant.FRAME_REFRESH_INTERVAL;

public class TargetComponent extends JPanel {

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
    private static final ArrayList<TargetComponent> components = new ArrayList<>();

    /**
     * 组件名称
     */
    private String name;

    /**
     * Type
     */
    private int type;

    /**
     * 运动轨迹
     */
    private List<Point> motionTrail = new ArrayList<>();

    /**
     * 注册组件
     */
    public void register() {
        components.add(this);
    }

    public static void initializeGlobalTimer() {
        if (globalTimer == null) {
            globalTimer = new Timer(UPDATE_DELAY, e -> {
                // 统一更新所有组件的位置
                for (TargetComponent component : components) {
                    component.updatePosition();
                }
            });
            globalTimer.start();
        }
    }

    public static int getComponentsCount() {
        return components.size();
    }

    private void updatePosition() {
        // 更新位置逻辑
        Point location = getLocation();
        if (location.y > 1000) {
            setLocation((int) (location.x + Math.random() * 21 - 10), 0);
        } else {
            setLocation((int) (location.x), location.y + 10);
        }
        repaint(); // 可选：重绘以更新视觉效果
    }
}
