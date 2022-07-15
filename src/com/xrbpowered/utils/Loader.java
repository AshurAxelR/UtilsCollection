package com.xrbpowered.utils;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.swing.JPanel;

public abstract class Loader {

	public static InputStream openStream(String uri) throws IOException {
		InputStream in = ClassLoader.getSystemResourceAsStream(uri);
		if(in==null)
			in = new FileInputStream(new File(uri));
		return in;
	}
	
	public static byte[] loadBytes(InputStream s) throws IOException {
		DataInputStream in = new DataInputStream(s);
		byte bytes[] = new byte[in.available()];
		in.readFully(bytes);
		in.close();
		return bytes;
	}

	public static byte[] loadBytes(String uri) throws IOException {
		return loadBytes(openStream(uri));
	}

	public static String loadString(InputStream s) throws IOException {
		return new String(loadBytes(s), StandardCharsets.UTF_8);
	}

	public static String loadStringDef(InputStream s, String def) {
		try {
			return loadString(s);
		}
		catch (IOException e) {
			return def;
		}
	}

	public static String loadString(String uri) throws IOException {
		return loadString(openStream(uri));
	}

	public static String loadStringDef(String uri, String def) {
		try {
			return loadString(uri);
		}
		catch (IOException e) {
			return def;
		}
	}
	
	private static final MediaTracker imageAssist = new MediaTracker(new JPanel());
	
	public static BufferedImage loadImage(InputStream s) throws IOException {
		Image img = Toolkit.getDefaultToolkit().createImage(loadBytes(s));
		try {
			imageAssist.addImage(img, 1);
			imageAssist.waitForID(1);
			imageAssist.removeImage(img);
		}
		catch(InterruptedException e) {
			throw new IOException("Interrupted while loading image");
		}
		BufferedImage bufImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		bufImg.getGraphics().drawImage(img, 0, 0, null);
		return bufImg;
	}

	public static BufferedImage loadImage(String uri) throws IOException {
		return loadImage(openStream(uri));
	}

	public static Font loadFont(InputStream s) throws IOException {
		try {
			Font font = Font.createFont(Font.TRUETYPE_FONT, s);
			s.close();
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
			return font;
		} catch(FontFormatException e) {
			throw new IOException("Bad font format");
		}
	}
	
	public static Font loadFont(String uri) throws IOException {
		return loadFont(openStream(uri));
	}

}