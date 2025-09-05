package encryption.sec05;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import encryption.sec01.DBConnect;

public class MemberLoginKeySalt {
	
	public static String Byte_to_String(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	// 해시값 생성
	public static String Hashing(byte[] password, String salt) throws Exception {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		// key-stretching
		// password + salt 결합해서 digest 생성
		for (int i = 0; i < 10000; i++) {
			String temp = Byte_to_String(password) + salt;
			md.update(temp.getBytes());
			password = md.digest();
		}
		return Byte_to_String(password);
	}

	public static void main(String[] args) throws Exception{
		// sha256으로 비밀번호 해시, 키 스트레칭 + salt 추가
		DBConnect dbCon = new DBConnect();
		Connection con = dbCon.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null; 
		
		Scanner sc = new Scanner(System.in);
		
		String dbPwd = null;
		String dbSalt = null;
		
		try {
			System.out.print("ID 입력 : ");
			String memID = sc.nextLine();
			System.out.print("비밀번호 입력 : ");
			String memPass = sc.nextLine();
			
			// 웹사이트에서 회원가입 진행 시 id는 유일하게 구성됨(id는 유일하다고 가정)
			// 해당 id가 있는지 확인 -> 정보 추출
			String sql = "select * from member where memID = '" + memID + "'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery(); // 입력된 id의 회원정보 저장
			
			if (rs.next()) {
				dbPwd = rs.getString(2); // 회원가입 시 해시되어져 저장된 비밀번호
				dbSalt = rs.getString(6); // 개인별로 저장한 salt값
				String hashPass = Hashing(memPass.getBytes(), dbSalt);
				System.out.println("dbPwd : " + dbPwd);
				System.out.println("hashPass : " + hashPass);
				
				if (dbPwd.equals(hashPass)) {
					System.out.println("로그인 성공");
				} else {
					System.out.println("로그인 실패");
				}
				
			} else {
				System.out.println("존재하지 않는 아이디입니다.");
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
			
		} finally {
			DBConnect.close(con, pstmt, rs);
			sc.close();
		}
	}

}
