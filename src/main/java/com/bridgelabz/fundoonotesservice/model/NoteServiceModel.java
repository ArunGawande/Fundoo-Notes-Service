package com.bridgelabz.fundoonotesservice.model;

import com.bridgelabz.fundoonotesservice.dto.NoteServiceDTO;

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
    private LocalDateTime reminderTime;
    @Column
    @ElementCollection(targetClass = String.class)
    List<String> collaborator;

    public NoteServiceModel(NoteServiceDTO noteServiceDTO) {
        this.title = noteServiceDTO.getTitle();
        this.description = noteServiceDTO.getDescription();
        this.userId = noteServiceDTO.getUserId();
        this.registerDate = noteServiceDTO.getRegisterDate();
        this.updateDate = noteServiceDTO.getUpdateDate();
        this.trash = noteServiceDTO.isTrash();
        this.isArchive = noteServiceDTO.isArchive();
        this.pin = noteServiceDTO.isPin();
        this.labelId = noteServiceDTO.getLabelId();
        this.emailId = noteServiceDTO.getEmailId();
        this.color = noteServiceDTO.getColor();
        this.reminderTime = noteServiceDTO.getReminderTime();
        this.collaborator = noteServiceDTO.getCollaborator();
    }

    public NoteServiceModel() {

    }
}
