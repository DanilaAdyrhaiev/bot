package com.example.Bot.services;


import com.example.Bot.entities.Channel;
import com.example.Bot.entities.Notebook;
import com.example.Bot.entities.User;
import com.example.Bot.repositories.NotebookRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class NotebookService {

    private final NotebookRepository notebookRepository;
    private HashMap<Long, Notebook> notebooks = new HashMap<>();
    private Gson gson;

    @Autowired
    public NotebookService(NotebookRepository notebookRepository) {
        this.notebookRepository = notebookRepository;
        gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public Notebook saveNotebook(Notebook notebook) {
        Notebook saveNotebook = notebookRepository.save(notebook);
        notebooks.put(saveNotebook.getId(), saveNotebook);
        return saveNotebook;
    }

    public Notebook getNotebookById(Long id) {
        return notebookRepository.findById(id).orElse(null);
    }

    public Notebook update(Notebook notebook){
        if (!notebookRepository.existsById(notebook.getId())) {
            throw new IllegalArgumentException("Note with id " +notebook.getId() + " not found");
        }
        Notebook updateNotebook = notebookRepository.save(notebook);
        notebooks.put(updateNotebook.getId(), updateNotebook);
        return updateNotebook;
    }

    public List<Notebook> findNotebooksByUserAndChannelAndStatus(User user, Channel channel, String status) {
        return notebookRepository.findByUserAndChannelAndStatus(user, channel, status);
    }

    public List<Notebook> findByStatus(String status){
        return notebookRepository.findByStatus(status);
    }

    @PostConstruct
    private void init(){
        File file = new File("Notebook.json");
        if(!file.exists()){
            System.out.println("File 'Notebook.json' does not exist. No action required.");
            return;
        }
        try (FileReader reader = new FileReader("Notebook.json")){
            Notebook[] notebooks = gson.fromJson(reader, Notebook[].class);
            for(Notebook notebook : notebooks){
                notebookRepository.save(notebook);
                this.notebooks.put(notebook.getId(), notebook);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    private void destroy(){
        File file = new File("Notebook.json");
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            String val = gson.toJson(notebooks.values());
            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(val);
                writer.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
