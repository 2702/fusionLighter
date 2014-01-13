package main.hellodrools;

import java.io.File;
import java.io.FileReader;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;

public class HelloDrools {

    private static RuleBase ruleBase = RuleBaseFactory.newRuleBase();
    private static PackageBuilder packageBuilder = new PackageBuilder();
    private static StatefulSession statefulSession;

    public static void main(String[] args) {
        initialiseDrools();
        initiliseMessageObject();
        runRules();
    }

    private static void initialiseDrools() {
        readDRLFile();
        checkForErrors();
        addPackageToRuleBase();
    }

    private static void addPackageToRuleBase()
    {
        try {
            ruleBase.addPackage(packageBuilder.getPackage());
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private static void checkForErrors()
    {
        PackageBuilderErrors errors = packageBuilder.getErrors();

        if (errors.getErrors().length > 0) {
            System.out.println("Some errors exists in packageBuilder");
            for (int i = 0; i < errors.getErrors().length; i++) {
                System.out.println(errors.getErrors()[i]);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
    }

    private static void readDRLFile()
    {
        try {
            packageBuilder.addPackageFromDrl(new FileReader(new File(System.getProperty("user.dir")+"/src", "testrules.drl")));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    // Method to fire all main.resources.rules
    private static void runRules() {
        statefulSession.fireAllRules();
    }

    // Method to insert message object in session
    private static void initiliseMessageObject() {
        Message msg = new Message();
        msg.setType("Hello");

        Message msg2 = new Message();
        msg2.setType("Hello");
        statefulSession = ruleBase.newStatefulSession();
        statefulSession.insert(msg);
        statefulSession.insert(msg2);
    }
}