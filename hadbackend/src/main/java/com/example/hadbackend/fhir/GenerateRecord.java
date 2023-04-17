package com.example.hadbackend.fhir;

import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import org.hl7.fhir.dstu2.model.Identifier;
import org.hl7.fhir.dstu2.model.Meta;
import org.hl7.fhir.dstu2.model.Coding;
import org.hl7.fhir.dstu2.model.InstantType;
import org.hl7.fhir.r4.model.*;


import java.util.Date;

public class GenerateRecord {

    void createmedicalrecords() {
        Bundle opdconsult = new Bundle();
        opdconsult.setId("Opdconsult");

        Meta meta= (Meta) opdconsult.getMeta(); // type meta but itt id IBase type
        meta.setVersionId("1");
        //meta.setLastUpdated(new InstantType(Date.from(date)));
        meta.addProfile("https://nrces.in/ndhm/fh…efinition/DocumentBundle");

        Coding coding= (Coding) meta.getSecurity();
        coding.setCode("V");
        coding.setDisplay("very restricted");
        coding.setSystem("https://terminology.hl7.org/5.1.0/CodeSystem-v3-Confidentiality.html");

        Identifier identifier= (Identifier) opdconsult.getIdElement();
        identifier.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d");
        identifier.setSystem("http://hip.in");

        opdconsult.setType(BundleTypeEnum.valueOf("document"));  // check this
        //opdconsult.setTimestampElement //No function called setTimesstamp




    }

}
