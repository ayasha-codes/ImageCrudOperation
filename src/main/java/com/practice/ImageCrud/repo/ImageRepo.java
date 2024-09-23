package com.practice.ImageCrud.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.practice.ImageCrud.model.Image;

public interface ImageRepo extends JpaRepository<Image, Long> {
}

