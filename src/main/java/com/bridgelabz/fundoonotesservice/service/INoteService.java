package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.dto.NoteServiceDTO;
import com.bridgelabz.fundoonotesservice.model.NoteServiceModel;
import com.bridgelabz.fundoonotesservice.util.Response;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public interface INoteService {
    Response addNotes(@Valid NoteServiceDTO notesDTO, String token);

    Response getNotesById(long id);
    List<NoteServiceModel> getAllNotes(String token);

    Response updateNotes(long id, @Valid NoteServiceDTO notesDTO, String token);

    Response trash(Long id, String token);

    Response delete(Long id, String token);

    Response archiveNote(Long id, String token);


    List<NoteServiceModel> getTrashNotes(String token);


    List<NoteServiceModel> getArchiveNotes(String token);

    List<NoteServiceModel> getPinnedNotes(String token);

    Response removeTrash(Long id, String token);

    Response addColour(String token, Long id, String colour);

    Response getColour(Long id);

    Response pinNotes(Long id, String token);

    Response unPinNotes(Long id, String token);

    Response addCollaborator(String token, String email, Long id, List<String> collaborator);

    NoteServiceModel setRemainder(String remainderTime, String token, Long id);
}
