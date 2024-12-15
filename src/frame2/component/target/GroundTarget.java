package frame2.component.target;

import frame2.component.TargetComponent;

import java.awt.*;

import static frame2.common.ComponentConstant.UNIT_WIDTH;

public class GroundTarget extends TargetComponent {

    public GroundTarget() {
        super();
        setBackground(new Color(114, 57, 63, 0));
        setBounds(0, 0, UNIT_WIDTH, UNIT_WIDTH);
    }

    @Override
    protected void setContent(Graphics g, int width, int height) {
        width -= 1;
        height -= 1;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(97, 43, 47));
        g2d.fillRoundRect(0, 0, width, height, width / 2, height / 2);
        g2d.setColor(new Color(207, 173, 173));

        for (int i = 1; i <= 2; i++) {
            int size = width / 5;
            g2d.drawOval(size * i, size * i, width - size * i * 2, height - size * i * 2);
        }
    }
}
