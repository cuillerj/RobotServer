
public class GetSqlConnection {
	public static final String connectionUser = "jean";
	public static final String connectionPassword = "manu7890";
	public static final String domotiqueUrl = "jdbc:mysql://jserver:3306/domotiquedata";
	public static final String meteoUrl = "jdbc:mysql://jserver:3306/meteo";
	public static final String robotUrl = "jdbc:mysql://jserver:3306/robot";
	public static String GetUser() {
		// TODO Auto-generated method stub
		return connectionUser;
	}
	public static String GetPass() {
		return connectionPassword;
	}
	public static String GetDomotiqueDB() {
		return domotiqueUrl;
	}
	public static String GetMeteoDB() {
		return meteoUrl;
	}
	public static String GetRobotDB() {
		return robotUrl;
	}
}

