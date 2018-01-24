package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.model.Grupo;
import com.example.model.Permissao;

public interface PermissaoRepository extends JpaRepository<Permissao, Long> {

	public List<Permissao> findByGruposIn(Grupo grupo);

}
