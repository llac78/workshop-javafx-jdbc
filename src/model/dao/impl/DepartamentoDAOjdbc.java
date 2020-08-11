package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DBException;
import model.dao.DepartamentoDAO;
import model.entities.Departamento;

public class DepartamentoDAOjdbc implements DepartamentoDAO {
	
	private Connection conn;
	
	public DepartamentoDAOjdbc(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void inserir(Departamento departamento) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, departamento.getNome());
			
			int linhasAfetadas = st.executeUpdate();
			
			if(linhasAfetadas > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					departamento.setId(id);
				}
				DB.fecharResultSet(rs);
			} else {
				throw new DBException("Erro inesperado. Nenhuma linha afetada!");
			}
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
		}
		
	}

	@Override
	public void update(Departamento departamento) {

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE department SET " 
					+ "Name = ? "
					+ "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);
					
			st.setString(1, departamento.getNome());	
			st.setInt(2, departamento.getId());	
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
			
		} finally {
			DB.fecharStatement(st);
		}
	}

	@Override
	public void deletarPorId(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM department WHERE Id = ?");
					
			st.setInt(1, id);	
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
			
		} finally {
			DB.fecharStatement(st);
		}
		
	}

	@Override
	public Departamento buscarPorId(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT * FROM department WHERE Id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();
			
			if(rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				
				return dep;
			}
			
			return null;
			
		} catch (SQLException e) {
			throw new DBException(e.getMessage());
		} finally {
			DB.fecharStatement(st);
			DB.fecharResultSet(rs);
		}
	}

	@Override
	public List<Departamento> listar() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("SELECT * FROM department ORDER BY Name");
			
			rs = st.executeQuery();
			
			List<Departamento> lista = new ArrayList<>();
			while(rs.next()) {
				Departamento dep = instanciarDepartamento(rs);
				lista.add(dep);
			}
			return lista;
			
		} catch (SQLException e) {
			throw new DBException("Erro ao listar!");
		} finally {
			DB.fecharStatement(st);
		}
	}
	
	private Departamento instanciarDepartamento(ResultSet rs) throws SQLException {
		Departamento dep = new Departamento();
		dep.setId(rs.getInt("Id"));
		dep.setNome(rs.getString("Name"));
		
		return dep;
	}

}
