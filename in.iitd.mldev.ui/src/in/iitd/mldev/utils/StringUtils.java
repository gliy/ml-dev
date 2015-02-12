package in.iitd.mldev.utils;

import java.util.Collection;

public final class StringUtils {

	private StringUtils () { /* Not intended to be instanciated */ }

	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public static boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String safeString(String s) { return (s == null) ? "" : s; }

	
	public static String join(Collection<? extends Object> objs, String sep) {
		StringBuilder bu = new StringBuilder();
		if(objs != null) {
			if(objs.isEmpty()) {
				return "";
			}
			for (Object obj : objs) {
				bu.append(obj).append(sep);
			}
		}
		return bu.toString().substring(0, bu.length() - sep.length());
	}
}
