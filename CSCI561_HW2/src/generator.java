import java.io.IOException;
import java.io.PrintWriter;

public class generator {

    public static void main(String args[]) throws IOException, InterruptedException {
        try {
            PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
            String line = "";
            for (int i = 0; i!=10; i++) {
                line = "";
                int temp = i;
                for (int j = temp; j != 10; j++) {
                    line += Integer.toString(j%10);
                }
                if (i!=0) {
                    for (int k = 0; k != i; k++) {
                        line += Integer.toString(k);
                    }
                }
                System.out.println(line);
            }
            writer.println(line);
            writer.close();
        } catch (IOException e) {
        }
    }
}
