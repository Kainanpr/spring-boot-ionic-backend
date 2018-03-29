package com.kainanpr.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kainanpr.cursomc.domain.Cliente;
import com.kainanpr.cursomc.services.ClienteService;

@RestController
@RequestMapping(value="/Clientes") //EndPoint
public class ClienteResource {
	
	@Autowired
	private ClienteService service;

	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<?> buscar(@PathVariable Integer id) {
		
		Cliente obj = service.buscar(id);
		
		return ResponseEntity.ok().body(obj);
		
	}
	
}//Fim da classe
