import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Culc {

    public static void main(String[] args) throws SQLException {

        List<String> list = new ArrayList<String>();
        System.out.println("Программа обнаружения аномалий. ver.1.0");
        System.out.println("Введите ip источника");
        Scanner ipSource = new Scanner(System.in);
        list.add(ipSource.next());
        System.out.println("Введите ip назначения");
        Scanner ipDest = new Scanner(System.in);
        list.add(ipDest.next());
        System.out.println("Введите порт назначения");
        Scanner portDest = new Scanner(System.in);
        list.add(portDest.next());

        /*long a = DAO.getDaoInstance().getSumBytes3Args(list.get(0), list.get(1), list.get(2));
        System.out.println("Количество байт с этого сокета " + a + " байт");

        long b = DAO.getDaoInstance().getAllBytes(list.get(2));
        System.out.println("Количество байт за время мониторинга " + b + " байт");

        float c = DAO.getDaoInstance().getAnomaly(b, a);
        System.out.println("Процент трафика " + c + " %");*/

        List<Long> traffic = DAO.getDaoInstance().getBytes(list.get(0), list.get(1), list.get(2));

        Culculator.showResult(traffic);
      //  Culculator.showResult();

    }
}
