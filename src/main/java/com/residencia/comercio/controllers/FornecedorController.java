package com.residencia.comercio.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.residencia.comercio.dtos.FornecedorDTO;
import com.residencia.comercio.entities.Fornecedor;
import com.residencia.comercio.exceptions.NoSuchElementFoundException;
import com.residencia.comercio.services.FornecedorService;

@RestController
@RequestMapping("/fornecedor")
public class FornecedorController {
	@Autowired
	FornecedorService fornecedorService;

	@GetMapping
	public ResponseEntity<List<Fornecedor>> findAllFornecedor() {
		if (fornecedorService.findAllFornecedor().isEmpty() == true) {
			throw new NoSuchElementFoundException("Não há Fornecedores cadastrados no sistema");
		} else {
			return new ResponseEntity<>(fornecedorService.findAllFornecedor(), HttpStatus.OK);
		}
	}

	@GetMapping("/dto/{id}")
	public ResponseEntity<FornecedorDTO> findFornecedorDTOById(@PathVariable Integer id) {
		return new ResponseEntity<>(fornecedorService.findFornecedorDTOById(id), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Fornecedor> findFornecedorById(@PathVariable Integer id) {
		if (fornecedorService.findFornecedorById(id) == null) {
			throw new NoSuchElementFoundException("Não foi encontrado Fornecedor com o id " + id);
		} else {
			return new ResponseEntity<>(fornecedorService.findFornecedorById(id), HttpStatus.OK);
		}
	}

	// Vai ser implementando futuramente para consumir API da Receita Federal!!
	@PostMapping
	public ResponseEntity<Fornecedor> saveFornecedor(@RequestParam String cnpj) {
		return new ResponseEntity<>(
				fornecedorService.saveFornecedor(
						fornecedorService.CnpjDTOtoFornecedor(fornecedorService.getCnpjDTOFromExternal(cnpj))),
				HttpStatus.CREATED);
	}

	@PostMapping("/completo")
	public ResponseEntity<Fornecedor> saveFornecedorCompleto(@RequestBody Fornecedor fornecedor) {
		return new ResponseEntity<>(fornecedorService.saveFornecedor(fornecedor), HttpStatus.CREATED);
	}

	@PostMapping("/dto")
	public ResponseEntity<FornecedorDTO> saveFornecedorDTO(@RequestBody FornecedorDTO fornecedorDTO) {
		fornecedorService.saveFornecedorDTO(fornecedorDTO);
		return new ResponseEntity<>(fornecedorDTO, HttpStatus.CREATED);
	}

	@PutMapping
	public ResponseEntity<Fornecedor> updateFornecedor(@RequestBody Fornecedor fornecedor) {
		return new ResponseEntity<>(fornecedorService.updateFornecedor(fornecedor), HttpStatus.OK);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Fornecedor> updateAddressFornecedor(@PathVariable Integer id, @RequestParam String cep) {
		return new ResponseEntity<>(fornecedorService.updateFornecedor(fornecedorService.updateAddressFornecedor(
				fornecedorService.findFornecedorById(id), fornecedorService.getCepDTOFromExternal(cep))), HttpStatus.OK);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteFornecedor(@PathVariable Integer id) {
		if (fornecedorService.findFornecedorById(id) == null) {
			return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
		} else {
			fornecedorService.deleteFornecedor(id);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
	}

}
