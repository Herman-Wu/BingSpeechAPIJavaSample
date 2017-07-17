import java.util.Arrays;
import java.util.List;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClients;
import java.io.File;
import java.io.FileInputStream;

public class Main{
    public static void main(String[] args){
        System.out.println("Hello Java Call Bing Speech API");

        try{
        	String token=gettoken();
            System.out.println(token);
     	             	
         	File file = new File("test.wav");
        	FileInputStream fileInputStream = new FileInputStream(file);
        	      	      	
        	HttpURLConnection connection = null;
        

        	URL url2 = new URL("https://speech.platform.bing.com/speech/recognition/conversation/cognitiveservices/v1?language=en-US");
        	connection = (HttpURLConnection) url2.openConnection();
        	   	
        	connection.setRequestProperty("Content-Type","audio/wav; codec=\"audio/pcm\"; samplerate=16000");
        	connection.setRequestProperty("Host","speech.platform.bing.com");
        	connection.setRequestProperty("Authorization", "Bearer "+token);
        	connection.setRequestProperty("Transfer-Encoding", "chunked");
        	connection.setRequestProperty("Accept","application/json;text/xml");
        	
        	connection.setChunkedStreamingMode((int)file.length());
        	
        	// Allow Inputs & Outputs
        	connection.setDoInput(true);
        	connection.setDoOutput(true);
        	connection.setUseCaches(false);
        	
        	// Enable POST method
        	connection.setRequestMethod("POST");

        	DataOutputStream outputStream = null;
        	
        	outputStream = new DataOutputStream( connection.getOutputStream() );

        
        	byte []buffer = new byte[1024];
        	int read = 0;
        	while ( (read = fileInputStream.read(buffer)) != -1 ) {
        	    outputStream.write(buffer, 0, read);       	   
        	}

        	fileInputStream.close();
        	outputStream.flush();
        	outputStream.close();
        	
        	System.out.println("The result is 00 "+ connection.getResponseCode());
        	System.out.println("The result is 00 "+ connection.getResponseMessage());
        	
        	InputStream stream = connection.getInputStream();
        	InputStreamReader isReader = new InputStreamReader(stream ); 

        	//put output stream into a string
        	BufferedReader br = new BufferedReader(isReader );
        	final StringBuilder sb  = new StringBuilder();

        	  String s = null;

              while ((s = br.readLine()) != null) {
            	  sb.append(s);         	  
              }
        	
              System.out.println(" Bing Speech API Recoganized Result:  "+sb);
                
           
        } catch (Exception e) {
        	System.out.println(e.getMessage());
        }

 
        
    }
    
    public static String gettoken() 
    { 
		String line = new String();
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("https://api.cognitive.microsoft.com/sts/v1.0/issueToken");
		try {
			post.setHeader("Ocp-Apim-Subscription-Key", "XXXXXXXXXXXXXXXXXXXX");
			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			line = rd.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}
}