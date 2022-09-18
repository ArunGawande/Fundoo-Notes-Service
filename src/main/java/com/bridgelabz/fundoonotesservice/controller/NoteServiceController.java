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

    /*
     Purpose : NotesController to process Data API
     version : 1.0
    */
@RestController
@RequestMapping("/notes")
public class NoteServiceController {
    @Autowired
    INoteService noteService;

        /*
         *@Purpose:to add Notes into the Notes Repository
         * @Param :NotesDTO
         */

    @PostMapping(value = "/addNotes")
    ResponseEntity<Response> addNotes(@Valid @RequestBody NoteServiceDTO noteServiceDTO, @RequestHeader String token) {
        Response response = noteService.addNotes(noteServiceDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
        *@Purpose : get list of Notes in the Notes Repository using id
         @Param  : id
        */

    @GetMapping("/getNotes/{id}")
    ResponseEntity<Response> getNotes(@PathVariable long id){
        Response response = noteService.getNotesById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

          /*
           *@Purpose : get list of Notes in the Notes Repository using id
            @Param  : token
           */

    @GetMapping("/getAllNotes")
    public List<NoteServiceModel> getAllNotes(@RequestHeader String token){

        return noteService.getAllNotes(token);
    }

        /*
         @Purpose : update Notes in the notes Repository
         @Param :   NotesDTO,  noteId and token
         */

    @PutMapping("updateNotes/{id}")
    ResponseEntity<Response> updateNotes(@Valid @RequestBody NoteServiceDTO noteServiceDTO, @PathVariable long id, @RequestHeader String token ){
        Response response = noteService.updateNotes(id, noteServiceDTO, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
            @Purpose :  add deleted Notes in the trash in the notes Repository
            @Param :    noteId and token
            */
    @DeleteMapping("trashNotes/{id}")
    ResponseEntity<Response> trash(@PathVariable Long id, @RequestHeader String token){

        Response response = noteService.trash(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
          @Purpose : get Trashed Notes in the notes Repository
          @Param :   token
          */
    @GetMapping("/getTrashNotes")
   public List <NoteServiceModel> getTrashNotes(@RequestHeader String token){

        return noteService.getTrashNotes(token);
    }

            /*
               @Purpose :  deleted Notes from the notes Repository
               @Param :    noteId and token
               */
    @DeleteMapping("deleteNotes/{id}")
    ResponseEntity<Response> delete(@PathVariable Long id, @RequestHeader String token){

        Response response = noteService.delete(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

          /*
           @Purpose :  Archive Notes in the notes Repository
           @Param :    noteId and token
            */
    @PutMapping("/archive")
    public ResponseEntity<Response> moveToArchive(@PathVariable Long id, @RequestHeader String token) {
        Response response = noteService.archiveNote(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

            /*
              @Purpose : get Archive Notes in the notes Repository
              @Param :    noteId and token
               */
    @GetMapping("/getArchive")
    public List<NoteServiceModel> getArchive(@RequestHeader String token){

        return noteService.getArchiveNotes(token);
    }

          /*
             @Purpose :  pin Notes in the notes Repository
             @Param :   noteId and token
             */
    @PutMapping("/pinNotes")
    public ResponseEntity<Response> pinNotes(@PathVariable Long id, @RequestHeader String token){
        Response response = noteService.pinNotes(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
                /*
                 @Purpose : unpin Notes in the notes Repository
                 @Param :   noteId and token
                  */
        @PutMapping("/unPinNotes")
        public ResponseEntity<Response> unPinNotes(@PathVariable Long id, @RequestHeader String token){
            Response response = noteService.unPinNotes(id, token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        /*
         @Purpose : get pin Notes in the notes Repository
         @Param :    noteId and token
          */
    @GetMapping("/getPin")
    public List<NoteServiceModel> getPin(@RequestHeader String token){

        return noteService.getPinnedNotes(token);
    }

        /*
             @Purpose :  remove Trashed Notes in the notes Repository
             @Param :    noteId and token
             */
    @DeleteMapping("/removeTrash/{id}")
    ResponseEntity<Response> removeTrash(@PathVariable Long id, @RequestHeader String token){

        Response response = noteService.removeTrash(id, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
          @Purpose : add colour to the Notes in the notes Repository
          @Param :    noteId, token and colour
          */

    @PutMapping("/addColour")
    public ResponseEntity<Response> addColour(@RequestHeader String token, @PathVariable Long id, @RequestHeader String colour) {
        Response response = noteService.addColour(token, id, colour);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        /*
             @Purpose :  get colour  Notes from the notes Repository by id
             @Param :    noteId
             */
    @GetMapping("/getColour")
    ResponseEntity<Response> getColour(@PathVariable Long id) {
        Response response = noteService.getColour(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
        @PostMapping(value = "/addCollaborator")
        ResponseEntity<Response> addCollaborator(@RequestHeader String token, @RequestParam String email, @PathVariable Long id, @RequestParam List<String> collaborator ) {
            Response response = noteService.addCollaborator(token, email, id, collaborator);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        
        @PostMapping(value = "/setRemainder/{id}")
        ResponseEntity<Response> setRemainder(@RequestHeader String token, @PathVariable Long id, @RequestParam String remainderTime) {
        NoteServiceModel noteServiceModel = noteService.setRemainder(remainderTime, token, id);
            Response response = new Response("Remainder set Successfully", 400, noteServiceModel);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
}
