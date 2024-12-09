에너 테이션 사용으로 스프링 컨테이너에 주입 시킨다
+ component- scan 
= 의존성 주입 

(기존 방식:  servlet- context 에서 컴포넌트 스캔을 경로 적용 시킨다.)  
그러나 Sample의 경우 root-context 에서 별개로 적용시킨다.

원래는 둘 중 하나를 헷갈리지 않게 사용하고, 컴포넌트를 아래로 추가한다.




이미지





프로젝트 진행 순서 

1) 요구 사항 분석 - 유스케이스 (uml)
2) DB 설계 - ERD
3) 화면 출력 -> 스토리 보드 (블럭다이아 그램)
- 피그마, 포토샵 , 파워포인트
4) Sequence Diagram





이미지




SampleTest 실행을 위해 @ContextConfiguration()에 root 파일 경로를 src부터 설정에서 가져와서 복사해서 경로 지정한다.
어노테이션이 자동으로 먹지 않으면 


이미지
폼에서 프레임워크 테스트를 추가(+ 수정)한다.


테스트를 통해서 의존 관계가 실행 가능한지 알게 됨..!
이미지


레스토랑을 호텔에서 필요해서 가져오는 경우 
SampleHotel 을 적용 시켜서 (복사,수정)


package com.company.sample;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")  
@Log4j

public class HotelTest {
	
	@Autowired
	private SampleHotel hotel;
	
	
	
	@Test
	public void testExist() {
		
		assertNotNull(hotel);
		
		log.info(hotel);
		log.info("------------------");
		log.info(hotel.getChef());
	}
}



이미지

객체 하나만 만들어 논 후 ~  다른곳에서 의존하고 써먹음  그럼으로  SampleHotel이 주가 되고 제어가 역전됨
new 대신
  제어의 역전  IOC / DI 의존성 주입

IOC 제어의 역전 (Inversion of Control)

DI(Dependency Injection) : 의존성 주입

의존성 주입은 제어 역전의 방법 중의 하나로, 

사용할 객체를 직접 생성하지 않고 외부 컨테이너가 생성한 객체를 주입받아 사용하는 방식을 말한다.






+ DataBase 연동하기


이미지
이미지



data 소스 연결하는 것 Hikari 

	<!-- Root Context: defines shared resources visible to all other web components -->
	<bean id="hikariConfig" class="com.zaxxer.hikari.HikariConfig">
		<property name = "driverClassName"  value="oracle.jdbc.OracleDriver"/>
		<property name = "jdbcUrl"  value="jdbc:oracle:thin:@localhost:1521:XE"/>
		<property name = "username"  value="kmr"/>
		<property name = "password"  value="kmr"/>
	</bean>
	

<!-- HikariCP configuration -->
	<bean id="dataSoarce" class="com.zaxxer.hikari.HikariDataSource" destroy-method= "close">
		<constructor-arg ref="hikariConfig" />
	
	</bean>
	
	
	<context:component-scan base-package="com.company.sample"></context:component-scan>
</beans>


빈 추가는 스프링 추가 하는 것 


이미지





데이터 소스 테스트 폴더에
@Test
testMyBatis()추가 -junitTest 실행해서 
root -context 안에 ㅡMybatis 가 잘 설치 되었나 확인한다.
** 실제 에러가 나서 확인해보니 dataSource 를 오타남 두 mybatis 관련 라이브러리 이름이 일치 해야 함

처음에 Green신호 받았어도 Connection test con- 이 연결된 데이터 소스 히카리 bean name= "dataSource"  으로 지정했어야 하는데 
여기서 오타가 나면  MyBatis 설치 후 바티스가 히카리 신호를 받을 수 없음으로 
바티스에서 dataSource를 호출했는데  property name 을 찾을 수 없다고 떴음...!! 

그래서  루트에 빈을 추가할 때  Hikari cp의   Bean name ="" 과  Mybatis property name="" 오타없이 일치바람..!!!





이미지





package com.company.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {
	@Select("SELECT sysdate FROM dual")
	public 	String getTime();

}



 Mapper class 컨트롤러 밑 폴더에 추가



이미지
이미지



인터페이스 2 만들 때 
리소스안에 mapper 폴더- TimeMapper.xml 만들어서 적용

쿼리관련 내용은 resource 안에 ...!!
test 관련 리소스여도 Mybatis -scan 경로에 맞게 main resource 에 만들어 준 것임..!



이미지



이미지



testGetTime2(){
}
실행확인


Log4jdbc- log4jdbc2설정

우선 pom.xml 에 라이브러리 적용 한다 
기존 log4j 밑에 
 log4 j2 - 1.16 



이미지
이미지


log4jdbc.spylogdelegator.name=net.sf.log4jdbc.log.slf4j.Slf4jSpyLogDelegator  복사해서 사용

해당 파일 만들 때 복사 적용이 링크로 되면 내용만 복사해서 
파일 만든다. text파일로 만들면 안되고 일반 File 만들어 내용 적용함. 그 후 알아서 파일 형식 변경됨..!


파일 만든 후 root-context 에 가서 <bean id "hikariConfig" 안에 내용을 변경한다. 

property value = "" 오라클 경로 앞에 :log4jdbc 
		<property name = "driverClassName"  value="net.sf.log4jdbc.sql.jdbcapi.DriverSpy"/>
		<property name = "jdbcUrl"  value="jdbc:log4jdbc:oracle:thin:@localhost:1521:XE"/>


아래 수정할 부분 밑줄..!





이미지






testClass 실행 시 위와 같이 sysdate 가 나와야함..


아래는 추가로 test 안에 리소스- log4j.xml  설정을 다시 만든 것임..!






이미지




<!-- 세가지 조건 변경하면 Junit 테스트할 때 필요한 info 만 결과로 도출됨 / jdbc 관련 결과는 안나옴 -->

	<logger name ="jdbc.audit">
	<level value ="warn" />
	</logger>
	
	<logger name ="jdbc.resultset">
	<level value ="warn" />
	</logger>
	
	<logger name ="jdbc.connection">
	<level value ="warn" />
	</logger>
	
