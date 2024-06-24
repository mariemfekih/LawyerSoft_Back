package com.example.gestion_user.controllers;


import com.example.gestion_user.entities.Action;
import com.example.gestion_user.services.ActionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/Action")
public class ActionController {

    @Autowired
    ActionService actionService ;



    @PostMapping
    public ResponseEntity<Action> addAction(@RequestBody Action a){
        Action action = actionService.addAction(a);
        return ResponseEntity.status(HttpStatus.CREATED).body(action);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Action> updateAction(@PathVariable Long id,@RequestBody Action action){
        Action existingFee = actionService.getActionById(id);
        if (existingFee == null) {
            return ResponseEntity.notFound().build();
        }
        Action actionUpdated = actionService.updateAction(id,action);
        return ResponseEntity.ok().body(actionUpdated);
    }
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{action-id}")
    public ResponseEntity<Void> deleteAction(@PathVariable("action-id") Long actionId) {
        actionService.deleteAction(actionId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping
    public ResponseEntity<List<Action>> getActions() {
        List<Action> listActions = actionService.getActions();
        return ResponseEntity.ok().body(listActions);
    }

    @GetMapping("/{action-id}")
    public ResponseEntity<Action> getFeeById(@PathVariable("action-id") Long actionId){
        Action a = actionService.getActionById(actionId);
        if (a != null) {
            return ResponseEntity.ok().body(a);
        } else {
            return ResponseEntity.notFound().build();
        }

    }
    @PostMapping("/by-ids")
    public List<Action> getActionsByIds(@RequestBody List<Long> ids) {
        return actionService.getActionsByIds(ids);
    }



}
