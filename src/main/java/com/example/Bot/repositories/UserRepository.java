package com.example.Bot.repositories;



import com.example.Bot.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByChatId(Long chatId);

    boolean existsByChatId(Long chatid);
}
