package ifmt.cba.Apps;

import ifmt.cba.VO.PessoaJuridicaVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;

public class CadastroPessoaJuridica {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Get nome fantasia from user
            String nomeFantasia = JOptionPane.showInputDialog("Digite o nome fantasia");
            if (nomeFantasia == null || nomeFantasia.trim().isEmpty()) {
                System.out.println("Operação cancelada: nome fantasia não pode ser vazio");
                return;
            }

            // Get razão social
            String razaoSocial = JOptionPane.showInputDialog("Digite a razão social");
            if (razaoSocial == null || razaoSocial.trim().isEmpty()) {
                System.out.println("Operação cancelada: razão social não pode ser vazia");
                return;
            }

            // Create and populate the PessoaJuridicaVO
            PessoaJuridicaVO pessoaJuridica = new PessoaJuridicaVO();
            pessoaJuridica.setNomeFantasia(nomeFantasia);
            pessoaJuridica.setRazaoSocial(razaoSocial);

            // Persist the person
            em.getTransaction().begin();
            em.persist(pessoaJuridica);
            em.getTransaction().commit();

            JOptionPane.showMessageDialog(null, "Pessoa Jurídica cadastrada com sucesso!\n" +
                    "Código: " + pessoaJuridica.getCodigo() + "\n" +
                    "Nome Fantasia: " + pessoaJuridica.getNomeFantasia() + "\n" +
                    "Razão Social: " + pessoaJuridica.getRazaoSocial());

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar pessoa jurídica: " + ex.getMessage());
            System.out.println("Inclusão não realizada: " + ex.getMessage());
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