package recall;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tools.Methods;

public class ExtractFDA {

	public static String googleSearch = "https://www.google.com/images?q=";
	public static String bingImageSearch = "https://www.bing.com/images/search?q=";
	public static String fdaSearch = "https://www.accessdata.fda.gov/scripts/cdrh/cfdocs/cfres/res.cfm?start_search=1&event_id=&productdescriptiontxt=&productcode=&IVDProducts=&rootCauseText=&recallstatus=&centerclassificationtypetext=&recallnumber=&productshortreasontxt=&firmlegalnam=&PMA_510K_Num=&pnumber=&knumber=&PAGENUM=500";
	public static String fdaCallLink = "https://www.accessdata.fda.gov/scripts/cdrh/cfdocs/cfres/res.cfm";

	public static void main(String[] args) throws Exception {
		//ExtractFDA.extractFDA_TO_Database("01/11/2019", "01/20/2019");
	}

	private static void extractFDA_TO_Database(String uid, String startDate, String endDate) throws Exception {
		LinkedList<Recall_Instance> recalls = ExtractFDA.extract_FDA_Recalls_By_DateRange(startDate, endDate);
		ExtractFDA.initCoverImage(recalls);
		for (Recall_Instance recall : recalls) {
			String pid = Methods.getUniqueID();
			Recall.addNewPost(pid, uid, recall.recallID, recall.title, recall.image, recall.reason,new ArrayList<String>(), recall.device_types, recall.level, recall.company, recall.device,
					recall.url, recall.date, Recall.ON);
		}
	}
	
	
	private static void extractFDA_TO_Database_Test(String startDate, String endDate) throws Exception {
		LinkedList<Recall_Instance> recalls = ExtractFDA.extract_FDA_Recalls_By_DateRange(startDate, endDate);
		ExtractFDA.initCoverImage(recalls);
	}

	/*
	 * format of date should be 01/01/2019;
	 */
	public static LinkedList<Recall_Instance> extract_FDA_Recalls_By_DateRange(String startDate, String endDate)
			throws Exception {
		LinkedList<Recall_Instance> recalls = ExtractFDA.get_FDA_Recall_Links(startDate, endDate);
		ExtractFDA.get_FDA_Recall_Instance(recalls);
		// return recalls;
		return ExtractFDA.removeDuplicate(recalls);
	}

	public static void initCoverImage(LinkedList<Recall_Instance> recalls) throws Exception {
		if (recalls == null || recalls.size() == 0) {
			return;
		}
		for (Recall_Instance recall : recalls) {
			recall.image = ExtractFDA.getDeviceBase64Image(recall.device);
		}
	}

	public static LinkedList<Recall_Instance> removeDuplicate(LinkedList<Recall_Instance> recalls) {
		LinkedList<Recall_Instance> result = new LinkedList<>();
		Set<String> ids = new HashSet<>();
		if (recalls == null || recalls.size() == 0) {
			return recalls;
		}
		for (Recall_Instance recall : recalls) {
			if (!ids.contains(recall.recallID)) {
				result.add(recall);
				ids.add(recall.recallID);
			}
		}
		recalls.clear();
		return result;
	}

	public static LinkedList<Recall_Instance> get_FDA_Recall_Links(String startDate, String endDate) throws Exception {
		// format of date postdatefrom=01%2F01%2F2019&postdateto=01%2F31%2F2019

		String[] sds = startDate.split("/");
		String[] eds = endDate.split("/");
		startDate = sds[0] + "%2F" + sds[1] + "%2F" + sds[2];
		endDate = eds[0] + "%2F" + eds[1] + "%2F" + eds[2];
		String dateInfo = "postdatefrom=" + startDate + "&postdateto=" + endDate;
		LinkedList<Recall_Instance> recalls = new LinkedList<>();
		String newLink = ExtractFDA.fdaSearch + "&" + dateInfo;
		Document page = Jsoup.connect(newLink).get();
		Element table = page.select("#res-results-table").first();
		Elements trs = table.child(1).select("tr");
		System.out.println(trs.size());
		int start_index = 0;
		for (Element tr : trs) {
			if (start_index > 1) {
				Elements tds = tr.children().select("td");
				int index = 0;
				Recall_Instance temp = new Recall_Instance();
				for (Element td : tds) {
					if (index == 0) {
						Element a = td.child(index);
						String[] hrefs = a.attr("href").split("\\?");
						temp.url = ExtractFDA.fdaCallLink + "?" + hrefs[1];
					}
					if (index == 1) {
						temp.level = Integer.parseInt(td.text().trim());
					}
					if (index == 2) {
						temp.date = td.text().trim();
					}
					if (index == 3) {
						temp.company = td.text().trim();
					}
					index++;

				}
				recalls.add(temp);
			}
			start_index++;
		}
		return recalls;
	}

	public static void get_FDA_Recall_Instance(LinkedList<Recall_Instance> recalls) throws Exception {
		if (recalls == null || recalls.size() == 0) {
			return;
		}
		for (Recall_Instance recall : recalls) {
			String newLink = recall.url;
			Document page = Jsoup.connect(newLink).get();
			recall.title = page.title();
			Elements tbodys = page.select("tbody");
			Element target = null;
			for (Element tb : tbodys) {
				if (tb.children().size() > 5) {
					target = tb;
					break;
				}
			}
			if (target != null) {
				Elements trs = target.select("tr");
				for (Element tr : trs) {
					if (tr.child(0).text().contains("Reason")) {
						recall.reason = tr.child(1).text();
						System.out.println(recall.reason);
					}

					if (tr.child(0).text().contains("Recall Event ID")) {
						recall.recallID = tr.child(1).text();
					}

					if (tr.child(0).text().contains("Product Classification")) {
						String dtype = tr.child(1).children().select("a").first().text();
						dtype = dtype.toLowerCase();
						System.out.println(dtype);
						recall.device_types = Arrays.asList(dtype.split(","));
					}

					if (tr.child(0).text().trim().equals("Product")) {
						String[] names = tr.child(1).text().split(",");
						recall.device = names[0];
						if(recall.device.length()>20)
						{
							recall.device=recall.device.substring(0, 20);
						}
					}

				}
			}

		}
	}

	public static String getDeviceBase64Image(String device) throws Exception {
		try {
			String newLink = ExtractFDA.bingImageSearch + URLEncoder.encode(device, "UTF-8");
			Document page = Jsoup.connect(newLink).get();
			Element div = page.select("#mmComponent_images_1").first();
			Element image = div.select("img").first();
			String cover_image = image.attr("src");
			URL url = new URL(cover_image);
			InputStream is = url.openStream();
			byte[] bytes = IOUtils.toByteArray(is);
			cover_image = "data:image/jpeg;base64," + Base64.encodeBase64String(bytes);
			return cover_image;
		} catch (Exception e) {
			return "";
		}

	}

	public static void testGoogleSearch() throws IOException, Exception {
		String query = "https://www.googleapis.com/customsearch/v1?key=AIzaSyBiyk-iyJHGvXrT7q8GQm0Pcn2mT3qf7h0&cx=016276095507823492403:dx5up6wjikg&searchType=image&q=";
		String key = "test"; // The query to search
		URL obj = new URL(query + key);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		// optional default is GET
		con.setRequestMethod("GET");
		// add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
		JsonObject jsonObject = new JsonParser().parse(response.toString()).getAsJsonObject();
	}

}
