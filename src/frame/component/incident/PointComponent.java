package frame.component.incident;

import common.Element;
import frame.component.interaction.target.GroundUnitBaseStander;
import frame.component.interaction.target.TargetComponent;
import frame.pipeLine.GlobalIncidentLine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static common.Constant.UNIT_MOVE_COUNT;
import static common.Element.COMPONENT_LAYER;
import static common.Element.layerPanel;
import static common.FrameConstant.UNIT_SIZE;

public class PointComponent extends JPanel implements StanderIncidentComponent {

    private final int interval = UNIT_MOVE_COUNT * 2;

    private int count = 0;

    /**
     * 入口
     */
    private JPanel inlet;

    private Point sp;

    /**
     * 出口
     */
    private JPanel outlet;

    private Point ep;

    public PointComponent(Point inPoint, Point outPoint) {
        inlet = new JPanel(null);
        inlet.setBounds(inPoint.x * UNIT_SIZE, inPoint.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
        inlet.setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                width -= 1;
                height -= 1;
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(67, 29, 37));
                g2d.drawRoundRect(0, 0, width, height, width / 2, height / 2);
                g2d.drawOval(width / 8, height / 8, width - width / 8 * 2, height - height / 8 * 2);
                g2d.drawOval(width / 8 * 2, height / 8 * 2, width - width / 8 * 4, height - height / 8 * 4);
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
        outlet = new JPanel(null);
        sp = inPoint;
        ep = outPoint;
    }

    public void register() {
        layerPanel.add(inlet, Element.SCENE_LAYER);
        layerPanel.add(outlet, Element.SCENE_LAYER);
        GlobalIncidentLine.addToPrepareComponents(this);
    }

    @Override
    public void incident() {
        if (count < interval)
            count++;
        else {
            if (sp != null && ep != null) {
                TargetComponent TC = new TargetComponent();
                TC.setBounds(sp.x * UNIT_SIZE, sp.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                TC.setBackground(new Color(147, 44, 44, 255));
                TC.register(layerPanel, sp, ep);
                layerPanel.add(TC, COMPONENT_LAYER);

                GroundUnitBaseStander GB = new GroundUnitBaseStander();
                GB.setPath(sp, ep);
                GB.setLocation(sp.x * UNIT_SIZE, sp.y * UNIT_SIZE);
                GB.register(layerPanel);
            }
            count = 0;
        }
    }
}
