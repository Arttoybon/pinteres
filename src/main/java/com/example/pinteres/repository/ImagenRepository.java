package com.example.pinteres.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pinteres.entity.Imagen;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
	
	List<Imagen> findByUsuarioNombreOrderByIdDesc(String nombre);
	List<Imagen> findAllByOrderByIdDesc();
	List<Imagen> findByUsuarioNombre(String nombre);

}
