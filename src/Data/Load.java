package Data;

import java.io.BufferedReader;
import java.io.File;
import javax.swing.filechooser.FileFilter;
import java.io.FileReader;

import javax.swing.JFileChooser;
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
	File f;
	final JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView());

	public Load(PApplet sketch) {
		fc.setFileFilter(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if (f.isDirectory()) {
					return true;
				} else {
					String name = f.getName().toLowerCase();
					return name.endsWith(".dat");
				}
			}

			@Override
			public String getDescription() {
				return "DAT Files";
			}
		});
		int r = fc.showOpenDialog(null);
		if (r == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
			
			String source = "";
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String line;
				while ((line = br.readLine()) != null) {
					source += line;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < Globals.objects.length; i++) {
				Globals.objects[i] = null;
			}

			source = Encryption.Decrypt(source);

			JSONObject json = JSONObject.parse(source);
			JSONArray objects = json.getJSONArray("objects");
			for (int i = 0; i < objects.size(); i++) {
				JSONObject object = objects.getJSONObject(i);
				switch (object.getString("name")) {
					case "Character":
						Character newChar = new Character(sketch, 
							object.getInt("mass"), 
							object.getInt("width"), 
							object.getInt("height"),
							new PVector(object.getInt("x"), object.getInt("y"))
						);
						InsertEmpty(newChar);
						break;
					case "Platform":
						Platform newPlatform = new Platform(sketch,
							object.getInt("width"), 
							object.getInt("height"),
							new PVector(object.getInt("x"), object.getInt("y"))
						);
						newPlatform.color = object.getInt("color");
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
							new PVector(object.getInt("x"), object.getInt("y"))
						);
						newSpike.width = object.getInt("width");
						newSpike.height = object.getInt("height");
						InsertEmpty(newSpike);
						break;
					default:
						break;
				}
			}
		}
	}

	public void InsertEmpty(Object newItem) {
		for (int i = 0; i < Globals.objects.length; i++) {
			if (Globals.objects[i] == null) {
				Globals.objects[i] = newItem;
				break;
			}
		}
	}
}
