package frame.component.interaction.tower;

import common.Element;
import entity.Direction;
import frame.component.interaction.StanderInteractionComponent;
import frame.pipeLine.GlobalInteractionLine;
import frame.pipeLine.GlobalParticleLine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static common.Element.COMPONENT_LAYER;
import static common.FrameConstant.UNIT_SIZE;

public class Lightning extends StanderInteractionComponent {

    /**
     * 组件消耗
     */
    private int cost = 5;

    /**
     * 点位原组件
     */
    private JPanel locPanel;

    public Lightning() {
        super();
        setBackground(new Color(140, 43, 140, 0));
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
        return interactionObjects.isEmpty();
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
        if (!interactionObjects.isEmpty()) {
            List<StanderInteractionComponent> removeList = new ArrayList<>();
            interactionEvent(removeList);
            removeList.forEach(interactionObjects::remove);
        }
    }

    private void interactionEvent(List<StanderInteractionComponent> removeList){
        for (StanderInteractionComponent comp : interactionObjects) {
            // 判断状态
            if (comp.isAliveState()) {
                // 判断是否超出范围
                Point compLocation = comp.getLocationOnScreen();
                if (calcDistance(this.getLocationOnScreen(), compLocation) > atkRange * UNIT_SIZE) {
                    removeList.add(comp);
                } else {
                    if (atkLoad > atkInterval) {
                        int targetValue = comp.getRestEndurance(atkValue - interactionObjects.size() + 1);
                        Point towerLocation = getLocation();
                        Point targetLocation = comp.getLocation();
                        GlobalParticleLine.registerLineParticle(Element.layerPanel, towerLocation, targetLocation, 10);
                        if (targetValue < 0) {
                            removeList.add(comp);
                        }
                        atkLoad = 0;
                    } else {
                        atkLoad += 10;
                    }
                }
            } else {
                removeList.add(comp);
            }
        }
    }
}
