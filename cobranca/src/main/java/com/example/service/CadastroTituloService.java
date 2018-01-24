package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.example.model.StatusTitulo;
import com.example.model.Titulo;
import com.example.repository.TituloRepository;
import com.example.repository.filter.TituloFilter;

@Service
public class CadastroTituloService {

	@Autowired
	private TituloRepository repositorio;

	public void salvar(Titulo titulo) {

		try {
			repositorio.save(titulo);
		} catch (DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido!");
		}

	}

	public void excluir(Long codigo) {
		repositorio.delete(codigo);
	}

	public List<Titulo> getTitulos() {
		return repositorio.findAll();
	}

	public String receberTitulo(Long codigo) {
		Titulo titulo = repositorio.findOne(codigo);
		titulo.setStatus(StatusTitulo.RECEBIDO);
		repositorio.save(titulo);
		
		return titulo.getStatus().getDescricao();
		
	}
	
	public List<Titulo> filtrar(TituloFilter filtro){
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return repositorio.findByDescricaoContaining(descricao);
	}

}
