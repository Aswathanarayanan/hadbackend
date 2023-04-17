package com.example.hadbackend.bean.notify;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DataFlowNotifyStatusNoti {
 
    private String sessionStatus;
    private String hipId;
    private ArrayList<DataFlowNotifystatusResp> statusResponses;
}
