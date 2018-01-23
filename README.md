
# Acticiti使用方法学习
tips:此项目需配置相应的数据库，具体见下文。
具体的Activiti知识学习笔记见印象笔记，链接如下[Activiti知识学习](https://app.yinxiang.com/shard/s48/nl/13235430/f1fb802b-5ace-4340-85d3-f1763100561f)
#Spring-Boot
Spring Boot是一个应用框架，可以轻松的创建，独立的运行。提倡约定大于配置，如在Maven项目中增加项目依赖spring-boot-starters-basic，
```xml
<dependency>
  <groupId>org.activiti</groupId>
  <artifactId>activiti‐spring‐boot‐starter‐basic</artifactId>
  <version>${activiti.version}</version>
</dependency>
```
自动配置包括：
自动创建Activiti ProcessEngine的Bean
自动注册Activiti Service为Spring的Bean
创建一个Spring Job Executor
自动扫描部署路径src/main/resources/processes下的流程处理文件
#结合Spring Boot的Activiti设计
##实验逻辑
实现一个流程，使得某人可以申请加入某公司，请求可被有权限的人通过或拒绝。
#实验环境
Java version:1.8.0_144
Maven version:3.5.0
MySQL version:5.5.36(port:3306 username:root password:null)
##实验工具
IDE：eclipse Mars.2 Release(4.5.2)（含Activiti-Designer插件）
数据库测试工具：Navicat Premium
测试工具：postman
##设计流程图
bpmn
![image.png](.attachments/image-2b3a9720-7ba6-4f34-9554-fe4d94104ae3.png)
xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<definitions xmlns="http://www.omg.org/spec/BPMN/20100524/MODEL" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:activiti="http://activiti.org/bpmn" xmlns:bpmndi="http://www.omg.org/spec/BPMN/20100524/DI" xmlns:omgdc="http://www.omg.org/spec/DD/20100524/DC" xmlns:omgdi="http://www.omg.org/spec/DD/20100524/DI" typeLanguage="http://www.w3.org/2001/XMLSchema" expressionLanguage="http://www.w3.org/1999/XPath" targetNamespace="http://www.activiti.org/test">
  <process id="joinProcess" name="Join process" isExecutable="true">
    <startEvent id="startevent1" name="Start">
      <extensionElements>
        <activiti:formProperty id="personId" name="person id" type="long" required="true"></activiti:formProperty>
        <activiti:formProperty id="compId" name="company Id" type="long" required="true"></activiti:formProperty>
      </extensionElements>
    </startEvent>
    <endEvent id="endevent1" name="End"></endEvent>
    <userTask id="ApprovalTask" name="批准任务" activiti:candidateUsers="${joinService.findUsers(execution)}" isForCompensation="true">
      <extensionElements>
        <activiti:formProperty id="joinApproved" name="Join Approved" type="enum">
          <activiti:value id="true" name="Approve"></activiti:value>
          <activiti:value id="false" name="Reject"></activiti:value>
        </activiti:formProperty>
      </extensionElements>
    </userTask>
    <sequenceFlow id="flow1" sourceRef="startevent1" targetRef="ApprovalTask"></sequenceFlow>
    <serviceTask id="AutoTask" name="自动配置任务" activiti:expression="${joinService.joinGroup(execution)}"></serviceTask>
    <sequenceFlow id="flow2" sourceRef="ApprovalTask" targetRef="AutoTask"></sequenceFlow>
    <sequenceFlow id="flow3" sourceRef="AutoTask" targetRef="endevent1"></sequenceFlow>
  </process>
  <bpmndi:BPMNDiagram id="BPMNDiagram_joinProcess">
    <bpmndi:BPMNPlane bpmnElement="joinProcess" id="BPMNPlane_joinProcess">
      <bpmndi:BPMNShape bpmnElement="startevent1" id="BPMNShape_startevent1">
        <omgdc:Bounds height="35.0" width="35.0" x="51.0" y="123.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="endevent1" id="BPMNShape_endevent1">
        <omgdc:Bounds height="35.0" width="35.0" x="470.0" y="123.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="ApprovalTask" id="BPMNShape_ApprovalTask">
        <omgdc:Bounds height="60.0" width="100.0" x="150.0" y="110.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNShape bpmnElement="AutoTask" id="BPMNShape_AutoTask">
        <omgdc:Bounds height="60.0" width="100.0" x="300.0" y="110.0"></omgdc:Bounds>
      </bpmndi:BPMNShape>
      <bpmndi:BPMNEdge bpmnElement="flow1" id="BPMNEdge_flow1">
        <omgdi:waypoint x="86.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="112.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="112.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="150.0" y="140.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow2" id="BPMNEdge_flow2">
        <omgdi:waypoint x="250.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="262.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="262.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="300.0" y="140.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
      <bpmndi:BPMNEdge bpmnElement="flow3" id="BPMNEdge_flow3">
        <omgdi:waypoint x="400.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="412.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="412.0" y="140.0"></omgdi:waypoint>
        <omgdi:waypoint x="470.0" y="140.0"></omgdi:waypoint>
      </bpmndi:BPMNEdge>
    </bpmndi:BPMNPlane>
  </bpmndi:BPMNDiagram>
</definitions>
```
#开发过程
##建立项目
1. 新建Spring Starter Project项目
1. 创建项目pom.xml文件
依赖为
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>
	<groupId>com.fengye.example</groupId>
	<artifactId>spring-boot-activiti</artifactId>
	<version>1.0-SNAPSHOT</version>
	<packaging>jar</packaging>
	<name>spring-boot-activiti</name>
	<description>Demo project for Spring Boot</description>
	<parent>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>1.3.0.RELEASE</version>
	</parent>
	<properties>
		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
		<java.version>1.8</java.version>
		<activiti.version>5.21.0</activiti.version>
	</properties>
	<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-rest</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
		<dependency>
			<groupId>org.activiti</groupId>
			<artifactId>activiti-spring-boot-starter-basic</artifactId>
			<version>${activiti.version}</version>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
</project>
```
3. 在路径src/main/resources/processes下，新建Activiti Diagram，命名为join.bpmn
如流程图设计所示
4. 配置文件
在application.properties中设置数据库配置
```java
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database=MYSQL
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/spring-boot-activiti?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
```
##设置数据库
创建数据库，名为spring-boot-activiti，编码方式为UTF-8，如图
![image.png](.attachments/image-2d1adf18-6f37-4ccb-aec9-f9dd1fdc82e5.png)
##核心代码
###实体类（model:Person\Comp）
Person.java
```java
@Entity
public class Person {	
	@Id
	@GeneratedValue
	private Long personId;	
	private String personName;
	@ManyToOne(targetEntity = Comp.class)
	private Comp comp;	
	public Person() {		
	}
	public Person(String personName) {
		this.personName = personName;
	}
	public Long getPersonId() {
		return personId;
	}
	public void setPersonId(Long personId) {
		this.personId = personId;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public Comp getComp() {
		return comp;
	}
	public void setComp(Comp comp) {
		this.comp = comp;
	}
}
```
Comp.java
```java
@Entity
public class Comp {
	@Id
	@GeneratedValue
	private Long compId;
	private String compName;
	@OneToMany(mappedBy = "comp", targetEntity = Person.class)
	private List<Person> people;
	
	public Comp(String compName)
	{this.compName = compName;}
	
	public Comp() {
		
	}
	
	public Long getCompId() {
		return compId;
	}
	
	public void setCompId(Long compId) {
		this.compId = compId;
	}
	
	public String getCompName() {
		return compName;
	}
	
	public void setCompName(String compName) {
		this.compName = compName;
	}
	
	public List<Person> getPeople() {
		return people;
	}
	
	public void setPeople(List<Person> people) {
		this.people = people;
	}
}
```
###DAO类
PersonRepository.java
```java
public interface PersonRepository extends JpaRepository<Person, Long> {
	
	public Person findByPersonName(String personName);
	
}
```
CompRepository.java
```java
public interface CompRepository extends JpaRepository<Comp, Long> {
	
}
```
###Service类
ActivitiService.java
```java
@Service
@Transactional
public class ActivitiService {
	//注入为我们自动配置好的服务
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private TaskService taskService;
	
	//开始流程，传入申请者的id以及公司的id
	public void startProcess(Long personId, Long compId) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("personId", personId);
		variables.put("compId", compId);
		runtimeService.startProcessInstanceByKey("joinProcess", variables);
	}
	
	//获得某个人的任务列表
	public List<Task> getTasks(String assignee) {
		return taskService.createTaskQuery().taskCandidateUser(assignee).list();
	}
	
	//完成任务
	public void completeTasks(Boolean joinApproved, String taskId) {
		Map<String, Object> taskVariables = new HashMap<String, Object>();
		taskVariables.put("joinApproved", joinApproved);
		taskService.complete(taskId, taskVariables);
	}
}
```
JoinService.java
```java
@Service
public class JoinService {
	@Autowired
	PersonRepository personRepository;
	@Autowired
	private CompRepository compRepository;
	
	//加入公司操作，可从DelegateExecution获取流程中的变量
	public void joinGroup(DelegateExecution execution) {
		Boolean bool = (Boolean) execution.getVariable("joinApproved");
		if (bool) {
			Long personId = (Long) execution.getVariable("personId");
			Long compId = (Long) execution.getVariable("compId");
			Comp comp = compRepository.findOne(compId);
			Person person = personRepository.findOne(personId);
			person.setComp(comp);
			personRepository.save(person);
			System.out.println("加入组织成功");
		} else {
			System.out.println("加入组织失败");
		}
	}
	
	//获取符合条件的审批人，这里写死，使用应用使用实际代码
	public List<String> findUsers(DelegateExecution execution) {
		return Arrays.asList("admin", "wtr");
	}
}
```
###WebController类
MyRestController.java
```java
@RestController
public class MyRestController {
	@Autowired
	private ActivitiService myService;
	
	//开启流程实例
	@RequestMapping(value = "/process/{personId}/{compId}", method = RequestMethod.GET)
	public void startProcessInstance(@PathVariable Long personId, @PathVariable Long compId) {
		myService.startProcess(personId, compId);
	}
	
	//获取当前人的任务
	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public List<TaskRepresentation> getTasks(@RequestParam String assignee) {
		List<Task> tasks = myService.getTasks(assignee);
		List<TaskRepresentation> dtos = new ArrayList<TaskRepresentation>();
		for (Task task : tasks) {
			dtos.add(new TaskRepresentation(task.getId(), task.getName()));
		}
		return dtos;
	}
	
	//完成任务
	@RequestMapping(value = "/complete/{joinApproved}/{taskId}", method = RequestMethod.GET)
	public String complete(@PathVariable Boolean joinApproved, @PathVariable String taskId) {
		myService.completeTasks(joinApproved, taskId);
		return "ok";
	}
	
	//Task的dto
	static class TaskRepresentation
	
	{
		private String id;
		private String name;
		
		public TaskRepresentation(String id, String name) {
			this.id = id;
			this.name = name;
		}
		
		public String getId() {
			return id;
		}
		
		public void setId(String id) {
			this.id = id;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}
	}
}
```
###启动类
```java
@SpringBootApplication
@ComponentScan("com.fengye.example")
@EnableJpaRepositories("com.fengye.example.dao")
@EntityScan("com.fengye.example.model")
public class ActivitiApplication {
	@Autowired
	private CompRepository compRepository;
	@Autowired
	private PersonRepository personRepository;
	
	
	public static void main(String[] args) {
		SpringApplication.run(ActivitiApplication.class, args);
	}
	
	//初始化模拟数据
	@Bean
	public CommandLineRunner init(final ActivitiService myService) {
		return new CommandLineRunner() {
			public void run(String... strings) throws Exception {
				if (personRepository.findAll().size() == 0) {
					personRepository.save(new Person("wtr"));
					personRepository.save(new Person("wyf"));
					personRepository.save(new Person("admin"));
				}
				if (compRepository.findAll().size() == 0) {
					Comp group = new Comp("great company");
					compRepository.save(group);
					Person admin = personRepository.findByPersonName("admin");
					Person wtr = personRepository.findByPersonName("wtr");
					admin.setComp(group); wtr.setComp(group);
					personRepository.save(admin); personRepository.save(wtr);
				}
			}
		};
	}
}
```
##文件夹结构
```txt
│  pom.xml    
└─src
  └─main
      ├─java
      │  │  ActivitiApplication.java
      │  │  
      │  └─com
      │      └─fengye
      │          └─example
      │              ├─controller
      │              │      MyRestController.java
      │              │      
      │              ├─dao
      │              │      CompRepository.java
      │              │      PersonRepository.java
      │              │      
      │              ├─model
      │              │      Comp.java
      │              │      Person.java
      │              │      
      │              └─service
      │                      ActivitiService.java
      │                      JoinService.java
      │                      
      └─resources
          │  application.properties
          │  
          └─processes
                  join.bpmn
                  join.png           
```
#项目测试
##自动创建数据库
启动项目后自动创建数据库
![image.png](.attachments/image-4af0592d-3aa2-4087-b953-4558ea929f6b.png)
![image.png](.attachments/image-61f60ab5-eaad-4aab-9d4f-de39c162b93b.png)
![image.png](.attachments/image-efd07912-7815-4cfb-b11e-30fda1c04934.png)
![image.png](.attachments/image-5c766895-85da-480f-b015-687d9f695e24.png)
##权限人添加公司
id为2的人员申请加入id为1的公司，在postman中进行测试，
http://localhost:8080/process/2/1
数据库效果
![image.png](.attachments/image-eb864456-b4bc-45b2-88de-fce12ed00484.png)
![image.png](.attachments/image-d1b9967c-ab38-48e5-aecb-f31089fb8d87.png)
##审批
http://localhost:8080/tasks?assignee=admin
用户admin和wtr具有审批权
![image.png](.attachments/image-234253a1-b9ba-4b48-8824-2b8488f49774.png)
##自动配置
http://localhost:8080/complete/true/10完成任务，true是同意，10是task的id此时wyf的记录里将comp_ompId更新为当前公司id
![image.png](.attachments/image-884efd95-78e5-4819-9e77-3a96ec56f79d.png)
![image.png](.attachments/image-7794ff3a-7ff4-43e6-a2bc-c9a1b58b6cad.png)
#代码解释
此处对代码流程做简要解释
- [ ] 首先是为Spring Boot中的Activiti添加JPA支持，增加的依赖是：
```xml
<dependency>
  <groupId>org.activiti</groupId>
  <artifactId>activiti‐spring‐boot‐starter‐jpa</artifactId>
  <version>${activiti.version}</version>
</dependency>
```
这就加入了Spring的配置，以及JPA用的Bean。默认使用Hibernate作为JPA提供者。
- [ ] 创建实体类Person.java
默认情况下，如果没有使用内存数据库，不会自动创建数据库表。因此在capplication.properties中加入如下配置：
```xml
spring.jpa.hibernate.ddl‐auto=update
```
- [ ] 创建Spring repository
提供直接可用的增删改查，添加一个通过personName查找Person的方法。Spring会基于约定自动实现它（也就是使用names属性）。

# 参考文献
Activiti实战
Activiti V5.21 用户手册 

##补充：流程的执行过程
要加入的公司id为1，申请加入的人的id为2:访问

http://localhost:8080/process/2/1

查看数据库表(ACT_RU_TASK ACT_RU_IDENTITYLINK)的变化

http://localhost:8080/tasks?assignee=admin 查看admin用户的任务

访问http://localhost:8080/complete/true/10 完成任务，true为同意(可以选择false)







