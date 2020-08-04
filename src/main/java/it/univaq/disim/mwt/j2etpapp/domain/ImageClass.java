package it.univaq.disim.mwt.j2etpapp.domain;

import lombok.Data;

import javax.persistence.*;

@Data
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
    private String location;

    @Column(columnDefinition = "text")
    private String caption;
}
