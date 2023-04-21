package com.example.hadbackend.service.fhir;

//import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.example.hadbackend.bean.auth.Login;
//import org.hl7.fhir.dstu2.model.*;
//import org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent;
//import org.hl7.fhir.dstu2.model.Bundle.BundleType;
//import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import com.example.hadbackend.bean.carecontext.Medicalrecords;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestIntent;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class GenerateRecord {

    public Bundle createmedicalrecords(com.example.hadbackend.bean.carecontext.Patient patient, Login login,Medicalrecords medicalrecords) throws ParseException {
         Bundle opdconsult = new Bundle();
        opdconsult.setId("Opdconsult");

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date(System.currentTimeMillis());

        Meta meta= (Meta) opdconsult.getMeta(); // type meta but itt id IBase type
        meta.setVersionId("1");
        meta.setLastUpdatedElement(new InstantType(formatter.format(date)));
        meta.addProfile("http://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");

        Coding coding= meta.addSecurity();
        coding.setCode("V");
        coding.setDisplay("very restricted");
        coding.setSystem("http://terminology.hl7.org/CodeSystem/v3-Confidentiality");

        Identifier identifier= opdconsult.getIdentifier();
        identifier.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d"); //check whether uuid
        identifier.setSystem("http://hip.in");

        opdconsult.setType(Bundle.BundleType.DOCUMENT);  // check this
        opdconsult.setTimestampElement(new InstantType(formatter.format(date))); //No function called setTimesst
        List<Bundle.BundleEntryComponent> lb = opdconsult.getEntry();
        addentriestobundle(opdconsult,lb,patient,login,medicalrecords);

        return opdconsult;
    }

    /**
     * This method validates the FHIR resources
     */
//    public static boolean validate(IBaseResource resource)
//    {
//
//        FhirInstanceValidator instanceValidator = new FhirInstanceValidator(supportChain);
//        validator = ctx.newValidator().registerValidatorModule(instanceValidator);
//        // Validate
//        ValidationResult result = validator.validateWithResult(resource);
//
//        // The result object now contains the validation results
//        for (SingleValidationMessage next : result.getMessages()) {
//            System.out.println(next.getSeverity().name() + " : " + next.getLocationString() + " " + next.getMessage());
//        }
//
//        return result.isSuccessful();
//    }
    void addentriestobundle(Bundle op, List<Bundle.BundleEntryComponent> lb, com.example.hadbackend.bean.carecontext.Patient p, Login login, Medicalrecords medicalrecords) throws ParseException {
        Composition composition=new Composition();
        composition.setId(String.valueOf(new Random().nextInt(900)));

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date(System.currentTimeMillis());

        Bundle.BundleEntryComponent bundleEntryComponent=new Bundle.BundleEntryComponent();
        bundleEntryComponent.setFullUrl("Composition/"+composition.getId());
        bundleEntryComponent.setResource(composition);
        lb.add(bundleEntryComponent);

        Meta meta=composition.getMeta();
        meta.setVersionId("1");
        meta.setLastUpdatedElement(new InstantType(formatter.format(date)));
        meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/OPConsultRecord");

        composition.setLanguage("en-IN");

        Narrative text=composition.getText();
        text.setStatus(Narrative.NarrativeStatus.GENERATED);
        text.setDivAsString("This is a OP consultant for patient");

        Identifier identifier=composition.getIdentifier();
        identifier.setSystem("https://ndhm.in/phr");
        identifier.setValue("645bb0c3-ff7e-4123-bef5-3852a4784813"); //can it be any uuid

        composition.setStatus(Composition.CompositionStatus.FINAL);
        composition.setDate(date); // how to add date
        composition.setTitle("Restraint Summary"); // not in ref ramya

        CodeableConcept type=composition.getType();
        Coding coding=new Coding();
        coding.setSystem("http://snomed.info/sct");
        coding.setCode("371530004");
        coding.setDisplay("Clinical consultation report");
        type.addCoding(coding);

        //Patient data

        Patient patient= new Patient();
        patient.setId(p.getId());
        Meta meta1=patient.getMeta();
        meta1.setVersionId("1");
        meta1.setLastUpdatedElement(new InstantType(formatter.format(date)));   //need to fix this
        meta1.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");
        patient.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("Patient Details");

//        patient.addIdentifier().setType(
//                new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number"))).
//                setSystem("https://healthid.ndhm.gov.in").setValue(p.getPatientid().toString());
        Identifier identifier1=patient.addIdentifier();
        Coding coding1=new Coding();
        coding1.setDisplay("Medical record number");
        coding1.setCode("MR");
        coding1.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203");
        identifier1.setType(new CodeableConcept(coding1));
        identifier1.setSystem("https://healthid.ndhm.gov.in");
        identifier1.setValue(p.getPatientid().toString());  //restrainsummery.getPatientId

        patient.addName().setText(p.getName()); //getname fro patient in db


        //check the structure again

        Patient.ContactComponent contactDetail=patient.addContact();
        ContactPoint c=contactDetail.addTelecom();
        c.setSystem(ContactPoint.ContactPointSystem.PHONE);
        c.setValue(p.getMobile().toString());
        c.setUse(ContactPoint.ContactPointUse.MOBILE);
//            if(p.getGender()=="M")
//                patient.setGender(Enumerations.AdministrativeGender.valueOf("male"));
//            else
//            patient.setGender(Enumerations.AdministrativeGender.valueOf("female"));

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date1=String.valueOf(p.getYear())+"-"+String.valueOf(p.getMonth())+"-"+String.valueOf(p.getDay());
        Date datecov = formatter1.parse(date1);
        patient.setBirthDate(datecov); //Date value

        String patientref="Patient/"+patient.getId();
        Reference reference=new Reference();
        reference.setReference("Patient/"+patient.getId());
        reference.setDisplay("Patient record");
        composition.setSubject(reference);

        Bundle.BundleEntryComponent bundleEntryComponent1=new Bundle.BundleEntryComponent();
        bundleEntryComponent1.setFullUrl("Patient/"+patient.getId());
        bundleEntryComponent1.setResource(patient);
        lb.add(bundleEntryComponent1); //addet to entry list

        composition.setDateElement(new DateTimeType(new Date()));

        //Preactioner

        Practitioner practitioner = new Practitioner();
        practitioner.setId("Practitioner-"+new Random().nextInt(900));
        practitioner.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType(formatter.format(date))).addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
        practitioner.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString(login.getName());
        practitioner.addIdentifier().setType(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MD", "Medical License number"))).setSystem("https://ndhm.in/DigiDoc").setValue(String.valueOf(login.getId()));
        practitioner.addName().setText(login.getName());

        composition.addAuthor(new Reference().setReference("Practitioner/"+practitioner.getId()));


        Bundle.BundleEntryComponent bundleEntry4 = new Bundle.BundleEntryComponent();
        bundleEntry4.setFullUrl("Practitioner/"+practitioner.getId());
        bundleEntry4.setResource(practitioner);
        lb.add(bundleEntry4);

        composition.setTitle("OP consulatation");

        //organisation

        Reference referenceCustodian = new Reference();
        referenceCustodian.setReference("Organization/Organization-01");
        referenceCustodian.setDisplay("IIITB Team 18 HIP");
        composition.setCustodian(referenceCustodian);


        //encounter

        Encounter en = new Encounter();
        en.setId("Encounter-"+new Random().nextInt(999));
        en.setStatus(Encounter.EncounterStatus.FINISHED);
        Meta meta2=en.getMeta();
        meta2.setLastUpdatedElement(new InstantType(date));
        meta2.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Encounter");
        en.getIdentifier().add(new Identifier().setSystem("https://ndhm.in").setValue(String.valueOf(login.getId())));
        en.setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
        en.setSubject(new Reference().setReference(patientref));
        en.setPeriod(new Period().setStartElement(new DateTimeType(new Date())).setEndElement(new DateTimeType(new Date())));
        Narrative nr = new Narrative();
        nr.setDivAsString("Out Patient Consultation Encounter");
        nr.setStatusAsString("generated");
        en.setText(nr);

        Bundle.BundleEntryComponent bundleEntry2 = new Bundle.BundleEntryComponent();
        bundleEntry2.setFullUrl("Encounter/"+en.getId());
        bundleEntry2.setResource(en);
        lb.add(bundleEntry2);

        composition.setEncounter(new Reference().setReference("Encounter/"+en.getId()));


        //coditions

        patientref = composition.getSubject().getReference().toString();

        List<Composition.SectionComponent> section =  composition.getSection();


        Condition condition1 = new Condition();
        condition1.setId("Condition-"+new Random().nextInt(900));
        condition1.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
        condition1.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString(medicalrecords.getPattern());  ///from consultation form what happened
        condition1.setSubject(new Reference(patientref));
        condition1.getCode().addCoding(new Coding()).setText(medicalrecords.getSymptoms()); //from consultation form symptom , other things like system code all are ignored

        // for multiple symptoms copy the same change the id and text symptoms

        Boolean sectionPresent = false;
        for(Composition.SectionComponent sec : section) {


            if(sec.getTitle().equalsIgnoreCase("Chief complaints")) {
                //if(op.getIllnessSummary()!=null) {
                    sec.getText().setDivAsString(medicalrecords.getSymptoms()); /////from consultation form  add symptom    symptom
                    sec.getText().setStatusAsString("generated");
                //}
                sec.addEntry(new Reference().setReference("Condition/"+condition1.getId()));
                sectionPresent = true;
            }
        }

        if(!sectionPresent)
        {
            Composition.SectionComponent sectionCheifComplaints = new Composition.SectionComponent();
            sectionCheifComplaints.setTitle("Chief complaints");
            //sectionCheifComplaints.getText().setDivAsString(op.getIllnessSummary().toString()); // from medicalrecords
            sectionCheifComplaints.getText().setStatusAsString("generated");
            sectionCheifComplaints.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "422843007", "Chief complaint section"))).
                    addEntry(new Reference().setReference("Condition/"+condition1.getId()));
            section.add(sectionCheifComplaints);

        }
        Bundle.BundleEntryComponent bundleEntry1 = new Bundle.BundleEntryComponent();
        bundleEntry1.setFullUrl("Condition/"+condition1.getId());
        bundleEntry1.setResource(condition1);
        lb.add(bundleEntry1);

        //medical request
        MedicationRequest medicationRequest = new MedicationRequest();
        medicationRequest.setId("MedicationRequest-"+new Random().nextInt(900));
        medicationRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
        medicationRequest.setStatus(MedicationRequest.MedicationRequestStatus.ACTIVE);
        medicationRequest.setIntent(MedicationRequestIntent.ORDER);
        //medicationRequest.setMedication(new CodeableConcept().setText(op.getMedicationItem())); //from medicalrecords
        medicationRequest.setSubject(new Reference().setReference(patientref));
        medicationRequest.setAuthoredOnElement(new DateTimeType(date));
        medicationRequest.setRequester(new Reference().setReference("Practitioner"+practitioner.getId()).setDisplay(login.getName()));
        medicationRequest.getReasonReference().add(new Reference().setReference("Condition"+condition1.getId()));
        Dosage dosage= medicationRequest.addDosageInstruction();
        dosage.setText("One tablet at once");
        dosage.addAdditionalInstruction().setText(medicalrecords.getTimings());
        dosage.setRoute(new CodeableConcept().setText(medicalrecords.getInstruction()));
        dosage.setMethod(new CodeableConcept(new Coding("http://snomed.info/sct", "421521009", "Swallow")));
//            medicationRequest.addDosageInstruction(new Dosage().setText("One tablet at once").addAdditionalInstruction(new CodeableConcept().setText(medicalrecords.getTimings()).
//            //	setTiming(new Timing().setRepeat(new TimingRepeatComponent().setFrequency(1).setPeriod(1).setPeriodUnit(UnitsOfTime.D))).
//            setRoute(new CodeableConcept().setText(medicalrecords.getInstruction()).
//                    setMethod(new CodeableConcept(new Coding("http://snomed.info/sct", "421521009", "Swallow")))));


        sectionPresent = false;
        for(Composition.SectionComponent sec : section) {


            if(sec.getTitle().equalsIgnoreCase("Medications")){
                    //sec.getText().setDivAsString(op.getClinicalHistory().toString());
                    //sec.getText().setStatusAsString("generated");
                sec.addEntry(new Reference().setReference("MedicationRequest/"+medicationRequest.getId()));
                sectionPresent = true;
            }
        }

        if(!sectionPresent)
        {

            Composition.SectionComponent section5 = new Composition.SectionComponent();
            section5.setTitle("Medications");
            //text is not there in json
            //section5.getText().setDivAsString(op.getClinicalHistory().toString());
            //section5.getText().setStatusAsString("generated");
            section5.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "721912009", "Medication summary document"))).
                    addEntry(new Reference().setReference("MedicationRequest/"+medicationRequest.getId()));
            //adding to compositon
            section.add(section5);

        }

        //adding to bundle
        Bundle.BundleEntryComponent bundleEntry3 = new Bundle.BundleEntryComponent();
        bundleEntry3.setFullUrl("MedicationRequest/"+medicationRequest.getId());
        bundleEntry3.setResource(medicationRequest);
        lb.add(bundleEntry3);


//        Condition condition1 = new Condition();
//        condition1.setId("Condition-"+new Random().nextInt(900));
//        condition1.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
//        condition1.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString( op.getProblemDescription());
//        condition1.setSubject(new Reference(patientref));
//        condition1.getCode().addCoding(new Coding("http://snomed.info/sct", op.getProblemDiagnosisCode(), op.getDiagnosticCertainity())).setText(op.getProblemDescription());
//
//        BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
//        bundleEntry1.setFullUrl("Condition/"+condition1.getId());
//        bundleEntry1.setResource(condition1);
//        lb.add(bundleEntry1);
//
//        for(int i=0 ; i<lb.size();i++)
//        {
//
//            Bundle.BundleEntryComponent bec = lb.get(i);
//            if(bec.getResource().getResourceType().toString().equalsIgnoreCase("Encounter"))
//            {
//                Encounter encounter = (Encounter)bec.getResource();
//
//                DiagnosisComponent dc =new DiagnosisComponent() ;
//                dc.getCondition().setReference("Condition/"+condition1.getId());
//
//                encounter.addDiagnosis(dc);
//
//
//            }
//        }
    }
}
