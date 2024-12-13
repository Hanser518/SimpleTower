package frame.component.interaction.discard;

import common.Element;
import entity.Direction;
import frame.component.incident.StanderIncidentComponent;
import frame.pipeLine.GlobalIncidentLine;
import frame.pipeLine.GlobalParticleLine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static common.Constant.FRAME_REFRESH_INTERVAL;
import static common.FrameConstant.UNIT_SIZE;
import static frame.pipeLine.GlobalIncidentLine.components;
import static frame.pipeLine.GlobalIncidentLine.removeComponents;

public class LandComponent extends JPanel implements StanderIncidentComponent {
    /**
     * 容器
     */
    private static JLayeredPane container = null;

    /**
     * 点位标识
     */
    private Direction towerLocation;

    /**
     * 目标
     */
    private TargetComponent target = null;

    /**
     * atk value
     */
    private final Integer atkValue = 5;

    /**
     * atk load
     */
    private int atkLoad = 15;

    /**
     * atk interval
     */
    private final Integer atkInterval = FRAME_REFRESH_INTERVAL / 3;

    /**
     * ???
     */
    private JPanel locPanel;

    /**
     * atk Range
     */
    private final Integer atkRange = 1;

    /**
     *
     */
    private int durationAbility = 10;

    public LandComponent() {
        super(null);
        setBackground(new Color(189, 199, 191, 0));
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                int wSize = UNIT_SIZE / 8;
                int hSize = UNIT_SIZE / 8;
                Graphics2D g2d = (Graphics2D) g;

                int alpha = (int) (255 * ((double) durationAbility / 20));
                g2d.setColor(new Color(155, 155, 155, alpha));
                g2d.drawRect(x + 1, y + 1, width - 3, height - 3);
                for (int i = 1; i < 8; i += 2) {
                    for (int j = 1; j < 8; j += 2) {
                        if (i == 1 || j == 1 || i == 7 || j == 7) {
                            g2d.setColor(new Color(123, 145, 99, alpha));
                            g2d.fillRect(wSize * j, hSize * i, wSize, hSize);

                            g2d.setColor(new Color(57, 69, 87));
                            g2d.drawRect(wSize * j, hSize * i, wSize, hSize);
                        }
                    }
                }
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
    }


    public void register(JLayeredPane panel, Point location, JPanel locPanel) {
        container = panel;
        this.towerLocation = new Direction(location, -1);
        this.locPanel = locPanel;
        GlobalParticleLine.registerBrokenParticle(container, this, this.getWidth());
        GlobalIncidentLine.addToPrepareComponents(this);
    }

    @Override
    public void incident() {
        if (components.isEmpty()) {
            return;
        }
        if (target == null) {
            TargetComponent prepare = null;
            int dist = 115411;
            for (JPanel rawComponent : components) {
                if (rawComponent instanceof TargetComponent tar) {
                    Direction tarLocation = tar.getTargetLocation();
                    int distFromTower = getDistance(tarLocation, towerLocation);
                    if (distFromTower > atkRange) {
                        continue;
                    }
                    prepare = distFromTower < dist ? tar : prepare;
                    dist = Math.min(distFromTower, dist);
                }
            }
            if (prepare != null) {
                target = prepare;
            }
        } else {
            if (!target.isAliveState()) {
                target = null;
            } else {
                Direction tarLocation = target.getTargetLocation();
                int distFromTower = getDistance(tarLocation, towerLocation);
                if (distFromTower > atkRange) {
                    target = null;
                } else {
                    Point location = target.getLocation();
                    int distance = getDistance(location, getLocation());
                    if (distance < UNIT_SIZE && durationAbility > 0) {
                        target.setResist(this);
                    } else {
                        target.cancelResist();
                    }
                    if (atkLoad > atkInterval) {
                        int targetValue = target.getRestValue(distFromTower <= 1 ? atkValue : atkValue / 2);
                        Point towerLocation = getLocation();
                        Point targetLocation = target.getLocation();
                        GlobalParticleLine.registerLineParticle(Element.layerPanel, towerLocation, targetLocation, 10);
                        if (targetValue < 0) {
                            target = null;
                        }
                        atkLoad = 0;
                    } else {
                        atkLoad++;
                    }
                }
            }
        }
    }

    public int getRestValue(int damage) {
        durationAbility -= damage;
        if (durationAbility < 0) {
            removeComponents.add(this);
            GlobalParticleLine.registerBrokenParticle(container, this, getWidth() / 2);
        }
        return durationAbility;
    }

    private int getDistance(Point p1, Point p2) {
        return (int) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }

    public int getCost() {
        return 3;
    }
}
