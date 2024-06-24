package com.example.gestion_user.services.servicesImpl;


import com.example.gestion_user.entities.Action;
import com.example.gestion_user.exceptions.NotFoundException;
import com.example.gestion_user.repositories.ActionRepository;
import com.example.gestion_user.services.ActionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ActionServiceImpl implements ActionService {

    @Autowired
    ActionRepository actionRepository ;

    @Override
    public Action addAction(Action a) {
        Action action = new Action();
        action.setAmount(a.getAmount());
        action.setReference(a.getReference());
        action.setDescription(a.getDescription());
        try {
            return actionRepository.save(action);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to create court: " + ex.getMessage());
        }    }

    @Override
    public Action updateAction( Long id, Action action) {
        Action existingFee = actionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fee not found with id: " + id));

        // Update the fields of the existing Fee entity with values from the DTO
        existingFee.setAmount(action.getAmount());
        existingFee.setReference(action.getReference());
        existingFee.setDescription(action.getDescription());


        // Save the updated Fee entity
        try {
            return actionRepository.save(existingFee);
        } catch (Exception ex) {
            throw new NotFoundException("Failed to update fee with id: " + id + ". " + ex.getMessage());
        }    }

    @Override
    public void deleteAction(Long idAction) {
        actionRepository.deleteById(idAction);
    }

    @Override
    public List<Action> getActions() {
        return actionRepository.findAll();    }

    @Override
    public Action getActionById(Long idAction) {
        return actionRepository.findById(idAction).get() ;    }

    @Override
    public List<Action> getActionsByIds(List<Long> ids) {
        return actionRepository.temp(ids);
    }

}
