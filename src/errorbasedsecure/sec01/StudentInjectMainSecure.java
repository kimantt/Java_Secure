package errorbasedsecure.sec01;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class StudentInjectMainSecure {

	public static void main(String[] args) {
		// 취약점이 있는 코드 / 잘못 개발된 코드
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Scanner sc = new Scanner(System.in);
		
		try {
			System.out.print("책 번호 입력 : ");
			String bookNo = sc.nextLine();
			System.out.println("bookNo : " + bookNo);
			
			// sql 쿼리문 작성 - 플레이스홀더 사용하지 않고 변수 이용해서 코딩
			String sql = "select * from book where bookNo = '" + bookNo + "'";
			System.out.println("sql : " + sql);
			pstmt = con.prepareStatement(sql); // 하드코딩된 쿼리 구문 그대로 적용 가능
			rs = pstmt.executeQuery();
			
			// 제목 출력
			System.out.println("----------------책 정보 조회----------------");
			System.out.println("책 번호 \t 제목 \t\t\t\t 가격");
			
			// 필요 내용만 출력
			while (rs.next()) {
				bookNo = rs.getString(1);
				String bookName = rs.getString(2);
				int bookPrice = rs.getInt(4);
				
				// 한 행씩 출력
				System.out.format("%-10s\t %-20s\t %6d \n", bookNo, bookName, bookPrice);
			}
			
		} catch(SQLException e) {
			//e.printStackTrace(); // 개발단계에서 개발자가 확인하기 위한 에러 출력
			System.out.println("프로그램 실행 중 오류 발생. 프로그램 종료.");
			
		} finally {
			DBConnect.close(con, pstmt, rs);
			sc.close();
		}
	}

}
