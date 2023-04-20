package com.example.hadbackend.bean.fhirBundle;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class EntryResource {
    private String resourceType;
    private String id;
    private EntryResourceMeta meta;
    private String language;
    private EntryResourcetext text;
    private Identifier identifier;
    private String status;
    private EntryResourcetype type;
    private EntryResourcesubject subject;
    private EntryResourceEncounter encounter;
    private String date;
    private ArrayList<EntryResourceCustodian> author;
    private String title;
    private EntryResourceCustodian custodian;
     
}
