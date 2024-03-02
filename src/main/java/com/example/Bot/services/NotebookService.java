package com.example.Bot.services;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.Notebook;
import com.example.Bot.entities.User;
import com.example.Bot.repositories.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class NotebookService {

    private final NotebookRepository notebookRepository;

    @Autowired
    public NotebookService(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
    }

    public Notebook saveNotebook(Notebook notebook) {
        return notebookRepository.save(notebook);
    }

    public List<Notebook> getAllNotebooks() {
        return (List<Notebook>) notebookRepository.findAll();
    }

    public Notebook getNotebookById(Long id) {
        return notebookRepository.findById(id).orElse(null);
    }

    public void deleteNotebook(Long id) {
        notebookRepository.deleteById(id);
    }

    public Notebook update(Notebook notebook){
        if (!notebookRepository.existsById(notebook.getId())) {
            throw new IllegalArgumentException("Note with id " +notebook.getId() + " not found");
        }
        return notebookRepository.save(notebook);
    }

    public List<Notebook> findNotebooksByUserAndChannelAndStatus(User user, Channel channel, String status) {
        return notebookRepository.findByUserAndChannelAndStatus(user, channel, status);
    }

    public List<Notebook> findByStatus(String status){
        return notebookRepository.findByStatus(status);
    }


}
