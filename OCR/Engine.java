package OCR;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

public class Engine {

	public static String player = "P-";
	public static String fabUserId="19D0BC07D0AB75E9";
	public static boolean allowUnknown = true;
	public static String[] guns = { "UMP", "CX4", "P90", "M1014", "MP5", "AKM", "DT11", "AWP", "Excalibur", "Knife",
			"1911", "Px4", "MK18", "357", "Frag", "Zone", "S85", "M6O", "Cyberblade", "Matadors", "FAL", "RFB", "Uzis",
			"Unknown" };

	static String tmpdir = System.getProperty("java.io.tmpdir");
	static String OS = System.getProperty("os.name").toLowerCase();
	static Executor pool = Executors.newFixedThreadPool(3);

	int kills = 0;
	int deaths = 0;
	int damage = 0;

	Robot r;
	HashMap<String, Integer> killed, died;
	Display kdisp;
	Display ddisp;
	Display dmgdis;

	public Engine() throws Exception {
		r = new Robot();

		killed = new HashMap<>();
		died = new HashMap<>();

		kdisp = new Display("Kills: " + kills, Color.GREEN);
		ddisp = new Display("Deaths: " + kills, Color.RED);
		dmgdis = new Display("Damage: " + damage, Color.CYAN, true);
		
		ProcessStats ps=new ProcessStats(this);
		pool.execute(ps);
		ProcessKills killCounter = new ProcessKills(this);
		pool.execute(killCounter);
		ProcessEndScreen endscreen = new ProcessEndScreen(this);
		pool.execute(endscreen);

	}

	public static void main(String[] args) throws Exception {
		new Engine();

	}

	public void CaptureScreen(boolean endscreen) throws IOException {
		Image i;
		BufferedImage image;
		String file;
		if (endscreen) {
			i = r.createScreenCapture(new Rectangle(500, 150, 800, 700));
			image = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_BGR);
			file = tmpdir + "/tempendscreen.png";
		} else {
			i = r.createScreenCapture(new Rectangle(500, 700, 700, 370));
			image = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
			file = tmpdir + "/myScreenShot.png";
		}
		Graphics g = image.getGraphics();
		g.drawImage(i, 0, 0, null);
		g.dispose();

		ImageIO.write(image, "png", new File(file));
	}

	public String execTessaract(boolean endgame, String file, String player) throws IOException {
		//
		// long start = System.currentTimeMillis();
		Process process;
		if (OS.contains("win")) {
			String browser = "C:\\Program Files\\Tesseract-OCR\\tesseract.exe";
			Runtime runtime = Runtime.getRuntime();
			process = runtime.exec(new String[] { browser, file, "stdout" });
		} else {
			process = Runtime.getRuntime().exec("tesseract " + file + " stdout");
		}

		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

		String line, msg = "", end = "";
		boolean getNext = false;
		while ((line = reader.readLine()) != null) {
			// System.out.println(line);

			if (getNext) {
				msg += line;
				getNext = false;
			}

			if (endgame) {
				if (line.contains(player)) {
					end = line;
					// System.out.println("found username");
				}
				// if (line.contains("MATCH TOTAL"))
				if (line.contains("TOTAL") || line.contains("SPECTATE")) {
					// System.out.println("total");
					return end;
				}
			} else if (line.contains("knocked") || line.contains("Knocked")) {
				msg += line + " ";
				if (line.contains(player))
					getNext = true;
			}
		}
		// System.out.println("Exec time: " + (System.currentTimeMillis() - start));
		return msg;

	}

	public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, Integer>> list = new LinkedList<>(hm.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<String, Integer> temp = new LinkedHashMap<>();
		for (Map.Entry<String, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	public static void copyFile(File from, File to) throws IOException {
		Files.copy(from.toPath(), to.toPath());
	}

}
