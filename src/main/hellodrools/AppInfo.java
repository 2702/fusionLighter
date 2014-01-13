package main.hellodrools;

import java.util.Date;

public class AppInfo
{
    private Date timestamp;
    public AppInfo()
    {
        this.timestamp = new Date();
    }

    @Override
    public String toString()
    {
        return "AppInfo{" + "startTime=" + timestamp + '}';
    }

    public Date getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }
}