package frame.component.interaction.target;

import common.Element;
import entity.Direction;
import frame.component.interaction.StanderInteractionComponent;
import frame.pipeLine.GlobalInteractionLine;
import frame.pipeLine.GlobalParticleLine;
import method.way.BuildSolution;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static common.Constant.UNIT_MOVE_COUNT;
import static common.Element.COMPONENT_LAYER;
import static common.FrameConstant.UNIT_SIZE;

public class GroundUnit extends StanderInteractionComponent {

    /**
     * 每单位移动量
     */
    private int[] motionValues;

    /**
     * 移动次数
     */
    private int motionCount = 0;

    /**
     * 移动路径
     */
    private List<Direction> motionPath = new ArrayList<>();

    public GroundUnit() {
        super();
        atkRange = 1;
        atkValue = 2;
        atkLoad = atkInterval - 1;
        setBackground(new Color(34, 34, 34, 0));

    }

    public void setPath(Point start, Point end) {
        // 计算路径、初始化点位
        motionPath = BuildSolution.getMapWay(mapMatrix, start, end);
        compLocation = motionPath.get(0);
        // 获取帧位移量
        motionValues = new int[UNIT_MOVE_COUNT];
        int frameMotionValue = UNIT_SIZE / UNIT_MOVE_COUNT;
        Arrays.fill(motionValues, frameMotionValue);
        int interval = UNIT_SIZE - frameMotionValue * UNIT_MOVE_COUNT;
        if (interval > 0) {
            int split = UNIT_MOVE_COUNT / interval;
            for (int i = 1; i < UNIT_MOVE_COUNT; i += split) {
                motionValues[i] += 1;
            }
        }
    }

    @Override
    public void register(JLayeredPane container) {
        this.container = container;
        this.container.add(this, COMPONENT_LAYER);
        GlobalInteractionLine.addToPrepareTarget(this);
    }


    @Override
    protected boolean isSearchingForInteraction() {
        return interactionObjects.isEmpty();
    }

    @Override
    protected void searchInteraction() {
        StanderInteractionComponent component = null;
        int compDistance = 16384;
        for (StanderInteractionComponent comp : GlobalInteractionLine.tower) {
            int distance = calcDistance(getCompLocation(), comp.getCompLocation());
            if (distance < 1 && distance < compDistance) {
                component = comp;
                compDistance = distance;
            }
        }
        if (component != null) {
            interactionObjects.add(component);
        }
    }

    @Override
    protected void triggerInteractionEvent() {
        if (working) {
            motion();
        }
        if (!interactionObjects.isEmpty()) {
            atk();
        }
        // 检测
        if (endurance < 0) {
            aliveState = false;
            container.remove(this);
            GlobalInteractionLine.addToRemoveTarget(this);
            GlobalParticleLine.registerBrokenParticle(container, this, 10);
        }
    }

    private void motion() {
        // 获取当前坐标
        Point location = getLocation();
        // 获取移动方向
        int dir = compLocation.direction;
        switch (dir) {
            case 0, 2 -> {
                int dist = (dir == 0 ? -1 : 1) * motionValues[motionCount];
                this.setLocation(location.x, location.y + dist);
            }
            case 1, 3 -> {
                int dist = (dir == 3 ? -1 : 1) * motionValues[motionCount];
                this.setLocation(location.x + dist, location.y);
            }

        }
        // 计算移动步骤
        motionCount += 1;
        if (motionCount == UNIT_MOVE_COUNT) {
            motionCount = 0;
            int index = motionPath.indexOf(this.compLocation);
            if (index == motionPath.size() - 1) {
                GlobalInteractionLine.addToRemoveTarget(this);
                aliveState = false;
            } else {
                this.compLocation = motionPath.get(index + 1);
            }
        }
    }

    private void atk() {
        List<StanderInteractionComponent> removeList = new ArrayList<>();
        for (StanderInteractionComponent comp : interactionObjects) {
            // 判断状态
            if (comp.isAliveState()) {
                // 判断是否超出范围
                Point compLocation = comp.getLocationOnScreen();
                if (calcDistance(this.getLocationOnScreen(), compLocation) > atkRange * UNIT_SIZE) {
                    // System.out.println(calcDistance(this.getCompLocation(), compLocation));
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
        removeList.forEach(interactionObjects::remove);
    }
}
