package com.pd.expense_tracker.facade;

import com.pd.expense_tracker.model.UserIdentifier;
import com.pd.expense_tracker.repository.UserIdentifierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserFacadeImpl {

    @Autowired
    private UserIdentifierRepository userIdentifierRepository;

    // CREATE
    public UserIdentifier addUser(UserIdentifier user) {
        return userIdentifierRepository.save(user);
    }

    // READ
    public Optional<UserIdentifier> getUser(String id) {
        return userIdentifierRepository.findById(id);
    }

    public List<UserIdentifier> getAllUsers() {
        return userIdentifierRepository.findAll();
    }

    // UPDATE
    public UserIdentifier updateUser(String id, UserIdentifier newUserData) {
        UserIdentifier existing = userIdentifierRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existing.setName(newUserData.getName());
        return userIdentifierRepository.save(existing);
    }

    // DELETE
    public void deleteUser(String id) {
        userIdentifierRepository.deleteById(id);
    }
}
