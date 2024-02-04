package webproject.factoryvision.domain.video.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import webproject.factoryvision.global.entity.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fellInfo")
public class Video extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fell_time")
    private LocalDateTime fellTime;

    private float confidence;
    private String message;

    @Column(name = "message_check")
    private int messageCheck;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "video_path")
    private String videoPath;

}
