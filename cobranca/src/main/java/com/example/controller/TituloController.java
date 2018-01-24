package com.example.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.model.StatusTitulo;
import com.example.model.Titulo;
import com.example.repository.filter.TituloFilter;
import com.example.service.CadastroTituloService;

@RestController
@RequestMapping("/titulos")
public class TituloController {

	private static final String CADASTRO_VIEW = "CadastroTitulo";

	@Autowired
	private CadastroTituloService service;

	@GetMapping("/novo")
	public ModelAndView novo() {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(new Titulo());
		return mv;
	}

	@PostMapping
	public ModelAndView salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes) {
		String redirect = "redirect:/titulos/novo";
		String mensagem = "Título cadastrado com sucesso!";
		if (errors.hasErrors()) {
			return new ModelAndView(CADASTRO_VIEW);
		}
		if (titulo.getCodigo() != null) {
			redirect = "redirect:/titulos";
			mensagem = "Título atualizado com sucesso!";
		}
		try {
			service.salvar(titulo);
			attributes.addFlashAttribute("mensagem", mensagem);
			return new ModelAndView(redirect);
		} catch (IllegalArgumentException e) {
			// TODO: handle exception
			errors.rejectValue("dataVencimento", null, e.getMessage());
			return new ModelAndView(CADASTRO_VIEW);
		}

	}

	@GetMapping
	public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro) {
		List<Titulo> titulos = service.filtrar(filtro);
		ModelAndView mv = new ModelAndView("PesquisaTitulos");
		mv.addObject("titulos", titulos);
		return mv;
	}

	@GetMapping(produces = "application/json", value = "/ajax")
	public @ResponseBody List<Titulo> searchAjax(@ModelAttribute("filtro") TituloFilter filtro) {
		List<Titulo> titulos = service.filtrar(filtro);
		return titulos;
	}

	@GetMapping("{codigo}")
	public ModelAndView edicao(@PathVariable("codigo") Titulo titulo) {
		ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
		mv.addObject(titulo);
		return mv;

	}

	@DeleteMapping("{codigo}")
	public RedirectView excluir(@PathVariable Long codigo, RedirectAttributes attributes) {
		service.excluir(codigo);
		attributes.addFlashAttribute("mensagem", "Título excluído com sucesso!");
		return new RedirectView("/titulos", true);

	}

	@RequestMapping(value = "{codigo}/receber", method = RequestMethod.PUT)
	public @ResponseBody String receber(@PathVariable Long codigo) {
		return service.receberTitulo(codigo);
	}

	@ModelAttribute("todosStatusTitulo")
	public List<StatusTitulo> todosStatusTitulo() {
		return Arrays.asList(StatusTitulo.values());
	}

}
