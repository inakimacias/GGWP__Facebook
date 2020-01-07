package dao;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import jdo.Usuario;

public class DataAccessObject {
	private static Logger LOGGER;
	private static DataAccessObject instance = null;
	private PersistenceManagerFactory pmf = null;
	
	public DataAccessObject() {
		BasicConfigurator.configure();
		LOGGER = Logger.getLogger("Logger");
		pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");	
	}
	
	public static DataAccessObject getInstance() {
		if (instance == null) {
			instance = new DataAccessObject();
			instance.initializeData();
		}		
		
		return instance;
	}
	
	private void initializeData() {
		System.out.println(" * Initializing data base");
		//Create Sample data
		Usuario dani = new Usuario();
		dani.setEmail("daniel@gmail.com");
		dani.setPassword("elDani");
						
		Usuario inaki = new Usuario();
		inaki.setEmail("inaki@gmail.com");
		inaki.setPassword("elInaki");
		
		Usuario ruben = new Usuario();
		ruben.setEmail("ruben@gmail.com");
		ruben.setPassword("elRuben");
		
		Usuario alberto = new Usuario();
		alberto.setEmail("alberto@gmail.com");
		alberto.setPassword("elAlberto");
		
		try {
			//Store users in DB
			DataAccessObject.getInstance().guardarUsuario(dani);
			DataAccessObject.getInstance().guardarUsuario(inaki);
			DataAccessObject.getInstance().guardarUsuario(ruben);
			DataAccessObject.getInstance().guardarUsuario(alberto);
		} catch (Exception ex) {
			System.out.println(" $ Error initializing data: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public void guardarUsuario(Usuario u) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();	
		try {
			
			
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
	
	public void borrarUsuario(String email) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {			
			tx.begin();

			@SuppressWarnings("unchecked")
			Query<Usuario> query = pm.newQuery("SELECT FROM "+Usuario.class.getName());
			for(Usuario user : query.executeList()) {
				if(user.getEmail()==email) {
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
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
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
	
	public boolean validarUsuario(String email, String password) {
		boolean existeUser=false;
		System.out.println("CACA");
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try {
			LOGGER.info("VALIDANDO "+email+" ; "+password);
			
			tx.begin();
			
			@SuppressWarnings("unchecked")
			Query<Usuario> query1 = pm.newQuery("SELECT FROM "+Usuario.class.getName());
			for(Usuario user : query1.executeList()) {
				if(user.getEmail()==email) {
					if(user.getPassword()==password) {
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
	
	public void actualizarPassword(String email, String newPass) {
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();	
		try {			
			tx.begin();

			LOGGER.info(" - Cambiando password del usuario "+email+" a "+newPass);
			Usuario u = getUser(email);
			u.setPassword(newPass);
			pm.makePersistent(u);
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error(" $ Error setting "+newPass+" as password for user: "+email+". " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}
	}
	
	public void deleteData() {
		List<Usuario> users = DataAccessObject.getInstance().getAllUsers();
		
		for (Usuario user : users) {
			DataAccessObject.getInstance().deleteObjectFromDB(user);
		}
	}
	
	public void deleteObjectFromDB(Object object) {
		PersistenceManager pm = pmf.getPersistenceManager();
		pm.getFetchPlan().setMaxFetchDepth(4);
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();
			System.out.println(" * Delete an object: " + object);
			
			pm.deletePersistent(object);
			
			tx.commit();
		} catch (Exception ex) {
			System.out.println(" $ Error deleting an object: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			pm.close();
		}
	}
	
	public List<Usuario> getAllUsers() {
		PersistenceManager pm = pmf.getPersistenceManager();
		pm.getFetchPlan().setMaxFetchDepth(4);
		Transaction tx = pm.currentTransaction(); 
		ArrayList<Usuario> users = new ArrayList<>();

		try {
			System.out.println("  * Querying users");
			tx.begin();
			
			@SuppressWarnings("unchecked")
			Query<Usuario> query1 = pm.newQuery("SELECT FROM "+Usuario.class.getName());
			for(Usuario user : query1.executeList()) {
				users.add(user);
			}
			
			tx.commit();
			
		} catch (Exception ex) {
			System.out.println("  $ Error querying users: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}

			pm.close();
		}

		return users;
	}
	
	public Usuario getUser(String email) {
		PersistenceManager pm = pmf.getPersistenceManager();
		pm.getFetchPlan().setMaxFetchDepth(4);
		Transaction tx = pm.currentTransaction();
		Usuario user = null; 

		try {
			System.out.println("  * Querying a User by email: " + email);
			tx.begin();
			
			Query<?> query = pm.newQuery("SELECT FROM " + Usuario.class.getName() + " WHERE email == '" + email + "'");
			query.setUnique(true);
			user = (Usuario) query.execute();
			
			tx.commit();
		} catch (Exception ex) {
			System.out.println("  $ Error querying a User: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
		return user;
	}
}
