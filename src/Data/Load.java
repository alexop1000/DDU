package Data;

import java.io.BufferedReader;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.io.FileReader;
import java.sql.Connection;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.Scrollable;
import javax.swing.filechooser.FileSystemView;

import Objects.*;
import Objects.Character;
import Util.Encryption;
import Util.Globals;
import Util.Object;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Load {

	public static void Reader(PApplet sketch) {
		JFrame listHolder = new JFrame();
		listHolder.setSize(500, 500);
		listHolder.setLocationRelativeTo(null);
		listHolder.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		listHolder.setLayout(null);
		listHolder.setVisible(true);

		DefaultListModel<String> listModel = new DefaultListModel<String>();
		JList<String> list = new JList<String>(listModel);
		list.setBounds(0, 0, 500, 500);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setBounds(0, 0, 500, 500);
		listScroller.add(list);
		listHolder.add(listScroller);

		// Load id and name from database
		ArrayList<String> sourceList = new ArrayList<String>();
		try {
			java.sql.Statement stmt = Globals.conn.createStatement();
			java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM levels");
			while (rs.next()) {
				String id = rs.getString("id");
				String name = rs.getString("name");
				// Add to table
				listModel.addElement(id + " - " + Encryption.Decrypt(name));
				sourceList.add(Integer.parseInt(id) - 1, rs.getString("data"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Refresh visible list
		try {
			list.updateUI();
			list.setVisibleRowCount(100);
			list.updateUI();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Load level from database
		list.addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				String selected = (String) list.getSelectedValue();
				String[] split = selected.split(" - ");
				String id = split[0];
				String name = split[1];
				String data = sourceList.get(Integer.parseInt(id) - 1);
				// Load level
				Load.Level(sketch, data);
				listHolder.dispose();
			}
		});

	}
	public static void Level(PApplet sketch, String source) {
		for (int i = 0; i < Globals.objects.length; i++) {
			Globals.objects[i] = null;
		}

		JSONObject json = JSONObject.parse(source);
		Globals.BG_COLOR = json.getInt("bgcolor");
		if (json.hasKey("playonly") && json.getBoolean("playonly") == true) {
			Globals.PLAY_ONLY = true;
		}
		if (json.hasKey("nextlevel")) {
			Globals.NEXT_LEVEL_NAME = json.getString("nextlevel");
		}
		JSONArray objects = json.getJSONArray("objects");
		for (int i = 0; i < objects.size(); i++) {
			JSONObject object = objects.getJSONObject(i);
			switch (object.getString("name")) {
				case "Character":
					Character newChar = new Character(sketch, 
						object.getInt("width"), 
						object.getInt("height"),
						new PVector(object.getInt("x"), object.getInt("y"))
					);
					if (object.hasKey("gravity")) {
						newChar.gravity = object.getFloat("gravity");
						newChar.speed = object.getFloat("speed");
						newChar.jumpPower = object.getFloat("jumppower");
					}
					InsertEmpty(newChar);
					break;
				case "Platform":
					Platform newPlatform = new Platform(sketch,
						object.getInt("width"), 
						object.getInt("height"),
						new PVector(object.getInt("x"), object.getInt("y"))
					);
					newPlatform.setColor(object.getInt("color"));
					InsertEmpty(newPlatform);
					break;
				case "Water":
					Water newWater = new Water(sketch,
						object.getInt("width"), 
						object.getInt("height"),
						new PVector(object.getInt("x"), object.getInt("y"))
					);
					newWater.defaultPosition = new PVector(object.getInt("x"), object.getInt("y"));
					InsertEmpty(newWater);
					break;
				case "Coin":
					Coin newCoin = new Coin(sketch, false,
						new PVector(object.getInt("x"), object.getInt("y"))
					);
					if (object.hasKey("reward")) {
						newCoin.coinReward = object.getInt("reward");
					}
					InsertEmpty(newCoin);
					break;
				case "Goal":
					Goal newGoal = new Goal(sketch,
						new PVector(object.getInt("x"), object.getInt("y"))
					);
					newGoal.width = object.getInt("width");
					newGoal.height = object.getInt("height");
					InsertEmpty(newGoal);
					break;
				case "Spike":
					Spike newSpike = new Spike(sketch,
						new PVector(object.getInt("x"), object.getInt("y")), object.getInt("size")
					);
					InsertEmpty(newSpike);
					break;
				default:
					break;
			}
		}
	}

	public static void InsertEmpty(Object newItem) {
		for (int i = 0; i < Globals.objects.length; i++) {
			if (Globals.objects[i] == null) {
				Globals.objects[i] = newItem;
				break;
			}
		}
	}
}
