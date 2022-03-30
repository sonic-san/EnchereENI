/**
 * 
 */
package fr.formation.enchere.eni.dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.formation.enchere.eni.bo.ArticleVendu;
import fr.formation.enchere.eni.dal.util.ConnectionProvider;

/**
 * Classe en charge de
 * @author tevin
 * @date 30 mars 2022
 * @version EnchereENI- V0.1
 * @since  30 mars 2022 - 14:35:14
 *
 */
public class ArticleDAO implements IArticleDAO{
	
	private final String SELECT = "SELECT no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie FROM ARTICLES_VENDUS";
	private final String INSERT = "INSERT INTO ARTICLES_VENDUS (no_article, nom_article, description, date_debut_encheres, date_fin_encheres, prix_initial, prix_vente, no_utilisateur, no_categorie) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final String UPDATE = "UPDATE ARTICLES_VENDUS SET nom_article = ?, description = ?, date_debut_encheres = ?, date_fin_encheres = ?, prix_initial = ?, prix_vente = ?, no_utilisateur = ?, no_categorie = ? WHERE no_article = ?";
	private final String DELETE = "DELETE INTO ARTICLES_VENDUS WHERE no_article = ?";

	/**
	*{@inheritedDoc}
	*/
	@Override
	public List<ArticleVendu> selectAll() {
		List<ArticleVendu> result = new ArrayList<ArticleVendu>();
		try (Connection con = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = con.prepareStatement(SELECT);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ArticleVendu articleVendu = new ArticleVendu(rs.getInt("no_article"), rs.getString("nom_article"),
						rs.getString("description"), rs.getDate("date_debut_encheres").toLocalDate(), rs.getDate("date_fin_encheres").toLocalDate(), rs.getInt("prix_initial"),
						rs.getInt("prix_vente"), rs.getInt("no_utilisateur"), rs.getInt("no_categorie");
				articleVendu.setNoArticle(rs.getInt("no_article"));
				result.add(articleVendu);
			}
		} catch (SQLException e) {
			throw new DALException("Probleme de select : " + e.getMessage());
		}
		return result;
	}

	/**
	*{@inheritedDoc}
	*/
	@Override
	public void insert(ArticleVendu articleVendu) throws DALException {
		try (Connection cnx = ConnectionProvider.getConnection()) {
		PreparedStatement stmt = cnx.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);

		stmt.setString(1, articleVendu.getNomArticle());
		stmt.setString(2, articleVendu.getDescription());
		stmt.setDate(3, Date.valueOf(articleVendu.getDateDebutEncheres()));
		stmt.setDate(4, Date.valueOf(articleVendu.getDateFinEncheres()));
		stmt.setInt(5, articleVendu.getMiseAPrix());
		stmt.setInt(6, articleVendu.getPrixVente());
		
		Integer nb = stmt.executeUpdate();
		if (nb > 0) {
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				articleVendu.setNoArticle((rs.getInt(1)));
			}
		}

	} catch (SQLException e) {
		throw new DALException("DAL - Erreur dans la fonction insert : " + e.getMessage());
	}
	}

	/**
	*{@inheritedDoc}
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
	
	public void update(ArticleVendu articleVendu, Integer id) throws DALException{
		try (Connection cnx = ConnectionProvider.getConnection()) {
			PreparedStatement stmt = cnx.prepareStatement(UPDATE);

			stmt.setString(1, articleVendu.getNomArticle());
			stmt.setString(2, articleVendu.getDescription());
			stmt.setDate(3, Date.valueOf(articleVendu.getDateDebutEncheres()));
			stmt.setDate(4, Date.valueOf(articleVendu.getDateFinEncheres()));
			stmt.setInt(5, articleVendu.getMiseAPrix());
			stmt.setInt(6, articleVendu.getPrixVente());
			
			stmt.setInt(7, id);
			
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			throw new DALException("DAL - Erreur dans la fonction update : " + e.getMessage());
		}
		
	}
	
}