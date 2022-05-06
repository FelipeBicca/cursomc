package com.felipebicca.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.felipebicca.cursomc.domain.Estado;
import com.felipebicca.cursomc.repositories.EstadoRepository;

@Service
public class EstadoService {

	@Autowired
	private EstadoRepository repo;

	public List<Estado> findAll(){
		return repo.findAllByOrderByNome();
	}
}
