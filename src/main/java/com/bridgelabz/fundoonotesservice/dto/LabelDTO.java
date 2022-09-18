package com.bridgelabz.fundoonotesservice.dto;

import com.bridgelabz.fundoonotesservice.model.NoteServiceModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class LabelDTO {
    private String labelName;
    private List<NoteServiceModel> notes;
}
