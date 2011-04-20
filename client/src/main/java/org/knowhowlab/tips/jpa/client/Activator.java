package org.knowhowlab.tips.jpa.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * @author dpishchukhin
 */
public class Activator implements BundleActivator {
    private ServiceRegistration serviceRegistration;

    public void start(BundleContext context) throws Exception {
        Dictionary<String, Object> props = new Hashtable<String, Object>();
        props.put("org.knowhowlab.osgi.shell.group.id", "jpa");
        props.put("org.knowhowlab.osgi.shell.group.name", "JPA tips commands");
        props.put("org.knowhowlab.osgi.shell.commands", new String[][]{
                {"lsstuds", "lsstuds - list students"},
                {"lsgrps", "lsgrps - list groups"},
                {"delgrp", "delgrp <id> - delete group"},
                {"delstud", "delstud <id> - delete student"},
                {"addgrp", "addgrp <name> - add group"},
                {"addstud", "addstud <first_name> <last_name> <group_id> - add student"}});
        serviceRegistration = context.registerService(CommandLineService.class.getName(), new CommandLineService(context), props);
    }

    public void stop(BundleContext context) throws Exception {
        serviceRegistration.unregister();
    }
}
