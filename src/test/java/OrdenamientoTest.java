
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JAST
 */
public class OrdenamientoTest {

    public OrdenamientoTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {
        List<String> participantes = new ArrayList<String>();
        for (int i = 1; i <= 20; i++) {
            participantes.add("Participante " + i);
        }
        for (String s : participantes) {
            System.out.println(s);
        }
        int iteraciones = participantes.size() * 5;
        for (int i = 1; i <= iteraciones; i++) {
            int p = (int) (Math.random() * participantes.size());
            System.out.println("Número aleatorio:" + p);
            participantes.add(participantes.remove(p));
        }
        System.out.println("Se desordenan!");
        int i = 0;
        while (i < participantes.size()) {
            if (i == participantes.size() - 1) {
                System.out.println(participantes.get(i) + " - " + participantes.get(0));
            } else {
                System.out.println(participantes.get(i) + " - " + participantes.get(i + 1));
            }
            i++;
        }
    }
}
