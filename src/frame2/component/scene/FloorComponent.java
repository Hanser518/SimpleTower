package frame2.component.scene;

import frame2.component.SceneComponent;

import java.awt.*;

import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;

public class FloorComponent extends SceneComponent {

    public FloorComponent() {
        super();
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int steps = Math.min(width / 5, height / 5);
        g2d.setColor(new Color(0xA1A1A2));
        for(int i = 0;i < 3;i ++){
            g2d.drawRect(i * steps, i * steps, width - 2 * i * steps, height - 2 * i * steps);
        }
        g2d.setColor(new Color(0xC7C7C7));
        g2d.drawLine(0, 0, width, height);
        g2d.drawLine(width, 0, 0, height);
    }
}
