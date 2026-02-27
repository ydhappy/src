package lineage.world.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import lineage.plugin.PluginController;
import lineage.share.TimeLine;

public class ScriptController {

	private static ScriptEngineManager sem;
	private static Map<String, ScriptEngine> engines;
	
	public static void init() {
		TimeLine.start("ScriptController...");
		
		sem = new ScriptEngineManager();
		engines = new HashMap<String, ScriptEngine>();
		synchronized (engines) {
			try {
				read(null);
			} catch (Exception e) { }
		}
		
		TimeLine.end();
		
		//
		PluginController.init(ScriptController.class, "init");
	}
	
	public static void close() {
		//
		PluginController.init(ScriptController.class, "close");
		//
		synchronized (engines) {
			engines.clear();
		}
	}
	
	private static void read(File file) {
		File[] list = file==null ? new File("scripts").listFiles() : new File(file.getAbsolutePath()).listFiles();
		for(File f : list) {
			if( f.isDirectory() ) {
				read(f);
			} else if(f.getName().endsWith(".js")) {
				String path = f.getAbsolutePath().replaceAll("\\\\", "/");
				FileReader fr = null;
		        try {
		        	//
		            ScriptEngine engine = null;
		            File scriptFile = new File(path);
		            if (scriptFile.exists()) {
			            // 
		            	fr = new FileReader(scriptFile);
			            engine = sem.getEngineByName("nashorn");
//		            	engine = sem.getEngineByName("rhino");
			            engine.eval(fr);
			            //
			            engines.put(path.substring(path.indexOf("/scripts/")+9), engine);
		            }
		        } catch (Exception e) {
		        } finally {
		            try {
		                if (fr != null)
		                    fr.close();
		            } catch (IOException ignore) { }
		        }
			}
		}
	}
	
	public static Invocable find(String path) {
		synchronized (engines) {
			try {
				return (Invocable) engines.get(path);
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public static List<String> getKeys() {
		synchronized (engines) {
			return new ArrayList<String>(engines.keySet());
		}
	}
	
	public static List<ScriptEngine> getValues() {
		synchronized (engines) {
			return new ArrayList<ScriptEngine>(engines.values());
		}
	}
}
