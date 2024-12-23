package frame2.test;

import frame2.component.scene.FloorScene;
import frame2.component.scene.PlatformScene;

import javax.swing.*;
import java.awt.*;

import static frame2.common.ComponentConstant.UNIT_HEIGHT;
import static frame2.common.ComponentConstant.UNIT_WIDTH;

public class SceneTest {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setBounds(1000, 600, 10 * UNIT_WIDTH, 11 * UNIT_HEIGHT);
        frame.setBackground(Color.white);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel(null);

        for(int i = 0;i < 10;i ++){
            for(int j = 0;j < 10;j ++){
                if (i % 2 == 1 || j % 2 == 1){
                    PlatformScene p1 = new PlatformScene();
                    p1.setLocation(i * UNIT_WIDTH, j * UNIT_HEIGHT);
                    panel.add(p1);
                } else {
                    FloorScene f1 = new FloorScene();
                    f1.setLocation(i * UNIT_WIDTH, j * UNIT_HEIGHT);
                    panel.add(f1);
                }
            }
        }
        frame.add(panel);

        frame.setVisible(true);
    }
}
