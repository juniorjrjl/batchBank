# Batch Bank

projeto do curso alura de desafio de estudo de Spring batch

# Como funciona?

O projeto faz transferencia entre contas bancarias por meio de um arquivo csv com separador "|", o arquivo deve seguir o seguinte padrão:

8989_00254-9 onde os 4 primeiros dígitos é a agência bancária os 5 digitos depois do pipe são a conta e o dígito depois do traço, o arquivo deve ser enviado usando a seguinte request:

```
curl --location 'http://localhost:8080/batch-bank/accounts' \
--form 'file=@"/Users/junior/Desktop/8989_00254-9.csv"'
```


![technology Kotlin](https://img.shields.io/badge/techonolgy-Kotlin-orange)
![technology SpringBoot](https://img.shields.io/badge/techonolgy-SpringBoot-success)
![technology SpringBatch](https://img.shields.io/badge/techonolgy-SpringBatch-success)
![technology Gradle](https://img.shields.io/badge/techonolgy-Gradle-success)
![technology PostgreSQL](https://img.shields.io/badge/techonolgy-PostgreSQL-blue)

## Getting Started

## Pré-requisitos

- Docker
- Docker-compose

## Subir o banco de dados

rode o seguinte comando no terminal:

```
docker-compose up --build
```

a aplicação está configurada para escutar a porta 8080

