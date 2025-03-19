package ro.unibuc.hello.data;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserEntity {

    @Id
    private String id; 
    private String name;
    private String role;
    private String password;

    public UserEntity() {}

    public UserEntity(String name, String role, String password) { 
        this.name = name;
        this.role = role;
        this.password = password;
    }

    public String getId() { return id; } 
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return String.format("User[name='%s', role='%s']", name, role);
    }
}
