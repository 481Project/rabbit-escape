package rabbitescape.ui.swing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * @brief retrieval and parsing. no swing.
 */
public class GitHubClient
{
    public final String baseURL = "https://api.github.com/repos/andybalaam/rabbit-escape/issues";
    public final String acceptHeader = "application/vnd.github.v3+json";
    private ArrayList<GitHubIssue> issues = null;
    private String errMsg = "";
    private int page=1; /**< .../issues?page=number */
    private boolean gotAllPages = false; /**< @brief do not query for more pages of issues */
    
    
    public GitHubClient()
    {
    }
    
    public void initialise()
    {
        String jsonIssues = apiCall( "" );
        issues = parseIssues( jsonIssues);
    }
    
    public void fetchComments(GitHubIssue ghi)
    {
        String jsonComments = apiCall( "/" + ghi.getNumber() + "/comments" );
        Pattern bodyPattern = Pattern.compile( "\"body\":\"([^\"\\\\]*(?:\\\\.[^\"\\\\]*)*)\"" ); 
        Matcher bodyMatcher = bodyPattern.matcher( jsonComments );
        while ( bodyMatcher.find() )
        {
            ghi.addToBody( bodyMatcher.group( 1 ) );
        }
    }
    
    public String getError()
    {
       return errMsg; 
    }
    
    public GitHubIssue getIssue( int index )
    {
        if( null==issues || index<0 )
        {
            return null;
        }
        if ( index >= issues.size() )
        {
            if ( gotAllPages )
            {
                return null;
            }/// @TODO do in different thread, give some progress meter
            String jsonIssues = apiCall( "?page=" + (++page) );
            ArrayList<GitHubIssue> extra = parseIssues(jsonIssues);
            issues.addAll( extra );
            if ( 0 == extra.size() ){
                gotAllPages = true;
                return null; // Github has no more to give
            }
        }
        return issues.get( index );
    }
    
    public int getIndexOfNumber( int issueNumber)
    {
        for( int i=0; i<issues.size(); i++ )
        {
            GitHubIssue ghi = issues.get( i );
            if( ghi.getNumber() == issueNumber )
            {
                return i;
            }
        }
        return -1;
    }


    private static ArrayList<GitHubIssue> parseIssues(String json)
    {   /// @TODO this is extremely crufty: hacking out most of the URL to split on.
        // This leaves the issue number as the first thing in the string.
        Pattern issuePattern = Pattern.compile( "\\{\"url\":\"https://api\\.github\\.com/repos/andybalaam/rabbit-escape/issues/" );
        String[] jsonIssuesStrings = issuePattern.split( json );
        ArrayList<GitHubIssue> ret= new ArrayList<GitHubIssue>();
        for( int i = 0; i < jsonIssuesStrings.length; i++ )
        {
            String jsonIssue = jsonIssuesStrings[i];
            if ( !"0123456789".contains( jsonIssue.substring( 0, 1 ) ) )
            {
                continue;
            }
            
            GitHubIssue ghi = new GitHubIssue( 
                GitHubJsonTools.getIntValue( jsonIssue, "number" ),
                GitHubJsonTools.getStringValue( jsonIssue, "title" ),
                GitHubJsonTools.getStringValue( jsonIssue, "body" ),
                GitHubJsonTools.getStringValuesFromArrayOfObjects( jsonIssue, "labels.name" )
            );
            ret.add( ghi );
        }
        return ret;
    }
    
    private String apiCall( String endURL )
    {
        /*  // uncomment to test progress dot tic
        try
        {
            Thread.sleep(5000);
        }
        catch ( InterruptedException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        String out="";

        try
        {
            url = new URL( baseURL+endURL );
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Accept",acceptHeader);
            is = connection.getInputStream();  // Throws an IOException
            br = new BufferedReader( new InputStreamReader( is ) );

            /* github (?always) returns the response on one line,
             * so this while loop is not slow. */
            while ( ( line = br.readLine() ) != null )
            {
                out = out + line + "\n";
            }
            return out;
        }
        catch ( MalformedURLException eMU ) 
        {
             eMU.printStackTrace();
        }
        catch (UnknownHostException eUH) 
        {
            errMsg = "Can't reach github.com.";
        }
        catch ( IOException eIO )
        {
             eIO.printStackTrace();
        }
        finally 
        {
            try 
            {
                if (is != null) is.close();
            } 
            catch ( IOException eIO )
            {
                // just an attempt at tidying. can ignore
            }
        }
        return null;
    }
}
