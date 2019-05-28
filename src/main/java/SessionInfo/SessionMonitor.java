package SessionInfo;

import java.util.HashMap;

public class SessionMonitor {

	public static HashMap<String, String> sessions = new HashMap<String, String>();
	
	public static String ipcode="X-FORWOARDED-FOR";
	public static int session_duration=1800;
	public static void addUserToSession(String uid, String token)
	{
		sessions.put(uid, token);
	}
	
	public static void removeUserFromSession(String uid)
	{
		sessions.remove(uid);
	}
	
	public static boolean checkCurrentTokenTime(String uid, String token)
	{
		if(uid==null)
		{
			return false;
		}
		String value=sessions.get(uid);
		
		if(value==null)
		{
			return false;
		}
		else
		{
			if(value.equals(token))
			{
				if((System.currentTimeMillis()-Long.parseLong(token))/1000>SessionMonitor.session_duration)
				{
					return false;
				}
				return true;
			}
			return false;
		}
	}
	
	public static boolean isUserInSession(String uid, String token)
	{
		//System.out.println("time :"+(System.currentTimeMillis()-Long.parseLong(token))/1000);
		if(uid==null)
		{
			return false;
		}
		String value=sessions.get(uid);
		if(value==null)
		{
			return false;
		}
		else
		{
			if(value.equals(token))
			{
				return true;
			}
			return false;
		}
	}
	
	public static boolean isUserInSession(String uid)
	{
		if(uid==null)
		{
			return false;
		}
		String value=sessions.get(uid);
		
		if(value==null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
}
