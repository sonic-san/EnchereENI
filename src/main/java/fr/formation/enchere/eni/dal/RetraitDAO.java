/**
 * 
 */
package fr.formation.enchere.eni.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.formation.enchere.eni.bo.Retrait;
import fr.formation.enchere.eni.dal.util.ConnectionProvider;

/**
 * Classe en charge de
 * 
 * @author cmounier2022
 * @date 29 mars 2022
 * @version EnchereENI- V0.1
 * @since 29 mars 2022 - 15:18:15
 *
 */
public class RetraitDAO implements IRetraitDAO {

	private final String SELECT = "SELECT no_article, rue, code_postal, ville FROM RETRAITS";
	private final String INSERT = "INSERT INTO RETRAITS (rue, code_postal, ville) VALUES (?, ?, ?)";
	private final String UPDATE = "UPDATE RETRAITS SET rue = ?, code_postal = ?, ville = ? WHERE no_article = ?";
	private final String DELETE = "DELETE INTO RETRAITS WHERE no_article = ?";
	private final String SELECT_BY_ID = "SELECT no_article, rue, code_postal, ville FROM RETRAITS WHERE no_article = ?";

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public List<Retrait> selectAll() throws DALException {
		List<Retrait> result = new ArrayList<Retrait>();
		try (Connection con = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = con.prepareStatement(SELECT);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Retrait retrait = new Retrait(rs.getInt("no_article"), rs.getString("rue"), rs.getString("code_postal"),
						rs.getString("ville"));
				result.add(retrait);
			}
		} catch (SQLException e) {
			throw new DALException("Probleme de select : " + e.getMessage());

		}
		return result;
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void insert(Retrait retrait) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {

			PreparedStatement stmt = cnx.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

			stmt.setString(1, retrait.getRue());
			stmt.setString(2, retrait.getCodePostal());
			stmt.setString(3, retrait.getVille());

			Integer nb = stmt.executeUpdate();
			if (nb > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					retrait.setNoArticle((rs.getInt(1)));
				}
			}

			Integer nb = stmt.executeUpdate();
			if (nb > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					retrait.setNoArticle((rs.getInt(1)));
				}
			}

		} catch (SQLException e) {
			throw new DALException("DAL - Erreur dans la fonction insert : " + e.getMessage());
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void delete(Integer id) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {

			PreparedStatement stmt = cnx.prepareStatement(DELETE, Statement.RETURN_GENERATED_KEYS);

			stmt.setInt(1, id);

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new DALException("Erreur dans la fonction delete : " + e.getMessage());
		}
	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void update(Retrait retrait, Integer id) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {

			PreparedStatement stmt = cnx.prepareStatement(UPDATE);

			stmt.setString(1, retrait.getRue());
			stmt.setString(2, retrait.getCodePostal());
			stmt.setString(3, retrait.getVille());

			stmt.setInt(4, id);

			stmt.executeUpdate();

		} catch (SQLException e) {
			throw new DALException("DAL - Erreur dans la fonction update : " + e.getMessage());
		}

	}

	/**
	 * {@inheritedDoc}
	 */
	@Override
	public void selectById(Integer id) throws DALException {
		Retrait result = null;
		try (Connection con = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = con.prepareStatement(SELECT_BY_ID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				result = new Retrait(rs.getInt("no_article"), rs.getString("rue"), rs.getString("code_postal"),
						rs.getString("ville"));

			}
		} catch (SQLException e) {
			throw new DALException("Probleme de select : " + e.getMessage());

		}
		return result;

	}

}
