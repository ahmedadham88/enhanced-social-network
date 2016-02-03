package com.iLabs.spice.handler;

//STEP 1. Import required packages
import java.sql.*;

public class DatabaseConnector {
 // JDBC driver name and database URL
 static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
 static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/zing";//"jdbc:mysql://localhost/EMP";

 //  Database credentials
 static final String USER = "zing";
 static final String PASS = "zingadmin";
 Connection conn = null;
 Statement stmt = null;
 
 public void DELETE (int d) throws ClassNotFoundException, SQLException{
	 Class.forName("com.mysql.jdbc.Driver");

	    //STEP 3: Open a connection
	    System.out.println("Connecting to database...");
	    conn = DriverManager.getConnection(DB_URL,USER,PASS);

	    //STEP 4: Execute a query
	    System.out.println("Creating statement...");
	    stmt = conn.createStatement();
	    
	    String sql;
	    sql = "DELETE FROM session_application WHERE user_id="+d;
	    stmt.executeUpdate(sql);
	    
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
 
 public void Write (String session_id, int id) throws ClassNotFoundException, SQLException{
	 	Class.forName("com.mysql.jdbc.Driver");

	    //STEP 3: Open a connection
	    System.out.println("Connecting to database...");
	    conn = DriverManager.getConnection(DB_URL,USER,PASS);

	    //STEP 4: Execute a query
	    System.out.println("Creating statement...");
	    stmt = conn.createStatement();
	    
	    String sql;
	    sql = "INSERT INTO session_application VALUES ('"+session_id+"',"+id+")";
	    stmt.executeUpdate(sql);
	    
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
 
 public String Read (int id) throws ClassNotFoundException, SQLException{
	 Class.forName("com.mysql.jdbc.Driver");
String result ="";
	    //STEP 3: Open a connection
	    System.out.println("Connecting to database...");
	    conn = DriverManager.getConnection(DB_URL,USER,PASS);

	    //STEP 4: Execute a query
	    System.out.println("Creating statement...");
	    stmt = conn.createStatement();
	    
	    String sql;
	    sql = "SELECT session_id FROM session_application WHERE user_id="+id;
	    ResultSet rs = stmt.executeQuery(sql);
	    while(rs.next()){
	       //Retrieve by column name
	      result  = rs.getString("session_id");
	    }
	    //STEP 6: Clean-up environment
	    rs.close();
	    
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
		    return result;
 }
 
 public static void main(String[] args) {
 Connection conn = null;
 Statement stmt = null;
 try{
    //STEP 2: Register JDBC driver
    Class.forName("com.mysql.jdbc.Driver");

    //STEP 3: Open a connection
    System.out.println("Connecting to database...");
    conn = DriverManager.getConnection(DB_URL,USER,PASS);

    //STEP 4: Execute a query
    System.out.println("Creating statement...");
    stmt = conn.createStatement();
    String sql;
    sql = "SELECT session_id, user_id FROM session_application";
    ResultSet rs = stmt.executeQuery(sql);

    //STEP 5: Extract data from result set
    while(rs.next()){
    	
       //Retrieve by column name
       int id  = rs.getInt("user_id");
      
       String first = rs.getString("session_id");
       

       //Display values
       System.out.print("ID: " + id);
       
       System.out.print(", First: " + first);
       
    }
    //STEP 6: Clean-up environment
    rs.close();
    stmt.close();
    conn.close();
 }catch(SQLException se){
    //Handle errors for JDBC
    se.printStackTrace();
 }catch(Exception e){
    //Handle errors for Class.forName
    e.printStackTrace();
 }finally{
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
 }//end try
 System.out.println("Goodbye!");
}//end main
}//end FirstExample
