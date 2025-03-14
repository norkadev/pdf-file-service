package com.norkadev.pdf_file_service.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "files")
@Data
@Builder
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String type;
    private long size;
    @Lob
    private byte[] data;

    public File() {
    }

    public File(Integer id, String name, String type, long size, byte[] data) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.size = size;
        this.data = data;
    }
}
