package com.bridgelabz.fundoonotesservice.service;

import com.bridgelabz.fundoonotesservice.dto.NoteServiceDTO;
import com.bridgelabz.fundoonotesservice.exception.NotesException;
import com.bridgelabz.fundoonotesservice.model.NoteServiceModel;
import com.bridgelabz.fundoonotesservice.repository.INoteServiceRepository;
import com.bridgelabz.fundoonotesservice.util.Response;
import com.bridgelabz.fundoonotesservice.util.TokenUtil;
import com.bridgelabz.fundoonotesservice.util.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteService implements INoteService {
    @Autowired
    INoteServiceRepository notesRepository;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    MailService mailService;

    @Autowired
    RestTemplate restTemplate;


    @Override
    public Response addNotes(@Valid NoteServiceDTO notesDTO, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
                NoteServiceModel noteServiceModel = new NoteServiceModel(notesDTO);
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

    /*
    Purpose : To get Notes by id
     */
    @Override
    public Response getNotesById(long id) {
            Optional<NoteServiceModel> notesModel = notesRepository.findById(id);
            return new Response("Notes Found With id..", 200, notesModel.get());
    }

    /*
    Purpose : To get Notes by Token
     */
    @Override
    public List<NoteServiceModel> getAllNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findById(userId);
        if (notesModel.isPresent()) {
            List<NoteServiceModel> getAllNotes = notesRepository.findAll();
            if (getAllNotes.size() > 0)
                return getAllNotes;
            else
                throw new NotesException(400, "No Data Found");
            }
        }
        throw new NotesException(400, "Notes not found");
    }

    /*
        Purpose : To update Notes
         */
    @Override
    public Response updateNotes(long id, @Valid NoteServiceDTO notesDTO, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()) {
            notesModel.get().setTitle(notesDTO.getTitle());
            notesModel.get().setDescription(notesDTO.getDescription());
//            notesModel.get().setLabelId(notesDTO.getLabelId());
//            notesModel.get().setEmailId(notesDTO.getEmailId());
//            notesModel.get().setColor(notesDTO.getColor());
//            notesModel.get().setReminderTime(notesDTO.getReminderTime());
            notesRepository.save(notesModel.get());
            return new Response("Notes Updated Successfully", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }



    @Override
    public Response trash(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
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
        }
        throw new NotesException(400, "Notes Cannot restored");
    }


    @Override
    public List<NoteServiceModel> getTrashNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
            List<NoteServiceModel> noteServiceModels = notesRepository.findByUserId(userId);
            List<NoteServiceModel> trashNotes = noteServiceModels.stream().filter(notes -> notes.isTrash() == true)
                    .collect(Collectors.toList());
            return trashNotes;
        }
        throw new NotesException(400, "Token Wrong");
    }


    @Override
    public Response removeTrash(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()) {
            notesModel.get().setTrash(false);
            notesRepository.save(notesModel.get());
            return new Response("Notes Restored", 200, null);
            }
        }
        throw new NotesException(400, "Trash is Empty");
    }


    @Override
    public Response delete(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()) {
            notesRepository.delete(notesModel.get());
            return new Response("Notes Deleted", 200, null);
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }


    @Override
    public Response archiveNote(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()){
            notesModel.get().setArchive(!notesModel.get().isArchive());
            notesModel.get().setArchive(false);
            notesRepository.save(notesModel.get());
            return new Response("Notes Not Archived", 200, null);
        } else {
            return new Response("Notes Archived", 200, null);
            }
        }
        throw new NotesException(400, "Token is Wrong");
//            if (notesModel.get().isArchive()==true){
//                notesModel.get().setArchive(false);
//                notesRepository.save(notesModel.get());
//                return new Response("Notes not archived", 200, null);
//            } else {
//                notesModel.get().setArchive(true);
//                notesRepository.save(notesModel.get());
//                return new Response("Notes Archived", 200, null);
//            }
    }



    @Override
    public List<NoteServiceModel> getArchiveNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
            List<NoteServiceModel> notes = notesRepository.findByUserId(userId);
            List<NoteServiceModel> archiveNotes = notes.stream().filter(note -> note.isArchive() == true)
                    .collect(Collectors.toList());
            return archiveNotes;
        }
        throw new NotesException(400, "Invalid Token");
    }



    @Override
    public Response pinNotes(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> isIdPresent = notesRepository.findByUserIdAndId(userId, id);
        if (isIdPresent.isPresent()) {
            isIdPresent.get().setPin(true);
            notesRepository.save(isIdPresent.get());
            return new Response("Notes pinned", 200, isIdPresent.get());
             }
        }
        throw new NotesException(400, "Notes Not Found");
    }


    @Override
    public Response unPinNotes(Long id, String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> isNotesPresent = notesRepository.findByUserIdAndId(userId, id);
        if (isNotesPresent.isPresent()) {
            isNotesPresent.get().setPin(false);
            notesRepository.save(isNotesPresent.get());
            return new Response("Notes pinned", 200, isNotesPresent.get());
            }
        }
        throw new NotesException(400, "Notes Not Found");
    }




    @Override
    public List<NoteServiceModel> getPinnedNotes(String token) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
            Long userId = tokenUtil.decodeToken(token);
            List<NoteServiceModel> notes = notesRepository.findByUserId(userId);
            List<NoteServiceModel> pinNotes = notes.stream().filter(note -> note.isPin() == true)
                    .collect(Collectors.toList());
            return pinNotes;
//        for (NotesModel note : notes) {
//            if (note.isPin()){
//                List<NotesModel> list = new ArrayList<>();
//                list.add(note);
//                return list;
//            }
//        }
//        throw new NotesException(400, "Not Pinned");
        }
        throw new NotesException(400, "Not Pinned");
    }


    @Override
    public Response addColour(String token, Long id, String colour) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);
        if (isUserPresent) {
        Long userId = tokenUtil.decodeToken(token);
        Optional<NoteServiceModel> notesModel = notesRepository.findByUserIdAndId(userId, id);
        if (notesModel.isPresent()) {
            notesModel.get().setColor(colour);
            notesRepository.save(notesModel.get());
            return new Response("Notes colour set", 200, null);
            }
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
    public Response addCollaborator(String token, String email, Long id, List<String> collaborator) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validateEmail/" + email, boolean.class);
       if (isUserPresent){
           Long userId = tokenUtil.decodeToken(token);
            Optional<NoteServiceModel> isNotesPresent = notesRepository.findById(userId);
            if (isNotesPresent.isPresent()){
                List<String> collabList = new ArrayList<>();
                collaborator.stream().forEach(collab ->{
                    UserResponse isEmailPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validateEmail/" + email, UserResponse.class);
                    if (isEmailPresent != null){
                        collabList.add(collab);
                    } else {
                        throw new NotesException(400, "Email Not Present");
                    }
                });
                Optional<NoteServiceModel> notes = notesRepository.findById(id);
                if (collabList.size()>0){
                    notes.get().setCollaborator(collabList);
                    notesRepository.save(notes.get());
                    NoteServiceModel notes1= new NoteServiceModel();
                    notes1.setTitle(notes.get().getTitle());
                    notes1.setUserId(notes.get().getUserId());
                    notesRepository.save(notes1);
                    return new Response("Added", 200, notes.get());
                }
            }
        }
        throw new NotesException(400, "Not Found");
    }


    @Override
    public NoteServiceModel setRemainder(String remainderTime, String token, Long id) {
        boolean isUserPresent = restTemplate.getForObject("http://USER-SERVICE:8081/user/validate/" + token, Boolean.class);

        if (isUserPresent) {
            Optional<NoteServiceModel> isNotesPresent = notesRepository.findById(id);
            if (isNotesPresent.isPresent()) {
                LocalDate today = LocalDate.now();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-mm-yyyy HH:mm:ss");
                LocalDate remainder = LocalDate.parse(remainderTime, dateTimeFormatter);
                if (remainder.isBefore(today)) {
                    throw new NotesException(400, "Invalid Remainder time");
                }
                isNotesPresent.get().setReminderTime(remainderTime);
                notesRepository.save(isNotesPresent.get());
            }
            throw new NotesException(400, "Notes Not Present");
        }
        throw new NotesException(400, "Invalid Token");
    }
}

