package com.bridgelabz.fundoonotesservice.model;

import com.bridgelabz.fundoonotesservice.dto.NoteServiceDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notes")
@Data
public class NoteServiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String title;
    private String description;
    private long userId;
    private LocalDateTime registerDate;
    private LocalDateTime updateDate;
    private boolean trash;
    private boolean isArchive;
    private boolean pin;
    private Long labelId;
    private String emailId;
    private String color;
    private String reminderTime;
    @Column
    @ElementCollection(targetClass = String.class)
    List<String> collaborator;
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    private List<LabelModel> labelList;

    public NoteServiceModel(NoteServiceDTO noteServiceDTO) {
        this.title = noteServiceDTO.getTitle();
        this.description = noteServiceDTO.getDescription();
//        this.labelId = notesDTO.getLabelId();
//        this.emailId = notesDTO.getEmailId();
//        this.color = notesDTO.getColor();
//        this.reminderTime = notesDTO.getReminderTime();
//        this.collaborator = notesDTO.getCollaborator();
    }

    public NoteServiceModel() {

    }
}
