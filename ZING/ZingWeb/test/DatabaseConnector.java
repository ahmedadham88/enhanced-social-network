

//STEP 1. Import required packages
import java.sql.*;

import java.util.concurrent.TimeUnit;

public class DatabaseConnector {
 // JDBC driver name and database URL
 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
 static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/zing";//"jdbc:mysql://localhost/EMP";

 //  Database credentials
 static final String USER = "zing";
 static final String PASS = "zingadmin";
 Connection conn = null;
 Statement stmt = null;
 
 public void databaseConnect(){
	try{
		//STEP 2: Register JDBC driver
	    Class.forName("com.mysql.jdbc.Driver");

	    //STEP 3: Open a connection
	    System.out.println("Connecting to database...");
	    conn = DriverManager.getConnection(DB_URL,USER,PASS);

	    //STEP 4: Execute a query
	    System.out.println("Creating statement...");
	    stmt = conn.createStatement();
	}catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
 }
 
 public void databaseDisconnect(){
	 
		    //finally block used to close resources
		    try{
		       if(stmt!=null)
		          stmt.close();
		    }catch(SQLException se2){
		    }// nothing we can do
		    try{
		       if(conn!=null)
		          conn.close();
		    }catch(SQLException se){
		       se.printStackTrace();
		    }//end finally try
		 
 }
 
public String [] getSessionIDs(int count, int priority){
	String [] result = new String [count];
	try{
	    
	    String sql;
	    sql = "select * from sessions where priority="+priority+" limit "+count;
	    ResultSet rs = stmt.executeQuery(sql);
	    int i = 0;
	    while(rs.next()){
	       //Retrieve by column name
	       result[i]  = rs.getString("session_id");
	       i++;
	    }
	    //STEP 6: Clean-up environment
	    rs.close();
	    
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	
	return result;
}
 
 public void removeSession(String sessionID){
try{
	    
	    String sql;
	    sql = "DELETE FROM sessions WHERE session_id='"+sessionID+"'";
	    stmt.executeUpdate(sql);
	    
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
}
 
 public int getPriority(String sessionID){
	 int result=0;
	 try{
		 String sql;
		    sql = "SELECT priority FROM sessions WHERE session_id='"+sessionID+"'";
		    ResultSet rs = stmt.executeQuery(sql);
		    while(rs.next()){
		       //Retrieve by column name
		       result  = rs.getInt("priority");
		    }
		    //STEP 6: Clean-up environment
		    rs.close();
	    
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	// System.out.println("Goodbye!");
	 return result;
 }
 
 public void insertTime (long time){
	 try{
		    
		    String sql;
		    sql = "INSERT INTO time VALUES ("+time+")";
		    stmt.executeUpdate(sql);
		    
		 }catch(SQLException se){
		    //Handle errors for JDBC
		    se.printStackTrace();
		 }catch(Exception e){
		    //Handle errors for Class.forName
		    e.printStackTrace();
		 }
 }
 
 public void setSessionID(String sessionID, int priority){
	 
	 try{
	    
	    String sql;
	    sql = "INSERT INTO sessions VALUES ('"+sessionID+"',"+priority+")";
	    stmt.executeUpdate(sql);
	    
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	// System.out.println("Goodbye!");
 }
 
public void updateStatistics(int a, int b, int c, int priority){
	 
	 try{
	    String sql;
	    sql = "update statistics  set sessions_created="+a+",sessions_terminated="+b+",sessions_rejected="+c+" where index_value="+priority;
	    stmt.executeUpdate(sql);
	    
	    
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	// System.out.println("Goodbye!");
 }
 
 public void updateSessionNumber(int a, int b, int c, int d){
	 
	 try{
	    String sql;
	    sql = "update session_number  set number_session="+a+",session_gold="+b+",session_silver="+c+",session_bronze="+d+" where index_value=1";
	    stmt.executeUpdate(sql);
	    
	    
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	// System.out.println("Goodbye!");
 }
 public int [] getSessionNumber() {
 int [] result = new int [4];
 try{
    String sql;
    sql = "SELECT number_session, session_gold, session_silver, session_bronze FROM session_number WHERE index_value=1";
    ResultSet rs = stmt.executeQuery(sql);
    while(rs.next()){
       //Retrieve by column name
       result[0]  = rs.getInt("number_session");
   //    System.out.println("1: "+ result[0]);
       result[1] = rs.getInt("session_gold");
    //   System.out.println("2: "+ result[1]);
       result [2] = rs.getInt("session_silver");
    //   System.out.println("3: "+ result[2]);
       result [3] = rs.getInt("session_bronze");
    //   System.out.println("4: "+ result[3]);
    }
    //STEP 6: Clean-up environment
    rs.close();
    
 }catch(SQLException se){
    //Handle errors for JDBC
    se.printStackTrace();
 }catch(Exception e){
    //Handle errors for Class.forName
    e.printStackTrace();
 }
// System.out.println("Goodbye!");
return result;
}//end main
 
 public int [] getStatistics(int priority) {
	 int [] result = new int [3];
	 try{
	    String sql;
	    sql = "SELECT sessions_created, sessions_terminated, sessions_rejected FROM statistics WHERE index_value="+priority;
	    ResultSet rs = stmt.executeQuery(sql);
	    while(rs.next()){
	       //Retrieve by column name
	       result[0]  = rs.getInt("sessions_created");
	       result[1] = rs.getInt("sessions_terminated");
	       result [2] = rs.getInt("sessions_rejected");
	    }
	    //STEP 6: Clean-up environment
	    rs.close();
	    
	 }catch(SQLException se){
	    //Handle errors for JDBC
	    se.printStackTrace();
	 }catch(Exception e){
	    //Handle errors for Class.forName
	    e.printStackTrace();
	 }
	// System.out.println("Goodbye!");
	return result;
	}//end main
 
 public static void main(String[]args) throws InterruptedException{
	 DatabaseConnector c = new DatabaseConnector();
	 c.databaseConnect();
	 //loop every 30 seconds
	 int time=0;
	 while(time<700){
		/* int[]result = c.getStatistics(1);
		 System.out.println("GOLD: "+result[0]+" "+result[1]+" "+result[2]);
		 int[]resulto = c.getStatistics(2);
		 System.out.println("SILVER: "+resulto[0]+" "+resulto[1]+" "+resulto[2]);
		 int[]resulti = c.getStatistics(3);
		 System.out.println("BRONZE: "+resulti[0]+" "+resulti[1]+" "+resulti[2]);*/
		 int [] result = c.getSessionNumber();
		 System.out.println("All: "+result[0]+" Gold "+result[1]+" Silver "+result[2]+ " Bronze "+result[3]);
		 TimeUnit.SECONDS.sleep(5);
		 time = time+5;
	 }
	 c.databaseDisconnect();
	// System.out.println("SUCCESS" + result[0]+result[1]+result[2]);
 }
}//end FirstExample
