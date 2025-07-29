package com.pd.expense_tracker.controller;

import com.pd.expense_tracker.facade.UserFacadeImpl;
import com.pd.expense_tracker.model.UserIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserResource {

    private final UserFacadeImpl userFacade;

    @Autowired
    public UserResource(UserFacadeImpl userFacade) {
        this.userFacade = userFacade;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<UserIdentifier> addUser(@RequestBody UserIdentifier user) {
        UserIdentifier createdUser = userFacade.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // READ by id
    @GetMapping("/{id}")
    public ResponseEntity<UserIdentifier> getUser(@PathVariable String id) {
        return userFacade.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // READ all
    @GetMapping
    public ResponseEntity<List<UserIdentifier>> getAllUsers() {
        List<UserIdentifier> users = userFacade.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UserIdentifier> updateUser(@PathVariable String id, @RequestBody UserIdentifier newUserData) {
        try {
            UserIdentifier updatedUser = userFacade.updateUser(id, newUserData);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
