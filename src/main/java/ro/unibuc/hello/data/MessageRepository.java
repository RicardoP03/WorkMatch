package ro.unibuc.hello.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends MongoRepository<MessageEntity, String> {
    @Query("{ '$or': [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ] }")
    public List<MessageEntity> findMessagesBetweenUsers(String id1, String id2);

    @Query("{ '$or': [ { 'senderId': ?0, 'receiverId': ?1 }, { 'senderId': ?1, 'receiverId': ?0 } ], 'content': { '$regex': ?2, '$options': 'i' } }")
    public List<MessageEntity> findMessagesBetweenUsersWithSubstring(String id1, String id2, String substring);

    public Optional<MessageEntity> findById(String id);
}
