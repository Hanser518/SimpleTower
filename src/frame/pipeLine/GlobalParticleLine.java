package frame.pipeLine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/**
 * 粒子组件-GlobalParticleLine
 */
import static common.Element.PARTICLE_LAYER;
import static common.FrameConstant.FRAME_REFRESH_INTERVAL;
import static common.FrameConstant.UNIT_SIZE;

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
    private static JLayeredPane container;

    public GlobalParticleLine() {
        super(null);
        this.setDoubleBuffered(true);
    }

    /**
     * 注册组件
     */

    public void register(JLayeredPane container, Integer liveCycle) {
        this.type = (int) (Math.random() * 4 + 1);
        this.speedX = (int) (Math.random() * 4 + 1);
        this.speedY = (int) (Math.random() * 4 + 1);
        this.liveCycle = liveCycle == null ? 12 : liveCycle;
        GlobalParticleLine.container = container;
        GlobalParticleLine.container.add(this, PARTICLE_LAYER);
        this.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Color compColor = c.getBackground();
                int red = 255 - compColor.getRed();
                int green = 255 - compColor.getGreen();
                int blue = 255 - compColor.getBlue();
                g.setColor(new Color(red, green, blue, 127));
                g.drawRect(x, y, width - 1, height - 1);
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
        components.add(this);
    }

    public void register2(JLayeredPane container, Integer liveCycle, Integer type) {
        this.type = type == null ? 5 : type;
        this.liveCycle = liveCycle == null ? 12 : liveCycle;
        this.speedX = (int) (Math.random() * 11 - 5);
        this.speedY = (int) (Math.random() * 11 - 5);
        GlobalParticleLine.container = container;
        GlobalParticleLine.container.add(this, PARTICLE_LAYER);
        this.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(new Color(24, 18, 133, 127));
                g.drawRect(x, y, width - 1, height - 1);
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
        components.add(this);
    }

    public static void registerBrokenParticle(JLayeredPane container, JPanel component, Integer liveCycle) {
        Point location = component.getLocation();
        for (int i = 0; i < 10; i++) {
            GlobalParticleLine pc = new GlobalParticleLine();
            pc.setBounds(location.x + component.getWidth() / 4, location.y + component.getHeight() / 4, liveCycle / 2, liveCycle / 2);
            Color componentColor = component.getBackground();
            pc.setBackground(new Color(componentColor.getRed(), componentColor.getGreen(), componentColor.getBlue(), 255));
            pc.register(container, liveCycle);
        }
    }

    public static void registerLineParticle(JLayeredPane container, Point start, Point end, Integer liveCycle) {
        int distance = (int) Math.sqrt(Math.pow(start.getX() - end.getX(), 2) + Math.pow(start.getY() - end.getY(), 2));
        int count = distance / 18 + 1;
        int moveX = (int) ((start.getX() - end.getX()) / count);
        int moveY = (int) ((start.getY() - end.getY()) / count);
        for (int i = 0; i < count; i++) {
            GlobalParticleLine pc = new GlobalParticleLine();
            pc.setBounds(start.x - moveX * i + UNIT_SIZE / 2, start.y - moveY * i + UNIT_SIZE / 2, 12, 12);
            Color componentColor = new Color(99, 61, 195, 153);
            pc.setBackground(componentColor);
            pc.register2(container, liveCycle, 5);
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
        scheduleValue++;
        if (type < 5) {
            int width = (int) (getWidth() * ((liveCycle - scheduleValue) / (double) liveCycle));
            int height = (int) (getHeight() * ((liveCycle - scheduleValue) / (double) liveCycle));
            switch (type) {
                case 1 -> setBounds(location.x - speedX, location.y - speedY, width, height);
                case 2 -> setBounds(location.x + speedX, location.y - speedY, width, height);
                case 3 -> setBounds(location.x + speedX, location.y + speedY, width, height);
                case 4 -> setBounds(location.x - speedX, location.y + speedY, width, height);
            }
        } else if (type == 5) {
            int width = (int) (getWidth() * ((liveCycle - scheduleValue) / (double) liveCycle));
            int height = (int) (getHeight() * ((liveCycle - scheduleValue) / (double) liveCycle));
            setBounds(location.x + speedX, location.y + speedY, width, height);
        }
        if (location.x < -this.getWidth() || location.x > 3000 || location.y < -this.getHeight() || location.y > 2000) {
            removeComponents.add(this);
        }
        if (scheduleValue > liveCycle) {
            removeComponents.add(this);
        }
    }
}
