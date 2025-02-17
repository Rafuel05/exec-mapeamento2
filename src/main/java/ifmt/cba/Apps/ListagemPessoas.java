package ifmt.cba.Apps;

import ifmt.cba.VO.PessoaVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.util.List;

public class ListagemPessoas {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Consulta JPQL para obter todas as pessoas ordenadas por nome
            String jpql = "SELECT p FROM PessoaVO p ORDER BY p.nome";
            TypedQuery<PessoaVO> query = em.createQuery(jpql, PessoaVO.class);
            List<PessoaVO> pessoas = query.getResultList();

            if (pessoas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem pessoas cadastradas!");
                return;
            }

            // Preparar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE PESSOAS ===\n\n");

            for (PessoaVO pessoa : pessoas) {
                relatorio.append(String.format("Código: %d\n", pessoa.getCodigo()));
                relatorio.append(String.format("Nome: %s\n", pessoa.getNome()));
                // Adiciona o tipo de pessoa (Física ou Jurídica)
                relatorio.append(String.format("Tipo: %s\n", 
                    pessoa.getClass().getSimpleName().replace("VO", "")));
                relatorio.append("\n" + "=".repeat(30) + "\n\n");
            }

            // Criar janela de exibição com barra de rolagem
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Configurar e mostrar dialog
            JDialog dialog = new JDialog();
            dialog.setTitle("Listagem de Pessoas");
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao listar pessoas: " + ex.getMessage());
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