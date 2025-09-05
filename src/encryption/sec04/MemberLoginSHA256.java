package encryption.sec04;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MemberLoginSHA256 {
	
	// sha256 static method는 회원가입할 때 사용했던 메서드와 동일해야함
	public static String sha256(String pass) throws NoSuchAlgorithmException {
		// sha256 해시알고리즘으로 password 변경 후 반환
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(pass.getBytes()); // byte input값(해시로 변경할 message)을 파라미터로 전달해야함
		
		return bytesToHex(md.digest()); // 16진수 문자열로 변환 시키는 함수 호출
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
	
	public static void main(String[] args) throws Exception {
		
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		
		Scanner sc = new Scanner(System.in);
		// 회원가입 시 MD5 알고리즘을 사용해서 비밀번호 저장
		// 로그인 진행할 때는 평문 비밀번호 받아서 저장된 해시값과 같은지 확인
		// 해시값은 역연산이 불가능하므로 로그인로직에서는 입려된 비밀번호를 해시값으로 변경해서 저장된 비밀번호 해시값과 일치하는지 확인
		try {
			System.out.print("ID 입력 : ");
			String memID = sc.nextLine();
			String memPass = sc.nextLine();
			for (int i = 0; i < 10000; i++) {
				memPass = sha256(memPass); // 스트레칭 진행, 크래킹 시간을 지연하는데 목적이 있음
			} // 회원가입할때와 동일한 알고리즘 사용, 해시값으로 변경 후 그 값으로 쿼리 구성
			
			// select 쿼리문 작성
			String sql = "select * from member where memID = '" + memID + "' and memPWD = '" + memPass + "'";
			pstmt = con.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			// 회원가입 진행 시 중복아이디를 허용하지 않았다고 가정
			// 위 쿼리가 진행되었다면 한명에 대한 정보가 나오거나 정보가 추출되지 않을 것임
			if (rs.next()) {
				System.out.println("로그인 성공");
				// 웹에서는 로그인되었다면 자격증명을 포함해서 클라이언트에게 전달
			} else {
				System.out.println("로그인 실패");
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			
		} finally {
			DBConnect.close(con, pstmt, rs);
			sc.close();
		}
	}
	
}
