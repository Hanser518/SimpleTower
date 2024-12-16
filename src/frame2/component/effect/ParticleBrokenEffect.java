package frame2.component.effect;

import frame2.component.EffectComponent;
import frame2.pipeLine.EffectLine;

import javax.swing.*;
import java.awt.*;

import static frame2.common.FrameConstant.EFFECT_LAYER;

public class ParticleBrokenEffect extends EffectComponent {

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


    public ParticleBrokenEffect(JPanel interaction1) {
        super(interaction1);
        bkColor = interaction1.getBackground();
        setBackground(new Color(bkColor.getRed(), bkColor.getGreen(), bkColor.getBlue(), 191));
    }

    public void register(JLayeredPane container) {
        super.register(container);
        for (int i = 0; i < 10; i++) {
            ParticleBrokenEffect PBE = new ParticleBrokenEffect(interaction1);
            PBE.subRegister(container);
        }
    }

    protected void subRegister(JLayeredPane container) {
        this.container = container;
        Point location = interaction1.getLocation();
        int compWidth = interaction1.getWidth() / 2;
        int compHeight = interaction1.getHeight() / 2;
        setBounds(location.x + compWidth / 2, location.y + compHeight / 2, compWidth, compHeight);
        horizontalValue = (int) (Math.random() * 10 - 4);
        portraitValue = (int) (Math.random() * 10 - 4);
        // System.out.print(horizontalValue + " " + portraitValue + " | ");
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
