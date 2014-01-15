package main.lighter;

import main.Lighter;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

public class LoggingLighter implements Lighter
{
    protected WorkingMemoryEntryPoint startEntryPoint;

    @Override
    public void up()
    {
        System.out.println("Lighter UP!");
    }

    @Override
    public void down()
    {
        System.out.println("Lighter Down!");
    }

    @Override
    public void init(WorkingMemoryEntryPoint startEntryPoint)
    {
        this.startEntryPoint = startEntryPoint;
    }
}
