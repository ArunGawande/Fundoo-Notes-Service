package com.bridgelabz.fundoonotesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class NoteServiceDTO {
    private String title;
    private String description;
//    private Long labelId;
//    private String emailId;
//    private String color;
//    private String reminderTime;
//    List<String> collaborator;

}
