package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Grupo;
import com.example.model.Usuario;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

	public List<Grupo> findByUsuariosIn(Usuario usuario);

}
