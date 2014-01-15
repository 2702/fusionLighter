package main;

import main.event.ReachedBottom;
import main.lighter.SwingLighter;
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

public class LighterDriver
{
    private final KnowledgeBuilder knowledgeBuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
    private StatefulKnowledgeSession knowledgeSession;
    private WorkingMemoryEntryPoint startEntryPoint;

    private final Lighter lighter;

    public LighterDriver(Lighter swingLighter)
    {
        this.lighter = swingLighter;
    }


    public static void main(String[] args) {
        new LighterDriver(new SwingLighter()).start();
    }

    private void start()
    {
        initDrools();
        initLighter(startEntryPoint);
        cleanUpSession();
    }

    private void initLighter(WorkingMemoryEntryPoint startEntryPoint)
    {
        startEntryPoint.insert(new ReachedBottom());
        lighter.init(startEntryPoint);
    }

    private void initDrools() {
        readFromFile();
        Collection<KnowledgePackage> knowledgePackages = knowledgeBuilder.getKnowledgePackages();

        KnowledgeBaseConfiguration config = KnowledgeBaseFactory.newKnowledgeBaseConfiguration();
        config.setOption(EventProcessingOption.STREAM);
        KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase(config);

        knowledgeBase.addKnowledgePackages(knowledgePackages);

        KnowledgeSessionConfiguration conf = KnowledgeBaseFactory.newKnowledgeSessionConfiguration();
        conf.setOption(ClockTypeOption.get("realtime"));
        knowledgeSession = knowledgeBase.newStatefulKnowledgeSession(conf, null);
        knowledgeSession.setGlobal("lighter", lighter);

        startEntryPoint = knowledgeSession.getWorkingMemoryEntryPoint("startEntryPoint");
        new Thread() {
            @Override public void run()
            {
                knowledgeSession.fireUntilHalt();
            }
        }.start();
    }

    private void readFromFile()
    {
        try
        {
            knowledgeBuilder.add(ResourceFactory.newReaderResource(new FileReader(new File(System.getProperty("user.dir") + "/res/rules", "lighterRules.drl"))), ResourceType.DRL);
        } catch (FileNotFoundException e){e.printStackTrace();}

        if (knowledgeBuilder.hasErrors())
        {
            System.out.println(knowledgeBuilder.getErrors().toString());
            throw new RuntimeException("Unable to compile drl\".");
        }
    }

    private void cleanUpSession()
    {
        knowledgeSession.halt();
        knowledgeSession.dispose();
    }

}
