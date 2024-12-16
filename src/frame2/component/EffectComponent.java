package frame2.component;

import javax.swing.*;
import java.awt.*;

public abstract class EffectComponent extends JPanel {

    /**
     * 交互对象1
     */
    protected JPanel interaction1;

    /**
     * 交互对象2
     */
    protected JPanel interaction2;

    /**
     * 容器
     */
    protected JLayeredPane container;

    /**
     * 单对象初始化
     * @param interaction1
     */
    public EffectComponent(JPanel interaction1) {
        super(null);
        this.interaction1 = interaction1;
    }

    /**
     * 双对象初始化
     * @param interaction1
     * @param interaction2
     */
    public EffectComponent(JPanel interaction1, JPanel interaction2) {
        super(null);
        this.interaction1 = interaction1;
        this.interaction2 = interaction2;
    }

    /**
     * 注册<br>
     * 仅初始化容器，不会进行管线注册
     */
    public void register(JLayeredPane container){
        this.container = container;
    }

    /**
     * 事件
     */
    public abstract void incident();

    /**
     * 获取容器
     */
    public JLayeredPane getContainer() {
        return container;
    }

    /**
     * 重置组件外形
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D graphics = (Graphics2D) g;
        setContent(graphics, getWidth() - 1, getHeight() - 1);
    }

    protected abstract void setContent(Graphics2D g, int width, int height);

}
