package ro.unibuc.hello.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends MongoRepository<ApplicationEntity, String> {
    
    @Query("{ 'seekerId': ?0 }")
    List<ApplicationEntity> findBySeekerId(String seekerId);

    @Query("{ 'jobId': ?0 }")
    List<ApplicationEntity> findByJobId(String jobId);

    @Query("{ 'jobId': ?0, 'seekerId': ?1 }")
    List<ApplicationEntity> findByJobIdAndSeekerId(String jobId, String seekerId);

    Optional<ApplicationEntity> findById(String id);
}
