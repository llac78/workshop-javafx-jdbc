package model.service;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.DepartamentoDAO;
import model.entities.Departamento;

public class DepartamentoService {
	
	private DepartamentoDAO dao = DAOFactory.criarDepartamentoDAO();
	
	public List<Departamento> listar(){
		 return dao.listar();
	}
	
	public void salvarOuAtualizar(Departamento dep) {
		if(dep.getId() == null) {
			dao.inserir(dep);
		} else {
			dao.update(dep);
		}
	}

}
