import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection{
    private static final String URL= "jdbc:mysql://localhost:3306/hy360_payroll_Database"; // H bash prepei na onomastei 'payroll_db' h allaje to
    private static final String USER= "root"; // To username ths mysql (synhthws root)
    private static final String PASS= "";     // To password ths mysql

    public static Connection getConnection(){
        try {
            //Fortosi tou driver (prepei na yparxei to mysql-connector jar sto project)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e){
            System.out.println("Sfalma syndeshs me th vash. Elegje ta URL, USER, PASS.");
            e.printStackTrace();
            return null;
        }
    }
}