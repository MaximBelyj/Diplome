import java.util.List;

class Culculator {
    
     static double culcMhat(double t){

        double exp = Math.exp(-(Math.pow(t, 2))/2);
        return exp*(1 - Math.pow(t, 2));

    }

     static double culcFi(double a, double b, double t){

        double koeff = 1/Math.sqrt(a);
        double func = Culculator.culcMhat((t-b)/a);
        return koeff*func;

    }

     static double culcWevlet(double a, double b, List<Long> list){

        double sum = 0;

        for (int t = 0; t < b; t++){

            double x = Culculator.culcFi(a, b, t) * list.get(t);
            sum += x;
        }

        return sum;
    }

     static void showResult(List<Long> list){

        for (double t = 1; t <= 100; t++) {
            for (double a = 1; a <= 15; a++) {
                double res = Culculator.culcWevlet(a, t, list);
                System.out.println(res);
                if (a == 15){
                    System.out.println("t= " + t);
                }
            }
        }
    }
}
