package constant;

import java.io.File;

public class UPath {
	public static String domain="http://129.89.35.212/";
	public static String data_path = "udata";
	public static String getSlash() {
		String os = System.getProperty("os.name");
		if (os.startsWith("Windows")) {
			return "\\";
		} else {
			return "/";
		}
	}

	public static String getFileABSPath() {
		UPath ptemp = new UPath();
		String path = ptemp.getClass().getResource("/").getPath();
		String os = System.getProperty("os.name");
		File file = new File(path + "test.txt");
		path = file.getParent();
		File file1 = new File(path + "test1.txt");
		path = file1.getParent();
		File file2 = new File(path + "test1.txt");
		path = file2.getParent();
		File file3 = new File(path + "test1.txt");
		path = file3.getParent();

		if (os.startsWith("Windows")) {
			path = path + "\\"+data_path+"\\";
			return path.replaceAll("%20", " ");
		} else {
			path = path + "/"+data_path+"/";
			return path.replaceAll("%20", " ");
		}
	}
	
	public static String getDefaultProfileImage() {
		return UPath.data_path + "/image/face.png";
	}

	public static String getDefaultProfileBGImage() {
		return UPath.data_path + "/image/bg.jpg";
	}

}
