package com.kainanpr.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kainanpr.cursomc.domain.Cidade;
import com.kainanpr.cursomc.domain.Cliente;
import com.kainanpr.cursomc.domain.Endereco;
import com.kainanpr.cursomc.domain.enums.TipoCliente;
import com.kainanpr.cursomc.dto.ClienteDTO;
import com.kainanpr.cursomc.dto.ClienteNewDTO;
import com.kainanpr.cursomc.repositories.ClienteRepository;
import com.kainanpr.cursomc.repositories.EnderecoRepository;
import com.kainanpr.cursomc.services.exceptions.DataIntegrityException;
import com.kainanpr.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

	public Cliente find(Integer id) {
		Cliente obj = repo.findOne(id);
		
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id
					+ ", Tipo: " + Cliente.class.getName());
		}
		
		return obj;
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		
		obj = repo.save(obj);
		
		enderecoRepository.save(obj.getEnderecos());
		
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
	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()));
		
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
				
		cli.getEnderecos().add(end);
		
		cli.getTelefones().add(objDTO.getTelefone1());
		
		if (objDTO.getTelefone2()!=null) {
			cli.getTelefones().add(objDTO.getTelefone2());
		}
		if (objDTO.getTelefone3()!=null) {
			cli.getTelefones().add(objDTO.getTelefone3());
		}
		
		return cli;
	}
	
	
	//Metodo auxiliar
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
