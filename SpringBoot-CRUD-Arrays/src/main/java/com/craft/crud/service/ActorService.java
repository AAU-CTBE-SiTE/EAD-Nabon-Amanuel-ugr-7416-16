package com.craft.crud.service;

import com.craft.crud.model.Actor;
import org.springframework.stereotype.Service;

@Service
public class ActorService {

    private Actor[] actors;
    private int actorCount;
    private int nextId;

    public ActorService() {
        this.actors = new Actor[10];
        this.actorCount = 0;
        this.nextId = 1;

        // Initialize array with the starting actor record
        actors[actorCount] = new Actor(nextId, "John Snow", 30, "British");
        actorCount++;
        nextId++;
    }

    public Actor[] getAllActors() {
        Actor[] activeActors = new Actor[actorCount];
        for (int i = 0; i < actorCount; i++) {
            activeActors[i] = actors[i];
        }
        return activeActors;
    }

    public Actor getActorById(int id) {
        for (int i = 0; i < actorCount; i++) {
            if (actors[i].getId() == id) {
                return actors[i];
            }
        }
        return null;
    }

    public Actor createActor(Actor actorInput) {
        if (actorCount == actors.length) {
            expandCapacity();
        }

        Actor created = new Actor(
                nextId,
                actorInput.getName().trim(),
                actorInput.getAge(),
                actorInput.getNationality().trim()
        );
        actors[actorCount] = created;
        actorCount++;
        nextId++;
        return created;
    }

    public Actor updateActor(int id, Actor updatedActor) {
        for (int i = 0; i < actorCount; i++) {
            if (actors[i].getId() == id) {
                actors[i].setName(updatedActor.getName().trim());
                actors[i].setAge(updatedActor.getAge());
                actors[i].setNationality(updatedActor.getNationality().trim());
                return actors[i];
            }
        }
        return null;
    }

    public Actor patchActor(int id, Actor partialActor) {
        for (int i = 0; i < actorCount; i++) {
            if (actors[i].getId() == id) {
                if (partialActor.getName() != null) {
                    actors[i].setName(partialActor.getName().trim());
                }
                if (partialActor.getAge() > 0) {
                    actors[i].setAge(partialActor.getAge());
                }
                if (partialActor.getNationality() != null) {
                    actors[i].setNationality(partialActor.getNationality().trim());
                }
                return actors[i];
            }
        }
        return null;
    }

    public boolean deleteActor(int id) {
        for (int i = 0; i < actorCount; i++) {
            if (actors[i].getId() == id) {
                // Shift remaining elements left so there are no gaps
                for (int j = i; j < actorCount - 1; j++) {
                    actors[j] = actors[j + 1];
                }
                actors[actorCount - 1] = null;
                actorCount--;
                return true;
            }
        }
        return false;
    }

    private void expandCapacity() {
        Actor[] expanded = new Actor[actors.length * 2];
        for (int i = 0; i < actorCount; i++) {
            expanded[i] = actors[i];
        }
        actors = expanded;
    }
}
