package com.payu.ratel.tests.tracing;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

public class DiagramGenerator {

    public void clientCalled(ClientCallMetadata clientCall) {

    }

    public void serverCalled(ServiceCallMetadata serviceCall) {

    }

    public static void main(String[] args) throws IOException {
        new DiagramGenerator().generate();
    }

    @SuppressWarnings("PMD")
    public void generate() throws IOException {
        String source = "@startuml\n";
        source += "Bob2 -> Alice2 : was ist das?\n";
        source += "@enduml\n";

        SourceStringReader reader = new SourceStringReader(source);
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Write the first image to "os"
        String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
        os.close();
        FileOutputStream fos = new FileOutputStream("c:\\tmp\\diagram.png");

        reader.generateImage(fos, new FileFormatOption(FileFormat.PNG));
        fos.close();

        // The XML is stored into svg
//        final String svg = new String(os.toByteArray(), Charset.forName("UTF-8"));
    }

}
