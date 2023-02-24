package com.example.bookservice.entities;




import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="users")
@Getter@Setter
public class User  {
    @Id
    @SequenceGenerator(
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_sequence"
    )
    private long id;

    private String email;
    private String password;
    private String username;

    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public User(long id, String email, String password, String username) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    @OneToMany(mappedBy = "owner")
    private List<Group> groupsUserOwns = new ArrayList<>();

    @JsonIgnoreProperties(value = "users")
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "users")
    List<Group> groups = new ArrayList<>();

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                '}';
    }

    public void removeGroup(Group group){
        groups.remove(group);
    }

}
