package com.example.Bot.repositories;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.Notebook;
import com.example.Bot.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotebookRepository extends CrudRepository<Notebook, Long> {
    List<Notebook> findByUserAndChannelAndStatus(User user, Channel channel, String status);

    List<Notebook> findByStatus(String status);
}
