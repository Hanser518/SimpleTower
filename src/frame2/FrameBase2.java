package frame2;

import frame2.component.scene.RouteScene;
import frame2.component.scene.StageScene;
import frame2.component.target.GroundTarget;
import frame2.pipeLine.EffectLine;
import frame2.pipeLine.SceneLine;
import frame2.pipeLine.TargetLine;
import frame2.pipeLine.TowerLine;

import javax.swing.*;

import java.awt.*;

import static frame2.common.FrameConstant.*;

public class FrameBase2 {


    public static void main(String[] args) {
        SwingUtilities.invokeLater(FrameBase2::new);
    }

    public FrameBase2() {
        MAIN_FRAME = new JFrame();
        // 更新屏幕参数
        updateScreenParam();

        // 窗口宽高
        MAIN_FRAME.setBounds(
                (int) (SCREEN_WIDTH * (1 - SCREEN_RATE) / 2),
                (int) (SCREEN_HEIGHT * (1 - SCREEN_RATE) / 2),
                FRAME_WIDTH, FRAME_HEIGHT);
        MAIN_FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MAIN_FRAME.setLayout(new BorderLayout());
        MAIN_FRAME.add(MAIN_LAYER, BorderLayout.CENTER);

//        MAIN_FRAME.setLayout(null);
//        MAIN_LAYER.setBounds(0, 0, 2000, 1000);
        MAIN_FRAME.add(MAIN_LAYER);
        EffectLine.initializeGlobalTimer();
        SceneLine.initializeGlobalTimer();
        TowerLine.initializeGlobalTimer();
        TargetLine.initializeGlobalTimer();

        example();
        MAIN_FRAME.setVisible(true);
    }

    private void updateScreenParam() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        SCREEN_WIDTH = screenSize.width;
        SCREEN_HEIGHT = screenSize.height;
        FRAME_WIDTH = (int) (SCREEN_WIDTH * SCREEN_RATE);
        FRAME_HEIGHT = (int) (SCREEN_HEIGHT * SCREEN_RATE) + 50;
    }

    private void example() {
        StageScene SC = new StageScene(10, 7);
        RouteScene RC = new RouteScene(SC.getSceneMatrix());
        for (int i = 0; i < 10; i++) {
            RC.addTarget(new GroundTarget(), null);
        }
        RC.register(MAIN_LAYER, new Point(SC.getSceneMatrix().length - 2, SC.getSceneMatrix()[0].length - 2), new Point(1, SC.getSceneMatrix()[0].length - 2));

        RouteScene RC2 = new RouteScene(SC.getSceneMatrix());
        for (int i = 0; i < 10; i++) {
            RC2.addTarget(new GroundTarget(), null);
        }
        RC2.register(MAIN_LAYER, new Point(SC.getSceneMatrix().length - 2, 1), new Point(1, SC.getSceneMatrix()[0].length - 2));

    }

}
