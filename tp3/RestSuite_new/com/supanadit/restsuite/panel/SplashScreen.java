package com.supanadit.restsuite.panel;
import com.supanadit.restsuite.helper.DefaultIcon;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JWindow;
import java.awt.*;
import javax.swing.*;
public class SplashScreen extends JWindow {
    public static int defaultWidth = 500;

    public static int defaultHeight = 300;

    public Dimension dimension;

    private long startTime;

    private int minimumMilliseconds;

    public SplashScreen() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dimension = new Dimension(defaultWidth, defaultHeight);
        setAlwaysOnTop(true);
        setIconImage(new DefaultIcon().getImage());
        setSize(dimension);
        setLocation((dim.width / 2) - (getSize().width / 2), (dim.height / 2) - (getSize().height / 2));
    }

    public void show(int minimumMilliseconds) {
        analyse.Analyse.printMe("SplashScreen","SplashScreen");
        this.minimumMilliseconds = minimumMilliseconds;
        setVisible(true);
        startTime = System.currentTimeMillis();
    }

    public void close() {
        analyse.Analyse.printMe("SplashScreen","SplashScreen");
        long elapsedTime = System.currentTimeMillis() - startTime;
        try {
            Thread.sleep(Math.max(minimumMilliseconds - elapsedTime, 0));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setVisible(false);
    }
}