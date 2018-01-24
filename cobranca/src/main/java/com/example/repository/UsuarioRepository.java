package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	public Usuario findByLogin(String userName);

}
