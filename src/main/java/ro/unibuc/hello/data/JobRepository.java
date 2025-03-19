package ro.unibuc.hello.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ro.unibuc.hello.data.JobEntity;

@Repository
public interface JobRepository extends MongoRepository<JobEntity,String>{
    JobEntity findById(int id);
    
    JobEntity findByPositionName(String positionName);
}
