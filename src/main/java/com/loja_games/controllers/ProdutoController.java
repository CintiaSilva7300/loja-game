package com.loja_games.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.loja_games.models.Produto;
import com.loja_games.repository.CategoriaRepository;
import com.loja_games.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
	
	@Autowired
	private final ProdutoRepository produtoRepository;
	
	@Autowired
	private final CategoriaRepository categoriaRepository;
	
	public ProdutoController(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
        this.produtoRepository = produtoRepository;
        this.categoriaRepository = categoriaRepository;
    }
	
	@GetMapping
    public ResponseEntity<List<Produto>> getAll()  {
		return ResponseEntity.ok(produtoRepository.findAll());
    }
	
	@GetMapping("/{id}")
    public ResponseEntity<Produto> getById(@PathVariable Long id)  {
		return produtoRepository.findById(id)
				.map(response -> ResponseEntity.ok(response))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
	
	@GetMapping("/titulo/{titulo}")
    public ResponseEntity<List<Produto>> getByTitulo(@PathVariable String nome) {
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
    }
	
	@PostMapping("/cria")
    public ResponseEntity<Produto> criaPostagem(@Valid @RequestBody Produto produto) {
		if(categoriaRepository.existsById(produto.getCategoria().getId())) 
			return ResponseEntity.status(HttpStatus.CREATED)
				.body(produtoRepository.save(produto));
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tema não existe", null);
    }
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/exclui/{id}")
	public ResponseEntity<Object> excluiPostagem(@PathVariable Long id) {
	    if (produtoRepository.existsById(id)) {
	        produtoRepository.deleteById(id);
	        return ResponseEntity.noContent().build();
	    } else {
	        String mensagem = "Produto com ID " + id + " não encontrada.";
	        Map<String, String> response = new HashMap<>();
	        response.put("mensagem", mensagem);
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	    }
	}
}
