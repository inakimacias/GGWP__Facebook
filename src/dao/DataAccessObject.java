package dao;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import jdo.Usuario;

public class DataAccessObject {
	Logger LOGGER;
	
	// Load Persistence Manager Factory - referencing the Persistence Unit defined in persistence.xml
	PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
	// Persistence Manager
	PersistenceManager pm = null;
	//Transaction to group DB operations
	Transaction tx = null;
	
	public DataAccessObject() {
		BasicConfigurator.configure();
		this.LOGGER = Logger.getLogger("Logger");
	}

	public void guardarUsuario(Usuario u) {
		try {
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();	
			
			tx.begin();
			
			pm.makePersistent(u);
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error("Error guardando datos " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}
	}
	
	public void borrarUsuario(Usuario u) {
		try {			
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();
			tx.begin();

			@SuppressWarnings("unchecked")
			Query<Usuario> query = pm.newQuery("SELECT FROM "+Usuario.class.getName());
			for(Usuario user : query.executeList()) {
				if(user.getEmail()==u.getEmail()) {
					LOGGER.info(" - Borrando de la BD: "+user.toString());
					pm.deletePersistent(user);
				}
			}
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error(" $ Error deleting 'User->Address' relation: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}
	}
	
	public void mostrarUsuarios() {
		try {
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();
			
			tx.begin();
			LOGGER.info("MOSTRANDO");
			@SuppressWarnings("unchecked")
			Query<Usuario> query1 = pm.newQuery("SELECT FROM "+Usuario.class.getName());
			for(Usuario user : query1.executeList()) {
				LOGGER.info(user.getEmail()+" ; "+user.getPassword());
			}
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error(" $ Error mostrando usuarios usando 'Query': " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}
	}
	public boolean validarUsuario(Usuario u) {
		boolean existeUser=false;
		try {
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();
			
			LOGGER.info("VALIDANDO "+u.getEmail()+" ; "+u.getPassword());
			
			tx.begin();
			
			@SuppressWarnings("unchecked")
			Query<Usuario> query1 = pm.newQuery("SELECT FROM "+Usuario.class.getName());
			for(Usuario user : query1.executeList()) {
				if(user.getEmail()==u.getEmail()) {
					if(user.getPassword()==u.getPassword()) {
						LOGGER.info("¡Password correcta!");
						existeUser=true;
					}
				}
			}
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error(" $ Error validando usuarios usando 'Query': " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}
		return existeUser;
	}
	
	public void actualizarPassword(Usuario u, String newPass) {
		try {			
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();	
			tx.begin();

			LOGGER.info(" - Cambiando password del usuario "+u.getEmail()+" a "+newPass);
			u.setPassword(newPass);
			pm.makePersistent(u);
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error(" $ Error setting "+newPass+" as password for user: "+u+". " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}
	}
}
