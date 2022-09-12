package com.bridgelabz.fundoonotesservice.controller;

import com.bridgelabz.fundoonotesservice.dto.NoteServiceDTO;
import com.bridgelabz.fundoonotesservice.model.NoteServiceModel;
import com.bridgelabz.fundoonotesservice.service.INoteService;
import com.bridgelabz.fundoonotesservice.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/notes")
public class NoteServiceController {
    @Autowired
    INoteService noteService;



    @PostMapping(value = "/addNotes")
    ResponseEntity<Response> addNotes(@Valid @RequestBody NoteServiceDTO noteServiceDTO, @RequestHeader String token) {
        Response response = noteService.addNotes(noteServiceDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
        *   to get list of Notes in the Notes Repository using id
        */

    @GetMapping("/getNotes/{id}")
    ResponseEntity<Response> getNotes(@PathVariable long id){
        Response response = noteService.getNotesById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

          /*
           *@Purpose : to get list of Notes in the Notes Repository using id
            @Param  : token
           */

    @GetMapping("/getAllNotes")
    public List<NoteServiceModel> getAllNotes(@RequestHeader String token){
        return noteService.getAllNotes(token);
    }

        /*
         @Purpose : Able to update Notes in the notes Repository
         @Param :   NotesDTO, id and token
         */

    @PutMapping("updateNotes/{id}")
    ResponseEntity<Response> updateNotes(@Valid @RequestBody NoteServiceDTO noteServiceDTO, @PathVariable long id, @RequestHeader String token ){
        Response response = noteService.updateNotes(id, noteServiceDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("trashNotes/{id}")
    ResponseEntity<Response> trash(@PathVariable Long id, @RequestHeader String token){

        Response response = noteService.trash(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/getTrashNotes")
   public List <NoteServiceModel> getTrashNotes(@RequestHeader String token){

        return noteService.getTrashNotes(token);
    }

    @DeleteMapping("deleteNotes/{id}")
    ResponseEntity<Response> delete(@PathVariable Long id, @RequestHeader String token){

        Response response = noteService.delete(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping("/archive")
    public ResponseEntity<Response> moveToArchive(@PathVariable Long id, @RequestHeader String token) {
        Response response = noteService.archiveNote(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getArchive")
    public List<NoteServiceModel> getArchive(@RequestHeader String token){

        return noteService.getArchiveNotes(token);
    }
    @PutMapping("/addColour")
    public ResponseEntity<Response> addColour(@RequestHeader String token, @PathVariable Long id, @RequestHeader String colour) {
        Response response = noteService.addColour(token, id, colour);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getColour")
    ResponseEntity<Response> getColour(@PathVariable Long id) {
        Response response = noteService.getColour(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getPin")
    public List<NoteServiceModel> pin(@RequestHeader String token){
        return noteService.pinned(token);
    }


    @DeleteMapping("/removeTrash/{id}")
    ResponseEntity<Response> removeTrash(@PathVariable Long id, @RequestHeader String token){

        Response response = noteService.removeTrash(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



}
