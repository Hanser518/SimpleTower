package frame.component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static common.FrameConstant.FRAME_REFRESH_INTERVAL;

public class ParticleComponent extends JPanel {

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
    private static final ArrayList<ParticleComponent> components = new ArrayList<>();
    private static final ArrayList<ParticleComponent> removeComponents = new ArrayList<>();
    /**
     * Type
     */
    private int type;

    /**
     * Type
     */
    private int speedX;

    /**
     * Type
     */
    private int speedY;

    private int state;

    /**
     * 注册组件
     */
    public void register() {
        this.type = (int) (Math.random() * 4 + 1);
        this.speedX = (int) (Math.random() * 5 + 1);
        this.speedY = (int) (Math.random() * 5 + 1);
        components.add(this);
    }

    public static void initializeGlobalTimer() {
        if (globalTimer == null) {
            globalTimer = new Timer(UPDATE_DELAY, e -> {
                // 统一更新所有组件的位置
                for (ParticleComponent component : components) {
                    component.updatePosition();
                }
                for (ParticleComponent component : removeComponents) {
                    components.remove(component);
                }
                removeComponents.clear();
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
        switch (type) {
            case 1 -> setLocation(location.x - speedX, location.y - speedY);
            case 2 -> setLocation(location.x + speedX, location.y - speedY);
            case 3 -> setLocation(location.x + speedX, location.y + speedY);
            case 4 -> setLocation(location.x - speedX, location.y + speedY);
        }
        if (location.x < 0 || location.x > 3000 || location.y < 0 || location.y > 2000) {
            removeComponents.add(this);
        }
        repaint(); // 可选：重绘以更新视觉效果
    }
}
