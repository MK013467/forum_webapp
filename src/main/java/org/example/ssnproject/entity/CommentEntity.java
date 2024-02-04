package org.example.ssnproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "comment_table")
@Entity
public class CommentEntity extends TimeEntity{

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String commentContents;

    @ManyToOne
    @JoinColumn( name = "user_id")
    private UserEntity commentWriter;

    @ManyToOne
    @JoinColumn( name = "post_id")
    private PostEntity postEntity;

}
