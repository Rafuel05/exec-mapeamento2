package ifmt.cba.Apps;

import ifmt.cba.VO.VendedorVO;
import ifmt.cba.VO.VendaVO;
import ifmt.cba.VO.ItemVendaVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListagemVendedores {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Consulta JPQL para obter vendedores ordenados por nome
            String jpql = "SELECT DISTINCT v FROM VendedorVO v ORDER BY v.nome";
            TypedQuery<VendedorVO> query = em.createQuery(jpql, VendedorVO.class);
            List<VendedorVO> vendedores = query.getResultList();

            if (vendedores.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem vendedores cadastrados!");
                return;
            }

            // Preparar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE VENDEDORES E COMISSÕES ===\n\n");
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for (VendedorVO vendedor : vendedores) {
                // Informações do vendedor
                relatorio.append(String.format("Código: %d\n", vendedor.getCodigo()));
                relatorio.append(String.format("Nome: %s\n", vendedor.getNome()));
                relatorio.append(String.format("Percentual de Comissão: %.1f%%\n", vendedor.getPerComissao()));
                
                // Buscar vendas a partir da tabela de vendas
                String jpqlVendas = "SELECT v FROM VendaVO v " +
                                  "JOIN FETCH v.itens " +
                                  "WHERE v.vendedor.codigo = :codigoVendedor " +
                                  "ORDER BY v.dataVenda";
                
                TypedQuery<VendaVO> queryVendas = em.createQuery(jpqlVendas, VendaVO.class);
                queryVendas.setParameter("codigoVendedor", vendedor.getCodigo());
                List<VendaVO> vendas = queryVendas.getResultList();

                if (vendas.isEmpty()) {
                    relatorio.append("\nNenhuma venda realizada\n");
                } else {
                    relatorio.append("\nVendas Realizadas:\n");
                    relatorio.append("----------------------------------------\n");
                    
                    float totalComissoes = 0;
                    
                    for (VendaVO venda : vendas) {
                        // Calcular valor total da venda
                        float valorTotalVenda = 0;
                        for (ItemVendaVO item : venda.getItens()) {
                            valorTotalVenda += item.getValorTotal();
                        }
                        
                        // Calcular comissão desta venda
                        float valorComissao = valorTotalVenda * (vendedor.getPerComissao() / 100);
                        totalComissoes += valorComissao;

                        relatorio.append(String.format("  Código da Venda: %d\n", venda.getCodigo()));
                        relatorio.append(String.format("  Data: %s\n", sdf.format(venda.getDataVenda())));
                        relatorio.append(String.format("  Valor Total da Venda: R$ %.2f\n", valorTotalVenda));
                        relatorio.append(String.format("  Valor da Comissão: R$ %.2f\n", valorComissao));
                        relatorio.append("  ----------------------------------------\n");
                    }
                    
                    relatorio.append(String.format("\nTotal de Comissões: R$ %.2f\n", totalComissoes));
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
            dialog.setTitle("Listagem de Vendedores e Comissões");
            dialog.add(scrollPane);
            dialog.setSize(800, 600);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao listar vendedores: " + ex.getMessage());
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