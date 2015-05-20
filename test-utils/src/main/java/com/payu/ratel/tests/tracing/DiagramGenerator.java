package com.payu.ratel.tests.tracing;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Stack;

import com.google.common.base.Strings;
import com.payu.ratel.client.ServiceCallListener;
import com.payu.ratel.context.ProcessContext;
import com.payu.ratel.context.RemoteServiceCallEvent;
import com.payu.ratel.context.RemoteServiceResponseEvent;
import com.payu.ratel.context.ServiceInstanceCallEvent;
import com.payu.ratel.context.ServiceInstanceResponseEvent;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * Examplary ServiceCallListener that works as a diagram generator.
 * If you use the same instance on both client and server side, you can use
 * method
 * generateTraceDiagram to generate a sequence diagram with inter-service
 * calls;
 */
@SuppressWarnings("PMD")
public class DiagramGenerator implements ServiceCallListener {

    private final Stack<String> context = new Stack<String>();
    private StringBuilder diagramTxt = new StringBuilder();

    public void generateTraceDiagram(String fileName) throws FileNotFoundException, IOException {
        String source = "@startuml\n";
        source += diagramTxt.toString();
        source += "@enduml\n";
        storeDiagram(source, fileName);
    }

    public void reset() {
        this.context.clear();
        this.diagramTxt = new StringBuilder();
    }

    private void storeDiagram(String source, String fileName) throws IOException, FileNotFoundException {
        SourceStringReader reader = new SourceStringReader(source);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Write the first image to "os"
        reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        os.close();
        FileOutputStream fos = new FileOutputStream(fileName);

        reader.generateImage(fos, new FileFormatOption(FileFormat.PNG));
        fos.close();
    }

    @Override
    public void serviceInstanceCalled(ServiceInstanceCallEvent event) {
        diagramTxt.append("activate ").append(context.peek()).append('\n');
    }

    @Override
    public void serviceInstanceResponded(ServiceInstanceResponseEvent event) {
        diagramTxt.append("deactivate ").append(context.peek()).append('\n');
    }

    @Override
    public void remoteServiceCalled(RemoteServiceCallEvent event) {
        String caller = getCurrentCaller();
        String callee = getCallee(event);
        diagramTxt.append(caller).append(" -> ").append(callee).append(" : ").append(event.getInput().getMethodName())
                .append('\n');
        context.push(callee);
    }

    private String getCallee(RemoteServiceCallEvent event) {
        return event.getInput().getApiType().getSimpleName().trim().replaceAll("\\s+", "");
    }

    private String getCurrentCaller() {
        String caller;
        if (context.isEmpty()) {
            caller = ProcessContext.getInstance().getProcessIdentifier();
        } else {
            caller = context.peek();
        }
        return Strings.nullToEmpty(caller).replaceAll("\\s+", "");
    }

    @Override
    public void remoteServiceResponded(RemoteServiceResponseEvent event) {
        context.pop();
    };

}
