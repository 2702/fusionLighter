package main.hellodrools;

public class Message {

    private String type;
    private String msgtext;
    private boolean processed = false;

    public Message()
    {
    }

    public Message(String msgtext)
    {
        this.msgtext = msgtext;
    }

    public boolean isProcessed()
    {
        return processed;
    }

    public void setProcessed(boolean processed)
    {
        this.processed = processed;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the msgtext
     */
    public String getMsgtext() {
        return msgtext;
    }

    /**
     * @param msgtext the msgtext to set
     */
    public void setMsgtext(String msgtext) {
        this.msgtext = msgtext;
    }
}