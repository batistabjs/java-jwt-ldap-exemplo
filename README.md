# java-jwt-ldap-exemplo
### Desenvolvimento:
Tecnologias: 
- Java 21
- Framework: Spring
- Gerenciador de Dependências: maven 3.9.14
- Thymeleaf Engine

### Para execução do projeto:
Instalar o Java 25 e Maven. 

Altere as variáveis do Active Directory correto em /resources/application.properties

No terminal, vá até a raiz do projeto e execute:
```
mvn install
```

```
mvn spring-boot:run
```

No navegador de internet, acesse:
```
http://localhost:8090
```

Para testar a geração do token use o curl no terminal:
```
curl -X POST http://localhost:8090/authenticate -H "Content-Type: application/json" -d '{"username":"seu_user_ldap","password":"sua_senha_ldap"}'
```
