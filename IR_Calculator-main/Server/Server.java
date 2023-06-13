import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DecimalFormat;

public class Server {
    private static final int SERVER_PORT = 12345;
    private static final String USUARIO_CORRETO = "admin";
    private static final String SENHA_CORRETA = "12345";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Servidor aguardando conexões...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String usuario = in.readLine();
                String senha = in.readLine();

                boolean autenticado = validarCredenciais(usuario, senha, USUARIO_CORRETO, SENHA_CORRETA);

                out.println(autenticado);

                if (autenticado) {
                    String nomeCliente = in.readLine(); // Recebe o nome do cliente

                    double[] informacoesCliente = lerInformacoesCliente(in);
                    double rendaBrutaAnual = informacoesCliente[0];
                    int idade = (int) informacoesCliente[1];
                    double despesasEnsinoAnual = informacoesCliente[2];
                    double despesasMedicasAnual = informacoesCliente[3];
                    double despesasPensaoAlimenticiaAnual = informacoesCliente[4];
                    double previdenciaSocialAnual = informacoesCliente[5];
                    int dependentes = (int) informacoesCliente[6];

                    double impostoDeRenda = calcularImpostoDeRenda(rendaBrutaAnual, idade, despesasEnsinoAnual,
                            despesasMedicasAnual, despesasPensaoAlimenticiaAnual, previdenciaSocialAnual, dependentes);

                    String impostoFormatado = formatarValorDecimal(impostoDeRenda);

                    out.println(nomeCliente + ": " + impostoFormatado); // Envia o nome do cliente e o imposto formatado para o cliente
                }

                in.close();
                out.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean validarCredenciais(String usuario, String senha, String usuarioCorreto, String senhaCorreta) {
        return usuario.equals(usuarioCorreto) && senha.equals(senhaCorreta);
    }

    private static double[] lerInformacoesCliente(BufferedReader in) throws IOException {
        double[] informacoesCliente = new double[7];
        informacoesCliente[0] = Double.parseDouble(in.readLine()); // Renda Bruta Anual
        informacoesCliente[1] = Integer.parseInt(in.readLine()); // Idade
        informacoesCliente[2] = Double.parseDouble(in.readLine()); // Despesas Ensino Anual
        informacoesCliente[3] = Double.parseDouble(in.readLine()); // Despesas Médicas Anual
        informacoesCliente[4] = Double.parseDouble(in.readLine()); // Despesas Pensão Alimentícia Anual
        informacoesCliente[5] = Double.parseDouble(in.readLine()); // Previdência Social Anual
        informacoesCliente[6] = Integer.parseInt(in.readLine()); // Dependentes
        return informacoesCliente;
    }

    private static double calcularImpostoDeRenda(double rendaBrutaAnual, int idade, double despesasEnsinoAnual,
                                                 double despesasMedicasAnual, double despesasPensaoAlimenticiaAnual,
                                                 double previdenciaSocialAnual, int dependentes) {
        double baseCalculo = rendaBrutaAnual - despesasEnsinoAnual - despesasMedicasAnual -
                despesasPensaoAlimenticiaAnual - previdenciaSocialAnual - (dependentes * 2293.08);

        double impostoDeRenda;

        if (idade < 65) {
            if (baseCalculo <= 22847.76) {
                impostoDeRenda = 0.0;
            } else if (baseCalculo <= 33919.80) {
                impostoDeRenda = (baseCalculo * 0.075) - 1713.58;
            } else if (baseCalculo <= 45012.60) {
                impostoDeRenda = (baseCalculo * 0.15) - 4257.57;
            } else if (baseCalculo <= 55976.16) {
                impostoDeRenda = (baseCalculo * 0.225) - 7633.51;
            } else {
                impostoDeRenda = (baseCalculo * 0.275) - 10432.32;
            }
        } else {
            if (baseCalculo <= 24322.80) {
                impostoDeRenda = 0.0;
            } else if (baseCalculo <= 35832.56) {
                impostoDeRenda = (baseCalculo * 0.075) - 1941.58;
            } else if (baseCalculo <= 45243.84) {
                impostoDeRenda = (baseCalculo * 0.15) - 4912.51;
            } else if (baseCalculo <= 55627.20) {
                impostoDeRenda = (baseCalculo * 0.225) - 8689.36;
            } else {
                impostoDeRenda = (baseCalculo * 0.275) - 11908.56;
            }
        }
        
        return impostoDeRenda;
    }

    private static String formatarValorDecimal(double valor) {
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        return decimalFormat.format(valor);
    }
}
