package wiliberm.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config extends Properties {
	private static final long serialVersionUID = -7370592346467501634L;

	public Config() {
		super();
	}

	public Config(String path) {
		super();
		try {
			load(new FileInputStream(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getInt(String key, int defaultValue) {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (Exception e) {
			setProperty(key, defaultValue + "");
			return defaultValue;
		}
	}

	public double getDouble(String key, double defaultValue) {
		try {
			return Double.parseDouble(getProperty(key));
		} catch (Exception e) {
			setProperty(key, defaultValue + "");
			return defaultValue;
		}
	}

	public void setInt(String key, int value) {
		setProperty(key, value + "");
	}

	public void setDouble(String key, double value) {
		setProperty(key, value + "");
	}
}
