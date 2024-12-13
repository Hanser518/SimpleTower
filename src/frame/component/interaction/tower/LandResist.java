package frame.component.interaction.tower;

import common.Element;
import entity.Direction;
import frame.component.interaction.StanderInteractionComponent;
import frame.component.status.abnormal.Resist;
import frame.pipeLine.GlobalInteractionLine;
import frame.pipeLine.GlobalParticleLine;

import javax.swing.*;
import java.awt.*;

import static common.Element.COMPONENT_LAYER;
import static common.FrameConstant.UNIT_SIZE;

public class LandResist extends StanderInteractionComponent {

    /**
     * 组件消耗
     */
    private int cost = 3;

    /**
     * 点位原组件
     */
    private JPanel locPanel;

    public LandResist() {
        super();
        atkRange = 3;
        atkValue = 6;
        setBackground(new Color(16, 107, 140, 0));
    }

    public void setLocation(Point location, JPanel locPanel) {
        this.compLocation = new Direction(location, -1);
        setLocation(location.x * UNIT_SIZE, location.y * UNIT_SIZE);
        this.locPanel = locPanel;
    }

    public int getCost() {
        return cost;
    }

    @Override
    public void register(JLayeredPane container) {
        this.container = container;
        container.add(this, COMPONENT_LAYER);
        GlobalInteractionLine.addToPrepareTower(this);
        GlobalParticleLine.registerBrokenParticle(container, this, this.getWidth());
    }

    @Override
    protected boolean isSearchingForInteraction() {
        return true;
    }

    @Override
    protected void searchInteraction() {
        StanderInteractionComponent component = null;
        int compDistance = 16384;
        for (StanderInteractionComponent comp : GlobalInteractionLine.target) {
            if (!interactionObjects.contains(comp)) {
                int distance = calcDistance(getCompLocation(), comp.getCompLocation());
                if (distance < atkRange && distance < compDistance) {
                    component = comp;
                    compDistance = distance;
                }
            }
        }
        if (component != null) {
            interactionObjects.add(component);
        }
    }

    @Override
    protected void triggerInteractionEvent() {
        // 检测
        if (endurance < 0) {
            aliveState = false;
            container.remove(this);
            GlobalInteractionLine.addToRemoveTower(this);
            GlobalParticleLine.registerBrokenParticle(container, this, 10);
        }
        if (!interactionObjects.isEmpty()) {
            for (StanderInteractionComponent comp : interactionObjects) {
                // 判断状态
                if (comp.isAliveState()) {
                    Resist resist = new Resist(this, comp);
                    comp.addToStatus(resist);
                    // 判断是否超出范围
                    Point compLocation = comp.getLocationOnScreen();
                    if (calcDistance(this.getLocationOnScreen(), compLocation) > atkRange * UNIT_SIZE) {
                        interactionObjects.remove(comp);
                    } else {
                        if (atkLoad > atkInterval) {
                            int targetValue = comp.getRestEndurance(atkValue - interactionObjects.size() + 1);
                            GlobalParticleLine.registerBrokenParticle(Element.layerPanel, comp, 10);
                            if (targetValue < 0) {
                                interactionObjects.remove(comp);
                            }
                            atkLoad = 0;
                        } else {
                            atkLoad += 10;
                        }
                    }
                } else {
                    interactionObjects.remove(comp);
                }
            }
        }
    }
}
