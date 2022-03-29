/**
 * 
 */
package fr.formation.enchere.eni.dal;

import fr.formation.enchere.eni.bo.Utilisateur;

/**
 * Classe en charge de
 * @author msorin2022
 * @date 29 mars 2022
 * @version EnchereENI- V0.1
 * @since  29 mars 2022 - 11:29:17
 *
 */
public interface IUtilisateurDAO {
	public void delete(Utilisateur utilisateur) throws DALException;
}
