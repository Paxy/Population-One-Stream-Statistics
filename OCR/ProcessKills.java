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

public class ProcessKills implements Runnable {

	Engine e;

	public ProcessKills(Engine e) {
		this.e = e;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.currentThread().sleep(500);
				e.CaptureScreen(false);

				String text = e.execTessaract(false, Engine.tmpdir + "/myScreenShot.png", Engine.player);
				if (text.contains(Engine.player)) {
					System.out.println(text);
					int c = countKills(text);
					if (c == -1)
						continue;
					e.kills++;
					System.err.println("Kills: " + e.kills + ", Deaths: " + e.deaths);
					e.kdisp.setText("Kills: " + e.kills);
					e.kdisp.setData(e.killed);
					System.out.println(e.killed);
					Thread.currentThread().sleep(6000);
				} else if (text.contains("Knocked down by")) {
					System.out.println(text);
					int c = countDied(text);
					if (c == -1)
						continue;
					e.deaths++;
					System.err.println("Kills: " + e.kills + ", Deaths: " + e.deaths);
					e.ddisp.setText("Deaths: " + e.deaths);
					e.ddisp.setData(e.died);
					System.out.println(e.died);
					Thread.currentThread().sleep(10000);
				} else {
					// if (text.length() > 0)
					// System.out.println("Debug:" + text);
					// Thread.currentThread().sleep(500);

				}

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private int countDied(String text) {
		//text = text.substring(text.length() - 4);
		text = getMatch(e.guns, text);
		if (text == null)
			if (Engine.allowUnknown)
				text = "Unknown";
			else
				return -1;

		for (String gun : e.guns) {
			if (text.contains(gun))
				if (e.died.containsKey(gun))
					e.died.put(gun, e.died.get(gun) + 1);
				else
					e.died.put(gun, 1);
		}
		e.died = Engine.sortByValue(e.died);
		return 0;
	}

	private int countKills(String text) {
		//text = text.substring(text.length() - 4);
		text = getMatch(e.guns, text);
		if (text == null)
			if (Engine.allowUnknown)
				text = "Unknown";
			else
				return -1;

		for (String gun : e.guns) {
			if (text.contains(gun))
				if (e.killed.containsKey(gun))
					e.killed.put(gun, e.killed.get(gun) + 1);
				else
					e.killed.put(gun, 1);
		}
		e.killed = Engine.sortByValue(e.killed);
		return 0;
	}

	private String getMatch(String[] guns2, String text) {
		String[] split = text.split(" ");
		String out = null;
		for (String string : split) {
			String tmp = getTheClosestMatch(e.guns, string);
			if (tmp != null)
				out = tmp;
			//System.out.println(tmp);
		}

		return out;
	}

	public String getTheClosestMatch(String[] collection, Object target) {
		try {
			int distance = Integer.MAX_VALUE;
			String closest = null;
			for (String compareObject : collection) {
				int currentDistance = StringUtils.getLevenshteinDistance(compareObject.toString(), target.toString());
				if (currentDistance < distance) {
					distance = currentDistance;
					closest = compareObject;
				}
			}
			if (distance > 2)
				return null;
			return closest;
		} catch (Exception e) {
			e.printStackTrace();
			return "Unknown";
		}
	}

}
