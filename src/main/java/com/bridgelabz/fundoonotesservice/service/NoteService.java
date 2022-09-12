package com.bridgelabz.fundoonotesservice.service;


import com.bridgelabz.fundoonotesservice.dto.NoteServiceDTO;
import com.bridgelabz.fundoonotesservice.exception.NotesException;
import com.bridgelabz.fundoonotesservice.model.NoteServiceModel;
import com.bridgelabz.fundoonotesservice.repository.NoteServiceRepository;
import com.bridgelabz.fundoonotesservice.util.Response;
import com.bridgelabz.fundoonotesservice.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService implements INoteService {
    @Autowired
    NoteServiceRepository notesRepository;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    MailService mailService;
    @Override
    public Response addNotes(@Valid NoteServiceDTO noteServiceDTO, String token) {
       Long userId = tokenUtil.decodeToken(token);
       if (userId != 0){
           NoteServiceModel noteServiceModel = new NoteServiceModel(noteServiceDTO);
           noteServiceModel.setUserId(userId);
           noteServiceModel.setRegisterDate(LocalDateTime.now());
           notesRepository.save(noteServiceModel);
           String body = "Mentor added Successfully with id  :" + noteServiceModel.getId();
           String subject = "Mentor added Successfully....";
           mailService.send(noteServiceModel.getEmailId(), body, subject);
           return new Response("Mentor Added Successfully", 200, noteServiceModel);
       }
        throw new NotesException(400, "Token is Wrong");
    }

    @Override
    public Response getNotesById(long id) {
        Optional<NoteServiceModel> notesModel = notesRepository.findById(id);
        return new Response("Notes Found With id..", 200, notesModel.get());
    }

    @Override
    public List<NoteServiceModel> getAllNotes(String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findById(userId);
        if (notesModel.isPresent()){
            List<NoteServiceModel> getAllNotes = notesRepository.findAll();
            if (getAllNotes.size() > 0)
                return getAllNotes;
            else
                throw new NotesException(400, "No Data Found");
        }
        throw new NotesException(400, "Notes not found");
    }


    @Override
    public Response updateNotes(long id, NoteServiceDTO noteServiceDTO, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesModel.get().setTitle(noteServiceDTO.getTitle());
            notesModel.get().setDescription(noteServiceDTO.getDescription());
            notesModel.get().setRegisterDate(noteServiceDTO.getRegisterDate());
            notesModel.get().setLabelId(noteServiceDTO.getLabelId());
            notesModel.get().setEmailId(noteServiceDTO.getEmailId());
            notesModel.get().setColor(noteServiceDTO.getColor());
            notesModel.get().setReminderTime(noteServiceDTO.getReminderTime());
            notesRepository.save(notesModel.get());
            return new Response("Notes Updated Successfully", 200, null);
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response trash(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            if (notesModel.get().isTrash()==true){
                notesModel.get().setTrash(false);
                notesRepository.save(notesModel.get());
                return new Response("Notes Restored from Trash", 200, null);
            } else {
                notesModel.get().setTrash(true);
                notesRepository.save(notesModel.get());
                return new Response("Notes Moved in Trash", 200, null);
            }
        }
        throw new NotesException(400, "Notes Cannot restored");
    }

    @Override
    public List<NoteServiceModel> getTrashNotes(String token) {
        Long userId = tokenUtil.decodeToken(token);
        List<NoteServiceModel> noteServiceModels = notesRepository.findByUserId(userId);
        List<NoteServiceModel> trashNotes = noteServiceModels.stream().filter(notes -> notes.isTrash()==true)
                .collect(Collectors.toList());
        return trashNotes;
    }

    @Override
    public Response removeTrash(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesModel.get().setTrash(false);
            notesRepository.save(notesModel.get());
            return new Response("Notes Restored", 200, null);
        }
        throw new NotesException(400, "Trash is Empty");
    }

    @Override
    public Response delete(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesRepository.delete(notesModel.get());
            return new Response("Notes Deleted", 200, null);
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response archiveNote(Long id, String token) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            if (notesModel.get().isArchive()==true){
                notesModel.get().setArchive(false);
                notesRepository.save(notesModel.get());
                return new Response("Notes not archived", 200, null);
            } else {
                notesModel.get().setArchive(true);
                notesRepository.save(notesModel.get());
                return new Response("Notes Archived", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found in Archive");
    }
    @Override
    public List<NoteServiceModel> getArchiveNotes(String token) {
        Long userId = tokenUtil.decodeToken(token);
        List<NoteServiceModel> notes = notesRepository.findByUserId(userId);
        List<NoteServiceModel> archiveNotes = notes.stream().filter(note -> note.isArchive()==true)
                .collect(Collectors.toList());
        return archiveNotes;
    }
    @Override
    public Response addColour(String token, Long id, String colour) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesModel.get().setColor(colour);
            notesRepository.save(notesModel.get());
            return new Response("Notes colour set", 200, null);
        }
        throw new NotesException(400, "Notes Not Found");
    }

    @Override
    public Response getColour(Long id) {
        Optional<NoteServiceModel> notesModel = notesRepository.findById(id);
        if (notesModel.isEmpty()){
            throw new NotesException(400, "Notes Not Found");
        } else {
            String colour = notesModel.get().getColor();
            return new Response("Note colour changed", 200, colour);
        }
    }

    @Override
    public List<NoteServiceModel> pinned(String token) {
        Long userId = tokenUtil.decodeToken(token);
        List<NoteServiceModel> notes = notesRepository.findByUserId(userId);
        for (NoteServiceModel note : notes) {
            if (note.isPin()){
                List<NoteServiceModel> list = new ArrayList<>();
                list.add(note);
                return list;
            }
        }
        throw new NotesException(400, "Not Pinned");
    }


}
