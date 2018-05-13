import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.logging.Level;

public class BashRunner {
	private static final Logger log = Logger.getLogger(BashRunner.class
			.getName());

	public static void run(String comand) {

		Runtime run = Runtime.getRuntime();

		try {
			String[] env = new String[] { "path=%PATH%;D:/Apps/SSH/bin/" };
			Process proc = run.exec(new String[] { "mintty.exe", "-c", comand },
					env);
			proc.waitFor();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			while (br.ready())
				System.out.println(br.readLine());
		} catch (Exception e) {
			log.log(Level.SEVERE, "failed " + comand + " ", e);
		}
	}
}