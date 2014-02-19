package main.lighter;

import javax.swing.*;
import java.util.Comparator;

public class FileNameComparator implements Comparator<ImageIcon>
{
    @Override
    public int compare(ImageIcon o1, ImageIcon o2)
    {
        return o1.getDescription().compareTo(o2.getDescription());
    }
}
