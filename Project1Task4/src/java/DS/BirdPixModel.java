/**
* Author: Enzhe Lu
* Last Modified: Feb 05 2016.
*
* This program is for screen capturating bird picture with author information
* and provide to the user.
* The interface is including two different jsp webpages to demonstrate the 
* result.
* User could select from dropdown list for either type of bird name and a random
* bird picture will generate for them
*/
package DS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Enzhe Lu
 */
public class BirdPixModel {
    
    private String pictureTag; // The search string of the desired picture
    private String pictureURL; // The URL of the picture image
    private String authorURL;  // The URL of the Author
    private String authorName; // The Name of Author
    
    /**
     * Do
     * @param searchTag 
     */
    public void doPictureSearch(String searchTag) {
        //assign pictureTag
        pictureTag = searchTag;
        //initialize response string
        String response = "";
        //processing Searchtag to be use in fetch
        searchTag = searchTag.replace(" ", "+");
        System.out.println(searchTag);
                
        // Create a URL for the desired page
        String flickrURL = "http://nationalzoo.si.edu/scbi/migratorybirds/featured_photo/bird.cfm?pix=" + searchTag;
        response = fetch(flickrURL);


        //System.out.println(response);

        /*
         * Find the picture URL to scrape
         *
         * Screen scraping is an art that requires looking at the HTML
         * that is returned creatively and figuring out how to cut out
         * the data that is important to you.
         * 
         * First find the src target that starts with farm
         */
        
        //find URL first
        int cutLeft = response.indexOf("src=\"https://ids.si.edu/ids/deliveryService");
        //initial usful varialbes
        int endOfPix = 0;
        String Substr = response;
        ArrayList<String> pixlist = new ArrayList<String>(); //lsit of picture URL
        ArrayList<String> autlist = new ArrayList<String>(); //list of author URL
        ArrayList<String> autNameList = new ArrayList<String>();  // List of author Name
        
        // If not found, then no such photo is available.
        if (cutLeft == -1) {
            pictureURL = pictureTag = null;
            return;
        }
        
        
        while(cutLeft > 0){
            //first get the picture
            
            // I don't want the src=" part, so skip 5 characters
            cutLeft = cutLeft + 5;
            // Cut the rest of string out
            Substr = Substr.substring(cutLeft);
            // Look for the close quote
            endOfPix = Substr.indexOf("\"");
            // Add to the Arraylist
            pixlist.add(Substr.substring(0, endOfPix));
            // get the rest of html file
            Substr = Substr.substring(endOfPix);
            
            //started to get author URL
            //search for begining of author hyperlink
            cutLeft = Substr.indexOf("href=");
            // I don't want the href=\" part, so skip 7 characters
            cutLeft = cutLeft + 7;
            Substr = Substr.substring(cutLeft);
            // Look for the close quote
            endOfPix = Substr.indexOf(" title");
            // Add to the Arraylist
            autlist.add("http://nationalzoo.si.edu/" + Substr.substring(0, endOfPix-1));
            // get the rest of html file
            Substr = Substr.substring(endOfPix);
            
            //started to get author Name
            //search for begining of author hyperlink
            cutLeft = Substr.indexOf(">");
            // I don't want the >" part, so skip 1 characters
            cutLeft = cutLeft + 1;
            Substr = Substr.substring(cutLeft);
            // Look for the close quote
            endOfPix = Substr.indexOf("<");
            // Add to the Arraylist
            autNameList.add(Substr.substring(0, endOfPix));
            // get the rest of html file
            Substr = Substr.substring(endOfPix);
            
            System.out.println(Substr.substring(0, endOfPix));
            
            // get cutleft again ( for loop usage)
            cutLeft = Substr.indexOf("src=\"https://ids.si.edu/ids/deliveryService");
        }
        
        // Random generator to generator randome selection
        Random rand = new Random();
        int randomNum = rand.nextInt(pixlist.size());
        

        // Now assign the value to global variable.
        pictureURL = pixlist.get(randomNum);
        authorURL = autlist.get(randomNum);
        authorName = autNameList.get(randomNum);
        System.out.println("pictureURL= " + pictureURL);
        System.out.println("authorURL= " + authorURL);
    }
    
    /**
     * This function is used to change the size and output the final URL
     * @param picsize is demeonstrate weather there is mobile or not
     * @return return picture URL
     */
    public String interestingPictureSize(String picsize) {
        if(picsize.equals("mobile")){
            pictureURL = pictureURL.replace("max_h=200", "max_w=250");
        }else{
            pictureURL = pictureURL.replace("max_h=200", "max_w=500");
        }
        return pictureURL;
    }
    
    /*
     * Return the picture tag.  I.e. the search string.
     * 
     * @return The tag that was used to search for the current picture.
     */
    public String getPictureTag() {
        return (pictureTag);
    }
    
        
    
    /*
     * Return the author hyper link.  I.e. the search string.
     * 
     * @return The url of who was author of current picture.
     */
    public String getAuthorURL() {
        return (authorURL);
    }
    
    /*
     * Return the author name.  I.e. the search string.
     * 
     * @return The name who was author of current picture.
     */
    public String getAuthorName() {
        return(authorName);
    }
    
    /*
     * Make an HTTP request to a given URL
     * 
     * @param urlString The URL of the request
     * @return A string of the response from the HTTP GET.  This is identical
     * to what would be returned from using curl on the command line.
     */
    
    private String fetch(String urlString) {
        String response = "";
        try {
            URL url = new URL(urlString);
            System.out.println(url);
            /*
             * Create an HttpURLConnection.  This is useful for setting headers
             * and for getting the path of the resource that is returned (which 
             * may be different than the URL above if redirected).
             * HttpsURLConnection (with an "s") can be used if required by the site.
             */
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String str;
            // Read each line of "in" until done, adding each to "response"
            while ((str = in.readLine()) != null) {
                // str is one line of text readLine() strips newline characters
                response += str;
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Eeek, an exception");
            // Do something reasonable.  This is left for students to do.
        }
        return response;
    }
    
}
