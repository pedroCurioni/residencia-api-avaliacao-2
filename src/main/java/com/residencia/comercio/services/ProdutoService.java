package com.residencia.comercio.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.residencia.comercio.dtos.ProdutoDTO;
import com.residencia.comercio.entities.Produto;
import com.residencia.comercio.repositories.CategoriaRepository;
import com.residencia.comercio.repositories.FornecedorRepository;
import com.residencia.comercio.repositories.ProdutoRepository;

@Service
public class ProdutoService {
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	CategoriaRepository categoriaRepository;
	
	@Autowired
	FornecedorRepository fornecedorRepository;
	
	@Value("${files.folder.path}")
	private Path path;

	public List<Produto> findAllProduto() {
		return produtoRepository.findAll();
	}

	public Produto findProdutoById(Integer id) {
		return produtoRepository.findById(id).isPresent() ? produtoRepository.findById(id).get() : null;
	}

	public ProdutoDTO findProdutoDTOById(Integer id) {
		return produtoRepository.findById(id).isPresent() ? produtoToDTO(produtoRepository.findById(id).get()) : null;
	}

	public Produto saveProduto(Produto produto) {
		return produtoRepository.save(produto);
	}

	
	public Produto saveProdutoDTO(ProdutoDTO produtoDTO) {
		return produtoRepository.save(produtoDTOtoEntity(produtoDTO));
	}

	public Produto updateProduto(Produto produto) {
		return produtoRepository.save(produto);
	}

	public void deleteProduto(Integer id) {
		produtoRepository.delete(produtoRepository.findById(id).get());
	}

	public void deleteProduto(Produto produto) {
		produtoRepository.delete(produto);
	}

	private Produto produtoDTOtoEntity(ProdutoDTO produtoDTO) {
		Produto produto = new Produto();
		
		if (produtoDTO.getCategoriaId() != null) {
			produto.setCategoria(categoriaRepository.findById(produtoDTO.getCategoriaId()).get());
		}
		if (produtoDTO.getFornecedorId() != null) {
			produto.setFornecedor(fornecedorRepository.findById(produtoDTO.getFornecedorId()).get());
		}
		
		produto.setIdProduto(produtoDTO.getIdProduto());
		produto.setNomeProduto(produtoDTO.getNomeProduto());
		produto.setSku(produtoDTO.getSku());

		return produto;
	}

	private ProdutoDTO produtoToDTO(Produto produto) {
		ProdutoDTO produtoDTO = new ProdutoDTO();
		
		produtoDTO.setCategoriaId(produto.getCategoria().getIdCategoria());
		produtoDTO.setCategoriaNome(produto.getCategoria().getNomeCategoria());
		produtoDTO.setFornecedorId(produto.getFornecedor().getIdFornecedor());
		produtoDTO.setFornecedorNome(produto.getFornecedor().getNomeFantasia());
		produtoDTO.setIdProduto(produto.getIdProduto());
		produtoDTO.setNomeProduto(produto.getNomeProduto());
		produtoDTO.setSku(produto.getSku());
		
		return produtoDTO;
	}
}
