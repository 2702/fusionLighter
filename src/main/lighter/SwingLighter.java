package main.lighter;

import main.event.ReachedBottom;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SwingLighter extends LoggingLighter
{
    private int bulbPower = 0;
    private final List<Icon> images = new ArrayList<Icon>();
    private CustomFrame appFrame;

    @Override
    public synchronized void up()
    {
        super.up();
        if (bulbPower < images.size())
        {
            bulbPower++;
        }
        sendImageToGUI();
    }

    @Override
    public synchronized void down()
    {
        super.down();
        if (bulbPower > 0)
        {
            bulbPower--;
        }
        if (bulbPower == 0)
        {
            startEntryPoint.insert(new ReachedBottom());
        }
        sendImageToGUI();
    }

    @Override
    public void init(final WorkingMemoryEntryPoint startEntryPoint)
    {
        super.init(startEntryPoint);
        loadImages();
        startGUI();
        while(true) { }
    }

    private void startGUI()
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                appFrame = new CustomFrame("Drools fusion lighter");
                appFrame.init();
            }
        });
    }

    private void loadImages()
    {
        final File imagesDirectory = new File(System.getProperty("user.dir") + "/res/images");
        List<ImageIcon> icons = new ArrayList<ImageIcon>();
        for (final File file : imagesDirectory.listFiles())
        {
            try
            {
                icons.add(new ImageIcon(ImageIO.read(file), file.getName()));
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        Collections.sort(icons, new FileNameComparator());
        for (Icon icon : icons)
        {
            images.add(icon);
        }
    }

    private void sendImageToGUI()
    {
        if (bulbPower < images.size())
        {
            appFrame.changeImage(bulbPower);
        }
    }

    class CustomFrame extends JFrame
    {
        private final List<JLabel> imageLabelsBuffer = new ArrayList<JLabel>();
        private JLabel imageLabel;
        final JButton button = new JButton("Click me!");

        public CustomFrame(String title) throws HeadlessException
        {
            super(title);
        }

        public void changeImage(int icon)
        {
            remove(imageLabel);
            imageLabel = imageLabelsBuffer.get(icon);
            add(imageLabel);
            revalidate();
            repaint();
        }

        public void init()
        {
            for (Icon icon : images)
            {
                imageLabelsBuffer.add(new JLabel(icon));
            }
            setLayout(new FlowLayout());
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            imageLabel = imageLabelsBuffer.get(0);
            add(button);
            add(imageLabel);

            button.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    startEntryPoint.insert(new main.event.ClickEvent());
                }
            });
            pack();
            setVisible(true);
        }
    }
}

