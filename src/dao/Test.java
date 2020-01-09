package dao;

public class Test {
	public static void main(String[] args) {
		DataAccessObject dao = DataAccessObject.getInstance();
		
		dao.mostrarUsuarios();
		
		dao.actualizarPassword("DanielDiez", "amapola");
		
		dao.borrarUsuario("Rubencin");
		
		dao.mostrarUsuarios();
	}

}