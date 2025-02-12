package ifmt.cba.Apps;

import ifmt.cba.VO.ProdutoVO;
import ifmt.cba.VO.GrupoProdutoVO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.swing.*;

public class CadastroProduto {

    public static void main(String[] args) {
        EntityManagerFactory emf = null;
        EntityManager em = null;

        try {
            emf = Persistence.createEntityManagerFactory("Testando");
            em = emf.createEntityManager();

            // Get product information from user
            String nome = JOptionPane.showInputDialog("Digite o nome do produto");
            if (nome == null || nome.trim().isEmpty()) {
                System.out.println("Operação cancelada: nome não pode ser vazio");
                return;
            }

            // Get price information
            String precoStr = JOptionPane.showInputDialog("Digite o preço de venda do produto");
            if (precoStr == null || precoStr.trim().isEmpty()) {
                System.out.println("Operação cancelada: preço não pode ser vazio");
                return;
            }
            Float precoVenda = Float.parseFloat(precoStr);

            // Get group code
            String grupoCodigoStr = JOptionPane.showInputDialog("Digite o código do grupo do produto");
            if (grupoCodigoStr == null || grupoCodigoStr.trim().isEmpty()) {
                System.out.println("Operação cancelada: código do grupo não pode ser vazio");
                return;
            }
            Long grupoCodigo = Long.parseLong(grupoCodigoStr);

            // Find the group
            GrupoProdutoVO grupo = em.find(GrupoProdutoVO.class, grupoCodigo);
            if (grupo == null) {
                JOptionPane.showMessageDialog(null, "Grupo não encontrado com o código: " + grupoCodigo);
                return;
            }

            // Create and populate the ProdutoVO
            ProdutoVO produto = new ProdutoVO();
            produto.setNome(nome);
            produto.setPrecoVenda(precoVenda);
            produto.setGrupo(grupo);

            // Persist the product
            em.getTransaction().begin();
            em.persist(produto);
            em.getTransaction().commit();

            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!\n" +
                    "Código: " + produto.getCodigo() + "\n" +
                    "Nome: " + produto.getNome() + "\n" +
                    "Preço: R$ " + String.format("%.2f", produto.getPrecoVenda()) + "\n" +
                    "Grupo: " + produto.getGrupo().getNome());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Digite um valor numérico válido");
            System.out.println("Erro de formato numérico: " + e.getMessage());
        } catch (Exception ex) {
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto: " + ex.getMessage());
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