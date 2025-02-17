package ifmt.cba.Apps;

import ifmt.cba.VO.PessoaJuridicaVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import javax.swing.*;
import java.util.List;

public class ListagemPessoasJuridicas {
    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Consulta JPQL para obter pessoas jurídicas ordenadas por nome fantasia (nome)
            String jpql = "SELECT pj FROM PessoaJuridicaVO pj ORDER BY pj.nome";
            TypedQuery<PessoaJuridicaVO> query = em.createQuery(jpql, PessoaJuridicaVO.class);
            List<PessoaJuridicaVO> pessoasJuridicas = query.getResultList();

            if (pessoasJuridicas.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Não existem pessoas jurídicas cadastradas!");
                return;
            }

            // Preparar relatório
            StringBuilder relatorio = new StringBuilder();
            relatorio.append("=== RELATÓRIO DE PESSOAS JURÍDICAS ===\n\n");

            for (PessoaJuridicaVO pessoa : pessoasJuridicas) {
                relatorio.append(String.format("Código: %d\n", pessoa.getCodigo()));
                relatorio.append(String.format("Nome Fantasia: %s\n", pessoa.getNomeFantasia()));
                relatorio.append(String.format("Razão Social: %s\n", pessoa.getRazaoSocial()));
                relatorio.append("\n" + "=".repeat(30) + "\n\n");
            }

            // Criar janela de exibição com barra de rolagem
            JTextArea textArea = new JTextArea(relatorio.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            // Configurar e mostrar dialog
            JDialog dialog = new JDialog();
            dialog.setTitle("Listagem de Pessoas Jurídicas");
            dialog.add(scrollPane);
            dialog.setSize(600, 400);
            dialog.setLocationRelativeTo(null);
            dialog.setModal(true);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao listar pessoas jurídicas: " + ex.getMessage());
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