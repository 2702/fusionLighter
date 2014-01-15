package main;

import org.drools.runtime.rule.WorkingMemoryEntryPoint;

public interface Lighter
{
    void up();

    void down();

    void init(WorkingMemoryEntryPoint startEntryPoint);
}
