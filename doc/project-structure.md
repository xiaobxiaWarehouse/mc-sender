## Project Structure

````
.
|____.editorconfig
|____.gitignore
|____doc
|____pom.xml
|____README.md

=========对外服务模块=========
|____superman-api
| |____pom.xml
| |____src
| | |____main
| | | |____java
| | | | |____com
| | | | | |____codi
| | | | | | |____yourpackage
| | | | | | | |____api
| | | | | | | | |____domain
| | | | | | | | | |____.gitkeep
| | | | | | | | |____result
| | | | | | | | | |____.gitkeep
| | | | | | | | |____service --------------> 服务接口
| | | | | | | | | |____.gitkeep
| | | | | | | |____package-info.java
| | | |____resources
| | |____test
| | | |____java
| |____superman-api.iml
| |____target
| | |____classes
| | |____generated-sources
| | | |____annotations
| | |____maven-archiver
| | | |____pom.properties
| | |____superman-server.jar

=========业务核心模块=========
|____superman-biz
| |____pom.xml
| |____src
| | |____main
| | | |____java
| | | | |____com
| | | | | |____codi
| | | | | | |____yourpackage
| | | | | | | |____biz
| | | | | | | | |____dao
| | | | | | | | | |____impl
| | | | | | | | | | |____.gitkeep
| | | | | | | | |____service
| | | | | | | | | |____impl   -----------------> 服务实现
| | | | | | | | | | |____.gitkeep
| | | |____resources
| | | | |____generator
| | | | | |____config.properties
| | | | | |____generatorConfig.xml
| | | | |____mybatis
| | | | | |____.gitkeep
| | |____test
| | | |____java
| |____superman-biz.iml
| |____target
| | |____classes
| | | |____generator
| | | | |____config.properties
| | | | |____generatorConfig.xml
| | | |____mybatis
| | | | |____.gitkeep

=========公共工程=========
|____superman-common
| |____pom.xml
| |____src
| | |____main
| | | |____java
| | | | |____com
| | | | | |____codi
| | | | | | |____yourpackage
| | | | | | | |____common
| | | | | | | | |____Const.java
| | | | | | | | |____ErrorConst.java
| | | |____resources
| | |____test
| | | |____java

=========Web工程，程序入口=========
|____superman-web
| |____pom.xml
| |____src
| | |____main
| | | |____java
| | | | |____com
| | | | | |____codi
| | | | | | |____yourpackage
| | | | | | | |____controller
| | | | | | | | |____BaseController.java
| | | | | | | | |____GlobalExceptionHandler.java
| | | | | | | |____servlet
| | | | | | | | |____WebInitServlet.java
| | | |____resources
| | | | |____generator
| | | | | |____config.properties
| | | | | |____generatorConfig.xml
| | | | |____spring
| | | | | |____config.properties
| | | | | |____spring-application.xml
| | | | | |____spring-mvc-security.xml
| | | | | |____spring-mvc.xml
| | | |____webapp
| | | | |____index.jsp
| | | | |____WEB-INF
| | | | | |____web.xml
| | |____test
| | | |____java
| | | | |____com
| | | | | |____codi
| | | | | | |____yourpackage
| | | | | | | |____BaseTest.java
| | | |____resources
| | | | |____log4j.properties

````
