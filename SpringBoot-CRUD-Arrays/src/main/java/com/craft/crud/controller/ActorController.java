package com.craft.crud.controller;

import com.craft.crud.model.Actor;
import com.craft.crud.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public ResponseEntity<Actor[]> getAllActors() {
        return ResponseEntity.ok(actorService.getAllActors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getActorById(@PathVariable int id) {
        Actor actor = actorService.getActorById(id);
        if (actor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Actor not found with ID: " + id);
        }
        return ResponseEntity.ok(actor);
    }

    @PostMapping
    public ResponseEntity<?> createActor(@RequestBody(required = false) Actor actor) {
        if (!hasValidFullFields(actor)) {
            return ResponseEntity.badRequest()
                    .body("Invalid input: name, positive age, and nationality are required.");
        }

        Actor created = actorService.createActor(actor);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateActor(@PathVariable int id,
                                         @RequestBody(required = false) Actor actor) {
        if (!hasValidFullFields(actor)) {
            return ResponseEntity.badRequest()
                    .body("Invalid input: name, positive age, and nationality are required.");
        }

        Actor updated = actorService.updateActor(id, actor);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Actor not found with ID: " + id);
        }
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchActor(@PathVariable int id,
                                        @RequestBody(required = false) Actor actor) {
        if (actor == null) {
            return ResponseEntity.badRequest()
                    .body("Request body is required for partial update.");
        }
        if (actor.getName() != null && actor.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Name cannot be blank.");
        }
        if (actor.getAge() < 0) {
            return ResponseEntity.badRequest().body("Age must be a positive number.");
        }
        if (actor.getNationality() != null && actor.getNationality().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Nationality cannot be blank.");
        }
        if (actor.getName() == null && actor.getNationality() == null && actor.getAge() <= 0) {
            return ResponseEntity.badRequest()
                    .body("At least one valid field (name, positive age, or nationality) must be provided.");
        }

        Actor updated = actorService.patchActor(id, actor);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Actor not found with ID: " + id);
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable int id) {
        boolean deleted = actorService.deleteActor(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Actor not found with ID: " + id);
        }
        return ResponseEntity.noContent().build();
    }

    private boolean hasValidFullFields(Actor actor) {
        if (actor == null) {
            return false;
        }
        if (actor.getName() == null || actor.getName().trim().isEmpty()) {
            return false;
        }
        if (actor.getAge() <= 0) {
            return false;
        }
        if (actor.getNationality() == null || actor.getNationality().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
