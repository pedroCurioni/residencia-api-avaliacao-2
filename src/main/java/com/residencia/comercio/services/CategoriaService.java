package com.residencia.comercio.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.residencia.comercio.dtos.CategoriaDTO;
import com.residencia.comercio.entities.Categoria;
import com.residencia.comercio.exceptions.NoSuchElementFoundException;
import com.residencia.comercio.repositories.CategoriaRepository;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoriaService {
	@Autowired
	CategoriaRepository categoriaRepository;

	public List<Categoria> findAllCategoria() {
		return categoriaRepository.findAll();
	}

	public Categoria findCategoriaById(Integer id) {
		return categoriaRepository.findById(id).isPresent() ? categoriaRepository.findById(id).get() : null;
	}

	public CategoriaDTO findCategoriaDTOById(Integer id) {
		if (categoriaRepository.findById(id).isPresent() == true) {
			CategoriaDTO categoriaDTO = categoriaToDTO(categoriaRepository.findById(id).get());
			return categoriaDTO;
		}
		else {
			throw new NoSuchElementFoundException("Categoria de ID " + id + " não encontrada.");
		}
	}

	public Categoria saveCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public Categoria saveCategoriaDTO(CategoriaDTO categoriaDTO) {
		return categoriaRepository.save(categoriaDTOtoEntity(categoriaDTO));
	}

	public Categoria saveCategoriaComFoto(String categoriaString, MultipartFile file) {

		Categoria categoriaConvertida = new Categoria();

		try {
			ObjectMapper objMapper = new ObjectMapper();
			categoriaConvertida = objMapper.readValue(categoriaString, Categoria.class);
		}catch(IOException e) {
			System.out.println("Ocorreu um erro na conversão");
		}

		Categoria categoriaBD = categoriaRepository.save(categoriaConvertida);

		categoriaBD.setNomeImagem(categoriaBD.getIdCategoria()+"_"+file.getOriginalFilename());

		Categoria categoriaAtualizada = categoriaRepository.save(categoriaBD);

		return categoriaAtualizada;
	}

	public Categoria updateCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	public void deleteCategoria(Integer id) {
		categoriaRepository.delete(categoriaRepository.findById(id).get());
	}

	public void deleteCategoria(Categoria categoria) {
		categoriaRepository.delete(categoria);
	}

	private Categoria categoriaDTOtoEntity(CategoriaDTO categoriaDTO) {
		Categoria categoria = new Categoria();

		categoria.setIdCategoria(categoriaDTO.getIdCategoria());
		categoria.setNomeCategoria(categoriaDTO.getNomeCategoria());
		
		return categoria;
	}

	private CategoriaDTO categoriaToDTO(Categoria categoria) {
		CategoriaDTO categoriaDTO = new CategoriaDTO();
		
		categoriaDTO.setIdCategoria(categoria.getIdCategoria());
		categoriaDTO.setNomeCategoria(categoria.getNomeCategoria());
		
		return categoriaDTO;
	}
}
