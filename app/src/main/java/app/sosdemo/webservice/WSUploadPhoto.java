package app.sosdemo.webservice;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by indianic on 17/12/15.
 */
public class WSUploadPhoto {
    private URL connectURL;
    private String responseString;
    private String awCode;
    private String dateTime;
    private byte[] dataToServer;
    private FileInputStream fileInputStream = null;

    public WSUploadPhoto(String urlString, String awCode, String dateTime) {
        try {
            connectURL = new URL(urlString);
            this.awCode = awCode;
            this.dateTime = dateTime;
        } catch (Exception ex) {
            Log.i("HttpFileUpload", "URL Malformatted");
        }
    }

    public String Send_Now(FileInputStream fStream, final String fileName) {
        fileInputStream = fStream;
        return Sending(fileName);
    }

    String Sending(final String fileName) {

        String iFileName = fileName;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag = "fSnd";
        try {
            Log.e(Tag, "Starting Http File Sending to URL");
            // Open a HTTP connection to the URL
            HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            final DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"HelpLogID\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(awCode);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"FileDateTime\"" + lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(dateTime);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + iFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag, "Headers are written");

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            fileInputStream.close();

            dos.flush();

            Log.e(Tag, "File Sent, Response: " + String.valueOf(conn.getResponseCode()));

            InputStream is = conn.getInputStream();

            // retrieve the response from server
            int ch;

            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            String s = b.toString();
            String response = s.substring(0, 16);
            Log.i("Response", response);
            dos.close();
            return response;
        } catch (MalformedURLException ex) {
            Log.e(Tag, "URL error: " + ex.getMessage(), ex);
            return "";
        } catch (IOException ioe) {
            Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
            return "";
        }
    }


}
