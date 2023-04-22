package com.example.hadbackend.bean.dataTransfer;

import java.util.List;

import com.example.hadbackend.bean.request.CmRequestKeyMaterial;
import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DataPush {
    
    
    private int pageNumber;
    private int pageCount;
    private String transactionId;
    private List<DataEntries> entries;
    private CmRequestKeyMaterial keyMaterial;
        
}
