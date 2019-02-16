import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComparatorDumpGolden {
    static List<Long> golden;
    static List<Long> dump;

    static List<Long> createList(String path) throws FileNotFoundException {
        File file = new File(Paths.get(path).toUri());

        System.out.println(file.getAbsolutePath());
        InputStream is = new FileInputStream(file);
        String str = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining(" "));
        ArrayList<Long> result = new ArrayList<>();
            String[] array = str.trim().split(" ");
            for (String val : array) {
                result.add(Long.valueOf(val));
            }
        return result;
    }

    static Map<Long, String> compare(List<Long> golden, List<Long> dump) {
        Map<Long, String> result = new HashMap<>();
        int min = golden.size() < dump.size() ? golden.size() : dump.size();
        System.out.println(min);
        System.out.println(dump.size());
        System.out.println(golden.size());
        for (int i = 0; i < min; i++) {
            if (!golden.get(i).equals(dump.get(i))) result.put(new Long(i),  golden.get(i).toString() + "-" + dump.get(i).toString());
        }
        return result;
    }

    public static void main(String[] args) throws FileNotFoundException {
        golden = createList("C:\\Users\\Ксения\\PSE\\pscx_emulator\\pscx_emulator\\golden\\golden_result.txt");
        dump = createList("C:\\Users\\Ксения\\PSE\\pscx_emulator\\pscx_emulator\\dump_output.txt");
        System.out.println(compare(golden, dump));
    }

}
