package com.example.gestion_user.services;

import com.example.gestion_user.entities.Action;

import java.util.List;

public interface ActionService {

    Action addAction(Action action) ;
    Action updateAction (Long id , Action action) ;
    void deleteAction (Long idAction) ;
    List<Action> getActions() ;
    Action getActionById (Long idAction);

    List<Action> getActionsByIds(List<Long> ids);
}
