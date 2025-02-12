package ifmt.cba.Apps;

import ifmt.cba.VO.PessoaFisicaVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;

public class CadastroPessoaFisica {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Get person name from user
            String nome = JOptionPane.showInputDialog("Digite o nome da pessoa física");
            if (nome == null || nome.trim().isEmpty()) {
                System.out.println("Operação cancelada: nome não pode ser vazio");
                return;
            }

            // Get RG information
            String rg = JOptionPane.showInputDialog("Digite o RG");
            if (rg == null || rg.trim().isEmpty()) {
                System.out.println("Operação cancelada: RG não pode ser vazio");
                return;
            }

            // Get CPF information
            String cpf = JOptionPane.showInputDialog("Digite o CPF");
            if (cpf == null || cpf.trim().isEmpty()) {
                System.out.println("Operação cancelada: CPF não pode ser vazio");
                return;
            }

            // Create and populate the PessoaFisicaVO
            PessoaFisicaVO pessoaFisica = new PessoaFisicaVO();
            pessoaFisica.setNome(nome);
            pessoaFisica.setRg(rg);
            pessoaFisica.setCpf(cpf);

            // Persist the person
            em.getTransaction().begin();
            em.persist(pessoaFisica);
            em.getTransaction().commit();

            JOptionPane.showMessageDialog(null, "Pessoa Física cadastrada com sucesso!\n" +
                    "Código: " + pessoaFisica.getCodigo() + "\n" +
                    "Nome: " + pessoaFisica.getNome() + "\n" +
                    "RG: " + pessoaFisica.getRg() + "\n" +
                    "CPF: " + pessoaFisica.getCpf());

        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar pessoa física: " + ex.getMessage());
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