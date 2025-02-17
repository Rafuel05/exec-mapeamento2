package ifmt.cba.Apps;

import ifmt.cba.VO.ClienteVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javax.swing.*;
import java.util.List;

public class ListagemClientes {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Consulta JPQL para obter clientes com contagem de vendas
            String jpql = "SELECT c, COUNT(v) " +
                         "FROM ClienteVO c " +
                         "LEFT JOIN VendaVO v ON v.cliente = c " +
                         "GROUP BY c " +
                         "ORDER BY c.nome";

            List<Object[]> resultado = em.createQuery(jpql).getResultList();

            if (resultado.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem clientes cadastrados!");
                return;
            }

            // Preparar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE CLIENTES ===\n\n");

            for (Object[] linha : resultado) {
                ClienteVO cliente = (ClienteVO) linha[0];
                Long qtdVendas = (Long) linha[1];

                relatorio.append(String.format("Código: %d\n", cliente.getCodigo()));
                relatorio.append(String.format("Nome: %s\n", cliente.getNome()));
                relatorio.append(String.format("Limite de Crédito: R$ %.2f\n", cliente.getLimiteCredito()));
                relatorio.append(String.format("Quantidade de Vendas: %d\n", qtdVendas));
                relatorio.append("\n" + "=".repeat(50) + "\n\n");

                // Debug - imprimir no console
                System.out.println("Debug - Cliente: " + cliente.getNome());
                System.out.println("Debug - Código: " + cliente.getCodigo());
                System.out.println("Debug - Quantidade de Vendas: " + qtdVendas);
                System.out.println("------------------------");
            }

            // Criar janela de exibição com barra de rolagem
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Configurar e mostrar dialog
            JDialog dialog = new JDialog();
            dialog.setTitle("Listagem de Clientes");
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception ex) {
            System.out.println("Erro detalhado:");
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Erro ao listar clientes: " + ex.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
            if (emf != null && emf.isOpen()) {
                emf.close();
            }
        }
    }
}