package ifmt.cba.Apps;

import ifmt.cba.VO.VendaVO;
import ifmt.cba.VO.ItemVendaVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListagemVendas {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Consulta JPQL para obter vendas ordenadas por data
            String jpql = "SELECT DISTINCT v FROM VendaVO v LEFT JOIN FETCH v.itens ORDER BY v.dataVenda";
            TypedQuery<VendaVO> query = em.createQuery(jpql, VendaVO.class);
            List<VendaVO> vendas = query.getResultList();

            if (vendas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem vendas cadastradas!");
                return;
            }

            // Preparar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE VENDAS ===\n\n");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for (VendaVO venda : vendas) {
                // Informações da venda
                relatorio.append(String.format("Código da Venda: %d\n", venda.getCodigo()));
                relatorio.append(String.format("Data da Venda: %s\n", sdf.format(venda.getDataVenda())));
                
                // Calcular valor total da venda
                float valorTotalVenda = 0;
                for (ItemVendaVO item : venda.getItens()) {
                    valorTotalVenda += item.getValorTotal();
                }
                relatorio.append(String.format("Valor Total da Venda: R$ %.2f\n", valorTotalVenda));
                
                // Listar itens da venda
                relatorio.append("\nItens da Venda:\n");
                relatorio.append("----------------------------------------\n");
                for (ItemVendaVO item : venda.getItens()) {
                    relatorio.append(String.format("  Código do Item: %d\n", item.getCodigo()));
                    relatorio.append(String.format("  Produto: %s\n", item.getProduto().getNome()));
                    relatorio.append(String.format("  Quantidade: %d\n", item.getQuantidade()));
                    relatorio.append(String.format("  Preço de Venda: R$ %.2f\n", item.getPrecoVenda()));
                    relatorio.append(String.format("  Percentual de Desconto: %.1f%%\n", item.getPerDesconto()));
                    relatorio.append(String.format("  Valor Total do Item: R$ %.2f\n", item.getValorTotal()));
                    relatorio.append("  ----------------------------------------\n");
                }
                
                relatorio.append("\n" + "=".repeat(50) + "\n\n");
            }

            // Criar janela de exibição com barra de rolagem
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Configurar e mostrar dialog
            JDialog dialog = new JDialog();
            dialog.setTitle("Listagem de Vendas");
            dialog.add(scrollPane);
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao listar vendas: " + ex.getMessage());
            ex.printStackTrace();
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