package frame2.component.effect;

import frame2.component.EffectComponent;
import frame2.pipeLine.EffectLine;

import javax.swing.*;
import java.awt.*;

import static common.FrameConstant.UNIT_SIZE;
import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;
import static frame2.common.FrameConstant.EFFECT_LAYER;

public class ParticleLightingEffect extends EffectComponent {

    /**
     * 水平移动速度
     */
    private int horizontalValue = 0;

    /**
     * 纵向移动速度
     */
    private int portraitValue = 0;

    /**
     * 生存周期
     */
    private int liveCycle = 10;

    /**
     * 组件进度
     */
    private int scheduleValue = 0;

    /**
     * Color
     */
    private Color bkColor = null;

    public ParticleLightingEffect(JPanel interaction1, JPanel interaction2) {
        super(interaction1, interaction2);
        bkColor = interaction1.getBackground();
        setBackground(new Color(bkColor.getRed(), bkColor.getGreen(), bkColor.getBlue(), 191));
    }

    public void register(JLayeredPane container) {
        super.register(container);
        int distance = (int) Math.sqrt(Math.pow(interaction1.getX() - interaction2.getX(), 2) + Math.pow(interaction1.getY() - interaction2.getY(), 2));
        int count = Math.min(24, distance / 24 + 1);
        int moveX = (interaction1.getX() - interaction2.getX()) / count;
        int moveY = (interaction1.getY() - interaction2.getY()) / count;
        for (int i = 0; i < count; i++) {
            ParticleLightingEffect PLE = new ParticleLightingEffect(interaction1, interaction2);
            PLE.subRegister(container, interaction1.getX() - moveX * i + UNIT_WIDTH / 4, interaction1.getY() - moveY * i + UNIT_HEIGHT / 4);
        }
    }

    protected void subRegister(JLayeredPane container, int x, int y) {
        this.container = container;
        int compWidth = interaction1.getWidth() / 4;
        int compHeight = interaction1.getHeight() / 4;
        setBounds(x, y, compWidth, compHeight);
        horizontalValue = (int) (Math.random() * 11 - 5);
        portraitValue = (int) (Math.random() * 11 - 5);
        EffectLine.addToPrepareComponents(this);
        container.add(this, EFFECT_LAYER);
    }

    @Override
    public void incident() {
        scheduleValue++;
        Point location = getLocation();
        int width = (int) (getWidth() * ((liveCycle - scheduleValue) / (double) liveCycle));
        int height = (int) (getHeight() * ((liveCycle - scheduleValue) / (double) liveCycle));
        setBounds(location.x + horizontalValue, location.y + portraitValue, width, height);
        if (location.x < -this.getWidth() || location.x > 3000 || location.y < -this.getHeight() || location.y > 2000) {
            EffectLine.addToRemoveComponents(this);
        }
        if (scheduleValue > liveCycle) {
            EffectLine.addToRemoveComponents(this);
        }
        if (this.getWidth() < 2 && this.getHeight() < 2) {
            EffectLine.addToRemoveComponents(this);
        }
    }

    @Override
    protected void setContent(Graphics2D g, int width, int height) {
        g.setColor(new Color(255 - bkColor.getRed(), 255 - bkColor.getGreen(), 255 - bkColor.getBlue(), 191));
        g.drawRect(0, 0, width, height);
    }
}
