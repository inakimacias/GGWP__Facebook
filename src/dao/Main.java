package dao;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import jdo.Usuario;

public class Main {
	public static void main(String[] args) {
		DataAccessObject dao = DataAccessObject.getInstance();
		
//		Usuario elDani = new Usuario();
//	    elDani.setEmail("danaso@opendeusto.es");
//	    elDani.setPassword("patata");
//	    
//	    Usuario elCorno = new Usuario();
//	    elCorno.setEmail("elCorno@gmail.com");
//	    elCorno.setPassword("danonino");
	
//		System.out.println(elDani.toString());
//		System.out.println(elCorno.toString());
//		
//		dao.guardarUsuario(elCorno);
//		dao.guardarUsuario(elDani);
//		
//		System.out.println(elDani.toString());
//		System.out.println(elCorno.toString());
		dao.mostrarUsuarios();
		
//		System.out.println("-RECREO USUARIOS");
//		elDani.setEmail("danaso@opendeusto.es");
//	    elDani.setPassword("patata");
//	    elCorno.setEmail("elCorno@gmail.com");
//	    elCorno.setPassword("danonino");
//	    
//	    System.out.println(elDani.toString());
//		System.out.println(elCorno.toString());
//		dao.mostrarUsuarios();
		
		boolean b = dao.validarUsuario("daniel@gmail.com","elDani");
		System.out.println(b);
		
//		System.out.println(elDani.toString());
//		System.out.println(elCorno.toString());
//		dao.mostrarUsuarios();
		
		dao.actualizarPassword("daniel@gmail.com", "amapola");
		
//		System.out.println(elDani.toString());
//		System.out.println(elCorno.toString());
//		dao.mostrarUsuarios();
		
		dao.borrarUsuario("daniel@gmail.com");
		
//		System.out.println(elDani.toString());
//		System.out.println(elCorno.toString());
//		dao.mostrarUsuarios();
	}
	
	public static void main1(String[] args) {
		BasicConfigurator.configure();
		final Logger LOGGER = Logger.getLogger("Logger");
		
		// Load Persistence Manager Factory - referencing the Persistence Unit defined in persistence.xml
		PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
		// Persistence Manager
		PersistenceManager pm = null;
		//Transaction to group DB operations
		Transaction tx = null;		
	    
	    Usuario elDani = new Usuario();
	    elDani.setEmail("danaso@opendeusto.es");
	    elDani.setPassword("patata");
	    
	    
	    Usuario elCorno = new Usuario();
	    elCorno.setEmail("elCorno@gmail.com");
	    elCorno.setPassword("danonino");
	    
	    
		//GUARDADO DE DATOS
		//GUARDADO DE DATOS
		//GUARDADO DE DATOS
		//GUARDADO DE DATOS
		try {
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();	
			tx.begin();
			
			pm.makePersistent(elDani);
			pm.makePersistent(elCorno);
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error("Error guardando datos " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}
		
		//SELECCIONAR DATOS
		//SELECCIONAR DATOS
		//SELECCIONAR DATOS
		//SELECCIONAR DATOS
		try {
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();
			tx.begin();
			
			@SuppressWarnings("unchecked")
			Query<Usuario> query1 = pm.newQuery("SELECT FROM "+Usuario.class.getName());
			@SuppressWarnings("unchecked")
			List<Usuario> usuarios = (List<Usuario>) query1.execute();

			tx.commit();
			
			LOGGER.info(" - Usuarios guardados en la BD:");
			for (Usuario usuario : usuarios) LOGGER.info("  -> " + usuario.getEmail() + ", "+ usuario.getPassword());
			
		} catch (Exception ex) {
			LOGGER.error(" $ Error seleccionando aeropuertos usando 'Query': " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}

		//BORRADO DE DATOS
		//BORRADO DE DATOS
		//BORRADO DE DATOS
		//BORRADO DE DATOS
		try {			
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();
			tx.begin();

			@SuppressWarnings("unchecked")
			Query<Usuario> query = pm.newQuery("SELECT FROM "+Usuario.class.getName());
			for(Usuario u : query.executeList()) {
				LOGGER.info(" - Borrando de la BD: "+u.toString());
				pm.deletePersistent(u);
			}
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error(" $ Error deleting 'User->Address' relation: " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}
		
		//ACTUALIZAR DATOS
		//ACTUALIZAR DATOS
		//ACTUALIZAR DATOS
		//ACTUALIZAR DATOS
		try {			
			pm = pmf.getPersistenceManager();
			tx = pm.currentTransaction();	
			tx.begin();

			LOGGER.info("");
			LOGGER.info(" - Cambiando password danaso@opendeusto.es de patata -> tomate ");
			elDani.setPassword("tomate");
			pm.makePersistent(elDani);
			
			tx.commit();
		} catch (Exception ex) {
			LOGGER.error(" $ Error updating  'patata -> tomate': " + ex.getMessage());
		} finally {
			if (tx != null && tx.isActive()) tx.rollback();
			if (pm != null && !pm.isClosed()) pm.close();
		}

	}

}