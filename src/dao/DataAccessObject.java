package dao;

import java.util.Map;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DataAccessObject {
	private static Logger LOGGER;
	private static DataAccessObject instance = new DataAccessObject();
	private HashMap<String,String> usuarios;
	
	public DataAccessObject() {
		BasicConfigurator.configure();
		LOGGER = Logger.getLogger("Logger");
		usuarios = new HashMap<String,String>();
		usuarios.put("DanielDiez","elDani");
		usuarios.put("Inakito","elInaki");
		usuarios.put("Rubencin","elRuben");
		usuarios.put("Albertillo","elAlberto");
	}
	
	public static DataAccessObject getInstance() {
		return instance;
	}
	
	public void guardarUsuario(String nick, String password) {
		LOGGER.info("-DAO- Guardando usuario");
		usuarios.put(nick, password);
	}
	
	public void borrarUsuario(String nick) {
		LOGGER.info("-DAO- Borrando usuario");
		usuarios.remove(nick);
	}
	
	public void mostrarUsuarios() {
		LOGGER.info("-DAO- Mostrando todos los usuarios");
		for(Map.Entry<String,String> entry : usuarios.entrySet()) {
			System.out.println("nick: "+entry.getKey()+"\npassword: "+entry.getValue());
		}
	}
	
//	public boolean validarUsuario(String nick, String password) {
//		boolean result = false;
//		System.out.println("HI THERE");
//		LOGGER.info("-DAO- Validando usuario");
//		if(usuarios.containsKey(nick)) {
//			if(usuarios.get(nick).equals(password)) {
//				result = true;
//			}
//		}
//		return result;
//	}
	
	public HashMap<String, String> getUsuarios() {
		return usuarios;
	}
	
	public void actualizarPassword(String nick, String newPass) {
		LOGGER.info("-DAO- Actualizando password");
		usuarios.replace(nick, newPass);
	}
	
	public void deleteData() {
		LOGGER.info("-DAO- Borrando todos los usuarios");
		usuarios.clear();
	}
}
