package com.codeploy.accademy.awslambda.awslambda;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

/**
 * All'interno di questa classe é presente il metodo handleRequest che riceve in
 * input un evento specifico del servizio S3.
 * 
 * Viene letto il file; si effettua una resize; si deploya l'oggetto finale in
 * un bucket di destinazione
 * 
 * @author andrea.ciaccia
 *
 */
public class CodeployLambdaFunctionHandler implements RequestHandler<S3Event, String> {

	private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

	public CodeployLambdaFunctionHandler() {
	}

	// Test purpose only.
	CodeployLambdaFunctionHandler(AmazonS3 s3) {
		this.s3 = s3;
	}

	/**
	 * handle request from event
	 */
	@Override
	public String handleRequest(S3Event event, Context context) {

		context.getLogger().log("Received event: " + event);
		String destBucket = System.getenv("DEST_BUCKET");
		String destFolder = System.getenv("DEST_FOLDER");
		String resizeHeight = System.getenv("RESIZE_HEIGHT");
		String resizeWidth = System.getenv("RESIZE_WIDTH");
		String fileOutPerm = System.getenv("PUBLIC");

		// Get the object from the event and show its content type
		String bucket = event.getRecords().get(0).getS3().getBucket().getName();
		String key = event.getRecords().get(0).getS3().getObject().getKey();
		S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
		String contentType = response.getObjectMetadata().getContentType();
		context.getLogger().log("Bucket origine: "+bucket);
		context.getLogger().log("File origine: "+key);
		context.getLogger().log("ContentType: " + contentType);
		context.getLogger().log("Bucket destinazione: "+destBucket);
		context.getLogger().log("Folder destinazione: "+destFolder);
		context.getLogger().log("Resize Height: "+resizeHeight);
		context.getLogger().log("Resize Width: "+resizeWidth);
		String format = "jpg";
		if (contentType.equals("image/png")) format = "png";

		InputStream reader = new BufferedInputStream(response.getObjectContent());
		BufferedImage image;

		try {

			image = ImageIO.read(reader);
			BufferedImage resized = resize(image, Integer.parseInt(resizeHeight), Integer.parseInt(resizeWidth));
			context.getLogger().log("Resize effettuata...");

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(resized, format, os);
			InputStream is = new ByteArrayInputStream(os.toByteArray());

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentLength(os.toByteArray().length);
			
			Path p = Paths.get(key);
			String file = p.getFileName().toString();
			
			String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			String fileNameOut = stripExtension(file)+"_"+timeStamp+"."+format;

			// il file viene scritto da stream
			if (fileOutPerm !=null && fileOutPerm.equals("true")) s3.putObject(new PutObjectRequest(destBucket, destFolder + fileNameOut, is, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
			else s3.putObject(new PutObjectRequest(destBucket, destFolder + fileNameOut, is, metadata));

			context.getLogger().log("File "+fileNameOut+ " caricato");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return contentType;

	}

	/**
	 * resize image with height,width params
	 * 
	 * @param img
	 * @param height
	 * @param width
	 * @return
	 */
	private BufferedImage resize(BufferedImage img, int height, int width) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}
	
	/**
	 * rimozione estensione da file
	 * @param str
	 * @return
	 */
	private String stripExtension (String str) {
        // Handle null case specially.
        if (str == null) return null;
        // Get position of last '.'.
        int pos = str.lastIndexOf(".");
        // If there wasn't any '.' just return the string as is.
        if (pos == -1) return str;
        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }
}