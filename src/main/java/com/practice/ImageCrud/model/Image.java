package com.practice.ImageCrud.model;



import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

	@Entity
	@Table(name = "images")
	@Data
	public class Image {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String fileName;
	    private String filePath;
	    private LocalDateTime uploadDate;

	    // Constructors, Getters, Setters
	    public Image() {}

	    public Image(String fileName, String filePath) {
	        this.fileName = fileName;
	        this.filePath = filePath;
	        this.uploadDate = LocalDateTime.now();
	    }

	   
	}

