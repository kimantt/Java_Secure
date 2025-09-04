<%@page import="java.sql.Timestamp" %>
<%@page import="java.sql.DriverManager" %>
<%@page import="java.sql.Connection" %>
<%@page import="java.sql.ResultSet" %>
<%@page import="java.sql.PreparedStatement" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<% try {
	Class.forName("oracle.jdbc.driver.OracleDriver");
	String db_address = "jdbc:oracle:thin:@localhost:1521:xe";
	String db_username = "C##SQL_USER";
	String db_pwd = "1234";
	Connection connection = DriverManager.getConnection(db_address, db_username, db_pwd);
	
	String insertQuery = "select MAX(num) from pratice_board";
	PreparedStatement pstmt = connection.prepareStatement(insertQuery);
	ResultSet result = pstmt.executeQuery();
	
	int num = 0;
	while (result.next()) {
		num = result.getInt("MAX(num)")+1; // 지금 입력되는 게시글의 게시글번호로 사용
	}
	
	Timestamp today_date = new Timestamp(System.currentTimeMillis());
	request.setCharacterEncoding("UTF-8");
	String writer = request.getParameter("writer");
	String title = request.getParameter("title");
	String content = request.getParameter("content");
	
	insertQuery = "insert into pratice_board(num, title, writer, content, regdate) values(?,?,?,?,?)";
	pstmt = connection.prepareStatement(insertQuery);
	pstmt.setInt(1, num);
	pstmt.setString(2, title);
	pstmt.setString(3, writer);
	pstmt.setString(4, content);
	pstmt.setTimestamp(5, today_date);
	
	pstmt.executeUpdate();
	
	response.sendRedirect("post_list.jsp"); // post_new.jsp -> 저장버튼 클릭하면 요청 -> post_new_send.jsp -> DB에 데이터 저장 완료되면 요청 -> post_list.jsp
	
} catch (Exception ex) { out.println("오류가 발생했습니다. 오류 메시지 : " + ex.getMessage());
}
%>