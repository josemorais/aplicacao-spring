package com.example.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.model.Grupo;
import com.example.model.Permissao;
import com.example.model.Usuario;
import com.example.repository.GrupoRepository;
import com.example.repository.PermissaoRepository;
import com.example.repository.UsuarioRepository;

@Component
public class CobrancaUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepositorio;

	@Autowired
	private GrupoRepository grupoRepositorio;

	@Autowired
	private PermissaoRepository permissaoRepositorio;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Usuario usuario = usuarioRepositorio.findByLogin(userName);

		if (usuario == null)
			throw new UsernameNotFoundException("Usuário não encontrado!");

		return new UsuarioSistema(usuario.getNome(), usuario.getLogin(), usuario.getSenha(), authorities(usuario));
	}

	public Collection<? extends GrantedAuthority> authorities(Usuario usuario) {
		return authorities(grupoRepositorio.findByUsuariosIn(usuario));
	}

	private Collection<? extends GrantedAuthority> authorities(List<Grupo> grupos) {
		// TODO Auto-generated method stub
		Collection<GrantedAuthority> auths = new ArrayList<>();

		for (Grupo grupo : grupos) {
			List<Permissao> permissoes = permissaoRepositorio.findByGruposIn(grupo);

			for (Permissao permissao : permissoes) {
				auths.add(new SimpleGrantedAuthority("ROLE_" + permissao.getNome()));
			}
		}
		return auths;
	}

}
