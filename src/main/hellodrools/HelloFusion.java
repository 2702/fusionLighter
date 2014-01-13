package main.hellodrools;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.definition.KnowledgePackage;
import org.drools.io.ResourceFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collection;

public class HelloFusion
{
    private static KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    private static Collection<KnowledgePackage> knowledgePackages;
    private static KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
    private static StatefulKnowledgeSession knowledgeSession;
    private static WorkingMemoryEntryPoint startEntryPoint;


    public static void main(String[] args) {
        initDrools();
        initMessageObjects();
    }

    private static void initDrools() {
        readFromFile();
        knowledgePackages = knowledgeBuilder.getKnowledgePackages();

        KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        config.setOption(EventProcessingOption.STREAM);
        knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(config);

        knowledgeBase.addKnowledgePackages(knowledgePackages);

        KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        conf.setOption(ClockTypeOption.get("realtime"));
        knowledgeSession = knowledgeBase.newStatefulKnowledgeSession(conf,null);

        startEntryPoint = knowledgeSession.getWorkingMemoryEntryPoint("startEntryPoint");
        new Thread() {
            @Override public void run()
            {
                knowledgeSession.fireUntilHalt();
            }
        }.start();
    }

    private static void readFromFile()
    {
        try
        {
            knowledgeBuilder.add(ResourceFactory.newReaderResource(new FileReader(new File(System.getProperty("user.dir") + "/src", "testfusion.drl"))), ResourceType.DRL);
        } catch (FileNotFoundException e){e.printStackTrace();}

        if (knowledgeBuilder.hasErrors())
        {
            System.out.println(knowledgeBuilder.getErrors().toString());
            throw new RuntimeException("Unable to compile drl\".");
        }
    }


    private static void initMessageObjects()
    {
        Message msg = new Message();
        msg.setMsgtext("1st message");
        startEntryPoint.insert(msg);

        mySleep(100);

        Message msg2 = new Message();
        msg2.setMsgtext("2nd message");
        startEntryPoint.insert(msg2);

        mySleep(100);

        Message msg3 = new Message();
        msg3.setMsgtext("3nd message");
        startEntryPoint.insert(msg3);

        mySleep(100);

        Message msg4 = new Message();
        msg4.setMsgtext("4nd message");
        startEntryPoint.insert(msg4);

        mySleep(4000);

        mySleep(100);
        startEntryPoint.insert(new Message("5"));
        mySleep(100);
        startEntryPoint.insert(new Message("6"));

        mySleep(6000);
        startEntryPoint.insert(new ReachedBottom());

        mySleep(4000);
        startEntryPoint.insert(new Message("7"));
        mySleep(100);
        startEntryPoint.insert(new Message("8"));

        mySleep(20000);

        knowledgeSession.halt();
        knowledgeSession.dispose();
    }

    private static void mySleep(long milis)
    {
        try
        {
            Thread.sleep(milis);
        }
        catch (final InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
