import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DAO {

    private static long sumAllBytes;
    private static long sumChooseBytes;

     /*
     * Здесь будет описано всё, что связано с
     * подключением к БД
     */

     private final static String URL = "jdbc:postgresql://localhost:5432/test";
     private final static String USERNAME = "postgres";
     private final static String PASSWORD = "123";

    /*
    * подключение к БД + запросы
    */

     private final static String QUERY_SELECT_1 = "SELECT SUM(bytes) as max FROM ipcaddump WHERE sourceip = ? AND destip = ? AND destport = ?;";
    private final static String QUERY_SELECT_2 = "SELECT SUM(bytes) as max FROM ipcaddump WHERE sourceip = ? AND destip = ?;";

    private final static String QUERY_SELECT_ALL_BYTES = "SELECT SUM(bytes) as max FROM ipcaddump;";


     private static Connection connection;
     private static PreparedStatement preparedStatement;
     private static Statement statement;

     private static final DAO DAO_INSTANCE = new DAO();

     private DAO() { // конструктор с установкой соединение с БД

       try{
           connection = DriverManager.getConnection(URL,USERNAME,PASSWORD); // устанавливаем соединение
       }catch (SQLException s){ // ловим ошибку
           s.printStackTrace();
       }
     }

     public static DAO getDaoInstance(){

       return DAO_INSTANCE;

     }

     public static void closeConnection() throws SQLException { //закрываем соединение

       try {
           connection.close();
           preparedStatement.close();

       } catch (SQLException s) {
             s.printStackTrace();
       }
     }

    /*
    *  метод по извлечению байтов из БД
    */

    public  <E extends Long> long getSocketThreeArg(String IpSource, String IpDestination, String PortDestination) throws SQLException {

        preparedStatement = connection.prepareStatement(QUERY_SELECT_1);
        preparedStatement.setString(1, IpSource);
        preparedStatement.setString(2, IpDestination);
        preparedStatement.setString(3, PortDestination);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){

            sumChooseBytes = resultSet.getLong("max");

        }

        return sumChooseBytes;
    }

    public  <E extends Long> long getSocketTwoArg(String IpSource, String IpDestination) throws SQLException {

        preparedStatement = connection.prepareStatement(QUERY_SELECT_2);
        preparedStatement.setString(1, IpSource);
        preparedStatement.setString(2, IpDestination);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){

            sumChooseBytes = resultSet.getLong("max");

        }

        return sumChooseBytes;
    }

    public  <E extends Long> long getAllBytes() throws SQLException {

        statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_SELECT_ALL_BYTES);

        while (resultSet.next()){

            sumAllBytes = resultSet.getLong("max");

        }

        return sumAllBytes;
    }

    public float getAnomaly(long sumAllBytes, long sumChooseBytes){

        return (float)(sumChooseBytes*100)/sumAllBytes;

    }
}
