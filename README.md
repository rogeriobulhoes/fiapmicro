# Service Architecture / API’s / Mobile Architecture

- Possibilidade de visualizar os filmes de um determinado gênero;
- Possibilidade de visualizar os detalhes de cada filme;
- Possibilidade de votar nos filmes que mais gostei;
- Possibilidade de marcar um filme ou série para ser visto no futuro;
- Possibilidade de buscar um filme por palavra-chave;
- Possibilidade de exibir os filmes mais vistos por categorias;
- Possibilidade de abrir um chamado técnico de algum problema que está acontecendo;
- Possibilidade de visualizar os filmes e séries que já foram assistido;

# Pré-requisitos
 - Java 8 - Install - https://www.java.com
 - Maven - Install - https://maven.apache.org/install.html
 - Docker - Install - https://docs.docker.com/engine/install/
 - Docker Compose - Install - https://docs.docker.com/compose/

# Considerações sobre a implementação
 Além do Service Discovery, Mensageria e Resiliência, também foram implementados 3 microsserviços:
  - Clientes: escrita e leitura na base de Clientes
  - Filmes  : escrita e leitura na base de Filmes
  - Serviços: outras operações (likes, favoritos, marcações para o futuro, etc)

O sistema utiliza um gateway Zuul como ponto único de entrada das requisições.

A URL base dos serviços está em http://localhost

Não houve a necessidade de implementação de um ConfigServer pelo fato da montagem
ser feita utilizando tecnologia de contêineres e, portanto, as configurações podem
ser enviadas na subida do sistema através de variáveis de ambiente. 

>> Inserir o Arquitetura da App <<


# Montagem do sistema e execução
```bash
$ git clone https://github.com/rogeriobulhoes/fiapmicro.git
$ cd fiapmicro
$ mvn clean install
$ cd docker
$ docker-compose build
$ docker-compose up -d
```
Foi utilizado o Swagger como gerador de documentação do sistema.
Ele pode ser acessado através da URL

http://localhost/swagger-ui.html

É possível selecionar determinada documentação de um serviço específico selecionando-o 
na parte superior direita da tela ("Select a spec")

# Api Documentation dos microsserviços de clientes
http://localhost/swagger-ui.html?urls.primaryName=clientes-api-docs#/clientes_service


## Version: 1.0

#### GET /v1/clientes
busca todos os clientes

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| offset | query |  | No | long |
| pageNumber | query |  | No | integer |
| pageSize | query |  | No | integer |
| paged | query |  | No | boolean |
| sort.sorted | query |  | No | boolean |
| sort.unsorted | query |  | No | boolean |
| unpaged | query |  | No | boolean |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Page«Cliente»](#page«cliente») |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST /v1/clientes
adiciona um cliente

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| cliente | body | cliente | Yes | string |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Cliente](#cliente) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### GET /v1/clientes/{id}
busca um cliente

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | long |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Cliente](#cliente) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |


#### POST /v1/clientes/{id}/chamados
abre um chamado para um cliente

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| chamado | body | chamado | Yes | string |
| id | path | id | Yes | long |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Chamado](#chamado) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### Models


#### Chamado

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| cliente | long |  | No |
| dataAbertura | dateTime |  | No |
| dataFechamento | dateTime |  | No |
| descricao | string |  | No |
| id | string (uuid) |  | No |
| motivo | string |  | No |

#### Cliente

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| cpf | string |  | No |
| id | long |  | No |
| idade | integer |  | No |
| nome | string |  | No |

#### Pageable

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| offset | long |  | No |
| pageNumber | integer |  | No |
| pageSize | integer |  | No |
| paged | boolean |  | No |
| sort | [Sort](#sort) |  | No |
| unpaged | boolean |  | No |

#### Page«Cliente»

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| content | [ [Cliente](#cliente) ] |  | No |
| empty | boolean |  | No |
| first | boolean |  | No |
| last | boolean |  | No |
| number | integer |  | No |
| numberOfElements | integer |  | No |
| pageable | [Pageable](#pageable) |  | No |
| size | integer |  | No |
| sort | [Sort](#sort) |  | No |
| totalElements | long |  | No |
| totalPages | integer |  | No |

#### Sort

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| empty | boolean |  | No |
| sorted | boolean |  | No |
| unsorted | boolean |  | No |%


# Api Documentation para o microsserviço de Filmes
http://localhost/swagger-ui.html?urls.primaryName=filmes-api-docs

## Version: 1.0

#### GET /v1/filmes
busca todos os filmes. Também utiliza os parâmetros genero e título.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| genero | query | genero | No | string |
| offset | query |  | No | long |
| pageNumber | query |  | No | integer |
| pageSize | query |  | No | integer |
| paged | query |  | No | boolean |
| sort.sorted | query |  | No | boolean |
| sort.unsorted | query |  | No | boolean |
| titulo | query | titulo | No | string |
| unpaged | query |  | No | boolean |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Page«Filme»](#page«filme») |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### POST /v1/filmes
adiciona um filme

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| cliente | body | cliente | Yes | string |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Filme](#filme) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |


#### GET /v1/filmes-mais-vistos
filmes que já foram assistidos

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| offset | query |  | No | long |
| pageNumber | query |  | No | integer |
| pageSize | query |  | No | integer |
| paged | query |  | No | boolean |
| sort.sorted | query |  | No | boolean |
| sort.unsorted | query |  | No | boolean |
| unpaged | query |  | No | boolean |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Page«Filme»](#page«filme») |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |


#### GET /v1/filmes-sucessos
sucessos por categoria

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| genero | query | genero | Yes | string |
| offset | query |  | No | long |
| pageNumber | query |  | No | integer |
| pageSize | query |  | No | integer |
| paged | query |  | No | boolean |
| sort.sorted | query |  | No | boolean |
| sort.unsorted | query |  | No | boolean |
| unpaged | query |  | No | boolean |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Page«Filme»](#page«filme») |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |


#### GET /v1/filmes/{id}
busca um filme

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | long |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Filme](#filme) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### Models


#### Filme

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| anoLancamento | integer |  | No |
| assistido | integer |  | No |
| detalhe | string |  | No |
| genero | string |  | No |
| id | long |  | No |
| likes | integer |  | No |
| lingua | string |  | No |
| tipo | string |  | No |
| titulo | string |  | No |

#### Pageable

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| offset | long |  | No |
| pageNumber | integer |  | No |
| pageSize | integer |  | No |
| paged | boolean |  | No |
| sort | [Sort](#sort) |  | No |
| unpaged | boolean |  | No |

#### Page«Filme»

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| content | [ [Filme](#filme) ] |  | No |
| empty | boolean |  | No |
| first | boolean |  | No |
| last | boolean |  | No |
| number | integer |  | No |
| numberOfElements | integer |  | No |
| pageable | [Pageable](#pageable) |  | No |
| size | integer |  | No |
| sort | [Sort](#sort) |  | No |
| totalElements | long |  | No |
| totalPages | integer |  | No |

#### Sort

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| empty | boolean |  | No |
| sorted | boolean |  | No |
| unsorted | boolean |  | No |%



# Api Documentation para o microsserviços genéricos
http://localhost/swagger-ui.html?urls.primaryName=servicos-api-docs

## Version: 1.0

#### POST /v1/assistidos
marcar um filme já assistido

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| body | body | body | Yes | [Assistido](#assistido) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | object |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |


#### POST /v1/chamados
abrir um chamado técnico

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| chamado | body | chamado | Yes | string |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Chamado](#chamado) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### GET /v1/chamados/{id}
busca um chamado

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | string |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Chamado](#chamado) |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

#### PUT /v1/chamados/{id}
fechar um chamado técnico

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path | id | Yes | string |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | [Chamado](#chamado) |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |


#### POST /v1/favoritos
marcar um filme como favorito

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| body | body | body | Yes | [Favorito](#favorito) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | object |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |


#### POST /v1/likes
marcar um filme com like

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| body | body | body | Yes | [Like](#like) |

##### Responses

| Code | Description | Schema |
| ---- | ----------- | ------ |
| 200 | OK | object |
| 201 | Created |  |
| 401 | Unauthorized |  |
| 403 | Forbidden |  |
| 404 | Not Found |  |

### Models


#### Assistido

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| clienteId | long |  | No |
| filmeId | long |  | No |
| id | long |  | No |

#### Chamado

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| cliente | long |  | No |
| dataAbertura | dateTime |  | No |
| dataFechamento | dateTime |  | No |
| descricao | string |  | No |
| id | string (uuid) |  | No |
| motivo | string |  | No |

#### Favorito

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| clienteId | long |  | No |
| filmeId | long |  | No |
| id | long |  | No |

#### Like

| Name | Type | Description | Required |
| ---- | ---- | ----------- | -------- |
| clienteId | long |  | No |
| filmeId | long |  | No |
| id | long |  | No |%


## docker-compose.yml
```docker
version: '3'

services:
  # --------------
  # database
  # --------------
  mysql:
    image: mysql:5.7
    command: --default-authentication-plugin=mysql_native_password
    ports:
      - "3306:3306"
    volumes:
      - $PWD/data:/var/lib/mysql
    environment:
        MYSQL_ROOT_PASSWORD: rootpass
        MYSQL_USER: netflixusr
        MYSQL_PASSWORD: netflix123
        MYSQL_DATABASE: netflix

  # --------------
  # phpadmin
  # --------------
  phpadmin:
    image: phpmyadmin/phpmyadmin
    links:
      - mysql:db
    ports:
      - "8081:80"

  # --------------
  # rabbitmq
  # --------------
  rabbitmq:
    image: rabbitmq:3-management
    ports:
      - "15672:15672"
      - "5672"
      - "25672"
    hostname: rabbitmq
    environment:
        RABBITMQ_DEFAULT_USER: netflixusr
        RABBITMQ_DEFAULT_PASS: netflix123

  # --------------
  # registry
  # --------------
  discovery:
    image: 1tiquinho/startupone:discovery1.0
    ports:
      - "8761:8761"
    hostname: discovery
    environment:
        SPRING_PROFILES_ACTIVE: docker

  # --------------
  # zuul proxy
  # --------------
  zuul:
    image: 1tiquinho/startupone:zuul3.0
    ports:
      - "80:8080"
    hostname: zuul
    links:
      - discovery
    environment:
        SPRING_PROFILES_ACTIVE: docker

  # --------------
  # clientes
  # --------------
  clientes:
    image: 1tiquinho/startupone:clientes3.0
    ports:
      - "8080"
    hostname: clientes
    links:
      - discovery
      - rabbitmq
    environment:
        SPRING_PROFILES_ACTIVE: docker
        MYSQL_URL: "jdbc:mysql://mysql:3306/netflix"
        MYSQL_USER: netflixusr
        MYSQL_PASS: netflix123
        RABBITMQ_HOST: rabbitmq
        RABBITMQ_PORT: 5672
        RABBITMQ_USERNAME: netflixusr
        RABBITMQ_PASSWORD: netflix123

  # --------------
  # filmes
  # --------------
  filmes:
    image: 1tiquinho/startupone:filmes3.0
    ports:
      - "8080"
    hostname: filmes
    links:
      - discovery
      - rabbitmq
    environment:
        SPRING_PROFILES_ACTIVE: docker
        MYSQL_URL: "jdbc:mysql://mysql:3306/netflix"
        MYSQL_USER: netflixusr
        MYSQL_PASS: netflix123
        RABBITMQ_HOST: rabbitmq
        RABBITMQ_PORT: 5672
        RABBITMQ_USERNAME: netflixusr
        RABBITMQ_PASSWORD: netflix123

  # --------------
  # servicos
  # --------------
  servicos:
    image: 1tiquinho/startupone:servicos3.0
    ports:
      - "8080"
    hostname: servicos
    links:
      - discovery
      - rabbitmq
    environment:
        SPRING_PROFILES_ACTIVE: docker
        MYSQL_URL: "jdbc:mysql://mysql:3306/netflix"
        MYSQL_USER: netflixusr
        MYSQL_PASS: netflix123
        RABBITMQ_HOST: rabbitmq
        RABBITMQ_PORT: 5672
        RABBITMQ_USERNAME: netflixusr
        RABBITMQ_PASSWORD: netflix123
```
