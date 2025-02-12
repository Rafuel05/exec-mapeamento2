package ifmt.cba.Apps;

import ifmt.cba.VO.FornecedorVO;
import ifmt.cba.VO.ProdutoVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.util.List;

public class ListagemFornecedores {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Consulta JPQL ordenada por razão social
            String jpql = "SELECT f FROM FornecedorVO f ORDER BY f.razaoSocial";
            TypedQuery<FornecedorVO> query = em.createQuery(jpql, FornecedorVO.class);
            List<FornecedorVO> fornecedores = query.getResultList();

            if (fornecedores.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem fornecedores cadastrados!");
                return;
            }

            // Preparar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE FORNECEDORES ===\n\n");

            for (FornecedorVO fornecedor : fornecedores) {
                relatorio.append(String.format("Código: %d\n", fornecedor.getCodigo()));
                relatorio.append(String.format("Nome Fantasia: %s\n", fornecedor.getNomeFantasia()));
                relatorio.append(String.format("Razão Social: %s\n", fornecedor.getRazaoSocial()));
                
                List<ProdutoVO> produtos = fornecedor.getProdutos();
                if (produtos.isEmpty()) {
                    relatorio.append("Nenhum produto associado\n");
                } else {
                    relatorio.append("Produtos fornecidos:\n");
                    for (ProdutoVO produto : produtos) {
                        relatorio.append(String.format("  - [%d] %s\n", 
                            produto.getCodigo(), 
                            produto.getNome()));
                    }
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
            dialog.setTitle("Listagem de Fornecedores");
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao listar fornecedores: " + ex.getMessage());
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