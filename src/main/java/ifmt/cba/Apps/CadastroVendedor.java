package ifmt.cba.Apps;

import ifmt.cba.VO.VendedorVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;

public class CadastroVendedor {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Coleta dados básicos
            String nome = JOptionPane.showInputDialog("Digite o nome do vendedor");
            if (nome == null || nome.trim().isEmpty()) {
                System.out.println("Operação cancelada: nome não pode ser vazio");
                return;
            }

            // Coleta RG
            String rg = JOptionPane.showInputDialog("Digite o RG do vendedor");
            if (rg == null || rg.trim().isEmpty()) {
                System.out.println("Operação cancelada: RG não pode ser vazio");
                return;
            }

            // Coleta CPF
            String cpf = JOptionPane.showInputDialog("Digite o CPF do vendedor");
            if (cpf == null || cpf.trim().isEmpty()) {
                System.out.println("Operação cancelada: CPF não pode ser vazio");
                return;
            }

            // Coleta Percentual de Comissão
            String perComissaoStr = JOptionPane.showInputDialog("Digite o percentual de comissão do vendedor");
            if (perComissaoStr == null || perComissaoStr.trim().isEmpty()) {
                System.out.println("Operação cancelada: percentual de comissão não pode ser vazio");
                return;
            }
            float perComissao = Float.parseFloat(perComissaoStr);

            // Valida o percentual de comissão
            if (perComissao < 0 || perComissao > 100) {
                JOptionPane.showMessageDialog(null, "Percentual de comissão deve estar entre 0 e 100");
                return;
            }

            // Cria e popula o VendedorVO
            VendedorVO vendedor = new VendedorVO();
            vendedor.setNome(nome);
            vendedor.setRg(rg);
            vendedor.setCpf(cpf);
            vendedor.setPerComissao(perComissao);

            // Persiste o vendedor
            em.getTransaction().begin();
            em.persist(vendedor);
            em.getTransaction().commit();

            JOptionPane.showMessageDialog(null, "Vendedor cadastrado com sucesso!\n" +
                    "Código: " + vendedor.getCodigo() + "\n" +
                    "Nome: " + vendedor.getNome() + "\n" +
                    "RG: " + vendedor.getRg() + "\n" +
                    "CPF: " + vendedor.getCpf() + "\n" +
                    "Percentual de Comissão: " + String.format("%.2f%%", vendedor.getPerComissao()));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Digite um valor numérico válido para o percentual de comissão");
            System.out.println("Erro de formato numérico: " + e.getMessage());
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar vendedor: " + ex.getMessage());
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