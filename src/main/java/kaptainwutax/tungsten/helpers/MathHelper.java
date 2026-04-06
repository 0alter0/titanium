package kaptainwutax.tungsten.helpers;

public class MathHelper {

	public static double roundToPrecision(double value, int precision) {
		    double scale = Math.pow(10, precision);
		    return Math.round(value * scale);
	}
}
