package br.com.devrodrigues.todolist.task;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonPropertyOrder({
        "id",
        "description",
        "title",
        "priority",
        "startAt",
        "endAt",
        "idUser",
        "createdAt"
})
@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    private String description;

    @Column(length = 50)
    private String title;

    private String priority;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime createdAt;
}