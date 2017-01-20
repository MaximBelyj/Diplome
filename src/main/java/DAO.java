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

     private final static String QUERY_SELECT_SUM_CHOOSE = "SELECT SUM(bytes) as max FROM ipcaddump WHERE sourceip = ? AND destip = ? AND destport = ?;";
     private final static String QUERY_SELECT_SUM_ALL = "SELECT SUM(bytes) as max FROM ipcaddump WHERE sourceip = ? AND destip = ?;";
     private final static String QUERY_SELECT_BYTES = "SELECT bytes FROM ipcaddump WHERE sourceip = ? AND destip = ? AND destport = ?;";
     private final static String QUERY_SELECT_ALL_BYTES = "SELECT SUM(bytes) as max FROM ipcaddump WHERE destport = ?;";


     private static Connection connection;
     private static PreparedStatement preparedStatement;

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

    public  <E extends Long> long getSumBytes3Args(String IpSource, String IpDestination, String PortDestination) throws SQLException {

        preparedStatement = connection.prepareStatement(QUERY_SELECT_SUM_CHOOSE);
        preparedStatement.setString(1, IpSource);
        preparedStatement.setString(2, IpDestination);
        preparedStatement.setString(3, PortDestination);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){

            sumChooseBytes = resultSet.getLong("max");

        }

        return sumChooseBytes;
    }

    public  <E extends Long> long getSumBytes2Arg(String IpSource, String IpDestination) throws SQLException {

        preparedStatement = connection.prepareStatement(QUERY_SELECT_SUM_ALL);
        preparedStatement.setString(1, IpSource);
        preparedStatement.setString(2, IpDestination);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){

            sumChooseBytes = resultSet.getLong("max");

        }

        return sumChooseBytes;
    }

    public List<Long> getBytes(String IpSource, String IpDestination, String PortDestination) throws SQLException {

        List<Long> listBytes = new ArrayList<Long>();
        long bytes = 0;

        preparedStatement = connection.prepareStatement(QUERY_SELECT_BYTES);
        preparedStatement.setString(1, IpSource);
        preparedStatement.setString(2, IpDestination);
        preparedStatement.setString(3, PortDestination);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){

            bytes = resultSet.getLong("bytes");
            listBytes.add(bytes);

        }

        return listBytes;
    }

    public  <E extends Long> long getAllBytes(String destport) throws SQLException {

        preparedStatement = connection.prepareStatement(QUERY_SELECT_ALL_BYTES);
        preparedStatement.setString(1, destport);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){

            sumAllBytes = resultSet.getLong("max");

        }

        return sumAllBytes;
    }

    public float getAnomaly(long sumAllBytes, long sumChooseBytes){

        return (float)(sumChooseBytes*100)/sumAllBytes;

    }
}
