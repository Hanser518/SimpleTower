package frame.component.scene;

import frame.component.interaction.tower.Lightning;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static common.FrameConstant.*;

public class WallComponent extends JPanel {

    public static final Integer WALL_STYLE0 = 0;

    public static final Integer WALL_STYLE1 = 1;

    public static final Integer WALL_STYLE2 = 2;

    /**
     * Wall类型
     */
    private Integer type;

    public WallComponent(Integer type) {
        super(null);
        if (type == null) {
            setDefaultStyle();
        } else {
            this.type = type;
            setStyle();
        }
    }

    private void setDefaultStyle() {
        setBackground(WALL_COLOR);
        setBounds(0, 0, UNIT_SIZE, UNIT_SIZE);
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                g.setColor(new Color(115, 104, 104, 255));
                g.drawRect(x + 2, y + 2, width - 4, height - 4);
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

    public boolean isDeploymentEnabled(JPanel component) {
        if (component instanceof Lightning) {
            if (type == null) {
                return true;
            } else {
                return type.equals(WALL_STYLE1);
            }
        } else {
            return false;
        }
    }

    private void setStyle() {
        setBackground(Color.gray);
        setBounds(0, 0, UNIT_SIZE, UNIT_SIZE);
        setBorder(new Border() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                if (type != null) {
                    int bitWidth = Math.max(width / 9, 1);
                    int bitHeight = Math.max(height / 9, 1);
                    Graphics2D g2d = (Graphics2D) g;
                    // 设置抗锯齿
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(71, 74, 84));
                    for (int col = 0; col < 6; col++) {
                        int rowHead = (col % 2 == 1) ? 0 : -bitWidth;
                        for (int row = 0; row < 3; row++) {
                            // 绘制矩形
                            g2d.fillRect(row * (bitWidth * 4 + 1) + rowHead, col * (bitHeight * 2 + 1), bitWidth * 4, bitHeight * 2);
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

}
