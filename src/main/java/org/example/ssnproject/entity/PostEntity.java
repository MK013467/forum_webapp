package org.example.ssnproject.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post_table")
public class PostEntity extends TimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name = "user_id" )
    private UserEntity writer;

    @Column(length = 500)
    private String contents;

    @Column
    private Integer views;

//  cascadetype.remove -> remove comments automatically when posts are erased
    @OneToMany(mappedBy = "postEntity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OrderBy("id asc")
    private List<CommentEntity> commentEntityList = new ArrayList<>();

}
