package ro.unibuc.hello.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ro.unibuc.hello.dto.Message;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<MessageEntity, String> {
    @Query("{ '$or': [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ] }")
    public List<Message> findMessagesBetweenUsers(String id1, String id2);
    public Optional<MessageEntity> findById(String id);
}
