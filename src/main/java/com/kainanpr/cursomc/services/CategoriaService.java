package com.kainanpr.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kainanpr.cursomc.domain.Categoria;
import com.kainanpr.cursomc.repositories.CategoriaRepository;
import com.kainanpr.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		Categoria obj = repo.findOne(id);
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Categoria.class.getName());
		}
		
		return obj;
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
		
	public Categoria update(Categoria obj) {
		//Para verificar se a categoria existe
		find(obj.getId());
		
		//mesmo metodo do insert
		//Se o id for null ele insere, caso contrario atualiza
		return repo.save(obj);
	}
	
}
