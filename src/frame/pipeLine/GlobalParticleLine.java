package frame.pipeLine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 粒子组件-GlobalParticleLine
 */
import static common.FrameConstant.FRAME_REFRESH_INTERVAL;

public class GlobalParticleLine extends JPanel {

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
    private static final ArrayList<GlobalParticleLine> components = new ArrayList<>();
    private static final ArrayList<GlobalParticleLine> removeComponents = new ArrayList<>();

    /**
     * Type
     */
    private int type;

    /**
     * speedX
     */
    private int speedX;

    /**
     * speedY
     */
    private int speedY;

    /**
     * 生存周期
     */
    private Integer liveCycle;

    /**
     * 组件进度
     */
    private int scheduleValue = 0;

    /**
     * 容器
     */
    private static JPanel container;

    public GlobalParticleLine() {
        super(null);
    }

    /**
     * 注册组件
     */
    public void register(JPanel container, Integer liveCycle) {
        this.type = (int) (Math.random() * 4 + 1);
        this.speedX = (int) (Math.random() * 4 + 1);
        this.speedY = (int) (Math.random() * 4 + 1);
        this.liveCycle = liveCycle == null ? 12 : liveCycle;
        GlobalParticleLine.container = container;
        GlobalParticleLine.container.add(this);
        components.add(this);
    }

    public static void createParticle(JPanel container, JPanel component, Integer liveCycle) {
        Point location = component.getLocation();
        for (int i = 0; i < 10; i++) {
            GlobalParticleLine pc = new GlobalParticleLine();
            pc.setBounds(location.x + component.getWidth() / 4, location.y + component.getHeight() / 4, liveCycle / 2, liveCycle / 2);
            pc.setBackground(component.getBackground());
            pc.register(container, liveCycle);
        }
    }

    public static void initializeGlobalTimer() {
        if (globalTimer == null) {
            globalTimer = new Timer(UPDATE_DELAY, e -> {
                // 统一更新所有组件的位置
                for (GlobalParticleLine component : components) {
                    component.updatePosition();
                }
                for (GlobalParticleLine component : removeComponents) {
                    components.remove(component);
                    container.remove(component);
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
        int width = (int) (getWidth() * ((liveCycle - scheduleValue) / (double)liveCycle));
        int height = (int) (getHeight() * ((liveCycle - scheduleValue) / (double)liveCycle));
        scheduleValue++;
        switch (type) {
            case 1 -> setBounds(location.x - speedX, location.y - speedY, width, height);
            case 2 -> setBounds(location.x + speedX, location.y - speedY, width, height);
            case 3 -> setBounds(location.x + speedX, location.y + speedY, width, height);
            case 4 -> setBounds(location.x - speedX, location.y + speedY, width, height);
        }
        if (location.x < -this.getWidth() || location.x > 3000 || location.y < -this.getHeight() || location.y > 2000) {
            removeComponents.add(this);
        }
        if (scheduleValue > liveCycle) {
            removeComponents.add(this);
        }
    }
}
