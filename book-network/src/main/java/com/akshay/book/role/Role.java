package com.akshay.book.role;

import com.akshay.book.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;


    @CreatedDate
    @Column(updatable = false,nullable = false)
    private LocalDateTime createdDate;

    @org.springframework.data.annotation.LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime LastModifiedDate;


}
