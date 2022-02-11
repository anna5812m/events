package com.example.events;

import com.example.events.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RepetitionController {

    private RepetitionRepository repetitionRepository;
 //   private CommonRepository<Repetition> repository;

    @Autowired
    public RepetitionController(RepetitionRepository repetitionRepository) {
        this.repetitionRepository = repetitionRepository;
    }

    @GetMapping("/events")
    public ResponseEntity<Iterable<Repetition>> getRepetitions(){
        return ResponseEntity.ok(repetitionRepository.findAll());
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Repetition> getRepetitionById(@PathVariable String id){
        Optional<Repetition> repetition = repetitionRepository.findById(id);
        if(repetition.isPresent())
            return ResponseEntity.ok(repetition.get());
        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/events/{id}")
    public ResponseEntity<Repetition> setCompleted(@PathVariable String id){
        Optional<Repetition> toDo = repetitionRepository.findById(id);
        if(!toDo.isPresent())
            return ResponseEntity.notFound().build();
        Repetition result = toDo.get();
        result.setCompleted(true) ;
        repetitionRepository.save(result);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
                buildAndExpand(result.getId()).toUri();
        return ResponseEntity.ok().header("Location",location.toString()).
                build();
    }

    @RequestMapping(value="/events", method = {RequestMethod.POST,
            RequestMethod.PUT})
    public ResponseEntity<?> createRepetition( @RequestBody Repetition repetition,
                                               Errors errors){

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().
                    body(RepetitionValidationErrorBuilder.fromBindingErrors(errors));
        }
        Repetition result = repetitionRepository.save(repetition);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().
                path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<Repetition> deleteRepetition(@PathVariable String id){
        repetitionRepository.delete(RepetitionBuilder.create().withId(id).build());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/events")
    public ResponseEntity<Repetition> deleteRepetition(@RequestBody Repetition repetition){
        repetitionRepository.delete(repetition);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public RepetitionValidationError handleException(Exception exception) {
        return new RepetitionValidationError(exception.getMessage());
    }
}
