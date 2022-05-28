package com.residencia.comercio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.residencia.comercio.dtos.CategoriaDTO;
import com.residencia.comercio.entities.Categoria;
import com.residencia.comercio.exceptions.NoSuchElementFoundException;
import com.residencia.comercio.services.CategoriaService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {
	@Autowired
	CategoriaService categoriaService;

	@GetMapping
	public ResponseEntity<List<Categoria>> findAllCategoria() {
		if (categoriaService.findAllCategoria().isEmpty() == true) {
			throw new NoSuchElementFoundException("Não há Categorias cadastradas no sistema");
		}
		else {
			return new ResponseEntity<>(categoriaService.findAllCategoria(), HttpStatus.OK);
		}
	}

	@GetMapping("/dto/{id}")
	public ResponseEntity<CategoriaDTO> findCategoriaDTOById(@PathVariable Integer id) {
		return new ResponseEntity<>(categoriaService.findCategoriaDTOById(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Categoria> findCategoriaById(@PathVariable Integer id) {
		if (categoriaService.findCategoriaById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado uma Categoria com o id " + id);
		} else {
			return new ResponseEntity<>(categoriaService.findCategoriaById(id), HttpStatus.OK);
		}
	}

	@PostMapping
	public ResponseEntity<Categoria> saveCategoria(@RequestBody Categoria categoria) {
		return new ResponseEntity<>(categoriaService.saveCategoria(categoria), HttpStatus.CREATED);
	}

	@PostMapping("/dto")
	public ResponseEntity<CategoriaDTO> saveCategoriaDTO(@RequestBody CategoriaDTO categoriaDTO) {
		categoriaService.saveCategoriaDTO(categoriaDTO);
		return new ResponseEntity<>(categoriaDTO, HttpStatus.CREATED);
	}

	@PostMapping(value = "/com-foto", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<Categoria> saveCategoriaComFoto(@RequestPart("categoria") String categoria, @RequestPart("file") MultipartFile file) {

		Categoria novoCategoria = categoriaService.saveCategoriaComFoto(categoria, file);
		return new ResponseEntity<>(novoCategoria, HttpStatus.CREATED);

	}

	@PutMapping
	public ResponseEntity<Categoria> updateCategoria(@RequestBody Categoria categoria) {
		return new ResponseEntity<>(categoriaService.updateCategoria(categoria), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCategoria(@PathVariable Integer id) {
		if (categoriaService.findCategoriaById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado uma Categoria com o id " + id);
		} else {
			categoriaService.deleteCategoria(id);
			return new ResponseEntity<>("Categoria deletada com sucesso", HttpStatus.OK);
		}
	}

}
