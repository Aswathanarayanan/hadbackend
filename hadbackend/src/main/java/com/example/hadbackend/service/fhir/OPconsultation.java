package com.example.hadbackend.service.fhir;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.example.hadbackend.bean.carecontext.Medicalrecords;
import lombok.SneakyThrows;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Dosage;
import org.hl7.fhir.r4.model.Encounter.EncounterStatus;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.hl7.fhir.r4.model.Composition.CompositionStatus;
import org.hl7.fhir.r4.model.Composition.SectionComponent;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestIntent;
import org.hl7.fhir.r4.model.MedicationRequest.MedicationRequestStatus;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Narrative.NarrativeStatus;

import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.springframework.stereotype.Service;

@Service
public class OPconsultation {
    static FhirContext ctx = FhirContext.forR4();

    static FhirInstanceValidator instanceValidator;
    static FhirValidator validator;

    public OPconsultation() throws DataFormatException, FileNotFoundException
    {
        init();
    }

    @SneakyThrows
    public Bundle bundleoutput(Medicalrecords medicalrecords){
        Bundle OPConsultNoteBundle=populateOPConsultNoteBundle(medicalrecords,null);

        // Instantiate a new parser
        IParser parser;

        // Enter file path (Eg: C://generatedexamples//bundle-prescriptionrecord.json)
        // Depending on file type xml/json instantiate the parser
        File file;
        String filePath = "./bundle.json";
//        if(FilenameUtils.getExtension(filePath).equals("json"))
//        {
            parser = ctx.newJsonParser();
        //}
//        else if(FilenameUtils.getExtension(filePath).equals("xml"))
//        {
//            parser = ctx.newXmlParser();
//        }

        // Indent the output
        parser.setPrettyPrint(true);

        // Serialize populated bundle
        String serializeBundle = parser.encodeResourceToString(OPConsultNoteBundle);

        // Write serialized bundle in xml/json file
        file = new File(filePath);
        file.createNewFile();
        FileWriter writer = new FileWriter(file);
        writer.write(serializeBundle);
        writer.flush();
        writer.close();

        return OPConsultNoteBundle;
    }
    public Bundle populateOPConsultNoteBundle(Medicalrecords medicalrecords, Bundle req)
    {

        System.out.println("creating bundle");

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date(System.currentTimeMillis());

        Bundle opCounsultNoteBundle=new Bundle();
        req = new Bundle();


            opCounsultNoteBundle.setId("OPConsultNote-example-01");

            Meta meta = opCounsultNoteBundle.getMeta();
            meta.setVersionId("1");
            meta.setLastUpdatedElement(new InstantType(formatter.format(date)));
            meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/DocumentBundle");


            meta.addSecurity(new Coding("http://terminology.hl7.org/CodeSystem/v3-Confidentiality", "V", "very restricted"));


            Identifier identifier = opCounsultNoteBundle.getIdentifier();
            identifier.setValue("305fecc2-4ba2-46cc-9ccd-efa755aff51d");
            identifier.setSystem("http://hip.in");


            opCounsultNoteBundle.setType(BundleType.DOCUMENT);


            opCounsultNoteBundle.setTimestampElement(new InstantType(medicalrecords.getDate()));

            // Add resources entries for bundle with Full URL
            List<BundleEntryComponent> listBundleEntries = opCounsultNoteBundle.getEntry();
            if(listBundleEntries.size()<=0) {
                Composition comp = new Composition();

                comp = populateOPConsultNoteCompositionResource(medicalrecords,comp,listBundleEntries );
            }

        System.out.println("opCounsultNoteBundle"+opCounsultNoteBundle);

        return opCounsultNoteBundle;
    }

    /**
     * This method initiates loading of FHIR default profiles and NDHM profiles for validation
     */
    public  void init() throws DataFormatException, FileNotFoundException
    {

        // Create xml parser object for reading profiles
        IParser parser = ctx.newJsonParser();

        // Create a chain that will hold our modules
        ValidationSupportChain supportChain = new ValidationSupportChain();

        // Add Default Profile Support
        // DefaultProfileValidationSupport supplies base FHIR definitions. This is generally required
        // even if you are using custom profiles, since those profiles will derive from the base
        // definitions.
        DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(ctx);

        // Create a PrePopulatedValidationSupport which can be used to load custom definitions.
        // In this example we're loading all the custom Profile Structure Definitions, in other scenario we might
        // load many StructureDefinitions, ValueSets, CodeSystems, etc.
        PrePopulatedValidationSupport prePopulatedSupport = new PrePopulatedValidationSupport(ctx);
        StructureDefinition sd ;

        /** LOADING PROFILES **/
        // Read all Profile Structure Definitions

//        String resourceFolderName = "Structuredefinitions";
//
//
//        URL Url = getClass().getClassLoader().getResource(resourceFolderName);
//        String baseFilePath = Url.getPath();
        String baseFilePath="/home/aswathanarayanan/HAD/definitions.json";
        File dir=new File(baseFilePath);
        FileFilter fileFilter=new WildcardFileFilter("*.json");
        File[] files = dir.listFiles(fileFilter);
        ArrayList<String> fileList =new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            //fileList.add(String.valueOf(files[i]));
            System.out.println(files[i]);
        }
        //String[] fileList = new File(baseFilePath).list(new WildcardFileFilter("*.json"));
        System.out.println(baseFilePath);
        //System.out.println(fileList.length());
        for(String file:fileList)
        {
            String fileFullPath = file; //baseFilePath.substring(1) +"/"+ file
            //Parse All Profiles and add to prepopulated support
            System.out.println(fileFullPath);

            sd = parser.parseResource(StructureDefinition.class, new FileReader(fileFullPath));

            prePopulatedSupport.addStructureDefinition(sd);
        }

        //Add Snapshot Generation Support
        SnapshotGeneratingValidationSupport snapshotGenerator = new SnapshotGeneratingValidationSupport(ctx);

        //Add prepopulated support consisting all structure definitions and Terminology support
        supportChain.addValidationSupport(defaultSupport);
        supportChain.addValidationSupport(prePopulatedSupport);
        supportChain.addValidationSupport(snapshotGenerator);
        supportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(ctx));
        supportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(ctx));

        // Create a validator using the FhirInstanceValidator module and register.
        instanceValidator = new FhirInstanceValidator(supportChain);
        validator = ctx.newValidator().registerValidatorModule(instanceValidator);

    }

    /**
     * This method validates the FHIR resources
     */
    public static boolean validate(IBaseResource resource)
    {
        // Validate
        ValidationResult result = validator.validateWithResult(resource);

        // The result object now contains the validation results
        for (SingleValidationMessage next : result.getMessages()) {
            System.out.println(next.getSeverity().name() + " : " + next.getLocationString() + " " + next.getMessage());
        }

        return result.isSuccessful();
    }

    public Composition populateOPConsultNoteCompositionResource(Medicalrecords medicalrecords , Composition comp, List<BundleEntryComponent> lb)
    {

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = new Date(System.currentTimeMillis());
        Composition composition = comp;
        String patientref = "";

        System.out.println("composition"+composition);

        if(composition.getId()==null) {
            composition.setId("Composition-"+new Random().nextInt(900));


            BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
            bundleEntry2.setFullUrl("Composition/"+composition.getId());
            bundleEntry2.setResource(composition);
            lb.add(bundleEntry2);




        }



        if(composition.getMeta().getId()==null) {
            Meta meta = composition.getMeta();
            meta.setVersionId("1");
            meta.setLastUpdatedElement(new InstantType(formatter.format(date)));
            meta.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/OPConsultRecord");
        }

        if(composition.getLanguage()==null) {
            comp.setLanguage("en-IN");
        }

        if(composition.getText().getStatus()==null) {
            Narrative text= composition.getText();
            text.setStatus((NarrativeStatus.GENERATED));
            text.setDivAsString("This is a OP consultation for patient");

        }
        if(composition.getIdentifier().getValue()==null) {
            Identifier identifier = composition.getIdentifier();
            identifier.setSystem("https://healthid.ndhm.gov.in");
            identifier.setValue(medicalrecords.getPatient().getPatientid().toString());
        }

        // Status can be preliminary | final | amended | entered-in-error
        composition.setStatus(CompositionStatus.FINAL);

        composition.setDate((medicalrecords.getDate()));

        // Kind of composition ("Clinical consultation report")
        CodeableConcept type = composition.getType();
        type.addCoding(new Coding("http://snomed.info/sct", "371530004", "Clinical consultation report"));
        type.setText("Clinical Consultation report");

        //composition.setType(new CodeableConcept(new Coding("http://snomed.info/sct", "371530004", "Clinical consultation report")).setText("Clinical Consultation report"));

        // Set subject - Who and/or what the composition/OPConsultNote record is about

        if(composition.getSubject().getReference()==null) {



            Patient patient = new Patient();
            patient.setId("Patient-"+new Random().nextInt(900));
            patient.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType(formatter.format(date))).addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Patient");
            patient.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString("Patient Details");
            patient.addIdentifier().setType(
                            new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MR", "Medical record number")))
                    .setSystem("https://healthid.ndhm.gov.in").setValue(medicalrecords.getPatient().getPatientid().toString());
            patientref = "Patient/"+patient.getId();

            Reference reference = new Reference();
            reference.setReference("Patient/"+patient.getId());
            reference.setDisplay("Patients BMR Record");
            composition.setSubject(reference);


            BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
            bundleEntry2.setFullUrl("Patient/"+patient.getId());
            bundleEntry2.setResource(patient);
            lb.add(bundleEntry2);


        }


        // Set Context of the Composition
        //composition.setEncounter(new Reference().setReference("Encounter/Encounter-01"));


        composition.setDateElement(new DateTimeType(new Date()));

        // Set author - Who and/or what authored the composition/OPConsultNote record

        if(composition.getAuthor().size()==0) {

            Practitioner practitioner = new Practitioner();
            practitioner.setId("Practitioner-"+new Random().nextInt(900));
            practitioner.getMeta().setVersionId("1").setLastUpdatedElement(new InstantType(formatter.format(date))).addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Practitioner");
            practitioner.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString(String.valueOf(medicalrecords.getDoctor().getId()));
            practitioner.addIdentifier().setType(new CodeableConcept(new Coding("http://terminology.hl7.org/CodeSystem/v2-0203", "MD", "Medical License number"))).setSystem("https://ndhm.in/DigiDoc").setValue(String.valueOf(medicalrecords.getDoctor().getId()));
            practitioner.addName().setText(medicalrecords.getDoctor().getName());

            System.out.println("336");

            composition.addAuthor(new Reference().setReference("Practitioner/"+practitioner.getId()));


            BundleEntryComponent bundleEntry4 = new BundleEntryComponent();
            bundleEntry4.setFullUrl("Practitioner/"+practitioner.getId());
            bundleEntry4.setResource(practitioner);
            lb.add(bundleEntry4);

        }


        // Set a Human Readable name/title
        composition.setTitle("MHMS - op_bmr.v2");

        // Set Custodian - Organization which maintains the composition

        if(composition.getCustodian()==null) {
            Reference referenceCustodian = new Reference();
            referenceCustodian.setReference("Organization/Organization-01");
            referenceCustodian.setDisplay("IIITB Team 18 HIP");
            composition.setCustodian(referenceCustodian);
        }

        if(composition.getEncounter().getReference()==null) {
            Encounter en = new Encounter();
            en.setId("Encounter-"+new Random().nextInt(999));
            en.setStatus(EncounterStatus.FINISHED);
            Meta meta2=en.getMeta();
            meta2.setLastUpdatedElement(new InstantType(medicalrecords.getDate()));
            meta2.addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Encounter");
            en.getIdentifier().add(new Identifier().setSystem("https://ndhm.in").setValue("iiitbteam18"));
            en.setClass_(new Coding("http://terminology.hl7.org/CodeSystem/v3-ActCode", "IMP", "inpatient encounter"));
            en.setSubject(new Reference().setReference(patientref));
            en.setPeriod(new Period().setStartElement(new DateTimeType(new Date())).setEndElement(new DateTimeType(new Date())));
            Narrative nr = new Narrative();
            nr.setDivAsString(medicalrecords.getInstruction());
            nr.setStatusAsString("generated");
            en.setText(nr);



//            if(op.getImprovementStatus()!=null) {
//                Narrative nrservice = new Narrative();
//                nrservice.setDivAsString(op.getImprovementStatus().toString());
//                nrservice.setStatusAsString("generated");
//                en.setServiceType(new CodeableConcept()).setText(nrservice);
//
//            }

//		if(op.getProblemDiagnosis()!=null)
//		{
//
//			 System.out.println("551");
//			Condition condition1 = new Condition();
//			  condition1.setId("Condition-"+new Random().nextInt(900));
//			  condition1.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
//			  condition1.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString( op.getProblemDescription());
//			  condition1.setSubject(new Reference(patientref));
//			  condition1.getCode().addCoding(new Coding("http://snomed.info/sct", op.getProblemDiagnosisCode(), op.getDiagnosticCertainity())).setText(op.getProblemDescription());
//
//			  BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
//				bundleEntry1.setFullUrl("Condition/"+condition1.getId());
//				bundleEntry1.setResource(condition1);
//				  lb.add(bundleEntry1);
//
//
//				  DiagnosisComponent dc =new DiagnosisComponent() ;
//					dc.getCondition().setReference("Condition/"+condition1.getId());
//
//					en.addDiagnosis(dc);
//
//		}

            BundleEntryComponent bundleEntry2 = new BundleEntryComponent();
            bundleEntry2.setFullUrl("Encounter/"+en.getId());
            bundleEntry2.setResource(en);
            lb.add(bundleEntry2);

            composition.setEncounter(new Reference().setReference("Encounter/"+en.getId()));
        }

        patientref = composition.getSubject().getReference().toString();

        List<SectionComponent> section =  composition.getSection();


        if(medicalrecords.getSymptoms()!= null)
        {

            Condition condition1 = new Condition();
            condition1.setId("Condition-"+new Random().nextInt(900));
            condition1.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
            condition1.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString(medicalrecords.getPattern());
            condition1.setSubject(new Reference(patientref));
            condition1.getCode().addCoding(new Coding()).setText(medicalrecords.getSymptoms());

//            Boolean sectionPresent = false;
//            for(SectionComponent sec : section) {
//
//
//                if(sec.getTitle().equalsIgnoreCase("Chief complaints")) {
//                    if(op.getIllnessSummary()!=null) {
//                        sec.getText().setDivAsString(op.getIllnessSummary().toString());
//                        sec.getText().setStatusAsString("generated");
//                    }
//                    sec.addEntry(new Reference().setReference("Condition/"+condition1.getId()));
//                    sectionPresent = true;
//                }
//            }
//
//            if(!sectionPresent)
//            {
//                SectionComponent sectionCheifComplaints = new SectionComponent();
//                sectionCheifComplaints.setTitle("Chief complaints");
//                sectionCheifComplaints.getText().setDivAsString(op.getIllnessSummary().toString());
//                sectionCheifComplaints.getText().setStatusAsString("generated");
//                sectionCheifComplaints.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "422843007", "Chief complaint section"))).
//                        addEntry(new Reference().setReference("Condition/"+condition1.getId()));
//                section.add(sectionCheifComplaints);
//
//            }

            BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
            bundleEntry1.setFullUrl("Condition/"+condition1.getId());
            bundleEntry1.setResource(condition1);
            lb.add(bundleEntry1);

        }

        if(medicalrecords.getMedicine()!=null )
        {
            MedicationRequest medicationRequest = new MedicationRequest();
            medicationRequest.setId("MedicationRequest-"+new Random().nextInt(900));
            medicationRequest.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/MedicationRequest");
            medicationRequest.setStatus(MedicationRequestStatus.ACTIVE);
            medicationRequest.setIntent(MedicationRequestIntent.ORDER);
            medicationRequest.setMedication(new CodeableConcept().setText(medicalrecords.getMedicine()));
            medicationRequest.setSubject(new Reference().setReference(patientref));
            medicationRequest.setAuthoredOnElement(new DateTimeType(medicalrecords.getDate()));
            medicationRequest.setRequester(new Reference().setReference("Practitioner/Practitioner-01").setDisplay(medicalrecords.getDoctor().getName()));
            medicationRequest.getReasonReference().add(new Reference().setReference("Condition/Condition-01"));
            medicationRequest.addDosageInstruction(new Dosage().setText("One tablet at once").addAdditionalInstruction(new CodeableConcept().setText(medicalrecords.getTimings())).
                    //	setTiming(new Timing().setRepeat(new TimingRepeatComponent().setFrequency(1).setPeriod(1).setPeriodUnit(UnitsOfTime.D))).
                            setRoute(new CodeableConcept().setText(medicalrecords.getInstruction())).
                    setMethod(new CodeableConcept(new Coding("http://snomed.info/sct", "421521009", "Swallow"))));




//            Boolean sectionPresent = false;
//            for(SectionComponent sec : section) {
//
//
//                if(sec.getTitle().equalsIgnoreCase("Medications")) {
//                    if(op.getClinicalHistory()!=null) {
//                        sec.getText().setDivAsString(op.getClinicalHistory().toString());
//                        sec.getText().setStatusAsString("generated");
//                    }
//                    sec.addEntry(new Reference().setReference("MedicationRequest/"+medicationRequest.getId()));
//                    sectionPresent = true;
//                }
//            }
//
//            if(!sectionPresent)
//            {
//
//                SectionComponent section5 = new SectionComponent();
//                section5.setTitle("Medications");
//                section5.getText().setDivAsString(op.getClinicalHistory().toString());
//                section5.getText().setStatusAsString("generated");
//                section5.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "721912009", "Medication summary document"))).
//                        addEntry(new Reference().setReference("MedicationRequest/"+medicationRequest.getId()));
//
//                section.add(section5);
//
//            }


            BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
            bundleEntry1.setFullUrl("MedicationRequest/"+medicationRequest.getId());
            bundleEntry1.setResource(medicationRequest);
            lb.add(bundleEntry1);
        }




//        if(op.getServiceName()!= null && op.getServiceName().equalsIgnoreCase("FollowUp"))
//        {
//
//            Narrative nr = new Narrative();
//            nr.setDivAsString(op.getServiceName());
//            Appointment appointment = new Appointment();
//            appointment.setId("Appointment-"+new Random().nextInt(900));
//            appointment.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Appointment");
//            appointment.setStatus(AppointmentStatus.BOOKED);
//            appointment.getParticipant().add(new AppointmentParticipantComponent().setActor(new Reference().setReference(patientref)).
//                    setStatus(ParticipationStatus.ACCEPTED).setActor(new Reference().setReference("Practitioner/Practitioner-01")).setStatus(ParticipationStatus.ACCEPTED));
//
//            appointment.setAppointmentType(new CodeableConcept(new Coding("http://snomed.info/sct", "185389009", "Follow-up visit"))).setText(nr);
//
//            appointment.setStartElement(new InstantType(op.getFollowUpDate()));
//
//            appointment.setEndElement(new InstantType(op.getFollowUpDate()));
//
//            if(op.getReferredDoctor()!=null) {
//                appointment.setDescription(op.getReferredDoctor().toString());
//            }
//
//            Boolean sectionPresent = false;
//            for(SectionComponent sec : section) {
//                if(sec.getTitle().equalsIgnoreCase("Follow Up")) {
//
//                    sec.addEntry(new Reference().setReference("Appointment/"+appointment.getId()));
//                    sectionPresent = true;
//                }
//            }
//
//            if(!sectionPresent)
//            {
//                SectionComponent sectionFollowup = new SectionComponent();
//                sectionFollowup.setTitle("Follow Up");
//                sectionFollowup.setCode(new CodeableConcept(new Coding("http://snomed.info/sct", "736271009", "Outpatient care plan"))).
//                        addEntry(new Reference().setReference("Appointment/"+appointment.getId()));
//                section.add(sectionFollowup);
//            }
//
//            BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
//            bundleEntry1.setFullUrl("Appointment/"+appointment.getId());
//            bundleEntry1.setResource(appointment);
//            lb.add(bundleEntry1);
//
//        }
//
//        if(op.getProblemDiagnosis()!=null)
//        {
//
//            Condition condition1 = new Condition();
//            condition1.setId("Condition-"+new Random().nextInt(900));
//            condition1.getMeta().addProfile("https://nrces.in/ndhm/fhir/r4/StructureDefinition/Condition");
//            condition1.getText().setStatus(NarrativeStatus.GENERATED).setDivAsString( op.getProblemDescription());
//            condition1.setSubject(new Reference(patientref));
//            condition1.getCode().addCoding(new Coding("http://snomed.info/sct", op.getProblemDiagnosisCode(), op.getDiagnosticCertainity())).setText(op.getProblemDescription());
//
//            BundleEntryComponent bundleEntry1 = new BundleEntryComponent();
//            bundleEntry1.setFullUrl("Condition/"+condition1.getId());
//            bundleEntry1.setResource(condition1);
//            lb.add(bundleEntry1);
//
//            for(int i=0 ; i<lb.size();i++)
//            {
//
//                BundleEntryComponent bec = lb.get(i);
//                if(bec.getResource().getResourceType().toString().equalsIgnoreCase("Encounter"))
//                {
//                    Encounter encounter = (Encounter)bec.getResource();
//
//                    DiagnosisComponent dc =new DiagnosisComponent() ;
//                    dc.getCondition().setReference("Condition/"+condition1.getId());
//
//                    encounter.addDiagnosis(dc);
//
//
//                }
//            }
//
//        }




        return composition;
    }

}
