package com.cisco.park.rest.api;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

  
public class ParkITClient
{
    
	private static String IPAddress = "10.155.69.194";
	
	public static void main(String args[]) throws Exception
    {
        ParkITClient fileUpload = new ParkITClient () ;
        File file = new File("/Users/apetrill/Desktop/photo1.jpg");
        fileUpload.executeImageRequest("http://"+IPAddress+":8080/ParkIT-test/rest/ParkITREST/uploadImage",
                file, file.getName()) ;
        String json = fileUpload.executeAvailabilityRequest("http://" +IPAddress+":8080/ParkIT-test/rest/ParkITREST/availability");
        
        Gson gson = new Gson();
        
        boolean[] parkingSpaces = gson.fromJson(json, boolean[].class);
        
        System.out.print(parkingSpaces[1]);
    } 
     
    public void executeImageRequest(String urlString, File file, String fileName){
        HttpClient client = new DefaultHttpClient() ;
        HttpPost postRequest = new HttpPost (urlString) ;
        try
        {
            //Set various attributes
            MultipartEntity multiPartEntity = new MultipartEntity () ;
            //multiPartEntity.addPart("fileDescription", new StringBody(fileDescription != null ? fileDescription : ""));
            //multiPartEntity.addPart("fileName", new StringBody(fileName != null ? fileName : file.getName())) ;
  
            FileBody fileBody = new FileBody(file, "application/octet-stream");
            //Prepare payload
            multiPartEntity.addPart(fileName, fileBody) ;
            multiPartEntity.addPart("SpotID",new StringBody("2"));
  
            //Set to request body
            postRequest.setEntity(multiPartEntity) ;
            //postRequest.setEntity(file) ;
             
            //Send request
            HttpResponse response = client.execute(postRequest) ;
             
            //Verify response if any
            if (response != null)
            {
                System.out.println(response.getStatusLine().getStatusCode());
                System.out.println(response.getStatusLine().getReasonPhrase());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace() ;
        }
        client.getConnectionManager().shutdown();
    }
    
    public String executeAvailabilityRequest(String url){
        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource service = client.resource(url);
        return service.accept(MediaType.APPLICATION_JSON).get(String.class);
    }

}