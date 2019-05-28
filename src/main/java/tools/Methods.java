package tools;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.imageio.ImageIO;
//import com.github.stuxuhai.jpinyin.PinyinFormat;
//import com.github.stuxuhai.jpinyin.PinyinHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
public class Methods {
	private static String __ENCODE__ = "GBK"; // 一定要是GBK

	private static String __SERVER_ENCODE__ = "GB2312"; // 服务器上的缺省编码
	private static final AtomicLong LAST_TIME_MS = new AtomicLong();

	public static String getUniqueID() {
		return UUID.randomUUID().toString().replaceAll("-", "fu");
	}
	
	public static List<String> convertStringtoList(String info)
	{		
		Gson gson = new Gson();
		
		List<String> result = gson.fromJson(info,  new TypeToken< ArrayList<String> >(){}.getType());
		return result;
	}

	public static String generateQRCodeImage(String text, int width, int height)
			throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
		BufferedImage image=MatrixToImageWriter.toBufferedImage(bitMatrix);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
        try {
            ImageIO.write(image, "PNG", bos);
            byte[] imageBytes = bos.toByteArray();
            String imageString ="data:image/png;base64,"+  Base64.getMimeEncoder().encodeToString(imageBytes);           
            bos.close();
            return imageString;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}

	public static long getStringTimeIDLong() {

		long now = System.currentTimeMillis();
		while (true) {
			long lastTime = LAST_TIME_MS.get();
			if (lastTime >= now)
				now = lastTime + 1;
			if (LAST_TIME_MS.compareAndSet(lastTime, now))
				return now;
		}
	}

	public static String getStringTimeID() {

		long now = System.currentTimeMillis();
		while (true) {
			long lastTime = LAST_TIME_MS.get();
			if (lastTime >= now)
				now = lastTime + 1;
			if (LAST_TIME_MS.compareAndSet(lastTime, now))
				return String.valueOf(now);
		}
	}

	public static String getCurrentTime() {

		Date now = new Date();

		int year = now.getYear() + 1900;
		int month = now.getMonth() + 1;
		int day = now.getDate();

		int hh = now.getHours();
		int mm = now.getMinutes();
		int ss = now.getSeconds();

		String clock = year + "-";

		if (month < 10) {
			clock = clock + "0";
		}

		clock = clock + month + "-";

		if (day < 10) {
			clock = clock + "0";
		}

		clock = clock + day + " ";

		if (hh < 10) {
			clock = clock + "0";
		}

		clock = clock + hh + ":";
		if (mm < 10) {
			clock = clock + '0';
		}
		clock = clock + mm + ":";
		if (ss < 10) {
			clock = clock + '0';
		}
		clock = clock + ss;
		return clock;
	}

	static int min = 50;
	static int max = 350;
	public static String names = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static int[] convertStringsToIntegers(String[] temp) {
		int[] result = new int[temp.length];
		for (int i = 0; i < temp.length; i++) {
			result[i] = Integer.parseInt(temp[i]);
		}
		return result;
	}

	public static String getAbbName(String fname) {
		String out = String.valueOf(fname.charAt(0));
		return out;

	}

	public static String genernateMD5(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());

			byte byteData[] = md.digest();

			// convert the byte to hex format method 1
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			return "";
		}

	}

	public static String encodeString(String var) {
		return new String(Base64.getMimeEncoder().encodeToString(var.getBytes()));
	}

	public static String decodeString(String var) {
		return new String(Base64.getMimeDecoder().decode(var.getBytes()));
	}

	public static String getGeneratedStringCode() {
		byte[] array = new byte[6]; // length is bounded by 7
		new Random().nextBytes(array);

		String generatedString = new String(array, Charset.forName("UTF-8"));
		return generatedString;
	}

	public static boolean isStringArrayContainString(String[] all, String value) {
		if (all != null && all.length > 0) {
			for (String s : all) {
				s = s.toLowerCase();
				value = value.toLowerCase();
				if (s.equals(value)) {
					return true;
				}
			}
		}
		return false;
	}

	public static void SaveBase64ToImageFile(String imageCode, String name) throws IOException {
		// String imageCode = "data:image/png;base64,iVBORw0KGgoAAAANSUhEU...";
		String base64Image = imageCode.split(",")[1];

		// Convert the image code to bytes.
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);

		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

		File imageFile = new File(name);

		ImageIO.write(bufferedImage, "jpg", imageFile);
	}

	public static int getRandomNumber() {
		Random random = new Random();
		long range = (long) max - (long) min + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * random.nextDouble());
		int randomNumber = (int) (fraction + min);
		return randomNumber;
	}

	public static int getRandomNumberFraction() {
		Random random = new Random();
		long range = (long) max - (long) min + 1;
		// compute a fraction of the range, 0 <= frac < range
		long fraction = (long) (range * random.nextDouble());
		int randomNumber = (int) (fraction + min);
		if (randomNumber > 100) {
			randomNumber = 100;
		}
		return randomNumber;
	}

	public static String getFileName(int ppid, String name) {
		String[] names = name.split("\\.");
		String out = names[0] + "_" + ppid;
		out += "." + names[1];
		return out;
	}

	public static Color getColor(int id, int type, int index) {
		int red = 42, green = 12, blue = 30;
		if (type == 0) {
			type++;
		}
		return new Color(id * red, type * green, blue * (index + 1));
	}

	public static String generateString(int length) {
		Random rng = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = names.charAt(rng.nextInt(names.length()));
		}
		return new String(text);
	}

	public static String[] convertListToArray(ArrayList<String> list) {
		String[] stockArr = new String[list.size()];
		stockArr = list.toArray(stockArr);
		return stockArr;
	}

	public static String getChinesePinyin(String name) {
//		String new_name = PinyinHelper.convertToPinyinString(name, "", PinyinFormat.WITHOUT_TONE);
//		return new_name.replaceAll("\\s+", ".");
		return "null";
	}

//	public static void main(String[] args) {
//		String name = "我的 世界 lol_new";
//		System.out.println(Methods.getChinesePinyin(name));
//	}

	public static void saveToFile(InputStream inStream, String target) throws IOException {
		OutputStream out = null;
		int read = 0;
		byte[] bytes = new byte[1024];
		out = new FileOutputStream(new File(target));
		while ((read = inStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		out.flush();
		out.close();
	}

	public static int compareChinese(String s1, String s2)

	{

		String m_s1 = null, m_s2 = null;

		try

		{

			// 先将两字符串编码成GBK

			m_s1 = new String(s1.getBytes(__SERVER_ENCODE__), __ENCODE__);

			m_s2 = new String(s2.getBytes(__SERVER_ENCODE__), __ENCODE__);

		}

		catch (Exception ex)

		{

			return s1.compareTo(s2);

		}

		int res = chineseCompareTo(m_s1, m_s2);

		// System.out.println("比较：" + s1 + " | " + s2 + "==== Result: " + res);

		return res;

	}

	// 获取一个汉字/字母的Char值

	public static int getCharCode(String s)

	{

		if (s == null && s.equals(""))
			return -1;// 保护代码
		byte[] b = s.getBytes();

		int value = 0;

		// 保证取第一个字符（汉字或者英文）

		for (int i = 0; i < b.length && i <= 2; i++)

		{

			value = value * 100 + b[i];

		}

		return value;

	}

	// 比较两个字符串

	public static int chineseCompareTo(String s1, String s2)

	{

		int len1 = s1.length();

		int len2 = s2.length();

		int n = Math.min(len1, len2);

		for (int i = 0; i < n; i++)

		{

			int s1_code = getCharCode(s1.charAt(i) + "");

			int s2_code = getCharCode(s2.charAt(i) + "");

			if (s1_code != s2_code)
				return s1_code - s2_code;

		}

		return len1 - len2;

	}

	public static void saveDataToFile(String data, String targetFile) {
		try {
			// write converted json data to a file named "CountryGSON.json"
			FileWriter writer = new FileWriter(targetFile);
			writer.write(data);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String sendGet(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuilder response = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();

	}

	// HTTP POST request
	public static String sendPost(String url_string, Map<String,Object> params) throws Exception {
        URL url = new URL(url_string);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	
	public static long calculateProcess(int index, long ctime) {
		long time = System.currentTimeMillis();
		long diff = time - ctime;
		System.out.println("Step " + index + " processing " + (diff));
		return time;
	}

}
