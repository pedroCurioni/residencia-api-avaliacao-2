package com.residencia.comercio.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.residencia.comercio.dtos.ProdutoDTO;
import com.residencia.comercio.entities.Produto;
import com.residencia.comercio.exceptions.NoSuchElementFoundException;
import com.residencia.comercio.services.ProdutoService;

@RestController
@RequestMapping("/produto")
public class ProdutoController {
	@Autowired
	ProdutoService produtoService;

	@GetMapping
	public ResponseEntity<List<Produto>> findAllProduto() {
		if (produtoService.findAllProduto().isEmpty() == true) {
			throw new NoSuchElementFoundException("Não há Produtos cadastrados no sistema");
		} else {
			return new ResponseEntity<>(produtoService.findAllProduto(), HttpStatus.OK);
		}
	}

	@GetMapping("/dto/{id}")
	public ResponseEntity<ProdutoDTO> findProdutoDTOById(@PathVariable Integer id) {
		return new ResponseEntity<>(produtoService.findProdutoDTOById(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> findProdutoById(@PathVariable Integer id) {
		if (produtoService.findProdutoById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado um Produto com o id " + id);
		} else {
			return new ResponseEntity<>(produtoService.findProdutoById(id), HttpStatus.OK);
		}
	}

	@PostMapping
	public ResponseEntity<Produto> saveProduto(@Valid @RequestBody Produto produto) {
		return new ResponseEntity<>(produtoService.saveProduto(produto), HttpStatus.CREATED);
	}

	@PostMapping(value = "/com-foto", consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<Produto> saveProdutoWithImage(@RequestPart("produto") String produto,
			@RequestPart("file") MultipartFile file) {
		return new ResponseEntity<>(produtoService.saveProdutoWithImage(produto, file), HttpStatus.CREATED);
	}

	@PostMapping("/dto")
	public ResponseEntity<ProdutoDTO> saveProdutoDTO(@RequestBody ProdutoDTO produtoDTO) {
		produtoService.saveProdutoDTO(produtoDTO);
		return new ResponseEntity<>(produtoDTO, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Produto> updateProduto(@RequestBody Produto produto) {
		return new ResponseEntity<>(produtoService.updateProduto(produto), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduto(@PathVariable Integer id) {
		if (produtoService.findProdutoById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado um Produto com o id " + id);
		} else {
			produtoService.deleteProduto(id);
			return new ResponseEntity<>("Produto deletado com sucesso", HttpStatus.OK);
		}
	}

}
