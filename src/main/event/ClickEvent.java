package main.event;

public class ClickEvent
{
    private boolean processed = false;

    public boolean isProcessed()
    {
        return processed;
    }

    public void setProcessed(boolean processed)
    {
        this.processed = processed;
    }
}