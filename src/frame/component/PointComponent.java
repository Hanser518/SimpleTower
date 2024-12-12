package frame.component;

import common.Element;
import frame.component.target.TargetComponent;
import frame.pipeLine.GlobalMotionLine;

import javax.swing.*;
import java.awt.*;

import static common.Constant.UNIT_MOVE_COUNT;
import static common.Element.COMPONENT_LAYER;
import static common.Element.layerPanel;
import static common.FrameConstant.UNIT_SIZE;

public class PointComponent extends JPanel implements StanderComponent{

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

    public PointComponent(Point inPoint, Point outPoint){
        inlet = new JPanel(null);
        outlet = new JPanel(null);
        sp = inPoint;
        ep = outPoint;
    }

    public void register(){
        layerPanel.add(inlet, Element.SCENE_LAYER);
        layerPanel.add(outlet, Element.SCENE_LAYER);
        GlobalMotionLine.addToPrepareComponents(this);
    }

    @Override
    public void motion() {
        if (count < interval)
            count++;
        else {
            if (sp != null && ep != null) {
                TargetComponent TC = new TargetComponent();
                TC.setBounds(sp.x * UNIT_SIZE, sp.y * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                TC.setBackground(new Color(147, 44, 44, 255));
                TC.register(layerPanel, sp, ep);
                this.add(TC);
                layerPanel.add(TC, COMPONENT_LAYER);
            }
            count = 0;
        }
    }
}
