package com.cisco.park.rest.api;


//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.PrintWriter;
//import java.util.Map;
//
//import javax.activation.DataHandler;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.POST;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.ResponseBuilder;
////
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.opencv.core.Core;
//import org.opencv.core.Mat;
//import org.opencv.photo.*;
//import org.opencv.core.CvType;
//import org.opencv.core.Scalar;
//import org.apache.cxf.jaxrs.ext.multipart.Attachment;
////
//import org.apache.commons.io.IOUtils;
//import org.apache.http.entity.mime.MultipartEntity;
//
////import org.jboss.resteasy.plugins.providers.multipart.InputPart;
////import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//import java.util.Map;
// 
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.Produces;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.MultivaluedMap;
//import javax.ws.rs.core.Response;
// 
//
//import org.apache.commons.io.IOUtils;
//import org.jboss.resteasy.plugins.providers.multipart.InputPart;
//import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;



import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import javax.activation.DataHandler;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.photo.*;
import org.opencv.core.CvType;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import com.google.gson.Gson;

import javapns.Push;
import javapns.communication.exceptions.CommunicationException;
import javapns.communication.exceptions.KeystoreException;
import javapns.devices.*;

@Path("/ParkITREST")
public class ParkITREST {

	   private final String UPLOADED_FILE_PATH = "/Users/apetrill/Documents/uploads/";
	   
	   private static String IPAddress = "http://asf-parkit.cisco.com/";
	   //asf-parkit.cisco.com
	   private static boolean[] parkingSpaces = new boolean[8];
	   
	   //private static Queue<> = new  Queue<String>();
	   

	   @GET
	   @Path("/test")
	   @Produces("text/html")
	   public String test(){
		   return ("<html><body><h1>JAX-RS Upload Form</h1><form action=\"http://"+IPAddress+":8080/ParkIT-test/rest/ParkITREST/uploadImage\" method=\"post\" enctype=\"multipart/form-data\"><p>Select a file : <input type=\"file\" name=\"uploadedFile.jpeg\" /></p><input type=\"submit\" value=\"2\" name = \"upload\" id = \"upload\"/></form></body></html>");
	   }

	   
	   

	   

	   //Accepting image sent by raspberry pi to the server
	   //Type of data: image need to collect when image was sent
	   @POST
	   @Path("/uploadImage")
	   @Consumes(MediaType.MULTIPART_FORM_DATA)
	   public Response uploadImage( @Context HttpServletRequest request) throws CommunicationException, KeystoreException{
		    int spaceNumber =0; 
		    String name= "";
		    String imageName = "dummy.jpg";
		    //String filePath = "/Users/apetrill/Documents/uploads/";
		    String filePath = System.getProperty("java.io.tmpdir");
		    System.out.println("temporary file path:"+filePath);
		   if(ServletFileUpload.isMultipartContent(request)){
			   
		   ServletFileUpload upload = new ServletFileUpload();
		   try {
			FileItemIterator iter = upload.getItemIterator(request);
			while (iter.hasNext()){
				FileItemStream item = iter.next();
				name  = item.getFieldName();
				InputStream stream = item.openStream();
			    if (item.isFormField()) {
			    	String spaceString = Streams.asString(stream);
			        System.out.println("Form field " + name + " with space number "
			            + spaceString + " detected.");
			        spaceNumber = Integer.parseInt(spaceString);
			        System.out.println(spaceNumber);				        
			        
			    } else {
			    	imageName = name;
			        System.out.println("File field " + name + " with file name "
			            + item.getName() + " detected.");
			           OutputStream out = new FileOutputStream(new File(filePath+imageName));

		            int read = 0;
		            byte[] bytes = new byte[1024];
		            while ((read = stream.read(bytes)) != -1) {
		               out.write(bytes, 0, read);
		            }
		            stream.close();
		            //out.flush();
		            out.close();

		            }
			    }
			System.out.println(name);
            BufferedImage img_original =ImageIO.read(new File(filePath+imageName));
            System.out.println("got here");
            parkingSpaces[spaceNumber] = DisplayImage.processImage(img_original);
            //if (parkingSpaces[spaceNumber]){
            	//System.out.println("car present");
            	//Push.alert("Hello World!", "/Users/apetrill/Documents/ParkITAPNS/ParkIT/ParkIT/push_certificates/ParkIT_aps_development.p12", "cisco123", false, "6e293126b4dbf2cd8841717c3ebf701e02167240fc26d3fcccf8c231e606584c");
            	//Push.test("/Users/apetrill/Documents/ParkITAPNS/ParkIT/ParkIT/push_certificates/ParkIT_aps_development.p12", "cisco123", false, "6e293126b4dbf2cd8841717c3ebf701e02167240fc26d3fcccf8c231e606584c");
            //}
            //alert("hello","/Users/apetrill/Documents/ParkITAPNS/ParkIT/ParkIT/push certificates/ParkIT_aps_development.p12",)
		} catch (FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   }
		   
	      return Response.ok("file uploaded").build();
	   }

	   private String getFileName(final Part part) {
		    final String partHeader = part.getHeader("content-disposition");
//		    LOGGER.log(Level.INFO, "Part Header = {0}", partHeader);
		    for (String content : part.getHeader("content-disposition").split(";")) {
		        if (content.trim().startsWith("filename")) {
		            return content.substring(
		                    content.indexOf('=') + 1).trim().replace("\"", "");
		        }
		    }
		    return null;
		}
	   
	   
	   //Sending json of possible data going to iphone and database
	   //Type of data so far:
	   //array of 1's and 0's to tell iPhone what parking spaces are available
	   //Times of cars entering/exiting parking spaces
	   
	   @GET
	   @Path("/availability")
	   @Produces(MediaType.APPLICATION_JSON)
	   public String availability(@Context HttpServletRequest request){

		   Gson gson = new Gson();


		   
		   String json = gson.toJson(parkingSpaces);


		   return json;
		}
	   
	   
	   
	   
	   //Receiving data from database - get or post?
	   //Type of data so far:
	   //array of 1's and 0's to tell iPhone what parking spaces are available
	   //@POST
	   //@Path(/database)
	   //@Consumes("application/json")

}
