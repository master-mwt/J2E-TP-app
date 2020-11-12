package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "images")
public class ImageClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "text", nullable = false)
    private String type;

    @Column(columnDefinition = "text", nullable = false)
    private String size;

    @Column(columnDefinition = "text", nullable = false)
    @NotBlank
    private String location;

    @Transient
    private boolean first;

    @Column(columnDefinition = "text")
    private String caption;
}
