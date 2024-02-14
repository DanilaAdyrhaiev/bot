package com.example.Bot.services;


import com.example.Bot.entities.Notebook;
import com.example.Bot.repositories.NotebookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
