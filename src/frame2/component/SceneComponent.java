package frame2.component;

import frame2.pipeLine.SceneLine;

import javax.swing.*;
import java.awt.*;

import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;
import static frame2.common.FrameConstant.SCENE_LAYER;

public abstract class SceneComponent extends JPanel {

    /**
     * 容器
     */
    protected JLayeredPane container;

    public SceneComponent(){
        super(null);
        setBackground(Color.WHITE);
    }

    /**
     * 注册组件
     */
    public void register(JLayeredPane container){
        this.container = container;
        container.add(this, SCENE_LAYER);
        SceneLine.addToPrepareComponents(this);
    }

    /**
     * 标准事件
     */
    public abstract void incident();

    /**
     * 获取组件容器
     * @return
     */
    public JLayeredPane getContainer(){
        return container;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setContent(g, getWidth(), getHeight());
    }

    protected abstract void setContent(Graphics g, int width, int height);

}
