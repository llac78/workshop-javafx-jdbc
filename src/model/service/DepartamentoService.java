package model.service;

import java.util.ArrayList;
import java.util.List;

import model.entities.Departamento;

public class DepartamentoService {
	
	public List<Departamento> listar(){
		
		 List<Departamento> lista = new ArrayList<>();
		 lista.add(new Departamento(1, "livros"));
		 lista.add(new Departamento(2, "computadores"));
		 lista.add(new Departamento(3, "Ferramentas"));
		 
		 return lista;
	}

}
