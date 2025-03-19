package ro.unibuc.hello.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends MongoRepository<JobEntity, String> {
    
    List<JobEntity> findByPositionName(String positionName); 
    
}
