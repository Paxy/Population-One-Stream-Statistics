package OCR;

import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

public class ProcessStats implements Runnable {

	Engine e;
	HashMap<String, Integer> initdata;
	HashMap<String, Integer> newdata;

	public ProcessStats(Engine e) {
		this.e = e;
		initdata = new HashMap<String, Integer>();
		newdata = new HashMap<String, Integer>();
	}

	@Override
	public void run() {
		while (true) {
			try {
				JSONObject user = JsonReader.getUser(e.fabUserId);
				JSONArray stats = (JSONArray) user.get("playerStatistics");

				for (int i = 0; i < stats.length(); i++) {
					JSONObject stat = (JSONObject) stats.get(i);

					String name = stat.getString("statisticName");
					int val = stat.getInt("value");

					if (initdata.get(name) == null)
						initdata.put(name, val);
					if (name.equals("PlayerSkill"))
						newdata.put(name, val);
					else if (name.equals("MMR1"))
						newdata.put(name, val);
					else
						newdata.put(name, val - initdata.get(name));

				}

				String display = "Games: " + newdata.get("CareerGamesPlayed") + "&emsp;";
				display += "Wins: " + newdata.get("CareerWins");
				if (newdata.get("CareerGamesPlayed") > 0)
					display += " ("
							+ (Math.round(newdata.get("CareerWins") * 1000 / newdata.get("CareerGamesPlayed")) / 10.0)
							+ " %)";
				display += "<br>";
				display += "Damage: " + newdata.get("CareerDamage");
				if (newdata.get("CareerGamesPlayed") > 0)
					display += ", DmgPerGame: "
							+ (Math.round(newdata.get("CareerDamage") / newdata.get("CareerGamesPlayed")));
				display += "<br>";
				display += "Kills: " + newdata.get("CareerKills");
				if (newdata.get("CareerGamesPlayed") > 0)
					display += "&emsp;KillsPerGame: "
							+ (Math.round(newdata.get("CareerKills") * 10 / newdata.get("CareerGamesPlayed")) / 10.0);
				display += "<br>";

				display += "Skill: " + newdata.get("PlayerSkill") + "&emsp;" + "MMR: " + newdata.get("MMR1") + "<br>";

				e.dmgdis.setText("<html>" + display + "</html>");

				//update kill counter
				int diff = e.kills - newdata.get("CareerKills");
				e.kills = newdata.get("CareerKills");
				if (e.killed.get("Unknown") == null)
					e.killed.put("Unknown", diff);
				else
					e.killed.put("Unknown", e.killed.get("Unknown") + diff);
				e.killed = Engine.sortByValue(e.killed);
				e.kdisp.setText("Kills: " + e.kills);
				e.kdisp.setData(e.killed);

				// System.out.println(newdata);

				Thread.currentThread().sleep(30000);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
