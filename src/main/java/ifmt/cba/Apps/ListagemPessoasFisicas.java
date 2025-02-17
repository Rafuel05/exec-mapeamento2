package ifmt.cba.Apps;

import ifmt.cba.VO.PessoaFisicaVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.util.List;

public class ListagemPessoasFisicas {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Consulta JPQL para obter pessoas físicas ordenadas por nome
            String jpql = "SELECT pf FROM PessoaFisicaVO pf ORDER BY pf.nome";
            TypedQuery<PessoaFisicaVO> query = em.createQuery(jpql, PessoaFisicaVO.class);
            List<PessoaFisicaVO> pessoasFisicas = query.getResultList();

            if (pessoasFisicas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem pessoas físicas cadastradas!");
                return;
            }

            // Preparar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE PESSOAS FÍSICAS ===\n\n");

            for (PessoaFisicaVO pessoa : pessoasFisicas) {
                relatorio.append(String.format("Código: %d\n", pessoa.getCodigo()));
                relatorio.append(String.format("Nome: %s\n", pessoa.getNome()));
                relatorio.append(String.format("RG: %s\n", pessoa.getRg() != null ? pessoa.getRg() : "Não informado"));
                relatorio.append(String.format("CPF: %s\n", pessoa.getCpf() != null ? pessoa.getCpf() : "Não informado"));
                relatorio.append("\n" + "=".repeat(30) + "\n\n");
            }

            // Criar janela de exibição com barra de rolagem
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Configurar e mostrar dialog
            JDialog dialog = new JDialog();
            dialog.setTitle("Listagem de Pessoas Físicas");
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao listar pessoas físicas: " + ex.getMessage());
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