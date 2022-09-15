package Data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import Objects.Character;
import Objects.Coin;
import Objects.Goal;
import Objects.Platform;
import Objects.Spike;
import Objects.Water;
import Util.Encryption;
import Util.Globals;
import processing.core.PApplet;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Save {
	File f;
	final JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());

	public Save(PApplet sketch) {
		fc.setFileFilter(new FileNameExtensionFilter("DAT Files", "dat"));
		int r = fc.showSaveDialog(null);
		if (r == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
			JSONObject json = new JSONObject();
			json.setInt("bgcolor", Globals.BG_COLOR);
			json.setBoolean("playonly", Globals.TEMP_PLAY_ONLY);
			json.setJSONArray("objects", new JSONArray());
			for (int i = 0; i < Globals.objects.length; i++) {
				if (Globals.objects[i] != null) {
					String name = Globals.objects[i].getClass().getSimpleName();
					JSONObject object = new JSONObject();
					switch (name) {
						case "Character":
							Character c = (Character)Globals.objects[i];
							object.setString("name", c.getClass().getSimpleName());
							object.setInt("mass", c.mass);
							object.setFloat("width", c.width);
							object.setFloat("height", c.height);
							object.setInt("x", (int) c.position.x);
							object.setInt("y", (int) c.position.y);
							json.getJSONArray("objects").append(object);
							break;
						case "Platform":
							Platform p = (Platform)Globals.objects[i];
							object.setString("name", p.getClass().getSimpleName());
							object.setFloat("width", p.width);
							object.setFloat("height", p.height);
							object.setInt("x", (int) p.position.x);
							object.setInt("y", (int) p.position.y);
							object.setInt("color", p.getColor());
							json.getJSONArray("objects").append(object);
							break;
						case "Water": 
							Water w = (Water)Globals.objects[i];
							object.setString("name", w.getClass().getSimpleName());
							object.setFloat("width", w.width);
							object.setFloat("height", w.height);
							object.setInt("x", (int) w.position.x);
							object.setInt("y", (int) w.position.y);
							json.getJSONArray("objects").append(object);
							break;
						case "Coin": 
							Coin c2 = (Coin)Globals.objects[i];
							object.setString("name", c2.getClass().getSimpleName());
							object.setInt("x", (int) c2.position.x);
							object.setInt("y", (int) c2.position.y);
							json.getJSONArray("objects").append(object);
							break;
						case "Goal":
							Goal g = (Goal)Globals.objects[i];
							object.setString("name", g.getClass().getSimpleName());
							object.setFloat("width", g.width);
							object.setFloat("height", g.height);
							object.setInt("x", (int) g.position.x);
							object.setInt("y", (int) g.position.y);
							json.getJSONArray("objects").append(object);
							break;
						case "Spike":
							Spike s = (Spike)Globals.objects[i];
							object.setString("name", s.getClass().getSimpleName());
							object.setFloat("size", s.size);
							object.setInt("x", (int) s.position.x);
							object.setInt("y", (int) s.position.y);
							json.getJSONArray("objects").append(object);
							break;
						default:
							break;
					}
				}
			}
			String source = Encryption.Encrypt(json.toString().replace(" ", "").replace("\n", ""));
			try {
				f.createNewFile();
				// make sure that it is .dat file type
				if (!f.getName().endsWith(".dat")) {
					f = new File(f.getAbsolutePath() + ".dat");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try (FileWriter fw = new FileWriter(f)) {
				fw.write(source);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
