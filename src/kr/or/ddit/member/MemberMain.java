package kr.or.ddit.member;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import kr.or.ddit.member.service.IMemberService;
import kr.or.ddit.member.service.MemberServiceImpl;
import kr.or.ddit.vo.MemberVO;

// JDBC(Java Database Connectivity) => 데이터베이스 연결 자바 API

/*
	회원정보를 관리하는 프로그램을 작성하는데 
	아래의 메뉴를 모두 구현하시오. (CRUD기능 구현하기)
	(DB의 MYMEMBER테이블을 이용하여 작업한다.)
	
	* 자료 삭제는 회원ID를 입력 받아서 삭제한다.
	
	예시메뉴)
	----------------------
		== 작업 선택 ==
		1. 자료 입력			---> insert
		2. 자료 삭제			---> delete
		3. 자료 수정			---> update
		4. 전체 자료 출력			---> select
		5. 작업 끝.
	----------------------
	 
	   
// 회원관리 프로그램 테이블 생성 스크립트 
create table mymember(
    mem_id varchar2(8) not null,  -- 회원ID
    mem_name varchar2(100) not null, -- 이름
    mem_tel varchar2(50) not null, -- 전화번호
    mem_addr varchar2(128),    -- 주소
    reg_dt DATE DEFAULT sysdate, -- 등록일
    CONSTRAINT MYMEMBER_PK PRIMARY KEY (mem_id)
);

*/
public class MemberMain {

	private Scanner scan;
	private IMemberService memservice;
	
	public MemberMain() {
		memservice = new MemberServiceImpl();
		scan = new Scanner(System.in); 
	}
	
	
	public static void main(String[] args) {
		MemberMain memObj = new MemberMain();
		memObj.start();
	}
	
	
	public void displayMenu(){
		System.out.println();
		System.out.println("----------------------");
		System.out.println("  === 작 업 선 택 ===");
		System.out.println("  1. 자료 입력");
		System.out.println("  2. 자료 삭제");
		System.out.println("  3. 자료 수정");
		System.out.println("  4. 전체 자료 출력");
		System.out.println("  5. 작업 끝.");
		System.out.println("----------------------");
		System.out.print("원하는 작업 선택 >> ");
	}
	
	
	public void start(){
		int choice;
		do {
			displayMenu();
			choice = scan.nextInt();
			switch(choice){
				case 1 : 
					insertMember();
					break;
				case 2 : 
					deleteMember();
					break;
				case 3 : 
					updateMember();
					break;
				case 4 : 
					displayAllMember();
					break;
				case 5 : 
					System.out.println("작업을 마칩니다.");
					break;
				default :
					System.out.println("번호를 잘못 입력했습니다. 다시입력하세요");
			}
		} while(choice!=5);
	}

	
	private void displayAllMember() {

		System.out.println();
		System.out.println("------------------------------------------");
		System.out.println(" ID\t생성일\t\t이 름\t전화번호\t주 소");
		System.out.println("------------------------------------------");
		
		List<MemberVO> memList = memservice.getTotalMember();
		
		if (memList.size() == 0) {
			System.out.println("회원정보가 존재하지 않습니다.");
		} else {
			for (MemberVO memberVO : memList) {
				String memId = memberVO.getMemId();
				String memName = memberVO.getMemName();
				String memTel = memberVO.getMemId();
				String memAddr = memberVO.getMemId();
				LocalDate regDt = memberVO.getRegDt();
				
				System.out.println(memId + "\t" + regDt + "\t" + memName + "\t" + memTel +"\t" + memAddr);
			}
		}
		System.out.println("------------------------------------------");
		System.out.println("출력 끝");
	}


	private void deleteMember() {

		System.out.println();
		System.out.println("삭제할 회원정보를 입력해주세요.");

		System.out.println("회원 ID >> ");
		String memId = scan.next();
			
		////////////////////////////////
		
		int cnt = memservice.removeMember(memId);
		
		if (cnt > 0) {
			System.out.println(memId + "인 회원정보 삭제 성공!");
		} else {
			System.out.println(memId + "인 회원정보 삭제 실패!");
		}
	}


	private void updateMember() {

		boolean isExist = false;
		
		String memId = "";
		
		do {
			System.out.println();
			System.out.println("수정할 회원정보를 입력해주세요.");
			
			System.out.println("회원 ID >> ");
			memId = scan.next();
			
			isExist = memservice.checkMember(memId);
			
			if (!isExist) {
				System.out.println("회원 ID가 " + memId + "인 회원은 존재하지 않습니다.");
				System.out.println("다시 입력해주세요.");
			}
			
		} while (!isExist);
		
		System.out.println("회원 이름 >> ");
		String memName = scan.next();
		
		System.out.println("회원 전화번호 >> ");
		String memTel = scan.next();
		
		scan.nextLine();
		
		System.out.println("회원 주소 >> ");
		String memAddr = scan.nextLine();
		
		////////////////////////////////
		
		MemberVO mv = new MemberVO();
		mv.setMemId(memId);
		mv.setMemName(memName);
		mv.setMemTel(memTel);
		mv.setMemAddr(memAddr);
		
		int cnt = memservice.modifyMember(mv);
		
		
		if (cnt > 0) {
			System.out.println(memId + "인 회원정보 수정 성공!");
		} else {
			System.out.println(memId + "인 회원정보 수정 실패!");
		}
	}


	private void insertMember() {
		
		boolean isExist = false;
		
		String memId = "";
		
		do {
			System.out.println();
			System.out.println("추가할 회원정보를 입력해주세요.");
			
			System.out.println("회원 ID >> ");
			memId = scan.next();
			
			isExist = memservice.checkMember(memId);
			
			if (isExist) {
				System.out.println("회원 ID가 " + memId + "인 회원은 이미 존재합니다.");
				System.out.println("다시 입력해주세요.");
			}
			
		} while (isExist);
		
		System.out.println("회원 이름 >> ");
		String memName = scan.next();
		
		System.out.println("회원 전화번호 >> ");
		String memTel = scan.next();
		
		scan.nextLine();
		
		System.out.println("회원 주소 >> ");
		String memAddr = scan.nextLine();
		
		////////////////////////////////
		
		MemberVO mv = new MemberVO();
		mv.setMemId(memId);
		mv.setMemName(memName);
		mv.setMemTel(memTel);
		mv.setMemAddr(memAddr);
		
		int cnt = memservice.registerMember(mv);
		
		if (cnt > 0) {
			System.out.println(memId + "인 회원정보 등록 성공!");
		} else {
			System.out.println(memId + "인 회원정보 등록 실패!");
		}
	}
}