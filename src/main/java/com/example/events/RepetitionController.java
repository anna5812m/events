package com.example.clientserver;

import com.example.clientserver.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class RepetitionController {
    private CommonRepository<Repetition> repository;

    @Autowired
    public RepetitionController(CommonRepository<Repetition> repository) {
        this.repository = repository;
    }

    @GetMapping("/client-server")
    public ResponseEntity<Iterable<Repetition>> getRepetitions(){
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/client-server/{id}")
    public ResponseEntity<Repetition> getRepetitionById(@PathVariable String id){
        return ResponseEntity.ok(repository.findById(id));
    }

    @PatchMapping("/client-server/{id}")
    public ResponseEntity<Repetition> setCompleted(@PathVariable String id){
        Repetition result = repository.findById(id);
        result.setCompleted(true);
        repository.save(result);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(result.getId()).toUri();
        ResponseEntity<Repetition> build = ResponseEntity.ok().header
                ("Location", location.toString()).build();
        return build;
    }

    @RequestMapping(value="/client-server", method = {RequestMethod.POST,
            RequestMethod.PUT})
    public ResponseEntity<?> createRepetition(@RequestBody Repetition toDo,
                                        Errors errors){
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().
                    body(RepetitionValidationErrorBuilder.fromBindingErrors(errors));
        }
        Repetition result = repository.save(toDo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/client-server/{id}")
    public ResponseEntity<Repetition> deleteToDo(@PathVariable String id){
        repository.delete(RepetitionBuilder.create().withId(id).build());
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/client-server")
    public ResponseEntity<Repetition> deleteToDo(@RequestBody Repetition toDo){
        repository.delete(toDo);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RepetitionValidationError handleException(Exception exception) {
        return new RepetitionValidationError(exception.getMessage());
    }
}
