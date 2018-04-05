package com.kainanpr.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kainanpr.cursomc.domain.Cliente;
import com.kainanpr.cursomc.dto.ClienteDTO;
import com.kainanpr.cursomc.repositories.ClienteRepository;
import com.kainanpr.cursomc.services.exceptions.DataIntegrityException;
import com.kainanpr.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;

	public Cliente find(Integer id) {
		Cliente obj = repo.findOne(id);
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Cliente.class.getName());
		}
		
		return obj;
	}
	
		
	public Cliente update(Cliente obj) {
		//Para verificar se a cliente existe
		Cliente newObj = find(obj.getId());
		
		updateData(newObj, obj);
		
		//mesmo metodo do insert
		//Se o id for null ele insere, caso contrario atualiza
		return repo.save(newObj);
	}

	public void delete(Integer id) {
		//Para verificar se a cliente existe
		find(id);
		
		try {
			repo.delete(id);
		} 
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir cliente porque há entidades relacionadas");
		}
			
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	//Recurso de paginação
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	//Metodo auxiliar
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
	}
	
	//Metodo auxiliar
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
