package core;

import frame.page.TestPage;
import frame.param.FontParam;
import frame.param.FrameParam;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static frame.param.FrameParam.*;

public class FrameBase extends JFrame implements ActionListener {

    private Timer freshTimer = new Timer(1000 / FRAME_REFRESH_INTERVAL, this);
    private boolean mouseClicked = false;

    public FrameBase() {
        setTitle("Simple Tower");
        setFont(FontParam.titleFont);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        FrameParam.screenWidth = screenSize.width;
        FrameParam.screenHeight = screenSize.height;
        FrameParam.frameWidth = (int) (FrameParam.screenWidth * FrameParam.frameRate);
        FrameParam.frameHeight = (int) (FrameParam.screenHeight * FrameParam.frameRate);
        // 窗口宽高
        setBounds(
                (int) (FrameParam.screenWidth * (1 - FrameParam.frameRate) / 2),
                (int) (FrameParam.screenHeight * (1 - FrameParam.frameRate) / 2),
                FrameParam.frameWidth,
                FrameParam.frameHeight + 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // getContentPane().add(new G2dPage(), BorderLayout.CENTER);
        init();
        new TestPage(this);
        freshTimer.start();
        setVisible(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                mouseClicked = true;
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (mouseClicked) {
            System.out.println("repaint");
            TestPage.init();
            mouseClicked = false;
        }
    }
}
