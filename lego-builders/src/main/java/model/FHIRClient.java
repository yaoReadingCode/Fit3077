package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

/**
 * 
 * @author Sriram, Zhi Tan
 * Class which has a static method for retrieving JSONObject from the FHIRServer
 * ref: https://www.journaldev.com/7148/java-httpurlconnection-example-java-http-request-get-post
 */
public abstract class FHIRClient {
	public static final String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";
	public static final String JSON_FORMAT = "_format=json";
	public static final String SORT_PARAMETER = "_sort=";
	public static final String DESCENDING_DATE = "-date";
	public static final String COUNT_PARAMETER = "_count=";


	/**
	 * A method for getting JSONObject using GET requests to the specified URL
	 * @param urlString the URL to get the JSONObject from
	 * @return the JSONObject of the urlString's page
	 * @throws IOException
	 */
	public static JSONObject getJSONObjectUsingGETRequest(String urlString) throws IOException {
		//URL urlLink = new URL("https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/Patient/$everything?_format=json");
		URL urlLink = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) urlLink.openConnection();
		
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		// If HTTP response is code 200, then proceed to extract data
		if (responseCode == HttpURLConnection.HTTP_OK) { 
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// converts the response string to JSONObject Type
			JSONObject jsonObject = new JSONObject(response.toString());
			// returns the JSONObject
			return jsonObject;

		} 
		//else print Get request failed
		else {
			System.out.println("GET request failed");
			return null;
		}

	}

}


