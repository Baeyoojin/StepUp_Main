package proj.stepUp.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import org.json.JSONObject;

public class PaymentUtil {
	
	public String getAccessToken() {
		String accessToken = "";
		String requrl = "https://api.iamport.kr/users/getToken";
		String impKey = "5141870845668140";
		String impSecret = "sJXXqv3VntXxgB3NXs91HYlLiLBy2hpDu9w4kyv4TsKz08IyK4okLsVW3hZTDpY66vCl4Nh5QusDWEyZ";
		
		try {
			 URL url = new URL(requrl);
			 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			 conn.setRequestMethod("POST");
			 conn.setDoOutput(true);
			 
			 BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			 StringBuilder sb = new StringBuilder();
			 sb.append("imp_key="+impKey);
			 sb.append("&imp_secret="+impSecret);
	         bw.write(sb.toString());
	         bw.flush();
	         
	         int responseCode = conn.getResponseCode();
	         System.out.println("responseCode : " + responseCode);
	         
	         BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	         String line = "";
	         String result = "";
	         
	         while ((line = br.readLine()) != null) {
	             result += line;
	         }
	         
	         //json 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
	         System.out.println("결과"+result);
	         System.out.println(line);
	         JSONObject jsonObject = new JSONObject(result);
	         JSONObject responseObj = jsonObject.getJSONObject("response");
	         accessToken = responseObj.getString("access_token");

	          
	          System.out.println("access_token : " + accessToken);
	          
	          br.close();
	          bw.close();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return accessToken;
	}
	
	public String createNum() {
	    Random random = new Random();
	    int createNum = 0;
	    String ranNum = "";
	    int letter = 4;
	    String resultNum = "";
	    
	    for (int i=0; i<letter; i++) { 
    		
			createNum = random.nextInt(9);		//0부터 9까지 올 수 있는 1자리 난수 생성
			ranNum =  Integer.toString(createNum);  //1자리 난수를 String으로 형변환
			resultNum += ranNum;			//생성된 난수(문자열)을 원하는 수(letter)만큼 더하며 나열
		}
	    LocalDateTime now = LocalDateTime.now();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");
	    String formattedDateTime = now.format(formatter);
	    
	    String orderNum = resultNum+"-"+formattedDateTime;//생성된 주문 번호
	    
	    return orderNum;
	}
}
