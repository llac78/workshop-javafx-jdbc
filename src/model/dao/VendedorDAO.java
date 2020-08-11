package model.dao;

import java.util.List;

import model.entities.Departamento;
import model.entities.Vendedor;

public interface VendedorDAO {
	
	void inserir(Vendedor vendedor);
	void update(Vendedor vendedor);
	void deletarPorId(Integer id);
	Vendedor buscarPorId(Integer id);
	List<Vendedor> listar();
	List<Vendedor> listarPorDepartamento(Departamento departamento);
}
