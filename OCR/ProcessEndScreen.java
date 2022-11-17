package OCR;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

public class ProcessEndScreen implements Runnable {

	Engine e;

	public ProcessEndScreen(Engine e) {
		this.e = e;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.currentThread().sleep(500);
				e.CaptureScreen(true);
				String text = e.execTessaract(true, Engine.tmpdir + "/tempendscreen.png", Engine.player);
				if (text.length() > 1) {
					
					
					//System.out.println(text);
					int dmg = processEndGame(text, Engine.player);
					if (dmg == -1)
						continue;
					e.damage += dmg;
					//e.dmgdis.setText("Damage: " + e.damage);

					File dest = new File("endscreen.png");
					dest.delete();
					Engine.copyFile(new File(Engine.tmpdir + "/tempendscreen.png"), dest);

					Thread.currentThread().sleep(10000);

				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private int processEndGame(String text, String player) {
		try {
			String[] splited = text.split(" ");
			if (splited.length < 2)
				return -1;
			if (splited[0].contains(player))
				return Integer.parseInt(splited[2].replaceAll("[\\D]", ""));
			else
				return Integer.parseInt(splited[3].replaceAll("[\\D]", ""));
		} catch (Exception e) {
			// System.err.println(e.getMessage());
			// e.printStackTrace();
			System.out.println("Split: " + text);
			return -1;
		}

	}
}
