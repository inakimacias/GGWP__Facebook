//package remote;
//
//import dao.DataAccessObject;
//
//public class ValidationService {
//	
//	private static ValidationService instance = null;
//	private DataAccessObject dao = null;
//	
//	public ValidationService getInstance() {
//		if (instance == null) {
//			instance = new ValidationService();
//		}		
//		
//		return instance;
//	}
//	
////	public String validate(String email, String password) {
////		String btos;
////		boolean b = dao.validarUsuario(email, password);
////		
////		if(b==true) {
////			btos="true";
////		}else {
////			btos="false";
////		}
////		
////		String myOut=btos+";"+email;
////
////		return myOut;
////	}
//}
