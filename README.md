## Projeto Java Elite

### Requisitos
##### Java 21
##### Docker
##### Makefile (opcional)

### Como rodar

#### Clona o repositório
```bash
git clone https://github.com/Paulooo0/java-elite-proj
```
#### Executando os comandos

Para facilitar o uso dos comandos, é utilizado `Makefile` (vem por padrão no Linux). Mas se você não possuir, pode simplesmente copiar os comandos em <a href="https://github.com/Paulooo0/java-elite-proj/blob/main/Makefile">Makefile</a>, e colar no seu terminal.

Comando para rodar conteiner de banco de dados + rodar a aplicação java:
```bash
make run
```

Para parar a aplicação, use `Ctrl+c`

Outros comandos:
```bash
make stop-db # Pausa o banco de dados

make down-db # Remove o conteiner de banco de dados (não remove os dados em si)

make up-db   # Roda apenas o conteiner de banco de dados

make run-app # Roda apenas a aplicação Java
```


### Endpoints

#### Livros
- Cria livro
  - `POST` `http://localhost:8080/livros`

  - Body (exemplo):
    ```json
    {
        "isbn": "0000000000001",
        "title": "testTitle",
        "author": "testAuthor"
    }
    ```

- Busca livro por título
  - `GET` `http://localhost:8080/livros?title={bookTitle}`

- Atualiza livro
  - `PUT` `http://localhost:8080/livros/{id}`

  - Body (exemplo):
    ```json
    {
        "author": "testAuthor2"
    }
    ```

- Atualiza estoque do livro
  - `PUT` `http://localhost:8080/livros/estoque/{id}?quantity={quantity}`
  
    OBS: {quantity} recebe um Integer

- Realiza deleção lógica do livro
  - `DELETE` `http://localhost:8080/livros/{id}`

#### Usuarios
- Cria usuário
  - `POST` `http://localhost:8080/usuarios`

  - Body (exemplo):
    ```json
    {
        "name": "test",
        "email": "test@test.com"
    }
    ```

#### Emprestimos
- Cria empréstimo
  - `POST` `http://localhost:8080/emprestimos`

  - Body (exemplo):
    ```json
    {
        "userId": "{userId}",
        "bookId": "{bookId}"
    }
    ```

- Lista empréstimos por id do usuário
  - `GET` `http://localhost:8080/emprestimos?userId={userId}`

- Retorna o valor atual do empréstimo
  - `GET` `http://localhost:8080/emprestimos/multa?loanId={loanId}`

- Lista todos os empréstimos atualmente ativos
  - `GET` `http://localhost:8080/emprestimos/ativos`

- Realiza devolução do empréstimo
  - `PUT` `http://localhost:8080/emprestimos/{id}`
