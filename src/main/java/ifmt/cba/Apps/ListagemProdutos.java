package ifmt.cba.Apps;

import ifmt.cba.VO.ProdutoVO;
import ifmt.cba.VO.GrupoProdutoVO;
import ifmt.cba.VO.FornecedorVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.util.List;

public class ListagemProdutos {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Consulta JPQL ordenada por nome do produto
            String jpql = "SELECT p FROM ProdutoVO p ORDER BY p.nome";
            TypedQuery<ProdutoVO> query = em.createQuery(jpql, ProdutoVO.class);
            List<ProdutoVO> produtos = query.getResultList();

            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem produtos cadastrados!");
                return;
            }

            // Preparar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE PRODUTOS ===\n\n");

            for (ProdutoVO produto : produtos) {
                relatorio.append(String.format("Código: %d\n", produto.getCodigo()));
                relatorio.append(String.format("Nome: %s\n", produto.getNome()));
                relatorio.append(String.format("Preço de Venda: %.2f\n", produto.getPrecoVenda()));
                
                GrupoProdutoVO grupo = produto.getGrupo();
                if (grupo != null) {
                    relatorio.append(String.format("Grupo: %s\n", grupo.getNome()));
                } else {
                    relatorio.append("Grupo: Não associado\n");
                }

                List<FornecedorVO> fornecedores = produto.getFornecedores();
                if (fornecedores.isEmpty()) {
                    relatorio.append("Nenhum fornecedor associado\n");
                } else {
                    relatorio.append("Fornecedores:\n");
                    for (FornecedorVO fornecedor : fornecedores) {
                        relatorio.append(String.format("  - [%d] %s\n", 
                            fornecedor.getCodigo(), 
                            fornecedor.getRazaoSocial())); // Ajustar se a classe FornecedorVO tiver outro campo para razão social
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
            dialog.setTitle("Listagem de Produtos");
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao listar produtos: " + ex.getMessage());
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