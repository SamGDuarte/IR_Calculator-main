# Aplicação Java Cliente/Servidor para Cálculo de Imposto de Renda
Este é um projeto de exemplo de uma aplicação cliente/servidor em Java que utiliza sockets e a biblioteca Swing para realizar o cálculo de imposto de renda. A aplicação permite que o cliente envie informações de renda e deduções para o servidor, que calcula o imposto de renda com base nessas informações e envia o resultado de volta para o cliente.

## Requisitos
- Java Development Kit (JDK) versão 8 ou superior instalado no servidor e nos clientes.
- Conhecimento básico sobre programação em Java e conceitos de sockets e Swing.

## Configuração do Ambiente
- Certifique-se de ter o JDK instalado corretamente no seu sistema.
- Baixe o código-fonte da aplicação ou clone o repositório.

## Executando o Servidor
- Navegue até o diretório do projeto no seu sistema.
- Abra um terminal ou prompt de comando.
- Compile o arquivo Server.java executando o seguinte comando:

```sh
javac Server.java
```
- Execute o servidor com o seguinte comando:
```sh
java Server
```
O servidor estará pronto para aceitar conexões dos clientes na porta especificada.

## Executando o Cliente
- Navegue até o diretório do projeto no seu sistema.
- Abra um terminal ou prompt de comando.
- Compile o arquivo Client.java executando o seguinte comando:

```sh 
javac Client.java
```
- Execute o cliente com o seguinte comando:
```sh
java Client
```

O cliente será iniciado e uma janela Swing será aberta, permitindo que você insira as informações necessárias para o cálculo do imposto de renda.

## Utilizando a Aplicação
- Após a execução do cliente, você verá uma janela Swing com campos para preenchimento das informações de renda e deduções.
- Preencha os campos corretamente com os valores desejados.
- Clique no botão "Calcular" para enviar as informações para o servidor e aguardar o resultado.
- O resultado do cálculo será exibido em um campo na janela do cliente.


# Considerações Finais
Este é apenas um exemplo básico de uma aplicação cliente/servidor em Java para cálculo de imposto de renda. É importante destacar que o código fornecido é apenas uma base e pode ser expandido e aprimorado conforme necessário para atender aos requisitos específicos do seu projeto.
