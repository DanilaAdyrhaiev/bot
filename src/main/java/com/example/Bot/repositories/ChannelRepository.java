package com.example.Bot.repositories;



import com.example.Bot.entities.Channel;
import com.example.Bot.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends CrudRepository<Channel, Long> {
    List<Channel> findByUser(User user);
    List<Channel> findByCategory(String category);

    List<Channel> findAllByCategoryIsNotNullOrderByRateDesc();
    List<Channel> findAllByOrderByRateDesc();

}
