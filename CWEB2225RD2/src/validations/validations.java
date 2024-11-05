package validations;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class validations {
	
	public static boolean validate_name(String name) 
	{
		if (name.isEmpty()) 
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public static boolean validate_email(String email) 
	{
		String emailRegex = "([A-Za-z0-9]+[.-_])*[A-Za-z0-9]+@[A-Za-z0-9-]+(\\.[A-Z|a-z]{2,})+";
		Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(email);
	    boolean matchFound = matcher.find();
	    
	    if(matchFound) {	
	    	return true;
	    } 
	    
	    else 
	    {
	    	return false;
	    }
	}
	
	public static boolean validate_phone(String phone)
	{
		if (phone.isEmpty())
		{
			return false;
		}
		
		try 
		{
			int valid = Integer.parseInt(phone);
		}
		catch (NumberFormatException ex)
		{
			return false;
		}
		
		if ((phone.length() > 9) && (phone.length() < 15))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean validate_address(String address)
	{
		if (address.isEmpty()) 
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
