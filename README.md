JobService
-----------

## Architecture
- codi-base
- codi-superman-base
- Servlet 3.0
- Tomcat 8.5.39
- Redis
- MySQL
- Spring
- SpringMVC
- Quartz

## Project Structure
- [Project Structure](doc/project-structure.md)

## Secondary Development

- 将`config`工程也导入Gitlab工程组
- 修改默认包名（`yourpackage`）,所有涉及到该名称的都必须要修改
- mybatis生成器配置修改，`jobservice-biz/src/main/resources/generator/config.properties`
- spring配置修改`jobservice-web/main/resources/spring/`,不需要的bean可以注释掉
- log4j文件名配置修改`jobservice-web/main/webapp/WEB-INF/web.xml`


## Run

- 执行`doc/tables_mysql_innodb.sql`
- 配置`CODI_HOME`变量


