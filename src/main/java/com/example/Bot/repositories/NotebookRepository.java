package com.example.Bot.repositories;


import com.example.Bot.entities.Notebook;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotebookRepository extends CrudRepository<Notebook, Long> {

}
