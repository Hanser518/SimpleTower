package frame2.component.scene;

import frame2.component.SceneComponent;

import java.awt.*;

import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;

public class PlatformComponent extends SceneComponent {

    public PlatformComponent() {
        super();
        setBackground(new Color(71, 74, 84));
        setBounds(0, 0, UNIT_WIDTH, UNIT_HEIGHT);
    }

    @Override
    public void incident() {

    }

    @Override
    protected void setContent(Graphics g, int width, int height) {
        width -= 1;
        height -= 1;
        Graphics2D g2d = (Graphics2D) g;
        int steps = Math.min(width / 5, height / 5);
        g2d.setColor(new Color(166, 170, 181));
        for(int i = 0;i < 3;i ++){
            g2d.drawRect(i * steps, i * steps, width - 2 * i * steps, height - 2 * i * steps);
        }
    }
}
