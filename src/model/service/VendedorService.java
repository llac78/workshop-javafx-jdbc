package model.service;

import java.util.List;

import model.dao.DAOFactory;
import model.dao.VendedorDAO;
import model.entities.Vendedor;

public class VendedorService {
	
	private VendedorDAO dao = DAOFactory.criarVendedorDAO();
	
	public List<Vendedor> listar(){
		 return dao.listar();
	}
	
	public void salvarOuAtualizar(Vendedor vendedor) {
		if(vendedor.getId() == null) {
			dao.inserir(vendedor);
		} else {
			dao.update(vendedor);
		}
	}
	
	public void remover(Vendedor vendedor) {
		dao.deletarPorId(vendedor.getId());
	}

}
